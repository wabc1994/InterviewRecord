# 布隆过滤器
## 底层实现数据结构
一个二进制数组位 + 多个hash 函数 bit array
![]()
## 
误判率， 
## 可以作为解决Redis当中的缓存穿透问题

如何把大量无效的请求挡在缓存之中

首先理解什么叫缓存穿透问题？ 查询一个肯定不存在的key, 这样话肯定要穿过缓存， 对数据库进行直接访问

所以我们可以在缓存上面增加一个布隆过滤器，将那些肯定不存在数据库当中的查询给过滤掉，这样就不用再来查询数据层面


可以看下官方链接的东西

判断一个既肯定不在缓存当中，又不在数据库当中的数据

[redis当中的布隆过滤器ReBloom](https://redislabs.com/blog/rebloom-bloom-filter-datatype-redis/)

# 利用bloomfilter 快速判断一个不存在的key

log-structured merger tree 大量牺牲读性能，增加写性能， 查询操作先查找内存，然后再查找磁盘，导致效率底下，要查找两次，我们可以设置一个bloomfilter过滤器，类似Redis 缓存和主数据之间的关系
