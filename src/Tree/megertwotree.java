package Tree;

public class megertwotree {

    //树中常见的递归套路题目
    //八股文写法， 先处理根节点，然后就是左边，右边
    // 根的左孩子节点= 递归调用函数(根节点的左孩子节点)
    // 根的右孩子节点= 递归调用函数(根节点的右孩子节点)

    //这个

    public TreeNode meger(TreeNode t1, TreeNode t2){
        if(t1==null&& t2==null){
            return null;
        }
        else if(t1!=null && t2!=null){
            //还是两部走的步骤
            t1.val+= t2.val;
            t1.right =meger(t1.right,t2.right);
            t1.left =meger(t2.left,t2.left);
        }
        else {
            return t1 == null ? t1 : t2;
        }
        return t1;

    }
    public TreeNode  solution(TreeNode t1,TreeNode t2){
        if(t1==null && t2==null)
        {
            return null;
        }
        else if(t1==null)
        {
            return t2;
        }
        else if(t2==null){
            return t1;
        }
        else {
            TreeNode merged= new TreeNode(t1.val+t2.val);
            merged.left=solution(t1.left,t2.left);
            merged.right=solution(t1.right,t2.right);
            return merged;
        }
    }
}
