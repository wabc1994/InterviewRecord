package syn;
import syn.service;
public class ThreadA extends  Thread{
    @Override
    public void run(){
        service.printA();
    }
}
