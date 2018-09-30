package Lock;

import java.util.Random;

public class Writer extends  Thread {
    private ReadWriteLis<Integer> shareList;
    public Writer(ReadWriteLis<Integer> shareList){
        this.shareList = shareList;
    }

    @Override
    public void run() {
        Random random  = new Random();
        int number = random.nextInt(100);
    shareList.add(number);
    try{
        Thread.sleep(100);
        System.out.println(getName() + " -> put: " + number);
    }catch (InterruptedException oe){
        oe.printStackTrace();
    }
    }
}
