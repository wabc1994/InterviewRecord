# IOC实现原理解析

HashMap 是ioc实现的关键， key 是bean id， value 是全限定名字报名加+,





```java
<bean id="courseDao" class="com.qcjy.learning.Dao.impl.CourseDaoImpl"></bean>

```
String classname = "com.qcjy.learning.Dao.impl.CourseDaoImpl"

下面就是Class.forname(classname),然后再继续调用newstance了，得到兑现之后就进入contain.put(id,)

```java

/利用Java反射机制，通过class的名称获取Class对象
       Class bean = Class.forName(cls.getText());
    //创建一个对象
         Object obj = bean.newInstance();
         得到一个对象之后我们可以把这个对象放到contain.put()缓存当中
         
        // 一个配置文件里面有很多个bean,然后每个bean都有多个属性，然后我们一个一个设置即可，
        // 在该种情况下面
      
      //对象的实例化是通过newInstance(),然后再通过属性注入的问题来解决的  
      
```



##流程
1. getbean() ，在抽象工厂当中 AbstractBeanFactory.java文件当中
2. dogetBean()  这里面是实际取得bean的地方，也是触发依赖注入的地方
3. createBean()
4. instantiate()  实例化bean
5. createBeanInstance() 用于创建实例Bean 


5. populateBean()   populateBean向创建好的Bean实例当中注入依赖关系（）属性注入， 填充bean的属性，以及处理好bean之间的对应关系
>在这里面先取得BeanDefinition中设置的property值，这些是property 来自BeanDefination的解析
先处理自动装配的注入autowired, 然后就是Bean Reference的解析 

    - 判断需要创建的Bean是否可以实例化， 这个类是否可以通过类的加载器来载入
    - 是否配置了相关了后置处理器
    - 创建bean 

[Java反射和代理实现IOC模式](https://blog.csdn.net/wwww1988600/article/details/7286887)

[Java Spring IOC](https://blog.csdn.net/mlc1218559742/article/details/52774805)