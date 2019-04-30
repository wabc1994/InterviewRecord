# 主要是总结下关键点问题

**反编译得到字节码javap** 

1. 提供了两个高级的字节码指令monitorenter和monitorexit，同步代码块是使用这两个高级字节码
2. 同步方法的话就依靠ACC_SYNCHRONIZED 标志符,区别同步方法与普通方法的区别
>其常量池中多了ACC_SYNCHRONIZED标示符。JVM就是根据该标示符来实现方法的同步的：当方法调用时，调用指令将会检查方法的 ACC_SYNCHRONIZED 访问标志是否被设置，如果设置了，执行线程将先获取monitor，获取成功之后才能执行方法体，方法执行完后再释放monitor。在方法执行期间，其他任何线程都无法再获得同一个monitor对象。


1. 首先理解对象头里面存储的Mark Word，充当一个对象监视器的作用

JVM一般是这样使用锁和Mark Word的：

1. 当没有被当成锁时，这就是一个普通的对象，Mark Word记录对象的HashCode，锁标志位是01，是否偏向锁那一位是0。

2. 当对象被当做同步锁并有一个线程A抢到了锁时，锁标志位还是01，但是否偏向锁那一位改成1，前23bit记录抢到锁的线程id，表示进入偏向锁状态

3. 线程阻塞的开销，然后引入操作系统的内核态和用户态， 线程切换开销情况

代码块同步是使用monitorenter和monitorexit指令实现

具体的对于所有请求锁的线程，对象监视器将线程分为一下几类


**注意请求锁，竞争锁这是两个东西**


1. contention list 所有**请求锁**的线程被首先放置在该竞争队列中

2. Entry list Contention List 中有机会获得锁的线程被放置到Entry List

3. Ondeck 任何时刻最多只有一个线程正在竞争锁，该线程为OnDeck

4. 调用wait()方法被阻塞的线程被放置到Wait Set中

4. owner获得锁的线程成为Owner

5. !owner 释放锁的线程

这面这个也可以将线程的状态关联起来

1. contentions list 和Entry list就是 waiting queue

2. waitset 就是出于阻塞状态的线程 blocking queue

3. ondeck就是准备就绪状态 ready thread

4. owner 就是running 状态 running thread


contentions list和entry list 都是阻塞状态的线程
>那些处于ContetionList、EntryList、WaitSet中的线程均处于阻塞状态，阻塞操作由操作系统完成（在Linxu下通过pthread_mutex_lock函数）

新请求锁的线程被首先加入到Contention List中，当某个拥有锁定线程(Owner状态)调用unlock之后，如果发现Entry List为空就从ContentionList中移动线程到Entry List中

# 主要查看的博客

博客主要分为三类吧，

1. 对象头

2. 具体synchronized 同步原理实现，具体代码等

3. 锁优化等操作

# synchronized 作用在静态方法和非静态方法

只要回答出一个东西即可， 这个问题也就是对象锁和类锁的区别

## 类锁的使用方法

比如的jvm 当中有100个该类的对象，当时只有一个类锁，有100个对象锁，在一个时间点只有一个线程能够执行获得该类的类锁，然后执行静态方法


>Class level lock prevents multiple threads to enter in synchronized block in any of all available instances of the class on runtime. This means if in runtime there are 100 instances of DemoClass, then only one thread will be able to execute demoMethod() in any one of instance at a time, and all other instances will be locked for other threads

```java
public class DemoClass
{
    //Method is static
    public synchronized static void demoMethod(){
 
    }
}
 

 
public class DemoClass
{
    public void demoMethod()
    {
        //Acquire lock on .class reference
        synchronized (DemoClass.class)
        {
            //other thread safe code
        }
    }
}
 

 
public class DemoClass
{
    // 锁住一个静态对象
    
    private final static Object lock = new Object();
 
    public void demoMethod()
    {
        //Lock object is static
        synchronized (lock)
        {
            //other thread safe code
        }
    }
}
```


## 对象锁

1. this
2. synchronized 方法
3. 或者直接锁住一个对象

锁住对象主要有一下几种使用方法

```java
public class DemoClass
{
    public synchronized void demoMethod(){}
}
 
or
 
public class DemoClass
{
    public void demoMethod(){
        synchronized (this)
        {
            //other thread safe code
        }
    }
}
 
or
 
public class DemoClass
{
    // 非静态对象
    private final Object lock = new Object();
    public void demoMethod(){
        synchronized (lock)
        {
            //other thread safe code
        }
    }
}
```

## 区别回答要点
**作用在静态方法**

1. 因为实例方法只能通过对象来使用的，所有synchronized(实例方法)就相当于synchronized这个对象, 对象锁

2. synchronized static就是synchronized(.class)  类锁,多个对象竞争同一吧锁

**是否产生互斥效果**

静态方法加锁，能和所有其他静态方法加锁的 进行互斥,但是不能和实例方法互斥

类锁和对象锁不同，他们之间不会产生互斥。

是否产生互斥主要是查看一个：线程是否竞争同一把锁

类锁和对象锁是两把锁，所以不存在互斥的说法


**怎样在程序当中判断是否产生互斥**
>主要是查看程序执行的顺序, 如果多个线程执行结果是串行的，那么就是产生了互斥的

[Synchronized同步静态方法和非静态方法总结 ](https://blog.csdn.net/u010842515/article/details/65443084)

比如下面的

```java
public class SynchronizedDemo {
    public synchronized void function() throws  InterruptedException{
        for(int i=0;i<3;i++){
            Thread.sleep(1000);
            System.out.println("synchronized function");
        }
    }
    public static synchronized void staticfunction(){
        for(int i=0;i<3;i++){
            try{
                Thread.sleep(1000);
                System.out.println("静态方法");
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        final SynchronizedDemo demo = new SynchronizedDemo();
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    SynchronizedDemo.staticfunction();
                    Thread.sleep(1000);
                }catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        });
        Thread t2= new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    demo.function();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        });
        t2.start();
        t1.start();

    }
}

```
是可以

- [Java的对象头和对象组成详解 ](https://blog.csdn.net/lkforce/article/details/81128115)

- [synchronized](https://blog.csdn.net/Thousa_Ho/article/details/77992743)

- [偏向锁、轻量级锁、自旋锁、重量级锁](https://blog.csdn.net/zqz_zqz/article/details/70233767)

[Java并发编程：Synchronized底层优化（偏向锁、轻量级锁） - liuxiaopeng - 博客园](https://www.cnblogs.com/paddix/p/5405678.html)