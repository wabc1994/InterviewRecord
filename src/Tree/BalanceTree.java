package Tree;

public class BalanceTree {
    public boolean isBalanced(TreeNode root) {
        if(root==null) return true;
        return height(root)!=-1;
    }
    private int height(TreeNode root){
        //下到上了，下面的如果不满题的话，
        if(root==null) return 0;
        int l=height(root.left);
        //先进行判断，如果左子树都不满足bst,，则不用再次进行计算，直接返回错误的结果即可
        // - 1标志是否满足题意的情况
        if(l==-1)
            return -1;
        int r = height(root.right);
        if(r==-1)
            return -1;
        //如果计算处理的条件不满足的话
        if(Math.abs(l-r)>1)
            return -1;
        return Math.max(l,r)+1;
    }


}
