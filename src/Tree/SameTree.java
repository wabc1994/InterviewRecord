package Tree;

public class SameTree {
    //两个数都为空情况下，是sameTree,
    //分为三种情况
    public boolean isSameTree(TreeNode p, TreeNode q) {
       if(p==null && q==null) return true;
       if(p==null || q==null) return false;
    //先根遍历的情况
       if(p.val ==q.val)
            return  isSameTree(p.left,q.left) && isSameTree(p.right,q.right);

    //其中一个树为空，另一个树不为空，则return false;
        return false;
}
}
