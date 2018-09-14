package Tree;

import sun.awt.image.ImageWatched;

import java.util.*;

public class PostOrderTraverse {
    public List<Integer>  solution(TreeNode root){

        LinkedList<Integer> result = new LinkedList<>();
        //双端队列
        Deque<TreeNode>  stack =new ArrayDeque<>();
        TreeNode p =root;
        while(!stack.isEmpty() || p!=null){
            if(p!=null)
            {

                stack.push(p);
                //
                result.addFirst(p.val);
                //先执行左边的情况，
                p = p.right;
            }
            else
            {
                //类似与中序遍历，非递归遍历方法来执行情况
                TreeNode node = stack.pop();
                p = node.left;
            }

        }
        return result;
    }



    public List<Integer> solution_1(TreeNode root){

        List<Integer> result =new LinkedList<>();
        Stack<TreeNode> stack = new Stack<>();
        TreeNode p = root;
        TreeNode listVisited= root;

        while(p!=null || !stack.isEmpty()){
            while(p!=null){
                //一直走到最左边的节点，这个节点肯定要第一个访问的，类似与中序遍历中一样，中序遍历与后序遍历的唯一差别就是，根节点和右孩子节点的区别而已
                stack.push(p);
                p = p.left;
            }
            //获取到最左的情况,获取栈顶部的元素，
            p = stack.peek();

            //p是叶子节点或者p是访问的上面的，这种情况时访问根节点情况
            if(p.right==null || p.right==listVisited){
                //可以访问p的值l;
                result.add(p.val);
                listVisited = p;
                stack.pop();
                //这里一定要注意，一定要重置为null，否则p不为空的话继续执行压入栈处理操作，
                //p==null 是执行出栈处理的情况
                p = null;
            }
            else{

                //此种情况下p的右节点肯定不为空，执行压入栈处理
                p =p.right;
            }
        }
        return result;
    }

}
