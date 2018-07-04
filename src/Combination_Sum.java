import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Combination_Sum {
    /**
     * Input: candidates = [2,3,5], target = 8,
     * A solution set is:
     * [
     *   [2,2,2,2],
     *   [2,3,3],
     *   [3,5]
     * ]
     * 什么时候才能在递归里面添加上遍历的顺序，就是遍历的start，当考虑元素顺序时（比如全排列时，就要加上start,）
     * 比如[2,3,4][4,2,3]是不同的元素，但
     */
    public List<List<Integer>> solution(int[] nums, int target)
    {
        List<List<Integer>> result = new ArrayList<>();
        List<Integer> temp = new ArrayList<>();
        if(nums.length==0) return result;
        Arrays.sort(nums);
        dfs(nums,target,result,temp,0);
        return result;
    }
    private void dfs(int [] nums,int target,List<List<Integer>> result, List<Integer>temp,int start)
    {
        //程序停止的步骤
        //条件剪枝，subset是不需要剪枝的，全排列是需要剪枝(元素的个数满足跟数组一样)， combination 也是一样
        //对数目有要求
        if(target<0) return;//剪枝条件停止
        if(target==0)
        {
            result.add(new ArrayList<>(temp));
            return; //是判断停止的条件
        }
        for(int i=0;i<nums.length;i++)
        {
            //这里不需要进行条件判断，
                temp.add(nums[i]);
                dfs(nums,target-nums[i],result,temp,i); //i可以重新使用
                temp.remove(temp.size()-1);

        }

    }
}
