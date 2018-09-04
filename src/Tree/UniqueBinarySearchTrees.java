package Tree;

import java.util.ArrayList;
import java.util.List;

public class UniqueBinarySearchTrees {

    //采用分治算法解决即可，类似于归并排序的问题，采用树形结构不断进行合并的问题
    public List<TreeNode> generateTrees(int n) {
        return genTrees(1, n);//从1作为root开始，到n作为root结束
    }


    //
    private List<TreeNode> genTrees(int start,int end){
       List<TreeNode> list = new ArrayList<>();
       //递归的结果出口
       if(start>end){
           list.add(null);
           return list;

       }

       if(start==end){
           list.add(new TreeNode(start));
           return list;
       }
       List<TreeNode> left, right;
       for(int i= start;i<=end;i++){

           //分开两边的情况，左右两边的情况，类似于分治法
           left = genTrees(start,i-1);
           right = genTrees(i+1,end);
           //两边两两组合起来即可，
           for(TreeNode Lnode :left){
                for(TreeNode rnode:right){
                    TreeNode root  = new TreeNode(i);
                    root.left = Lnode;
                    root.right= rnode;
                    list.add(root);
                }
            }
       }
       return  list;
    }


}




