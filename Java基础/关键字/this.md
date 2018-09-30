# this 
表示当前对象，对象锁，对象引用计数
## ### 对synchronized(this)的一些理解（很好的解释了对象锁，注意其中的this关键字）
1. 当两个并发线程访问同一个对象object中这个synchronzied(this)同步代码块时，一个时间内只能有个一个线程可以执行同步代码块。另一个线程必须等待当前线程执行完这个代码块后才能获得该代码块
2. 另一方面，当一个线程访问object的一个synchronized(this)同步代码块，其他线程可以执行访问该object

3. 尤其关键的是，当一个线程访问object的一个synchronized(this)同步代码块时，其他线程对object中所有其它synchronized(this)同步代码块的访问将被阻塞。

4. 第三个例子同样适用其它同步代码块。也就是说，当一个线程访问object的一个synchronized(this)同步代码块时，它就获得了这个object的对象锁。结果，其它线程对该object对象所有同步代码部分的访问都被暂时阻塞。

5. 以上规则对其它对象锁同样适用

