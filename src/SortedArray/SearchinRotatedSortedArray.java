package SortedArray;

/**
 * 问题的关键查找有序数组，可以包含相同元素的情况，所以比较
 */
public class SearchinRotatedSortedArray {
    public boolean search(int[] nums, int target) {
        if(nums==null||nums.length==0) {return false;}
        int n = nums.length;
        int l=0,r = n-1;

        while(r>=l)
        {
            int mid = (l+r)/2;

            if(nums[mid]==target)
            {return false;}

            if(nums[mid] < nums[r])
            {
                if(nums[mid]<target && nums[r]>=target)
                {
                    l = mid+1;
                }
                else
                { r = mid-1;}
            }
            else if(nums[mid]>nums[r])
            {
                if(nums[l]<= target && nums[mid]>target )
                {r = mid-1;}
                else
                { l = mid+1;}
            }

            //增加了一种判断的条件情况， 有可能相同的情况出现
            else {
                r--;
            }
        }
        return false;
    }

    }
}
