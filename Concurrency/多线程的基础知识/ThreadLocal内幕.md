# ThreadLocal 的定义

Java中的ThreadLocal类允许我们创建一个只能被一个线程读写的变量，就跟名字一样是线程本地拥有的，不是线程共享的。即使两个线程同时执行这段代码，她们也无法访问到对方的ThreadLocal 变量



ThreadLocal属于线程，ThreadLocalMap由ThreadLocal持有，说到底，ThreadLocalMap 也是线程所持有。每个线程Thread都有自己的ThreadLocalMap。

**如果保证数据的安全性，线程安全**



# 如何工作

ThreadLocalmap就好比是一个Hash，key是对threadlocal的弱引用，value是该线程拥有的值，白了就是实现了说线程隔离这种情况，根本不存在变量共享这种操作**(每个线程都有自己的副本)**，所以就达到了线安全的目的。


## thread、threadlocal、threadlocalmap,Entry

1. ThreadLocal类用于存储以线程为作用域的数据，线程之间数据隔离
2. ThreadLocal类用于存储以线程为作用域的数据，线程之间数据隔离
3. Thread类比较常用，线程类内部维持一个ThreadLocalMap类实例（threadLocals)。

1. 每个线程都有一个threadlocals变量 可以通过getMaps(Thread t)

关系：

1. ThreadLocal的实现是这样的：每个Thread 维护一个Thread维护一个ThreadLocalMap映射表，这个映射表的key是ThreadLocal本身，value 是真正需要存储的 Object。

2. 也就是说 ThreadLocal 本身并不存储值，它只是作为一个 key 来让线程从 ThreadLocalMap 获取 value。值得注意的是图中的虚线，表示 ThreadLocalMap 是使用 ThreadLocal 的弱引用作为 Key 的，弱引用的对象在 GC 时会被回收。

>也就是说我们线程是通过threadlocal找到value, 如果我们的threadlocal被GC挥手了，造成了threadlocalmap当中存在key为空的value, 还是存在一条可达性的thread- threadlocalmap- value的可达性强引用关系，但是实际上这个value是线程永远不
不可能再去使用的一个对象。
那么久会导致这个value不能被GC正常回收，如果线程也不结束的话，那么这条引用就一直存在，永远无法回收，造成内存泄漏。
   




```java
  ThreadLocalMap getMap(Thread t) {
        return t.threadLocals;
    }
```

2. 每个线程可以有多个threadlocal<Integer>, threadloacl<String>,都存储在线程的threadlocalmap，可以这么理解threadlocalmap就好比一个hashmap，key 是对threadlocal的软引用，value就是线程要操作的变脸情况

**java.lang.thread**

```java
 /* ThreadLocal values pertaining to this thread. This map is maintained
     * by the ThreadLocal class. */
    ThreadLocal.ThreadLocalMap threadLocals = null;
```

**Threadlocalmap当中的Entry**

Entry<Threadlocal<?>,Object v>

```java
static class Entry extends WeakReference<ThreadLocal<?>> {
            /** The value associated with this ThreadLocal. */
            Object value;

            Entry(ThreadLocal<?> k, Object v) {
                super(k);
                value = v;
            }
        }
```

# 源码

Threadlocal主要的方法有
1. get  map.getEntry
2. set  map.set
3. remove   map.remove
4. createMap
5. getMap


扩容大小是2/3, 初始容量是16的大小

threadlocal 实质调用的方式是threalocalmap当中的方法


1. get方法


```java
 public T get() {
 // 1. 获得当前线程
        Thread t = Thread.currentThread();
        // 2. 获取该线程的ThreadLocalMap对象 ->>分析1
        ThreadLocalMap map = getMap(t);
        // 3. 若该线程的ThreadLocalMap对象已存在，则替换该Map里的值；否则创建1个ThreadLocalMap对象
        if (map != null) {
        
        
        // 这一步是关键
            ThreadLocalMap.Entry e = map.getEntry(this);
            if (e != null) {
                @SuppressWarnings("unchecked")
                T result = (T)e.value;
                return result;
            }
        }
        // 初始化
        return setInitialValue();
    }



```
2. set方法
类似于map的set方法，设置thread当中的threadlocalmap当前threadlocal的值，**一般而言我们都是只设置一个的形式**一个thread 可以有多个threadlocal变量，

```java
public void set(T value) {
        Thread t = Thread.currentThread();
        ThreadLocalMap map = getMap(t);
        if (map != null)
            map.set(this, value);
        else
            createMap(t, value);
    }
    // 为
    void createMap(Thread t, T firstValue) {
        t.threadLocals = new ThreadLocalMap(this, firstValue);
    }
```
3. remove方法

主要是去除thread当中threadlocalmap里面的threadlocal和object对象,

```java
 public void remove() {
         ThreadLocalMap m = getMap(Thread.currentThread());
         if (m != null)
             m.remove(this);
     }
```

## Threadlocalmap
可以把这个比作hashmap，他同样有自己的hash 函数，扩容函数，get,set remove 等map映射表常用的函数,

1. 表的大小是2的幂
2. 采用链地址方法解决hashmap冲突，
3. 采用懒加载的方式，只有当第一次尝试要添加firstKey, firstValue 才会创建一个表
4. 扩容机制

**ThreadLocalMap提供了一种为ThreadLocal定制的高效实现，并且自带一种基于弱引用的垃圾清理机制。**


# 使用

本质也是创建一个对象map对象，然后进行map.put 和get方法，自动发现到底是哪一个线程使用该变量，这块是自动进行记录。

只需实例化对象一次 & 不需知道它是被哪个线程实例化，如果发现没有初始化就要调用

## 在Runnable任务中创建一个ThreadLocal 对象

每人任务都可以调用这个对象，在runnable中使用这个对象,看那个任务（线程调用该对象，ThreadLocal就get(线程，该线程对应的值情况)）


```java
 private T setInitialValue() {
        T value = initialValue();
        Thread t = Thread.currentThread();
        ThreadLocalMap map = getMap(t);
        if (map != null)
            map.set(this, value);
        else
            createMap(t, value);
        return value;
    }
```

# 与ThreadPoolExectors线程池一起使用带来什么问题
正常情况下，线程执行完之后就死忙了，与该线程有关的私有变量也就会被GC所搜集，但是在线程池当中的线程是可以重用，也就是线程不会死忙，导致与线程相关的变量不能被GC收集，导致内存泄漏。

这个问题也就是该如何清空线程相关的thread threadlocals里面的变量情况

threadLocal好的使用习惯，是每次使用完ThreadLocal，都调用它的remove()方法，清除数据不再使用的Entry() 方法。


## 非线程池方式使用
**为何会出现该种问题？**

>我们知道ThreadLocal变量是维护在Thread内部的，这样的话只要我们的线程不退出，对象的引用就会一直存在。当线程退出时，Thread类会进行一些清理工作，其中就包含ThreadLocalMap，Thread调用exit方法如下


## 线程池方式使用
但是，当我们使用线程池的时候，就意味着当前线程未必会退出（比如固定大小的线程池，线程总是存在的）。如果这样的话，将一些很大的对象设置到ThreadLocal中（这个很大的对象实际保存在Thread的threadLocals属性中），这样的话就可能会出现内存溢出的情况。

也就是说，ThreadLocal在没有线程池使用的情况下，正常情况下不会存在内存泄露，但是如果使用了线程池的话，就依赖于线程池的实现，如果线程池不销毁线程的话，那么就会存在内存泄露。所以我们在使用线程池的时候，使用ThreadLocal要格外小心！


**正确的使用方式**
>为了安全地使用ThreadLocal，必须要像每次使用完锁就解锁一样，在每次使用完ThreadLocal后都要调用remove()来清理无用的Entry。


[ThreadLocal造成OOM内存溢出案例演示与原理分析](https://blog.csdn.net/xlgen157387/article/details/78298840)

[Java多线程编程-多图深入分析ThreadLocal原理,主要是看后的情况](https://blog.csdn.net/xlgen157387/article/details/78297568)

# 与同步机制的比较

对于多线程的资源共享问题，同步机制采用了"以时间换空间""的方式，而ThreadLocal 采用了"以空间换时间的""方式

1. 前者是准备一份变量，让不同的线程排队访问，而后者为每一线程都提供了一份变量，因此可以同时访问而互不影响的情况实现




# 参考链接

[(6条消息)ThreadLocal源码分析（JDK8)](https://blog.csdn.net/u010887744/article/details/54730556)