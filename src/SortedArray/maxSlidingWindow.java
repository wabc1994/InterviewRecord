package SortedArray;

import java.util.Deque;
import java.util.LinkedList;

/**
 * @ClassName maxSlidingWindow
 * @Description TODO
 * @Author coderlau
 * @Date 2019/1/21 11:57 AM
 * @Version 1.0
 **/
public class maxSlidingWindow {


    //

    public int[] maxSlidingWindow(int[] nums, int k) {
        if (nums == null || nums.length == 0) return new int[0];
        int[] res = new int[nums.length - k + 1];
        int idx = 0;
        Deque<Integer> deque = new LinkedList<>();
        for (int i = 0; i < nums.length; i++) {
            if (!deque.isEmpty() && deque.peekFirst() + k <= i) deque.pollFirst();
            while (!deque.isEmpty() && nums[deque.peekLast()] <= nums[i]) deque.pollLast();
            deque.offer(i);
            if (i >= k - 1) res[idx++] = nums[deque.peekFirst()];
        }
        return res;
    }
}
