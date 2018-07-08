public class HouseRobber {
    public int solution_1(int [] nums)
    {
        //时间复杂度为O(N^2)空间复杂度为o(n), 不是动态规划问题（自己的想法）
        if(nums.length==0) return 0;
        if(nums.length==1) return nums[0];
        int len = nums.length;
        int [] dp =new int [len];
        dp[0]= nums[0];
        dp[1] = nums[1];

        //dp[i]表示包含nums[i]后的最大值，其中i-1个元素是不能选取的，只能选取0 到i-2区间断的元素，在这中间
        //
        for(int i=2;i<len;++i)
        {   int max_ = dp[0];
            //表示从dp[0]到dp[i-2]中最大的值，这样就达不到动态规划中的记录的目的。
             for(int j=1;j<i-1;j++)
                if(dp[j]>max_)
                    max_=dp[j];
            dp[i] = max_+nums[i];
        }
        //最大值存在最后两个元素之间，可以利用下面的方法进行优化
        return dp[len-2]>dp[len-1]?dp[len-2]:dp[len-1];
    }

    //解法二是解法一的升级改造版，如果把dp[1]从一开始设置为最大值而不是能够取得的值，就变成了动态规划题
   //正在的动态规划解法，考虑加入与否，
    //处理元素i的时候，可以考虑两种情况，一种是加入，如果加入的话则是dp[i]=nums[i]+dp[i-1] ,不加入的话就是
    //dp[i] = dp[i-1]; 所以 dp[i] = max(dp[i-1],dp[i-2]+nums[i]),这两种情况
    public int solution_2(int [] nums) {
        //时间复杂度为O(N^2)空间复杂度为o(n),
        if (nums.length == 0) return 0;
        if (nums.length == 1) return nums[0];
        int len = nums.length;
        int[] dp = new int[len];
        //在这里dp[i]记录的是最大值
        dp[0] = nums[0];
        dp[1] = Math.max(nums[0],nums[1]);
        int i=2;
        for(;i<len;++i)
        {
            dp[i] = Math.max(dp[i-2]+nums[i],dp[i-1]);
        }
        return dp[len-1];
    }



    public int solution_3(int [] nums)
   {

   //时间复杂度为O(N),空间负载度为O(1);
       int a=0,b=0;
       //a记录偶数， b记录期数，
       for(int i=0;i<nums.length;++i)
       {
           if(i%2==0)
               a = Math.max(a+nums[i], b);
           else
               b = Math.max(a,b+nums[i]);
       }
       return Math.max(a,b);
   }

    public static void main(String[] args) {
     HouseRobber test = new HouseRobber();
     int [] arr ={9,0,0,9};
     System.out.println(test.solution_2(arr));
    }
}
