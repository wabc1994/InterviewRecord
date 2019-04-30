# 基本组成
1. 块设备层，系统中能够随机（不需要按顺序）访问固定大小数据片（chunks）的设备被称作块设备，这些数据片就称作块。最常见的块设备是硬盘，



2. 文件系统 file system ntf fat ext等文件系统 ，一个磁盘可以划分很多区，然后每个区可以建立不同的文件系统，为了屏蔽这些文件系统的操作，

我们需要再进行一次抽象，抽象出来虚拟文件系统



文件系统file 下面又分为page cache 磁盘预读操作，page cache 是缓存操作的


在底层的块设备和文件系统直接我们还是要进行一步的抽象处理的，然后在文件系统上面我们还需要进一步得抽象出来， 

![A79Nxf.png](https://s2.ax1x.com/2019/04/10/A79Nxf.png)
``
在最底层的硬件设备之间还是存在一个BLOCK device Driver Layer  驱动层

[![A7SPzT.md.png](https://s2.ax1x.com/2019/04/10/A7SPzT.md.png)](https://imgchr.com/i/A7SPzT)


![A7vH5q.png](https://s2.ax1x.com/2019/04/11/A7vH5q.png)

1. 磁盘控制器
2. DMA 直接内存访问

直接内存访问(DMA)是一种完全由硬件执行I/O交换的工作方式。在这种方式中，DMA控制器从CPU完全接管对总线的控制，数据交换不经过CPU，而直接在内存和I/O设备之间进行 

## 数据从磁盘到内核缓冲区


read()系统调用导致内核向磁盘控制硬件发出一条命令要从磁盘获取数据。磁盘控制器通过DMA直接将数据写入内核的内存缓冲区，不需要主CPU进一步帮助。

[![A7v4Kg.md.png](https://s2.ax1x.com/2019/04/11/A7v4Kg.md.png)](https://imgchr.com/i/A7v4Kg)


## 一次完整的read调用过程

[一次完整的调用过程情况](https://blog.csdn.net/gdj0001/article/details/80136364)


![A7OK3D.png](https://s2.ax1x.com/2019/04/11/A7OK3D.png)


# 参考链接

- [linux中的页缓存和文件IO](https://blog.csdn.net/gdj0001/article/details/80136364)

- 从比较高的层次来解读[read 系统调用剖析](https://www.ibm.com/developerworks/cn/linux/l-cn-read/index.html)

- [内核对轮询IO(阻塞/非阻塞)的实现](https://blog.csdn.net/qq_28992301/article/details/53142826)


设备驱动层实现了poll或者select等方面的情况，设备驱动有等待队列，里面放着进程

- [Linux内核开发之阻塞非阻塞IO](https://blog.csdn.net/yi412/article/details/25001991)