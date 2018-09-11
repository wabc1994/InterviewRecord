package Tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


public class LowestCommonAncestor {
    //最近的公共的祖先情况
    public TreeNode solution(TreeNode root,TreeNode p, TreeNode q){
        if(root==null) return null;
        //如果是
        if(root==p || root==q) return root;
        TreeNode left = solution(root.left, p,q);
        TreeNode right = solution(root.right,p,q);
        if(left!=null && right!=null){
            return root;
        }
        return left!=null?left:right;
    }
}
