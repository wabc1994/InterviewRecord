public class ProductofArrayExceptSelf {
    /**
     * 题目链接
     * https://leetcode.com/problems/product-of-array-except-self/discuss/65622/Simple-Java-solution-in-O(n)-without-extra-space
     *
     * solution1:[a1,a2,a3,a4] 得到的最终结果是[ a2* a3 * a4,  a1 *a3 *a4, a1* a2 *a4, a1 * a2 * a3],
     * 转换成两种情况分解成两个数组的情况，left = [   1,       a1,   a1 * a2, a1 * a2 * a3],
     *                                              right =[ a4*a3*a2,  a4*a3,     a4,        1  ]
     */
    //空间复杂度为o(n), 遍历两次
    static public int[] solution_1(int [] nums){
        if(nums.length == 0 ||nums.length == 1) return nums;
        int n = nums.length;
        int [] result = new int[n];
        int [] left = new int [n];
        int [] rigth = new int [n];
        left[0] = 1;
        rigth[n-1] = 1;

        for(int i=1;i<n;++i){
            left[i] = left[i-1] * nums[i-1];
        }

        for(int j=n-2;j>=0;--j){
            rigth[j] = nums[j+1] * rigth[j+1];
        }
        for(int i=0;i<n;i++){
            result[i] = left[i]*rigth[i];
        }
        return result;
    }
    //空间复杂度进行优化，[1,a1,a1*a2,a1*a2*a3],第一遍的结果
    //                  要chen
    public static int [] solution_2(int []nums){
        if(nums.length == 0 ||nums.length == 1) return nums;
        int n = nums.length;
        //空间负责为0(1),
        int [] left = new int [n];
        left[0] = 1;
        for(int i=1;i<n;++i){
            left[i] = left[i-1] * nums[i-1];
        }
        int right =1;
        for(int i=n-1;i>=0;--i){
            left[i] *= right;
            right *= nums[i];
        }
        return left;

    }
    public static void main(String[] args) {
        int[] nums = new int[]{1, 2, 3, 4};
        int[] reult = ProductofArrayExceptSelf.solution_1(nums);
        for (int i = 0; i < nums.length; ++i) System.out.println(reult[i]);
    }
}
