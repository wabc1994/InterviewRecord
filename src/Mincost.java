/**
 * Created by liuxiongcheng on 2017/4/11.
 */
public class Mincost {
    public static int min(int x,int y,int z)
    {
        if(x<y)
            return (x<z)?x:z;
        else
            return(y<z)?y:z;
    }
    final int R=3;
   final int C=3;
// 递归的方法
    static int MinConst(int cost[][],int m,int n)
    {
//该点有三中情况，
             if(m<0||n<0)
                 return 0;
             else if(m==0||n==0)
             {

                 System.out.println(cost[m][n]);
                 return cost[m][n];
             }
             else

                 return cost[m][n]+min(MinConst(cost,m-1,n-1),MinConst(cost,m,n-1),MinConst(cost,m-1,n));
    }
    static int MinConstdp(int cost[][],int m,int n)
    {
        
    }

    public static void main(String[] args) {
        int [][]cost={{1,2,3},{4,8,2},{1,5,3}};
        System.out.println(MinConst(cost,2,2));
    }
}
