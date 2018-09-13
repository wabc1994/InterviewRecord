package Tree;


import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

//preorder traversal ,采用stack来使用情况
public class BinaryTreePreorderTraversal {
    public List<Integer> solution(TreeNode root) {

        Stack<TreeNode> stack = new Stack<>();
        List<Integer> result = new ArrayList<>();
        if (root == null) return result;
        stack.add(root);
        while (!stack.isEmpty()) {
            TreeNode pre = stack.pop();
            result.add(pre.val);
            if (pre.right != null) {
                stack.add(pre.right);
            }
            if (pre.left != null) {
                stack.add(pre.left);
            }
        }
        return result;
    }
}