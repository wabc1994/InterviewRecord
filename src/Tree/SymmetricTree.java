package Tree;

import java.util.*;

/**
 * Given a binary tree, check whether it is a mirror of itself (ie, symmetric around its center).
 *
 * For example, this binary tree [1,2,2,3,4,4,3] is symmetric:
 *
 *     1
 *    / \
 *   2   2
 *  / \ / \
 * 3  4 4  3
 * But the following [1,2,2,null,3,null,3] is not:
 *     1
 *    / \
 *   2   2
 *    \   \
 *    3    3
 *    //判断相同的镜像树
 */
public class SymmetricTree {
     //递归方法解决该问题
    public boolean isSymmetric(TreeNode root) {
    //如果不是为空的情况下，如何进行处理
        //求出镜像树
        if(root==null || (root.right==null && root.left==null)) {return true;}
       //重复判断
       // if(root.right==null) {return false;}
        //if(root.left==null){return false;}

        return helper(root.left,root.right);
    }


    private boolean helper(TreeNode left, TreeNode right){
        if(left==null && right==null ){return true;}
        if(left==null) {return false;}
        if(right==null) {return false;}
        if(right.val!=left.val) {
            return false;
        }
        return helper(left.left,right.right) && helper(right.left,left.right);
    }
    /**  对上述代码进行优化情况
     *
    class Solution {
        public boolean isSymmetric(TreeNode root) {
            //如果不是为空的情况下，如何进行处理
            //求出镜像树
            return root==null || helper(root.left, root.right);
        }
        private boolean helper(TreeNode left, TreeNode right){
            if(left==null|| right==null) return left == right;
            if(right.val!=left.val) {
                return false;
            }
            return helper(left.left,right.right) && helper(right.left,left.right);
        }
    }*/
    //递归做法的多种写法
    private boolean recursiveIsSymmetric(TreeNode left, TreeNode right) {

        if (null == left && null == right) {
            return true;
        }
        if (null == left || null == right) {
            return false;
        }
        //
        boolean leftLeftRightRight = recursiveIsSymmetric(left.left, right.right);
        boolean leftRightRightLeft = recursiveIsSymmetric(left.right, right.left);
        if (leftLeftRightRight && leftRightRightLeft && left.val == right.val) {
            return true;
        }
        return false;
    }
    //非递归方法进行解决
    //使用stack
    private boolean isSymmetricISC(TreeNode root) {
        if (root == null) {
            return true;
        }
        Stack<TreeNode> stack = new Stack<>();
        stack.push(root.left);
        stack.push(root.right);
        while (!stack.empty()) {
            TreeNode right = stack.pop();
            TreeNode left = stack.pop();
            if (left == null && right == null) {
                continue;
            }
            if (left == null || right == null || left.val != right.val) {
                return false;
            }
            stack.push(left.left);
            stack.push(right.right);
            stack.push(left.right);
            stack.push(right.left);
        }
        return true;

    }
    //使用队列


}
