# 背景

为何需要这个锁?
 
 针对 Synchronized的性能问题，我们增加了Lock ReetrantLock ReadWriteLock ReetrantLockReadWriteLock 
 
等等进行优化，但是ReetrantLock 下面还是存在一大堆的问题， 我们知道读写操作也是阻塞的，就像我们之前在lock.md里面讲到的一样，会产生写饥饿的情况


# 写饥饿的问题
因为在读线程非常多而写线程非常少的情况，写线程可发生饥饿现象，也就是因为大量的读操作存在读操作都阻塞写线程

那么如何解决读不阻塞写的情况？

这就是JDK1.8新出的stampedLock要解决的问题


# StampedLock

核心思想

**在读的时候如果发生了写操作，导致数据不一致则应当重读而不是在读的时候阻塞写操作，但是写写操作还是会阻塞的**

如何理解数据不一致性?

其实就是版本号，stampLock 维护了一个版本号，stamp 变量


**具体而言**

>因此写线程可能几乎很少被调度成功！当读执行的时候另一个线程执行了写，则读线程发现数据不一致则执行重读即可或者锁升级为悲观模式(刚开始是乐观模式，然后升级为悲观模式)。所以读写都存在的情况下，
 在读的时候如果发现有写的操作，那么就重新读一次。
 
 **那下一个问题就是StampedLock是怎样知道读的时候发生了写操作呢？**
 
主要为维护了一个版本号stamp,类似于邮编号，写操作会对该变量进行一个stamp+1 操作
 
**tryOptimisticRead()**
有两种锁，一种是悲观锁，另一种是乐观锁，如果线程拿到悲观模式的锁，那么就是读写互斥，如果是乐观锁状态的话，就是不互斥状态
 
 **writeLock()**
 
 
[StampedLock变化](http://ifeve.com/java-8-stampedlocks-vs-readwritelocks-and-synchronized/)

[Java1.8 增加的功能StampedLock](https://blog.csdn.net/sunfeizhi/article/details/52135136)