package Tree;

import java.util.Stack;

public class KthSmallestElementinaBST {
    //堆排序针对的是数组，数组都是排列好的情况进行排列，使用数组进行构建
    public int solution_1(TreeNode root, int k){

        //how can i transfer the root to the array format;

        //利用二叉树的层次遍历，中间层次是有序数组的情况

        Stack<TreeNode> stack =new Stack<>();
        TreeNode p =root;
        while(p!=null|| !stack.isEmpty())
        {
            while(p!=null){
                stack.push(p);
                p=p.left;
            }
            if(!stack.isEmpty()){
                p = stack.pop();
                --k;
                if(k==0)
                { return p.val;}
                p = p.right;
            }
        }
        return -1;
    }
    //利用二叉搜索树的基本性质即可
    public int solution_2(TreeNode root,int k){
        //递归出口条件
        if(root!=null) {
            if (root.right==null && root.left==null && k==1) {return root.val;}
            int n = count(root.left);
            if (k <= n) {
               return solution_2(root.left, k);

            } else if(k>n+1){
               return solution_2(root.right, k-n-1);
            }

        }
        return root.val;
    }
    private int count(TreeNode root) {
        if (root == null) {
            return 0;
        }
        return 1+ count(root.left)+ count(root.right);
    }






    public void heaplify(int [] array,  int i){
        int l = left(i);
        int r = right(i);
        int min = array[i];
        if(array[l]<min){
            min = l;
        }
        if(array[r]<array[min]){
            min = r;
        }
        if(min!=i){
            swap(array[min],array[i]);
            heaplify(array, min);
        }

    }
    private void swap(int a,int b){
        int tmp =a;
        a=b;
        b =a;
    }
    private int left(int i){
        return 2* i+1;
    }
    private int right(int i){
        return 2* i +2;
    }

   private void create_heap(int []array){
        int n =array.length;
        int start = n-1/2;
        for(int i=start;i>=0;--i){
            heaplify(array,i);
        }

    }




}
