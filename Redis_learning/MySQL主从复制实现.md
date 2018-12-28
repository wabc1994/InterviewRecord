# MySQL主从复制实现



## 配置

### 主服务器

log-bin=mysql-bin        #开启二进制日志(实现主从复制一定要开启二进制文件)

### 主从服务器日志文件
1. master-log
2. relay-log
可以参考的连接

### 从服务器

在从服务器当中开启start slave 命令，然后就读取 master.info 与主服务器建立一个连接，然后从服务器这边就建立了一个I/0 线程

### 

## 线程

1. 主服务器当中 IO线程把用户客户端对数据库的操作记录下来(update, insert, delete等对数据进行)，

2. Slave_IO线程负责把主库的bin日志(Master_Log)内容， 抄写到从库当中的中继日志上面(Relay_Log)

3. Slave_SQL线程主要负责把中继日志上的语句在从数据库上面进行更新操作，实现主从数据同步

## 实现

MySQL同步功能由3个线程(master上1个，slave上2个)来实现。从库执行 START SLAVE 语句后，slave就创建一个I/O线程。I/O线程连接到master上，并请求master发送二进制日志中的语句。master创建一个线程来把日志的内容发送到slave上。这个线程在master上执行 SHOW PROCESSLIST 语句后的结果中的 Binlog Dump 线程便是。slave上的I/O线程读取master的 Binlog Dump 线程发送的语句，并且把它们拷贝到其数据目录下的中继日志(relay logs)中。第三个是SQL线程，salve用它来读取中继日志，然后执行它们来更新数据

这个过程也可以总结如下面的情况

![](https://github.com/wabc1994/InterviewRecord/blob/master/Redis_learning/picture/mysql.png)



## 命令行

查看状态 ，在从服务器当中执行下面的命令可以

show slave status\G

主要监控主从复制的状态

## 故障

主要是查看从服务器上面的两个线程是否正常运行

1. Slave_IO线程相对比较简单，一般不容易出错，如果Slave_IO_Running显示为No，多为网络连接不上，权限不够等环境问题。
2. Slave_SQL 线程出错的话主要是主从两个数据库上面执行的SQL语句导致两个数据库当中的数据不一致

上面2当中的问题也可以总结为主从不一致

可以参考下面两个链接

[](https://blog.csdn.net/chagaostu/article/details/47685329)

## 主从复制的优势

1. 实现读写分离，实现高可用，主数据库复制写入，从数据库复制读(在从服务器可以执行查询工作(即我们常说的读功能)，降低主服务器压力；)

2. 在从主服务器进行备份，避免备份期间影响主服务器服务；

3. 当主服务器出现问题时，可以切换到从服务器。

4. 发扬不同表引擎的优点。目前Myisam表的查询速度比innodb略快，而写入并发innodb比myIsam要好。那么，我们可以使用innodb作为 master，处理高并发写入，使用master作为slave，接受查询。或在myisam slave中建立全文索引，解决innodb无全文索引的弱点。
