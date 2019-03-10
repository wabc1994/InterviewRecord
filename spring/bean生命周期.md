# 最基本流程


## Aware接口

为何需要这几个接口来做事情?

>为了运用Spring所提供的一些功能，有必要让Bean了解Spring容器对其进行管理的细节信息，如让Bean知道在容器中是以那个名称被管理的，
或者让Bean知道BeanFactory或者ApplicationContext的存在，也就是产让该Bean可以取得BeanFactory或者ApplicationContext的实例，
如果Bean可以意识到这些对象，那么就可以在Bean的某些动作发生时。


[BeanNameAware 和BeanFactoryAware ](https://www.jianshu.com/p/7248ccef9382)

1. BeanNameAware接口

2. BeanFactorAware


## PostProcessor

1. BeanFactoryPostProcessor


2. BeanpostProcessor