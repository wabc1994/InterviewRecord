import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class permutation1{
    //没有相同元素的情况,深度优先优先，
    /**
     *[1,2,3] 结果为[[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]
     * 只考虑满的情况，
     * http://fisherlei.blogspot.com/2012/12/leetcode-permutations.html（参看subset 深度优先的解法）
     */

    public List<List<Integer>> solution_1(int []nums)
    {
        List<List<Integer>> result = new ArrayList<>();
        if(nums.length==0) return result;
        List<Integer> temp = new ArrayList<>();
        boolean [] mark  = new boolean[10];
        Arrays.fill(mark,false);
        /*在这里区别与subset的情况，其中subset 要控制，在这里回溯树的每个结点可选择的数目是num[0],num[1],,,num[length-1]等
        情况，即 [1,2,3] 当树的处理结点是3的时候下面的结点依然可以有三个 供选择，1，2,3,]
        而subset [1,2,3] 当遍历到2时，只有对下面的3进行处理，所有他们是step+1，处理下一个结点
        */
        GeneratePermute(nums, mark,temp,result);
        return result;

    }

    //对外隐藏这个元素，这道题可以参看之前的题目
    //// 时间复杂度O(n!)，空间复杂度O(n)
    private void GeneratePermute(int []num,  boolean []visited, List<Integer>tmp, List<List<Integer>> result)
    {
        //一直遍历到最后一个元素，才考虑添加到最后的结果，确保所有的元素都加进来，
        //如果是考虑所有的情况（不要求所有的元素均添加进来的话，把相应的判断添加进来，l）
        //类比subset 集合元素的情况

        if(tmp.size()==num.length)
        {
            //下一步停止，0 算作是第一层的情况，n-1代表是下一个元素
            result.add(new ArrayList<>(tmp));
            return;
        }
        for(int i=0;i<num.length;i++)
        {
            //如果没有被访问
            if(!visited[i])
            {
                visited[i] = true;
                tmp.add(num[i]);
                GeneratePermute(num ,visited,tmp,result);
                //弹出最后一个元素
                tmp.remove(tmp.size()-1);
                //访问完成，
                visited[i] = false;
            }
        }
    }

    //递归同样的思路，简洁一些
    public List<List<Integer>> permute(int[] nums)
    {
        Arrays.sort(nums);
        List<List<Integer>> result = new ArrayList<>();
        List<Integer> path =new ArrayList<>();
        if(nums.length==0) return  result;
        dfs(nums, path, result);
        return result;
    }

    private void dfs(int []nums, List<Integer> path, List<List<Integer>>result)
    {
        if(path.size()==nums.length)
        {
            //使用Java重新构建数组
            result.add(new ArrayList<>(path));
            return;
        }
        for(int i=0;i<nums.length;i++)
        {
            //判断是否存在某个元素了，
            int mark = path.indexOf(nums[i]);
            if(mark==-1) {
                path.add(nums[i]);
                dfs(nums,path,result);
                path.remove(path.size()-1);
            }
        }
    }
}
