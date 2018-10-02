package Tree;
/**
 *从前序遍历和中序遍历中构建原来的树情况
 */
public class ConstructBinaryTreefromPreorderandInorderTraversal {
    public TreeNode buildTree(int[] preorder, int[] inorder) {
        TreeNode root=null;
        int n =  preorder.length;
        if(n==0)  {return root;}
        return helper(preorder,0,n-1,inorder, 0,n-1);
    }

    private TreeNode helper(int [] preorder, int start_1,int last_1, int []inorder, int start_2,int last_2){
        if(start_1>last_1 || start_2>last_2) {return null;}

        TreeNode root = new TreeNode(preorder[start_1]);
        //找到的是索引位置，不是左边的元素长度情况

        int index = find(preorder[start_1],inorder, start_2,last_2);

        //左边的元素个数是index,
        int leftsize = index-start_2;


        // inorder : 开始位置 start_2 ，截止位置是index-1;
        root.left = helper(preorder,start_1+1,start_1+leftsize,inorder, start_2,index-1);
        //index + 1 代表开始位置, last_2 代表截止位置
        root.right = helper(preorder,start_1+leftsize+1,last_1,inorder,index+1,last_2);

        return root;
    }

    private int find(int element,int [] array, int start, int last){
        //查找成功返回一个数组下标元素
        for(int i=start;i<=last;i++){
            if(array[i]==element)
            {return i;}
        }
        //否则返回0
        return -1;
    }
}


