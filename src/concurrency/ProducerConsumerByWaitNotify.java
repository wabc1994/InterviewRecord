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
            /**
             * 实现对该变量对逐渐增加一个操作
             */
            index = autoIndex.getAndIncrement();

        }
        @Override
        public String toString() {
            return "product :" +  index ;
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
                    //操作好后通知所有的人
                    queue.notifyAll();
                }
            }
        }

        void produce(){
            //代表生产一个产品

            Product p =new Product();
            queue.offer(p);
            System.out.println("produce "+ p.toString());
        }
    }
    class Consumer implements  Runnable{
        void consume(){
            System.out.println("consume "+ queue.remove(queue.size()-1).toString());
        }
        @Override
        public void run() {
            while(true){
                synchronized (queue){
                    //判断是否满足一定的条件
                    if(queue.size()<=0){
                        try{
                            //也可以选择让这个地方的线程进行睡眠
                            //Thread.sleep(1000),让其满足一定的条件后再进行执行即可
                            queue.wait();
                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }

                    }
                    else{
                        //满足一定的条件，进行一定程度上面的
                        consume();
                    }
                queue.notifyAll();
                }

            }
        }
    }

    public static void main(String[] args) {
        ProducerConsumerByWaitNotify pcbw = new ProducerConsumerByWaitNotify();
        Producer producer = pcbw.new Producer();
        Consumer consumer = pcbw.new Consumer();
        new Thread(producer).start();
        new Thread(consumer).start();
        new Thread(consumer).start();
    }
}
