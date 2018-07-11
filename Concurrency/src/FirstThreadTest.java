public class FirstThreadTest extends Thread {
    int i=0;
    public void run()
    {
        for(;i<100;++i){
            System.out.println(getName()+" "+i);
        }
    }

    public static void main(String[] args) {
        //外层的循环是主程序，i从0到99，当i=20时线程切换到小线程中执行
        for(int i=0;i<100;++i){
            System.out.println(Thread.currentThread().getName()+":"+i);
            if(i==20){
                //当直接继承thread时候直接使用即可，
                new FirstThreadTest().start();
                new FirstThreadTest().start();
            }
        }
    }
}
