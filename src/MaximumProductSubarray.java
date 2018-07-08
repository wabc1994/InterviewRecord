
public class MaximumProductSubarray {
   /*
   最大连续子序列乘积
    */
   public int solution_1(int [] nums)
   {
       //空间复杂度为o(n),时间复杂度为o(n),保存前面的两种情况下来，
       int [] dp_max =new int [nums.length]; //maximum product ending with nums[i]
       int [] dp_min =new int [nums.length];  // minimum product ending with nums[i]
       dp_max[0] = dp_min[0] =nums[0];
       int result = dp_max[0];
       for(int i=1;i<nums.length;++i)
       {
           //在这个位置考虑要不要加入保留前面的元素，如果num[i],本身比较大的化
           dp_max[i]=dp_min[i]=nums[i];
           //判断是否要加入保留前面的i-1部分，如果不保留那就是i个元素重新自己另起一行，断开
          //还可以进一步进行优化：不用判断num[i]的正负号
           //dp_max[i] =  Math.max(nums[i],Math.max(nums[i]*dp_max[i-1]),nums[i]*dp_min[i-1])
           //dp_min[i] = Math.min(nums[i],Math.min(nums[i]*dp_max))
           if(nums[i]>0)
           {
               //保存极端的两种情况
               dp_max[i] = Math.max(nums[i],nums[i]*dp_max[i-1]);
               dp_min[i] = Math.min(nums[i],nums[i]*dp_min[i-1]);
           }else {
               dp_max[i] = Math.max(nums[i],nums[i]*dp_min[i-1]);
               dp_min[i] = Math.min(nums[i],nums[i]*dp_max[i-1]);
           }
           result = Math.max(dp_max[i], result);
       }
       return result;
   }
   //improve the performance to space complexityO(n), space complexity O(1); reduce the
    //
   public int solution_2(int [] nums)
   {
       //减少时间复杂度即可，只使用四个数保存中间结果即可，
       if(nums.length<1) return 0;
       int result=nums[0];
      int pre_min=nums[0],pre_max=nums[0],n= nums.length;
      int current_max, current_min;
      for(int i=1;i<n;++i) {
          if (nums[i] > 0) {
              //更新操作等关键步骤
              current_max = Math.max(nums[i], nums[i] * pre_max);
              current_min = Math.min(nums[i], nums[i] * pre_min);
          } else {
              //更新操作等关键步骤
              current_max = Math.max(nums[i], nums[i] * pre_min);
              current_min = Math.min(nums[i], nums[i] * pre_max);
          }
          pre_min = current_min;
          pre_max = current_max;
          result = Math.max(current_max, result);
      }
      return result;
   }

   //只要保持一空间复杂度
   public int solution_3(int[] nums)
   {
       if(nums.length<1) return 0;
       int result=nums[0];
       int pre_min=nums[0],pre_max=nums[0],n= nums.length;
       for(int i=1;i<n;++i) {
           int temp = pre_max;
           pre_max = Math.max(nums[i], Math.max(nums[i] * pre_max,nums[i]*pre_min));
           pre_min = Math.min(nums[i], Math.min(nums[i] * pre_min,nums[i]*temp));
           result = Math.max(pre_max, result);
       }
       return result;
   }

   public static void main(String[] args) {
        MaximumProductSubarray test = new MaximumProductSubarray();
        int [] arr = {2,3,-2,-5};
        //如果不设置dp_min的话，单独是dp[2] = 3,的话，按照加分那道题目的解法，max =3,是会遗漏的
        //所以得保存下最小的情况下来
        //dp_max[1]= 3 dp_min[1] = -6, 接下来判断，判断nums[2]=4 的正负情况，如果
        System.out.println(test.solution_1(arr));
       System.out.println(test.solution_2(arr));
    }
}
