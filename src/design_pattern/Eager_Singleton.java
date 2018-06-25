package design_pattern;

public class Eager_Singleton {
    /*饿汉式单例模式，类加载时就初始化,单例对象在类加载后就开始初始化，即使没有客户端调用getInstance()方法
    饿汉式的创建方法在一些场景中将无法使用，比如Singleton实例的创建是依赖参数或者配置文件的，在getInstance()
    之前必须要调用某个方法设置参数给他，那种这种单例模式写法就无法使用了。（并且该方法不提供异常处理）
    */
    private static final Eager_Singleton instance = new Eager_Singleton();

    //类的单例被声明为static 和final变量了，在第一次加载到内存中时就会初始化，所以是线程安全。
    private Eager_Singleton(){}
    public static Eager_Singleton getInstance(){
        return instance;
    }
}
/**
 * 对eager initialization 进行一定的改进，采用静态代码快， static block initialization,
 * static and eager 模式都是在类加载的时候就初始化单列对象，不方便使用，虽然是线程安全的
 */
/
class StaticBlockSinleton{
    private static StaticBlockSinleton instance;
    private StaticBlockSinleton() {
        //static block initialization for exception handle
        static{
            try{
                instance = new StaticBlockSinleton();
            }catch (Exception e)
            {
                throw  new RuntimeException("Exception occured in creating singleton");
            }
        }
    }
    public static StaticBlockSinleton getInstance()
    {
        return instance;
    }
}
