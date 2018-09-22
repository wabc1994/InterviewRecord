package Dymaticprogramming;

public class DecodeWays {
    public int numDecodings(String s) {
        int len = s.length();
        if(len==0) return 0;
        int [] dp = new int [len+1];
        dp[0] = 1;
        dp[1] = s.charAt(0)!=0 ? 1:0;
        //0 就当是一个障碍点，如果是等于0 ，则，0只能和前面的一个数进行捆绑判断，
        //i-1
        for(int i = 2;i<=len;++i){
            if(s.charAt(i-1)!=0)
                dp[i] =dp[i-1];
            int num = Integer.parseInt(s.substring(i-2,i));
            if(num>=10 && num<=26)
                dp[i] = dp[i-2];

        }
        return dp[len];

    }

    public static void main(String[] args) {
        DecodeWays test =new DecodeWays();
        int s=test.numDecodings("226");
        System.out.println(s);
    }
}