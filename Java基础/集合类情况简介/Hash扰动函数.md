# HashMap

核心在于回答出来扰动函数的基础概念情况
hash 

扰动函数
>主要是为了加大hash值低位的随机性，使得分布更加均匀，从而提高对应数组存储下标位置的随机性&均匀性，最终减少hash冲突，两次就够了，

已经达到了搞位低位同时参与运算的目的了：

## JDK1.7

四次右位

```java
final int hash(Object k) {
        int h = hashSeed;
        if (0 != h && k instanceof String) {
            return sun.misc.Hashing.stringHash32((String) k);
        }
 
        h ^= k.hashCode();  // 先调用hash code情况， 一次扰动
 
        // This function ensures that hashCodes that differ only by
        // constant multiples at each bit position have a bounded
        // number of collisions (approximately 8 at default load factor).
        h ^= (h >>> 20) ^ (h >>> 12);   
        
        return h ^ (h >>> 7) ^ (h >>> 4);
    }

```

**总结**

hash值计算方式： 扰动处理= 9次扰动=4次位运算+ 5次异或运算


目标

>扰动函数，充分利用高位和低位的信息，减少发生过这段代码是为了对key的hashCode进行扰动计算
防止不同hashCode的高位不同但低位相同导致的hash冲突。简单点说，就是为了把高位的特征和低位的特征组合起来，降低哈希冲突的概率，也就是说，尽量做到任何一位的变化都能对最终得到的结果产生影响。


## rehash

rehash问题的话，我们只要HashMapJDK1.7和JDK1.8的区别：JDK1.8考虑的是如何再进一步减少发生冲突的可能性

首先明确一定，rehash 元素在新hashtable当中有两种可能性，

1. 原index跟新index一样，没变

2. 新index = index + oldcapacity 旧的容量

JDK1.7是跟没扩容之前一样的计算方法 **(位置关系不变)**，JDK1.8之后是采用扩容两倍后多出来的1位，决定采用上述当中的1还是2

1. 如果多出来的一位是0， 索引就不改变

2. 如果多出来的以为是1， 索引就是原来index +旧链表的容量

>经过rehash之后，元素的位置要么是在原位置，要么是在原位置再移动2次幂的位置

[HashMap中在resize()时候的rehash, 即再哈希法的理解 ](https://blog.csdn.net/qq_27093465/article/details/52270519)

```java

if(e.hash & oldc==0)
      采用方法一
    
  else
      方法二
      
```

     

>这个设计确实非常的巧妙，既省去了重新计算hash值的时间，而且同时，由于新增的1bit是0还是1可以认为是随机的，
因此resize的过程，均匀的把之前的冲突的节点分散到新的bucket了。这一块就是JDK1.8新增的优化点。有一点注意区别，JDK1.7中rehash的时候，旧链表迁移新链表的时候，如果在新表的数组索引位置相同，则链表元素会倒置，但是从上图可以看出，JDK1.8不会倒置。


## 为何长度要设计成为2的倍数问题

主要是为了实现高效的取余运算情况，通按位与操作实现了替代取余操作等情况, 数组长度是2^的次方，减一操作后会变成了111全部都是1的数字，其余部分都是0操作了，那在这种情况下面，与0按位与操作都是0，与1操作才有效
就取到了低位掩码的效果，在这种情况，高位全部归0， 结果就是截取到了最低4位，



```java
static int indexFor(int h, int length) {
    return h & (length-1);
}
```

>位运算(&)效率要比代替取模运算(%)高很多，主要原因是位运算直接对内存数据进行操作，不需要转成十进制，因此处理速度非常快。

## JDK1.8

Java 8中这一步做了优化，只做一次16位右位移异或混合，而不是四次，但原理是不变的。
只需要考虑高16位和低16即可

```java
static final int hash(Object key) {
    int h;
    return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}
```

**位运算为何从四次变为了1次的情况**

但明显Java 8觉得扰动做一次就够了，做4次的话，多了可能边际效用也不大，所谓为了效率考虑就改成一次了


**hash计算方式**

扰动处理= 2次扰动 = 1次位运算 + 1次异或元素

**为何**


# ConcurrentHashMap

关于在ConcurrentHashMap 和HashMap 当中为何要采用两

## jdk1.7

```java
private int hash(Object k) {
    int h = hashSeed;

    if ((0 != h) && (k instanceof String)) {
        return sun.misc.Hashing.stringHash32((String) k);
    }

    h ^= k.hashCode();

    // Spread bits to regularize both segment and index locations,
    // using variant of single-word Wang/Jenkins hash.
    h += (h <<  15) ^ 0xffffcd7d;
    h ^= (h >>> 10);
    h += (h <<   3);
    h ^= (h >>>  6);
    h += (h <<   2) + (h << 14);
    return h ^ (h >>> 16);
}

int j = (hash >>> segmentShift) & segmentMask;

```

## jdk1.8

 //通过扰动函数,来获取hash,扰动函数将hashcode的值与低位的值异或并保证最高位为0.(保证最终结果为正整数) 

```java
static final int spread(int h) {
    return (h ^ (h >>> 16)) & HASH_BITS;
}
```


jdk官方给的效果：边际效果的角度老考虑

为何减少了hash的次数主要是从可用性、边际效果的角度老考虑这种情况吧

z直接还是直接取模元素了

```java

int hash = key.hashCode();
    int index = (hash & 0x7FFFFFFF) % tab.length;
```

# 参考链接

Wang/Jenkins 哈希算法


# 常见面试题

[Java集合必会](https://cloud.tencent.com/developer/article/1184097)

[JDK 源码中 HashMap 的 hash 方法原理是什么？](https://www.zhihu.com/question/20733617)

- HashMap为什么不直接使用hashCode() 作为处理后的hash值直接作为table的下标呢？

hashCode()方法返回的是int整数类型，其范围为-(2 ^ 31)~(2 ^ 31 - 1)，约有40亿个映射空间，
而HashMap的容量范围是在16（初始化默认值）~2 ^ 30，HashMap通常情况下是取不到最大值的，并且设备上也难以提供这么多的存储空间，
从而导致通过hashCode()计算出的哈希值可能不在数组大小范围内，进而无法匹配存储位置

- 为啥数组长度要保证为2的幂次方？

1. 只有当数组长度为2的幂次方时，h&(length-1)才等价于h%length，即实现了key的定位，2的幂次方也可以减少冲突次数，提高HashMap的查询效率；


2. 如果length 为 2 的次幂 则 length-1 转化为二进制必定是 11111……的形式，
在于 h 的二进制与操作效率会非常的快，而且空间不浪费；如果 length 不是 2 的次幂，比如 length 为 15，则 length - 1 为 14，对应的二进制为 1110，在于 h 与操作，最后一位都为 0 ，而 0001，0011，0101，1001，1011，0111，1101 这几个位置永远都不能存放元素了，空间浪费相当大，
更糟的是这种情况中，数组可以使用的位置比数组长度小了很多，这意味着进一步增加了碰撞的几率，减慢了查询的效率！这样就会造成空间的浪费。