package Dymaticprogramming;

public class BestTimetoBuyandSellStock {
/**
 * 股票购买[7,1,5,3,6,4],  对一个数组（代表股票的价值）执行两个操作，一个是买操作，一个是卖操作，如何能让利润最大化
 */

//不采用动态规划算法解决，每个位置处往前找一个最小值作为股票的购买日子去，把第i个元素作为卖出的日子，
     public int solution_1(int [] prices){
         if(prices.length==0||prices==null||prices.length==1) return 0;
         int n = prices.length;
         int max_price=0;
         for(int i=1;i<n;i++){
             int min = prices[0];
             for(int j=0;j<i;++j){
                 if(prices[j]<min)
                     min = prices[j];
                 }
                 if(min<prices[i])
                     max_price= Math.max(prices[i]-min,max_price);
         }
         return max_price;
         }

         //贪心算法，对上述算法进行优化即可，在查找的过程中即记录一个
     public int solution_2(int[] prices) {
         if(prices.length<2) return 0;
         int profit = 0;//差价，也就是利润的问题
         //代表最小位置信息
         int min = prices[0];
         for(int i=1;i<prices.length;++i){
             profit = Math.max(profit,prices[i]-min);
             //记录最小的位置
             min = Math.min(min,prices[i]);
         }
         return profit;

}
    public int solution_3(int[] prices){
        if(prices.length<2) return 0;
        int max_profit = 0,max_cur=0;
        for(int i=1;i<prices.length;++i){
             max_cur= Math.max(prices[i]-prices[i-1]+ max_cur,0);
             max_profit = Math.max(max_profit,max_cur);
        }
        return max_profit;
    }

}
