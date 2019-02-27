# ep_poll_callback

- 就是向内核注册一个fd 以及fd上面监听的感兴趣事件后，内核会注册一个回调函数
- 当fd 上面的事件准备就绪后，就会调用ep_poll_callback 函数，
- 回调函数然后将红黑树上面的对应epitem放到rdlist, 
- 用户调用epoll_wait(),就检查rdlist 就绪链表当中是否有数据，如果有返回给用户
- epoll_wait(int epfd, events,max,iteim)

>Each ep_poll_callback() is called when fd calls wakeup() on epfd.
 So account new event in user ring.
 epoll_ctl 支持添加移除 fd，我们只看添加的情况。epoll_ctl 的主要操作在 ep_insert, 它做了以下事情：

![](https://github.com/wabc1994/InterviewRecord/blob/master/io复用/picture/ep_poll_callback.png)
 
 # epitem
 epoll_ctl 支持添加移除 fd，我们只看添加的情况。epoll_ctl 的主要操作在 ep_insert, 它做了以下事情：
 
 - 初始化一个 epitem，里面包含 fd，监听的事件，就绪链表，关联的 epoll_fd 等信息
 - 调用 ep_item_poll(epitem, ep_ptable_queue_proc[[1]])。ep_item_poll 会调用 vfs_poll， vfs_poll 会调用上面说的 file->f_op->poll 将 ep_poll_callback[[2]] 注册到 waitqueue
 - 调用 ep_rbtree_insert(eventpoll, epitem) 将 epitem 插入 evenpoll 对象的红黑树，方便后续查找
 
 初始化一个 epitem，里面包含 fd，监听的事件，就绪链表，关联的 epoll_fd 等信息

![](https://github.com/wabc1994/InterviewRecord/blob/master/io复用/picture/ep_poll_callback().png)

# eppoll_entry结构体
创建struct eppoll_entry(为了放入设备等待队列),这个是真正的监听设备层

当设备就绪，唤醒等待队列上的等待者时，ep_poll_callback就会被调用，将epitem放入rdlist，每次调用epoll_wait就只收集rdlist里的fd就可以了