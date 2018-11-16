package SortedArray;

/**
 * 一个大小为n+1的数组，元素为1到n之间的数据，假设必有一个元素是重复(出现了多次的情况)，请查找出该元素是哪一个。
 * 其中的情况,返回成功为0；
 *
 将[1,3,4,2,2] 转换为正常的情况

 转换为下面该种情况后的数据情况
[1,2,2,3,4]


 [
 [1,2,3,4,2]
*/

public class FindtheDuplicateNumber {
   //参考链接[http://fisherlei.blogspot.com/2015/10/leetcode-find-duplicate-number-solution.html]


    public int solution_1(int []nums){
       if(nums==null|| nums.length==0){
           return -1;
       }
        int length = nums.length;
       //桶排序的算法来做情况，
        for(int i=0;i<length;i++){

            while(nums[nums[i]-1]!=nums[i]){
                swap(nums[i],nums[nums[i]-1]);
            }


        }
        for(int i=0;i<length;i++){
            if(nums[i]-1!=i)
            {
                return nums[i];
            }
        }
        return -1;

    }
    private void swap(int a, int b){
        int tmp =a;
        a = b;
        b = tmp;
    }

    //采用环的思想来做这道题目即可,


    public int solution_2(int [] nums){
        if(nums==null|| nums.length==0) {return  -1;}
        int slow = nums[0];
        int high = nums[nums[0]];
        //在这里的循环条件还是不一样的情况
        while(slow!=high){
            slow = nums[slow];
            high = nums[nums[high]];
        }
        //不要从第一开始开始的情况就是这样
        slow = 0;
        while(slow!=high){
            slow = nums[slow];
            high = nums[high];
        }
        return slow;
    }
    //二分查找的情况


    //中间的数据为[n/2],统计如果左边

    public int solution_3(int []nums){
        if(nums==null|| nums.length==0) {return  -1;}
        int high = nums.length-1;
        int low =0;
        while(low<high){
            int mid = (low+high)/2;
            int count=0;
            for(int i=0;i<nums.length;i++){
               if(nums[i]<=mid){
                   count++;
               }
            }
            //如果统计的格式多余
            if(count>mid){
                high = mid;

            }
            else {
                low =mid+1;
            }
        }
        return low;
    }
}
