package Threadlocal;



class Bank{
    //Threadlocal解决的是不同线程共享变量的可见性问题，多个线程共享了同一个实列对象的局部变量所致

    private int money = 1000;
    public void deposit(int money){
        String threadName = Thread.currentThread().getName();
        System.out.println(threadName +"--当前账户余额"+this.money);
        this.money +=money;
        System.out.println(threadName+"--存入"+money + "后账户余额为: "+ this.money);
        try{
            Thread.sleep(1000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
public class non_threadlocal {
    public static void main(String[] args) {
        Bank bank = new Bank();
        Thread Xmthread =new Thread(()->bank.deposit(200),"小明");
        Thread xmthread = new Thread(()->bank.deposit(200),"小刚");
        Thread ghthread =new Thread(()->bank.deposit(200), "小红");
        xmthread.start();
        Xmthread.start();
        ghthread.start();

    }
}
