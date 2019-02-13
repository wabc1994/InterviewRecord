# 概述 I/O pattern
在处理IO的时候，阻塞和非阻塞都是同步IO(select,poll,epoll都是同步的)。两种I/O多路复用模式: Reactor和Proactor; 无论哪种方式，一般来讲I/0多路复用机制都依赖一个事件多路分离器(Event Demultiplexer),有两个东西需要注意
- 事件分离器(event dispatcher)，事件分发器的作用，即将那些读写事件源分发给各读写事件的处理者，就像送快递的在楼下喊: 谁谁谁的快递到了， 快来拿吧！
- 事件处理器(event handler)
- 事件分离器负责将请求事件传递给事件处理器
- 开发人员在开始的时候需要在分发器那里注册感兴趣的事件，并提供相应的处理者（event handler)，或者是回调函数；
分离器对象可将来自事件源的I/O事件分离出来，并分发到对应的read/write事件处理器(Event Handler)
------
两个与事件分离器有关的模式是Reactor和Proactor。Reactor模式采用同步IO，而Proactor采用异步IO。

## Reactor模式
一般地,I/O多路复用机制（I/O multiplexing mechanisms）都依赖于一个事件多路分离器(Event Demultiplexer)。我们常见的事件多路分用器包括：Linux 的 EPOLL 和 Windows 的 IOCP。
两个与事件分离器有关的模式是Reactor和Proactor。Reactor模式采用同步IO，而Proactor采用异步IO。

在Reactor中，事件分离器负责等待文件描述符或socket为读写操作准备就绪，然后将就绪事件传递给对应的处理器，最后由处理器负责完成实际的读写工作。
Linux epoll 使用 Reactor 模式。Reactor 模式使用同步 I/O（一般来说）。Reactor 的标准（典型）的工作方式是：
1. 注册读就绪事件和相应的事件处理器
2. Reactor 等待事件
3. 事件到来，Reactor 分发可读写事件（读写准备就绪）到用户事件处理函数
4. 用户读取数据
5. 用户处理数据
6. 事件处理器完成实际的读操作，处理读到的数据，注册新的事件，然后返还控制权。

与 Proactor 模式相比，Reactor 模式下，用户有责任在收到可读写事件后进行实际的 I/O 操作。
## Proactor模式
而在Proactor模式中，处理器--或者兼任处理器的事件分离器，只负责发起异步读写操作。IO操作本身由操作系统来完成。传递给操作系统的参数需要包括用户定义的数据缓冲区地址和数据大小，操作系统才能从中得到写出操作所需数据，或写入从socket读到的数据。事件分离器捕获IO操作完成事件，然后将事件传递给对应处理器。比如，在windows上，处理器发起一个异步IO操作，再由事件分离器等待IOCompletion事件。典型的异步模式实现，都建立在操作系统支持异步API的基础之上，我们将这种实现称为“系统级”异步或“真”异步，因为应用程序完全依赖操作系统执行真正的IO工作。
Windows IOCP 使用 Proactor 模式。Proactor 模式使用异步 I/O。Proactor 的标准（典型）的工作方式是：
1. 处理器发起异步读操作（注意：操作系统必须支持异步IO）。在这种情况下，处理器无视IO就绪事件，它关注的是完成事件。
2. 事件分离器等待操作完成事件
3. 在分离器等待过程中，操作系统利用并行的内核线程执行实际的读操作，并将结果数据存入用户自定义缓冲区，最后通知事件分离器读操作完成。
4. 事件分离器呼唤处理器。
5. 事件处理器处理用户自定义缓冲区中的数据，然后启动一个新的异步操作，并将控制权返回事件分离器。

**Proactor 模式下，用户在调用异步 I/O 时会传递一个 Buffer 给系统，系统进行实际的 I/O 操作并从传递给系统的 Buffer 中获取或者放入数据。**

## 对比
### 相同点
可以看出，两个模式的相同点，都是对某个IO事件的事件通知(即告诉某个模块，这个IO操作可以进行或已经完成)。在结构上，两者也有相同点：demultiplexor负责提交IO操作(异步)、查询设备是否可操作(同步)，然后当条件满足时，就回调handler；
### 不同点
不同点在于，异步情况下(Proactor)，当回调handler时，表示IO操作已经完成（数据已从系统内核拷贝到程序内存）；同步情况下(Reactor)，回调handler时，表示IO设备可以进行某个操作(can read or can write，数据准备就绪，但是用户需要自己将数据从系统内核拷贝到程序内存)。
[具体对比可以查看这片文献](http://www.yeolar.com/note/2012/12/15/io-design-patterns/)

**reactor是同步I/O, proactor是异步I/O**

## reactor实现机制
使用Reactor模型，必备的几个组件:事件源、Reactor框架、多路复用机制、事件分离器、事件处理器
>1. 在redis的reactor模型中使用的多路复用机制即使epoll()、select();
>2. 多路复用机制只是Reactor模型的一部分
如下入所示

![reactor模式](https://github.com/wabc1994/InterviewRecord/blob/master/io%E5%A4%8D%E7%94%A8/%E8%AE%BE%E8%AE%A1%E6%A8%A1%E5%BC%8F/reactor.jpg)

1. 事件源头
Linux上是文件描述符，Windows上就是Socket或者Handle了，这里统一称为“句柄集”；程序在指定的句柄上注册关心的事件，比如I/O事件。
2.  event demultiplexer 事件分离器
    1. 程序首先将其关心的句柄（事件源）及其事件注册到event demultiplexer上；
    2. 当有事件到达时，event demultiplexer会发出通知“在已经注册的句柄集中，一个或多个句柄的事件已经就绪”；
    3. 程序收到通知后，就可以在非阻塞的情况下对事件进行处理了。
    
3. Event Handler——事件处理程序(一般这里都是提供一个接口而已)
An abstract base class that defines an interface for processing events that occur on handles
 事件处理程序提供了一组接口，每个接口对应了一种类型的事件，供Reactor在相应的事件发生时调用，执行相应的事件处理。通常它会绑定一个有效的句柄。
 > 总的接口下面对应一种事件处理是concrete event handler
4. 多路复用程序
redis中所谓的封装的多路复用程序是select、epoll和poll() 系统调用
![多路复用的结构](https://github.com/wabc1994/InterviewRecord/blob/master/Redis_learning/picture/seletct_epoll.jpg)

5. Reactor(initiation dispatcher同一个东西)

In the reactor pattern, the initiation dispatcher is the most crucial component. Often this is also known as the ‘Reactor’. 
 class that defines an abstraction for registering/removing event handlers, and running the application's event  loop reactively

6. Concrete event handler
A class that implements the interface define by Event Handler in an application-specific manner.

Reactor

** 在一些结构也将多路复用程序归类到 demultiplexer  ,其实也有可能两者是同一个东西**
## 第二种结构
![reactor的结构倾向于(Java中的结构)](https://github.com/wabc1994/InterviewRecord/blob/master/io%E5%A4%8D%E7%94%A8/%E8%AE%BE%E8%AE%A1%E6%A8%A1%E5%BC%8F/reactor_art2.jpg)

对于这种结构的详细描述请看文献一
```java
class Reactor     
{     
public  
    int register_handler(Event_Handler pHandler, int event);     
    int remove_handler(Event_Handler pHandler, int event);     
    void handle_events(timeval ptv);     
    // ...     
}
```
## reactor和proactor的区别

# 参考链接
- [英文文献一](http://www.diranieh.com/DP/POSA_Reactor.htm)
- [英文文献二](http://kasunpanorama.blogspot.com/2015/04/understanding-reactor-pattern-with-java.html) 



