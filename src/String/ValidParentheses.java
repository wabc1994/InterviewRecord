package String;

import java.nio.charset.CharacterCodingException;
import java.util.Stack;

public class ValidParentheses {
    //如何证明括号的有效性问题

    //采用stack来证明该问题

    public boolean solution(String s){
        if(s==null || s.length()==1)
        {
            return false;
        }
        Stack<Character> stack = new Stack<>();

        //   先执行上述的步骤情况

        for(char c:s.toCharArray()) {
            //前面都是入stack ，后面的话就是出stack的情况
            if (c == '(') {
                stack.push(c);
            } else if (c == '[') {
                stack.push(c);
            } else if (c == '{') {
                stack.push(c);
            }
            //不过不是上述字符的话就不用入stack,就要换一方式判断该种情况

            else if(!stack.isEmpty()){
                char tmp = stack.peek();
                //分别有三种情况可以考虑, 分别是配对成功的情况
                if (tmp == '(' && c == ')'){
                    stack.pop();

                } else if (tmp == '[' && c == ']') {
                    stack.pop();
                } else if (tmp == '{' && c == '}') {
                    stack.pop();
                }
             else
                {
                    return false;
                }

            }
            //如果是一开始就是这类字符 })] 的话，那么其实可以直接返回错误的情况了
            else
            {
                return false;
            }
        }

        if(stack.isEmpty())
            {
            return true;
        }
        else {
            return false;
        }

    }


    //简介版代码情况

    public boolean solution_2(String s){
        Stack<Character> stack = new Stack<>();
        for(int i=0;i<s.length();i++){
            if(s.charAt(i)=='(' || s.charAt(i)=='['|| s.charAt(i)=='{')
            {
                stack.push(s.charAt(i));
            }
            else if(s.charAt(i)==']' && !stack.isEmpty() && stack.peek()=='['){
                stack.pop();
            }
            else if(s.charAt(i)=='}' && !stack.isEmpty() && stack.peek()=='{'){
                stack.pop();
            }
            else if(s.charAt(i)==')' && !stack.isEmpty() && stack.peek()=='('){
                stack.pop();
            }
            else {
                return false;
            }
        }
        return stack.isEmpty();
    }


    //终极版本代码
    public boolean  solution_3(String s){
        Stack<Character> stack =   new Stack<>();
        for(char c: s.toCharArray()){
            //
            if(c=='{')
            {
                stack.push('}');
            }
            else if(c=='[')
            {
                stack.push(']');
            }
            else if(c=='(')
            {
                stack.push(')');
            }

            //针对 该种情况元素 "}" 一开始就是这种情况的
            else if(stack.isEmpty()||  stack.pop()!=c )
            {
                return false;
            }

        }

        return stack.isEmpty();
    }
}
