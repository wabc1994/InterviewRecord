public class SyncThread implements Runnable {
    private static int count;
     //第一种创建线程的方法

    public SyncThread() {
        count = 0;
    }
    @Override
    //一个线程访问一个对象中的synchronized(this)同步代码块时，其他试图访问该对象的线程将被阻塞，
    public void run() {
        synchronized (this){
            for(int i=0;i<5;++i)
            {
                try {
                    System.out.println(Thread.currentThread().getName() + ":"+(count++));
                    Thread.sleep(100);
                }catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    public static int getCount() {
        return count;
    }

    public static void main(String[] args) {
        SyncThread syncThread   =new SyncThread();
        //类对象的向上升级，子类的
        Thread thread1 = new Thread(syncThread,"SyncThread1");
        Thread thread2 = new  Thread(syncThread,"SyncThread2");
        thread1.start();
        thread2.start();
    }
}
