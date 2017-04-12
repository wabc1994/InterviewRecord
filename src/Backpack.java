/**
 * Created by liuxiongcheng on 2017/4/12.
 */
/*背包问题2
问题描述
给出n个物品的体积weight[i]和其价值Value[i]，将他们装入一个大小为W的背包，最多能装入的总价值有多大？
考虑到价值问题，状态不发生变化，只是对于状态我们所记录的内容方式变化，我们现在记录的是其价值，而不是其放置的物品的大小。*/
public class Backpack {
    static int max(int a, int b) {
        return (a > b) ? a : b;
    }/*
  递归方法解决*/

    static int knapSack(int W, int weight[], int value[], int n) {
        if (n == 0 || W == 0)
            return 0;
        else if (weight[n - 1] > W)
            return knapSack(W, weight, value, n - 1);
        else
            return max(value[n - 1] + knapSack(W - weight[n - 1], weight, value, n - 1), knapSack(W, weight, value, n - 1));
    }

    /*动态programming 还是使用数组来解决，只不过数组记录的是箱子里面物品的价值*/
    static int knapSack2(int W, int wt[], int val[], int n) {
        int i, w;
        int k[][] = new int[n + 1][W + 1];
        for (i = 0; i <= n; i++) {
            for (w = 0; w <= W; w++) {
                if (i == 0 || w == 0)
                    k[i][w] = 0;
                else if (wt[i - 1] < W)
                    k[i][w] = max(val[i - 1] + k[i - 1][W - wt[i - 1]], k[i - 1][w]);//比较两个数的大小，两种情况，加进去的情况和不加进去的情况；
                else

                    k[i][w] = k[i - 1][w];
            }
        }
        return k[n][W];
    }
    static int backpack11(int m, int[] A, int[] v) {
        if (m == 0 || A == null || v == null || A.length == 0)
            return 0;
            int len = A.length;
            int[][] val = new int[len][m + 1];
        for (int i = 0; i < len; i++) {
                val[i][0] = 0;
            }
            for (int i = 0; i < m + 1; i++) {
                if (i >= A[0])
                    val[0][i] = v[0];
            }
            for (int i = 1; i < len; i++)
            {
                for (int j = 1; j < m + 1; j++) {
                    if (j >= A[i]) {
                        val[i][j] = max(val[i - 1][j], val[i - 1][j - A[i]] + v[i]);
                    } else {
                        val[i][j] = val[i - 1][j];
                    }
                }
            }
            return val[len - 1][m];

        }



    public static void main(String[] args) {
        int [] value={60,100,120};
        int [] weight={10,20,30};
        int W=50;
        int n=value.length;
        System.out.println(knapSack(W,weight,value,n));
        System.out.println(backpack11(W,weight,value));
    }
}
