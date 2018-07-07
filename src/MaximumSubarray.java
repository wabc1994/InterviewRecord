public class MaximumSubarray {
    /*
    在一个数组中找到一个连续的数组子序列，让子序列的和达到最大值，
    比如[-2,1,-3,4,-1,2,1,-5,4] 在上述数组中，最大的子序列和为[4,-1,2,1]，返回值为6这中情况下
     */
    //最大连续子序列和,
    //自己出现的错误的地方是没有在保持一个1连续的局面

    public int maxSubArray(int [] nums)
    {
        int len = nums.length;
        int [] dp =new int [len];
        dp[0] = nums[0];
        int max = nums[0];
     //其中dp[i] 表示 the maxinum subarray ending with A[i];
        for(int i=1;i<len;++i)
        {
            //表示处理第i个元素，其中value表示
            //如果dp[i-1]代表前面的数组结果，
            //自己错误的解法为 dp[i] = max(dp[i-1]+nums[i], dp[i-1]这样求出来的结果是不连续
            //正确的解法，为了保证连续性， 应该是把nums[i]作为必须项，就是把这nums[i]这项必加进来看看
            //dp[i]逐步寻找最大值,max表示num[i-1]之前所能保存的最大值，dp[i-1]代表前面的数据量都是0不要

            dp[i] = nums[i] + (dp[i-1]>0 ? dp[i-1]:0);
            max = Math.max(max,dp[i]);
        }
        return max ;
    }
    public int solution_2(int)

    public static void main(String[] args) {
        MaximumSubarray test= new MaximumSubarray();
        int [] test_array = {-2,1,-3,4,-1,2,1,-5,4};
        System.out.println(test.maxSubArray(test_array));

    }
}
