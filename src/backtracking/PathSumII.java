package backtracking;

import Tree.TreeNode;

import java.util.*;


public class PathSumII {
    public List<List<Integer>> pathSum(TreeNode root, int sum) {
        List<List<Integer>> result = new ArrayList<>();
        if(root==null) return result;
        List<Integer> tmp=new ArrayList<>();
        return dp(root, result,sum, tmp);
    }
    private  List<List<Integer>> dp(TreeNode root,List<List<Integer>> result,int sum,List<Integer> tmp){
        //满足题意的前提条件
        if(root==null) return null; //the same as null

        tmp.add(root.val);
        if(sum==root.val &&root.right==null && root.left==null )
             result.add(new ArrayList<>(tmp));
        //处理根根节点情况,

        dp(root.left,result,sum-root.val, tmp);
        dp(root.right,result,sum-root.val,tmp);
        tmp.remove(tmp.size()-1);
        return result;
    }



    //第二种回溯方法,


    private List<List<Integer>> dp_1(TreeNode root,List<List<Integer>> result,int sum,List<Integer> tmp){
        //满足题意的前提条件
        if(root==null) return null;
        //hui

        //正式可以添加的结果
        if(sum==root.val &&root.right==null && root.left==null ){
            //满足题意中间结果可以添加，
            tmp.add(root.val);
            //最终结果可以添加情况
            result.add(new ArrayList<>(tmp));
            //回溯素算法
            tmp.remove(tmp.size()-1);
            //处理根根节点情况,
        }
        //到根节点下面到节点情况
        tmp.add(root.val);
        //左右节点到情况
        if(root.left!=null) {
            dp(root.left,result,sum-root.val,tmp);
            tmp.remove(tmp.size()-1);
        }
        if(root.right!=null) {
            dp(root.right,result,sum-root.val,tmp);
            tmp.remove(tmp.size()-1);
        }
        return result;
    }
}
