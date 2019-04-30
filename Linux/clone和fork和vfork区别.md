# 实质
底层都是调用do_fork来实现

只不过clone() 在大部分情况下都是使用做创建线程，

有几个关键的宏 flag标志位选项


>One use of clone() is to implement threads: multiple flows of control
        in a program that run concurrently in a shared address space.

1. CLONE_VM 子进程和父进程共用内存空间
2. CLONE_VFORK 父进程被挂起，直至子进程释放虚拟内存资源，孩子进程执行完毕或者exec() 或者exit() 操作，再执行负进程
3. CLONE_FILES If CLONE_FILES is set, the calling process and the child process share the same file descriptor table.
4. CLONE_FS , 父子进程共享相同的文件系统
5. CLONE_SIGHAND, 父子进程共享相同的信号处理函数

**线程在内存当中占用的资源**

1. stack 每个线程都有自己独立stack
2. program counter
3. 寄存器
4. 线程id

[同一进程中的线程究竟共享哪些资源](https://blog.csdn.net/zzuchengming/article/details/52131940)


**线程的上下文切换**

上下文切换的开销
当 CPU 从执行一个线程切换到执行另外一个线程的时候，它需要先存储当前线程的本地的数据，程序指针等，然后载入另一个线程的本地数据，程序指针等，最后才开始执行。这种切换称为“上下文切换”(“context switch”)。CPU 会在一个上下文中执行一个线程，然后切换到另外一个上下文中执行另外一个线程。
上下文切换并不廉价。如果没有必要，应该减少上下文切换的发生。

**线程的切换是不需要切换地址空间的**


- fork创造的子进程是父进程的完整副本，复制了父亲进程的资源，包括内存的内容task_struct内容，父子进程具有相同的代码块和数据等内存里面的东西
- vfork vfork创建的子进程与父进程共享数据段,而且由vfork()创建的子进程将先于父进程运行  execve() 函数族执行别的任务的情况
- clone Linux上创建线程一般使用的是pthread库 实际上linux也给我们提供了创建线程的系统调用，就是clone


# 疑问
有了写时复制了，为何还需要vfork这种函数？
>vfork就是为了解决fork创建一进程的巨大开销的，先有vfork 再有fork写时复制技术


# vfork,fork, clone

1. fork使用标志位 SIGCHID // 新创建的子进程终结的时候，发送信号通知其父进程

2. vfork 使用的标记位，CLONE_VFORK | CLONE_VM | SIGCHID// 新创建的子进程是要共享了父进程的数据段，

# 参考链接
[Linux 中使用 clone 函数](https://blog.csdn.net/gdh55555/article/details/48048753)

[浅析fork()和底层实现](https://www.cnblogs.com/tp-16b/p/9005079.html)

[Linux Namespace简介](http://chengqian90.com/Linux%E5%86%85%E6%A0%B8/Linux-Namespace%E7%AE%80%E4%BB%8B.html)