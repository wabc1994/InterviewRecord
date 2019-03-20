# 简单点说
1. 空间预分配，每次扩容1.5倍

2. 底层实现是动态数组(类似于c++中STL中vector)，所以支持随机访问的特性，

3. 不是线程安全的，如果要线程安全使用vector，或者写实复制数组CopyOnWriteArrayList()


就是空间预分配机制，扩容为1.5倍，支持随机访问的特性(RandomAccess)，线程不是安全的(首先在单线程环境下使用)

当ArrayList扩容的时候，首先会设置新的存储能力为原来的1.5倍，vector容量扩容为两倍


1. 面试回答要点
    - 扩容机制
    - fail-fast机制（ 快速失败机制) 多线程并发修改异常 modCount
    - 线程安全性等问题
    - 基础特性，数据结构

## 基本流程


1. add(E e)
    1. ensureCapacityInternal(size+1);
    elementData[size++]= e
2. ensureCapacityInternal(minCapacity)
    1. ensureExplicitCapacity(minCapacity)
3. ensureExplicityCapacity(minCapacity)
    - grow(minCapacity)
4. grow
    - 判断与MAX_ARRAY_SIZE上限条件等情况

**核心**

```java
 int newCapacity = oldCapacity + (oldCapacity >> 1);
```

为何要采用1.5倍
>在这里有一个疑问，为什么每次扩容处理会是 1.5 倍，而不是 2.5、3、4 倍呢？通过 google 查找，发现 1.5 倍的扩容是最好的倍数。因为一次性扩容太大(例如 2.5 倍)可能会浪费更多的内存(1.5 倍最多浪费 33%，而 2.5 被最多会浪费 60%，3.5 倍则会浪费 71%……)。但是一次性扩容太小，需要多次对数组重新分配内存，对性能消耗比较严重。所以 1.5 倍刚刚好，既能满足性能需求，也不会造成很大的内存消耗。
底层实现是数组 ，支持随机访问的特性

**最大容量限制**

```java
private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
```

**底层出处**

```java
transient Object[] elementData; // non-private to simplify nested class access
```
上述数组[]elementData 是实际存储的数组中东西

1. 默认容量为10，当程序指定的minCapacity 小于默认容量10的时候，容量直接定为10
2. 如果minCapacity大于最小容量，则直接设置为minCapacity

minCapacity <=  newCapacity <= MAX_ARRAY_SIZE

要满足上述的请求，minCapacity 代表需要的最小长度(需要的)


**modCount++**

主要是为了机制的实现情况的fast-fail 为了解决多线程的问题，当一个线程打算修改一个集合的结构，而另一个线程正在进行遍历该集合，那么久会抛出异常的情况，


初步知道fail-fast产生的原因就在于程序在对 collection 进行迭代时，某个线程对该 collection 在结构上对其做了修改，这时迭代器就会抛出 ConcurrentModificationException 异常信息，从而产生 fail-fast。
>当方法检测到对象的并发修改，但不允许这种修改时就抛出该异常。


**改进**

modCount++ 下面的fast-fail机制是有问题的，所以后面出现了改进方案就有了写时复制数组CopyOnwrite

**minCapacity**



## 源码实现

size代表没有进行扩容之前数组的已有元素大小，size+1就是minCapacity代表我们需要的最少大小，至少要有这么大

1. add(E e)

```java
public boolean add(E e) {
        ensureCapacityInternal(size + 1);  // Increments modCount!!
        elementData[size++] = e;
        return true;
    }
```


2. ensureCapacityInternal(int minCapacity) 确保参数对了就可以了

```java
 private void ensureCapacityInternal(int minCapacity) {
    
    // 如果elementData是通过调用默认方法的实现的， 
        if (elementData == DEFAULTCAPACITY_EMPTY_ELEMENTDATA) {
            minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity);
        }

        ensureExplicitCapacity(minCapacity);
```  

3. ensureExplicitCapacity()  
```java
     private void ensureExplicitCapacity(int minCapacity) {
           //
            modCount++;
     
            // overflow-conscious code
            if (minCapacity - elementData.length > 0)
                grow(minCapacity);
        }

```

4. grow方法
```java
private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
//最大数组最大
/**
 * Increases the capacity to ensure that it can hold at least the
 * number of elements specified by the minimum capacity argument.
 *
 * @param minCapacity the desired minimum capacity
 */
private void grow(int minCapacity) {
    // overflow-conscious code，
    // 原始数组长度
    int oldCapacity = elementData.length;
    int newCapacity = oldCapacity + (oldCapacity >> 1);
    //如果数组新的长度比最小的，直接采用minCapacity
    
    if (newCapacity - minCapacity < 0)
        newCapacity = minCapacity;
    //hugaCapacity
    if (newCapacity - MAX_ARRAY_SIZE > 0)
        newCapacity = hugeCapacity(minCapacity);
    // minCapacity is usually close to size, so this is a win:
    elementData = Arrays.copyOf(elementData, newCapacity);
}
```

5. 确保新参数不会达到上限的情况

newCapacity大于ArrayList的所允许的最大容量,处理

```java
   private static int hugeCapacity(int minCapacity) {
       if (minCapacity < 0) // overflow, 不符合要求
           throw new OutOfMemoryError();
       //
       return (minCapacity > MAX_ARRAY_SIZE) ?
           Integer.MAX_VALUE :
           MAX_ARRAY_SIZE;
   }
```

## 扩容过程

上述的判断步骤主要是下面的情况

1. 得到当前的ArrayList的容量(oldCapacity)。
2. 计算除扩容后的新容量(newCapacity)，其值(oldCapacity + (oldCapacity >>1))约是oldCapacity 的1.5倍。
3. 这里采用的是移位运算。为什么采用这种方法呢？应该是出于效率的考虑。
4. 当newCapacity小于所需最小容量，那么将所需最小容量赋值给newCapacity。
5. newCapacity大于ArrayList的所允许的最大容量,处理。进行数据的复制，完成向ArrayList实例添加元素操作。

接下看才查看hugeCapacity(minCapacity )



# 快速失败机制fast-fail机制
使用迭代器的时候会出现这个问题，就是迭代器类内部又一个ExpectmodCount变量,在调用构造函数初始化该变量的时候

就让集合类的Arraylist对象的modCount =expectmodCount

迭代器每次往下面走的话都要进行一个判断的情况

```java
final Entry<K,V> nextEntry() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
```

在迭代过程中，判断 modCount 跟 expectedModCount 是否相等，如果不相等就表示已经有其他线程修改了,就会抛出快速失败机制等情况



# 如何解决fail-fast机制问题

1. 在遍历的过程当中所有涉及到改变modCount值的地方全部都得synchronized或者直接使用Collections.synchronizedList, 这样可以解决。但是不推荐，因为增删造成的同步锁会可能阻塞遍历操作

2. 方案二推荐使用CopyOnwriteArrayList 来替换ArrayList,

# 与LinkedList

底层实现是双向循环链表，所以ArrayList的区别本质上就是数组和链表的本质区别

数组支持随机访问的特性(查询性能比较好)， 但是如果是删除和插入(分为在中间插入，和在数组后面插入)， 如果是在中间插入，就要涉及到数组的大量移动，时间复杂度就比较高



**双向循环链表**

```java
transient int size = 0;
 
    /**
     * Pointer to first node.
     * Invariant: (first == null && last == null) ||
 *            (first.prev == null && first.item != null)
     */
    transient Node<E> first;
 
    /**
     * Pointer to last node.
     * Invariant: (first == null && last == null) ||
     *            (last.next == null && last.item != null)
     */
    transient Node<E> last;

```

数组的大量移动主要涉及到两个操作 

1. Arrays.copyOf()
2. System.arraycopy()

关于这两个函数的区别也可以在网上查找写资料情况

### 为何在设计到大量的删除和插入操作，ArrayList的效率会远远比LinkedList 差很多的情况？

因为ArrayList是使用数组实现的,若要从数组中删除或插入某一个对象，需要移动后段的数组元素，从而会重新调整索引顺序,
调整索引顺序会消耗一定的时间，所以速度上就会比LinkedList要慢许多. 

相反,LinkedList是使用链表实现的,若要从链表中删除或插入某一个对象,只需要改变前后对象的引用即可

# 参考链接

[ArrayList - Java 提高篇 - 极客学院Wiki](http://wiki.jikexueyuan.com/project/java-enhancement/java-twentyone.html)