# locks 
先理清整个锁的基本机制

java.util.concurrent.locks 是bao形式

Lock 和ReadWriteLock是两个接口，其中3是对2的继承接口

1. java.util.concurrent.locks.Lock

2. java.util.concurrent.locks.ReadWriteLock

3. java.util.concurrent.locks.ReentrantReadWriteLock
   默认是非公平模式

  - readLock() 返回一个读锁
  - writeLokc() 返回一个写锁

  基本使用
  
```java
public interface ReadWriteLock {
    /**
     * 返回读锁
     */
    Lock readLock();
    /**
     * 返回写锁
     */
    Lock writeLock();
}
ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
```


# 面试当中叫你如何是吸纳一个读写锁的实时

## 基本流程
1. 写一个读写锁类ReadWriteLock
2. 两个线程， 一个写线程(一个共享变量，一个锁)，一个读线程(一个锁，一个共享变量)（），线程的run方法当中需要获得锁
3. 主程序, 定个多个线程，定义一个共享变量，定义一个锁, 然后使用这些变量初始化这些锁，保证这类线程都共享这把锁和变量，所以这两个东西在内存当中只能创建一个。

## 两个int实现读写锁

上述步骤的关键是是如何实现步骤一种的锁机制， 在这里我们肯定是内部调用synchronized同步各个方法， 否则就要使用lock当中的同步器类AbstractQueue然后调用底层的unsafe类型来做， 

所以这道题目还是使用synchronized 来实现比较好点

1. 读写锁的时候要非常注意的是线程之间的通信，当不满足相应的条件的时候
2. 主要是wait()和notify()、sleep()、notifyAll()操作之前最好是要写下, 主要是注意在这里面有两个东西wait 主要处理中断错误的情况， 而notify() 是不需要进行处理这个


## 如何解决写饥饿的问题
1. 使用两个变量
2. readlock  当没有writecount>0时候进行一直等待操作，
3. writelock 当writecount> 0时一直进入等待操作，否则获得锁，然后再进行判断是否readcount是否>0 , 
 
```java

    private int readcount = 0;
    
    private int writecount = 0;
    
    public void lockread() throws InterruptedException{
        while(writecount > 0){
            synchronized(this){
                wait();
            }
        }
        readcount++;
        //进行读取操作
        System.out.println("读操作");
    }
    
    public void unlockread(){
        readcount--;
        synchronized(this){
            notifyAll();
        }
    }
    
    
   // <!--在正常的情况下，我们如果把两个操作放在前面的基本情况
    // 但是这种情况会发生写操作饥饿的情况-->
    
    public void lockwrite2() throws InterrupedException{
    while(writecount>0|| readcount>0)
        wait();
    }
      writecount++;
    }
    
    
    public void lockwrite() throws InterruptedException{
        while(writecount > 0){
            synchronized(this){
                wait();
            }
        }
        //之所以在这里先++，是先占一个坑，避免读操作太多，从而产生写的饥饿等待
        writecount++;
        // 将读操作放在下面的
        while(readcount > 0){
            synchronized(this){
                wait();
            }
        }
        //进行写入操作
        System.out.println("写操作");
    }
    
    public void unlockwrite(){
        writecount--;
        synchronized(this){
            notifyAll();
        }
    }
    
    public static void main(String[] args) throws InterruptedException {
        MyReadWriteLock readWriteLock = new MyReadWriteLock();
        for(int i = 0; i < 2; i++){
            Thread2 thread2 = new Thread2(i, readWriteLock);
            thread2.start();
        }
        
        for (int i = 0; i < 10; i++) {
            Thread1 thread1 = new Thread1(i, readWriteLock);
            thread1.start();
        }
        
    }

}

class Thread1 extends Thread{
    public int i;
    public MyReadWriteLock readWriteLock;
    
    public Thread1(int i, MyReadWriteLock readWriteLock) {
        this.i = i;
        this.readWriteLock = readWriteLock;
    }

    @Override
    public void run() {
        try {
            readWriteLock.lockread();
            Thread.sleep(1000);//模拟耗时
            System.out.println("第"+i+"个读任务");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            readWriteLock.unlockread();
        }
    }
}



class Thread2 extends Thread{
    public int i;
    public MyReadWriteLock readWriteLock;
    
    public Thread2(int i, MyReadWriteLock readWriteLock) {
        this.i = i;
        this.readWriteLock = readWriteLock;
    }

    @Override
    public void run() {
        try {
            readWriteLock.lockwrite();
            Thread.sleep(1000);
            System.out.println("第"+i+"个写任务");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            readWriteLock.unlockwrite();
        }
    }
}
```

上述代码还可以使用一个writerequest请求记录。如果有一个先满足写请求再满足读读情况

```java
public class ReadWriteLock{

  private int readers       = 0;
  private int writers       = 0;
  private int writeRequests = 0;

  public synchronized void lockRead() throws InterruptedException{
 // 如果是读请求的话， 也可以先放在后面再来进行处理吧
     // 在这里面两个都要满足自己的要求
    while(writers > 0 || writeRequests > 0){
      wait();
    }
    readers++;
  }

  public synchronized void unlockRead(){
    readers--;
    notifyAll();
  }

  public synchronized void lockWrite() throws InterruptedException{
    writeRequests++;

    while(readers > 0 || writers > 0){
      wait();
    }
    writeRequests--;
    writers++;
  }

  public synchronized void unlockWrite() throws InterruptedException{
    writers--;
    notifyAll();
  }
}
```


# 参考连接
[Read / Write Locks in Java](http://tutorials.jenkov.com/java-concurrency/read-write-locks.html)


