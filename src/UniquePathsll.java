public class UniquePathsll {
    /**
     *中间加入一个障碍，
     * @return, return path
     */
    public int solution_1(int[][] obstacleGrid){
        if(obstacleGrid==null||obstacleGrid.length==0)

          { return 0;}

        int m = obstacleGrid.length;
        int n = obstacleGrid[0].length;
        if(obstacleGrid[0][0]==1||obstacleGrid[m-1][n-1]==1)
            {return 0;}
        int[][] dp = new int[m][n];
        dp[0][0]=1;

        //left column
        for(int i=1; i<m; i++){
            if(obstacleGrid[i][0]==1){
                dp[i][0] = 0;
            }else{
                dp[i][0] = dp[i-1][0];
            }
        }

        //top row
        for(int i=1; i<n; i++){
            if(obstacleGrid[0][i]==1){
                dp[0][i] = 0;
            }else{
                dp[0][i] = dp[0][i-1];
            }
        }
        //fill up cells inside
        for(int i=1; i<m; i++){
            for(int j=1; j<n; j++){
                if(obstacleGrid[i][j]==1){
                    dp[i][j]=0;
                }else{
                    dp[i][j]=dp[i-1][j]+dp[i][j-1];
                }

            }
        }

        return dp[m-1][n-1];
    }
    //空间复杂度没有优化的
    public int solution_2(int [][]obstracleGrid){
        if(obstracleGrid.length==0||obstracleGrid[0].length==0||obstracleGrid[0][0]==1)
        { return 0;}
        int [] dp = new int[obstracleGrid[0].length];
        //第一行的路径不一定就是1，如果是obstracleGrid[0][j] =1||obstracleGrid[i][0] =1
        dp[0] = 1;
        for(int i=1;i<obstracleGrid[0].length;++i){
            //等于上面的那个状态
            dp[i] = obstracleGrid[0][i]==1?0:dp[i-1];
        }
        /* 开始处理下面的元素 */
        for(int i=1;i<obstracleGrid.length;++i){
            //等于上一次的状态
            dp[0] =obstracleGrid[i][0]==1?0:dp[0];
            for(int j=1;j<obstracleGrid[0].length;++j){
                       dp[j] = obstracleGrid[i][j]==1?0:dp[j-1]+dp[j];
            }
        }
        return dp[obstracleGrid[0].length-1];
    }
}
