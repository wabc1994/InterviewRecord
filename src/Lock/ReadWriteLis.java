package Lock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * ReadWriteLock 也是一个接口，ReentrantReadWriteLock 是其实现机制
 */

public class ReadWriteLis<E> {
    private List<E> list  = new ArrayList<>();
    private ReadWriteLock rwLock =  new ReentrantReadWriteLock();
    public ReadWriteLis(E initialElements) {
        list.addAll(Arrays.asList(initialElements));
    }
    public void add(E element) {

        //多个锁需要重新定义
        Lock writeLock = rwLock.writeLock();
        writeLock.lock();
        try {
            list.add(element);
        } finally {
            writeLock.unlock();
        }
    }

    public E get(int index) {
        Lock readLock = rwLock.readLock();
        readLock.lock();
         try {
             return list.get(index);
 } finally {
       readLock.unlock();
      }
    }
    public int size() {
        Lock readLock = rwLock.readLock();
        readLock.lock();

        try {
            return list.size();
        } finally {
            readLock.unlock();
        }
    }
}
