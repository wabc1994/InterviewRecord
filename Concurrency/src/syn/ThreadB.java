package syn;

public class ThreadB extends Thread{
    @Override
    public void run(){
        service.printB();
    }
}
