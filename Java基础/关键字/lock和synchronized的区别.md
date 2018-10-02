线程、synchronized、 lock 、 wait()、notify()、 notifyAll()
# 线程与对象的关系
线程是做事情的执行载体，对资源(对象)进行执行一定的操作
# 线程的几种状态

 - 新建（new）：处于该状态的时间很短暂。已被分配了必须的系统资源，并执行了初始化。表示有资格获得CPU时间。调度器可以把该线程变为runnable或者blocked状态  
 - 就绪（Runnable）：这种状态下只要调度器把时间片分配给线程，线程就能运行。处在这种状态就是可运行可不运行的状态  
 - 阻塞（Bolocked）：线程能够运行，但有个条件阻止它的运行。当线程处于阻塞状态时，调度器将会忽略线程，不会分配给线程任何CPU时间（例如sleep）。只有重新进入了就绪状态，才有可能执行操作。
 -死亡（Dead）：处于死亡状态的线程讲不再是可调度的，并且再也不会得到CPU时间。任务死亡的通常方式是从run()方法返回。
对象锁也叫对象监控器
一个任务进入阻塞状态，可能有如下原因： 
1.sleep 
2.wait()，知道线程得到了notify()或者notifyAll()消息，线程才会进入就绪状态。 
3.任务在等待某个输入/输出完成 
4.线程在试图在某个对象上调用其同步控制方法，但是对象锁不可用，因为另一个任务已经获取了这个锁

多线程之间竞争的是共享变量，不包括局部变量和定义在函数中参数的，这些都是线程独享的，不需要跟其他线程进行竞争


多线程代码的书写套路：
在一个主类中定义一些互斥变量（可以是对象，一般是其他类的对象），获取
## Java中多线程的实现方式
**When you create a thread, you need an instance of Runnable **

 有四种方式实现线程： 
   - 继承Thread
   - 实现Runnable接口
   - 实现Callable和
   - 通过线程池创建一个线程 
###  到底多线程共享的资源是什么？
共享一个东西，这个共享的东西可以是一个数据(一个对象)
实现Runnable接口定义线程类，还有一个重要功能就是可以处理同一资源，实现资源共享(将共享资源放在runnable实现借口类中)
一个类实现借口Runnable ，最终也是要通过Thread 类来创建线程的， **new Thread(参数一，参数二)才是真正创建的线程**， 参数一为实现了runnable借口的类对象，参数二为最终的线程名字， 参数一可重复利用多次(可以理解为线程竞争资源，多个线程在但一定时间内只能有个获得执行参数一中的代码体，参数一对象类中要实现lock锁或者synchronized等同步操作)
# 锁的作用是什么？ 

锁主要用来实现资源共享的同步。只有获取到了锁才能访问该同步代码，否则等待其他线程使用结束释放锁。

# synchronized 的缺陷  
synchronized是java中的一个关键字，也就是说是Java语言内置的特性。那么为什么会出现Lock呢？
在上面一篇文章中，我们了解到如果一个代码块被synchronized修饰了，当一个线程获取了对应的锁，并执行该代码块时，其他线程便只能一直等待，等待获取锁的线程释放锁，而这里获取锁的线程释放锁只会有两种情况：

- 读-读不互斥，读线程可以并发执行； 
- 2、读-写互斥，有写线程时，读线程会堵塞； 
- 3、写-写互斥，写线程都是互斥的

1. 获取锁的线程执行完了该代码块，然后线程释放对锁的占有；
2. 执行发生异常，此时JVM会让线程自动释放锁。那么如果这个获取锁的线程由于要等待IO或者其他原因（比如调用sleep方法）被阻塞了，但是又没有释放锁，其他线程便只能干巴巴地等待，试想一下，这多么影响程序执行效率。因此就需要有一种机制可以不让等待的线程一直无期限地等待下去（比如只等待一定的时间或者能够响应中断），通过Lock就可以办到。
3. 再举个例子：当有多个线程读写文件时，读操作和写操作会发生冲突现象，写操作和写操作会发生冲突现象，但是读操作和读操作不会发生冲突现象。
但是采用synchronized关键字来实现同步的话，就会导致一个问题：
如果多个线程都只是进行读操作，所以当一个线程在进行读操作时，其他线程只能等待无法进行读操作。
因此就需要一种机制来使得多个线程都只是进行读操作时，线程之间不会发生冲突，通过Lock就可以办到。
4. 另外，通过Lock可以知道线程有没有成功获取到锁。这个是synchronized无法办到的。
  
    总结一下，也就是说Lock提供了比synchronized更多的功能。但是要注意以下几点：

     1. Lock不是Java语言内置的，synchronized是Java语言的关键字，因此是内置特性。Lock是一个类，通过这个类可以实现同步访问；
     2. lock和synchronized有一点非常大的不同，采用synchronized不需要用户去手动释放锁，当synchronized方法或者synchronized代码块执行完之后，系统会自动让线程释放对锁的占用；而Lock则必须要用户去手动释放锁，如果没有主动释放锁，就有可能导致出现死锁现象。
     所以为了保证锁最终被释放(发生异常情况)，要把互斥区放在try内，释放锁放在finally内。**（放在finally代码快表示到时候一定执行）**
lock 提供来读写锁，读写是
# lock 和synchronized的区别和联系

### 不同之处
实现机制的区别、性能不一致、释放机制、作用位置不一样
   1. 数据同步需要依赖锁，那锁的同步又依赖谁？synchronized给出的答案是在软件层面依赖JVM，系统会监控锁的释放与否；而Lock给出的方案是在硬件层面依赖特殊的CPU指令，是Java中的一个类，必须由程序员释放
   2. 资源竞争激励的情况下，lock性能会比synchronize好，竞争不激励的情况下，synchronize比lock性能好。
   3. 因为当调用Synchronized修饰的代码时，并不需要显示的加锁和解锁的过程，所以称之为隐式锁.Lock显示锁
   4. Lock中的ReentrantLock 和synchronized 都是可重入锁

## synchronized 
要区别的是作用的范围和锁的东西是两个概念，**作用于方法上，作用于对象上，锁住对象实例，锁住一个代码块**

1. 当synchronized作用在**方法上**时，锁住的便是**对象实列（this）,对象锁**

2. 当作用在静态方法时锁住的便是对象对应的Class实例，因为Class数据存在于永久带，因此静态方法锁相当于该类的一个全局锁；static synchronized 称为类锁
3. 当synchronized作用于某一个对象实例时，锁住的便是**对应的代码块**。在HotSpot 中JVM实现中，锁有个专门的名字：对象监视器。

4. 通常和wait，notify，notifyAll一块使用。
      **wait 释放程度大于sleep,
      **区别就是对象锁，cpu（竞争的资源）**
      **wait：释放占有的对象锁，释放CPU。**  
      **sleep：则是释放CPU，但是不释放占有的对象锁。**
      **notify：唤醒等待队列中的一个线程，使其获得锁进行访问。**
      **notifyAll：唤醒等待队列中等待该对象锁的全部线程，让其竞争去获得锁。**  
      *线程得到了notify()或者notifyAll()消息，线程才会进入就绪状态*
    *wait(),notify() ,notifyAll()都是是对象所有，用于多线程的沟通 
轮询 polling :
#### wait和sleep的区别
为何需要wait() 和sleep() 来理解？
   ->sleep()使线程睡过去,也就是暂停执行,wait()是线程等待事件的发生,如果你的线程需要某个条件才能继续运行,就用wait(),他不占资源,notify()通知相应的在等待的线程条件已经满足,可以启动了,采用wait()的线程只能够通过notify或者是notifyAll()启动,不能够自己启动的

1. wait和notify方法定义在Object类中，因此会被所有的类所继承。 这些方法都是final的，即它们都是不能被重写的，不能通过子类覆写去改变它们的行为。 而sleep方法是在Thread类中是由native修饰的，本地方法。
2. 当线程调用了wait()方法时，它会释放掉对象的锁。 
   另一个会导致线程暂停的方法：Thread.sleep()，它会导致线程睡眠指定的毫秒数，但线程在睡眠的过程中是不会释放掉对象的锁的。
3. 正因为wait方法会释放锁，所以调用该方法时，当前的线程必须拥有当前对象的monitor，也即lock，就是锁。要确保调用wait()方法的时候拥有锁，即，wait()方法的调用必须放在synchronized方法或synchronized块中。
 一些同步解释
 
[what is the difference between the sleep() and wait()?](https://blog.csdn.net/Alex19881006/article/details/24647697)
## lock 
Lock 是JDK1.5 下面提供的， 
Lock是一个接口，提供了无条件的、可轮询的、定时的、可中断的锁获取操作，所有的加锁和解锁操作方法都是显示的，因而称为显示锁，下面的实现有下面这几种情况下。Lock的几个实现类ReentrantLock、ReentrantReadWriteLock.ReadLock和ReentrantReadWriteLock.WriteLock解析。 
ReentrantReadWriteLock maintains two separate locks, one for reading and one for writing:


Lock需要定义为一个类的成员属性，在try语句之前上锁，在try语句中需要放互斥区，在finally 释放锁。读写锁(ReadWriteLock)，我们会有一种需求，在对数据进行读写的时候，为了保证数据的一致性和完整性，需要读和写是互斥的，写和写是互斥的，但是读和读是不需要互斥的，这样读和读不互斥性能更高些
这里重点介绍ReentrantLock。ReentrantLock，意思是“可重入锁”，ReentrantLock是唯一实现了Lock接口的类，并且ReentrantLock提供了更多的方法。 

ReentrantReadWriteLock是对ReentrantLock的复杂扩展，能适合更加复杂的业务场景，ReentrantReadWriteLock可以实现一个方法中读写分离的锁的机制。而ReentrantLock只是加锁解锁一种机制。
### 可重入锁
概念：也叫做递归锁，指的是同一线程 外层函数获得锁之后 ，内层递归函数仍然有获取该锁的代码，但不受影响。简单的说：当一个线程请求得到一个对象锁后再次请求此对象锁,可以再次得到该对象锁,就是说在一个synchronized方法/块或ReentrantLock的内部调用本类的其他synchronized方法/块或ReentrantLock时，是永远可以拿到锁。
 （外层函数，内层递归函数）

 