# Executors
主要提供的几种静态工厂方法,

Executors 本质还是调用ThreadPoolExecutors构造方法，只不过当中使用的参数不一样， 其中大部分是使用LinkedBlockingQueue


主要注意的是该种方法等的基础知识

newCachedThreadPool：使用SynchronousQueue作为阻塞队列，队列无界，线程的空闲时限为60秒。这种类型的线程池非常适用IO密集的服务，因为IO请求具有密集、数量巨大、不持续、服务器端CPU等待IO响应时间长的特点。服务器端为了能提高CPU的使用率就应该为每个IO请求都创建一个线程，以免CPU因为等待IO响应而空闲。




[Executors类中创建线程池的几种方法的](https://blog.csdn.net/u010412719/article/details/52489843)


[Executors和ThreadPoolExecutor线程池](https://blog.csdn.net/xlxxcc/article/details/52108534)