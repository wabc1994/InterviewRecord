# join
线程等待操作：主线程等待子线程执行完毕，子线程执行的耗时大主线程

6.Thread.join详解
# 为什么要用join()方法
在很多情况下，主线程生成并起动了子线程，如果子线程里要进行大量的耗时的运算，主线程往往将于子线程之前结束，但是如果主线程处理完其他的事务后，需要用到子线程的处理结果，也就是主线程需要等待子线程执行完成之后再结束，这个时候就要用到join()方法了

**主线程需要等待子线程的执行结果**

我等你执行的结果操作哈

# join方法的作用

JDK中对join方法解释为：“等待该线程终止”，换句话说就是：”当前线程等待子线程的终止“。也就是在子线程调用了join()方法后面的代码，只有等到子线程结束了当前线程才能执行。
主要作用是同步，它可以使得线程之间的并行执行变为串行执行。在A线程中调用了B线程的join()方法时，表示只有当B线程执行完毕时，A线程才能继续执行。 


# 参数

join()方法中如果传入参数，则表示这样的意思：

如果A线程中掉用B线程的join(10)，则表示A线程会等待B线程执行10毫秒，10毫秒过后，A、B线程并行执行。

需要注意的是，jdk规定，join(0)的意思不是A线程等待B线程0秒，而是A线程等待B线程无限时间，直到B线程执行完毕，即join(0)等价于join()。

join方法的原理就是调用相应线程的wait方法进行等待操作的，例如A线程中调用了B线程的join方法，则相当于在A线程wait方法，当B线程执行完（或者到达等待时间），B线程会自动调用自身的notifyAll方法唤醒A线程，从而达到同步的目的。

**wait()和notifyAll()配合使用**
wait() 是object()所有的，wait()是

# 源码
判断核心 while(isAlive){ wait();}

```java
public final synchronized void join(long millis)
    throws InterruptedException {
        long base = System.currentTimeMillis();
        long now = 0;

        if (millis < 0) {
            throw new IllegalArgumentException("timeout value is negative");
        }
       //参数为0，调用Object.wait(0)等待
        if (millis == 0) {
            while (isAlive()) {
                wait(0);
            }
        } else {
            while (isAlive()) {
                long delay = millis - now;
                if (delay <= 0) {
                    break;
                }
                wait(delay);//参数非0，调用Object.wait(time)等待
                now = System.currentTimeMillis() - base;
            }
        }
    }
```

```java
主函数(主线程){
    定义一个变量; // (类似于互斥变量，孩子线程和和主线程都要使用该共享变量)
    孩子线程操作该要操作变量
}

```

# 孩子线程调用wait(),真正wait()的 是主线程
在上述例子中，主线程调用子线程对象的join()方法，因此主线程在此位置需要释放锁，并进行等待。
wait() 需要释放对象锁，