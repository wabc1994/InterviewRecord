# happen-before也是内存模型中的一个东西
happens-before 关系保证：如果线程 A 与线程 B 满足 happens-before 关系，则线程 A 执行动作的结果对于线程 B 是可见的。 
**如果两个操作未按 happens-before 排序，JVM 将可以对他们任意重排序。**
[参考链接一](https://blog.csdn.net/lemon89/article/details/50963894)
[参考链接二](https://blog.csdn.net/ns_code/article/details/17348313)