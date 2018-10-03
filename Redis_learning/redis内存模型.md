# 前言
Redis是目前最火爆的内存数据库之一，通过在内存中读写数据，大大提高了读写速度，可以说Redis是实现网站高并发不可或缺的一部分。
我们使用Redis时，会接触Redis的5种对象类型（字符串、哈希、列表、集合、有序集合），丰富的类型是Redis相对于Memcached等的一大优势。在了解Redis的5种对象类型的用法和特点的基础上，进一步了解Redis的内存模型，对Redis的使用有很大帮助，例如：丰富的对象类型是Redis相对于Memcached等的一大优势。在了解Redis的五种对象类型的用法和特点的基础，进一步了解redis的内存模型，对redis的使用帮助是很大的，比如如下这种情况+
1. 估算redis的内存使用量。目前为止，内存的使用成本还是相对很高的，使用内存不能无所顾忌， 根据需求合理评估redis的内存使用情况，选择合适的机器配置，可以在满足需求的情况下节约成本。
2. 优化内存占用。了解redis 内存模型可以选择更加合适的数据类类型和编码，更好的利用redis内存。
3. 分析解决问题当redis 出现阻塞、内存占用等问题，尽快发现导致问题的原因，便于分析解决问题。

这篇文章主要介绍Redis的内存模型（以3.0为例），包括Redis占用内存的情况及如何查询、不同的对象类型在内存中的编码方式、内存分配器(jemalloc)、简单动态字符串(SDS)、RedisObject等；然后在此基础上介绍几个Redis内存模型的应用
# 目录
## [-.Redis内存统计](一.Redis内存统计)

## [二.Redis内存划分]()
    # [1、数据](或者称为对象)
    # [2、进程本身运行需要的对象]
    # [3、缓存内存]
    # [4、内存碎片]
## [三、Redis数据存储但细节]
    # [1、概述]
    # [2.jemalloc]
    # [3.redisObject]
## 一、Redis 内存统计情况
工欲善其事必先利其器，在说明Redis内存之前首先说明如何统计Redis使用内存的情况。
在客户端通过redis-cli连接服务器后（后面如无特殊说明，客户端一律使用redis-cli），通过info命令可以查看内存使用情况：
> info memory

其中，info命令可以显示redis服务器的许多信息，包括服务器基本信息、CPU、内存、持久化、客户端连接信息等等；memory是参数，表示只显示内存相关的信息。
返回结果中比较重要的几个说明如下：
  - use_memory：Redis 内存分配器分配的内存总量（单位是字节），包括使用的虚拟内存(swap);
  - use_memory_rss:Redis进程占操作系统的内存(单位是字节)，与top及ps命令看到的值是一致的；除了分配器分配的内存之外，used_memory_rss还包括进程运行本身需要的内存、内存碎片等，但是不包括虚拟内存。use_memory_rss:Redis进程占操作系统的内存(单位是字节)，与top及ps命令看到的值是一致的；除了分配器分配的内存之外，used_memory_rss还包括进程运行本身需要的内存、内存碎片等，但是不包括虚拟内存。
由于在实际应用中，Redis的数据量会比较大，此时进程运行占用的内存与Redis数据量和内存碎片相比，都会小得多；因此used_memory_rss和used_memory的比例，便成了衡量Redis内存碎片率的参数；这个参数就是mem_fragmentation_ratio。
  - mem_fragmentation_ratio：内存碎片比率，该值是used_memory_rss / used_memory的比值。
  - （4）mem_allocator：Redis使用的内存分配器，在编译时指定；可以是 libc 、jemalloc或者tcmalloc，默认是jemalloc；截图中使用的便是默认的jemalloc。
##  二、Redis 内存划分
Redis作为内存数据库，在内存中存储的内容主要是数据（键值对）；通过前面的叙述可以知道，除了数据以外，Redis的其他部分也会占用内存。
Redis的内存占用主要可以划分为以下几个部分：
### 1. 数据
作为数据库，数据是最主要的部分；这部分占用的内存会统计在used_memory中
Redis使用键值对存储数据，其中的值（对象）包括5种类型，即字符串、哈希、列表、集合、有序集合。这5种类型是Redis对外提供的，实际上，在Redis内部，每种类型可能有2种或更多的内部编码实现；此外，Redis在存储对象时，并不是直接将数据扔进内存，而是会对对象进行各种包装：如redisObject、SDS等；这篇文章后面将重点介绍Redis中数据存储的细节
### 2. 进程本身需要的运行内存
Redis主进程本身运行肯定需要占用内存，如代码、常量池等等；这部分内存大约几兆，在大多数生产环境中与Redis数据占用的内存相比可以忽略。这部分内存不是由jemalloc分配，因此不会统计在used_memory中。
   
   **补充说明：除了主进程外，Redis创建的子进程运行也会占用内存，如Redis执行AOF、RDB重写时创建的子进程。当然，这部分内存不属于Redis进程，也不会统计在used_memory和used_memory_rss中。**
### 3. 缓存内存
缓冲内存包括客户端缓冲区、复制积压缓冲区、AOF缓冲区等；其中，客户端缓冲存储客户端连接的输入输出缓冲；复制积压缓冲用于部分复制功能；AOF缓冲区用于在进行AOF重写时，保存最近的写入命令。在了解相应功能之前，不需要知道这些缓冲的细节；这部分内存由jemalloc分配，因此会统计在used_memory中
### 4.内存碎片
内存碎片是redis在分配、回收物理内存过程中产生的。例如，如果对数据的更改频繁、而且数据之间的大小相差很大，可能导致redis 释放的空间在物理内存中并没有释放，但redis又无法有效利用，这就形成了内存碎片。内存碎片是不回头统计在used_memory中。
内存碎片的产生与对数据进行的操作、数据的特点等都有关；此外，与使用的内存分配器也有关系：如果内存分配器设计合理，可以尽可能的减少内存碎片的产生。后面将要说到的jemalloc便在控制内存碎片方面做的很好。

如果Redis服务器中的内存碎片已经很大，**可以通过安全重启的方式减小内存碎片：因为重启之后，Redis重新从备份文件中读取数据，在内存中进行重排，为每个数据重新选择合适的内存单元，减小内存碎片。**
## 三、Redis数据存储但细节
### 1、概述
关于Redis数据存储的细节，涉及到内存分配器（如jemalloc）、简单动态字符串（SDS）、5种对象类型及内部编码、redisObject。在讲述具体内容之前，先说明一下这几个概念之间的关系。
下图是执行set hello world时，所涉及到的数据模型。
![一个key_value存储的过程]
（1）dictEntry：Redis是Key-Value数据库，因此对每个键值对都会有一个dictEntry，里面存储了指向Key和Value的指针；next指向下一个dictEntry，与本Key-Value无关。
 (2）Key：图中右上角可见，Key（”hello”）并不是直接以字符串存储，而是存储在SDS结构中。
（3）redisObject：Value(“world”)既不是直接以字符串存储，也不是像Key一样直接存储在SDS中，而是存储在redisObject中。实际上，不论Value是5种类型的哪一种，都是通过redisObject来存储的；而redisObject中的type字段指明了Value对象的类型，ptr字段则指向对象所在的地址。不过可以看出，字符串对象虽然经过了redisObject的包装，但仍然需要通过SDS存储
 (4)实际上，redisObject除了type和ptr字段以外，还有其他字段图中没有给出，如用于指定对象内部编码的字段；后面会详细介绍。
### 2.jemalloc   
Redis在编译时便会指定内存分配器；内存分配器可以是 libc 、jemalloc或者tcmalloc，默认是jemalloc。
jemalloc作为Redis的默认内存分配器，在减小内存碎片方面做的相对比较好。jemalloc在64位系统中，将内存空间划分为小、大、巨大三个范围；每个范围内又划分了许多小的内存块单位；当Redis存储数据时，会选择大小最合适的内存块进行存储。
### 3.redisObject
前面说到，Redis对象有5种类型；无论是哪种类型，Redis都不会直接存储，而是通过redisObject对象进行存储。
redisObject对象非常重要，Redis对象的类型、内部编码、内存回收、共享对象等功能，都需要redisObject支持，下面将通过redisObject的结构来说明它是如何起作用的。

redisObject的定义如下（不同版本的Redis可能稍稍有所不同）：
```c
typedef struct redisObject {
　　unsigned type:4;
　　unsigned encoding:4;
　　unsigned lru:REDIS_LRU_BITS; /* lru time (relative to server.lruclock) */
　　int refcount;
　　void *ptr;
} robj;
```
redisObject的每个字段的含义和作用如下：
#### 1.type
type字段表示对象的类型，占4个比特；目前包括REDIS_STRING(字符串)、REDIS_LIST (列表)、REDIS_HASH(哈希)、REDIS_SET(集合)、REDIS_ZSET(有序集合)。
#### 2.encoding
encoding表示对象的内部编码，占4个比特。
对于Redis支持的每种类型，都有至少两种内部编码，例如对于字符串，有int、embstr、raw三种编码。通过encoding属性，Redis可以根据不同的使用场景来为对象设置不同的编码，大大提高了Redis的灵活性和效率。以列表对象为例，有压缩列表和双端链表两种编码方式；如果列表中的元素较少，Redis倾向于使用压缩列表进行存储，因为压缩列表占用内存更少，而且比双端链表可以更快载入；当列表对象元素较多时，压缩列表就会转化为更适合存储大量元素的双端链表。
通过object encoding命令，可以查看对象采用的编码方式
#### 3.lru
lru记录的是对象最后一次被命令程序访问的时间，占据的比特数不同的版本有所不同
通过对比lru时间与当前时间，可以计算某个对象的空转时间；object idletime命令可以显示该空转时间（单位是秒）。object idletime命令的一个特殊之处在于它不改变对象的lru值。
lru值除了通过object idletime命令打印之外，还与Redis的内存回收有关系：如果Redis打开了maxmemory选项，且内存回收算法选择的是volatile-lru或allkeys—lru，那么当Redis内存占用超过maxmemory指定的值时，Redis会优先选择空转时间最长的对象进行释放。
#### 4.refcout
refcount与共享对象
refcount 记录的是该对象引用的次数，类型为整型。refcount的作用，主要在于对象的引用计数和内存回收。当创建新对象时，refcount初始化为1；当有新程序使用该对象时，refcount加1；当对象不再被一个新程序使用时，refcount减1；当refcount变为0时，对象占用的内存会被释放。
Redis中被多次使用的对象(refcount>1)，称为共享对象。Redis为了节省内存，当有一些对象重复出现时，新的程序不会创建新的对象，而是仍然使用原来的对象。这个被重复使用的对象，就是共享对象。目前共享对象仅支持整数值的字符串对象。
#### 5.ptr
ptr指针指向具体的数据，如前面的例子中，set hello world，ptr指向包含字符串world的SDS