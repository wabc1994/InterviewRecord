# bean在容器当中是非线程安全的
bean在spring容器当中是非线程安全的，那么是如何保证线程的问题呢

spring当中的bean分为两种

1. 有状态的bean：就是有实例变量的对象，可以保存数据，是非线程安全的，每个

2. 没状态的bean：没有实例变量的bean，或者后续不能改变的问题等情况

spring没有针对bean当中的声明对象设置一线程安全问题



# ThreadLocal 保证线程安全
让每个线程都有一个bean 本地变量的情况，每个线程都有自己的bean 情况，

# 参考链接

[Spring Bean Scope 有状态的Bean 无状态的Bean](https://blog.csdn.net/anyoneking/article/details/5182164)