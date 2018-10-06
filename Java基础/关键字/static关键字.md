```

在《Java编程思想》P86页有这样一段话：
“static方法就是没有this的方法。在static方法内部不能调用非静态方法，反过来是可以的。而且可以在没有创建任何对象的前提下，仅仅通过类本身来调用static方法。这实际上正是static方法的主要用途。”
```

相关的基础知识

类的加载机制：static 代码块和static变量只会在类加载第一次初石化

# 一.static关键字的用途

在《Java编程思想》P86页有这样一段话：
“static方法就是没有this的方法。在static方法内部不能调用非静态方法，反过来是可以的。而且可以在没有创建任何对象的前提下，仅仅通过类本身来调用static方法。这实际上正是static方法的主要用途。”

方便在没有创建对象的情况下来进行调用（方法/变量）

很显然，被static关键字修饰的方法或者变量不需要依赖于对象来进行访问，只要类被加载了，就可以通过类名去进行访问。

# ## static方法

static方法一般称作静态方法，由于静态方法不依赖于任何对象就可以进行访问，因此对于静态方法来说，是没有this的，因为它不依附于任何对象，既然都没有对象，就谈不上this了。并且由于这个特性，在静态方法中不能访问类的非静态成员变量和非静态成员方法，因为非静态成员方法/变量都是必须依赖具体的对象才能够被调用。

　　但是要注意的是，虽然在静态方法中不能访问非静态成员方法和非静态成员变量，但是在非静态成员方法中是可以访问静态成员方法/变量的

## static变量 

tatic变量也称作静态变量，静态变量和非静态变量的区别是：静态变量被所有的对象所共享，在内存中只有一个副本，它当且仅当在类初次加载时会被初始化。而非静态变量是对象所拥有的，在创建对象的时候被初始化，存在多个副本，各个对象拥有的副本互不影响。static成员变量的初始化顺序按照定义的顺序进行初始化。（涉及到类的加载机制问题，初次加载的情况）

## static代码块

static关键字还有一个比较关键的作用就是 用来形成静态代码块以优化程序性能。static块可以置于类中的任何地方，类中可以有多个static块。在类初次被加载的时候，会按照static块的顺序来执行每个static块，并且只会执行一次。(无论创建了多少次对象)

　　为什么说static块可以用来优化程序性能，是因为它的特性:只会在类加载的时候执行一次。下面看个例子:

```java
`class` `Person{``    ``private` `Date birthDate;``    ` `    ``public` `Person(Date birthDate) {``        ``this``.birthDate = birthDate;``    ``}``    ` `    ``boolean` `isBornBoomer() {``        ``Date startDate = Date.valueOf(``"1946"``);``        ``Date endDate = Date.valueOf(``"1964"``);``        ``return` `birthDate.compareTo(startDate)>=``0` `&& birthDate.compareTo(endDate) < ``0``;``    ``}``}`
```

```Java
public class Test {

    static{
        System.out.println("Static");
    }

    {
        System.out.println("Non-static block");
    }

    public static void main(String[] args) {
        Test t = new Test();
        Test t2 = new Test();
    }
}
```
输出情况
```java
Static
Non-static block
Non-static block
```

另外，static块可以出现类中的任何地方（只要不是方法内部，记住，任何方法内部都不行），并且执行是按照static块的顺序执行的

# static 使用误区

## 1.static关键字会改变类中成员的访问权限吗？

有些初学的朋友会将java中的static与C/C++中的static关键字的功能混淆了。在这里只需要记住一点：与C/C++中的static不同，Java中的static关键字不会影响到变量或者方法的作用域。在Java中能够影响到访问权限的只有private、public、protected（包括包访问权限）这几个关键字。看下面的例子就明白了

## 2.能通过this访问静态成员变量吗？

这里面主要考察队this和static的理解。this代表什么？this代表当前对象，那么通过new Main()来调用printValue的话，当前对象就是通过new Main()生成的对象。而static变量是被对象所享有的，因此在printValue中的this.value的值毫无疑问是33。在printValue方法内部的value是局部变量，根本不可能与this关联，所以输出结果是33。在这里永远要记住一点：静态成员变量虽然独立于对象，但是不代表不可以通过对象去访问，所有的静态方法和静态变量都可以通过对象访问

（只要访问权限足够）。