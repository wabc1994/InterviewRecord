
# locks 与AQS 总体使用关系

Reetrantlock 当中定义个sync extends AQS
然后分为公平锁和非公平锁 nofairsync 和fairsync  这两者分别又是extends sync 


# AbstractQueuedSynchronizer(AQS,队列同步器)
是一个抽象类abstract class，提供了一个基于FIFO队列，可以用于构建锁或者其他相关同步装置的基础框架。

该同步器（以下简称同步器）利用了一个int来表示状态，期望它能够成为实现大部分同步需求的基础。

使用的方法是继承，子类通过继承同步器并需要实现它的方法来管理其状态，管理的方式就是通过类似acquire和release的方式来操纵状态，获得锁和释放锁。

然而多线程环境中对状态的操纵必须确保原子性，因此子类对于状态的把握，需要使用这个同步器提供的以下三个方法对状态进行操作：

1. state
2. 内置的FIFO队列，带头双向循环链表，每个链表节点时内部封装一个线程
3. Node 主要包括四个pre，next waitStatus和thread

这个类的基本属性

```java
ublic abstract class AbstractQueuedSynchronizer
    extends AbstractOwnableSynchronizer
    implements java.io.Serializable {

    /**
     *      +------+  prev +-----+       +-----+
     * head |      | <---- |     | <---- |     |  tail
     *      +------+       +-----+       +-----+	
 	 */

    private transient volatile Node head;

    private transient volatile Node tail;

    /**
     * 抽象出来的资源
     */
    private volatile int state; //state>=0
    // =0表示这个锁目前是没有线程锁住，state 
}

```

## CHL队列当中的节点
```java
//主要是通过静态类是先的
static final class Node {

	// 省略部分代码...
		
	volatile Node prev; // 前节点
	
	volatile Node next; // 后节点

	volatile int waitStatus;// 等待状态
	
	volatile Thread thread; // 节点线程
	
	Node nextWaiter; // 节点模式

	Node() { }
	
	Node(Thread thread, Node mode) { 
		this.nextWaiter = mode;
		this.thread = thread;
	}
	
	Node(Thread thread, int waitStatus) { 
		this.waitStatus = waitStatus;
		this.thread = thread;
	}
}
```

[waitStatus详解情况](https://juejin.im/post/5ae755606fb9a07ab97942a4)

SIGNAL —— -1，表示：当当前节点释放锁的时候，需要唤醒下一个节点， 如果waitStatus取值==-1==SIGNAL 

节点也分为两种，分别是共享和独占:


> 在这里面的所谓的独占共享模式其实也是ReetantLock读写锁
> 在这个点上比synchronized提供了更加细化的锁粒度情况，如果临界区只涉及读操作，那么可以多个线程进入临界区，提高了效率，
>  

```java
// 共享模式，表示节点里面的线程要获取的是共享锁，即一个锁可以被不同的线程拥有
static final Node SHARED = new Node();

//独占模式， 表示节点中的线程要获取的锁是独占的，也就是该锁只能被一个线程拥有

static final Node EXCLUSIVE =null;
```

[CHL队列的节点](https://my.oschina.net/oxf1992/blog/837368)

# Lock 与AbstractQueuedSynchronizer
LOCK的实现类其实都是构建在AbstractQueuedSynchronizer上，这是每个Lock实现类都持有自己**内部类Sync的实例**，而这个Sync就是继承AbstractQueuedSynchronizer(AQS)。为何要实现不同的Sync呢？这和每种Lock用途相关。另外还有AQS的State机制

一个最简单的实现如下所示

# 实现关键
自旋锁和CAS操作保证原子操作，可以查看代码部分
当一个线程获取锁失败时，是如何被添加到阻塞队列CHL队列当中的， 主要是通过addWaiter（Node mode）实现


```java
// 头结点
private transient volatile Node head;

// 尾节点, 双向循环的尾部节点
private  transient volatile Node tail;
private Node addWaiter(Node mode){
	Node node= new Node(Thread.currentThread, mode);
	// 获取得到前驱节点基本情况
	Node pred = tail;
	if(pre!=null){
	
	node.pre =pred
	
	if(compareAndSetTail(pred,node)){
	
	/ 通过 CAS 操作设置 tail 为 node
		// 关键 ->失败表示在这期间有其他线程的节点被设置为新的尾节点
	     pred.next= node;
	     return node;
	}
}
// 关键 -> 当[等待队列]为空，或者新节点入队失败时（说明存在并发），代码才会执行到这
enq(node);

}
```
在等待队列尾部添加一个线程，

```java
private Node enq(final Node node){
   		// 这里就是一个循环， 空循环实现一个自旋锁的功能，只有添加成功返回， 这个锁才被释放
   		for(;;){
   		Node t =tail;
   		if(t==null){
   		// 构建等待队列的头节点，实质是创建一个空的循环双链表
			if (compareAndSetHead(new Node())){
				tail = head;
			}
		} else {
			// 设置该节点为新的尾节点
			node.prev = t;
			if (compareAndSetTail(t, node)) {
				t.next = node;
				return t;
			}
		}
   		}
   		}
}
```
# 如何自定义同步类器
下面就是如何使用同步类器，一个类只需要在内部定义一个stactic 类extends AbstractQueueSynchronizer即可

案例如下所示

```java
public class Mutex {
     // 内部包括一个静态类
    static class Sync extends AbstractQueuedSynchronizer {

        public Sync() {
            setState(100); // set the initial state, being unlocked.
        }

        @Override
        protected boolean tryAcquire(int ignore) {
            return compareAndSetState(100, 1);
        }

        @Override
        protected boolean tryRelease(int ignore) {
            setState(100);
            return true;
        }
    }
     // 类中保持这么一个同步类对象
    private final Sync sync = new Sync();
    
    public void lock() {
        sync.acquire(0);
    }

    public void unlock() {
        sync.release(0);
    }

```

[AbstractQueuedSynchronizer by Using a Simple Mutex Example](https://hackernoon.com/brief-introduction-to-abstractqueuedsynchronizer-by-using-a-simple-mutex-example-2f2bc9aa3c54)


后面的链接给出了一个CountDownLatch是如何使用AQS锁实现的
# synchronized比较
灵活
1. 公平锁和非公平锁
2. 独占锁和共享锁
3. 等待时间
4. 线程可以被中断
5. 
# reference
前面两个讲解是最详细的，可以仔细进行复习

[一行一行源码分析AbstractQueuedSynchronizer](https://javadoop.com/2017/06/16/AbstractQueuedSynchronizer/)

[一行一行源码分析
AbstractQueuedSynchronizer (二)](https://javadoop.com/post/AbstractQueuedSynchronizer-2)

[1](http://ifeve.com/introduce-abstractqueuedsynchronizer/)

[AbstractQueuedSynchronizer](https://hackernoon.com/brief-introduction-to-abstractqueuedsynchronizer-by-using-a-simple-mutex-example-2f2bc9aa3c54)

[什么是AQS框架](https://blog.csdn.net/wangyangzhizhou/article/details/40958637)

[Java多线程（七）之同步器基础：AQS框架深入分析 - ](https://blog.csdn.net/vernonzheng/article/details/8275624)

[CountDownLatch说明 AQS 的实现原理](http://www.cnblogs.com/fengzheng/p/9153720.html)