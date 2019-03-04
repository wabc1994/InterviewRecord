# 精细化内存管理
1. 过期策略
[](https://github.com/wabc1994/InterviewRecord/blob/master/Redis_learning/过期策略.md)
2. 数据淘汰策略
[](https://github.com/wabc1994/InterviewRecord/blob/master/Redis_learning/内存淘汰策略.md)

3. 内存管理分配策略

在这里我们主要是介绍redis 内存管理技术进行总结， 并与memcached 当中的slab机制进行比较

# Redis内存管理
优秀的内存管理：
1. SDS 动态字符串，并且获取字符串长度的时间长度为O(1)
2. 压缩队列表
3. 对象refcount 引用计数，每个redisobj都有
4. hyperloglog 基数估计 估计对象的大小，应用比如统计重复的ip上面的情况

![](https://github.com/wabc1994/InterviewRecord/blob/master/Redis_learning/picture/Redis内存管理.png)

其余部分我在另一篇也写了一些，直接贴链接了

[](https://github.com/wabc1994/InterviewRecord/blob/master/Redis_learning/redis内存模型.md)