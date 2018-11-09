package SortedArray;

public class SearchinRotatedSortedArrayII {
    public int search(int[] nums, int target) {
        if(nums==null||nums.length==0) {return -1;}
        int n = nums.length;
        int l=0,r = n-1;

        while(r>=l)
        {
            int mid = (l+r)/2;

            if(nums[mid]==target)
            {return mid;}

            if(nums[mid] < nums[r])
            {
                if(nums[mid]<target && nums[r]>=target)
                {
                    l = mid+1;
                }
                else
                { r = mid-1;}
            }
            else
            {
                if(nums[l]<= target && nums[mid]>target )
                {r = mid-1;}
                else
                { l = mid+1;}
            }
        }
        return -1;
    }
}
