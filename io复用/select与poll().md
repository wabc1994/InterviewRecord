# poll()函数
```c
int poll(struct pollfd *fd, unsigned int  nfds, int timeout)
struct pollfd{
   int fd;   // 文件描述符 //file descriptor  /* 想查询的文件描述符. */
   short event;// 要监听的事件， 是要读操作还是写操作情况 //event of interest on fd  /* 等待的事件 */
   short revent;  //返回值  //event that occurred on fd  /* 实际发生了的事件 */
}
其中上面的*fd 可以申明为数组形式，也可以申明为指针形式的情况
```
>一个pollfd 结构体表示监听一个文件描述符是否发生事件event ， nfds 表示有多少个这样的文件pollfd 结构体，一般情况下 nfds有多个值，所以poll一次可以监听多个文件描述符
是否发生某种事件的情况，其中在调用之前设置fd和event两个选项
 
## 参数取值
 event字段可有的取值如下情况所示
  * POLLIN 没有数据可读
  * POLLRDNORM 有正常数据可以读
  * POLLRDBAND 有优先数据可读
  * POLLPRI 有高级优先数据可以读
  * POLLOUT 写操作不会阻塞
  * POLLWRNORM 写正常数据不会阻塞
  * POLLBAND 写优先数据不会阻塞
  * POLLMSG 又一个SIGPOLL消息可用
## 返回值
* 大于0：数组fds中准备好读、写或出错状态的那些socket描述符的总数量；
* -1：  poll函数调用失败，同时会自动设置全局变量errno；
* ==0：数组fds中没有任何socket描述符准备好读、写，或出错；此时poll超时，超时时间是timeout毫秒；换句话说，如果所检测的 socket描述符上没有任何事件发生的话，那么poll()函数会阻塞timeout所指定的毫秒时间长度之后返回，如果timeout==0，那么 poll() 函数立即返回而不阻塞，如果timeout==INFTIM，那么poll() 函数会一直阻塞下去，直到所检测的socket描述符上的感兴趣的事件发 生是才返回，如果感兴趣的事件永远不发生，那么poll()就会永远阻塞下去；


## 具体内核实现
内核实现，poll系统调用的内核实现是sys_poll，poll()函数调用过程如下
 1. 调用poll函数
 2. 进入sys_poll等系列内核调用。
 3. 准备数据：，注册__pollwait（这是通过初始化poll_wqueues来完成的），复制数据至内核，重新组织成struct poll_list等等。
 4. 对所有的struct pollfd循环，以调用do_pollfd函数。
 5. do_pollfd调用file->f_op->poll函数。
 6. 然后调用__pollwait创建一个struct poll_table_entry，并将其与当前进程绑定。
 7. 将当前进程挂在socket的等待队列上。
 8. 有数据就绪时唤醒进程
 
 [具体内容实现机制探究](https://blog.csdn.net/zmxiangde_88/article/details/8099049) 
 
 ## 应用案例
 ```java
 # include <unistd.h>
 # include <sys/poll.h>
 # define TIMEOUT 5 //POLL timeout
 int main(void){
 struct pollfd fds[2];
 int ret;
 //watch stdin for input
 fds[0].fd = STDIN_FILENO;
 fds[0].events =  POLLIN;
 fds[1].fd = STDOUT_FILENO;
 fds[1].events = POLLOUT;
 ret = poll(fds,2, TIMEOUT *1000);
 int (ret==-1){
     perror("poll");
     return -1;
 }
 if(!ret){
     printf("%d seconds elapased.\n", TIMEOUT);
     return 0;
     
 }
 if (fds[0].revents & POLLIN)
     printf("stdin is readable\n");
 if (fds[1].revents & POLLOUT)
     printf("stdout is writable");
 return 0;
 
 }
```

### select() 优缺点
#### 优点
select()目前几乎在所有的平台上支持，其良好跨平台支持也是它的一个优点。poll()由于某些Unix系统不支持poll()
select() 提供了更好的超时方案：直到微妙级别
#### 缺点
>1. 每次调用 select()，都需要把 fd 集合从用户态拷贝到内核态，这个开销在 fd 很多时会很大，同时每次调用 select() 都需要在内核遍历传递进来的所有fd，这个开销在 fd 很多时也很大。
>2. 单个进程能够监视的文件描述符的数量存在最大限制，在 Linux 上一般为 1024，可以通过修改宏定义甚至重新编译内核的方式提升这一限制，但是这样也会造成效率的降低。
##  select() 和 poll()区别
poll()相对于select 其中一个优点吧:
>假设我们在一个应用中使用了poll ，我们可以无需在每次调用时重构struct pollfds fds这个数组， 相同的结构可能会被调用多次，必要的时候内核会把结构体数组中的revents 字段清空
fds：是一个struct pollfd结构类型的数组，用于存放需要检测其状态的Socket描述符；每当调用这个函数之后，系统不会清空这个数组，操作起来比较方便；特别是对于socket连接比较多的情况下，在一定程度上可以提高处理的效率；这一点与select()函数不同，调用select()函数之后，select()函数会清空它所检测的socket描述符集合，导致每次调用select()之前都必须把socket描述符重新加入到待检测的集合中；

但是select和poll系统调用的本质是一样的， 前者是BSD unix 中引入的， 后者是System V 中引入的，poll的机制与select()类似，没有本质上的区别，管理多个文件描述符也是进行轮询；然后根据文件描述符进行处理，但是**poll()没有最大文件描述符数量的限制**(但是数量过大后性能也是下降的)，poll()和select()同样存在一个最大缺点就是， <font color=red >包含大量文件描述符的数组被整体复制于用户态和内核的地址空间之间，而不论这些文件描述符是否就绪，他的开销都随着文件描述符的数量的增加而线性增加</font>
poll() 的实现和 select() 非常相似，只是描述 fd 集合的方式不同，poll() 使用 pollfd 结构而不是 select() 的 fd_set 结构，其他的都差不多。

# select()和poll()超详细比较
本内容来自Linux系统编程2.10.4
尽管select() 和poll()是完成一样的工作，但是poll()系统调用优于select():
1. poll 无需使用者计算最大的文件描述符加一和传递该参数。
2. poll在应对较大值的文件描述符时效率更高，想像一下用select()监控900个文件描述符---内核需要检查每个集合中每一个比特为 
，直到第900个。
3. select()的文件描述符集合时静态大小的，所以要作出权衡：要么集合很小，限制了select()可以监视的文件描述符的最大值，要么较大但是效率不高。尤其时当不能确定集合的组成是否稀疏时，对较大的位掩码的操作效率不是很大， 
*使用poll则可以创建合适大小的数组。 只需要监视一项或传递一个结构体
4. 使用select(), 文件描述符集合在返回时要重新创建， 这样的话之后每个调用都必须重新初始化他们。poll()可以重复使用该结构体情况。

# select()和poll() 比较情况介绍(英文情况)
1. The select() API requires that the application pass in an array of bits in which one bit is used to represent each descriptor number. When descriptor numbers are very large, it can overflow the 30KB allocated memory size, forcing multiple iterations of the process. This overhead can adversely affect performance
2. The poll() API allows the application to pass an array of structures rather than an array of bits. Because each pollfd structure can contain up to 8 bytes, the application only needs to pass one structure for each descriptor, even if descriptor numbers are very large.
# select的缺点：
1. 单个进程能够监视的文件描述符的数量存在最大限制，通常是1024，当然可以更改数量，但由于select采用轮询的方式扫描文件描述符，文件描述符数量越多，性能越差；(在linux内核头文件中，有这样的定义：#define __FD_SETSIZE    1024)
2. 内核 / 用户空间内存拷贝问题，select需要复制大量的句柄数据结构，产生巨大的开销；
3. select返回的是含有整个句柄的数组，应用程序需要遍历整个数组才能发现哪些句柄发生了事件；
4. select的触发方式是水平触发，应用程序如果没有完成对一个已经就绪的文件描述符进行IO操作，那么之后每次select调用还是会将这些文件描述符通知进程。 
>每次调用select，都需要把fd集合从用户态拷贝到内核态，这个开销在fd很多时会很大**
>同时每次调用select都需要在内核遍历传递进来的所有fd，这个开销在fd很多时也很大
>select支持的文件描述符数量太小了，默认是1024
# select缺点的应用场景情况
拿select模型为例，假设我们的服务器需要支持100万的并发连接，则在__FD_SETSIZE 为1024的情况下，则我们至少需要开辟1k个进程才能实现100万的并发连接。除了进程间上下文切换的时间消耗外，从内核/用户空间大量的无脑内存拷贝、数组轮询等，是系统难以承受的。因此，基于select模型的服务器程序，要达到10万级别的并发访问，是一个很难完成的任务。
如何解决上述问题就是下面一个章节介绍的东西epoll()

上述文中参考链接情况
[参考链接一](https://blog.csdn.net/qq546770908/article/details/53082870)
[参考链接二]()
