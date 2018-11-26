package SortedArray;

import java.util.Arrays;

public class ReversePair {
    public  int solution(int []num) {
        if (num == null || num.length == 0) {
            return 0;
        }
        return reverse(num, 0, num.length - 1);
    }
    private  static int reverse(int [] nums, int left, int right){
     if(left>=right) {return 0;}
    int mid = (left+right)/2;
    int leftcount = reverse(nums, left, mid);
    int rightcout =  reverse(nums, mid+1,right);
    int count =  merger(nums, left, mid, mid+1,right);
        return leftcount + rightcout + count;
}

    private static  int merger(int [] nums, int lstart, int lend ,int rstart, int rend){
        int mark_start_1 = lstart;
        int mark_start_2  =rstart;
        int [] tmp = new int[nums.length];
        int r=lstart;
        int count =0;

        while(lstart<=lend && rstart<=rend){
            if(nums[lstart] > nums[rstart]){
                tmp[r++] = nums[rstart++];
                count += mark_start_2-lstart;
            }
            else{
                tmp[r++] = nums[lstart++];
            }
        }

        while(lstart<=lend){
            tmp[r++]=nums[lstart++];

        }

        while(rstart<=rend){
            tmp[r++]=nums[rstart++];
        }

        for(int i=mark_start_1;i<=rend;i++){
            nums[i] = tmp[i];
        }
        return count;
    }




    public  int solution_2(int []nums){
        return mergeSort(nums, 0, nums.length-1);
    }
    private int mergeSort(int[] nums, int s, int e){
        if(s>=e) {return 0;}
        int mid = s + (e-s)/2;
        //int mid = (s+e)/2 也可以
        int cnt = mergeSort(nums, s, mid) + mergeSort(nums, mid+1, e);
        for(int i = s, j = mid+1; i<=mid; i++){
            //为何要转化为2.0的形式，主要是为了以防止溢出
            while(j<=e && nums[i]/2.0 > nums[j]) {j++;}
            cnt += j-(mid+1);
        }
        Arrays.sort(nums, s, e+1);
        return cnt;
    }
}
