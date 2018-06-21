public class first_missing_positive {
    public int solution(int[] nums)
    {
        int n = nums.length;
        for(int i=0;i<n;i++)
        {
            while(nums[i]!=i+1)
            {
                if(nums[i]>n||nums[i]<=0)
                    break;
                if(nums[i]==nums[nums[i]-1])
                    break;
                badSwap(nums[i],nums[nums[i]-1]);

            }
        }
        for(int i=0;i<n;i++)
        {
            if(nums[i]!=i+1)
                return i+1;

        }
        return n+1;
    }
    public void badSwap(int var1, int var2)
    {
        int temp = var1;
        var1 = var2;
        var2 = temp;
    }
}
