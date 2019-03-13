# 自旋锁
线程自旋的时候占用cpu的cpu什么也不做，通过for空循环实现， 

# 自旋锁时间阈值
如何获取自旋的执行时间呢？如果自旋的时间太长，会有大量的线程处于自旋状态占用CPU资源，进而影响系统的整体性能


# epoll
eventloop 当中的自旋锁问题，主要是用来改变红黑树的情况

主要是回调函数那里，将准备就绪的文件描述符放到准备就绪列表里面即可

```java
static int ep_poll_callback(wait_queue_entry_t *wait, unsigned mode, int sync, void *key)
{
	int pwake = 0;
	unsigned long flags;
	struct epitem *epi = ep_item_from_wait(wait);
	struct eventpoll *ep = epi->ep;
	int ewake = 0;
	
	/* 获得自旋锁 ep->lock来保护就绪队列
	 * 自旋锁ep->lock在ep_poll()里被释放
	 * /
	spin_lock_irqsave(&ep->lock, flags);

	/* If this file is already in the ready list we exit soon */
	/* 在这里将就绪事件添加到rdllist */
	if (!ep_is_linked(&epi->rdllink)) {
		list_add_tail(&epi->rdllink, &ep->rdllist);
		ep_pm_stay_awake_rcu(epi);
	}
	...
}
```

1. 操作红黑树的话就是使用互斥锁，时间复杂度为O(log(N))
2. 回调函数的话是直接使 spinlock_t lock 时间复杂度为O(1)


# 适应自旋锁

一个锁自旋的时候什么也不做，是很浪费CPU资源的,那么该旋转多少次呢(旋转多长时间)

在旋转了这么长时间后，如果还不能获得锁的话，那么线程就会进入睡眠挂起状态，避免浪费cpu资源

因此自旋等待的时间必须要有一定的限度，如果自旋超过了限定的次数仍然没有成功获得锁，就应当使用传统的方式去挂起线程了（默认10次）


**缺陷**

自旋是需要消耗CPU的，如果一直获取不到锁的话，那该线程就一直处在自旋状态，白白浪费CPU资源。

**方案**

解决这个问题最简单的办法就是指定自旋的次数，例如让其循环10次，如果还没获取到锁就进入阻塞状态。

**激励方法**

上一次是否获得锁，直接影响下一次获得锁的可能性问题？

上次自旋获得了锁，那么下次自旋的次数就增加，如果上次自旋没有获得锁，那么下次获得锁的概率就可能很低，自旋的次数就比较少的情况


但是JDK采用了更聪明的方式——适应性自旋，简单来说就是线程如果自旋成功了，则下次自旋的次数会更多，如果自旋失败了，则自旋的次数就会减少