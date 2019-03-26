# 分布式session服务器共享的问题

## 问题的定义？

先来理解这是一个什么问题？ 当我们应对搞并发量时，搞一个分布式式的服务器集群方式，再加上负载均衡 **(ngnix + tomcat )**，
将客户端的请求发送到不同服务器上面，
比如那么这样就带来如果一个用户访问server1， 之后又把被分配到另一个server2，
那么在server1上面的保存的客户端通信信息session, 如何从server共享到serve2 这就是个问题。


这个问题也就是如何解决session跨域共享的问题， 

>distributed session cache 

分布式session也是属于高并发的一个问题，当被问到高并发问题，是

## 解决方案

1. spring session + redis

2. 托管到缓存 比如redis, memcached专门的缓存里面

3. 12306 背后的分布式内存对象缓存系统GemFire

4. session复制


我们主要是想了解 基于Redis或者memcached来保存session


# 基于缓存的保存session

提供一个集群来保存session 共享信息，其他应用统统把自己的sessionx 信息存放在session 集群里面的服务器组，当应用系统需要

session 信息时直接到session 集群服务器上去读取，目前大多都是使用Memcache或redis来对Session进行存储。以这种方式来同步session，不会加大数据库的负担，并且安全性比用cookie大大的提高，把session放到内存里面，比从文件中读取要快很多