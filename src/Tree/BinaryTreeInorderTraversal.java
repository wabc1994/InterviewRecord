package Tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class BinaryTreeInorderTraversal {
    //实现中序遍历
    public List<Integer> solution_1(TreeNode root) {
      List<Integer> result =new ArrayList<>();
      if(root==null) return result;
      solution_1(root.left);
      result.add(root.val);
      solution_1(root.right);
      return result;
    }

    public List<Integer> solution_2(TreeNode root) {
        List<Integer> result =new ArrayList<>();
        if(root==null) return result;
        Stack<TreeNode> tmp = new Stack<>();
        TreeNode p = root;
        //p是遍历指针，如果一直有最左边结点，一直遍历； 第二种情况是如果没有最左边结点，就要访问stack中的结点
        while(p!=null || !tmp.isEmpty()) {
            //两种情况
            //第一种情况，来到最左边的结点,最左边的结点肯定是没有左孩子结点
            while (p != null) {
                tmp.add(p);
                p = p.left;
            }

            //判断最左边的结点是否有右结点，继续按照上面的步骤遍历遍历到其最左边的结点
            if (!tmp.isEmpty()) {
                //遍历最左边的结点
                TreeNode t = tmp.pop();
                result.add(t.val);
                if (t.right != null)
                    p = t.right;

            }
        }
        return result;
    }

}
