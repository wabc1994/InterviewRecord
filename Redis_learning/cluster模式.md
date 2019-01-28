
# 集群当中的基本概念
[普通Hash函数/一致性Hash原理/Hash槽](https://blog.csdn.net/clz1314521/article/details/80604555)
[数据迁移方式 Hash槽 和 一致性hash对比，优缺点比较](https://blog.csdn.net/tianpeng341204/article/details/78963850)


# zookeeper
分布式服务协调，包括服务配置和发现，等功能

主要是利用发布/订阅功能

1. 配置中心
2. 注册中心

# 在集群模式当中也有



# 节点失效或者集群扩容或者缩容

**数据迁移的基本问题**

- Memcached采用一直性hash算法实现，所以是有损扩容的，只能最大程度降低缓存失效。

- redis 使用hash槽 slot 实现数据分片cluster功能，当面对集群节点扩容或者缩容的使用，数据的迁移可以做到无损，数据迁移过程当中整个集群也可以正常对外提供正常的服务，整个过程是没有downtime时间的


缓存分片技术主要分为三种情况, 
1. 基于客户端的分片技术shareJedis（memcached 就是使用该种分布式技术）
2. 基于中间代理的分片技术 Twemtproxy 
3. 基于服务器端的分片技术(redis3.0 版本开始之后有)

其中1和2主要是主要是在没有3出现的时候主要使用的情况


# 基于客户端的shard

我们知道Memcached的分布式其实是一种“伪分布式”，也就是它的服务器结点之间其实是相互无关联的，之间没有网络拓扑关系，由客户端来决定一个key是存放到哪台机器。

就是各个节点之间是没有联系的，没有通信的，互相之间不知道彼此的基本信息

>Several users and companies have rolled their own partitioning implementations for Redis. Most of these rely on client side partitioning. In client side partitioning, clients are responsible for determining where to write or read a given key.


# memcached 当中的伪分布式
各个memcached服务器之间互不通信，各自独立存取数据，不共享任何信息。服务器并不具有分布式功能，分布式部署取决于memcache客户端。

# 基于服务器端的shard
1. 每个节点服务器开启两个tcp 连接端口，一个是6379 用于与客户端进行一个正常的连接，处理客户端方面的request ,读写查询

2. 另一个端口是采用16379 +10000，用于节点服务器之间的通信情况，各个服务器是彼此知道各自的状态的(grossip)，只要其中任何一个服务器节点down 掉之后，整个集群都不能正常对外进行提供服务，再这个时候就需要进行slot的重新分配(reshard)，
并且进行数据key-value的基础迁移

# 不保证强一致性
会丢失部分写操作，这其中也是主从复制模式的主要缺点之一。
> 主要是因为 Redis 主从复制模式采用异步复制的模型。 

比如 下面的基本流程
1. 客户端向服务器B发送一个写命令ZADD
2. 服务器端向客服端发送一个回复命令，代表写成功
3. B 向自己的从服务器slave a1,a2,a3 发送同步命令

在上面的三个步骤当中，如果3步当中 a1,a2,a3没有办法顺利完成主从复制(由于网络原因获取其他方面的各种情况下)，比如a3没有办法完成正常的同步，漏写ZADD，同时写masterB down了，a3 变成了master,这就导致了丢失部分写操作。