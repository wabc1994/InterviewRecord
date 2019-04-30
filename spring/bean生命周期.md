# 最基本流程

首先要理解几个概念，bean实例创建，实例属性注入，初始化都是不同的概念，然后才是使用bean


1. spring对Bean进行实例化 相当于程序当中(new Xx()) 调用一些无参构造函数，实例化的bean入口就是实例化的方法的真正入口还是getBean。从BeanDefinition 到bean就是bean的实例化，getbean 就是setbean 关系

2. spring 将值和bean 的引用注入bean 对应的属性当中 setter方法

3. 


## Aware接口

为何需要这几个接口来做事情?

>为了运用Spring所提供的一些功能，有必要让Bean了解Spring容器对其进行管理的细节信息，如让Bean知道在容器中是以那个名称被管理的，
或者让Bean知道BeanFactory或者ApplicationContext的存在，也就是产让该Bean可以取得BeanFactory或者ApplicationContext的实例，
如果Bean可以意识到这些对象，那么就可以在Bean的某些动作发生时。


[BeanNameAware 和BeanFactoryAware ](https://www.jianshu.com/p/7248ccef9382)

1. BeanNameAware接口

spring将bean的id传给setBeanName()方法；

2. BeanFactorAware接口

spring将调用setBeanFactory方法，将BeanFactory实例传进来；

3. ApplicationContextAware接口

它的setApplicationContext()方法将被调用，将应用上下文的引用传入到bean中；

## BeanPostProcessor

后置处理器：BeanPostProcessor接口作用是：如果我们需要在Spring容器完成Bean的实例化、配置和其他的初始化前后添加一些自己的逻辑处理，我们就可以定义一个或者多个BeanPostProcessor接口的实现，然后注册到容器中。


2. BeanPostProcessor接口

它的postProcessAfterInitialization接口方法将被调用；

public Object postProcessAfterInitialization(Object arg0, String arg1);在bean初始化之后的操作

public Object postProcessBeforeInitialization(Object arg0, String arg1);在bean初始化之前的操作


## BeanFactoryPostProcessor




## InitializingBean


对于Bean实现 InitializingBean，它将运行 afterPropertiesSet()在所有的 bean 属性被设置之后。

initializingBean 是一个接口，实现这个接口我们必须实现afterPropertieSet 方法

前者顾名思义在Bean属性都设置完毕后调用afterPropertiesSet()方法做一些初始化的工作，后者在Bean生命周期结束前调用destory()方法做一些收尾工作


对应配置文件中<bean>的init-method和



## DisposableBean


对于 Bean 实现了DisposableBean，它将运行 destroy()在 Spring 容器释放该 bean 之后。

对应配置文件当中的destroy-method方法


# 参考链接

[bean的生命周期](https://blog.csdn.net/qiuchaoxi/article/details/80654395)