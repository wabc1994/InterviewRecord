package Lock;

import java.util.Random;

public class Reader extends  Thread {
    private ReadWriteLis<Integer> sharedList;

    public Reader(ReadWriteLis<Integer> sharedList) {
        this.sharedList = sharedList;
    }
@Override
    public void run() {
        Random random = new Random();
        int index = random.nextInt(sharedList.size());
        Integer number = sharedList.get(index);

        System.out.println(getName() + " -> get: " + number);
        try {
            Thread.sleep(100);
        } catch (InterruptedException ie ) { ie.printStackTrace(); }

    }
}
