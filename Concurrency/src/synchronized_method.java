public class synchronized_method {
    /**
     * synchronized 修饰方法和修饰同步代码块的区别
     * 同步方法直接在方法上加synchronized实现加锁，同步代码块则在方法内部加锁，很明显，同步方法锁的范围比较大，
     * 而同步代码块范围要小点，一般同步的范围越大，性能就越差，一般需要加锁进行同步的时候，
     * 肯定是范围越小越好，这样性能更好*
     */
    //synchronzied method,在A,B两种情况都是在对象上面加锁，效果一样
    public synchronized void methodA(){
        System.out.println("methodA,,,,");
        try{
            //线程睡眠并不会让线程t1释放对象上的锁，
            Thread.sleep(5000);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }
    public void methodB(){
        synchronized (this){
            System.out.println("methodB ....");
        }
    }
    public void methodC(){
        String str = "sss";
        synchronized (str){
            System.out.println("methodC....");
        }
    }

    public static void main(String[] args) {
        final synchronized_method obj = new synchronized_method();
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                obj.methodA();
            }
        });
        t1.start();
        Thread t2 =new Thread(new Runnable() {
            @Override
            public void run() {
                obj.methodB();
            }
        });
        t2.start();
        Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {
                obj.methodC();
            }
        });
        t3.start();
    }
}
