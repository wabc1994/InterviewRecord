# redis 内存分配策略

对不同平台下面的malloc和free 当中的代码进行一个封装，同时也是为了精细化内存管理等方面的知识

先来了解下两种内存的情况
1. 外部碎片： 没有被分配出去的(不属于任何进程)， 但是由于这块内存太小了导致无法分配给申请大于这块内存的进程，导致不能使用
2. 内部碎片： 已经分配出去(可以指明是属于哪个一进程的)，却不能被利用的空间，只有进程释放掉， 才能被系统重新利用(这个也叫内存泄漏)


malloc 和calloc() 的区别主要是malloc() 函数和calloc()函数的主要区别是前者不能初始化所分配的内存空间，而后者能，相同点都是返回申请内存的首地址

malloc是用于动态分配内存的常用函数。该内存分配在“堆”上。

先看下面定义的各种API,redis 当中与内存分配相关的函数功能主要有一下几个模块

**void *代表一个不指定类型的指针**

```c
 void *zmalloc(size_t size);     
 //调用zmalloc申请size个大小的空间
void *zcalloc(size_t size);      
//调用系统函数calloc函数申请空间
void *zrealloc(void *ptr, size_t size);
void zfree(void *ptr) // 释放内存空间
size_t zmalloc_used_memory(void);   
// 获取使用的内存大小
void zmalloc_enable_thread_safeness(void);    // 开启线程安全模式
void zmalloc_set_oom_handler(void (*oom_handler)(size_t));   
// 自定义的内存溢出处理方法
float zmalloc_get_fragmentation_ratio(size_t rss);  
// 使用内存和所给内存之比
size_t zmalloc_get_rss(void);  
// 获取rss信息
size_t zmalloc_get_private_dirty(void);   
// 获取实际物理分配的内存

//原内存重新变化为空间size 的大小
```

# API解析

## zmalloc
```c
void *zmalloc(size_t size){
    // 调用内存分配方式 
    //PRE_SIZE 用来记录这块内存的大小情况，大小是8个字节，
    // 实际使用当中为了内存对齐， malloc(size+8) 分配的内存可能比size+8 还要多一些， 
    void *ptr = malloc(size+PRE_SIZE);
    // 处理内存泄漏的基本情况
   
    if(!ptr){
        //如果申请的结果为null，说明发生了oom,调用oom的处理方法
        zmalloc_oom_handler(size);

    }
     // 进行内存统计
#ifdef HAVE_MALLOC_SIZE
    update_zmalloc_stat_alloc(zmalloc_size(ptr));
    return ptr;
#else
    *((size_t*)ptr) = size;
    update_zmalloc_stat_alloc(size+PREFIX_SIZE);
    return (char*)ptr+PREFIX_SIZE;
#endif
}
```


## zcalloc
```c
/* 调用系统函数calloc函数申请空间 */
void *zcalloc(size_t size) {
	//calloc与malloc的意思一样，不过参数不一样
	//void *calloc(size_t numElements,size_t sizeOfElement),;
    //numElements * sizeOfElement才是最终的内存的大小,numElements 代表有几个这样的内存块大小
	//所在这里就是申请一块大小为size+PREFIX_SIZE的空间
    void *ptr = calloc(1, size+PREFIX_SIZE);
   

    // 下面的处理操作都是一样的情况
    if (!ptr) zmalloc_oom_handler(size);
#ifdef HAVE_MALLOC_SIZE
    update_zmalloc_stat_alloc(zmalloc_size(ptr));
    return ptr;
#else
    *((size_t*)ptr) = size;
    update_zmalloc_stat_alloc(size+PREFIX_SIZE);
    return (char*)ptr+PREFIX_SIZE;
#endif
}
```


## update_zmalloc_stat_alloc
```c
/* 申请新的_n大小的内存，分为线程安全，和线程不安全的模式 */
#define update_zmalloc_stat_alloc(__n) do { \
    size_t _n = (__n); \
    if (_n&(sizeof(long)-1)) _n += sizeof(long)-(_n&(sizeof(long)-1)); \
    // 判断是否是线程安全的方式
    if (zmalloc_thread_safe) { \
        update_zmalloc_stat_add(_n); \
    } 
    // 如果不是线程安全的模式就是直接添加就行了，
    else { \
        used_memory += _n; \
    } \
} while(0)

```

## 线程安全模式
```c
#define update_zmalloc_stat_add(__n) do { 
    // 线程安全是通过加锁实现的
    pthread_mutex_lock(&used_memory_mutex); 
    used_memory += (__n); 
    pthread_mutex_unlock(&used_memory_mutex); 
} while(0)

```


# 特色点
1. 添加了对使用内存使用的统计update_zmalloc_stat_alloc
2. 支持线程安全模式， 通过锁的机制对use_memory 进行控制
3. 增加对内存溢出的处理


```c
static size_t used_memory =0; // 使用内存的大小
static int zmalloc_thread_safe = 0; // 线程安全模式
pthread_mutex_t used_memory_mutex = PTHREAD_MUTEX_INITIALIZED // 为此服务器
```


# 链接

[Redis源码剖析——内存分配](https://www.liuin.cn/2018/03/23/Redis%E6%BA%90%E7%A0%81%E5%89%96%E6%9E%90%E2%80%94%E2%80%94%E5%86%85%E5%AD%98%E5%88%86%E9%85%8D/)

[c语言当中的malloc， calloc， recalloc](https://blog.csdn.net/zhangxiao93/article/details/43966425)

[互斥锁的使用pthread_mutex_t](https://blog.csdn.net/yusiguyuan/article/details/14148311)

[c语言当中malloc()操作堆内存]
