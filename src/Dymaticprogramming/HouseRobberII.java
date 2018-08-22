package Dymaticprogramming;


import java.util.Set;
import java.util.TreeSet;

public class HouseRobberII {
    /**
     *
     * @param nums the house money  第一个房子和最后一个房子相连成一个圆圈，不能选择相连的两个房子偷东西
     * @return
     */
    //my solution
    public  static  int solution(int[] nums){
        if(nums==null || nums.length==0 || nums.length==2)
            return 0;
        //自己的解法，不是很周到的情况，考虑有点不足
        Set<Integer> element = new TreeSet<>();
        int []dp =new int[nums.length];
        if(nums.length==1) return nums[0];
        //接下来就是元素个数超过三个以上的情况
        dp[0] =nums[0];
        dp[1] = nums[1];
        int result= Math.max(dp[0],dp[1]);
        element.add(dp[0]>dp[1]?0:1);
        //处理到倒数最后一个结果之前，这样
        int i=2;
        for(;i<nums.length;++i){
            if(dp[i-2]+nums[i]>dp[i-1]) {
                dp[i] = dp[i - 2] + nums[i];
                //包含这个元素情况
                element.add(i);
            }
            else
                dp[i] = dp[i-1];
            dp[i] = Math.max(dp[i-2] +nums[i],dp[i-1]);
            if(dp[i]>result)
                result = dp[i];
        }
        //
        //问题的关注点，如何判断最后一个元素是否和第一元素是否同时入选
        if(element.contains(0))
            return dp[i-1];
        else
            return dp[i];
    }


    //right solution
    public  int solution_2(int []nums){
        if(nums.length==1) return nums[0];
        //两个元素的情况
        if(nums.length==2) return Math.max(nums[0],nums[1]);
        //三个元素的情况
        return Math.max(robber(nums, 0,nums.length-1),robber(nums, 1,nums.length));

    }
    private int  robber(int[] nums, int begin, int end){
        //边界条件的处理情况
        if (nums == null || begin >= end) return 0;
        //只有三个元素的情况下
        if(end-begin==1) return Math.max(nums[begin],nums[end]);
        //四个元素以上的情况
        int even,odd;
        if(begin%2==0) {
            even = nums[begin];
            odd = Math.max(nums[begin+1],nums[begin]);
        }
        else
        {
            odd = nums[begin];
            even = Math.max(nums[begin+1],nums[begin]);
        }
        for(int i=begin+2;i<end;++i) {
            if (i % 2 == 0) {
                even = Math.max(odd,even+nums[i]);
            }
            else
                odd = Math.max(even,odd+nums[i]);
        }
        return Math.max(even,odd);

    }
    public static void main(String[] args) {
        int [] array={1,2,3,1,1,10};
        System.out.println(HouseRobberII.solution(array));
       // System.out.println();

    }

}
