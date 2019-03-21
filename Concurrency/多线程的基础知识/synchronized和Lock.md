# 面试当中该如何答出这个区别点
1. Lock是一个接口，synchronized是java 关键字，synchronized是内置语言实现的,通过JVM字节码实现，比如monitorEnter **监视器进入** 和monitor exit **对象监视器退出**，Lock 是底层的操作系统CPU指令实现的
2. synchronized 在发生异常时，会自动释放线程占用的锁，因此不会导致死锁的发生，lock 在发生异常的时候，如果没有主动通过unlock()去释放锁，则很有可能造成死锁现象，因此使用Lock时需要在finally块中释放锁
3. Lock可以让等待锁的线程相应中断；而synchronized不行，使用synchronized时，等待的线程会一直等待下去，不能够响应中断，lockInterruptibly()
4. 通过Lock可以知道有没有成功获取锁，而synchronized却无法办到
5. Lock可以提高多个线程读操作的效率，提供了读写锁，读读不互斥的操作，synchronized同一时刻不管是读还是写都只能有一个线程对共享资源操作，其他线程只能等待


6. lock 提供公平公正锁和非公平所，synchronzied只有非公平锁
7. lock可以获取知道锁的各种状态 trylock()方法


lock 提供trylock() 尝试获取锁的多种选择，

lock可与条件变量使用


# 参考链接

[详解synchronized与Lock的区别与使用 - brickworkers的博客 - CSDN博客](https://blog.csdn.net/u012403290/article/details/64910926)