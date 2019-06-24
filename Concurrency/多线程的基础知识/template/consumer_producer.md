# 生产者和消费者
使用多种方式实现生成者和消费者，主要注意的一个点就是如何保持同步
当队列为空的时候和队列为满的是， 对线程进行一个阻塞，


**主要有四种方式实现消费者和生产者问题**

1. synchronized, 线程通信方式wait()、notifyAll(),notify()
2. ReentrantLock + Condition， signal(), await()
3. BlockingQueue， 不用消费者和生产者之间的通知机制，因为BlockingQueue 本质就是对ReetrantLock和condition的封装


**实现关键**

1. 生产者生产的数量要等于大于产品容器仓库的存放容量
2. 消费者消费的数量要等于大于产品容器仓库的存放容量

生产或者消费多次，可以使用for 循环实现或者while() 循环实现

只有这样才能产生并发同步的问题，同步类的代码体都是在产品容器类当中实现的


**不满足条件**
1. wait(), notify() ,signal() 都是采用while 循环不停等待

**满足条件**
1. 满足条件对变量进行正常的消费或者生产之后就要改变产品容器的计数状态，并通知其他线程

# synchronize

该种方式实现生成者和消费者对象,同步、

1. 产品，可以定义为数字类，或者字符串类，可以自己封装一个类，也可以使用原始类
1. 产品容器类，装产品，普通队列或者数组**有大小容量限制**， 定义两个方法，队列添加产品(produce())，队列删除产品(consume()),produce()和consume()采用synchronized 修饰，
    
    - consume()，往容器类的数据结构删除产品
    - produce(), 往容器类的数据结构添加产品
   
>可以使用容器，数组等
  
 
2. 生产者线程， 有一个容器类，构造函数
    - public 构造函数(容器类)
    - 调用 容器器类的produce()方法，
3. 消费者线程， 有一个构造函数，调用容器类的consume()
    - public 构造函数(容器类)
    - 调用 容器器类的consume()方法，
    
4. 主函数，定义一个容器类，生产者和消费者使用同一个容器初始化，

实现上述五个步骤可以放到一个代码段里面，也可以分开好多个文件来实现



简化版本的代码体

class consumer 动作的声明
```java 


先判断不满足条件的，然后进行等待，然后再在满足条件的前提条件下面进行 notifyAll()方法的情况 

while(true)
{
        synchronized(锁住同步资源)
        {
        
        while(不满足条件) {
         wait() 方法
         
         
        }
        notify()
}


其中while循环可以放在多个地方

```



```java
public class ProducerConsumerInJava { 
    public static void main(String args[]) 
    { 
        System.out.println("How to use wait and notify method in Java"); 
        System.out.println("Solving Producer Consumper Problem");
        Queue<Integer> buffer = new LinkedList<>(); 
        int maxSize = 10; 
        Thread producer = new Producer(buffer, maxSize, "PRODUCER"); 
        Thread consumer = new Consumer(buffer, maxSize, "CONSUMER"); 
        producer.start(); 
        consumer.start(); 
    } 
} 

class Producer extends Thread 
{ 
    private Queue<Integer> queue; 
    private int maxSize; 
    public Producer(Queue<Integer> queue, int maxSize, String name)
    { super(name); 
    this.queue = queue; 
    this.maxSize = maxSize; } 
    @Override
     public void run() { 
        //一直生产
        while (true) { 
            
            // synchronized锁机制和和while 条件判断的顺序关系，我们统一使用
            synchronized (queue) 
            { 
                // 
                while (queue.size() == maxSize) 
                {   try 
                    { 
                        System.out .println("Queue is full, " + "Producer thread waiting for " + "consumer to take something from queue");
                        queue.wait();
                    } 
                    catch (Exception ex) 
                    { 
                    ex.printStackTrace(); 
                    } 
                } 
                //不满条件了
                Random random = new Random(); 
                int i = random.nextInt(); 
                System.out.println("Producing value : " + i); 
                queue.add(i); 
                queue.notifyAll();
            }
                
                        }
                    }
                }

                
class Consumer extends Thread {
    // 共享资源
    private Queue<Integer> queue; 
    // maxSize这个数据其实只对生产者其作用
    // 0 其实就是消费者限制的因素
    private int maxSize;
    public Consumer(Queue<Integer> queue, int maxSize, String name)
    {   
        super(name); 
        this.queue = queue; 
        this.maxSize = maxSize; 
    } 
    @Override
    // 消费者该做的事情，操作共享资源
     public void run() 
    {
        while (true) 
        { 
            synchronized (queue) 
               { 
                   // 不满足条件，先调用wait()函数
                  while (queue.isEmpty())
                  { 
                      System.out.println("Queue is empty," + "Consumer thread is waiting" + " for producer thread to put something in queue"); 
                      try { 
                          queue.wait();
                      } 
                      catch (Exception ex) 
                      { ex.printStackTrace(); } 
                  } 
                  //满足添加进行添加
                  System.out.println("Consuming value : " + 
                  queue.remove()); 
                  queue.notifyAll(); 
               } 
        } 
    } 
} 

```

也可以将生产者线程和消费者线程抽象为一个， 封装成为方法

```java

public class Threadexample
{
    public static void main(String [] args){
        final Pc pc =new Pc();
    }
    Thread t1=new Thread(new Runnable() {
       
        @Override
        public void run(){
            try{
                pc.produce();
            }
            catch(InternalError e){
                e.printStackTrace();
            }
            
        }
    });
  
   Thread t2 = new Thread(new Runnable() 
            { 
                @Override
                public void run() 
                { 
                    try
                    { 
                        pc.consume(); 
                    } 
                    catch(InterruptedException e) 
                    { 
                        e.printStackTrace(); 
                    } 
                } 
            }); 
   
      class Pc{
          LinkedList<Integer> list = new LinkedList<>();
          int capacity =2;
          public void produce() throws InterruptedException{
              int value =0;
              while(true){
                  synchronized (list){
                      while(list.size()==capacity){
                          wait();
                          System.out.println(""Producer produced-"+value)
                          list.add(value++);
                          notifyAll();
                          Thread.sleep(1000);
                );
                      }
                  }
              }
          }
          public void consumer() throws InterruptedException{
              while(true){
                  synchronized (list){
                      while(list.size()==0)
                          wait();
                    
                      int val = list.removeFirst();
                        System.out.println(val);
                        notifyAll();
                        Thread.sleep(1000);
                      
                  }
              }
          }
      }
}

```


# 阻塞队列

共享一个阻塞队列， 队列是怎么样的情况在这里我们不用管，因为阻塞队列本身已经实现了，通知线程之间的通知机制wait(),notify()都等了，所以我们不
- 当队列为空，没有消息的时候take()阻塞
- 当队列为满，put()阻塞
- 采用阻塞队列，其实也是需要线程之间通信的， 只不过换了一种方式,比如Conditon.await()线程等待机制（如果是使用synchronized关键字的话， 如果队列当中没有准备好物品， 消费者线程是需要进行等待的） 和Condition.singal()通知机制

## 1. 消息
 
 ```java
 package com.journaldev.concurrency;
  public class Message {
      private String msg;
      public Message(String str){
          this.msg=str;
      }
        public String getMsg() {
         return msg;
     }
}
 ```
## 2. 生产者
Producer这个类会产生消息并将其放入队列中

```java

package com.journaldev.concurrency;
import java.util.concurrent.BlockingQueue;
public class Producer implements Runnable {
    private BlockingQueue<Message> queue;
    public Producer(BlockingQueue<Message> q){
        this.queue=q;
    }
    @Override
    // 生产者不断生产物品，采取for 循环表示
    public void run() {
        //生产消息，for 循环是一直在生产消息的
        for(int i=0; i<100; i++){
            Message msg = new Message(""+i);
            try {
            // 延迟一下即可
                Thread.sleep(i);
                queue.put(msg);
                System.out.println("Produced "+msg.getMsg());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //添加退出消息，最后一个消息
        // 其实这里也可以不设置停止， 然后生产者一直在生产
        Message msg = new Message("exit");
        try {
            queue.put(msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

``` 

## 3. 消费者

Consumer类会从队列获取消息进行处理。如果获取的是退出消息则结

```java
private BlockingQueue<Message> queue;
     
    public Consumer(BlockingQueue<Message> q){
        this.queue=q;
    }
 
    @Override
    public void run() {
        try{
            Message msg;
            //获取并处理消息直到接收到“exit”消息，一直获取消息
            
           //  一般消费者我们都是采用while 循环进行消费，主要满足条件就不断地进行消费， 所以一直采用
           
           //如果上面生产者没有停止生产那段代码的话， 其实
           
           // while(true) 如果生产者不中断生产即可
           // 设置为while(true)
            while((msg = queue.take()).getMsg() !="exit"){
            Thread.sleep(10);
            System.out.println("Consumed "+msg.getMsg());
            }
        }catch(InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```

## 4.主程序

主程序创建一个消费者和一个生产者

```java
public class test{
    // 共享同一个队列；
    BlockingQueue<Message> queue= new BlockingQueue<Message>(10);
  
    Consumer consumer = new Consumer(queue);
    Producer producer = new Produer(queue);
    // 生产者消息
    new Thread(producer).start();
    // 
    new Thread(consumer).start();
    System.out.println("Producer and Consumer has been started");
}
```

## 简化版面

```java
package ProducerConsumer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
public class ProducerConsumerWithBlockingQueue {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Integer> blockingQueue = new LinkedBlockingDeque<>(2);
        Thread producerThread = new Thread(() -> {
            try {
                int value = 0;
                while (true) {
                    blockingQueue.put(value);
                    System.out.println("Produced " + value);
                    value++;
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread consumerThread = new Thread(() -> {
            try {
                while (true) {
                    int value = blockingQueue.take();
                    System.out.println("Consume " + value);
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        producerThread.start();
        consumerThread.start();
        producerThread.join();
        consumerThread.start();
        }
     }
```

# 使用ReetranLock 实现生成者和消费者问题


其实reetrantlock 编写生产者和消费者同synchronized一样，只是将synchronzie改成lock 和unlock ,并且通过两个条件进行

同样因为使用了try方法，所以

- notfull 
- notempty.await 

## 仓库容器类

负责同步类代码编写

```java
class SelfQueue{
    int max = 5; // 变量进行控制
    // 实际的存储结构问题
    LinkedList<Integer> ProdLine = new LinkedList<Integer>();
    Lock lock = new ReentrantLock(); 
    Condition notfull = lock.newCondition();  
    Condition notempty = lock.newCondition();
   //不定义产品类，直接使用原始数据类型
    public void produce(int ProdRandom){       
        try {
            lock.lock();
            // 不满足条件就一直等待
            while(max == ProdLine.size()){
                System.out.println("存储量达到上限，请等待");
                // 不满条件所以要等待
                notfull.await();
            }
            // 满足条件就进行改变
            ProdLine.add(ProdRandom);
            notempty.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally{
            lock.unlock();
        }
    } 

    public void consume(){  
        int m = 0;
        
        try {
            lock.lock();
            while(ProdLine.size() == 0){
                System.out.println("队列是空的，请稍候");
                // notempty 要等待
                notempty.await();
            }
             m = ProdLine.removeFirst();
             notfull.signal(); 
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }finally{
                        lock.unlock();
                        return m;
                    }
                }
            }
            
          
```


## 生产者


```java
class Producer implements Runnable{
    private final SelfQueue selfqueue;

    public Producer(SelfQueue selfqueue) {
          this.selfqueue = selfqueue;
    }

    public void run() {
      // 消费的容量大于仓库容量，不进行代码同步控制 大于5 
      for (int i = 0; i < 10; i++) {
            Random random = new Random();
            int ProdRandom=random.nextInt(10);
            System.out.println("Produced: " + ProdRandom);
            selfqueue.produce(ProdRandom);          
      }
  }
}

```

## 消费者

```java
//消费者
class Consumer implements Runnable{
    private final SelfQueue selfqueue;

    public Consumer(SelfQueue selfqueue) {
          this.selfqueue = selfqueue;
    }

    public void run() {
        // 不断进行消费， 让其他资源的锁进行控制即可
      while(true) {
              System.out.println("Consumed: "+ selfqueue.consume());
      }
  }
}

```

## 主程序

```java
public class PessimisticLockPattern {
    public static void main(String[] args){     
         SelfQueue selfqueue = new SelfQueue();

         //创建生产者线程和消费者线程
         Thread prodThread = new Thread(new Producer(selfqueue));
         Thread consThread = new Thread(new Consumer(selfqueue));

         //启动生产者线程和消费者线程
         prodThread.start();
         consThread.start();
    }
}

```

# 注意事项

其实我们可以在consume，produce端代码处都设置成为while(),不断进行生产或者消费的，而不用设置多少次的情况，
在所有的情况当中直接使用阻塞队列是最方便的情况，什么都不用控制就可以了，直接不用进行判断仓库是否为空或者数据为，所有
 
# 参考链接

[JAVA多线程用lock、synchronized、阻塞队列](https://blog.csdn.net/antony9118/article/details/51500278)

[Java实现生产者消费者问题与读者写者问题](https://my.oschina.net/hosee/blog/485121)

[How to use wait, notify and notifyAll in Java](https://javarevisited.blogspot.com/2015/07/how-to-use-wait-notify-and-notifyall-in.html)