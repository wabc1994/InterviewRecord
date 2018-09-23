
# 传统MySQL 和Memcache
传统MySQL+ Memcached架构遇到的问题
　　实际MySQL是适合进行==海量数据存储的==，通过Memcached将==热点数据==加载到cache，加速访问，很多公司都曾经使用过这样的架构，但随着==业务数据量的不断增加，和访问量的持续增长==，我们遇到了很多问题：
1. MySQL需要不断进行==拆库拆表==，Memcached也需不断跟着扩容，==扩容和维护==工作占据大量开发时间。
2.  Memcached与MySQL数据库数据一致性问题。
3.  Memcached数据命中率低或down机，大量访问直接穿透到DB，MySQL无法支撑。
4.  跨机房cache同步问题。
5. memcache但是不支==持数据的持久化==，服务器关闭之后数据全部丢失
6. Redis支持数据的持久化（RDB快照和AOF日志），可以将==内存中的数据保持在磁盘中，重启的时候可以再次加载进行使用==


# NoSQL的出现

最近几年，业界不断涌现出很多各种各样的NoSQL产品，那么如何才能正确地使用好这些产品，最大化地发挥其长处，是我们需要深入研究和思考的问题，实际归根结底最重要的是了解这些产品的定位，并且了解到每款产品的tradeoffs，在实际应用中做到扬长避短，总体上这些NoSQL主要用于解决以下几种问题：
1. 少量数据存储，高速读写访问。此类产品通过数据全部in-momery 的方式来保证高速访问，同时提供数据落地的功能，实际这正是Redis最主要的适用场景。
2. 海量数据存储，分布式系统支持，数据一致性保证，方便的集群节点添加/删除。
3. 这方面最具代表性的是dynamo和bigtable 2篇论文所阐述的思路。前者是一个完全无中心的设计，节点之间通过gossip方式传递集群信息，数据保证最终一致性，后者是一个中心化的方案设计，通过类似一个分布式锁服务来保证强一致性,数据写入先写内存和redo log，然后定期compat归并到磁盘上，将随机写优化为顺序写，提高写入性能。
4. Schema free，auto-sharding等。比如目前常见的一些==文档数据库==都是支持schema-free的，直接存储json格式数据，并且支持auto-sharding等功能，比如MongoDB。

## 基本区别
1 、Redis不仅仅支持简单的k/v类型的数据，同时还提供list，set，zset，hash等数据结构的存储。memcache支持简单的数据类型，String。
2 、Redis支持数据的备份，即master-slave模式的数据备份。
3 、Redis支持数据的持久化，可以将内存中的数据保持在磁盘中，重启的时候可以再次加载进行使用,而Memecache把数据全部存在内存之中
4、 redis的速度比memcached快很多
5、Memcached是多线程，非阻塞IO复用的网络模型；Redis使用单线程的IO复用模型。

## 背后的本质区别
### 存储方式的区别
**Redis中，并不是所有的数据都一直存储在内存中的。这是和Memcached相比一个最大的区别。Redis只会缓存所有的 key的信息，如果Redis发现内存的使用量超过了某一个阀值，将触发swap的操作，Redis根据“swappability = age*log(size_in_memory)”计 算出哪些key对应的value需要swap到磁盘。然后再将这些key对应的value持久化到磁盘中，同时在内存中清除。这种特性使得Redis可以 保持超过其机器本身内存大小的数据。**
当然，机器本身的内存必须要能够保持所有的key，毕竟这些数据是不会进行swap操作的。同时由于Redis将内存 中的数据swap到磁盘中的时候，==提供服务的主线程==和==进行swap操作==的子线程会共享这部分内存，所以如果更新需要swap的数据，Redis将阻塞这个 操作，直到子线程完成swap操作后才可以进行修改。使用Redis特有内存模型前后的情况对比：
VM off: 300k keys, 4096 bytes values: 1.3G used
VM on: 300k keys, 4096 bytes values: 73M used
VM off: 1 million keys, 256 bytes values: 430.12M used
VM on: 1 million keys, 256 bytes values: 160.09M used
VM on: 1 million keys, values as large as you want, still: 160.09M used

当 从Redis中读取数据的时候，如果读取的key对应的value不在内存中，那么Redis就需要从==swap文件中加载相应数据==，然后再返回给请求方。 这里就存在一个==I/O线程池==的问题。在默认的情况下，Redis会出现阻塞，即完成所有的swap文件加载后才会相应。这种策略在客户端的数量较小，进行 批量操作的时候比较合适。但是如果将Redis应用在一个大型的网站应用程序中，这显然是无法满足大并发的情况的。所以Redis运行我们设置I/O线程 池的大小，对需要从swap文件中加载相应数据的读取请求进行并发操作，减少阻塞的时间。如果希望在海量数据的环境中使用好Redis，我相信理解Redis的内存设计和阻塞的情况是不可缺少的。

### redis I/O模型
Redis使用==单线程的IO复用模型==，自己封装了一个简单的AeEvent事件处理框架，主要实现了==epoll、kqueue和select==，对于单纯只有IO操作来说，单线程可以将速度优势发挥到最大，但是Redis也提供了一些简单的计算功能，比如排序、聚合等，对于这些操作，单线程模型实际会严重影响整体吞吐量，CPU计算过程中，整个IO调度都是被阻塞住的。
### memcache I/0
 
　　Memcached是多线程，非阻塞IO复用的网络模型，分为监听主线程和worker子线程，监听线程监听网络连接，接受请求后，将连接描述字pipe 传递给worker线程，进行读写IO, 网络层使用libevent封装的事件库，多线程模型可以发挥多核作用，但是引入了cache coherency和锁的问题，比如，Memcached最常用的stats 命令，实际Memcached所有操作都要对这个全局变量加锁，进行计数等工作，带来了性能损耗。

### memcache 内存管理
Memcached使用==预分配的内存池的方式==，使用slab和大小不同的chunk来管理内存，Item根据大小选择合适的chunk存储，==内存池的方式可以省去申请/释放内存的开销，并且能减小内存碎片产生==，但这种方式也会带来一定程度上的空间浪费，并且在内存仍然有很大空间时，新的数据也可能会被剔除。



