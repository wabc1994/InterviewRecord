public class SearchinRotatedSortedArray {
    /**
     * 将一个有序数组按照一定的逆置即可，如果 []
     *
     */public int search(int[] nums, int target) {
        if(nums.length==0||nums==null) return -1;
        int n = nums.length;
        int l= 0;
        int r = n-1;
        //在这里的话二分法 l <=r 要注意这里的坑
        while(l<=r){
            int mid = (l+r)/2;
            //第一种情况找到了该元素
            if(nums[mid]==target)
                return mid;
            //第二种情况
            else if(nums[mid]<nums[r]){
                //在最右边满足有序列的情况下查找
                if(target>nums[mid] && target<=nums[r])
                    //在左边满足题意的情况下查找
                    l =mid+1;
                else
                    r = mid-1;
            }
            else
            {
                //在右边这部分满足递增序列部分找找
                if(target>=nums[l] && target<nums[mid])
                    r = mid-1;
                //这样得到的元素也不是递增的情况
                else
                    r = mid+1;

            }
        }
        return -1;
     }

   //递归版本情况，
    public int solutiuon_res(int[] nums, int target) {
        if (nums.length == 0 || nums == null) return -1;

        int n = nums.length;

        return find(nums, target, 0, n - 1);

     }
        public int find(int []nums,int target, int left,int right){

        if(left>right)return  -1;
        int mid =( right+left)/2;
        if(nums[mid]==target)
            return mid;
        if(nums[mid]<nums[right]) {
            if (nums[mid] < target && nums[right] >= target)
                find(nums,target,mid+1, right );
            else
                find(nums,target,left,mid-1);
        }
        else
        {
            if(nums[left]<=target && target<nums[mid])
                find(nums,target, left, mid-1);
            else
                find(nums, target, mid+1, right);
        }
        return -1;
        }



}
