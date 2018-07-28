package syn;

public class service {
    synchronized public static void printA(){
        try{
            System.out.println("线程名字："+ Thread.currentThread().getName()+"在"+System.currentTimeMillis()+"进入print(A)");
            Thread.sleep(3000);
            System.out.println("线程名字："+ Thread.currentThread().getName()+"在"+System.currentTimeMillis()+"出去print(A)");

        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }
    synchronized  public static void printB(){
        System.out.println("线程名字："+ Thread.currentThread().getName()+"在"+System.currentTimeMillis()+"进入print(B)");
        System.out.println("线程名字："+ Thread.currentThread().getName()+"在"+System.currentTimeMillis()+"出去print(B)");


    }
}
