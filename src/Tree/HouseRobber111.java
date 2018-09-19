package Tree;

public class HouseRobber111 {
    //一个二叉树代表，节点中的值代表可以的偷的钱，
    //类似于红黑树，不能都是偷父子节点，即有边相连的两个节点不能偷
    //就比如查找两个节点的最近公共祖先节点的情况下
    //对于一个节点而言
    //有三种情况，第一种是不要这个节点，可以加入左右两个节点即可（可同时加入）
    //第二种是不要要加入该节点，这种情况就是不要左右子节点的情况
public int rob(TreeNode root){
    //程序的跳出接口,来到最后一个节点初
        if(root==null) return 0;
    //叶节点
    int root_val = 0;
    //向上层值返回结果即可
        if(root.right==null && root.left==null) {
        return root.val;
    }
    //有一边为叶子节点
    //下面这种两种情况是
    //排除了根节点了的情况
    //综合处理该情况
    //采用叠加的方式
    int left = rob(root.left);
    int right = rob(root.right);
    //防止空指针异常，左右子树有一种情况
        if(root.left != null)
    root_val += rob(root.left.right)+ rob(root.left.left);
        if(root.right!=null)
    root_val += rob(root.right.right) + rob(root.right.left);
        return Math.max(root.val+ root_val, left + right);
}

}
