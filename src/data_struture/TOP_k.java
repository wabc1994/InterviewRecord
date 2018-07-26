package data_struture;

import java.util.*;

//固定容量的优先级队列，模拟大顶堆，用于解决topN小的问题

public class TOP_k<E extends Comparable> {
    private PriorityQueue<E> queue;
    private int maxSize;

    public TOP_k(int maxSize) {
        if(maxSize<=0) throw new IllegalArgumentException();
        this.maxSize = maxSize;
        this.queue = new PriorityQueue<>(maxSize, new Comparator<E>() {
            @Override
            public int compare(E o1, E o2) {
                return o2.compareTo(o1);
            }
        });
    }
    public void add(E e){
        if(queue.size()<maxSize){
            queue.add(e);
        }
        else
        {
            /* peek获取堆顶的元素但是不删除，element（）获取堆顶元素并且删除 */
            E peek;
            peek = queue.peek();
            if(e.compareTo(peek)<0){
                //poll（）获取并删除堆顶元素，如果优先级队列为空，则返回null,
                queue.poll();
                queue.add(e);
            }
        }
    }
    public  List<E> sortedList(){
        List<E> list = new ArrayList<>(queue);
        Collections.sort(list);
        return list;
    }

    public static void main(String[] args) {
        final TOP_k pq = new TOP_k<>(10);
        Random random = new Random();

        for(int i=0;i<100;i++){
            int rNum = random.nextInt(1000);
            System.out.println(rNum);
            pq.add(rNum);
        }
        Iterable<Integer> iter = new Iterable<Integer>() {
            @Override
            public Iterator<Integer> iterator() {
                return pq.queue.iterator();
            }
        };
        for(Integer item:iter){
            System.out.println(item+ ",");
        }
        System.out.println();
        System.out.println("PriorityQueue 排序后的遍历");
        while(!pq.queue.isEmpty()) System.out.println(pq.queue.poll() + ",");
    }
}
