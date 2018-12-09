package Tree;

import java.util.LinkedList;
import java.util.List;

/**
 * @ClassName ZTree
 * @Description TODO
 * @Author coderlau
 * @Date 2018/12/7 3:34 PM
 * @Version 1.0
 **/
public class ZTree {
    public List<List<Integer>> zigzagLevelOrder(TreeNode root) {
        //利用层次遍历的基本情况
        //该种情况如何处理
        // 元素可以从中间左右两边进行一个基本情况的添加
        List<List<Integer>>result = new LinkedList<>();
        if(root==null)
        {return result;}
        helper(root, result, 0);
        return result;
    }
    private void helper(TreeNode root,  List<List<Integer>> result, int depth){
        if(root==null)
        { return;}
        if(depth >= result.size())
        { result.add(new LinkedList<>());}
        // 在这里取出一个元素而已， 实质上并没有改变原来的元素情况，


        if(depth >>1== 0){
            result.get(depth).add(root.val);
        }
        else{
            result.get(depth).add(0,root.val);
        }
        helper(root.left, result, depth+1);
        helper(root.right, result, depth+1);
    }
    // 上述是第一种解法 也可以使用层次遍历， 标记遍历的每层节点数目情况
}
