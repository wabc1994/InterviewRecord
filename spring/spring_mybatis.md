# spring + mybatis 整合

spring 减少new 等操作，在代码中自己创建对象，以前我们在代码创建中要new 的东西全部改由spring来管理，z

注册bean

## bean 定义

就比如在一个测试代码中 如果不使用spring bean ,就要自己在testQueryById() 方法当中进行一定程度的new 

BookDao bookDao = new BookDao() 这样的代码中

```java
public class BookDaoTest extends BaseTest {

    @Autowired  记住在这里使用了 @Autowired 使用注解进行了配置bean,所以在其他xml 中我们是不需要再进行配置的情况
        
       
        
        
    什么情况下使用xml 文件呢，第一是那种不在代码中可能出现的情况， 比如
    private BookDao bookDao;
    @Test
    public void testQueryById() throws Exception {
        long bookId = 1000;
        //不用new 可以直接使用就行了， spring可以直接帮助我们创建的bookDao，我们直接使用就可以了
        Book book = bookDao.queryById(bookId);
        System.out.println(book);
    }
```
---------------------
bean 也是一个对象， 同样有定义(只定义bunew )，名称,注入(new 创建),  作用范围(变量的作用域)

疑问1: 通过注解配置的bean 我可以理解(可以在代码体中直接使用即可，想在上面那种情况下)，但是通过xml 文件配置的bean，  怎么去使用呢

## bean名字

注解方式获得的bean 默认名字是类名字的小写(如果是使用在service当中)，或者方法名(如果是使用在类方法当中)

一般而言，在service层接口层当中是没有spring配置，serveice 层当中使用dao层当中的对象

一个控制反转，另一个是依赖注入(主要强度的是对象之间的依赖关系，属性之间的关系等情况),

Bean 名字等情况

![](https://github.com/wabc1994/InterviewRecord/blob/master/spring/pict/beanname.png)



## 为何有些bean没有名字和id ?

但是只有一个class,  有些bean 不需要进行制定name和id号之间，只要这个bean 不需要在其他配置进行引用，但是只需指明它的类型参数即可， 如果实在是要在代码中获取该bean'

//Bean Cfg File without Bean id

```java
<bean class="com.ds.DemoBean">         
<property name="msg" value="Hello"/> </bean>
//We can Access
Object obj=factory.getBean("com.ds.DemoBean)
```









#  控制反转也相应的有三种

控制反转就是向容器当中穿件一个bean

1. 基于xml 配置文件，xml 中是使用id 来标记一个bean , bean的id 是bean的名字

2. 基于注解， service ， dao, web  @Service 等也可以制定一个bean 的名字

3. 基于Java 类 （configuration 类 和@bean 方法结合使用）,该种方法其实对基于xml配置方式的另一种解释情况、

   其中第三种需要进行扫设置另外一个参数， 在xml 文件配置

   > <context:component-scan base-package="com.yonyou.config"/>



   第三种方法之所以要设置上述的东西，主要是为了告诉spring 容器，这个是个bean，当扫描到这里的时候，将这个设置成为一个bean, 

   另一方面也可以使用@ComponentScan(basePackages = "基类来解决这个问题")

   要注意如果是xml 方式的配置， 也是需要进行写Java代码的结构， 只不过不要加那些注解接口

   >**<context:component-scan base-package=”XX.XX”/>**

   使用Java配置了了类, 使用Java配置类，如果是使用这种方式，一般而言都是单独将这种类型的代码放在一个包下面 Config 包下面。这种方式可能也是需要进行扫描的方式进行的

   ```java
   @Configuration
   public class JavaConfig {
       @Bean
       public User user(){
           User user = new User("jay","1234");
           user.setAddress(this.addr());
           return user;
       }
   
       @Bean
       public Address addr(){
           Address addr = new Address();
           addr.setProvice("BJ");
           addr.setCity("beijing");
           return addr;
       }
   }
   ```





   # 依赖注入的三种方式

   bean注入可以使用@Autowired、@Resource来完成。但它们之间是有区别的。首先来看一下

   下面两种方式的区别

   # @Resoirce 注解

   代码

   ```java
   
   public class StudentService3 implements IStudentService {
       //@Resource(name="studentDao")放在此处也是可行的
       private IStudentDao studentDao;  // 这是一个字段 
       @Resource(name="studentDao") 这是一个定义在其他地方名为 studentDao的bean， 
           在这里的含义就是使用这个bean 来装配这个字端
       private String id;
   public void setId(String id) {
   this.id = id;
   }
    @Resource(name="studentDao") // 通过此注解完成从spring配置文件中查找名称为studentDao的bean来装配字段studentDao，如果spring配置文件中不存在 studentDao名称的bean则转向按照bean类型经行查找
    public void setStudentDao(IStudentDao studentDao) {
   　　this.studentDao = studentDao;
   }
   ```



要使用上面的名为studentDao 的bean 必选在其他地方有定义这个bean才行

```
<bean id="studentDao" class="com.wch.dao.impl.StudentDao"></bean>
```



```java
@Autowired放在此处也是可行的 ,按照类型给字段装配bean 这种去情况
```



1. **@Resource和@Autowired都可以书写标注在字段或者该字段的setter方法之上**

    **e、使用@Autowired时你的Bean必须以@Service或@Component注解才行**

1. 属性注入
2. 构造方法注入
3. 工厂方法注入



@ value注解

在spring boot  当中是可以直接获取到 配置文件中的值的

# spring 容器当中管理的bean

上面两个解决了spring 容器当中bean的

无论是什么样的方式配置bean, 都是向容器当中注册一个bean, 那么就要有个问题， 程序是如何从容器当中进一步获取bean？

这是我自己提出的一个问题？

区别？ @Autiwired @ 等方式只是标记这是一个bean, 进行相应的以来注入，跟我提出的问题, 主类程序如何获取一个bean, 还是不一样的情况， 注意这是对类进行的哪几种操作方式情况，

关于如何从容器当中获取bean,主要有五种方式这中情况

如果采用 xml 文件配置的bean 类型 ， 如果没有给出id 号的话， 该如何使用呢？ 就是tong



# spring通过给字段自动装配bean

1. 组件扫描(component scanning ):Spring会自动发现应用上下文中所创建的bean.
2. autowiring 自动装配:Spring自动满足bean之间的依赖.

上面该种情况下，如果是自动扫描的话， 就要 <!-- 自动扫包 -->
<context:component-scan base-package="com.cn"></context:component-scan>===参考链接

[如何进行整合各个配置文件](https://blog.csdn.net/qq598535550/article/details/51703190)



# spring 容器

ApplicationContext   和BeanFactory 这是两个基本spring基本容器



# spring + mybatis 

## mybatis 的基本工作流程

获取一个SQLSession ， 这个东西的获取有多种途径

1. 通过Reader 对象读取 读取总的配置文件 mybaits.xml 文件 (mybatis 配置文件当中也需要很多的mappers 文件夹情况)

直接在dao层写接口，每个接口跟一个Mapper相对应即可