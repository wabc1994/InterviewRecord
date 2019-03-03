# 实质
底层都是调用do_fork来实现

只不过clone() 在大部分情况下都是使用做创建线程，

有几个关键的宏 flag标志位选项


1. CLONE_VM 子进程和父进程共用内存空间
2. CLONE_VFORK 父进程被挂起，直至子进程释放虚拟内存资源，孩子进程执行完毕或者exec() 或者exit() 操作，再执行负进程
3. CLONE_FILES 父子进程共享相同的文件系统
4. CLONE_FS ,父子进程共享相同的文件描述符，

等等


- fork创造的子进程是父进程的完整副本，复制了父亲进程的资源，包括内存的内容task_struct内容，父子进程具有相同的代码块和数据等内存里面的东西
- vfork vfork创建的子进程与父进程共享数据段,而且由vfork()创建的子进程将先于父进程运行  execve() 函数族执行别的任务的情况
- clone Linux上创建线程一般使用的是pthread库 实际上linux也给我们提供了创建线程的系统调用，就是clone

# 参考链接
[Linux 中使用 clone 函数](https://blog.csdn.net/gdh55555/article/details/48048753)