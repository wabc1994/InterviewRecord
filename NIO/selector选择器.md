# selector 情况

selector 代表一个线程，一个selector 可以同时监听多个channel

每个channel 上面有四种事件，将一个channel 向selector显示器

interest代表自己感兴趣的事件，一个线程监听多事件，减少线程切换的开销



1. Selector的创建

```java
Selector selector = Selector.open();

```

2. 注册Channel到Selector

```java
channel.configureBlocking(false);
SelectionKey key = channel.register(selector,Selectionkey.OP_READ)

```
Channel 必须是非阻塞的方式，FileChannel 不适用于Selector,因为FileChannel 不能切换为非阻塞的模式


1. connect
2. accept 
3. Read
4. Write 


使用selectionKey的四个常量来表示

1. SelectionKey.OP_CONNECT

2. SelectionKey.OP_ACCEPT

3. SelectionKey.OP_READ

4. SelectionKey.OP_WRITE

# 源码实现情况

还是使用了Linux底层的epoll(),底层实现 EpollSelectorImpl构造函数

select 返回感兴趣的事件其实就是通过epoll_wait来实现的情况

EPollArrayWrapper完成了对epoll文件描述符的构建，以及对linux系统的epoll指令操纵的封装。维护每次selection操作的结果，即epoll_wait结果的epoll_event数组

# 参考链接

[Java NIO系列教程（六）Selector](http://ifeve.com/selectors/)