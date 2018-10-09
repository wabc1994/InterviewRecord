package SortedArray;

import java.util.HashMap;
import java.util.Map;

public class MajorityElement {
    //暴力解决
    public int solution_1(int[] nums) {
        int maxcount = 1;
        int mark = nums[0];
        for(int i = 0; i < nums.length; i++) {
            int count = 0;
            for (int j = 0; j < nums.length; j++) {
                if (nums[j] == nums[i]) {
                    ++count;
                }
            }
            if (count > maxcount) {
                maxcount = count;
                mark = nums[i];
            }
            if (maxcount > (nums.length << 1)) {
                return mark;
            }

        }
        return mark;
    }
    //0(n)  时间复杂度和空间复杂度均为一样的情况
    // hashmap
    public int solution_2(int []nums)
    {
        if(nums.length==0) {return -1;}
        Map<Integer, Integer> result =new HashMap<>();
        for(int i=0;i< nums.length;i++){
            if(!result.containsKey(nums[i]))
            {
                result.put(nums[i],1);

            }
            //如何进行更新操作
            else {
                int  count =result.get(nums[i]);
                ++count;
                result.put(nums[i],count);
                if(count>nums.length/2)
                {
                    return nums[i];
                }

            }


        }
        return 0;
    }
    //接下来就是moore 算法,又三种情况，把第一个元素当成是出现次数最多的元素情况
       //元素个数大于n/2的元素又什么特点情况

    public int solution_3(int [] nums){
        if(nums.length==1) {return nums[0];}
        int max_index=0,count=1;
         // If count is 0, set the current candidate to majority and set count to 1
        for(int i=1;i<nums.length;i++){
            //统计计数，如果跟前面的东西相同，直接增加即可
            if(nums[max_index]==nums[i]){
                ++count;

            }
            ////increase counter if majority is the element at i
            else{
               --count;

            }
            // 减少到一定程度直接重新换新的选手即可
            if(count==0){
                count=1;
                max_index = i;
            }

        }
        return nums[max_index];
    }
}





