Kafaka 它是一个分布式的、分区的和重复的分布式消息队列。

与传统的消息队列模型相比， kafka在下面有新的优势情况：

Apache kafka 与传统的消息传递技术相比优势之处在于哪里：

- 快速：单一的kafka 代理可以处理成千上万的客户端，每秒处理兆字节的读写操作；

- 可伸缩性： 在一组机器上对数据进行分区和简化，以支持更大的数据

- 持久性： 消息是持久性的，并在集群中进行复制，以防止数据不见
- 设计： 他提供了容错性和持久性

核心概念

1. Producer
2. Consumer
3. Broker
4. Cluster
5. Topic
6. Partitions
7. Offset
8. Consumer groups

这其中的基本概念

![Screen Shot 2018-11-13 at 11.30.46 PM](/Users/coderlau/Desktop/Screen Shot 2018-11-13 at 11.30.46 PM.png)



如何标记一个消息的唯一性？

![Screen Shot 2018-11-13 at 11.46.23 PM](/Users/coderlau/Desktop/Screen Shot 2018-11-13 at 11.46.23 PM.png)

# topic 

将一个话题分区partition ，Partition 是物理上的概念，每个 Topic 包含一个或多个 Partition。

# partition

将一个topic 分开为多个partition, 

![Screen Shot 2018-11-13 at 11.40.47 PM](/Users/coderlau/Desktop/Screen Shot 2018-11-13 at 11.40.47 PM.png)

内部的partition是如何工作的情况

# 在Kafka中broker的意义是什么?

在Kafka集群中，broker术语用于引用服务器

# Kafka为什么需要复制?



Kafka的信息复制确保了任何已发布的消息不会丢失，并且可以在机器错误、程序错误或更常见些的软件升级中使用。replication 可以设置这个参数，比如设置为N ，只有有1到N-1个服务器运行就可以了

# kafka是如何保持消息有序性

# kafka 如何保持消息不见

# 消息是如何在分区中进行记录的

offset 主要是偏移量的概念保证消息的有序性

具体是如何使用的情况



[具体架构设计](https://www.infoq.cn/article/kafka-analysis-part-1)