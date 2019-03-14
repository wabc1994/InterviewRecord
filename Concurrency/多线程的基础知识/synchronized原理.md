# 主要是总结下关键点问题


1. 首先理解对象头里面存储的Mark Word，充当一个对象监视器的作用

JVM一般是这样使用锁和Mark Word的：

1. 当没有被当成锁时，这就是一个普通的对象，Mark Word记录对象的HashCode，锁标志位是01，是否偏向锁那一位是0。

2. 当对象被当做同步锁并有一个线程A抢到了锁时，锁标志位还是01，但是否偏向锁那一位改成1，前23bit记录抢到锁的线程id，表示进入偏向锁状态

3. 线程阻塞的开销，然后引入操作系统的内核态和用户态， 线程切换开销情况

代码块同步是使用monitorenter和monitorexit指令实现

具体的对于所有请求锁的线程，对象监视器将线程分为一下积累


**注意请求锁，竞争锁这是两个东西**


1. contention list 所有**请求锁**的线程被首先放置在该竞争队列中
2. Entry list Contention List 中有机会获得锁的线程被放置到Entry List
3. Ondeck 任何时刻最多只有一个线程正在竞争锁，该线程为OnDeck
4. 调用wait()方法被阻塞的线程被放置到Wait Set中
4. owner 获得锁的线程成为Owner
5. !owner 释放锁的线程




新请求锁的线程被首先加入到Contention List中，当某个拥有锁定线程(Owner状态)调用unlock之后，如果发现Entry List为空就从ContentionList中移动线程到Entry List中

# 主要查看的博客

博客主要分为三类吧，

1. 对象头

2. 具体synchronized 同步原理实现，具体代码等

3. 锁优化等操作

[Java的对象头和对象组成详解 ](https://blog.csdn.net/lkforce/article/details/81128115)

[synchronized](https://blog.csdn.net/Thousa_Ho/article/details/77992743)

[java 中的锁 -- 偏向锁、轻量级锁、自旋锁、重量级锁](https://blog.csdn.net/zqz_zqz/article/details/70233767)

[Java并发编程：Synchronized底层优化（偏向锁、轻量级锁） - liuxiaopeng - 博客园](https://www.cnblogs.com/paddix/p/5405678.html)