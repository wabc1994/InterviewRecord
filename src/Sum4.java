import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Sum4 {

    public List<List<Integer>> solution_1(int [] nums, int target)
    {
        //与sum3一样采用夹逼准则，前后2个指针 for循环的次数为n-2，注意处理开头处的相同字符即可

        if(nums.length<4) return null;
        List<List<Integer>> result = new ArrayList<>();
        //Sorting the array costs O(nlogn),左右夹逼一趟的时间复杂度为n, 两个for循环n^2 ,所以总的时间复杂度为你你n^3;
        Arrays.sort(nums);
        for(int first=0;first<nums.length-3;first++)
        {
            if(first>0 && nums[first]==nums[first-1]) continue;
            for(int second=first+1;second<nums.length-2;second++)
            {
                if(second>first+1 && nums[second]==nums[second-1]) continue;
                int third= second+1;
                int last = nums.length-1;
                while(last>third)
                {
                    int sums = nums[first] + nums[second] + nums[third]+ nums[last];
                    if(sums<target) {
                        ++third;
                        while(third<last && nums[third-1]==nums[third]) ++third;
                        }
                        else if(sums>target) {
                        --last;
                        while(last>third && nums[last+1]==nums[last])
                        --last;
                    }
                    else {
                        result.add(Arrays.asList(nums[first],nums[second],nums[third],nums[last]));
                        --last;
                        ++third;
                        while(third<last && nums[third-1]==nums[third]) ++third;
                        while(last>third && nums[last+1]==nums[last]) --last; }
                }
            }
        }
        return result;
    }




}
