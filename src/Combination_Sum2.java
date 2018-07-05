import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Combination_Sum2 {
    /*
    Input: candidates = [2,5,2,1,2], target = 5, 可以出现相同元素，并且每个元素只能出现一次，跟有相同元素的subset
    一样的解法
    A solution set is:
   [
   [1,2,2],
   [5]
   ]*/

    public List<List<Integer>> combinationSum2(int [] candidates, int target)
    {
        List<List<Integer>>  result=new ArrayList<>();
        List<Integer> path =new ArrayList<>();
        if(candidates.length==0) return result;
        Arrays.sort(candidates);
        dfs(candidates,target,0,result,path);
        return result;
    }

    private void dfs(int[]candidates,int target,int start, List<List<Integer>> result,List<Integer>path)
    {
        if(target<0) return;
        if(target==0)
            result.add(path);
        for(int i=start;i<candidates.length;i++)
        {
            //处理相同元素
            //   if(i>start && candidates[i]==candidates[i-1]) continue;
            path.add(candidates[i]);
            dfs(candidates,target-candidates[i],i+1,result,path);//每个元素只能使用一次
            path.remove(path.size());
            //后向去重法
            while(i+1<candidates.length &&candidates[i]==candidates[i+1]) ++i;
        }
    }
}
