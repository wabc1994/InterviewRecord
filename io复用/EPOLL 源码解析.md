
# 重要的基础数据
1. eventpoll 结构体 epoll_ctl(0)
2. 在epoll中，对于每一个事件，都会建立一个epitem结构体，如下所示：


这个结构是注册在红黑树上面的
```java
struct epitem{
    struct rb_node  rbn;//红黑树节点
    struct list_head    rdllink;//双向链表节点， 该结构体相应的链表节点
    struct epoll_filefd  ffd;  //事件句柄信息
    struct eventpoll *ep;    //指向其所属的eventpoll对象
    struct epoll_event event; //期待发生的事件类型
}
```
# 参考连接
[【Linux深入】epoll源码剖析](https://blog.csdn.net/baiye_xing/article/details/76352935)

[源码剖析Linux epoll实现机制及Linux上惊群 ](https://blog.csdn.net/tgxallen/article/details/78086360)
