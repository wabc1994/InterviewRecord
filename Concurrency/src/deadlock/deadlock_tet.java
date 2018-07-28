package deadlock;

public class deadlock_tet {
    public static void main(String[] args) {
        try{
            DealThread t1= new DealThread();
            t1.setFlag("a");
            Thread thread1 =new Thread(t1);
            thread1.start();
            Thread.sleep(1000);
            t1.setFlag("b");
            Thread thread2 =new Thread(t1);
            thread2.start();
            }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
