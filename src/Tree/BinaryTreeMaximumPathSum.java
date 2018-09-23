package Tree;

import apple.laf.JRSUIUtils;

public class BinaryTreeMaximumPathSum {
    int maxValue;
    public int maxPathSum(TreeNode root) {
        maxValue = Integer.MIN_VALUE;
        helper_2(root);
        //helper_1(root);
        return maxValue;
    }
    private int helper_1(TreeNode tree) {
        if (tree == null) return 0;
        int left = helper_1(tree.left);
        int right = helper_1(tree.right);
        //更新结果是否等情况
        if (left < 0) {
            maxValue = Math.max(maxValue, tree.val + right);

        } else if (right < 0) {
            maxValue = Math.max(maxValue, tree.val + left);
        } else {
            maxValue = Math.max(maxValue, tree.val + right + left);
        }
        return Math.max(right, left) + tree.val;
    }


    private int helper_2(TreeNode root) {
        if (root == null) {
            return 0;
        }
        //小于0的则不取，使用0作为替代工具即可
        int left = Math.max(helper_2(root.left),0);
        int right = Math.max(helper_2(root.right),0);
        // Computes the max ret it could get in current recursion
        //更新
        maxValue= Math.max(maxValue, left+root.val+right);


        // Returns the largest path starting with current node,
        // It could be the single node, or the node + left or right path
        //路径只可能两种情况中一种
        return Math.max(root.val+left, root.val+right);
    }
}
