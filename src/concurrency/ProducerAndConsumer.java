package concurrency;

public class ProducerAndConsumer {

    public static void main(String[] args) {
        Clerk clerk = new Clerk();
        Thread produceThread = new Thread(new Producer(clerk));
        Thread consumeThread = new Thread(new Consumer(clerk));
        produceThread.start();
        consumeThread.start();
    }
}
class Clerk {
    private static final int MAX_PRODUCT = 20;
    private static final int MIN_PRODUCT = 10;
    private int product_number = 0;

    //如果设置程对象，同步对象即可，这里同步product_number 所以同步方法区即可
    public synchronized void produce() {
        if (this.product_number >= MAX_PRODUCT) {
            try {
                //进入等待时间
                wait();
                System.out.println("产品已满,请稍候再生产");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //不需要等待情况下的值
        else {
            this.product_number++;
            System.out.println("生产者生产了第" + this.product_number + "个产品");
            notifyAll();
        }
    }


    public synchronized void consume() {
        if (this.product_number < MIN_PRODUCT) {
            try {
                wait();
                System.out.println("产品处于缺货状态\"");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            this.product_number--;
            notifyAll();
        }
    }
}
    class Producer extends Thread {
        private Clerk clerk;

        public Producer(Clerk clerk) {

            this.clerk = clerk;
        }


        @Override
        public void run() {
            System.out.println("生产者开始生产产品");
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                clerk.produce();
            }
        }
    }

    class  Consumer extends  Thread{
    private Clerk clerk;

        public Consumer(Clerk clerk) {
            this.clerk = clerk;
        }

        @Override
        public void run() {
            System.out.println("消费者开始消费产品");

            while(true){
                try{
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                clerk.consume();
            }
        }
    }


