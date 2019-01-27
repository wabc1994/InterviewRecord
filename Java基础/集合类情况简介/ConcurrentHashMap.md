[参考链接](https://www.ibm.com/developerworks/cn/java/java-lo-concurrenthashmap/)
# 前提知识

## Java内存模型

## 指令重排序

## happen-before 原则



# ConcurrentHashMap 如何实现线程安全的？

JDK1.7 

**减少锁的粒度segment锁**

它的思想是将物理上的一个锁，拆成逻辑上的多个锁，增加并行度，从而降低锁竞争。它的思想也是用空间来换时间；

核心数据结构

segments [] 数组， 默认的时候是16个节点的情况， hashEntry 每个节点, treeNode(后面转换成为)

1. public V get(Object key)不涉及到锁，也就是说获得对象时没有使用锁；

2. put、remove方法要使用锁，但并不一定有锁争用，原因在于ConcurrentHashMap将缓存的变量分到多个Segment，每个Segment上有一个锁，只要多个线程访问的不是一个Segment就没有锁争用，就没有堵塞，各线程用各自的锁，ConcurrentHashMap缺省情况下生成16个Segment，也就是允许16个线程并发的更新而尽量没有锁争用；


>Segment< K,V >[] segments ,  将全部数组+链表，分成多个模块之前的情况，

segment 继承了reetrantlock 所以可以充当多的功能

static final class Segment<K,V> extends ReentrantLock implements Serializable

![concurrentHashMap](https://github.com/wabc1994/InterviewRecord/blob/master/Java%E5%9F%BA%E7%A1%80/%E5%9B%BE%E7%89%87/ConcurrentHashMap7.png)

# ConcurrentHashMap与HashMap的区别

一个是线程安全，一个是非线程安全的问题

# ConcurrentHashMap 与Hashtable的区别

总体思想

1. 在1.8版本以前，ConcurrentHashMap采用分段锁的概念，使锁更加细化
2. 但是1.8已经改变了这种思路，而是利用CAS+Synchronized来保证并发更新的安全，当然底层采用数组+链表+红黑树的存储结构。

HashTable容器使用**synchronized**来保证线程安全，但是在线程竞争激烈的情况下，HashTable的效率非常低

# JDK1.7和JDK1.8 ConcurrentHashMap 实现区别

# 数据结构的变化



[两者包括的基础数据结构](https://blog.csdn.net/wengcheng_k/article/details/78868759)



### JDK1.7



1. 一个ConcurrentHashMap中包含一个Segment<K,V>[] segments 数组。 
2. 一个Segment对象中包含一个HashEntry<K,V>[] table数组。 
3. 一个HashEntry对象包含hash值，Key，Value，以及下一个HashEntry对象JDK1.8

`Segment`继承`ReentrantLock`重入锁（`Segment<K,V> extends ReentrantLock`），也就是说每个`Segment` 对象都是重入锁， Segment 也被称为“锁段”的概念， 

### JDK1.8

如何保证并发中的线程安全问题， 主要思想是使用CAS(compare and swap , 比较然后进行交换)和sychronized锁实现的线程安全问题，并且结构上面跟hashmap jdk1.8 一样的情况下，采用数组+链表 + 红黑树的结构实现

1. Sychronized  是对象锁，锁住的主要是Node<K,V> 锁住一个小节点， sychronized(一个节点)
2. 无锁思想- 执行者CAS [ConcurrentHashmap具体是如何利用CAS](https://blog.csdn.net/weixin_42636552/article/details/82383272)， 如何CAS技术是为了保证如何在不加锁(乐观)的情况下如何实现线程安全访问变量的



# 乐观锁、悲观锁、加锁和无锁

乐观锁和悲观锁是从宏观面上面来讲的， 是一种思想。实际映射到并发编程中那就是加锁和无锁

这两种派系映射到并发编程中就如同加锁与无锁的策略，即加锁是一种悲观策略，无锁是一种乐观策略，因为对于加锁的并发程序来说，它们总是认为每次访问共享资源时总会发生冲突，因此必须对每一次数据操作实施加锁策略。 
而无锁则总是假设对共享资源的访问没有冲突，线程可以不停执行，无需加锁，无需等待，一旦发现冲突，无锁策略则采用一种称为CAS的技术来保证线程执行的安全性，这项CAS技术就是无锁策略实现的关键，下面我们进一步了解CAS技术的奇妙之处。



## 关于是如何从加锁进化到无锁的情况?

- [从加锁到无锁](https://dzone.com/articles/how-cas-compare-and-swap-java)



- [Java中的CAS实现](https://howtodoinjava.com/java/multi-threading/compare-and-swap-cas-algorithm/)