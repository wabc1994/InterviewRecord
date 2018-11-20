[参考链接](https://www.ibm.com/developerworks/cn/java/java-lo-concurrenthashmap/)
# ConcurrentHashMap 如何实现高并发的？
减少锁的粒度
它的思想是将物理上的一个锁，拆成逻辑上的多个锁，增加并行度，从而降低锁竞争。它的思想也是用空间来换时间；
核心数据结构
>Segment< K,V >[