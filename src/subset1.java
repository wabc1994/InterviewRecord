import java.util.ArrayList;
import java.util.List;
import java.lang.Integer;
import java.util.Arrays;
/*  三种方法http://bangbingsyb.blogspot.com/2014/11/leetcode-subsets-i-ii.html
* */
public class subset1 {
    //with no duplicate element，回溯算法Input : 1 2 3
   /* Output :     // this space denotes null element.
            1
            1 2
            1 2 3
            1 3
            2
            2 3
            3
            使用深度优先搜算算法
     */
    public List<List<Integer>> solution_1(int[] nums) {
        List<List<Integer>> list = new ArrayList<>();
        list.add(new ArrayList<>());
        Arrays.sort(nums);
        backtrack(list, new ArrayList<>(), nums, 0);
        return list;
    }

    public void backtrack(List<List<Integer>> list , List<Integer> tempList, int [] nums, int start){
        list.add(new ArrayList<>(tempList));
        for(int i = start; i < nums.length; i++){
            tempList.add(nums[i]);
            backtrack(list, tempList, nums, i + 1);
            /*清空所有的路径中的内容*/
            tempList.remove(tempList.size() - 1);
        }
    }
}
