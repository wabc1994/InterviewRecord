package design_pattern;
/**
 * 登记式模式（holder），内部类只有在外部类被调用的时候才加载，产生实例对象，并且不不加锁，该模式可以兼顾饿汉模式和懒汉模式的优点
 */

public class Holder_Sinleton {
    private  Holder_Sinleton(){}

    public static Holder_Sinleton getInstance()
    {
        return Holder.object;
    }
    private static  class Holder{
        private static final Holder_Sinleton object= new Holder_Sinleton();
    }

}
