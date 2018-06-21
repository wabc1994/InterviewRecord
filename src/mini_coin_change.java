import java.util.List;
import java.util.ArrayList;
public class mini_coin_change {

    //解析
    //一共有三种做法，一种是深度优先搜索，遍历每种情况递归，先根遍历然后向下遍历（联想树的先根遍历）
    /* 第一种是逐步添加处理，处理每个元素，分解问题f(n)=f(n-1)+num[n-1],  所以
    逐步处理n 个元素的情况，想得到一个一空集合的情况，F(0)= 空集合， F(1)= 空集合+1 ， 空集合添加1的情况共有一种情况{1},f(1) = f(0)+f(1),其中f(1)={1}
    f(2) = f(1) +2 , f(1)={[],[1]} 添加2的情况得到新的元素{[2],[1,2]} 所以f(2) = f(1)+从2得到的新元素，f(2) = {[],[1],[2],[1,2]}
    for(i=0;i<f(n-1)中的每种类树；i++)
    */

    //第三是位运算情况（二进制的情况）
    //http://bangbingsyb.blogspot.com/2014/11/leetcode-subsets-i-ii.html 详细的情况


    //第一中解法，
    List<List<Integer>> res=new ArrayList<List<Integer>>();
    public List<List<Integer>> subsets(int[] nums)
    {
        if(nums==null || nums.length<=0) return null;
        res.add(new ArrayList<Integer>());
        List<Integer> one=new ArrayList<>();
        getFullPute(nums,one,0);
        return res;
    }

    private void getFullPute(int[] nums,List<Integer> one, int i)
    {
        for(;i<nums.length;i++)
        {
            //形成一个子小集
            one.add(nums[i]);
            //子小集加入最终的结果中
            res.add(one);
            //进行下一次递归，到最后一个元素
            getFullPute(nums, one, i+1);
            //逐步弹出最后一个元素，然后在第一到最后一个元素
            one.remove(one.size()-1);
        }

    }

}
