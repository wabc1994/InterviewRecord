package concurrency;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 使用notify() 和wait()实现生产和消费者模型
 */
public class ProducerConsumerByWaitNotify {
    private static AtomicInteger autoIndex = new AtomicInteger();
    /**
     * 共享资源， 存放产品的情况 //生产者仓库，存放的是资源（一些系列的product）
     */
    private static LinkedList<Product> queue = new LinkedList<Product>();

    class Product{

        private int index;

        public Product() {
            index = autoIndex.getAndIncrement();

        }
        @Override
        public String toString() {
            return "product__" + index + "   " + super.toString();
        }
    }

    class Producer implements Runnable{
        @Override
        public void run() {
            //一直执行如下代码结构
            while(true){
                synchronized(queue){
                    if(queue.size()==50){
                        try{
                            queue.wait();
                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        produce();
                    }
                    queue.notifyAll();
                }
            }
        }

        void produce(){
            Product p =new Product();
            queue.offer(p);
            System.out.println("produce "+ p.toString());
        }
    }
    class Consumer implements  Runnable{
        void consume(){
            System.out.println("consume"+ queue.remove(0).toString());
        }

        @Override
        public void run() {
            while(true){
                synchronized (queue){
                    if(queue.size()<=0){
                        try{
                            queue.wait();
                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }

                    }else{
                        consume();
                    }
                queue.notifyAll();
                }

            }
        }
    }

    public static void main(String[] args) {
        //c        ProducerConsumerByWaitNotify pcbw = new ProducerConsumerByWaitNotify();
        Producer producer = pcbw.new Producer();
        Consumer consumer = pcbw.new Consumer();
        new Thread(producer).start();
        new Thread(consumer).start();
        new Thread(consumer).start();
    }
}
