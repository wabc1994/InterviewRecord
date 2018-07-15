import java.util.Arrays;

public class UniquePaths {
    /**
     *
     * @param m 长
     * @param n 宽
     * @return how many path from the 左上角到右下角的位置，只能像下或者向右，动态规划法，
     */
    public int solution_1(int m, int n){
        return unqiue_path_1(n,m);

    }
    //递归方法，总的方法等于右下两种方法叠加起来的总体情况，超时
    private int unqiue_path_1(int row,int column){
        //当只有一行或者一列的时候只有一种情况，一条路径,
        if(row==1 || column==1) return 1;
       //m代表向右走的情况（总的路径数）， n代表向做,
        return unqiue_path_1(row,column-1)+unqiue_path_1(row-1,column) ; //在这里不用加上2
    }
            //采用一个记忆数组，记录每个位置的情况，如果这个dp[i][j]的值>0,证明已经算出来，
    public int solution_2(int m,int n){
       int [][] dp = new int[n][m];
       return unqiue_path_2(0,0,n,m,dp);
    }
    private int unqiue_path_2(int i,int j, int row, int column,int [][]dp){
        if(i==row-1 || j==column-1) return 1;
        if(i>=row || j>=column) return 0;
        if(dp[i][j]>0) return dp[i][j];
        dp[i][j] = unqiue_path_2(i+1,j,row,column,dp)+unqiue_path_2(i,j+1,row,column,dp);
        return dp[i][j];
    }
    //动态规划算法，

    public int solution_3(int m, int n){
        //倒过来求解的
        int [][] dp=new int [n][m];
        //
        for(int i=0;i<m;++i){
            dp[0][i] =1;
        }
        for(int j=0;j<n;j++){
            dp[j][0]=1;
        }
        for(int i=1;i<n;i++){
            for(int j=1;j<m;j++){
                dp[i][j] =dp[i-1][j]+dp[i][j-1];
            }
        }
        return dp[n-1][m-1];

    }

    //空间优化的方法
    public int solution_3_1(int m, int n){
        if(m<1||n<1) return 0;
        int [] dp = new int[m];
        Arrays.fill(dp,1);
        for(int i=1;i<n;++i){
            for(int j=1;j<m;++j){
                dp[j] += dp[j-1];
            }
        }
        return dp[m-1];

    }
    public static void main(String[] args) {
        UniquePaths uniquePaths = new UniquePaths();
        System.out.println(uniquePaths.solution_1(7,1));
        System.out.println(uniquePaths.solution_2(3,2));
    }

}
