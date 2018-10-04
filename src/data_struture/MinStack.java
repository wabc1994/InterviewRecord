package data_struture;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 * 155. Min Stack
 * Design a stack that supports push, pop, top, and retrieving the minimum element in constant time.
 *
 * push(x) -- Push element x onto stack.
 * pop() -- Removes the element on top of the stack.
 * top() -- Get the top element.
 * getMin() -- Retrieve the minimum element in the stack. 要求使用
 */
public class MinStack {
    //构造函数情况

    private int min;
    private Stack<Integer> stack;

    public MinStack() {
        stack = new Stack<>();
    }

    //压入stack
    public void push(int x) {
        if (stack.isEmpty()) {
            min = x;
            stack.push(x);
        }
        if (x < min) {
            min = x;
            stack.push(2 * x - min);
        } else {
            stack.push(x);
        }

    }

    //弹出stack顶部的元素，删除
    public void pop() {
        if (stack.isEmpty()) {
            System.out.println("Stack is empty");
            return;
        }
        System.out.print("Top Most Element Removed: ");
        int y = stack.peek();
        if (y >= min) {
            System.out.println(y);

        } else {
            System.out.println(min);
            min = 2 * min - y;
        }
    }

    //返回stack顶部的元素情况，不删除
    public int top() {
        if (stack.isEmpty()) {
            System.out.println("Stack is empty ");
            return -1;
        }
        Integer y = stack.peek();
        System.out.println("Top Most Element is: ");
        if (y >= min) {
            return y;

        } else {
            return min;
        }
    }

    public int getMin() {
        if (stack.isEmpty()) {
            System.out.println("stack is empty");
            return -1;
        }
        return min;
    }


        public static void main(String[] args) {
        MinStack test =new MinStack();
        test.push(-2);
        test.push(0);
        test.push(-3);
        System.out.println(test.getMin());
        test.pop();
        System.out.println(test.top());
        System.out.println(test.stack.size());
        System.out.println(test.getMin());
    }
}
