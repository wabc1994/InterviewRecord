[TOC]



# 红黑树与AVL树的区别 

## 红黑树的一个案列

![Screen Shot 2018-11-14 at 11.37.02 AM](/Users/coderlau/Desktop/Screen Shot 2018-11-14 at 11.37.02 AM.png)

1. Root is always black.（根结点是黑的。）
2. All NULL leaves are black, both children of red node are black and vice-versa.（每一个叶子节点，NIL都是黑色）
3. Every simple path from a given node to any of its descendant leaves contains the same number of black
   nodes.（对于每一个节点，从该节点到叶子节点路径上面的黑节点数目是一样的）
4. Path form root to farthest leaf is no more than twice as long as path from root to nearest leaf.

每一个NIL是黑色的，使用矩形表示，非叶子节点采用圆形表示

## 英文答案

>AVL trees maintain a more rigid balance than red-black trees. The path from the root to the deepest leaf in an AVL tree is at most ~1.44 lg(n+2), while in red black trees it's at most ~2 lg (n+1).

As a result, lookup in an AVL tree is typically faster, but this comes at the cost of slower insertion and deletion due to more rotation operations. So use an AVL tree if you expect the number of lookups to dominate the number of updates to the tree.

### 红黑树的高度问题

棵拥有n个内部结点的红黑树的树高h<=2log(n+1)

### 红黑树的优点

红黑是用非严格的平衡来换取增删节点时候==旋转次数的降低==，任何不平衡都会在三次旋转之内解决，而AVL是严格平衡树，因此在增加或者删除节点的时候，根据不同情况，旋转的次数比红黑树要多。所以红黑树的插入效率更高！！！

## 不同点

- 红黑树要求从根节点到叶子节点的最长路径不大于最短路径的两倍
- AVL 树要求每一个子树的左右孩子节点高度差不超过1

保持平衡的要求上面，AVL树要求大于RBtree, 也就带来了search, insert 和delete操作的性能差异

a

## 相同点

- 插入、删除操作为了保持原来的性质，有可能要进行必要的调整，主要看调整的代价，时间复杂度；其中AVL树的调整频繁，代价大(要保持高度平衡)，而RBtree 删除和插入的时间复杂度要好于AVL，
- 而对于search 操作而言，不涉及旋转调整操作，由于AVL树保持高度平衡，树的最大高度不超过1.44log(n), 而红黑树最大深度不超过 2log(n+2), 

##  使用

### 红黑树为何能比AVL树高效的原因

1. 从1这点来看红黑树是牺牲了严格的高度平衡的优越条件为代价红黑树能够以O(log2 n)的时间复杂度进行搜索、插入、删除操作。
2. 此外，由于它的设计，任何不平衡都会在三次旋转之内解决。
3. 红黑树并不追求“完全平衡”——它只要求部分地达到平衡要求，降低了对旋转的要求，从而提高了性能。

所以红黑树的插入效率更高

主要是维持原有性质需要进行旋转的次数和时间复杂度

### 分析

1. 如果插入一个node引起了不满足树的性质，比如改变了红黑树的性质，或者破坏了AVL树的平衡性质，AVL和RB-Tree 都是最多只需要两次2次旋转操作，即两者都是O(1), 但在删除节点的时候，最坏情况下，AVL树需要维护从被删除NODE到root 这条路径上面所有NODE的平衡性，因此平衡性为O(logN), 而RB-Tree 做多只需要三次旋转，只需要0(1) 的复杂度。
2. 其次，AVL的结构相对于RB-TREE来说更为平衡，在插入和删除的时候更加容易引起树的unbalanace，因此在大量数据需要插入或者删除时，AVL需要rebalance的频率会更高。因此，RB-Tree在需要大量插入和删除node的场景下，效率更高。自然，由于AVL高度平衡，因此AVL的search效率更高。 

### 红黑树的应用领域

#### 基础数据结构的实现

典型的应用是关联数组，ava中的TreeSet,TreeMap，广泛用在C++的STL中。如map和set都是用红黑树实现的

### Linux  

- 进程调度中的完全公平调度算法，在左边的节点最需要CPU,实现了选择哪一个进程获得CPU进行运行（比如上图中的1节点，1数值代表进程的虚拟时钟vruntime。）
- 内存管理模块

在这里补充下vruntime 

> CFS调度器设计了一个参数叫Virtual Runtime（简称vruntime）来记录当前任务(进程)所获得的CPU运行时间，值越小代表获得的CPU运行时间也少，不符合公平的原则，因此该节点就是进程调用器应该选择获得CPU运行时间的进程



### 选择RBTree 还是 AVL

简单来说，就是看你对这个数据结构的使用要求，如果 

- 应用操作性能主要集中在查询search操作的话，那就应该选择AVL
- 应用操作性能主要集中delete和insert 的话还是选择RBTree更加具有优势

**每个进程的累计运行时间保存在自己的vruntime字段里，哪个进程的vruntime最小就获得本轮运行的权利。**



[linux CFS](每个进程的累计运行时间保存在自己的vruntime字段里，哪个进程的vruntime最小就获得本轮运行的权利。)

epoll里面有一个文件描述符也是采用了  数组+链表的情况，每一个事件都是

epoll为何能实现百万句柄监听，对比select()只可以监听最大1024个文件描述符

[红黑树的基本操作](https://blog.csdn.net/xiaofei0859/article/details/73618954)

# 参考链接

[红黑树（RB-tree）比AVL树的优势在哪？](https://blog.csdn.net/mmshixing/article/details/51692892)

