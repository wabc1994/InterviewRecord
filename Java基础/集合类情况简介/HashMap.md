[TOC]



# 总体思路

从总体上把握，设计得最初目标，取一个元素时间复杂度为O(1), 在冲突那里，如果发生冲突的元素变得多了，链表变得很长，查找一个元素就会变成遍历一遍链表，在这种情况下，遍历一个链表的时间复杂为O(N),这与设计之初的目标是相违背的，所以当链表的长度很长，大于8个节点时，就使用红黑树来进行



>想一想HashMap设计的初衷，想用key通过时间复杂度O（1）内拿到对应的Value，对吧，那现实中是会有hash冲突的，那有冲突咱们必须解决啊，所以链表就形成了，对吧？为什么要设计链表，知道了吧？因为要解决冲突，那链表的出现带来了一个问题，HashMap设计的初衷，想用key通过时间复杂度O（1）内拿到对应的Value，但是链表变长是不是就对时间复杂度O（1）相违背了呢（因为定位到一个数组之后，还需要遍历一下链表才能通过key拿到value）？于是乎有了链表长度达到8之后 会进行转红黑树，对吧？所以转红黑树这个事情其实 服务于HashMap设计初衷：用key通过时间复杂度O（1）内拿到对应的Value。这些东西可能需要看一下源码，你才能理解得更清楚。

#  什么叫hash？

## 原理

hash方法的功能是根据Key来定位这个K-V在链表数组中的位置的。也就是hash方法的输入应该是个Object类型的Key，输出应该是个int类型的数组下标，key.hashcode()的整体取值范围为 -2^32  到2^32-1 ,32位的概括，整数的情况

## 实现

我们只要调用Object对象的hashCode()方法，该方法会返回一个整数，然后用这个数对HashMap或者HashTable的容量进行取模就行了。

任何对象都有hashCode(), 所以无论key是什么类型的值，最终都可以直接转换为统一的整形数据

## 例如

在整个JDK1.7当中，主要有两个函数来

>hash ：该方法主要是将Object转换成一个整型。
>
>indexFor ：该方法主要是将hash生成的整型转换成链表数组中的下标。

```java
final int hash(Object k) {
    int h = hashSeed;
    if (0 != h && k instanceof String) {
        return sun.misc.Hashing.stringHash32((String) k);
    }

    h ^= k.hashCode();
    h ^= (h >>> 20) ^ (h >>> 12);
    return h ^ (h >>> 7) ^ (h >>> 4);
}

static int indexFor(int h, int length) {
    return h & (length-1);
}

```

在JDK1.8后改变了hash的方式, 主要改变了扰动函数的设置，后面会具体介绍JDK1.8中的扰动函数是如何设置的情况。

在这里JDK1.7是做了四次的Hash 扰动函数

```java
 h ^= k.hashCode();
    h ^= (h >>> 20) ^ (h >>> 12);
    return h ^ (h >>> 7) ^ (h >>> 4)
```

关于Java JDK1.7hash 函数的官方描述

> **使用hash()方法对一个对象的hashCode进行重新计算是为了防止质量低下的hashCode()函数实现。由于hashMap的支撑数组长度总是 2 的幂次，通过右移可以使低位的数据尽量的不同，从而使hash值的分布尽量均匀。**

也可以查看下面的情况

[JDK1.7扰动函数详解](http://www.iteye.com/topic/709945)

而JDK1.8则进行了优化，简化，直接结合高位16位和低16位，进行一次扰动函数设置即可

> 关于Java 8中的hash函数，原理和Java 7中基本类似。Java 8中这一步做了优化，只做一次16位右位移异或混合，而不是四次，但原理是不变的。

```java
static final int hash(Object key) {
    int h;
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}
```

上面是得到hash值，然后还要通过对容量做模运算，得到index数组

> 在JDK1.8的实现中，优化了高位运算的算法，通过hashCode()的高16位异或低16位实现的：(h = k.hashCode()) ^ (h >>> 16)，主要是从速度、功效、质量来考虑的。以上方法得到的int的hash值，然后再通过`h & (table.length -1)`来得到该对象在数据中保存的位置。





# 源码解读

关键部分的情况

其底层数据结构是**数组**称之为**哈希桶**，每个**桶里面放的是链表**，链表中的**每个节点**，就是哈希表中的**每个元素**。

## 数据结构

数组+链表（红黑树jdk1.8之后，当链表长度大于8时） 

链表中节点

```java
static class Node<K,V> implements Map.Entry<K,V> {
        final int hash;//hash
        final K key;  //key 不可修改
        V value; 
        Node<K,V> next; //下一个节点

        Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    //每一个节点的hash值，是将key的hashCode 和 value的hashCode 亦或得到的。
        public final int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }
每一个节点的hash值，是将key的hashCode 和 value的hashCode 亦或得到的。
```

数组部分

Node<K,V> [] table 

这个是数组链表

### ![hashmap](https://github.com/wabc1994/InterviewRecord/tree/master/Java%E5%9F%BA%E7%A1%80/%E5%9B%BE%E7%89%87)

## 基础参数设置

区别容量的东西，其中容量跟size不是同一个东西

### 默认容量

```java
static final int DEFAULT_INITIAL_CAPACITY = 1<<4
```

底层hash 的数组大小为16，每个数组链接一个链表，均使用位运算，提升运算速度，数组的大小一定为2的指数

### 最大容量

```
 static final int MAXIMUM_CAPACITY = 1 << 30;
```

2 ^30 

### 默认的加载因子

为0.75

```java
static final int float DEFAULT_LOAD_FACTOR =0.75f
```

### 当前HashMap修改次数

```java
transient int modCount
```

阈值

下次需要进行扩容时，HashMap中的键值对数目，等于容量加载因子

```
int threshold
```

//哈希表内元素数量的阈值，当哈希表内元素数量超过阈值时，会发生扩容resize()。

## 红黑树节点情况

```
static final class TreeNode<K,V> extends LinkedHashMap.Entry<K,V> {
    TreeNode<K,V> parent; // 父亲节点
    TreeNode<K,V> left; // 左子树
    TreeNode<K,V> right; //右子树
    TreeNode<K,V> prev;
    boolean red;// 颜色属性
}
```

# 三种数据结构

1. 数组，也就是hash桶
2. 链表
3. 红黑树

# 扩容机制

由于HashMap扩容开销很大(需要创建数组，链表，重新哈希以及分配)，因此扩容需要遵守一定的机制

- 容量

- 加载因子:决定了 HashMap 中的元素占有多少比例时进行扩容

  HashMap的默认加载因子为0.75，这是在时间、空间两个方面均衡下的结果：

  - 加载因子太大的话发生冲突的可能性就会大，查找的效率反而变得低了。
  - 太小的话频繁rehash, 导致性能降低





  扩容带来的性能影响：扩容操作时，会new一个新的`Node`数组作为哈希桶，然后将原哈希表中的所有数据(`Node`节点)移动到新的哈希桶中，相当于对原哈希表中所有的数据重新做了一个put操作。所以性能消耗很大，**可想而知，在哈希表的容量越大时，性能消耗越明显。**



  扩容部分核心内容机制

  ```java
  final Node<K,V>[] resize() {
          //oldTab 为当前表的哈希桶
          Node<K,V>[] oldTab = table;
          //当前哈希桶的容量 length
          int oldCap = (oldTab == null) ? 0 : oldTab.length;
          //当前的阈值
          int oldThr = threshold;
          //初始化新的容量和阈值为0
          int newCap, newThr = 0;
          //如果当前容量大于0
          if (oldCap > 0) {
              //如果当前容量已经到达上限
              if (oldCap >= MAXIMUM_CAPACITY) {
                  //则设置阈值是2的31次方-1
                  threshold = Integer.MAX_VALUE;
                  //同时返回当前的哈希桶，不再扩容
                  return oldTab;
              }//否则新的容量为旧的容量的两倍。 
              else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
                       oldCap >= DEFAULT_INITIAL_CAPACITY)//如果旧的容量大于等于默认初始容量16
                  //那么新的阈值也等于旧的阈值的两倍
                  newThr = oldThr << 1; // double threshold
          }
      
      //如果当前表是空的，但是有阈值。代表是初始化时指定了容量、阈值的情况，只是进行使用而已
          else if (oldThr > 0) // initial capacity was placed in threshold
              newCap = oldThr;//那么新表的容量就等于旧的阈值，这里就不是扩容，而是缩小的情况
          else {}//如果当前表是空的，而且也没有阈值。代表是初始化时没有任何容量/阈值参数的情况               // zero initial threshold signifies using defaults
              newCap = DEFAULT_INITIAL_CAPACITY;//此时新表的容量为默认的容量 16
              newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);//新的阈值为默认容量16 * 默认加载因子0.75f = 12
          }
          if (newThr == 0) {//如果新的阈值是0，对应的是  当前表是空的，但是有阈值的情况
              float ft = (float)newCap * loadFactor;//根据新表容量 和 加载因子 求出新的阈值
              //进行越界修复
              newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ?
                        (int)ft : Integer.MAX_VALUE);
          }
          //更新阈值 
          threshold = newThr;
          @SuppressWarnings({"rawtypes","unchecked"})
          //根据新的容量 构建新的哈希桶
              Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
          //更新哈希桶引用
          table = newTab;
          //如果以前的哈希桶中有元素
          //下面开始将当前哈希桶中的所有节点转移到新的哈希桶中
          if (oldTab != null) {
              //遍历老的哈希桶
              for (int j = 0; j < oldCap; ++j) {
                  //取出当前的节点 e
                  Node<K,V> e;
                  //如果当前桶中有元素,则将链表赋值给e
                  if ((e = oldTab[j]) != null) {
                      //将原哈希桶置空以便GC
                      oldTab[j] = null;
                      //如果当前链表中就一个元素，（没有发生哈希碰撞）
                      if (e.next == null)
                          //直接将这个元素放置在新的哈希桶里。
                          //注意这里取下标 是用 哈希值 与 桶的长度-1 。 由于桶的长度是2的n次方，这么做其实是等于 一个模运算。但是效率更高
                          newTab[e.hash & (newCap - 1)] = e;
                          //如果发生过哈希碰撞 ,而且是节点数超过8个，转化成了红黑树（暂且不谈 避免过于复杂， 后续专门研究一下红黑树）
                      else if (e instanceof TreeNode)
                          ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);
                      //如果发生过哈希碰撞，节点数小于8个。则要根据链表上每个节点的哈希值，依次放入新哈希桶对应下标位置。
                      else { // preserve order
                          //因为扩容是容量翻倍，所以原链表上的每个节点，现在可能存放在原来的下标，即low位， 或者扩容后的下标，即high位。 high位=  low位+原哈希桶容量
                          //低位链表的头结点、尾节点
                          Node<K,V> loHead = null, loTail = null;
                          //高位链表的头节点、尾节点
                          Node<K,V> hiHead = null, hiTail = null;
                          Node<K,V> next;//临时节点 存放e的下一个节点
                          do {
                              next = e.next;
                              //这里又是一个利用位运算 代替常规运算的高效点： 利用哈希值 与 旧的容量，可以得到哈希值去模后，是大于等于oldCap还是小于oldCap，等于0代表小于oldCap，应该存放在低位，否则存放在高位
                              if ((e.hash & oldCap) == 0) {
                                  //给头尾节点指针赋值
                                  if (loTail == null)
                                      loHead = e;
                                  else
                                      loTail.next = e;
                                  loTail = e;
                              }//高位也是相同的逻辑
                              else {
                                  if (hiTail == null)
                                      hiHead = e;
                                  else
                                      hiTail.next = e;
                                  hiTail = e;
                              }//循环直到链表结束
                          } while ((e = next) != null);
                          //将低位链表存放在原index处，
                          if (loTail != null) {
                              loTail.next = null;
                              newTab[j] = loHead;
                          }
                          //将高位链表存放在新index处
                          if (hiTail != null) {
                              hiTail.next = null;
                              newTab[j + oldCap] = hiHead;
                          }
                      }
                  }
              }
          }
          return newTab;
      }
  
  ```

  总结一下

  容量扩容为原来的两倍，阈值也扩展为原来的两倍，细节部分还是要经过一些的判断函数的，



  什么时候进行扩容

  ```java
  int s = m.size();
  if(s>threshold)
      resize();
  ```



  - 如果旧的容量满足要求(小于最大容量)

    - 小于最大容量MAX_CAP,则容量扩容为原来的两倍，阈值同样两倍
    - 如果大于MAX_CAP的话就不再进行扩容了



    ```java
     if (oldCap > 0) {
                //如果当前容量已经到达上限
                if (oldCap >= MAXIMUM_CAPACITY) {
                    //则设置阈值是2的31次方-1
                    threshold = Integer.MAX_VALUE;
                    //同时返回当前的哈希桶，不再扩容
                    return oldTab;
                }//否则新的容量为旧的容量的两倍。 
                else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
                         oldCap >= DEFAULT_INITIAL_CAPACITY)//如果旧的容量大于等于默认初始容量16
                    //那么新的阈值也等于旧的阈值的两倍
                    newThr = oldThr << 1; // double threshold
            }
    ```

  - 如果旧的容量为0，但是阈值不为0

    - 也就是指定了初始容量，只是没有使用，**这种情况就不是扩容了，而是缩小，让容量等于阈值即可**

  ```java
  //如果当前表是空的，但是有阈值。代表是初始化时指定了容量、阈值的情况，只是进行使用而已
          else if (oldThr > 0 ) // initial capacity was placed in threshold //oldCap==0
              newCap = oldThr;//那么新表的容量就等于旧的阈值，这里就不是扩容，而是缩小的情况
  ```



  - 如果当前的阈值为空，容量也为空的话
    - 重新进行初始化即可，设置容量为16，阈值为12

  ```java
  lse {}//如果当前表是空的，而且也没有阈值。代表是初始化时没有任何容量/阈值参数的情况               // zero initial threshold signifies using defaults
              newCap = DEFAULT_INITIAL_CAPACITY;//此时新表的容量为默认的容量 16
              newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);//新的阈值为默认容量16 * 默认加载因子0.75f = 12
          }
  ```

  # 红黑树进解决冲突部分的情况



  主要涉及到将原有oldtable 中的元素复制到旧的newtable 中当去即可，该种情况 

  - 如果没有冲突，即使链表中只有一个元素，还是使用原来的链表情况

  ```java
  if (e.next == null)
                          //直接将这个元素放置在新的哈希桶里。
                          //注意这里取下标 是用 哈希值 与 桶的长度-1 。 由于桶的长度是2的n次方，这么做其实是等于 一个模运算。但是效率更高
                          newTab[e.hash & (newCap - 1)] = e;
  ```



  - 如果发生冲突，并且链表节点数目超过8个，插入红黑树的情况，使用红黑树进行储存

    ```java
    //如果发生过哈希碰撞 ,而且是节点数超过8个，转化成了红黑树（暂且不谈 避免过于复杂， 后续专门研究一下红黑树）
                        else if (e instanceof TreeNode)
                            ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);
                        //如果发生过哈希碰撞，节点数小于8个。则要根据链表上每个节点的哈希值，依次放入新哈希桶对应下标位置。
    ```

  数组辅助到新的数组中，分红黑树和链表讨论

  - 如果发生冲突，并且链表的数目小于8个，不适用红黑树进行存储，直接使用链表，直接进行归为操作

  ```java
   else { // preserve order
                          //因为扩容是容量翻倍，所以原链表上的每个节点，现在可能存放在原来的下标，即low位， 或者扩容后的下标，即high位。 high位=  low位+原哈希桶容量
                          //低位链表的头结点、尾节点
                          Node<K,V> loHead = null, loTail = null;
                          //高位链表的头节点、尾节点
                          Node<K,V> hiHead = null, hiTail = null;
                          Node<K,V> next;//临时节点 存放e的下一个节点
                          do {
                              next = e.next;
                              //这里又是一个利用位运算 代替常规运算的高效点： 利用哈希值 与 旧的容量，可以得到哈希值去模后，是大于等于oldCap还是小于oldCap，等于0代表小于oldCap，应该存放在低位，否则存放在高位
                              if ((e.hash & oldCap) == 0) {
                                  //给头尾节点指针赋值
                                  if (loTail == null)
                                      loHead = e;
                                  else
                                      loTail.next = e;
                                  loTail = e;
                              }//高位也是相同的逻辑
                              else {
                                  if (hiTail == null)
                                      hiHead = e;
                                  else
                                      hiTail.next = e;
                                  hiTail = e;
                              }//循环直到链表结束
                          } while ((e = next) != null);
                          //将低位链表存放在原index处，
                          if (loTail != null) {
                              loTail.next = null;
                              newTab[j] = loHead;
                          }
                          //将高位链表存放在新index处
                          if (hiTail != null) {
                              hiTail.next = null;
                              newTab[j + oldCap] = hiHead;
                          }
                      }
                  }
              }
          }
  ```



  上面的代码包括两个部分：

  - 扩容机制

  - 重新进行hash, 将旧的东西复制到新的链表中去吧，这里要考虑是否冲突了，当某个链表长度大于1个元素，即是有多个元素时，就是发生了冲突，在这里要注意重新hash后的区别点，

  - 在重新hash的过程中，原属于一个桶中的Entry对象可能被分配到不同的桶中去了，因为HashMap的容量发生了改变，那么得到的hash值也会发生改变。

  - ### 


# jdk1.7 与jdk 1.8的区别

相比于之前的版本， JDK1.8之后在解决哈希冲突时有了较大的变化，当链表长度大于阈值（默认为8）时，将链表转化为红黑树，以减少搜索时间。

#### HashMap1.7与1.8的区别

- 1. 处理哈希码的方式
     1.7处理hashCode采用了4次位运算加5次异或运算
     1.8处理hashCode采用了仅使用了一次位运算和一次异或运算，key如果为null,计算结果为0
- 2. 扩容时链表的插入方式
     1.7采用头插法，扩容的时候会造成链表逆序，容易出现环形链表
     并发插入时会出现数据丢失，因为并发时拿到的链头可能不是最新的链头，会出现后面的覆盖掉前面数据的情况
     1.8采用尾插法，不会出现链表逆序，不容易出现环形链表
- 3. 数据结构
     1.7采用数组+链表
     1.8采用数组+链表+红黑树

# 与Hashtable的对比

**HashMap 和 Hashtable 的区别**

1. **线程是否安全：** HashMap 是非线程安全的，HashTable 是线程安全的；HashTable 内部的方法基本都经过 `synchronized` 修饰。（如果你要保证线程安全的话就使用 ConcurrentHashMap 吧！）；
2. **效率：** 因为线程安全的问题，HashMap 要比 HashTable 效率高一点。另外，HashTable 基本被淘汰，不要在代码中使用它；
3. **对Null key 和Null value的支持：** HashMap 中，null 可以作为键，这样的键只有一个，可以有一个或多个键所对应的值为 null。。但是在 HashTable 中 put 进的键值只要有一个 null，直接抛出 NullPointerException。
4. **初始容量大小和每次扩充容量大小的不同 ：** ①创建时如果不指定容量初始值，Hashtable 默认的初始大小为11，之后每次扩充，容量变为原来的2n+1。HashMap 默认的初始化大小为16。之后每次扩充，容量变为原来的2倍。②创建时如果给定了容量初始值，那么 Hashtable 会直接使用你给定的大小，而 HashMap 会将其扩充为2的幂次方大小（HashMap 中的`tableSizeFor()`方法保证，下面给出了源代码）。也就是说 HashMap 总是使用2的幂作为哈希表的大小,后面会介绍到为什么是2的幂次方。
5. **底层数据结构：** JDK1.8 以后的 HashMap 在解决哈希冲突时有了较大的变化，当链表长度大于阈值（默认为8）时，将链表转化为红黑树，以减少搜索时间。Hashtable 没有这样的机制。

# 长度为何是2的次幂

对于取余运算，对于取余运算，我们首先想到的是：哈希值%length = bucketIndex。但当底层数组的length为2的n次方时， h&(length - 1) 就相当于对length取模，而且速度比直接取模快得多，这是HashMap在速度上的一个优化。除此之外，HashMap 的底层数组长度总是2的n次方的主要原因是什么呢？



## 为何需要进行取模运算

`return h & (length-1);`是什么意思呢？其实，他就是取模。Java之所有使用位运算(&)来代替取模运算(%)，最主要的考虑就是效率。**位运算(&)效率要比代替取模运算(%)高很多，主要原因是位运算直接对内存数据进行操作，不需要转成十进制，因此处理速度非常快。**

**使用位运算取代传统的取模运算，可以提高效率**

> **位运算(&)效率要比代替取模运算(%)高很多，主要原因是位运算直接对内存数据进行操作，不需要转成十进制，因此处理速度非常快。**

中间还差解释部分的情况

HashMap 的底层数组长度总是2的n次方的原因有两个，即当 length=2^n 时：

> h %2^n  = h & (2^n-1) ,这种方法等效却不等价，位运算直接不使用转换为10进制，直接利用计算机中的二进制0和1进行运算，提高了运算效率的问题，包括之前的各种默认值的计算全都是位运算的情况



更加详细的一个说明情况如下

>X % 2^n = X & (2^n – 1)
>
>2^n表示2的n次方，也就是说，一个数对2^n取模 == 一个数和(2^n – 1)做按位与运算 。假设n为3，则2^3 = 8，表示成2进制就是1000。2^3 = 7 ，即0111
>
>此时X & (2^3 – 1) 就相当于取X的2进制的最后三位数。从2进制角度来看，X / 8相当于 X >> 3，即把X右移3位，此时得到了X / 8的商，而被移掉的部分(后三位)，则是X % 8，也就是余数。

1.**不同的hash值发生碰撞的概率比较小，这样就会使得数据在table数组中分布较均匀，空间利用率较高，查询速度也较快**

2.**h&(length - 1) 就相当于对length取模，而且在速度、效率上比直接取模要快得多，即二者是等价不等效的，这是HashMap在速度和效率上的一个优化。**

## 普通取模运算带来的问题？

对于hashcode 很大的key, 无论是用取模运算还是位运算都无法直接解决冲突较大的问题。比如：`CA11 0000`和`0001 0000`在对`0000 1111`进行按位与运算后的值是相等的。CA11 0000 和0001 0000 都是比较大的hashcode, 与2^4-1做与位运算结果都是相同的？



> 如果某个一次，考虑极端情况，我们对所有的key进行hashcode 都得到比较大的二进制数，这个hash值跟容量进行与位运算，最终得到的数组下标（桶下标）都是一样的情况，这样就造成了很大的冲突。

这个问题在JDK1.7和JDK1.8中的处理是不一样的情况

JDK1.8中，综合利用高位和低位的因素 h>>>16,就是讲hash右移动16位(一共有32位)

> h = h^(h>>>16) ,低位和高位的数据都可以得到了

比如下面这种情况，这是JDK1.8中的扰动函数设置

![图片](https://github.com/wabc1994/InterviewRecord/blob/master/Java%E5%9F%BA%E7%A1%80/%E5%9B%BE%E7%89%87/hash.png)



> 这样做的目的就在于你求于的时候包含了高16位和第16位的特性 也就是说你所计算出来的hash值包含从而使得你的hash值更加不确定 来降低碰撞的概率

# LinkedHashMap

HashMap的直接子类LinkedHashMap 继承了HashMap的所用特性，并且还通过额外维护一个双向链表保持了有序性, 通过对比LinkedHashMap和HashMap的实现有助于更好的理解HashMap。

# 参考链接情况

[美团原文](https://www.2cto.com/kf/201505/401433.html)



[hash中的取模运算情况](https://www.hollischuang.com/archives/2091)



 # 如何记忆(面试过程中增加记忆)

1. 基础数据结构
2. jdk1.7 与jdk 1.8的区别
3. 新增红黑树
4. 简化hash 函数，这里面也叫做扰动函数
5. 减少位移动运算和异或运算
6. 扩容机制是如何工作的？

## 与Hashtable的区别

## 扩容机制

## 是否可以put null

# 与LinkedHashtable的区别

# 进一步改进 ConcurrentHashtable的区别

# 与Hashset的区别
底层实现其实就是hashMap，直接定义了一个map对象，

```java
private transient HashMap<E,Object> map;
public HashSet() {
        map = new HashMap<>();
    }
```

然后各种方法基本都是直接调用Hashmap, 各种基本方法吧，基本全是HashMap的情况


