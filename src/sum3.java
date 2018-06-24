import java.util.*;

public class sum3 {


    public List<List<Integer>> threeSum(int[] nums) {
        if (nums.length < 3) return null;
        Arrays.sort(nums);
        List<List<Integer>> res = new LinkedList<>();
        final int target = 0;
        //处理三个位置的相同元素， i处背后的元素， j背后的元素， k和k之前的元素，相比较

        for (int i = 0; i < nums.length - 2; ++i) {
            //相同的数字处理一次即可， i j , k
            // 其中 i固定，j 初始化为i+1, k= nums.length-1(最后一个元素)，
            // 分解为两两相加的情况，


            if (i > 0 && nums[i] == nums[i - 1]) continue;


            int j = i + 1;
            int k = nums.length - 1;
            while (j < k) {
                if (nums[i] + nums[j] + nums[k] < target) {
                    ++j;
                    while (nums[j] == nums[j - 1] && j < k) ++j;
                } else if (nums[i] + nums[j] + nums[k] > target) {
                    --k;
                    while (nums[k] == nums[k + 1] && j < k) --k;
                } else {
                    res.add(Arrays.asList(nums[i], nums[j], nums[k]));
                    //
                    ++j;
                    --k;
                    //处理相邻的相同元素，只处理一次，
                    while (nums[j] == nums[j - 1] && j < k) ++j;
                    while (nums[k] == nums[k + 1] && k > j) --k;
                }
            }
        }
        return res;
    }


    public List<List<Integer>> soltion_2(int[] nums) {
        //二分法查找的情况，先固定一个值
        Arrays.sort(nums);
        if (nums.length < 3) return null;
        List<List<Integer>> res = new ArrayList<>();
        for (int i = 0; i < nums.length - 2; i++) {
            //两种情况，第一是开头的直接做，第二是非开头的重复（前面有些做过的元素情况）
            if (i == 0 || (i > 0 && nums[i] != nums[i - 1])) {
                int j = i + 1;
                int k = nums.length - 1;
                int sum = 0 - nums[i];
                while (j < k) {
                    if (nums[j] + nums[k] < sum) {
                        j++;
                        while (nums[j] == nums[j - 1] && j < k)
                            ++j;
                    } else if (nums[j] + nums[k] > sum) {
                        k--;
                        while (nums[k] == nums[k + 1]) --k;
                    } else {
                        res.add(Arrays.asList(nums[i], nums[j], nums[k]));
                        --k;
                        ++j;
                        while (j < k && nums[j] == nums[j + 1]) ++j;
                        while (j < k && nums[k] == nums[k - 1]) --k;
                    }
                }

            }
        }
        return res;

    }



}