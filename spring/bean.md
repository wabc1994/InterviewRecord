# 一个大池子容器
Spring其实就是一个大型的工厂，而Spring容器中的Bean就是该工厂的产品,对于Spring容器能够生产那些产品，则取决于配置文件中配置。
Spring容器集中管理Bean的实例化。

```java

<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xmlns="http://www.springframework.org/schema/beans"
	xsi:schemaLocation="http://www.springframework.org/schema/beans
	http://www.springframework.org/schema/beans/spring-beans-3.0.xsd">
	<bean id="bean1" class="com.Bean1">
		<constructor-arg value="chenssy"/>
		<constructor-arg value="35-354"/>
	</bean>
	
```

则Spring相当于调用如下代码：
```java
Bean bean = new com.Test("chenssy","35-354");
```
>At it’s very core, Spring offers a container, often referred to as the Spring application
context, that creates and manages application components.
These components
or beans, are wired together inside of the Spring application context to make a
complete application, much like bricks, mortar, timber, nails, plumbing, and wiring are
bound together to make a house.
The act of wiring beans together is based on a pattern known as dependency injection.

IOC容器类似于工厂模式中生产对象的工厂类Factory，工厂类根据传递进来的类名生成具体对象，就是IOC根据配置文件里面对象类名定义 动态的生成对象的过程。

@Controller
 Its primary purpose in this class is to identify this
 class as a component for purposes of component-scanning
 ## Spring容器的初始化
 ApplicationContext作为Spring容器 ,BeanFactory，
>ApplicationContext是一个接口，它继承自BeanFactory接口，除了包含BeanFactory的所有功能之外，在国际化支持、资源访问（如URL和文件）、事件传播等方面进行了良好的支持。
# Bean
## 什么是bean?
被称作 bean 的对象是构成应用程序的支柱也是由 Spring IoC 容器管理的。bean 是一个被实例化，组装，并通过 Spring IoC 容器所管理的对象。这些 bean 是由用容器提供的配置元数据创建的.
bean源码
```java
Indicates that a method produces a bean to be managed by the Spring container.

 <h3>Overview</h3>

 <p>The names and semantics of the attributes to this annotation are intentionally
 similar to those of the {@code <bean/>} element in the Spring XML schema. For
 example:

 <pre class="code">
     @Bean
     public MyBean myBean() {
         // instantiate and configure MyBean obj
         return obj;
    }</pre>
1. 记住，@Bean就放在方法上，就是产生一个Bean 
2. 凡是子类及带属性、方法的类都注册Bean到Spring中，交给它管理；
3. @Bean 用在方法上，告诉Spring容器，你可以从下面这个方法中拿到一个Bean
```
意思是@Bean明确地指示了一种方法，什么方法呢——产生一个bean的方法，并且交给Spring容器管理；从这我们就明白了为啥@Bean是放在方法的注释上了，因为它很明确地告诉被注释的方法，你给我产生一个Bean，然后交给Spring容器，剩下的你就别管了
## bean与Java对象的关系
Java对象的创建和初始化是人
bean的创建和销毁是spring容器
1. Java面向对象，对象有方法和属性，那么就需要对象实例来调用方法和属性（即实例化）；
2. 凡是有方法或属性的类都需要实例化，这样才能具象化去使用这些方法和属性
3. 规律：凡是子类及带有方法或属性的类都要加上注册Bean到Spring IoC的注解；
4. 把Bean理解为类的代理或代言人（实际上确实是通过反射、代理来实现的），这样它就能代表类拥有该拥有的东西了
5. 5、我们都在微博上@过某某，对方会优先看到这条信息，并给你反馈，那么在Spring中，你标识一个@符号，那么Spring就会来看看，并且从这里拿到一个Bean或者给出一个Bean
## 注解
1. 一类是使用Bean，即是把已经在xml文件中配置好的Bean拿来用，完成属性、方法的组装；比如@Autowired , @Resource，可以通过byTYPE（@Autowired）、byNAME（@Resource）的方式获取Bean；
2. 2、一类是注册Bean,@Component , @Repository , @ Controller , @Service , @Configration这些注解都是把你要实例化的对象转化成一个Bean，放在IoC容器中，等你要用的时候，它会和上面的@Autowired , @Resource配合到一起，把对象、属性、方法完美组装。 
## 加载bean
ApplicationContext和BeanContext 用于加载Bean
## 属性注入
构造函数、普通set和
   

## 三种配置方式
bean有三种配置方式java、xml、注解,如何从配置文件完成到bean的创建
## 两种bean
Spring中有两种类型的Bean，一种是普通Bean，另一种是工厂Bean，即FactoryBean，这两种Bean都被容器管理，但工厂Bean跟普通Bean不同，其返回的对象不是指定类的一个实例，其返回的是该FactoryBean的getObject方法所返回的对象。
## bean的声明周期
## 作用域
# DI components(组件)


# AOP

## AOP中的动态代理机制
[Spring AOP - Proxy](https://www.tutorialspoint.com/springaop/springaop_proxy.htm)


## PointCut 切点
切点的功能是指出切面的通知应该从哪里织入应用的执行流。切面只能织入公共方法。

## Adivcse 通知
[有关各种通知的使用方法](https://blog.csdn.net/topwqp/article/details/8695180)
## org.aspectj

#　IoC
## 控制反转提出的背景
>在采用面向对象方法设计的软件系统中，底层实现都是由N个对象组成的，所有的对象通过彼此的合作，最终实现系统的业务逻辑。即软件系统中对象之间的耦合，对象A和对象B之间有关联，对象B又和对象C有依赖关系，这样对象和对象之间有着复杂的依赖关系，所以才有了控制反转这个理论。

##为何称为控制反转

控制反转的基本思路是创建对象的权利由程序员的手中转移到spring容器来创建，spring根据具体的代码逻辑来决定什么时间点来出创建模块之间相互依赖的对象

1. 软件系统在没有引入IoC容器之前，对象A依赖对象B，那么A对象在实例化或者运行到某一点的时候，自己必须主动创建对象B或者使用已经创建好的对象B，其中不管是创建还是使用已创建的对象B，控制权都在我们自己手上。
所以控制反转IoC(Inversion of Control)是说创建对象的控制权进行转移，以前创建对象的主动权和创建时机是由自己把控的，而现在这种权力转移到第三方，比如转移交给了IoC容器，它就是一个专门用来创建对象的工厂，你要什么对象，它就给你什么对象，有了IoC容器，依赖关系就变了，原先的依赖关系就没了，它们都依赖IoC容器了，通过IoC容器来建立它们之间的关系

## IOC的好处
1. 可维护性比较好，非常便于进行单元测试，便于调试程序和诊断故障。代码中的每一个Class都可以单独测试，彼此之间互不影响，只要保证自身的功能无误即可，这就是组件之间低耦合或者无耦合带来的好处。

[参考链接](https://blog.csdn.net/bestone0213/article/details/47424255?utm_source=blogxgwz0)


# 参考

[链接一](https://blog.csdn.net/tuxedolinux/article/details/79056162)

[链接二](https://blog.csdn.net/bestone0213/article/details/47424255?utm_source=blogxgwz0)

[事物管理](https://blog.csdn.net/bao19901210/article/details/41724355?utm_source=blogxgwz2)