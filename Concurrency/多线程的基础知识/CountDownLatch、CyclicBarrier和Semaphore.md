# 功能
CountDownLatch这个类能够使一个线程等待其他线程(一个线程组)完成一些事件后主线程再执行。例如，应用程序的主线程希望在负责启动框架服务的线程已经启动所有的框架服务之后再执行。
[参考链接](http://w

# 在这里主要讲解如何使用这个同步类

就是在代码中如何书写这类同步方法代码体，
套路，注意这只是一种写法， 还有很多种写法


# 两个的区别
CountDownLatch 强度一个线程对另一个操作(这些操作由其他一个或多个线程完成)
>CountDownLatch: A synchronization aid that allows one or more threads to wait until a set of operations being performed in other threads completes.
 
CycliBarrier  强度一个线程组互相等待,地位相同
>CyclicBarrier : A synchronization aid that allows a set of threads to all wait for each other to reach a common barrier point.

可否重用也是另外一个区别


# 代码体一
## 主函数
定义一个同步类对象， 利用线程类中定义多个线程类对象， 都使用同一个同步器进行构造，

## 线程类 
线程类可以是实现了Runnable,接口
一般而言都是在线程类当中定义一个同步类， 线程类的属性包括一个同步类对象， 然后构造参数




## 案例
```java
public class Test {
    // 主函数
    public static void main(String[] args) {
        int N = 4;
        CyclicBarrier barrier  = new CyclicBarrier(N);
        for(int i=0;i<N;i++)
            // 所有的线程都是使用同一个东西，这个变量也类似于共享变量吧， 大家都知道这个类的存在
            new Writer(barrier).start();
        
    }
    //线程类
    static class Writer extends Thread{
        // 同步器对象
            private CyclicBarrier cyclicBarrier;
            // 构造函数
            public Writer(CyclicBarrier cyclicBarrier) {
                this.cyclicBarrier = cyclicBarrier;
            }
     // 执行任务当中， 使用线程同步类
            @Override
            public void run() {
                System.out.println("线程"+Thread.currentThread().getName()+"正在写入数据...");
                try {
                    Thread.sleep(5000);      //以睡眠来模拟写入数据操作
                    System.out.println("线程"+Thread.currentThread().getName()+"写入数据完毕，等待其他线程写入完毕");
                    
                    //一个线程执行完任务,后， 同步器对象回环栅栏就是调用一次await(), 表示当前线程进入等待状态
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }catch(BrokenBarrierException e){
                    e.printStackTrace();
                }
                System.out.println("所有线程写入完毕，继续处理其他任务...");
            }
        }
    }
```

# 代码二

没有线程类， 没有构造器， 直接在线程类执行任务前后，对同步类对象调用相关的函数
```java
public class Test {
     public static void main(String[] args) {   
         final CountDownLatch latch = new CountDownLatch(2);
          
         new Thread(){
             public void run() {
                 try {
                     System.out.println("子线程"+Thread.currentThread().getName()+"正在执行");
                    Thread.sleep(3000);
                    System.out.println("子线程"+Thread.currentThread().getName()+"执行完毕");
                   //每个线程完成一个事件后都减少一次
                    latch.countDown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
             };
         }.start();
          
         new Thread(){
             public void run() {
                 try {
                     System.out.println("子线程"+Thread.currentThread().getName()+"正在执行");
                     Thread.sleep(3000);
                     System.out.println("子线程"+Thread.currentThread().getName()+"执行完毕");
                       //每个线程完成一个事件后都减少一次
                     latch.countDown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
             };
         }.start();
          
         try {
             System.out.println("等待2个子线程执行完毕...");
             // 所有的任务线程执行完后， 才在主程序当中执行该await()体
            latch.await();
            System.out.println("2个子线程已经执行完毕");
            System.out.println("继续执行主线程");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
     }
}
```
