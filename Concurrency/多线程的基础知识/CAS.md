#  cas
 
 要记住三个参数乐观锁的使用的机制就是CAS。 
        
    在CAS方法中，CAS有三个操作数，内存值V，旧的预期值E，要修改的新值U。当且仅当预期值E和内存值V相等时，将内存值V修改为U，否则什么都不做。

CAS的缺点：

CAS虽然很高效的解决了原子操作问题，但是CAS仍然存在三大问题。

循环时间长开销很大。
只能保证一个共享变量的原子操作。
ABA问题。

>CAS says “I think V should have the value A; if it does, put B there, otherwise don’t change it but tell me I was wrong.

自己的理解，就是为何在这里需要三个值， 


# 例子Atomic类
在Atomic 原子类实现

```java
public final int incrementAndGet(){
   // 线程一直循环， 如果不成功，
    for(;;){
        int current = getValue();  // current 是 Value 当前的值
        int next = value + 1;      // 期望值next
        if(compareAndSet(current,next)){   
            return next;
        }
    }
}

 public final boolean compareAndSet(int expect, int update) {
        return unsafe.compareAndSwapInt(this, valueOffset, expect, update);
    }

```



这段代码是一个无限循环，也就是CAS的自旋(线程不会阻塞)。循环体当中做了三件事：

1.获取当前值。

2.当前值+1，计算出目标值。

3.进行CAS操作，如果成功则跳出循环，如果失败则重复上述步骤。


**如果保证**

上面如何保证从内存取到最新的值，那么就是get 方法该做的事情了;value 

```java
//这个方法的作用是获取变量的当前值。

private final int get(){
    return value
    }

private volatile int value;
```
>如何保证获得的当前值是内存中的最新值呢？很简单，用volatile关键字来保证。有关volatile关键字的知识

自己的理解

单独看上述代码，就是如果是一个线程在该过程当中执行，

# ABA 问题
 可以发现，CAS实现的过程是先取出内存中某时刻的数据，在下一时刻比较并替换，那么在这个时间差会导致数据的变化，此时就会导致出现“ABA”问题。 
 
1. 什么是”ABA”问题？ 
 
 比如说一个线程one从内存位置V中取出A，这时候另一个线程two也从内存中取出A，并且two进行了一些操作变成了B，然后two又将V位置的数据变成A，这时候线程one进行CAS操作发现内存中仍然是A，然后one操作成功。 
 尽管线程one的CAS操作成功，但是不代表这个过程就是没有问题的。
 
 2. 解决
 
使用版本号AtomicStampedReference 或者AtomicMarkedReference [E,int stamp]二元组进行标志,
 
