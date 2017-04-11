/**
 * Created by liuxiongcheng on 2017/4/10.
 */
public  class SubSequence {
    static int longestCommonSubsequence(String A,String B)
    {
        if(A.length()==0||A==null||B==null||B.length()==0)
            return 0;
        int m=A.length();
        int n=B.length();
        int dp[][]=new int[m+1][n+1];
        for(int i=0;i<=m;i++)
        {
           for(int j=0;j<=n;j++)
           {
               if(i==0||j==0)
                   dp[i][j]=0;   //先把两行作为0;
               else if(A.charAt(i-1)==B.charAt(j-1))
               {
                   dp[i][j]=dp[i-1][j-1]+1;
               }
               else

                   dp[i][j]=max(dp[i-1][j],dp[i][j-1]);
           }
       }
       //打印函数
        int index=dp[m][n]+1;
        char[] lcs=new char[index+1];
        lcs[index]='\0';
        int i=m,j=n;
        while(i>0&&j>0)
        {
            if(A.charAt(i-1)==B.charAt(j-1))
            {
                lcs[index-1]=A.charAt(i-1);
                i--;
                j--;
                index--;
            }
            else if(dp[i-1][j]>dp[i][j-1])
                i--;
            else
                j--;
        }
        System.out.println(lcs);
       return dp[m][n];
    }
 //   v0	           1	2	3	4	5	6	7
           //       0	M	Z	J	A	W	X	U
     //    0	Ø	0	0	0	0	0	0	0	0
    //        1	X	0	0	0	0	0	0	1	1
    //        2	M	0	1	1	1	1	1	1	1
   //         3	J	0	1	1	2	2	2	2	2
   //         4	Y	0	1	1	2	2	2	2	2
   //         5	A	0	1	1	2	3	3	3	3
    //        6	U	0	1	1	2	3	3	3	4
   //         7	Z	0	1	2	2	3	3	3	4
    static  int max(int a,int b)
    {
        return a>b?a:b;
    }

    public static void main(String[] args) {
        System.out.println("sub");
        System.out.println(longestCommonSubsequence("abcdf","adf"));
    }
}
