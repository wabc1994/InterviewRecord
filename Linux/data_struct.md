# Linux各种数据结构体挂载
各种数据结构体之间有挂靠点的说法


比如 一个epitem 可以放在红黑树上面
也可以放在就绪双向队列当中

所以需要在很多地方，在结构体当中定义的时候， 需要分别定义一个

1. rbnode 
2. list_node

```c
struct epitem {
    /* RB tree node used to link this structure to the eventpoll RB tree */
    /* eventpoll内部的红黑树的挂载点 */
    
    struct rb_node rbn;

    /* List header used to link this structure to the eventpoll ready list */
    /* 所有已经ready的epitem都会被挂载到eventpoll的rdllist中 */
    
4    
    struct list_head rdllink;

    /*

```

同样比如
wait_queue_head_t和wait_queue_t的关系
在wait_queue_t当中要定义一个task_list， 


# 其实这种思想当中
在其他方面也有很多的体现，比如Redis当中的各种结构