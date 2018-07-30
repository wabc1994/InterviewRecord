package Communicate;

public class testnotify {
    //主线程停止不前，没有被唤醒
    public static void main(String[] args) {
        try{
            String lock  =new String();
            System.out.println("syn上面");
            synchronized (lock){
                System.out.println("syn第一行");
                lock.wait();
                System.out.println("waint 后面");
            }
            System.out.println("syn后面的代码");
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
