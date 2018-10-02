# select 一些概念性东西的理解情况

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



