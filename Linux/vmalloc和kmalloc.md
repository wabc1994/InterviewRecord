[TOC]


# kmalloc,malloc和vmalloc

首先是malloc和其他两个的区别：kmalloc和vmalloc主要是内核内存分配管理，malloc主要是用户内存分配

1. malloc主要是分配0到3GB之间的低位内存
2. kmalloc 分配3GB到high-memory之间，的内核内存(小内存分配，以字节为单位)
3. vmalloc分配的内存是在VMALLOC_START和VMALLOC_END(4Gb)之间的内存

> 关于Linux内核空间和用户空间内存映射分配的问题可以参考后面的链接情况



 

 ## kmalloc和vmalloc的区别



## kmalloc

Kmalloc() is use to allocate memory requested from the kernel.The memory allocated is returned by the API and it is physically as well virtually contiguous.

**kmalloc()****用于申请较小的、连续的物理内存

1. 以字节为单位进行分配，在<linux/slab.h>中，所以是小内存的情况， slab就是为在伙伴系统为小内存数据结构而设计的，将两者联系看待
2. void \*kmalloc(size_t size, int flags) 分配的内存物理地址上连续，虚拟地址上自然连续*
3. kmalloc()的内存分配是基于slab机制实现的,slab机制是为分配小内存而提供的一种高效的机制;
4. 但是slab机制也不是独立的,它本身也是在页分配器的基础上来划分更细粒度的内存供调用者使用;
5. 也就是说,系统先使用页分配器（伙伴系统）分配以页为最小单位的连续物理地址,然后,kmalloc()再在这个基础上根据调用者的需要进行切分的

Kmalloc()是建立在slab机制上面的情况

 >Yes, physically contiguous memory is not required in many of the cases. Main reason for kmalloc being used more than vmalloc in kernel is performance. The book explains, when big memory chunks are allocated using vmalloc, kernel has to map the physically non-contiguous chunks (pages) into a single contiguous virtual memory region. Since the memory is virtually contiguous and physically non-contiguous, several virtual-to-physical address mappings will have to be added to the page table. And in the worst case, there will be (size of buffer/page size) number of mappings added to the page table.
  This also adds pressure on TLB (the cache entries storing recent virtual to physical address mappings) when accessing this buffer. This can lead to thrashing.

  在内核很多时候并不一定是需要使用连续的地址空间。之所以在kmalloc和vmalloc进行选择主要是基于性能的缘故。

  我们知道vmolloc分配得到的虚拟地址是连续，但是物理地址不一定连续，为了从不连续的物理地址转换为连续的虚拟地址，内核需要一定的映射，对页表进行一定的更改以达到虚拟地址连续的目的，这个过程是需要花费代价的。

在内核中kmalloc使用得vmalloc频繁，vmalloc使用得比较少，（主要是vmalloc本身物理地址不连续，为了维护虚拟地址连续，必须要进行一定的映射，这个映射是要付出代价的）

 主要有下面几个点：
 1.kmalloc主要是分配小内存，以字节为单位，vmalloc主要是大内存，
 2.kmalloc 分配得到的内存物理地址连续，虚拟地址也是连续，而vmalloc物理地址不连续，而虚拟地址连续



## vmalloc

vmalloc()函数适用于大块内存的申请环境中;但是它申请的内存不能直接用于DMA传输;因为DMA传输需要使用物理地址连续的内存块;


 ## 使用的话
 >vmalloc() is very rarely used, because the kernel rarely uses virtual memory. kmalloc() is what is typically used, 

1. vmalloc比kmalloc要慢
2. 这对于要进行DMA传输的设备来说,是非常重要的;
3. kmalloc申请的内存块在物理地址空间上是连续的,所以它申请的内存块可以直接用于DMA传输;vmalloc申请的内存块在虚拟地址空间上连续,但是在物理地址空间上不要求连续,所以它申请的内存块不能直接用于DMA传输;
4. kmalloc和vmalloc都是基于slab机制实现的,但是kmalloc的速度比vmalloc的速度快;__get_free_pages是基于buddy机制实现的,速度也较快;__
5. __kmalloc和vmalloc都是以字节为单位进行申请,而__get_free_pages()则是以页为单位进行申请
6. kmalloc()分配的内存处于3GB~high_memory之 间
7. kmalloc()分配的内存处于3GB~high_memory之 间
8. kmalloc能分配的大小有限,vmalloc和malloc能分配的大小相对较大




 # 参考链接
[stackoverflow](https://stackoverflow.com/questions/116343/what-is-the-difference-between-vmalloc-and-kmalloc)



[以页为分配单位和以字节为分配单位](http://www.cnblogs.com/sky-heaven/p/6955563.html)



[Linux用户空间与内核空间内存映射](https://blog.csdn.net/Fybon/article/details/18043809)