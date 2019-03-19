# CopyOnWriteArrayList
使用CopyOnWriteArrayList可以线程安全地遍历，因为如果另外一个线程在遍历的时候修改List的话，实际上会拷贝出一个新的List上修改，而不影响当前正在被遍历的List

读多写少的并发场景

## 缺点


缺点：

- 耗内存（集合复制）
- 实时性不高 

CopyOnWriteArrayList适合用在“读多，写少”的“并发”应用中，换句话说，它适合使用在读操作远远大于写操作的场景里，比如缓存。它不存在“扩容”的概念，

## 优点
1. 数据一致性完整，为什么？因为加锁了，并发数据不会乱(底层使用ReetrantLock) 添加的时候
2. 解决了集合容器遍历时候的线程同步问题，避免了一个线程在修改，另一个线程在进行正常的读写操作



## 源码


add(),remove()等操作都是利用ReetrantLock锁来实现的基本功能情况


1. ReetrantLock()  


**add()源码**

同一个时间只有一个线程能够进行add()操作,只能进行一份复制


```java
/**
     * Appends the specified element to the end of this list.
     *
     * @param e element to be appended to this list
     * @return {@code true} (as specified by {@link Collection#add})
   
    public boolean add(E e) {
        final ReentrantLock lock = this.lock;//重入锁
        lock.lock();//加锁啦
        try {
            Object[] elements = getArray();
            int len = elements.length;
            Object[] newElements = Arrays.copyOf(elements, len + 1);//拷贝新数组
            newElements[len] = e;
            setArray(newElements);//将引用指向新数组  1
            return true;
        } finally {
            lock.unlock();//解锁啦
        }
    }
```


**remove源码**

```java
public E remove(int index) {
    final ReentrantLock lock = this.lock;
    lock.lock();
    try {
        Object[] elements = getArray();
        int len = elements.length;
        E oldValue = get(elements, index); // 获取volatile数组中指定索引处的元素值
        int numMoved = len - index - 1;
        if (numMoved == 0) // 如果被删除的是最后一个元素，则直接通过Arrays.copyOf()进行处理，而不需要新建数组
            setArray(Arrays.copyOf(elements, len - 1));
        else {
            Object[] newElements = new Object[len - 1];
            System.arraycopy(elements, 0, newElements, 0, index);    // 拷贝删除元素前半部分数据到新数组中
            System.arraycopy(elements, index + 1, newElements, index, numMoved);// 拷贝删除元素后半部分数据到新数组中
            setArray(newElements); // 更新volatile数组
        }
        return oldValue;
    } finally {
        lock.unlock();
    }
```

## reetrantLock

恍然大悟，小样，原来add()在添加集合的时候加上了锁，保证了同步，**避免了多线程写的时候会Copy出N个副本出来**。(想想，你在遍历一个10个元素的集合，每遍历一次有1人调用add方法，你说当你遍历10次，这add方法是不是得被调用10次呢？是不是得copy出10分新集合呢？万一这个集合非常大呢？)



2. 解决了像ArrayList、Vector这种集合多线程遍历迭代问题，记住，Vector虽然线程安全，只不过是加了synchronized关键字，迭代问题完全没有解决！


## 适用场景
1. 读多写少场景
2. 比如黑名单之类的情况


# Vector 
vector是ArrayList 的线程安全版本，利用synchronized关键字实现的，效率比较低，但是vector同样存在一个问题就是

**同样无法解决安全遍历的问题**
>为什么线程安全的Vector也不能线程安全地遍历呢？其实道理也很简单，看Vector源码可以发现它的很多方法都加上了synchronized来进行线程同步，例如add()、remove()、set()、get()，但是Vector内部的synchronized方法无法控制到遍历操作，所以即使是线程安全的Vector也无法做到线程安全地遍历。

# 参考链接
[Java中的CopyOnWriteArrayList ](https://juejin.im/post/5aaa2ba8f265da239530b69e)
