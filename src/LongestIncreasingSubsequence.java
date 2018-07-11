import java.util.ArrayList;
import java.util.Arrays;

public class LongestIncreasingSubsequence {
    public int solution(int [] nums)
    {
        int n = nums.length;
        if(n==0) return 0;
        if(n==1) return 1;
        int max = 1;
        int [] dp =new int[n];
        Arrays.fill(dp,1);
        dp[0] =1;
        for(int i=1;i<n;++i){
            for(int j=i-1;j>=0;--j)
            {
                if(nums[j]<nums[i])
                {
                    //因为要求一个增长序列，u=
                   // 状态转移方程dp[i]在前面选择一个num[j]<nums[i],总多条件中选择一个·满足题意的max 的j,然后加i
                    dp[i] = Math.max(dp[j]+1,dp[i]);
                }
                //如果一个元素找不到一个比他任何一个比他小的元素，那么以这个元素为nums[i] 为尾巴的增长序列只能为1，自己本身

                }

            max = Math.max(max,dp[i]);

        }
        return max;
    }
    //二分查找的思路
    public int solution_2(int [] nums)
    {
        ArrayList<Integer> lis= new ArrayList<>();
        for(int x:nums)
        {
            int insertPos = lowerBound(lis,0, lis.size(),x);
            if(insertPos >= lis.size()){
                lis.add(x);
            }else {
                lis.set(insertPos,x);
            }
        }
        return lis.size();
    }
    private static  int lowerBound(ArrayList<Integer> A, int first, int last,int target){
        while(first!=last){
            int mid = (last+first) /2;

             if(A.get(mid) < target)
                first = mid+1;
            else
                last = mid;
        }
                return first;
    }
    public int solution_3(int []nums){
    //对上述代码的一个 简化版本
    int[] tails = new int[nums.length];
    int size = 0;
    for (int x : nums) {
        int i = 0, j = size;
        while (i != j) {
            int m = (i + j) / 2;
            if (tails[m] < x)
                i = m + 1;
            else
                j = m;
        }
        tails[i] = x;
        //如果元素的最后一个位置插入，size++规模要加以
        if (i == size) ++size;
    }
    return size;
}
    public static void main(String[] args) {
        LongestIncreasingSubsequence test = new LongestIncreasingSubsequence();
        int [] array = {10,9,2,5,3,7,101,18};
        System.out.println(test.solution(array));
    }
}
