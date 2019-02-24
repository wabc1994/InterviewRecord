# Redis.conf
在配置文件当中默认采取的是rdf的持久化机制
```conf
save 900 1 # 15分钟内至少有一个键被更改 
save 300 10 # 5分钟内至少有10个键被更改
save 60 10000 # 1分钟内至少有10000个键被更改
```
上述三个条件是或的基本关系， 只要上面三个条件当中的某两个成立即可

当然也可以是手动进行在中观输入上述命令行

# bgsave和save命令
1. save 是主进程完成持久化的工作，期间服务器是不能处理客户端的请求， 阻塞Redis主进程，直到保存完成为止。在主进程阻塞期间，

服务器不能处理客户端的任何请求。rdb.c当中的rdbsave()函数
2. bgsave 是主进程fork 一个子进程来完成持久化的工作， 当孩子进程完成持久化的工作后， 就会像主进程汇报。rdbSaveBackground()函数当中利用了fork, 然后子进程还是调用的rdbsave() 函数，在此期间， 父进程(即Redis主进程)则继续处理客户端的请求。

## bgsave执行流程

bgsave 主要是rdbsavebackgroud(),rdbsavebackgroud() fork一个子进程，子进程调用函数rdbsave
![redis基本执行流程](https://github.com/wabc1994/InterviewRecord/blob/master/Redis_learning/picture/Screen%20Shot%202018-12-28%20at%208.55.23%20PM.png)

1. Redis父进程首先判断：当前是否在执行save，或bgsave/bgrewriteaof（后面会详细介绍该命令）的子进程，如果在执行则bgsave命令直接返回。bgsave/bgrewriteaof 的子进程不能同时执行，主要是基于性能方面的考虑：两个并发的子进程同时执行大量的磁盘写操作，可能引起严重的性能问题。
2. 父进程执行fork操作创建子进程，这个过程中父进程是阻塞的，Redis不能执行来自客户端的任何命令
3. 父进程fork后，bgsave命令返回”Background saving started”信息并不再阻塞父进程，并可以响应其他命令
4. 子进程创建RDB文件，根据父进程内存快照生成临时快照文件，完成后对原有文件进行原子替换
5. 子进程发送信号给父进程表示完成，父进程更新统计信息


关于这两个命令， 由于save()会阻塞服务器， 导致不能对外提供服务，所以在生产环境当中是很少使用这种命令的，基本都是使用bgsave， 并且在主从复制过程当中master也是通过bgsave 来完成主从复制的

>The two commands of SAVE and BGSAVE call rdbSave functions, but they call each other differently. SAVE calls rdbSave directly, blocking Redis main process until storage is completed. During the main process blocking, the server can't handle any requests from the client. BGSAVE then fork a sub process, the child process is responsible for calling the rdbSave, and after the save is completed, the main process is sent to the main process to notify the save has been completed. The Redis server can still continue to handle the client's request during the BGSAVE execution. Use commands to persist save storage:./redis-cli -h IP -p port save./redis-cli -h IP -p port

SAVE 和 BGSAVE 两个命令都会调用 rdbSave 函数，但它们调用的方式各有不同：

SAVE 直接调用 rdbSave ，阻塞 Redis 主进程，直到保存完成为止。在主进程阻塞期间，

服务器不能处理客户端的任何请求。

BGSAVE 则 fork 出一个子进程，子进程负责调用 rdbSave ，并在保存完成之后向主进程发送信号，通知保存已完成。 Redis 服务器在BGSAVE 执行期间仍然可以继续处理客户端的请求。


子进程将数据集写入到一个临时 RDB 文件中。
当子进程完成对新 RDB 文件的写入时，Redis 用新 RDB 文件替换原来的 RDB 文件，并删除旧的 RDB 文件。

# 过程保存rdbsave()
遍历服务器下面的每个数据库，然后针对每个写操作当中涉及的，在这里面Redis利用操作系统的写时复制技术，只有经过改变的数据才需要重新写， 


**写时复制技术**
```c
//循环服务端下的所有数据库

    for (j = 0; j < server.dbnum; j++) {
    	//获取单个数据库
        redisDb *db = server.db+j;
        dict *d = db->dict;
        // 如果为空的话基本情况局势
        if (dictSize(d) == 0) continue;
        // 或者一个迭代器类型
        di = dictGetSafeIterator(d);
        if (!di) {
            fclose(fp);
            return REDIS_ERR;
        }
        /* Write the SELECT DB opcode */
        if (rdbSaveType(&rdb,REDIS_RDB_OPCODE_SELECTDB) == -1) goto werr;
        if (rdbSaveLen(&rdb,j) == -1) goto werr;

        /* Iterate this DB writing every entry */
        //为何采取这种代码书写方式
        while((de = dictNext(di)) != NULL) {
            sds keystr = dictGetKey(de);
            robj key, *o = dictGetVal(de);
            long long expire;

            initStaticStringObject(key,keystr);
            expire = getExpire(db,&key);
            //将里面的键值存入rdb中
            if (rdbSaveKeyValuePair(&rdb,&key,o,expire,now) == -1) goto werr;
        }
        dictReleaseIterator(di);
    }
    di = NULL; /* So that we don't release it again on error. */

    /* EOF opcode */
    if (rdbSaveType(&rdb,REDIS_RDB_OPCODE_EOF) == -1) goto werr;

    /* CRC64 checksum. It will be zero if checksum computation is disabled, the
     * loading code skips the check in this case. */
    cksum = rdb.cksum;
    memrev64ifbe(&cksum);
    if (rioWrite(&rdb,&cksum,8) == 0) goto werr;

    /* Make sure data will not remain on the OS's output buffers */
    if (fflush(fp) == EOF) goto werr;
    if (fsync(fileno(fp)) == -1) goto werr;
    if (fclose(fp) == EOF) goto werr;

    /* Use RENAME to make sure the DB file is changed atomically only
     * if the generate DB file is ok. */
    if (rename(tmpfile,filename) == -1) {
        redisLog(REDIS_WARNING,"Error moving temp DB file on the final destination: %s", strerror(errno));
        unlink(tmpfile);
        return REDIS_ERR;
    }
    redisLog(REDIS_NOTICE,"DB saved on disk");
    server.dirty = 0;
    server.lastsave = time(NULL);
    server.lastbgsave_status = REDIS_OK;
    return REDIS_OK;
```


## rdbsave() 是如何保持持久化一个entry, <key,value>机制

rdb 快照的本质就是保存数据库当中的key,value 键值对， rdbsave() 调用rdbsavekeyavluepair(

```c
/* Save a key-value pair, with expire time, type, key, value.
 * On error -1 is returned.
 * On success if the key was actually saved 1 is returned, otherwise 0
 * is returned (the key was already expired). */
int rdbSaveKeyValuePair(rio *rdb, robj *key, robj *val,
                        long long expiretime, long long now)
{
    /* Save the expire time */


    if (expiretime != -1) {
        // 如果过期时间少于当前时间，那么表示该key已经失效，返回不做任何保存；
        /* If this key is already expired skip it */
        if (expiretime < now) return 0;
        // 如果当前遍历的entry有失效时间属性，那么保存REDIS_RDB_OPCODE_EXPIRETIME_MS即252，即"FC"以及失效时间到rdb文件中，
        if (rdbSaveType(rdb,REDIS_RDB_OPCODE_EXPIRETIME_MS) == -1) return -1;
        if (rdbSaveMillisecondTime(rdb,expiretime) == -1) return -1;
    }

    // 接下来保存redis key的类型，key，以及value到rdb文件中；
    /* Save type, key, value */


    if (rdbSaveObjectType(rdb,val) == -1) return -1;
    if (rdbSaveStringObject(rdb,key) == -1) return -1;
    if (rdbSaveObject(rdb,val) == -1) return -1;
    return 1;
}

```
# rdbload()
Redis的main()会调用loadDataFromDisk()，而loadDataFromDisk()会调用rdbLoad，调用方式如下：

RDB文件的载入工作是在服务器启动时自动执行的，并没有专门的命令。但是由于AOF的优先级更高，因此当AOF开启时，Redis会优先载入AOF文件来恢复数据；只有当AOF关闭时，才会在Redis服务器启动时检测RDB文件，并自动载入。服务器载入RDB文件期间处于阻塞状态，直到载入完成为止。

# 参考链接

[rdb源码](https://cgiirw.github.io/2018/09/13/Redis_RDB/)

[rdb的执行流程](http://www.cnblogs.com/kismetv/kismetv/p/9137897.html)