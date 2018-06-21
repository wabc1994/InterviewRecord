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

    //解析
    //一共有三种做法，一种是深度优先搜索，遍历每种情况递归，先根遍历然后向下遍历（联想树的先根遍历）
    /* 第一种是逐步添加处理，处理每个元素，分解问题f(n)=f(n-1)+num[n-1],  所以
    逐步处理n 个元素的情况，想得到一个一空集合的情况，F(0)= 空集合， F(1)= 空集合+1 ， 空集合添加1的情况共有一种情况{1},f(1) = f(0)+f(1),其中f(1)={1}
    f(2) = f(1) +2 , f(1)={[],[1]} 添加2的情况得到新的元素{[2],[1,2]} 所以f(2) = f(1)+从2得到的新元素，f(2) = {[],[1],[2],[1,2]}
    for(i=0;i<f(n-1)中的每种类树；i++)

   图片解释情况 https://www.google.com/url?sa=i&rct=j&q=&esrc=s&source=images&cd=&cad=rja&uact=8&ved=2ahUKEwi3ns3i4OTbAhXJbbwKHfiqA3oQjRx6BAgBEAU&url=http%3A%2F%2Fmassivealgorithms.blogspot.com%2F2014%2F06%2Fleetcode-subsets-java.html&psig=AOvVaw2RARmgmqivaoPWD4HYJVMa&ust=1529670448648009
    */

    //第三是位运算情况（二进制的情况）
    //http://bangbingsyb.blogspot.com/2014/11/leetcode-subsets-i-ii.html 详细的情况


    //第一中解法，
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
