# memcached 的线程模型

主线程+ worker 线程池的， 单进程多线程模型，事件机制采用libEvent




# 工作流程
 
memcached是多线程，非阻塞IO复用的网络模型，分为监听主线程和worker子线程，监听线程监听网络连接，接受请求后，将连接描述字pipe传递给worker线程，进行读写IO，网络层使用libevent封装的事件库，多线程模型可以发挥多核作用。

# 缺陷

1. memcached不支持内存数据的持久化操作，所有的数据都以in-memory的形式存储。

2. 支持的数据类型比较少

3. 也不支持备份



# 内存管理方式

基于内存的数据库，对内存管理等操作要进行一个精打细算， 采用类似于linux 管理内存碎片的方法slab机制

# 数据一致性

Memcached提供了cas(check and set)命令和锁机制，可以保证多个并发访问操作同一份数据的一致性问题。并且memcache 支持加锁操作，但是redis 当中是没有锁操作的，提供了事务这个概念， 保证每条命令都是原子操作的


**什么是缓存当中的并发操作**
>多个Memcached client并发set同一个key的场景


[(转)Memcached多线程模型 - 简书](https://www.jianshu.com/p/98a02d771b2d)

[Memcached线程模型分析 - KangRoger的专栏 - CSDN博客](https://blog.csdn.net/kangroger/article/details/48104055)