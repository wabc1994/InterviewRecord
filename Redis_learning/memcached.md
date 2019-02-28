# memcached 的线程模型

主线程+ worker 线程池的， 单进程多线程模型，事件机制采用libEvent




# 工作流程
 
memcached是多线程，非阻塞IO复用的网络模型，分为监听主线程和worker子线程，监听线程监听网络连接，接受请求后，将连接描述字pipe传递给worker线程，进行读写IO，网络层使用libevent封装的事件库，多线程模型可以发挥多核作用。

# 缺陷

1. memcached不支持内存数据的持久化操作，所有的数据都以in-memory的形式存储。

2. 支持的数据类型比较少,memcache只提供了一种数据类型：key->value。key是字符串，value也必须是字符串。

3. 也不支持备份，



# 内存管理方式


**lru**
基于内存的数据库，对内存管理等操作要进行一个精打细算， 采用类似于linux 管理内存碎片的方法slab机制


**刚开始**

封装malloc free等函数，频繁调用造成系统开销

**后来**

memcache借鉴了linux操作系统中的slab管理器的模式，使用slab方式来管理从操作系统申请到的内存。

slab类似于内存池的实现，Item根据大小选择合适的chunk存储，内存池的方式可以省去申请/释放内存的开销，并且能减小内存碎片产生


slab 从操作系统处以页(1M,4kb)为单位批发内存后，然后再把每个page按照chunk_size 切分成为若干个chunk,再以chunk 为单位进行, 

[](https://github.com/wabc1994/InterviewRecord/blob/master/Redis_learning/picture/memcached_slab技术.png)
**缺陷**

容易造成空间浪费，如果没有找到合适大小的chunk 来存储新的key 和value

![](https://github.com/wabc1994/InterviewRecord/blob/master/Redis_learning/picture/memcached内存管理技术.png)

[从源码角度理清memcache缓存服务 ](http://www.cnblogs.com/wangtao_20/p/4839573.html)

# 数据一致性

Memcached提供了cas(check and set)命令和锁机制，可以保证多个并发访问操作同一份数据的一致性问题。并且memcache 支持加锁操作，但是redis 当中是没有锁操作的，提供了事务这个概念， 保证每条命令都是原子操作的


**什么是缓存当中的并发操作**
>多个Memcached client并发set同一个key的场景


[(转)Memcached多线程模型](https://www.jianshu.com/p/98a02d771b2d)

[Memcached线程模型分析 ](https://blog.csdn.net/kangroger/article/details/48104055)