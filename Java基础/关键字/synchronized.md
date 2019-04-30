[TOC]

# 理解锁的基础知识

如果想要透切的理解java锁的来龙去脉，需要了解一下知识

# ## 基础知识一：锁的类型

锁从宏观上分类，分为悲观锁与乐观锁。

### 乐观锁

乐观锁是一种乐观思想，即认为读多写少，遇到并发写的可能性低，每次去拿数据的时候都认为别人不会修改，所以不会上锁，但是在更新的时候会判断一下在此期间别人有没有去更新这个数据，采取在写时先读出当前版本号，然后加锁操作（比较跟上一次的版本号，如果一样则更新），如果失败则要重复读-比较-写的操作。

**java中的乐观锁基本都是通过CAS操作实现的，CAS是一种更新的原子操作，比较当前值跟传入值是否一样，一样则更新，否则失败。**

### 悲观锁

悲观锁是就是悲观思想，即认为写多，遇到并发写的可能性高，每次去拿数据的时候都认为别人会修改，所以每次在读写数据的时候都会上锁，这样别人想读写这个数据就会block直到拿到锁。java中的悲观锁就是Synchronized,AQS框架下的锁则是先尝试cas乐观锁去获取锁，获取不到，才会转换为悲观锁，如RetreenLock。

## 基础知识之二：java线程阻塞的代价

java的线程是映射到操作系统原生线程之上的，如果要阻塞或唤醒一个线程就需要操作系统介入，需要在户态与核心态之间切换，这种切换会消耗大量的系统资源，因为用户态与内核态都有各自专用的内存空间，专用的寄存器等，用户态切换至内核态需要传递给许多变量、参数给内核，内核也需要保护好用户态在切换时的一些寄存器值、变量等，以便内核态调用结束后切换回用户态继续工作。

> 1. 如果线程状态切换是一个高频操作时，这将会消耗很多CPU处理时间；
> 2. 如果对于那些需要同步的简单的代码块，获取锁挂起操作消耗的时间比用户代码执行的时间还要长，这种同步策略显然非常糟糕的。

synchronized会导致争用不到锁的线程进入阻塞状态，所以说它是java语言中一个重量级的同步操纵，被称为重量级锁，为了缓解上述性能问题，JVM从1.5开始，引入了轻量锁与偏向锁，默认启用了自旋锁，他们都属于乐观锁。

****明确java线程切换的代价，是理解java中各种锁的优缺点的基础之一。****

# Java对象头与Monitor

在JVM中，对象在内存中的布局分为三个区域：

> 对象头
>
> 实例数据
>
> 对其填充



![Java对象](/Users/coderlau/Desktop/Java对象.png)

- 实例变量：存放类的属性数据信息，包括父类的属性信息，如果是数组的实例部分还包括数组的长度，这部分内存按4字节对齐。
- 填充数据：由于虚拟机要求对象起始地址必须是8字节的整数倍。填充数据不是必须存在的，仅仅是为了字节对齐，这点了解即可。
- 而对于顶部，则是Java头对象，它实现synchronized的锁对象的基础。

​               一般而言，synchronized使用的锁对象是存储在Java对象头里的，jvm中采用2个字来存储对象头(如果对象是数组则会分配3个字，多出来的1个字记录的是数组长度)，其主要结构是由Mark Word 和 Class Metadata Address 组成，其结构说明如下表：

| 虚拟机位数 | 对象头结构             | 说明                                                         |
| :--------- | ---------------------- | ------------------------------------------------------------ |
| 32/64bit   | Mark Word              | 存储对象的hashCode、锁对象或分代年龄或者gc标志等             |
| 32/64bit   | Class Metadata Address | 类型指针指向对象的类元数据，JVM通过这个指针确定该对象是哪个类的实例。 |

其中Mark Word在默认情况下存储着对象的HashCode、分代年龄、锁标记位等以下是32位JVM的Mark Word默认存储结构



| 锁状态   | 25bit        | 4bit         | 1bit是否是偏向锁 | 2bit 锁标志位 |
| -------- | ------------ | ------------ | ---------------- | ------------- |
| 无锁状态 | 对象HashCode | 对象分代年龄 | 0                | 01            |

除了上述列出的Mark Word默认存储结构外，还有如下可能变化的结构：

![对象头部分的东西](/Users/coderlau/Desktop/对象头部分的东西.png)

其中轻量级锁和偏向锁是Java 6 对 synchronized 锁进行优化后新增加的，稍后我们会简要分析。这里我们主要分析一下重量级锁也就是通常说synchronized的对象锁，锁标识位为10，其中指针指向的是monitor对象（也称为管程或监视器锁）的起始地址。

对象头：Monitor (每个对象都存在着一个 monitor 与之关联),获得锁就是获得对象关联的Monitor，**对象与其 monitor 之间的关系有存在多种实现方式**，如monitor可以与对象一起创建销毁或当线程试图获取对象锁时自动生成，**但当一个 monitor 被某个线程持有后，它便处于锁定状态。**

## Monitor

在Java虚拟机(HotSpot)中，monitor是由ObjectMonitor实现的，其主要数据结构如下（位于HotSpot虚拟机源码ObjectMonitor.hpp文件，C++实现的）。

```java
ObjectMonitor() {
    _header       = NULL;
    _count        = 0; //记录个数
    _waiters      = 0,
    _recursions   = 0;
    _object       = NULL;
    _owner        = NULL;
    _WaitSet      = NULL; //处于wait状态的线程，会被加入到_WaitSet
    _WaitSetLock  = 0 ;
    _Responsible  = NULL ;
    _succ         = NULL ;
    _cxq          = NULL ;
    FreeNext      = NULL ;
    _EntryList    = NULL ; //处于等待锁block状态的线程，会被加入到该列表
    _SpinFreq     = 0 ;
    _SpinClock    = 0 ;
    OwnerIsThread = 0 ;
  }


```

> ObjectMonitor 中有两个队列， _WaitSet和_EntryList，用来保存ObjectWaiter对象列表（每个等待锁的线程都会被封装成ObjectWaiter对象）_owner指向只有ObjectMonitor对象的线程，_
>
> 1. 当多个线程访问一段同步代码块时，首先会进入_EntryList集合
> 2. 当线程获得对象的Monitor后进入Onwer区域并把monitor中的owner 变量设置为owner变量设置为当前线程，同时monitor中的计数器count加1
> 3. 当线程调用 wait() 方法，将释放当前持有的monitor，owner变量恢复为null，count自减1，同时该线程进入 WaitSe t集合中等待被唤醒。若当前线程执行完毕也将释放monitor(锁)并复位变量的值，以便其他线程进入获取monitor(锁)。

![过程图](/Users/coderlau/Desktop/线程执行过层.png)

由此看来，monitor对象存在于每个Java对象的对象头中(存储的指针的指向)，synchronized锁本质上都是对指定对象相关联的monitor的获取，这个过程是互斥性的，也就是说同一时刻只有一个线程能够成功，其它失败的线程会被阻塞，并放入到同步队列中，进入BLOCKED状态。

# synchronized 原理

## 底层语义原理

现在我们重新定义一个synchronized修饰的同步代码块，在代码块中操作共享变量i，如下

```java
public class SyncCodeBlock {
public int i;
public void syncTask(){
//同步代码库
synchronized (this){
i++;
}

```



编译上述代码并使用javap反编译后得到字节码如下(这里我们省略一部分没有必要的信息)：

```java
Classfile /Users/zejian/Downloads/Java8_Action/src/main/java/com/zejian/concurrencys/SyncCodeBlock.class
  Last modified 2017-6-2; size 426 bytes
  MD5 checksum c80bc322c87b312de760942820b4fed5
  Compiled from "SyncCodeBlock.java"
public class com.zejian.concurrencys.SyncCodeBlock
  minor version: 0
  major version: 52
  flags: ACC_PUBLIC, ACC_SUPER
Constant pool:
  //........省略常量池中数据
  //构造函数
  public com.zejian.concurrencys.SyncCodeBlock();
    descriptor: ()V
    flags: ACC_PUBLIC
    Code:
      stack=1, locals=1, args_size=1
         0: aload_0
         1: invokespecial #1                  // Method java/lang/Object."<init>":()V
         4: return
      LineNumberTable:
        line 7: 0
  //===========主要看看syncTask方法实现================
  public void syncTask();
    descriptor: ()V
    flags: ACC_PUBLIC
    Code:
      stack=3, locals=3, args_size=1
         0: aload_0
         1: dup
         2: astore_1
         3: monitorenter  //注意此处，进入同步方法
         4: aload_0
         5: dup
         6: getfield      #2             // Field i:I
         9: iconst_1
        10: iadd
        11: putfield      #2            // Field i:I
        14: aload_1
        15: monitorexit   //注意此处，退出同步方法
        16: goto          24
        19: astore_2
        20: aload_1
        21: monitorexit //注意此处，退出同步方法
        22: aload_2
        23: athrow
        24: return
      Exception table:
      //省略其他字节码.......
}

```

我们主要关注字节码中的如下代码

```java
3: monitorenter  //进入同步方法
//..........省略其他  
15: monitorexit   //退出同步方法
16: goto          24
//省略其他.......
21: monitorexit //退出同步方法
```

从字节码中可知同步语句块的实现使用的是monitorenter 和 monitorexit 指令，其中monitorenter指令指向同步代码块的开始位置，monitorexit指令则指明同步代码块的结束位置。


### **synchronized 是重量级锁**

> 当锁处于这个状态下，其他线程试图获取锁都会被阻塞住，当持有锁的线程释放锁之后会唤醒这些线程。



**### synchronized的可重入性**

>从互斥锁的设计上来说，当一个线程试图操作一个由其他线程持有的对象锁的临界资源时，将会处于阻塞状态，但当一个线程再次请求自己持有对象锁的临界资源时，这种情况属于重入锁，请求将会成功，在java中synchronized是基于原子性的内部锁机制，是可重入的，因此在一个线程调用synchronized方法的同时在其方法体内部调用该对象另一个synchronized方法，也就是说一个线程得到一个对象锁后再次请求该对象锁，是允许的，这就是synchronized的可重入性




### 代码解释可重入性

````java
public class AccountingSync implements Runnable{
    static AccountingSync instance=new AccountingSync();
    static int i=0;
    static int j=0;
    @Override
    public void run() {
        for(int j=0;j<1000000;j++){

            //this,当前实例对象锁
            synchronized(this){
                i++;
                increase();//synchronized的可重入性
            }
        }
    }

    public synchronized void increase(){
        j++;
    }


    public static void main(String[] args) throws InterruptedException {
        Thread t1=new Thread(instance);
        Thread t2=new Thread(instance);
        t1.start();t2.start();
        t1.join();t2.join();
        System.out.println(i);
    }
}

````

正如代码所演示的，在获取当前实例对象锁后进入synchronized代码块执行同步代码，并在代码块中调用了当前实例对象的另外一个synchronized方法，再次请求当前实例锁时，将被允许，进而执行方法体代码，这就是重入锁最直接的体现，需要特别注意另外一种情况，当子类继承父类时，子类也是可以通过可重入锁调用父类的同步方法。注意由于synchronized是基于monitor实现的，因此每次重入，monitor中的计数器仍会加1



java 虚拟机中的同步(Synchronization)基于进入和退出管程(Monitor)对象实现， 无论是显式同步(有明确的 monitorenter 和 monitorexit 指令,即同步代码块)还是隐式同步都是如此。在 Java 语言中，同步用的最多的地方可能是被 synchronized 修饰的同步方法。同步方法 并不是由 monitorenter 和 monitorexit 指令来实现同步的，而是由方法调用指令读取运行时常量池中方法的 ACC_SYNCHRONIZED 标志来隐式实现的，关于这点，稍后详细分析。下面先来了解一个概念Java对象头，这对深入理解synchronized实现原理非常关键。



synchronized是基于Monitor来实现同步的。对象锁或者对象监控器
Monitor从两个方面来支持线程之间的同步：

   >1. 互斥执行
   >
   >  >2. 协作

1、Java 使用对象锁 ( 使用 synchronized 获得对象锁 ) 保证工作在共享的数据集上的线程互斥执行。** 使用synchronize 就是为了申明获得对象锁**

2、使用 notify/notifyAll/wait 方法来协同不同线程之间的工作。（对象锁，notify 等都是对象本身就具有的）

3. Class和Object都关联了一个Monitor。(the owner 是获得Monitor)

   ![Screen Shot 2018-10-06 at 7.18.04 PM](/Users/coderlau/Desktop/Screen Shot 2018-10-06 at 7.18.04 PM.png)

Monitor的工作原理

- 线程进入同步方法中(synchronized 修饰的代码块)
- 为了继续执行临界区的代码，线程必须获取Monitor锁，如果获取成功，将成为该监视者对象的拥有者，任一时刻内，监视者对象只属于一个活动线程（The Owner）
- 拥有监视者对象的线程可以调用 wait() 进入等待集合（Wait Set），同时释放监视锁，进入等待状态。
- 其他线程调用 notify() / notifyAll() 接口唤醒等待集合中的线程，这些等待的线程需要重新获取监视锁后才能执行 wait() 之后的代码。
- 同步方法执行完毕了，线程退出临界区，并释放监视锁。

# synchronized锁住的是代码还是对象？

答案： 

1. synchronized 锁住的是括号里面的对象，**而不是代码，对于非static 方法，锁住的也是对象就是this**

2. 被synchronized修饰的代码块表示：一个线程在操作该资源时，不允许其他线程操作该资源。

   > 说白了synchronized其实只是一个标志，做一个显示，告诉你这块代码要怎么了， 底层其实 Monitor 对象锁来实现的Monitorenter 和monitorexit等cpu指令来执行

# synchronized的评价

### 早起的synchronized 

同时我们还必须注意到的是在Java早期版本中，synchronized属于重量级锁，效率低下，因为**监视器锁（monitor）是依赖于底层的操作系统的Mutex Lock**来实现的，而**操作系统实现线程之间的切换**时需要从用户态转换到内核心态，这个状态之间的转换需要相对比较长的时间，时间成本相对较高，这也是为什么早期的synchronized效率低的原因。

### 后期的synchrionized

庆幸的是在Java 6之后Java官方对从JVM层面对synchronized较大优化，所以现在的synchronized锁效率也优化得很不错了，Java 6之后，为了减少获得锁和释放锁所带来的性能消耗，引入了轻量级锁和偏向锁，接下来我们将简单了解一下Java官方在JVM层面对synchronized锁的优化。

# java虚拟机对synchronized 的优化

问题： JDK 1.6哪些对锁做了哪些优化？

下面就是该问题的答案

java中每个对象都可作为锁，锁有四种级别，每个对象一开始都是无锁的，随着线程间争夺锁，越激烈，锁的级别越高，并且锁只能升级不能降级。锁的实现机制与java对象头息息相关，锁的所有信息，都记录在java的对象头中（上面讲过）

- 锁一共有四种状态：
  - 无锁状态
  - 偏向锁
  - 轻量级锁
  - 重量级锁(synchronized锁)



> 随着锁的竞争，锁可以从偏向锁升级到轻量级锁，再升级的重量级锁，但是锁的升级是单向的，也就是说只能从低到高升级，不会出现锁的降级，

不同的锁有不同特点，每种锁只有在其特定的场景下，才会有出色的表现，java中没有哪种锁能够在所有情况下都能有出色的效率，引入这么多锁的原因就是为了应对不同的情况；

重量级锁是悲观锁的一种，自旋锁、轻量级锁与偏向锁属于乐观锁，所以现在你就能够大致理解了他们的适用范围，但是具体如何使用这几种锁呢，

关于重量级锁，前面我们已详细分析过，下面我们将介绍偏向锁和轻量级锁以及JVM的其他优化手段。

## 偏向锁

### 引入背景

偏向锁时Java6 之后加入的新锁，它是一种针对加锁操作的优化手段。经过研究发现，在大多数情况下，锁不仅不存在多线程竞争，而且总是由同一线程多次获得锁，因此为了减少**同一线程多次获得锁的代价而引入偏向锁**。

### 核心思想

如果一个线程获得了一个锁，那么锁就进入偏向模式， 此时Mark Word 的结构也变为偏向锁结构，**当这个线程再次请求锁时，无需再做任何同步操作，即获取锁的过程，这样就省去了大量有关锁申请的操作，从而也就提供程序的性能。**

### 使用场景

所以，对于没有锁竞争的场合，偏向锁有很好的优化效果，毕竟极有可能连续多次是同一个线程申请相同的锁。但是对于锁竞争比较激烈的场合，偏向锁就失效了，因为这样场合极有可能每次申请锁的线程都是不相同的，因此这种场合下不应该使用偏向锁，否则会得不偿失，需要注意的是，偏向锁失败后，并不会立即膨胀为重量级锁，而是先升级为轻量级锁。下面我们接着了解轻量级锁。

> 比较适合用在线程竞争激烈程度比较小的场合,不适用于竞争比较激烈的环境。如果线程竞争比较激烈，那么应该禁用偏向锁。

## 轻量级锁

### 背景

倘若偏向锁失败，虚拟机并不会立即升级为重量级锁，它还会尝试使用一种称为轻量级锁的优化手段(1.6之后加入的)，此时Mark Word 的结构也变为轻量级锁的结构。

### 作用

轻量级锁能够提升程序性能的依据是“对绝大部分的锁，在整个同步周期内都不存在竞争”，注意这是经验数据。需要了解的是，轻量级锁所适应的场景是线程交替执行同步块的场合，如果存在同一时间访问同一锁的场合，就会导致轻量级锁膨胀为重量级锁。

## 自旋锁
自旋锁是指当一个线程尝试获取某个锁时，如果该锁已被其他线程占用，就一直循环检测锁是否被释放，而不是进入线程挂起或睡眠状态。
### 背景

轻量级锁失败后，虚拟机为了避免线程真实地在操作系统层面挂起，还会进行一项称为自旋锁的优化手段。这是基于在大多数情况下，线程持有锁的时间都不会太长，如果直接挂起操作系统层面的线程可能会得不偿失，毕竟操作系统实现线程之间的切换时需要从用户态转换到核心态，这个状态之间的转换需要相对比较长的时间，时间成本相对较高，因此自旋锁会假设在不久将来，当前的线程可以获得锁，因此虚拟机会让当前想要获取锁的线程做几个空循环(这也是称为自旋的原因)，一般不会太久，可能是50个循环或100循环，在经过若干次循环后，如果得到锁，就顺利进入临界区。如果还不能获得锁，那就会将线程在操作系统层面挂起，这就是自旋锁的优化方式，这种方式确实也是可以提升效率的。最后没办法也就只能升级为重量级锁了。
**所谓自旋锁简单来说就是线程通过循环来等待而不是睡眠**
> 自旋锁原理非常简单，如果持有锁的线程能在很短时间内释放锁资源，那么那些等待竞争锁的线程就不需要做内核态和用户态之间的切换进入阻塞挂起状态，它们只需要等一等（自旋），等持有锁的线程释放锁后即可立即获取锁，这样就**避免用户线程和内核的切换的消耗**。
>while(前一个线程的锁还未释放){空循环} 而不是 thread.sleep() 睡眠方式等待
## CHL锁
CLH锁也是一种基于链表的可扩展、高性能、公平的自旋锁，申请线程只在本地变量上自旋，它不断轮询前驱的状态，如果发现前驱释放了锁就结束自旋。(CLH是在前驱节点的属性上自旋)

### 使用

线程自旋是需要消耗cup的，说白了就是让cup在做无用功，如果一直获取不到锁，那线程也不能一直占用cup自旋做无用功，所以需要设定一个自旋等待的最大时间。

> 如果持有锁的线程执行的时间超过自旋等待的最大时间扔没有释放锁，就会导致其它争用锁的线程在最大等待时间内还是获取不到锁，这时争用线程会停止自旋进入阻塞状态。
### 好处
自旋的好处是线程不需要睡眠和唤醒，减小了系统调用的开销。 
## 各种锁比较总结如下情况

```
| 隔离级别                   | 脏读   | 不可重复读 | 幻读   |
| -------------------------- | ------ | ---------- | ------ |
| 未提交读(Read uncommitted) | 可能   | 可能       | 可能   |
| 已提交读(Read committed）  | 不可能 | 可能       | 可能   |
| 可重复读(Repeatable read） | 不可能 | 不可能     | 肯能   |
| 可串行化（Serializable ）  | 不可能 | 不可能     | 不可能 |
```

| 锁       | 优点                                                         | 缺点                                             | 适用场景                             |
| -------- | ------------------------------------------------------------ | ------------------------------------------------ | ------------------------------------ |
| 偏向锁   | 加锁和解锁不需要额外的消耗，和执行非同步方法比仅存在纳秒级的差距。 | 如果线程间存在锁竞争，会带来额外的锁撤销的消耗。 | 适用于只有一个线程访问同步块场景。   |
| 轻量级锁 | 竞争的线程不会阻塞，提高了程序的响应速度                     | 如果始终得不到锁竞争的线程使用自旋会消耗CPU。    | 追求响应时间。同步块执行速度非常快。 |
| 重量级锁 | 线程竞争不使用自旋，不会消耗CPU。                            | 线程阻塞，响应时间缓慢。                         | 追求吞吐量。                         |

# 等待唤醒机制与synchronized

所谓等待唤醒机制本篇主要指的是notify/notifyAll和wait方法，在使用这3个方法时，必须处于synchronized代码块或者synchronized方法中，否则就会抛出IllegalMonitorStateException异常，这是因为调用这几个方法前必须拿到当前对象的监视器monitor对象，也就是说notify/notifyAll和wait方法依赖于monitor对象，在前面的分析中，我们知道monitor 存在于对象头的Mark Word 中(存储monitor引用指针)，而synchronized关键字可以获取 monitor ，这也就是为什么notify/notifyAll和wait方法必须在synchronized代码块或者synchronized方法调用的原因。
使用notify/notifyAll 和wait()方法必须在synchonized代码块内，也就是必须获得对象锁，否则会抛出一异常java.lang.IlegalMonitorStateException的异常

> you must verify that all invokations of the `wait`, `notify` and `notifyAll` methods are taking place only when the calling thread owns the appropriate monitor，  in order to own the  object ‘ s Monitor , you can use
>
> synchronized(object) or synchronized(this) 

比如如下的代码,代码没有在synchronized代码块

```java
public static void main(String[] args) throws InterruptedException 
{
        Object obj = new Object();
        obj.wait();
        obj.notifyAll();}

```

还有一种情况就是，即使是代码块中，如果没有获得对象锁也是不行的情况

```java
public static void main(String[] args) throws InterruptedException{
    Object obj = new Object();
    Object lock =new Object();
    synchronized(lock){
        obj.wait();
        obj.notifyAll()；
    }
}
```

通过上面的代码也可以发现synronized锁住的不是代码块，而是对象锁，*这才是真正的工作机制所在*，倘若是获得锁住代码块的话，上述的代码块肯定是不会抛出异常的才对的

```java
synchronized (obj) {
       obj.wait();
       obj.notify();
       obj.notifyAll();         
 }
```

需要特别理解的一点是，与sleep方法不同的是wait方法调用完成后，线程将被暂停，但wait方法将会释放当前持有的监视器锁(monitor)*(释放cpu资源、释放对象锁)*，直到有线程调用notify/notifyAll方法后方能继续执行，而**sleep方法只让线程休眠(不获得CPU)并不释放锁**。同时**notify/notifyAll方法调用后，并不会马上释放监视器锁**，而是在相应的synchronized(){}/synchronized方法执行结束后才自动释放锁。



**所以，要解决IllegalMonitorStateException这个问题，必须在wait和notifyAll的时候，获得该对象的锁，以保证同步。** 上述异常是一个RunningTimeException[官方文档](https://docs.oracle.com/javase/7/docs/api/java/lang/IllegalMonitorStateException.html)

> 上述异常的情况说明
>
> Thrown to indicate that a thread has attempted to wait on an object's monitor or to notify other threads waiting on an object's monitor without owning the specified monitor.
>
>  

### notifyAll()、notify()、wait()

#### 第一种方式

锁住方法所属的实例对象，使用的使用是使用一个对象

```java
public synchrnoized void method(){
    然后就可以使用 this.notify()
    //或者直接调用notify()
}
```



#### 第二种方式

类锁， 锁定方法所属的实例的class，也叫，如果是使用在单例模式

```  java
public Class Test{
 public static synchronized void method（）{
    //然后调用：Test.class.notify()...
 }
}
```

### 第二种方式

锁住其他对象

````java
public Class Test{
    public Object lock = new Object();
    public static void method(){
        synchronized(lock){
            lock.wait
            locl.notify();
            //锁住的也是对象锁，但是是其他对象，
        }
    }
}
````
[synchronized的四种用法 ](https://blog.csdn.net/sinat_32588261/article/details/72880159)
- 对象
- 方法，不可被继承
- 类
- 静态方法
### 英文解释这部情况

A thread becomes the owner of the object 's   monitor in one of the three ways:



1. By executing a synchronized instance method  of the object;
2. By executiong the body of a synchronized statement that synchronized on the object.
3. For objects of type class, by execting a synchronized static method of that class



**一定要回答到这点(才能叫做回到了synchronized原理)**



## # 一定要看该链接的情况

【参考链接](https://blog.csdn.net/javazejian/article/details/72828483#synchronized%E5%BA%95%E5%B1%82%E8%AF%AD%E4%B9%89%E5%8E%9F%E7%90%86)
# sychronized 锁的局限性

在JDK 1.6之前，synchronized这个重量级锁其性能一直都是较为低下，虽然在1.6后，进行大量的锁优化策略（【死磕Java并发】—–深入分析synchronized的实现原理）,但是与Lock相比synchronized还是存在一些缺陷的：虽然synchronized提供了便捷性的隐式获取锁释放锁机制（基于JVM机制），但是它却缺少了获取锁与释放锁的可操作性，可中断、超时获取锁，且它为独占式在高并发场景下性能大打折扣。


# synchronized 作用在静态方法和非静态方法的简单区别
1. 对象锁
2. 类锁 ，class对象