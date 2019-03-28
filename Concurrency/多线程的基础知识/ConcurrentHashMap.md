# jdk1.7
两个内部类
1. Segement 继承ReetrantLock,采用分段锁的基本概念, HashEntry代表一个键值对
2. HashEntry 
3. 数组+ 链表+ + 红黑树等概念
# jdk1.8
引申出了多中数据结构 Node<k,v>TreeBin，Traverser等对象内部类。
1. 利用UnSafe类的CAS原子操作Node<K,V>  
2. 改用synchronized 而不是 reetrantlock， synchronized锁已经优化得足够好

# ConcurrentHashMap1.8当中使用volatile 修饰的变量

## 类的数据属性


1. sizeClt
2. baseCount
3. transient volatile Node<K,V>[] table;
4. private transient volatile int transferIndex;
5. 


## 节点
```java
static class Node<K,V> implements Map.Entry<K,V> {
        final int hash;
        final K key;
        volatile V val;
        volatile Node<K,V> next;
        ```
