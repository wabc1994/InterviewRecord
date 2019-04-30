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

[内存管理](https://github.com/wabc1994/InterviewRecord/blob/master/Redis_learning/redis内存模型.md)

# 源码解读
- zmalloc(size_t size)


申请内存问题
```java

void *zmalloc(size_t size) {
    void *ptr = malloc(size+PREFIX_SIZE);
 
    if (!ptr) zmalloc_oom_handler(size);
#ifdef HAVE_MALLOC_SIZE
    update_zmalloc_stat_alloc(zmalloc_size(ptr));
    return ptr;
#else
    *((size_t*)ptr) = size;
    update_zmalloc_stat_alloc(size+PREFIX_SIZE);
    return (char*)ptr+PREFIX_SIZE;
#endif
}
```


update_zmalloc_stat_alloc主要完成两个功能
- 完成8字节对齐
- 判断是否线程安全问题
    - used_memory+n 线程安全的话
    - update_zmalloc_stat_add(_n) ,使用锁pthred_mutex_lock(&used_memory_metex)

**面试常见的问题**

更新used_memory+_n 是使用内存对齐技术处理后的_n
```java
#define update_zmalloc_stat_alloc(__n) do { \
    size_t _n = (__n); \
    
    // 手动进行内存对齐的工作
    if (_n&(sizeof(long)-1)) _n += sizeof(long)-(_n&(sizeof(long)-1)); \
    if (zmalloc_thread_safe) { \
        update_zmalloc_stat_add(_n); \
    } else { \
        used_memory += _n; \
    } \
} while(0)

```


linux64位平台下面的内存对齐8字节内存对齐

完成内存对齐的主要是在update_zmalloc_stat_alloc函数的前半部分

1. zmalloc(size) --- ptr = size+PREFIX_SIZE =_n 


2. _n +=size(long)-(_n&size(long)-1)  为了内存对齐需要增加的那部分内容

3. used_memory+=_n  申请一个size 大小的内存块实际系统要申请的内存是_n 大小



[”zmalloc“函数-内存对齐技术](https://juejin.im/entry/5b2b4993e51d4553156bdb30)

[内存分配与对齐方式](https://blog.csdn.net/wallwind/article/details/24234487)

# 为何需要内存对齐

CPU一次性能读取数据的二进制位数称为字长，也就是我们通常所说的32位系统（字长4个字节）、64位系统（字长8个字节）的由来。所谓的8字节对齐，就是指变量的起始地址是8的倍数。比如程序运行时（CPU）在读取long型数据的时候，只需要一个总线周期，时间更短，如果不是8字节对齐的则需要两个总线周期才能读完数据。
        本文中我提到的8字节对齐是针对64位系统而言的，如果是32位系统那么就是4字节对齐。实际上Redis源码中的字节对齐是软编码，而非硬编码。里面多用sizeof(long)或sizeof(size_t)来表示。size_t（gcc中其值为long unsigned int）和long的长度是一样的，long的长度就是计算机的字长。这样在未来的系统中如果字长（long的大小）不是8个字节了，该段代码依然能保证相应代码可用。

[Redis内存管理的基石zmallc.c源码解读](https://blog.csdn.net/guodongxiaren/article/details/44747719)