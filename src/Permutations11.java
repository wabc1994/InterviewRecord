import java.util.*;
import static java.util.Arrays.*;
public class Permutations11 {
    //with 相同的元素即可
    public List<List<Integer>> permuteUnique(int[] nums)
    {
        sort(nums);
        List<List<Integer>> result = new ArrayList<>();
        if(nums.length==0) return result;
        List<Integer> path = new ArrayList<>();
        boolean []mark = new boolean[nums.length];
        Arrays.fill(mark,false);
        dfs(nums,mark,path,result);
        return result;
    }
    private void dfs(int []nums, boolean [] mark,List<Integer>path, List<List<Integer>> result)
    {

        if(path.size()==nums.length )
        {
            result.add(new ArrayList<>(path));
            return;
        }
        for(int i=0;i<nums.length;i++)
        {
            //如果这个元素被被访问过的直接不处理
            if(mark[i]) continue;
            //去重元素,第一种去重
            if(i>0 && nums[i]==nums[i-1] && !mark[i-1]) continue;//do nothing to the same element,跳出循环，第一个不考虑
            //类似subset处理
            if(!mark[i])
            {
                mark[i]=true;
                path.add(nums[i]);
                dfs(nums, mark,path, result);
                path.remove(path.size()-1);
                //这次递归到最后一次后，又要把所有的标记数组标注为false,每一次作准备，
                //确保每一次从根结点开始处，标注数组都要保持
                mark[i]= false;
                //第二种去重方法 while(i+1<nums.length && nums[i]==nums[i+1]) ++i;(后向去重)
            }
        }
    }
    //采用HashSet 实现去重,
    public List<List<Integer>> solution_3(int[] nums) {
        HashSet<List<Integer>> rm = new HashSet<>();
        Arrays.sort(nums);
        List<List<Integer>> result = new ArrayList<>();
        if (nums.length == 0) return result;
        List<Integer> temp = new ArrayList<>();
        boolean[] mark = new boolean[10];
        Arrays.fill(mark, false);
        dfs_1(nums, mark, temp, result, rm);
        return result;
    }
    private void dfs_1(int []num, boolean []visited,List<Integer>tmp, List<List<Integer>> result,Set<List<Integer>> rm )
    {
        if(tmp.size()==num.length && !rm.contains(new ArrayList<>(tmp)))
        {
            //下一步停止，0 算作是第一层的情况，n-1代表是下一个元素
            result.add(new ArrayList<>(tmp));
            rm.add(new ArrayList<>(tmp));
            return;
        }
        for(int i=0;i<num.length;i++)
        {
            //如果没有被访问
            if(!visited[i])
            {
                visited[i] = true;
                tmp.add(num[i]);
                dfs_1(num,visited,tmp,result,rm);
                //弹出最后一个元素
                tmp.remove(tmp.size()-1);
                //访问完成，
                visited[i] = false;
            }
        }
    }
}
