package Dymaticprogramming;


//如何查找其中的两次最大值,左边一次， 买卖股票系列的第三次情况
public class SellStock {
    public int maxProfit(int[] prices) {
        if(prices.length<2) return 0;
        int n= prices.length;
        int [] left =new int[n];
        int [] right = new int[n];
        left[0] = 0;
        //min代表最低价时买进股票，
        int min = prices[0];
        for(int i=1; i<n;++i){
            // prices[i]-min 表示在该处卖出去的股票价格，
            left[i] = Math.max(left[i-1], prices[i]-min);
            min = Math.min(min,prices[i]);
        }
        right[n-1] =0;
        int max = prices[n];
        for(int i= n-2;i>=0;--i){
            right[i] = Math.max(max-prices[i],right[0]);
            max = Math.max(max, prices[i]);
        }
        int result = left[0] +right[0];
        for(int i=1;i<n;i++){
            result = Math.max(result,left[i]+right[i]);
        }
  return result;
    }
}
