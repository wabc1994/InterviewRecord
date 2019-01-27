import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;

import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;
import com.sun.org.apache.xpath.internal.SourceTree;

// 如何使用两个两个int类型实现一个读写锁的问题，这是阿里面试题


//在zhe



//

public class ReadWriteLock {
    // private static AtomicInteger rAtomicInteger =new AtomicInteger(0);
    private static int readcount = 0;
    private static int writecount = 0;
    // private static AtomicInteger wAtomicInteger =new AtomicInteger(0);

    
    publiwhile (write    wait();    }
        readcount++;
        System.out.printl
      }
    

    

    public synchronized void readUnlock() throws InterruptedException {
        readcount--;
       System.out.println("release the read lock")
        notifyAll();
    }

    public synchronized void writeLock() throws InterruptedException {
        while (writecount > 0) {
            wait();
        }
        writecount++;

    }

    public synchronized void unwriteLock() throws InterruptedException {
        writecount--

    fyAll();
    }

    public static void main(String[] args) {
        ReadWriteLock lock = new ReadWriteLock();
        int count = 0;
        final int number = 5;
        for (int i = 0; i < number; i++) {
            new workWead(count, lock).start();
        }
        for (int i = 0; i < number; i++) {
            new Writework(count, lock).start();
        }

    }
}

class workWead extends Thread {
    private int count;
    private ReadWriteLock readWriteLock = null;

    public workWead(int count, ReadWriteLock lock) {
        this.count = count;
        this.readWriteLock = lock;
    }

    @Override
    public void run() {

        try {
            readWriteLock.readLock();
            sleep(100);
            System.out.println(" thread read number " + count);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

class Writework extends Thread {
    private int count;
    private ReadWriteLock readWriteLock = null;

    public Writework(int count, ReadWriteLock lock) {
        this.count = count;
        this.readWriteLock = lock;
    }

    @Override
    public void run() {

        try {
            readWriteLock.readLock();
            sleep(100);
            count++;
            System.out.println("the write result" + count);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}