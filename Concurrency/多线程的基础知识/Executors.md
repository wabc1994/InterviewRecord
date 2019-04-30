# Executors
主要提供的几种静态工厂方法,

Executors 本质还是调用ThreadPoolExecutors构造方法，只不过当中使用的参数不一样， 其中大部分是使用LinkedBlockingQueue


主要注意的是该种方法等的基础知识

newCachedThreadPool：使用SynchronousQueue作为阻塞队列，队列无界，线程的空闲时限为60秒。这种类型的线程池非常适用IO密集的服务，因为IO请求具有密集、数量巨大、不持续、服务器端CPU等待IO响应时间长的特点。服务器端为了能提高CPU的使用率就应该为每个IO请求都创建一个线程，以免CPU因为等待IO响应而空闲。


## ThreadPoolExecutor

七大参数设置

1. corePoolSize  核心线程池数

2. maximumPoolSize：线程池允许的最大线程数;,可以把这部分线程比喻为外包，只有在特定的情况下才使用，然后同时约定了在空闲时候线程的存活时间，  max-core 外包


3. keepalivetime :指的是空闲线程结束的超时时间; 当核心线程池数量和最大线程数量一致的时候，该参数是不起作用的


4. unit ：是一个枚举，表示 keepAliveTime 的单位;

5. workQueue：表示存放任务的BlockingQueue<Runnable队列。

6. threadFactory：每个线程创建的地方 ，可以给线程起个好听的名字，设置个优先级啥的


7. handler：饱和策略，大家都很忙，咋办呢，有四种策略 

## JDK自带线程池




#### newFixedThreadPool
1. newFixedThreadPool**(固定大小的线程池)** max= core 外包线程等于0，当线程池中的线程数超过核心线程数后，任务都会被放到阻塞队列中，采用LinkedBlockingQueue,用的是默认容量 Integer.MAX_VALUE，相当于没有上限。
                                                                                              
FixedThreadPool 用于负载比较重的服务器，为了资源的合理利用，需要限制当前线程数量                                                                                   

 keepAliveTime 为 0，也就是多余的空余线程会被立即终止（由于这里没有多余线程，这个参数也没什么意义了）

#### SingleThreadPool
2. **(单一线程线程池)** 用于串行执行任务的场景，每个任务必须按顺序执行，不需要并发执行。

使用场合：SingleThreadExecutor 用于串行执行任务的场景，每个任务必须按顺序执行，不需要并发执行

#### newCachedThreadPool

3. newCachedThreadPool**(缓存线程池)**, 核心线程数量为0，非核心线程数量无上限，也就是全部都是外包，但是每个线程的空闲时间为60s，

使用的阻塞队列是SynchronousQueue, 队列大小为0，只起到传递任务，并不会保存，

比较适合：用于并发执行大量短期的小任务，或者是负载较轻的服务器

#### newScheduledThreadPool

线程数量最多为Integer.MAX_VALUE, 使用DelayedWorkQueue 作为任务队列。主要是为了执行定时任务以及周期任务

优先级队列DelayedWorkQueue，证添加到队列中的任务，会按照任务的延时时间进行排序，延时时间少的任务首先被获取。


### 阻塞队列

1. ArrayBlockingQueue 基于数组、有界，按 FIFO（先进先出）


2. LinkedBlockingQueue 基于链表，按FIFO （先进先出） 排序元素，Executors.newFixedThreadPool() 使用了这个队列

3. SynchronousQueue：具有优先级的、无限阻塞队列  Executors.newCachedThreadPool使用了这个队列


4. PriorityBlockingQueue 具有优先级队列，无阻塞队列


5. DelayedWorkQueue 延迟优先级队列，Executor.newScheduledThreadPool 使用该队列，底层是一个堆，每个堆元素是一个延时任务，延时时间少的任务首先被获取。
>延迟队列DelayQueue是一个无界阻塞队列，它的队列元素只能在该元素的延迟已经结束或者说过期才能被出


## 参考链接

[Executors类中创建线程池的几种方法的](https://blog.csdn.net/u010412719/article/details/52489843)


[Executors和ThreadPoolExecutor线程池](https://blog.csdn.net/xlxxcc/article/details/52108534)