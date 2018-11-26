package SortedArray;


//在一个有序数组查找给定元素的第一个出现的下标位置，和最后出现的下标位置情


/*
nums = [5,7,7,8,8,10], target = 8
        Output: [3,4]
        Example 2:

        Input: nums = [5,7,7,8,8,10], target = 6
        Output: [-1,-1]
*/


import java.util.Arrays;

public class FindFirstandLastPositionofElementinSortedArray {

    // 时间复杂度为Olog(n), 不能采用遍历的方式，直接遍历的话是O(n), 时间复杂度为O(n);


    //充分利用有序数组的形式，二分查找等性质可以搞一下的情况

    public int[]  searchRange(int [] nums, int target){
         int [] result = new int[2];
         int  len =  nums.length;
         Arrays.fill(result,-1);
         int low = 0, high =  len-1;
         while(low<high){
            int mid = (low+high)/2;
            if(nums[mid]>target){
                high =  mid-1;
            }
            else if(nums[mid]<target){
                low =mid+1;
            }
            //接下来的情况是关键的要素情况,当元素情况相等的时候，如果做 nums[mid]== target , 则向两边都是可以有前进的方向的情况
            else
            {
                //两边的方向其实都是可以走的情况下，如果走呢


                    int left = mid-1;
                    int right= mid + 1;
                    while(left>=low  && nums[left]== target)
                    {
                        --left;
                    }
                    result[0]= left+1;
                    while(right<=high && nums[right]==target){
                        ++right;
                    }
                    result[1] = right -1;
                    return result;
                }


            }

        //如果不查找成功的情况就是下面这种，

        //如果查找不成功的话
        return result;

    }


    //将最后一个else 代码中的代码体抽象出来即可该情况

    public int[] solution_2(int [] num ,int target){
        if(num==null ||  num.length==0){
            return null;
        }
        int result[] = new int[2];
        Arrays.fill(num, -1);
        int low =0;
        int high = num.length-1;
        while(low<=high){
            int mid = (low+high)/2;


            //基本

            if(num[mid]>target){
                high = mid-1;
            }
            else if(num[mid]<target) {
                low = mid + 1;
            }
            else {
                //查找成功的情况
                 int left =  helper_left(num, low, mid,target);
                 int right= helper_right(num, mid, high, target);
                 result[0] = left;
                 result[1] =  right;
                 return result;
            }
        }
        return result;

    }



    private int helper_left(int [] num, int low, int mid,int target){
         int mark = mid;
         while(mark>=low &&  num[mark]==target){
             --mark;
         }
         return mark+1;
    }


    private int helper_right(int [] num, int mid, int high, int target)
    {
        int mark = mid;
        while(mark<=high && num[mark]==target)
        {
            mark++;
        }
        return mark-1;
    }


    //左边的情况， 可能比较难以理解的就是， 将两种方式结合起来的情况，nums[]

    public int[] solution_3(int [] num ,int target) {
        if (num == null || num.length == 0) {
            return null;
        }
        int result[] = new int[2];
        Arrays.fill(num, -1);
        int i = 0, n = num.length, j = n - 1;
        while (i < j) {
            int mid = (i + j) / 2;
            if (num[mid] < target) {
                i = mid + 1;
            }
            //分开三种情况来讨论
            /*
            *            else if(nums[mid]>target)
            *                  j = mid - 1
            *            else
            *               左后是两则相等 nums[mid]==  target,
            *               那么两边的 first index of target 肯定是在 j = mid 的左边元素
            *
            *
            *           //将上述两种情况综合起来不就是两种
            *
            * */

            else {
                j = mid;
            }
        }
        if (num[i] == target) {
            result[0] = i;
        }
        //如果左边的第一元素都找不到的情况下，那么两边都是没找到的情况下
        else {
            return result;
        }
        j = n - 1;
        while (i < j) {

           //考虑只有两个数的情况，mid = 0 , i = 0; 不符合题意情况
            int mid = (i + j) / 2 + 1;

            //同样是三种情况


            if (num[mid] > target) {
                    j = mid - 1;
            } else {
                i = mid;
            }
        }
        //找到最后一个元素的情况下
        result[1] = j;

        return result;
    }

}
