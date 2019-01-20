
# 管道技术

**首先声明一点哈：客服端和服务器端之间的通信也都是socket，先建立连接哈，之后才是发送命令(建立连接后的数据发送)**


redis客户端和服务器端之间是一种Tcp通信，一次处理一条客户端命令，需要一次RTT时间，并且返回一个结果

10个命令，RTT为1s, 那就是10s, 每次返回一个很小的基础结果而已，管道就是为了讲多次命令改成一次发送，降低客户端和服务器端之间的RTT次数，


**多个命令行一次返回结果**

将先处理好的命令结果存放在队列当中，先处理的放在对头，后处理的放在队尾巴，形成有序的情况



[分布式缓存Redis之Pipeline](https://blog.csdn.net/u011489043/article/details/78769428)


特别是针对一些Redis当中的loop 循环不断插入的基本情况下更是如此

RTT 开销非常巨大


```java
for(int i=0；i<10000;i++){
    jedis.set(String.valueOf(i),String.valueOf("liuxiong"));
}
```



采用这两种方式插入的方式

时间对别大概是20000ms，19694ms





将上述代码改动下
671ms 不到七百的操作,

```java
for(int i=0；i<10000;i++){
    pipeline.set(String.valueOf(i),String.valueOf("liuxiong"));
```

53条发送一次大概的样子吧


[Redis client/server之间是如何进行交互的](https://www.infoq.cn/article/communication-redis-clientserver)

[Redis的主要是一些代码的对比结果，使用jedis 每一调插入和直接直接使用管道插入的情况](https://blog.csdn.net/unscdf117/article/details/79070141)

[Redis批量操作详解及性能分析讲解得非常清晰的基本情况](https://blog.csdn.net/Jinlu_npu/article/details/79984127)