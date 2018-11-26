# epoll()函数

将监听注册从实际监听中分离出来，解决了 select() 和poll中存在的问题

主要包括三个函数

```c
int epoll_create(int size);  
int epoll_ctl(int epfd, int op, int fd, struct epoll_event *event);  
int epoll_wait(int epfd, struct epoll_event *events,int maxevents, int timeout); 
```

op操作主要有如下的几个步骤

一句话总结上面三个函数所做的事情就是如下这种情况：

> 一个系统epoll_create()调用初始化一个epoll上下文(一个epoll对象实例)，  poll_ctl 从上下文中加入或者删除要监听的文件描述符，epoll_wait()第三个执行真正的时间等待

具体可以总结如下情况：

1. 使用起来很清晰，首先要调用epoll_create建立一个epoll对象。
2. epoll_ctl可以操作上面建立的epoll，例如，将刚建立的socket加入到epoll中让其监控，或者把 epoll正在监控的某个socket句柄移出epoll，不再监控它等等。
3. epoll_wait在调用时，在给定的timeout时间内，当在监控的所有句柄**(文件描述符)**中有事件发生时，就返回用户态的进程。



## epoll_create(int size) 

首先调用epoll_create建立一个epoll实例，**返回与该实例关联的文件描述符**。参数size是内核保证能够正确处理的最大句柄数(其实就是文件描述符)，多于这个最大数时内核可不保证效果。（size参数告诉内核要监听的文件描述符数组，但不是最大值，传递一个适当的近似值会带来性能的提升，但不需要给出确认的数字）==注意从这里开始内核就开始存储要监听的文件描述符了，不用像select()和poll()一样将先准备好的文件描述符集合从用户空间拷贝到内核空间==epoll_ctl中的epfd是epoll_create实例，就是epoll_create调用返回的东西
(实际创建的是epoll instance， 但仅仅返回一个文件描述符，这个文件描述符与epoll instance 相关联)
两个重要概念
 - epoll instance 
 - a file descriptor 
>A successful call to epoll_create( ) instantiates a new epoll instance, and returns a file descriptor associated with the instance. This file descriptor has no relationship to a real file; it is just a handle to be used with subsequent calls using the epoll facility.
```
int epfd;
epdf = epoll_create(100); // plan to watch 100 fd
```

返回值：

- 出错时，返回-1， 设置errno为下列值之一：
  - EINVAL size不是正数。
  - ENFILE 系统达到打开文件数的上限
  - ENOMEN 没有足够的内存来完成该次操作
### How does the epoll instance "remembers" file descriptors?
For obvious reason, the epoll have to somehow "remembers" the file descriptors it was asked to pay attention to. epoll uses a very commonly used kernel data structure - Red–black tree (abbreviated as RB-Tree), to keep track of all file descriptors that are currently being monitored by a certain epoll instance. The root of the RB-Tree is the rbr member of struct eventpoll, and is initialized within the ep_alloc() function.


## epoll_ctl控制epoll

 epoll_ctl()可以向指定的epoll实例中加入或者删除文件描述符，参数op指定对fd要进行的操作， event 参数描述epoll更具体的行为。

每次调用 epoll_ctl只是在往内核的数据结构中加入新的socket句柄
用三个宏来表示：
- EPOLL_CTL_ADD：注册新的fd到epfd中；
- EPOLL_CTL_MOD：修改已经注册的fd的监听事件；
- EPOLL_CTL_DEL：从epfd中删除一个fd；

```c
struct epoll_event{
    _u32 events;  //要监听的事件
    union{
        void * ptr;
        int fd;
        _u32 u32
        _64 u64;
    }data;
};
```

​     epoll events结构体中的events参数列出了在fd给定文件描述符上监听的事件，通常将event.data.fd设定为fd，

# epoll_ wait

> 红黑树：存储epoll_ctl新加入的socket
>
> list链表：监听准备就绪的socket 
>
> epoll文件系统
>
> File 文件系统

  epoll_wait()等待给定epoll 实例关联的文件描述符上的事件，对epoll_wait() 的调用等待epoll实例epfd中的文件描述符fd 上面事件

## 为何epoll_wait这么高效

1. 在内核中，一切皆文件。所以epoll向内核注册了一个文件系统，用于存储上述被监听的socket ，当你调用epoll_create时，就会在这个虚拟的epoll文件系统创建一个file节点。当然，这个file不是普通文件，它只服务于epoll;

2. Epoll 在被内核初始化，同时会开辟出epoll自己的内核高速cache区，用于安置每一个我们想监听的socket， 这些socket会以红黑树的形式存在内核cache里面， 以支持快速的查找、插入、删除。

3. epoll的高效就在于，当我们调用epoll_ctl往里塞入百万个句柄时，epoll_wait仍然可以飞快的返回，并有效的将发生事件的句柄给我们用户。这是由于我们在**调用epoll_create时，内核除了帮我们在*epoll文件系统*里建了个file结点，在内核cache里建了*个红黑树用于存储以后epoll_ctl传来的socket外*，还会再建立一个==list链表，用于存储准备就绪的事件==，当epoll_wait调用时，仅仅观察这个list链表里有没有数据即可。有数据就返回，没有数据就sleep，等到timeout时间到后即使链表没数据也返回。所以，epoll_wait非常高效。





##  与select、 poll

selec、poll的缺陷
1. 大量的文件描述符的复制，从用户太到内核太
> epoll() 使用mmap内存映射技术解决这个问题
2. 针对所有的文件描述符进行遍历，当select被唤醒的时候，它并不知道是被哪个或者哪些文件唤醒的，所以它要对位图中所有的文件描述符进行遍历查询(调用该文件的poll接口)。可以想象，如果select的文件比较多，并且大部分文件都是不活跃的，那么这些select中的大部分poll将会没有任何意义，做了很多无用工(对位图中的每个坑进行检查)
> 所以epoll就使用一个相对智能的回调机制：当某个文件准备好之后要唤醒等待线程时，它不是简单的把等待者设置为可运行，它还会进一步在等待者的结构中刻上“XXX到此一游”，这样，当线程被唤醒之后，它就可以通过这些留言看到是谁唤醒了自己，而不是逐个询问刚才是谁把我唤醒了。

通常情况下即使我们要监听百万级别的句柄， 大多一次也就返回很少量准备就绪的句柄而已
>The biggest difference between epoll and traditional I/O multiplexing mechanisms is that, instead of building and passing a giant array of file descriptors into the Kernel every time, the application simply acquires an epoll instance and register file descriptors onto it


## 准备就绪链表list

当我们执行epoll_ctl时，除了把socket放到epoll文件系统file对象对应的红黑树外，还会给内核中断处理程序注册一个回调函数，如果这个句柄的中断到了，就把它放到准备就绪list链表里。所以，当一个socket上有数据到了，内核在把网卡上的数据copy到内核中后就来把socket插入到准备就绪链表里了。

## 红黑树
如此，一颗红黑树，一张准备就绪句柄链表，少量的内核cache，就帮我们解决了大并发下的socket处理问题。执行epoll_create时，创建了红黑树和就绪链表，执行epoll_ctl时，如果增加socket句柄，则检查在红黑树中是否存在，存在立即返回，不存在则添加到树干上，然后向内核注册回调函数，用于当中断事件来临时向准备就绪链表中插入数据。执行epoll_wait时立刻返回准备就绪链表里的数据即可。

## 内存映射机制
这里也使用了内存映射（mmap）技术，这样便彻底省掉了这些文件描述符在系统调用时复制的开销。(从用户空间到内核空间)，**内存共享也是进程通信最高效的方式**
在这点上，epoll是通过内核于用户空间mmap同一块内存实现的。而如果你想我一样从2.5内核就关注epoll的话，一定不会忘记手工 mmap这一步的。
介绍linux内存映射mmap()机制情况
>内存映射(mmap)是Linux操作系统的一个很大特色，它可以将系统内存映射到一个文件（设备）上，以便可以通过访问文件内容来达到访问内存的目的。这样做的最大好处是提高了内存访问速度，并且可以利用文件系统的接口编程（设备在Linux中作为特殊文件处理）访问内存，降低了开发难度。
## callback function
[回调函数](https://idndx.com/2014/09/22/the-implementation-of-epoll-3/)
这里就体现了软件中比较常用的“回调机制”(callback)，当然有些比较通俗的叫法就叫做“钩子”(hook)，**也就是在某些事件发生的时候知会一些实体**，知会的方法就是调用对方提供的钩子函数。**因为可能某一个实体对一个事件的发生很感兴趣，但是这个事件并不是随时随地都会发生的**，而具体在什么时候发生只有"体制内"流程才知道。比方说你去一个地方找一个同事A，但是他不在，你可能会给他附近的同事B说：“如果他回来的话你告诉我一声”。这就是一个回调机制，当一个自己不知道的事情发生的时候通过一种机制马上通知到自己。



# 参考链接
[非常重要写的非常好](https://www.cnblogs.com/lojunren/p/3856290.html)