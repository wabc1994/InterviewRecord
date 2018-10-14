package design_pattern;
/**
 * 登记式模式（holder），内部类只有在外部类被调用的时候才加载，产生实例对象，并且不不加锁，该模式可以兼顾饿汉模式和懒汉模式的优点
 */

public class Holder_Sinleton {

    //构造函数定义为私有的，这样外部就不能通过其他途径创建该类的对象，只能通过类的静态方法 创建一个对象
    private  Holder_Sinleton(){}

    //定义为公开的访问权限
    public static Holder_Sinleton getInstance()
    {
        return Holder.object;
    }

    private static  class Holder{
        private static final Holder_Sinleton object= new Holder_Sinleton();
    }

}
