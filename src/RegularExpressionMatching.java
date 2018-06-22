public class RegularExpressionMatching {
    //https://sites.google.com/site/jennyshelloworld/company-blog/regular-expression-matching---dp

    /*two solution solve this problem*/
    public boolean isMatch(String text, String pattern) {
        return isMatch_1(text, 0, pattern, 0);
    }

    private static boolean matchFirst(String s, int i, String p, int j) {
        //递归的求解方法，其中当两者都匹配到最后一个字符串，证明前面两者都可以连接得上来
        //
        if (j == p.length()) return i == s.length();
        if (i == s.length()) return j == p.length();
        //第三种情况的话是true;
        return p.charAt(j) == '.' || s.charAt(i) == p.charAt(j);

    }

    private static boolean isMatch_1(String s, int i, String p, int j) {
        if (j == p.length()) return i == s.length();
        final char b = p.charAt(j);
        //next char is not * then must match current character
        if (j == p.length() - 1 || p.charAt(j + 1) != '*') {
            // 匹配成功继续下一个字符
            if (matchFirst(s, i, p, j))
                isMatch_1(s, i + 1, p, j + 1);
            else
                return false;
        } else {
            if (isMatch_1(s, i, p, j + 2)) return true;
            while (matchFirst(s, i, p, j))
                if (isMatch_1(s, ++i, p, j + 2))
                    return true;
            return false;
        }
    }


    public boolean dynamic_programming(String text, String pattern)
    {
        int m = text.length();
        int n = pattern.length();
        boolean [][] dp= new boolean[m+1][n+1];
        dp[0][0] = true;
        //两个字符串都为空的时候，则为true
        //当text不为空字符串的时候，pattern为空的时候则为false
        for(int i=1;i<=m;i++)
        {
            dp[i][0] = false;
        }
        for(int i = 1;i <= m;i++)
        {
            for(int j=1;j <= n;j++)
            {
                if(pattern.charAt(j-1) == '.' || pattern.charAt(j-1) == pattern.charAt(i-1))
                    dp[i][j] = dp[i-1][j-1];

                else if(pattern.charAt(j-1) == '*')
                {
                //pattern.charAt(j-1)='*' 表示这个j-2字符可以不起作用， 出现0次, 也可以出现过次的情况
                    // 比如 S[i] = x  b= xa*  其中a字符不起作用的时候， b = x
                              // s[i]= x b = x.* 其中
                    dp[i][j] = dp[i][j-2];
                    //进一步考虑起作用的情况，起1次，2次，三次的情况，考虑前一个字符是什么的情况下

                    if(pattern.charAt(j-2) == '.' || pattern.charAt(j-2) == text.charAt(i-1))
                        //不考虑这种i 字符的情况了，相单与第一种情况下，综合考虑.* 连续的情况
                        dp[i][j] = dp[i][j] || dp[i-1][j];
                }

                else
                    //如果pattern[j-1]不等与'.' 和'*'两种情况，并且也不与text[i-1], 则可以直接false
                   return false;
        }

    }
     //动态规划方法来求解
    /*
    T=[0][0] =true
               T[i-1][j-1] if p[j]  =s[i]|| p[j]='.'
    T[i][j] =  T[i][j-2]    if p[j] = '*'      分两种情况考虑，回到上一步的情况，在这地方考虑两种情况，一个是'.' 和'*' 连着使用
               false     s[i]!=p[j]   &&p[j] !='.' &&p[j]!='*'

               上面即时状态转移方程的情况基础
    * */
}
