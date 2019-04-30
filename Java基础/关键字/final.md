# final

## 修饰类

不可被继承，比如string就是该种情况

## 修饰类方法

该方法不可用被子类重写，是最后一个版本了


## 修饰类变量

修改普通变量的话代表不可改变，如果修饰类变量比如在定义的或者构造函数当中进行初始化



## 修饰引用类型

final修饰的引用类型不可用改变指向，但是该引用指向的对象是可以变的

## 与static的区别

static主要声明为该变量属于整个类所有，可以通过类名不用定义对象都可以访问，变量在整个jvm当中只有一份，并且



## 与public 与private等关键字的基本使用情况

如果是与public与private 使用的话，主要是考虑子类继承的关系

1. public final 子类可以继承父类该方法

2. private final 子类不能继承父类的该方法

在Java当中的继承当中，private方法是不被继承的

如果类中只有private的构造方法，那么此类不可以被继承。

## final与finalize的区别

finalize()是Object 固有方法，调用System.gc()的时候执行,

[(7条消息)System.gc()与Object.finalize()的区别](https://blog.csdn.net/vernonzheng/article/details/8042820)