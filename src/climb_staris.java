import java.util.ArrayList;
import java.util.List;


/*
* https://leetcode.com/problems/climbing-stairs/solution/*/
public class climb_staris {
    /*

采用数组（或者HashMap类的）保存中间一个结果，每一次先判断数组中是否已经出现了包含了
结果，如果包括结果了，直接返回结果表示，如果没有包括，继续向下做下去
空间复杂度为O(N), 时间复杂度也为O(n);
 */
    public int climbstairs(int n) {
        int[] memo = new int[n + 1];
        return climb(0, n, memo);

    }

    public int climb(int i, int n, int memo[]) {
        if (i > n)
            return 0;
        if (i == n)
            return 1;
        if (memo[i] > 0) {
            return memo[i];
        }
        memo[i] = climb(i + 1, n, memo) + climb(i + 2, n, memo);
        return memo[i];
    }

    /*
     * 动态规划算法基础,时间复杂度和空间复杂度同上面一样，数组记录中间结果，但我们最终返回的不需要中间结果，
     * 不需要一个这么长的数组，仅需要最后一个结果即可
     *
     * */
    public int soultion2(int n) {
        int[] dp = new int[n + 1];
        if (n < 2)
            return 1;
        dp[0] = 1;
        dp[1] = 1;
        dp[2] = 2;
        for (int i = 3; i <= n; i++) {
            dp[i] = dp[i - 2] + dp[i - 1];

        }
        return dp[n];

    }

    public int solution_3(int n) {

        if (n < 2)
            return 1;
        int first = 1;
        int second = 2;

        for(int i = 3; i<=n;i++)
        {
           int third = first + second ;
            first = second ;
            second = third;
        }
        return second;

    }

}


