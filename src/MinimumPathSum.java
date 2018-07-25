public class MinimumPathSum {
    /*
    *Input:
[
  [1,3,1],
  [1,5,1],
  [4,2,1]
]
Output: 7  最短路径之和
Explanation: Because the path 1→3→1→1→1 minimizes the sum.
     */
    //很简单的动态规划题目
    public int solution1(int[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) return 0;
        int m = grid.length;
        int n = grid[0].length;
        int[][] dp = new int[m][n];
        dp[0][0] = grid[0][0];
        for (int i = 0; i < m; ++i) {
            dp[i][0] = dp[i - 1][0] + grid[i][0];
        }
        for (int j = 0; j < n; ++j) {
            dp[0][j] = dp[0][j - 1] + grid[0][j];
        }
        for (int i = 1; i < m; ++i) {
            for (int j = 1; j < n; ++j) {
                dp[i][j] = Math.min(dp[i - 1][j], dp[i][j - 1]) + grid[i][j];
            }
        }

        return dp[m - 1][n - 1];
    }

    //空间进行优化处理，使用一个一维数组进行迭代处理

    public int solution2(int[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) return 0;
        int m = grid.length;
        int n = grid[0].length;
        int[] dp = new int[n];
        dp[0] = grid[0][0];
        //初始化动态规划数组
        for (int i = 1; i < n; ++i) {
            dp[i] = dp[i - 1] + grid[0][i];
        }
        for (int i = 1; i < m; ++i) {
            dp[0] = dp[0] + grid[i][0];
            for (int j = 1; i < n; ++j) {

                //dp[j]代笔dp[i-1][j], dp[j-1]代表dp[i][j-1]
                dp[j] = Math.min(dp[j], dp[j - 1]) + grid[i][j];
            }
        }
        return dp[n - 1];
    }

    public static void main(String[] args) {
        MinimumPathSum test = new MinimumPathSum();

    }
}