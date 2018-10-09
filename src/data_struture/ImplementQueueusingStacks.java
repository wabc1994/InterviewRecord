package data_struture;

import java.util.Stack;

/**
 * 如何使用一个stack实现一个队列
 */
public class ImplementQueueusingStacks {
    private Stack<Integer> in;
    private Stack<Integer> out;
    public ImplementQueueusingStacks() {
        in = new Stack<>();
         out=new Stack<>();
    }
    public  void push(int x){
        in.push(x);
    }
    public int peek(){
    if(out.isEmpty()){
        while(!in.isEmpty())
        {
            out.push(in.pop());
        }
    }
    return out.peek();
    }
    public void pop()
    {
        peek();
        out.pop();

    }
    public boolean empty(){
        return in.size()==0 &&out.size()==0;
    }
}
