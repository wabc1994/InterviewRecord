
import java.util.Arrays;

//与上一题相区别的是，由于返回结果是一个值而已，所以不用考虑相同元素的干扰
public class Sum3Closest {

    public int ThreeSumClosest(int[] nums, int target) {
        Arrays.sort(nums);
        //不用考虑相同元素
        int result = nums[0] + nums[nums.length - 1] + nums[1];

        for(int i = 0; i < nums.length - 2; i++) {
            int start = i + 1, end = nums.length - 1;
            while (start < end) {
                int sum = nums[i] + nums[start] + nums[end];
                //需要交换的情况，碰到更小的元素，更加接近与target
                ///接下来查找更加接近与target的目标，查找方向
                //向左走，小于 target
                if (sum > target)
                    --end;
                    //否则大于等于， target
                else if (sum < target)
                    ++start;
                else
                    return target;
                if (Math.abs(sum - target) < Math.abs(result - target))
                    result = sum;
                    //碰到一样值的可以直接返回即可
            }
        }
        return result;
    }

}
