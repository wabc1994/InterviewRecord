package Tree;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Stack;
import java.util.TreeMap;

/**
 * @ClassName InvertBinaryTree
 * @Description TODO
 * @Author coderlau
 * @Date 2018/12/6 1:37 PM
 * @Version 1.0
 **/
public class InvertBinaryTree {
    public TreeNode invertTree(TreeNode root) {
        if (root == null || (root.right == null && root.left == null)) {
            return root;
        }
        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()) {
            TreeNode node = stack.pop();
            TreeNode left = node.left;
            node.left = node.right;
            node.right = left;
            if (node.right != null) {
                stack.push(root.right);
            }
            if (node.left != null) {
                stack.push(root.left);
            }
        }
        return root;
    }

    public TreeNode solution_2(TreeNode root) {
        if (root == null || (root.right == null && root.left == null))
        {return root;}
        Deque<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            TreeNode left = node.left;
            node.left = node.right;
            node.right = left;
            if (node.right != null) {
                queue.offer(root.right);
            }
            if (node.left != null) {queue.offer(root.left);
            }
        }
        return root;
    }
}
