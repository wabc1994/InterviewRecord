package SortedArray;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @ClassName MyStackQueue
 * @Description TODO
 * @Author coderlau
 * @Date 2019/1/21 1:35 PM
 * @Version 1.0
 **/
public class MyStackQueue {

    // 使用两个队列来模拟stack,其中一个队列存放正常的元素情况，另一个stack 存放的是stack 顶部的元素情况

    //

    private Queue<Integer> queue1,queue2;


    // 基础的构造函数

    public MyStackQueue() {
        this.queue1 = new LinkedList<>();


    }


    public void push(int x){
      queue1.offer(x);
      for(int i=0;i<queue1.size()-1;i++){
          queue1.offer(queue1.poll());
      }
    }


    public void top(){
       queue1.peek();
    }

    public void pop(){
        queue1.poll();
    }


    public void isEmpty(){
        queue1.isEmpty();
    }


}
