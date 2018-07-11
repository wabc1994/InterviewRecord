import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorPoolController {
    public static void main(String[] args) {
        /*
        * 创建一个可缓存的线程池。如果线程池的大小超过了处理任务所需要的线程
        * 那么就会回收部分空闲（60秒不执行任务）的线程，当任务数量增加时。此线程池
        * 又可以智能地增加新线程来处理任务。此线程池不会对线程大小作出限制，
        * 线程池大小完全依赖操作系统或者（jvm） 能够创建的最大线程大小。
        *
        * */
        ExecutorService service = Executors.newCachedThreadPool();

    }
}
