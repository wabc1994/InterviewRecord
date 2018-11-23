# 简单点说
1. 空间预分配，每次扩容1.5倍

2. 底层实现是动态数组(类似于c++中STL中vector)，所以支持随机访问的特性，

3. 不是线程安全的，如果要线程安全使用vector，或者写实复制数组CopyOnWriteArrayList()


# 特性

就是空间预分配机制，扩容为1.5倍，支持随机访问的特性(RandomAccess)，线程不是安全的(首先在单线程环境下使用)

当ArrayList扩容的时候，首先会设置新的存储能力为原来的1.5倍

```java
 int newCapacity = oldCapacity + (oldCapacity >> 1);
```

底层实现是数组 ，支持随机访问的特性

其实这个数组有一个长度设置，最大值为如何

```java
private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;
```

```java
transient Object[] elementData; // non-private to simplify nested class access
```

上述数组[]elementData 是实际存储的数组中东西

1. 默认容量为10，当程序指定的minCapacity 小于默认容量10的时候，容量直接定为10
2. 如果minCapacity大于最小容量，则直接设置为minCapacity

minCapacity <=  newCapacity <= MAX_ARRAY_SIZE

要满足上述的请求，minCapacity 代表需要的最小长度(需要的)

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
    if (newCapacity - MAX_ARRAY_SIZE > 0)
        newCapacity = hugeCapacity(minCapacity);
    // minCapacity is usually close to size, so this is a win:
    elementData = Arrays.copyOf(elementData, newCapacity);
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

# 与LinkedList

底层实现是链表，所以ArrayList的区别本质上就是数组和链表的本质区别

数组支持随机访问的特性(查询性能比较好)， 但是如果是删除和插入(分为在中间插入，和在数组后面插入)， 如果是在中间插入，就要涉及到数组的大量移动，时间复杂度就比较高

数组的大量移动主要涉及到两个操作 

1. Arrays.copyOf()
2. System.arraycopy()

关于这两个函数的区别也可以在网上查找写资料情况

### 为何在设计到大量的删除和插入操作，ArrayList的效率会远远比LinkedList 差很多的情况？

 因为ArrayList是使用数组实现的,若要从数组中删除或插入某一个对象，需要移动后段的数组元素，从而会重新调整索引顺序,调整索引顺序会消耗一定的时间，所以速度上就会比LinkedList要慢许多. 相反,LinkedList是使用链表实现的,若要从链表中删除或插入某一个对象,只需要改变前后对象的引用即可