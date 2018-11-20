# Linux内核中的同步操作
## 原子操作


## 互斥锁

互斥锁主要用于实现内核中的互斥访问功能。对它的访问必须遵循一些规则：同一时间只能有一个任务持有互斥锁，而且只有这个任务可以对互斥锁进行解锁。

没有获得锁的是

## 自旋锁

自旋锁与互斥锁有点类似，只是自旋锁不会引起调用者睡眠，如果自旋锁已经被别的执行单元保持，调用者就一直循环在那里看是否该自旋锁的保持者已经释放了锁，"自旋"一词就是因此而得名(做一个循环while)。

### 为何选择自旋锁？与一般的互斥锁的优势所在

由于自旋锁使用者一般保持锁时间非常短，因此选择自旋而不是睡眠是非常必要的，自旋锁的效率远高于互斥锁。



### 选择自旋锁的场景？

Spinlocks should be used to lock data in situations where the lock is not held for a long time

### 不可重入锁

在Linux 系统中的自旋锁不是可重入锁，区别于其他操作系统，（比如Java中的自旋锁就是可重复锁）



什么叫做可重入锁机制

> 也叫递归锁，是指同一个线程外层函数获得锁后，内层函数(递归调用或者调用外部同样sychronized的方法)同时也可获得锁，不受到影响。

Don't hold it for a long time.

> 调用者持有锁的时间比较短

## 读写自旋锁

将自旋锁进一步划分为读写类型，主要是针对读读场景，可以提高并发性。

Since it is typically safe for multiple threads to read data concurrently, so long as nothing modifies the data, reader/writer locks allow multiple concurrent readers but only a single writer 

1. 读者只对共享资源进行读访问
2. 写者则需要对共享资源进行写操作。

遵循下面的几种原则

1. Write demand mutual exelusion
2. 写操作是互斥
3. 并发读操作是可以的 Mulitple concurrent Reading is ok
4. when Reading ,writing must be diabled



### 读写锁

主要是针对读场景，

## 顺序锁
## 信号量

其实也分为读写信号量的情况，

1. 内核同步信号量
2. 用户进程同步信号量(用于进程进行通信的，进程五大通信手段之一)

# 针对上面的几种锁，该如何选择

| 需求             | 建议的加锁方法 |
| ---------------- | -------------- |
| 低开销加锁       | 选择自旋锁     |
| 短期锁定         | 选择自旋锁     |
| 长期锁定         | 选择信号量     |
| 睡眠的同时持有锁 | 选择信号量     |
| 上下文中断加锁   | 选择自旋锁     |

