
# 阻塞队列LinkedBlockingQueue



队列当中元素中书数目大小为count  声明为原子类，所有保证了底层数据结构的线程安全操作


```java
  private final AtomicInteger count = new AtomicInteger();
```


1. 提供阻塞方法和非阻塞方法 put() offer()  
2. 两把锁 读锁，写锁，分别对应一个条件变量


三种构造方法

**入队列**
- put()算法，为阻塞算法，直到队列有空余时，才能为队列加入新元素
- offer()算法为非阻塞算法，如果队列已满，立即返回或等待一会再返回，通过返回值ture或false，标记本次入队操作是否成功


**出队列**

- take()算法为阻塞算法，直到队列有非空时，才将允许调用线程取出数据
- poll()算法为非阻塞算法，如果队列为空，立即返回或等待一会再返回，通过返回值ture或false，标记本次出队操作是否成功
- peek()算法比较特殊，只返回队列中的第一个元素，既不出队，也不阻塞，如果没有元素，就返回null


```java
 public void put(E e) throws InterruptedException {
        if (e == null) throw new NullPointerException();     
        int c = -1;   
        // 写锁
        final ReentrantLock putLock = this.putLock;
```

````java
// 读锁
 public E take() throws InterruptedException {
        E x;
        int c = -1;
        final AtomicInteger count = this.count;
        final ReentrantLock takeLock = this.takeLock;

````

**上述方法都是只获取两把锁当中的读锁或者写锁**


还有一个就是从队列当中remove() 一个元素 ，是两种把锁都要获取到，入队和出队都是只获取一个锁，而remove()方法需要同时获得两把锁
```java

// 需要变量同一个链表进行一个查找



 public boolean remove(Object o) {
        if (o == null) return false;
        fullyLock();
        // 保持前面节点情况 ，pre= trail p代表当前节点情况
        try {
            for (Node<E> trail = head, p = trail.next;p != null;
                 trail = p, p = p.next) {
                
                
                // unlink 就是链表节点连接情况
                
                
                if (o.equals(p.item)) {
                    unlink(p, trail);
                    return true;
                }
            }
            return false;
        } finally {
            fullyUnlock();
        }
    }
```

## 避免死锁
在读写锁两把锁当中如何避免


两把锁的上锁顺序和解锁顺序是相反的情况。

```java
 void fullyLock() {

        putLock.lock();       // 先加写锁
        takeLock.lock();     // 再加读锁
    }

    void fullyUnlock() {
        takeLock.unlock();  /* 先解锁读锁 */
        putLock.unlock();   /* 再解锁写锁 */
    }

```


## 线程通知机制
1. await()
2. singal()

## 如何提高吞吐量

两把锁， 双锁

LinkedBlockingQueue将读和写操作分离，可以让读写操作在不干扰对方的情况下，完成各自的功能，提高并发吞吐量。
在写这篇文章时

## 总结

在上面分析LinkedBlockingQueue的源码之后，可以与ArrayBlockingQueue做一个比较。 
相同点有如下2点： 
1. 不允许元素为null 
2. 线程安全的队列

**注意点**

- 同一个线程不能同时执行读锁和写入锁操作，
- 两个线程，一个线程可以执行put操作，另个线程可以同时执行take()




## 对比

不同点有如下几点： 
1. ArrayBlockingQueue底层基于定长的数组，所以容量限制了；LinkedBlockingQueue底层基于链表实现队列，所以容量可选，如果不设置，那么容量是int的最大值 


2. ArrayBlockingQueue内部维持一把锁和两个条件，同一时刻只能有一个线程队列的一端操作；LinkedBlockingQueue内部维持两把锁和两个条件，同一时刻可以有两个线程在队列的两端操作，但同一时刻只能有一个线程在一端操作。 


3. LinkedBlockingQueue的remove()类似方法时，**由于需要对整个队列链表实现遍历**，所以需要获取两把锁，对两端加锁。





**为何ArrayBlockingQueue**

如果使用双锁的话，会带来额外的设计复杂性



1. ArrayBlockingQueue完全可以采用分离锁，从而实现生产者和消费者操作的完全并行运行。

2. 之所以没这样去做，猜测是因为ArrayBlockingQueue的数据写入和获取操作已经足够轻巧(数组访问元素下标比较简单，可以直接操作)，以至于引入独立的锁机制，除了给代码带来额外的复杂性外，

3. 其在性能上完全占不到任何便宜。 

4. ArrayBlockingQueue和LinkedBlockingQueue间还有一个明显的不同之处在于，前者在插入或删除元素时不会产生或销毁任何额外的对象实例，而后者则会生成一个额外的Node对象。这在长时间内需要高效并发地处理大批量数据的系统中，其对于GC的影响还是存在一定的区别。而在创建ArrayBlockingQueue时，我们还可以控制对象的内部锁是否采用公平锁，默认采用非公平锁


**关于上述第四点**

- 避免频繁创建对象带来的开销同样是体现在 Disruptor 基于RingBuffer开销 



# 参考链接

- [LinkedBlockingQueue的双锁,源码分析 - jackyechina的专栏 - CSDN博客](https://blog.csdn.net/jackyechina/article/details/54618329)

- [深入理解阻塞队列（三）——LinkedBlockingQueue源码分析 - xingfeng_coder的博客 - CSDN博客](https://blog.csdn.net/qq_19431333/article/details/73087366)