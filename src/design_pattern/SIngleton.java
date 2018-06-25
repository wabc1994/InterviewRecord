package design_pattern;

public class SIngleton {
    private static SIngleton obj;//在实际使用的时候才创建，类加载到内存的时候还未加载
    //把构造函数设置为private 只能通过getInstance(), not thread safe
    /*
   分为三种单列模式：1.懒汉式单例，不是线程安全
    Lazy initialisation defers creation of an object until you absolutely need it.
    * */
    private SIngleton() { }
    public static SIngleton getInstance()
    {
        //优点是第一次调用的时候才初始化，避免内存浪费。缺点是：必须加锁synchronized 才能保证单例（加锁会影响效率）
        //如果两个线程调用getInstance()方法，
        if(obj==null)
            obj = new SIngleton();
        return obj;//返回同一个类实例
    }

}
class Singleton_threadsafe
{
    /*
    * 懒汉式单例：加锁实现线程安全，任何时候只能有一个线程可以访问getInstance()方法，效率底下，*/
    private Singleton_threadsafe() {}
    private static  Singleton_threadsafe obj;
    public static  synchronized Singleton_threadsafe getInstance()
    {
        if(obj==null)
            obj =new Singleton_threadsafe();
                return obj;
    }
}
