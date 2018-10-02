* [RPC](#rpc 基础)
* [Duddo](#Duddo框架学习)
# RPC
远程过程调用（调用部署在远程机器上的服务）就是RPC（Remote Procedure Call Protocol），在各大互联网公司中被广泛使用，如阿里巴巴的HSF、Dubbo（开源）、Facebook的Thrift（开源）、Google GRPC（开源）、Twitter的Finagle（开源）等。
## RPC 于HTTP异同(一个考点)
RPC服务和HTTP服务对比

（最主要是底层的依赖协议不一样）
先说一下他们最本质的区别，就是RPC主要是基于TCP/IP协议的，而HTTP服务主要是基于HTTP协议的，我们都知道HTTP协议是在传输层协议TCP之上的，所以效率来看的话，RPC当然是要更胜一筹啦！下面来具体说一说RPC服务和HTTP服务

## OSI网络七层模型

在说RPC和HTTP的区别之前，我觉的有必要了解一下OSI的七层网络结构模型（虽然实际应用中基本上都是五层），它可以分为以下几层： （从上到下）
1.  第一层：应用层。定义了用于在网络中进行通信和传输数据的接口；
2. 第二层：表示层。定义不同的系统中数据的传输格式，编码和解码规范等；
3. 第三层：会话层。管理用户的会话，控制用户间逻辑连接的建立和中断；
4. 第四层：传输层。管理着网络中的端到端的数据传输；
5. 第五层：网络层。定义网络设备间如何传输数据；
6. 第六层：链路层。将上面的网络层的数据包封装成数据帧，便于物理层传输；
7. 第七层：物理层。这一层主要就是传输这些二进制数据。

     实际应用过程中，五层协议结构里面是没有表示层和会话层的。应该说它们和应用层合并了。我们应该将重点放在应用层和传输层这两个层面。因为HTTP是应用层协议，而TCP是传输层协议。好，知道了网络的分层模型以后我们可以更好地理解为什么RPC服务相比HTTP服务要Nice一些！

## RPC的服务架构
 主要分为服务端Server 和客户端client 情况, 成熟的rpc框架还会有服务注册功能模块 Registry（注册中心）的角色,
 ![框架图](https://img-blog.csdn.net/20181002131701885?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzI4MzUwOTk3/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)
*  注册中心，用于服务端注册远程服务以及客户端发现服务
*  服务端，对外提供后台服务，将自己的服务信息注册到注册中心
*  客户端，从注册中心获取远程服务的注册信息，然后进行远程过程调用
注册中心Duddo 采用zookeeper分布式协调服务
[注册中心服务理解](https://www.cnkirito.moe/rpc-registry/)
## 总结
总结
RPC服务和HTTP服务还是存在很多的不同点的，一般来说，RPC服务主要是针对大型企业的，而HTTP服务主要是针对小企业的，因为RPC效率更高，而HTTP服务开发迭代会更快。总之，选用什么样的框架不是按照市场上流行什么而决定的，而是要对整个项目进行完整地评估，从而在仔细比较两种开发框架对于整个项目的影响，最后再决定什么才是最适合这个项目的。一定不要为了使用RPC而每个项目都用RPC，而是要因地制宜，具体情况具体分析。

# Duddo
[总体框架原理介绍](https://www.cnkirito.moe/rpc-registry/)

[面试问题点](https://mp.weixin.qq.com/s/PdWRHgm83XwPYP08KnkIsw)

[核心原理：服务暴露的本质情况](http://ifeve.com/dubbo-consumer-service2invoker/)


