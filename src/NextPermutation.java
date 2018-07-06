import java.util.Arrays;

public class NextPermutation {
    /**
     * 下一个全排列的情况，从右到左查找一个
     * http://fisherlei.blogspot.com/2012/12/leetcode-next-permutation.html
     */
    //6-8-7-4-3-2
    // we need to find the position of 6 and the first position bigger than six the first time
    public void solution(int[] nums) {

        int n = nums.length;
        if(n<2) return;
        int backward_1 = n-1;
        while(backward_1>0) {
            if(nums[backward_1]>nums[backward_1-1])
                break;
            else
                --backward_1;
        }

        //1，3，4，5，8 这样的情况的话 backward会止步不前，所以只需要交换后面两个元素即可 1，2 也是
        if(backward_1==n-1)
            swap(nums,n-2,n-1);

        //如果都是逆序的话 8，5，4，3，1的话，这种情况下是也是逆序即可,对所有的元素进行一个基本的逆序即可
        else if(backward_1==0)
            recurive(nums,0,n-1);
        //now n point to 8


        //  接下来都是正常的情况下的转变即可， 3 4 1 2 ， 这种backward_1=4 , backward_2=4 都是同一个数
        int min_1 = backward_1-1;
        int backward_2 = n-1;
        //在8的右边进行查找，如果可以找得到一个比6大的数则证明还有下面一个permutation
        //6 8 5 2 1 这样的情况下一个排列开头的数字与8是重合的8 6 5 2 ，要确保所有的在partition8 的因此backward
        //要添加 1,2,3
        while(backward_2>=backward_1) {
            if(nums[backward_2]<nums[min_1])
                break;
            else
                --backward_2;
        }
        swap(nums, min_1,backward_2);
        recurive(nums, backward_1,n-1);
        }
        private void swap(int [] nums, int i, int j) {
        int temp = nums[i];
        nums[i]=nums[j];
        nums[j]=temp;
    }
    //头尾交换即可，时间复杂度为Olog(m)
    private void recurive(int [] nums,int start ,int last) {
      while(start<last) {
          swap(nums,start,last);
          ++start;
          --last;
      }
    }

    //进行代码简化

    public void solution_2(int []nums)
    {
        // 6 8 7 5 4 3 1  8
        if(nums.length<2) return;
        int n=nums.length, j=n-2;
        while(j>=0 && nums[j]>nums[j+1])
        {
            --j;
        }
        //多种情况同一处理 这个时候j是指在6这里的，从后面一个开始比较，
        if(j<0)
        {
            Arrays.sort(nums, 0,n-1);

        }
        int i=j+1;
        while(i<n && nums[i]>nums[j]) i++;
        --i;
        swap(nums,i,j);
        Arrays.sort(nums,j+1,n-1);

    }
}
