package Tree;

import java.util.*;

/**
 * 二叉树的层次遍历等情况
 *Given a binary tree, return the level order traversal of its nodes' values. (ie, from left to right, level by level).
 *
 * For example:
 * Given binary tree [3,9,20,null,null,15,7],
 *     3
 *    / \
 *   9  20
 *     /  \
 *    15   7
 * return its level order traversal as:
 * [
 *   [3],
 *   [9,20],
 *   [15,7]
 * ]
 */

public class BinaryTreeLevelOrderTraversal {
    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();

        //如何记录层次关系即可
        Queue<TreeNode> deque = new LinkedList<>();
        if(root==null){ return result;}
        deque.add(root);

        while(!deque.isEmpty()){
            //弹出该元素即可, 如何判断是属于同一层次的关系
            List<Integer> path = new ArrayList<>();
            //level 属于遍历高层次关系，
            int level = deque.size();

            for(int i=0;i<level;i++){

                    //先处理左右的元素情况
                TreeNode p = ((LinkedList<TreeNode>) deque).pop();
                path.add(p.val);
                    if(p.left!=null){
                        deque.offer(p.left);
                    }
                    if(p.right!=null){
                        deque.offer(p.right);
                    }
            }
            result.add(path);
        }
        return result;

    }

}
