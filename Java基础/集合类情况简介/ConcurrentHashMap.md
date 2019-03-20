[参考链接](https://www.ibm.com/developerworks/cn/java/java-lo-concurrenthashmap/)
# 前提知识

## Java内存模型

回答这个问题主要是从计算机系统结构当中的缓存和主存，CPU三者的关系，Java线程内存模型

Java当中的内存模型是共享内存模型，JMM分为栈和共享堆，JMM 主要考虑的两个问题
1. 共享变量在各个线程之间的可见性问题 volidate 要解决的问题
2. 共享变量在各个线程之间的竞争问题  synchronized ReetrantLock 要解决的问题


## 指令重排序



**volatile关键字**

在基本清除了java内存模型之后，我们开始详细说明一下volatile关键字，在concurrentHashMap之中，有很多的成员变量都是用volatile修饰的。被volatile修饰的变量有如下特性:

- 使得变量更新变得具有可见性，只要被volatile修饰的变量的赋值一旦变化就会通知到其他线程，如果其他线程的工作内存中存在这个同一个变量拷贝副本，那么其他线程会放弃这个副本中变量的值，重新去主内存中获取

- 产生了内存屏障，防止指令进行了重排序，关于这点的解释，请看下面一段代码

## happen-before 原则



# ConcurrentHashMap 如何实现线程安全的？

JDK1.7当中的实现线程安全主要可以总结为下面几个点：
1. 分段锁 segement继承reetrantLock锁


# HashMap 存在的问题
1. 线程不是安全
2. 我们平时开发过程中用的比较多的集合，但它是非线程安全的，在涉及到多线程并发的情况，进行put操作有可能会引起死循环，导致CPU利用率接近100%。

**所以我们在向面试官介绍ConcurrentHashMap的时候，核心就是要明白介绍的重点是什么：与HashMap 相比，ConcurrentHashMap 到底是怎样实现了线程安全这个东西**

## JDK1.7 

**减少锁的粒度segment锁**

它的思想是将物理上的一个锁，拆成逻辑上的多个锁，增加并行度，从而降低锁竞争。它的思想也是用空间来换时间；

核心数据结构

sgements 是一种内部类，继承自T
segments [] 数组， 默认的时候是16个节点的情况， hashEntry 每个节点, treeNode(后面转换成为)

Segment继承ReentrantLock用来充当锁的角色，每个 Segment 对象守护每个散列映射表的若干个桶。
HashEntry 用来封装映射表的键 / 值对；
每个桶是由若干个 HashEntry 对象链接起来的链表

1. public V get(Object key)不涉及到锁，也就是说获得对象时没有使用锁；

2. put、remove方法要使用锁，但并不一定有锁争用，原因在于ConcurrentHashMap将缓存的变量分到多个Segment，每个Segment上有一个锁，只要多个线程访问的不是一个Segment就没有锁争用，就没有堵塞，各线程用各自的锁，ConcurrentHashMap缺省情况下生成16个Segment，也就是允许16个线程并发的更新而尽量没有锁争用；


>Segment< K,V >[] segments ,  将全部数组+链表，分成多个模块之前的情况，

segment 继承了reetrantlock 所以可以充当多的功能

static final class Segment<K,V> extends ReentrantLock implements Serializable

![concurrentHashMap](https://github.com/wabc1994/InterviewRecord/blob/master/Java%E5%9F%BA%E7%A1%80/%E5%9B%BE%E7%89%87/ConcurrentHashMap7.png)

# ConcurrentHashMap与HashMap的区别

一个是线程安全，一个是非线程安全的问题

# ConcurrentHashMap 与Hashtable的区别

总体思想

1. 在1.8版本以前，ConcurrentHashMap采用分段锁的概念，使锁更加细化
2. 但是1.8已经改变了这种思路，而是利用CAS+Synchronized来保证并发更新的安全，当然底层采用数组+链表+红黑树的存储结构。

HashTable容器使用**synchronized**来保证线程安全，但是在线程竞争激烈的情况下，HashTable的效率非常低

# JDK1.7和JDK1.8 ConcurrentHashMap 实现区别
[两者包括的基础数据结构](https://blog.csdn.net/wengcheng_k/article/details/78868759)



## JDK1.7

1. 一个ConcurrentHashMap中包含一个Segment<K,V>[] segments 数组。 
2. 一个Segment对象中包含一个HashEntry<K,V>[] table数组。 
3. 一个HashEntry对象包含hash值，Key，Value，以及下一个HashEntry对象

![JDK1.7中的ConcurrentHashMap](https://github.com/wabc1994/InterviewRecord/blob/master/Java%E5%9F%BA%E7%A1%80/%E5%9B%BE%E7%89%87/JDK1.7ConcurrentHashMap.png)

**整个JDK1.7ConcurrentHashMap分为三部分 segments 数组，元素为segment， segment里面是一个hashtable, 是一个table[] 数组， 每个数组是一个桶，桶当中是链表或者红黑树的概念**

`Segment`继承`ReentrantLock`重入锁（`Segment<K,V> extends ReentrantLock`），也就是说每个`Segment` 对象都是重入锁， Segment 也被称为“锁段”的概念， 

**保证线程安全**

1. 分段锁是多个线程间的并发操作
2. 用Volatile 变量协调写线程间的内存可见性问题
3. 用HashEntry 对象的不变形降低读操作对加锁的需求


其包含两个核心静态内部类 Segment和HashEntry。

1. Segment继承ReentrantLock用来充当锁的角色，每个 Segment 对象守护每个散列映射表的若干个桶。
2. HashEntry 用来封装映射表的键 / 值对；
3. 每个桶是由若干个 HashEntry 对象链接起来的链表。

一个 ConcurrentHashMap 实例中包含由若干个 Segment 对象组成的数组，下面我们通过一个图来演示一下 ConcurrentHashMap 的结构：

## JDK1.8

![JDK1.8当中的结构](https://github.com/wabc1994/InterviewRecord/blob/master/Java%E5%9F%BA%E7%A1%80/%E5%9B%BE%E7%89%87/JDK1.8.png)

**整个JDK1.8当中ConcurrentHashMap 分为两部分，就像HashMap**

引申出了多中数据结构 Node<k,v>TreeBin，Traverser等对象内部类。
1. 利用UnSafe类的CAS原子操作Node<K,V>  
2. 改用synchronized 而不是reetrantlock， 说明在JDK1.8之后synchronized锁已经优化得足够好，在reetrantlock 和synchronzied 之间我们可以直接使用该同步关键字了

如何保证并发中的线程安全问题， 主要思想是使用CAS(compare and swap , 比较然后进行交换)和sychronized锁实现的线程安全问题，并且结构上面跟HashMap jdk1.8 一样的情况下，采用数组 + 链表 + 红黑树的结构实现

几个基本数据结构代替jdk1.7当中的Segement类和HashEntry类
- table 类 代替segements  transient volatile HashEntry<K,V> table table

- Node<K,V>implement Map.Entry<K,V>接口，代替了JDK1.7当中的HashEntry 

- Node是最核心的内部类，它包装了key-value键值对，所有=入ConcurrentHashMap的数据都包装在这里面。它与HashMap中的定义很相似，但是但是有一些差别它对value和next属性设置了volatile同步锁(与JDK7的Segment相同)，它不允许调用setValue方法直接改变Node的value域，它增加了find方法辅助map.get()方法。 

- ForwardingNode ：一个特殊的Node节点，hash值为-1，其中存储nextTable的引用。

- Node<K,V>与table 之间的基本关系Node<K,V> [] table 

- 一个很关键的变量 SZIECTL 关键变量，这个变量直接声明为 private static int 类型，主要用来控制真个table，主要通过Unsafe,当中的CAS操作保证这个多个线程操作的线程安全

- TreeNode树节点类，另外一个核心的数据结构。当链表长度过长的时候，会转换为TreeNode。但是与HashMap不相同的是，它并不是直接转换为红黑树，而是把这些结点包装成TreeNode放在TreeBin对象中，由TreeBin完成对红黑树的包装。而且TreeNode在ConcurrentHashMap集成自Node类，而并非HashMap中的集成自LinkedHashMap.Entry< K,V >类，也就是说TreeNode带有next指针，这样做的目的是方便基于TreeBin的访问

- TreeBin， hash==-2 代表一个treeBin节点，要知道我们将链表转换成为红黑树的时候，是将整个东西转化为红黑树，从头结点开始到尾巴结点，而不是在同一个桶当中红黑树和链表结合的方式

>treeBin这个类并不负责包装用户的key、value信息，而是包装的很多TreeNode节点。它代替了TreeNode的根节点，也就是说在实际的ConcurrentHashMap“数组”中，存放的是TreeBin对象，而不是TreeNode对象，这是与HashMap的区别。另外这个类还带有了读写锁。


### 1. Node 源码分析

1. value和next 都设置为volatile 同步
2. 不允许调用setValue方法直接改变Node的value
3. 增加find 方法辅助map.get()方法

```java
//保存key，value及key的hash值的数据结构。
class Node<K,V> implements Map.Entry<K,V> {
  final int hash; // 
  final K key;
  volatile V val;  //其中volatile修饰key 和value, 保证内存的可见性问题，这也是线程安全一个重要方面之一
  volatile Node<K,V> next;
  //... 省略部分代码
  //其中value和next都用volatile修饰，保证并发的可见性。
  
 //不允许直接改变value的值
        public final V setValue(V value) {
            throw new UnsupportedOperationException();
        }

}
       Node<K,V> find(int h,Object k){
    Node<K,V> e=this;
    if(k!=null){
        do{
            k ek;
            if(e.hash==h&& ((ek = e.key) == k || (ek != null && k.equals(ek))))
                                                   return e;
        }while((e=e.next)!=null);
        
    }
    return null;
       }
```


### 2. ForwordingNode

- 一个用于连接两个table的节点类。它包含一个nextTable指针，用于指向下一张表。

- 而且这个节点的key value next指针全部为null，它的hash值为-1. 这里面定义的find的方法是从nextTable里进行查询节点，而不是以自身为头节点进行查找

```java
static final class ForwardingNode<K,V> extends Node<K,V> {
    // 节点数组tab
        final Node<K,V>[] nextTable;
        ForwardingNode(Node<K,V>[] tab) {
            super(MOVED, null, null, null);
            this.nextTable = tab;
        }
        //省略部分代码
        }
        
        //这里面定义的find的方法是从nextTable里进行查询节点，而不是以自身为头节点进行查找
Node<K,V> find(int h, Object k) {
            // loop to avoid arbitrarily deep recursion on forwarding nodes
            outer: for (Node<K,V>[] tab = nextTable;;) {
                Node<K,V> e; int n;
                if (k == null || tab == null || (n = tab.length) == 0 ||
                    (e = tabAt(tab, (n - 1) & h)) == null)
                    return null;
                for (;;) {
                    int eh; K ek;
                    if ((eh = e.hash) == h &&
                        ((ek = e.key) == k || (ek != null && k.equals(ek))))
                        return e;
                    if (eh < 0) {
                        if (e instanceof ForwardingNode) {
                            tab = ((ForwardingNode<K,V>)e).nextTable;
                            continue outer;
                        }
                        else
                            return e.find(h, k);
                    }
                    if ((e = e.next) == null)
                        return null;
                }
            }
        }
    }

```
ForwardingNode的hash为-1，用于指向扩容的下一个nexttable 

### 3. SIZECTL 这是实现线程安全的第一步

sizeCtl 这是变量是互斥变量，同一时间只能有一个线程获得修改这个变量的权利，是互斥的，修改也是通过CAS操作执行的基本情况


```java

/**
 * 这个sizeCtl是volatile的，那么他是线程可见的，一个思考:它是所有修改都在CAS中进行，但是sizeCtl为什么不设计成LongAdder(jdk8出现的)类型呢？
 * 或者设计成AtomicLong(在高并发的情况下比LongAdder低效)，这样就能减少自己操作CAS了。
 *
 * 默认为0，用来控制table的初始化和扩容操作，具体应用在后续会体现出来。是一个多线程之间的共享变量
 * -1 代表table正在初始化或有线程在进行扩容
 * -N 表示有N-1个线程正在进行扩容操作
 * 其余情况：
 *1、如果table未初始化，表示table需要初始化的大小。
 *2、如果table初始化完成，表示table的容量，默认是table大小的0.75 倍，居然用这个公式算0.75（n - (n >>> 2)）。
 **/
 // 声明为volatile 保证多线程之间的可见性问题
 
 /**
      * 盛装Node元素的数组 它的大小是2的整数次幂
      * Size is always a power of two. Accessed directly by iterators.
      */
 transient volatile Node<K,V>[] table;

 
 private transient volatile int sizeCtl;

 private static int RESIZE_STAMP_BITS = 16;
 
 private static final int RESIZE_STAMP_SHIFT = 32 - RESIZE_STAMP_BITS;

```



哈希桶Table初始化：发生在第一次put的时候，而不是创建对象的时候
>首先来看一下table是如何初始化的，初始化table的工作将发生在进行put操作时，如果发现table还没有被初始化，那么就会调用方法initTable来进行table的初始化，下面展示了初始化table的具体流程代码：

**初始化代码**
```java
private final Node<K,V>[] initTable() {
        Node<K,V>[] tab; int sc;
        while ((tab = table) == null || tab.length == 0) {
            if ((sc = sizeCtl) < 0)
                Thread.yield(); // lost initialization race; just spin 发生有其他线程在对hashtable 进行扩容的话，那么久要自旋，让出CPU的
            else if (U.compareAndSwapInt(this, SIZECTL, sc, -1)) {
                try {
                    if ((tab = table) == null || tab.length == 0) {
                        
                    //如果sc>0 直接等于sc 要不然就等于默认容量
                        int n = (sc > 0) ? sc : DEFAULT_CAPACITY;
                        @SuppressWarnings("unchecked")
                        Node<K,V>[] nt = (Node<K,V>[])new Node<?,?>[n];
                        table = tab = nt;
                        sc = n - (n >>> 2);
                    }
                } finally {
                    sizeCtl = sc;
                }
                break;
            }
        }
        return tab;
        
```
>sizeCtl是一个用于同步多个线程的共享变量，如果它的当前值为负数，则说明table正在被某个线程初始化或者扩容，所以，如果某个线程想要初始化table或者对table扩容，需要去竞争sizeCtl这个共享变量，获得变量的线程才有许可去进行接下来的操作，没能获得的线程将会一直自旋thread.yield()来尝试获得这个共享变量，所以获得sizeCtl这个变量的线程在完成工作之后需要设置回来，使得其他的线程可以走出自旋进行接下来的操作。而在initTable方法中我们可以看到，当线程发现sizeCtl小于0的时候，他就会让出CPU时间，而稍后再进行尝试，当发现sizeCtl不再小于0的时候，就会通过调用方法compareAndSwapInt来讲sizeCtl共享变量变为-1，以告诉其他试图获得sizeCtl变量的线程，目前正在由本线程在享用该变量，在我完成我的任务之前你可以先休息一会，等会再来试试吧，我完成工作之后会释放掉的。而其他的线程在发现sizeCtl小于0的时候就会理解这种交流，他们会让出cpu时间，等待下次调度再来尝试获取sizeCtl来进行自己的工作。在完成初始化table的任务之后，线程需要将sizeCtl设置成可以使得其他线程获得变量的状态，这其中还有一个地方需要注意，就是在某个线程通过U.compareAndSwapInt方法设置了sizeCtl之前和之后进行了两次check，来检测table是否被初始化过了，这种检测是必须的，因为在并发环境下，可能前一个线程正在初始化table但是还没有成功初始化，也就是table依然还为null，而有一个线程发现table为null他就会进行竞争sizeCtl以进行table初始化，但是当前线程在完成初始化之后，那个试图初始化table的线程获得了sizeCtl，但是此时table已经被初始化了，所以，如果没有再次判断的话，可能会将之后进行put操作的线程的更新覆盖掉，这是极为不安全的行为。


### 如何利用sychronized锁进行put
1. Sychronized是对象锁，锁住的主要是Node<K,V> 锁住一个小节点， sychronized(一个节点) 细化锁， 只锁住桶头元素，只要锁住头元素，其他线程就不能获取到该桶了



应用在put代码当中，remove 代码也是一样的思想，在实质上面的

HashMap如何进行put的也是非常关键的一步

**put方法如何实现**

1. put(k,va)
2. putval()
3. synchronized 锁住定位后的hash桶桶开头的元素即可


- 在来介绍put方法，这个put方法依然沿用HashMap的put方法的思想，根据hash值计算这个新插入的点在table中的位置i，如果i位置是空的，直接放进去，
- 否则进行判断，如果i位置是树节点，按照树的方式插入新的节点，否则把i插入到链表的末尾。


**put当中的多线程操作**

1. 如果一个或者多个线程正在table进行扩容，当前节点也是要加入扩容的操作当中的，如何进行判断是否需要进行扩容操作，

**forwardding**

- 如果检测到需要插入的位置被forward节点占有，就帮助进行扩容；

- 如果检测到要插入的节点是非空的并且不是forward节点，就对这个节点加锁，这样就保证了线程安全，

- 如果检测到要插入的位置是空的，那么直接放入，并且不需要加锁操作

- 如果这个位置存在节点，说明发生了hash碰撞，首先需要判断这个节点的类型，如果是链表节点(fh>0), 则得到的结点就是hash值相同的节点组成的链表的头节点。需要依次向后遍历确定这个新加入的值所在位置。如果遇到hash值与key值都与新加入节点是一致的情况，则只需要更新value值即可。否则依次向后遍历，直到链表尾插入这个结点。  如果加入这个节点以后链表长度大于8，就把这个链表转换成红黑树。如果这个节点的类型已经是树节点的话，直接调用树节点的插入方法进行插入新的值。
                                                   

```java
public V put(K key, V value) {
    return putVal(key, value, false);
}

    /** Implementation for put and putIfAbsent */
final V putVal(K key, V value, boolean onlyIfAbsent) {
    //ConcurrentHashMap 不允许插入null键，HashMap允许插入一个null键
    if (key == null || value == null) throw new NullPointerException();
    //计算key的hash值
    int hash = spread(key.hashCode());
    int binCount = 0;
    //for循环的作用：因为更新元素是使用CAS机制更新，需要不断的失败重试，直到成功为止。
    for (Node<K,V>[] tab = table;;) {
        // f：链表或红黑二叉树头结点，向链表中添加元素时，需要synchronized获取f的锁。
        Node<K,V> f; int n, i, fh;
        //判断Node[]数组是否初始化，没有则进行初始化操作
        // concurrentHashMap的初始化是放在这一部分进行的
        if (tab == null || (n = tab.length) == 0)
            tab = initTable();
        //通过hash定位Node[]数组的索引坐标，是否有Node节点，如果没有则使用CAS进行添加（链表的头结点），添加失败则进入下次循环。
       
       // 通过CAS操作尝试获得 桶头处的节点元素情况，当中的
        else if ((f = tabAt(tab, i = (n - 1) & hash)) == null) {
            if (casTabAt(tab, i, null,new Node<K,V>(hash, key, value, null)))
                break;                   // no lock when adding to empty bin
        }
        //检查到内部正在移动元素（Node[] 数组扩容）
        else if ((fh = f.hash) == MOVED)
            //帮助它扩容
            tab = helpTransfer(tab, f);
        else {
            V oldVal = null;
            //锁住链表或红黑二叉树的头结点
            synchronized (f) {
                //判断f是否是链表的头结点
                if (tabAt(tab, i) == f) {
                    //如果fh>=0 是链表节点
                    if (fh >= 0) {
                        binCount = 1;
                        //遍历链表所有节点
                        for (Node<K,V> e = f;; ++binCount) {
                            K ek;
                            //如果节点存在，则更新value
                            if (e.hash == hash &&
                                ((ek = e.key) == key ||
                                 (ek != null && key.equals(ek)))) {
                                oldVal = e.val;
                                if (!onlyIfAbsent)
                                    e.val = value;
                                break;
                            }
                            //不存在则在链表尾部添加新节点。
                            Node<K,V> pred = e;
                            if ((e = e.next) == null) {
                                pred.next = new Node<K,V>(hash, key,
                                                          value, null);
                                break;
                            }
                        }
                    }
                    //TreeBin是红黑二叉树节点
                    else if (f instanceof TreeBin) {
                        Node<K,V> p;
                        binCount = 2;
                        //添加树节点
                        if ((p = ((TreeBin<K,V>)f).putTreeVal(hash, key,
                                                      value)) != null) {
                            oldVal = p.val;
                            if (!onlyIfAbsent)
                                p.val = value;
                        }
                    }
                }
                }
            if (binCount != 0) {
                //如果链表长度已经达到临界值8 就需要把链表转换为树结构
                if (binCount >= TREEIFY_THRESHOLD)
                    treeifyBin(tab, i);
                if (oldVal != null)
                    return oldVal;
                break;
            }
        }
    }
    //将当前ConcurrentHashMap的size数量+1
    addCount(1L, binCount);
    return null;
}
```


### 三个CAS操作i出的节点值

2. 无锁思想- 执行者CAS [ConcurrentHashmap具体是如何利用CAS](https://blog.csdn.net/weixin_42636552/article/details/82383272)， 如何CAS技术是为了保证如何在不加锁(乐观)的情况下如何实现线程安全访问变量的

```java
//ConcurrentHashMap定义了三个原子操作，用于对指定位置的节点Node<k,v>进行操作。正是这些原子操作保证了ConcurrentHashMap的线程安全。

//获得在i位置上的Node节点
  //  ((long)i << ASHIFT) + ABASE 实质就是定位哪个桶，桶上面的哪个位置信息
   
   //获取i处的节点值
    static final <K,V> Node<K,V> tabAt(Node<K,V>[] tab, int i) {
        return (Node<K,V>)U.getObjectVolatile(tab, ((long)i << ASHIFT) + ABASE);
    }
        //利用CAS算法设置i位置上的Node节点。之所以能实现并发是因为他指定了原来这个节点的值是多少
        //在CAS算法中，会比较内存中的值与你指定的这个值是否相等，如果相等才接受你的修改，否则拒绝你的修改
        //因此当前线程中的值并不是最新的值，这种修改可能会覆盖掉其他线程的修改结果  有点类似于SVN
    static final <K,V> boolean casTabAt(Node<K,V>[] tab, int i,
                                        Node<K,V> c, Node<K,V> v) {
        return U.compareAndSwapObject(tab, ((long)i << ASHIFT) + ABASE, c, v);
    }
        //利用volatile方法设置节点位置的值
    static final <K,V>void setTabAt(Node<K,V>[] tab, int i, Node<K,V> v) {
        U.putObjectVolatile(tab, ((long)i << ASHIFT) + ABASE, v);
    }
```

### 如何进行扩容 
1. transfer() 扩容主要是这个方法实现的，扩容和转化为红黑树是完全两个东西，这两者之间的基本关系就是先扩容然后实在是不行的话
再进行转换成为红黑树的结构。


**扩容如何保证线程安全**

在线程安全主要是通过之前定义的三个CAS操作和forwarding节点来操作的，



1. tabAt()
2. casTabAt()
3. U.compareAndSwapInt(this, SIZECTL, sc = sizeCtl, sc - 1)

```java
//利用CAS方法更新这个扩容阈值，在这里面sizectl值减一，说明新加入一个线程参与到扩容操作
                if (U.compareAndSwapInt(this, SIZECTL, sc = sizeCtl, sc - 1)) {
                    if ((sc - 2) != resizeStamp(n) << RESIZE_STAMP_SHIFT)
                        return;
                    finishing = advance = true;
                    i = n; // recheck before commit
                }

```


```java

//扩容详细过程
private final void transfer(Node<K,V>[] tab, Node<K,V>[] nextTab) {
        int n = tab.length, stride;
        if ((stride = (NCPU > 1) ? (n >>> 3) / NCPU : n) < MIN_TRANSFER_STRIDE)
            stride = MIN_TRANSFER_STRIDE; // 每个线程所负责转移的数组的区间最少为MIN_TRANSFER_STRIDE=16,也就是说数组的连续16个位置都是由这个线程来进行转移，其他线程不允许接触这连续的16个位置，必须发生线程之间大量的内存冲突。换另一个角度来说，每个线程负责连续16个大小区间的数组转移。
        if (nextTab == null) {            // 初始化生成新的扩容数组
            try {
                @SuppressWarnings("unchecked")
                Node<K,V>[] nt = (Node<K,V>[])new Node<?,?>[n << 1];  //新创建两倍原数组大小的新数组
                nextTab = nt;
            } catch (Throwable ex) {      // try to cope with OOME
                sizeCtl = Integer.MAX_VALUE;
                return;
            }
            nextTable = nextTab;  //nextTable为类成员变量，只有在扩容的过程中有作用，在其他时刻都是null值。nextTable指向新数组
            transferIndex = n;   //转移后的节点偏移量
        }
        int nextn = nextTab.length;
        ForwardingNode<K,V> fwd = new ForwardingNode<K,V>(nextTab);
        boolean advance = true;   //遍历
        boolean finishing = false; //保证在提交扩容后的新数组时，原数组中的所有元素都已经被遍历
        for (int i = 0, bound = 0;;) {
            Node<K,V> f; int fh;
            while (advance) {
                int nextIndex, nextBound;
                if (--i >= bound || finishing)   //bound为数组区间下限值，i为当前转移数组的位置,--i处理转移下一个节点位置，从后往前处理
                    advance = false;  //退出while循环
                else if ((nextIndex = transferIndex) <= 0) {   //表示原数组已经分割完了
                    i = -1;
                    advance = false;  //退出while循环
                }
                else if (U.compareAndSwapInt
                         (this, TRANSFERINDEX, nextIndex,    
                          nextBound = (nextIndex > stride ?
                                       nextIndex - stride : 0))) {  //CAS操作修改transferIndex值，代表下一个线程转移原数组的节点的位置
                    bound = nextBound;  //设置当前线程转移原数组区间的下限值
                    i = nextIndex - 1;  //从后往前处理
                    advance = false;  //退出while循环
                }
            }
            if (i < 0 || i >= n || i + n >= nextn) {
                int sc;
                if (finishing) {   //扩容完成
                    nextTable = null;   //将nextTable置为null，表示当前扩容过程完成
                    table = nextTab;    //table指向扩容后的新数组
                    sizeCtl = (n << 1) - (n >>> 1);  //将szieCtl设置为正数，设置为原数组的3/2，即新数组的3/4
                    return;
                }
                if (U.compareAndSwapInt(this, SIZECTL, sc = sizeCtl, sc - 1)) {
                    if ((sc - 2) != resizeStamp(n) << RESIZE_STAMP_SHIFT)   //因为只有一个线程扩容时sc=resizeStamp(n)+2,所以该if语句是在最后一个线程完成扩容操作时，将finishing置为true，表示正确完成。
                        return;
                    finishing = advance = true;
                    i = n; // recheck before commit
                }
            }
            else if ((f = tabAt(tab, i)) == null)
                advance = casTabAt(tab, i, null, fwd);   //将原数组相应位置直接设置为fwd,表示该位置已经遍历过
            else if ((fh = f.hash) == MOVED)
                advance = true; // 表示该数组位置已经被其他线程处理过了
            else {  //否则需要将原数组位置相应元素复制到新数组上
                synchronized (f) {   //上锁
                    if (tabAt(tab, i) == f) {   //再次核对，防止其他线程对该hash值进行修改
                        Node<K,V> ln, hn;
                        if (fh >= 0) {   //说明该位置存放的是普通节点
                            int runBit = fh & n;  //判断原数组中的节点的hash的 log(n)位为0或者1
                            Node<K,V> lastRun = f;
                            for (Node<K,V> p = f.next; p != null; p = p.next) {
                                int b = p.hash & n;
                                if (b != runBit) {
                                    runBit = b;
                                    lastRun = p;
                                }
                            }
                            if (runBit == 0) {   
                                ln = lastRun;  //指向链表的最后出现连续log(n)位为0的第一个节点
                                hn = null;
                            }
                            else {     
                                hn = lastRun;   //指向链表的最后出现连续log(n)位为1的第一个节点
                                ln = null;
                            }
                            for (Node<K,V> p = f; p != lastRun; p = p.next) {
                                int ph = p.hash; K pk = p.key; V pv = p.val;
                                if ((ph & n) == 0)
                                    ln = new Node<K,V>(ph, pk, pv, ln);
                                else
                                    hn = new Node<K,V>(ph, pk, pv, hn);
                            }
                            setTabAt(nextTab, i, ln);   //将hash值的 log(n) 位为0的节点链表复制到新数组对应原来数组的位置
                            setTabAt(nextTab, i + n, hn);  //将Hash值的 log(n) 位为1的节点链表复制到新数组对应原来数组位置+n
                            setTabAt(tab, i, fwd);  //将该数组位置设置为已处理
                            advance = true;
                        }
                        else if (f instanceof TreeBin) {   //说明该数组位置是红黑树根节点
                            TreeBin<K,V> t = (TreeBin<K,V>)f;
                            TreeNode<K,V> lo = null, loTail = null;
                            TreeNode<K,V> hi = null, hiTail = null;
                            int lc = 0, hc = 0;
                            for (Node<K,V> e = t.first; e != null; e = e.next) {
                                int h = e.hash;
                                TreeNode<K,V> p = new TreeNode<K,V>
                                    (h, e.key, e.val, null, null);
                                if ((h & n) == 0) {   //判断红黑树中节点的hash值的 log(n) 位为0，说明该节点应该存放到新数组中对应原数组的位置
                                    if ((p.prev = loTail) == null)
                                        lo = p;
                                    else
                                        loTail.next = p;
                                    loTail = p;
                                    ++lc;
                                }
                                else {    //判断红黑树中节点的hash值的 log(n) 位为1，说明该节点应该存放到新数组中对应原数组位置+n
                                    if ((p.prev = hiTail) == null)
                                        hi = p;
                                    else
                                        hiTail.next = p;
                                    hiTail = p;
                                    ++hc;
                                }
                            }
                            //根据链表中节点的个数和UNTREEIFY_THRESHOLD进行比较，如果小于等于，则不需要将链表转换为红黑树；如果大于，则需要将链表转换为红黑树
                            ln = (lc <= UNTREEIFY_THRESHOLD) ? untreeify(lo) :   
                                (hc != 0) ? new TreeBin<K,V>(lo) : t;
                            hn = (hc <= UNTREEIFY_THRESHOLD) ? untreeify(hi) :
                                (lc != 0) ? new TreeBin<K,V>(hi) : t;
                            setTabAt(nextTab, i, ln);   //复制到新数组中
                            setTabAt(nextTab, i + n, hn);  //复制到新数组中
                            setTabAt(tab, i, fwd);  //将原数组中相应位置为fwd，表示该位置已经被处理过
                            advance = true;  //继续进行遍历
                        }
                    }
                }
            }
        }
    }
    
```

主要


sizeClt主要是表明有几个线程正在进行扩容操作，我们主要通过U.compareAndSwapInt来改变sizeCTL来实现


```java
    //helpTransfer函数的主要作用是如果有线程正在进行扩容操作,则帮助其他线程进行扩容操作
    final Node<K,V>[] helpTransfer(Node<K,V>[] tab, Node<K,V> f) {
        Node<K,V>[] nextTab; int sc;
        if (tab != null && (f instanceof ForwardingNode) &&
            (nextTab = ((ForwardingNode<K,V>)f).nextTable) != null) {   //帮助进行扩容
            int rs = resizeStamp(tab.length);
            while (nextTab == nextTable && table == tab &&
                   (sc = sizeCtl) < 0) {
                if ((sc >>> RESIZE_STAMP_SHIFT) != rs || sc == rs + 1 ||
                    sc == rs + MAX_RESIZERS || transferIndex <= 0)
                    break;
                //CAS修改sizeCtl=sizeCtl+1,表示新增加一个线程辅助扩容
                if (U.compareAndSwapInt(this, SIZECTL, sc, sc + 1)) {  
                   //
                    transfer(tab, nextTab);  // 最终还是调用transfer 来完成
                   
                    break;
                }
            }
            return nextTab;
        }
        return table;
    }

```





![](https://github.com/wabc1994/InterviewRecord/blob/master/Java基础/图片/transfer.jpg)




整个扩容操作分为两个部门
1. 单线程完成构建nexttable工作，容量变为原来的两倍
2. 多线程完成将原table当中的元素复制到nextTable中，这里允许多线程进行操作。




# 乐观锁、悲观锁、加锁和无锁

乐观锁和悲观锁是从宏观面上面来讲的， 是一种思想。实际映射到并发编程中那就是加锁和无锁

这两种派系映射到并发编程中就如同加锁与无锁的策略，即加锁是一种悲观策略，无锁是一种乐观策略，因为对于加锁的并发程序来说，它们总是认为每次访问共享资源时总会发生冲突，因此必须对每一次数据操作实施加锁策略。 
而无锁则总是假设对共享资源的访问没有冲突，线程可以不停执行，无需加锁，无需等待，一旦发现冲突，无锁策略则采用一种称为CAS的技术来保证线程执行的安全性，这项CAS技术就是无锁策略实现的关键，下面我们进一步了解CAS技术的奇妙之处。



## 关于是如何从加锁进化到无锁的情况?

- [从加锁到无锁](https://dzone.com/articles/how-cas-compare-and-swap-java)



- [Java中的CAS实现](https://howtodoinjava.com/java/multi-threading/compare-and-swap-cas-algorithm/)

# 简单总结对别

## jdk1.7

两个内部类
1. Segement 继承ReetrantLock,采用分段锁的基本概念, HashEntry代表一个键值对

2. HashEntry<key,value> 链表当中的节点，key,hash,next 等字段声明为final,不可更改的情况，这样可以减少过线程之间的同步操作，**不变性**

3. 数组+ 链表+ + 红黑树等概念

4. 在put, remove、put 方法都是要lock 然后再try{完成主要的操作逻辑}最后在finally当中的 释放lock，然后再在get 方法当中是不用加锁，不用执行代码块 


## jdk1.8

主要设计上的变化有以下几点:

1. 不采用segment而采用node，锁住node来实现减小锁粒度。

2. 设计了MOVED状态 当resize的中过程中 线程2还在put数据，线程2会帮助resize。

3. 使用3个CAS操作来确保node的一些操作的原子性，这种方式代替了锁。实质上put,set,get

4. sizeCtl的不同值来代表不同含义，起到了控制的作用。

# 源码阅读
[jdk1.8ConcurrentHashMap](https://blog.csdn.net/fjse51/article/details/55260493)

[Java8—ConcurrentHashMap](https://blog.csdn.net/u012834750/article/details/71536618)

[ConcurrentHashMap源码分析](https://blog.csdn.net/u010723709/article/details/48007881)

[并发容器Map之一：ConcurrentHashMap原理(jdk1.8)](https://www.cnblogs.com/duanxz/archive/2012/10/08/2714933.html)

[oncurrentHashMap JDK1.7 源码解析](https://www.cnblogs.com/ITtangtang/p/3948786.html)

