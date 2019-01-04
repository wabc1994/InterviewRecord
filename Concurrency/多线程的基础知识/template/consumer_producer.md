# 生产者和消费者
使用多种方式实现生成者和消费者，主要注意的一个点就是如何保持同步
当队列为空的时候和队列为满的是， 对线程进行一个阻塞，

1.当队列为空，阻塞读取线程

2.当队列满，阻塞写线程


## synchronize
该种方式实现生成者和消费者对象,同步
1. 普通队
2. 生产者线程， queue.offer()
3. 消费者线程， queue.poll()
4. 生产者和消费者操作同一个队列


 
```java
// 生产者
public class Consumer implements Runnable{
    private BlockingQueue<Message>queue;
    public Consumer(BlockingQueue<Message> q){
        this.queue=q;
      }
      @Override
      public void run(){
         
      }
   }
```




## 阻塞队列

同样是共享一个阻塞队列， 队列是怎么样的情况在这里我们不用管，因为阻塞队列本身已经实现了
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


# 参考链接

[JAVA多线程（五）用lock、synchronized、阻塞队列](https://blog.csdn.net/antony9118/article/details/51500278)

