# 概述 Reactor pattern
在处理IO的时候，阻塞和非阻塞都是同步IO(select,poll,epoll都是同步的)。两种I/O多路复用模式: Reactor和Proactor; 无论哪种方式，一般来讲I/0多路复用机制都依赖一个事件多路分离器(Event Demultiplexer),有两个东西需要注意
- 事件分离器(event dispatcher)，事件分发器的作用，即将那些读写事件源分发给各读写事件的处理者，就像送快递的在楼下喊: 谁谁谁的快递到了， 快来拿吧！
- 事件处理器(event handler)
- 事件分离器负责将请求事件传递给事件处理器
- 开发人员在开始的时候需要在分发器那里注册感兴趣的事件，并提供相应的处理者（event handler)，或者是回调函数；
分离器对象可将来自事件源的I/O事件分离出来，并分发到对应的read/write事件处理器(Event Handler)

## 具体实现机制
使用Reactor模型，必备的几个组件:事件源、Reactor框架、多路复用机制、事件分离器、事件处理器
>1. 在redis的reactor模型中使用的多路复用机制即使epoll()、select();
>2. 多路复用机制只是Reactor模型的一部分
如下入所示
![]()



## 简单描述
思想就是通过设计多个中间人，代理分发， 以后每次去取快递的时候都可以使用这个思想，

Reactor模型主要由多路复用器(Acceptor)、事件分发器(Dispatcher)、事件处理器(Handler)三部分组成




