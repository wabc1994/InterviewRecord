# select一些概念性东西的理解情况

## 文件描述符集合fd_set

在网络程序中，一个进程同时处理多个文件描述符是很常见的情况。select() 系统调用可以让进程同时监控多个I/O文件描述符（管道、或者 socket）等，当没有I/0文件描述符准备好的时候，select()阻塞， 当其中任何一个设备准备好后，select() 返回

文件描述符集合采用位图的形式

## select 调用形式

```c
#include<sys/select.h>
#include<sys/time.h>
int select(int maxfd, fd_set * readfds, fd_set * writefds, fd_sets exceptfds, const struct timeval * timeout)
```

select的第一个参数是文件描述符集中要被检测的比特数，这个值必须比待检测的最大文件描述符大1；

0001 0000 最大的文件描述符是 5 ，所有maxfd = 6;



参数readfds指定了被读监控的文件描述符集；
参数writefds指定了被写监控的文件描述符集；
而参数exceptfds指定了被例外条件监控的文件描述符集。
参数timeout起了定时器的作用：到了指定的时间，无论是否有设备准备好，都返回调用。timeval的结构定义如下：

```c
struct timeval{
long tv_sec; //表示几秒
long tv_usec; //表示几微妙
}
```

Timeout 取不同的值，该调用有三种不同的性质：

* Timeout =0 ,表示该调用立即返回；
* Timeout = null,  select() 调用阻塞，直到知道有文件描述符准备好
* Timeout 为整数， 就是一般的定时器

Select 调用返回时，除了除了那些已经就绪的描述符外，select将清除readfds、writefds和exceptfds中的所有没有就绪的描述符。select的返回值有如下情况

1．正常情况下返回就绪的文件描述符个数；
2．经过了timeout时长后仍无设备准备好，返回值为0；
3．如果select被某个信号中断，它将返回‐1并设置errno为EINTR。
4．如果出错，返回‐1并设置相应的errno

系统提供了四个宏对描述符集合进行操作：

```c
#include<sys/select.h>
#include<sys/time.h>
void FD_SET(int fd, fd_set *fdset);
//宏FD_SET设置文件描述符集fdset中对应于文件描述符fd的位(设置为1)
void FE_ZERO(fd_set* fdset)
//宏FD_ZERO设置文件描述符fdset中所有的为（设置为0）
void FD_CLR(int fd, fd_set *fdset)
//宏FD_CLR清除文件描述符集fdset中对应于文件描述符fd的位（设置为0）
//宏FD_CLR和宏FD_SET是相反的操作

//上述三个宏操作是要在select()
FD_ISSET(int fd, fd_set * fdset) 
判断经过select后fd文件描述符是否准备好，是否准备好读写，或者有异常情况
宏FD_ISSET使用在select后面的情况
```

比如如下的案列

```c
#include <sys/select.h>
#include <sys/time.h>
fd_set readset;
FD_ZERO(&readset);
FD_SET(5, &readset);
FD_SET(33, &readset);
```

则文件描述符集readset中对应于文件描述符5和33的相应位被置为1，如图1所示：

再执行如下程序后：
FD_CLR(5, &readset);
则文件描述符集readset对应于文件描述符5的相应位被置为0，如图2所示：
通常，操作系统通过宏FD_SETSIZE来声明在一个进程中select所能操作的文件描述符的最大数目。例如：
在4.4BSD的头文件中我们可以看到：

```c
＃ifndef FD_SETSIZE

define FD_SETSIZE 1024

endif
```

# 更深层次的探究(内核到底是如何进行实现这个构成)
## 1、select实现
select的调用过程如下所示：
![过程图](https://github.com/wabc1994/Leetcode2/blob/master/io%E5%A4%8D%E7%94%A8/picture/select%E8%BF%87%E7%A8%8B%E5%9B%BE.png)
主要分别有如下八个过程
1. 使用copy_from_user从用户空间拷贝fd_set到内核空间
2. 注册回调函数__pollwait
3. 遍历所有fd，调用其对应的poll方法（对于socket，这个poll方法是sock_poll，sock_poll根据情况会调用到tcp_poll,udp_poll或者datagram_poll）
4. 以tcp_poll为例，其核心实现就是__pollwait，也就是上面注册的回调函数。
5. __pollwait的主要工作就是把current（当前进程）挂到设备的等待队列中，不同的设备有不同的等待队列，对于tcp_poll来说，其等待队列是sk->sk_sleep（注意把进程挂到等待队列中并不代表进程已经睡眠了）。在设备收到一条消息（网络设备）或填写完文件数据（磁盘设备）后，会唤醒设备等待队列上睡眠的进程，这时current便被唤醒了。
6. poll方法返回时会返回一个描述读写操作是否就绪的mask掩码，根据这个mask掩码给fd_set赋值。
7. 如果遍历完所有的fd，还没有返回一个可读写的mask掩码，则会调用schedule_timeout是调用select的进程（也就是current）进入睡眠。当设备驱动发生自身资源可读写后，会唤醒其等待队列上睡眠的进程。如果超过一定的超时时间（schedule_timeout指定），还是没人唤醒，则调用select的进程会重新被唤醒获得CPU，进而重新遍历fd，判断有没有就绪的fd。
8. 把fd_set从内核空间拷贝到用户空间。

>上述过程存在的缺点
1. 把fd_set从内核空间拷贝到用户空间，开销非常大
2. 需要遍历处理每个集合fd_set，效率非常低下
