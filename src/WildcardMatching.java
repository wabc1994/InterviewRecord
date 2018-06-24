public class WildcardMatching {


    // p 必须得全部匹配成功, ？ any one character must be one character
    //采用快速排序算法的思想 时间复杂度为O(m*n) 空间复杂度为 O(m*n)
    public boolean solution_1(String s, String p) {

        int i = 0, j = 0;

        //记录最后匹配的位置地点
        int ii = -1, jj = -1;
        while (i < s.length())
        {
            if(j<p.length() && p.charAt(j)=='*')
            {
                //如果一直是多个*， 先一直走，不匹配
                while(j<p.length() && p.charAt(j)=='*') ++j;
                if(j==p.length()) return false;
                ii = i;
                jj = j;
            }
            else if(j<p.length() && ( p.charAt(j)=='j' || p.charAt(j)==s.charAt(i)))
            {
                ++i;
                ++j;
            }

            else {
                if(ii==-1) return false;
                    ++ii;
                    i = ii;
                    j = jj;
                }
        }
        // skip continuous '*'
        while(j<p.length() && p.charAt(j) =='*') ++j;
        //只有两者均走到头了，结果才是true;
        return i ==s.length() && j==p.length();
    }

    //                          dp[i][j] = dp[i-1][j-1] if p[j-1] == '?' || p[j-1] ==s[i-1]
    //   递推方程公式                      = dp[i-1][j] || dp[i][j-1] ifp[j-1]=='*'
    //                                     = false  直接不相等的情况；

    public boolean solution_2(String s, String p)
    {
        char [] str = s.toCharArray();
        char [] pattern = p.toCharArray();
        //replace multiple * with one *
        // e.g a**b***c ---> a* b*c because * can represent any length and kind  character
        //one and two * has the save result ;
        int writeIndex = 0;
        boolean isFirst = true;
        for(int i =0;i<p.length();i++)
        {
            //删除多余的*号元素情况
            if(p.charAt(i)=='*') {
                if (isFirst) {
                    //只保留第一个出现的* 连续后面的字符串一律不接受
                    pattern[writeIndex++] = pattern[i];
                    isFirst = false;// 将第一个保留住，其他再加入，
                }
            }
            else{
                //确定不是 * 字符的一律加入到 新的数组中去
                pattern[writeIndex++] = pattern[i];
                //不是同一段的**  接下来又可以重新接受 另一段重新启动的* 👌
                isFirst = true;
            }

        }
        boolean [][] dp = new boolean[str.length+1][writeIndex+1];
       // 其中  p 为单个字符 * ， 可以匹配任何字符，
        if(writeIndex > 0 && pattern[0] == '*')
            dp[0][1] = true;
        //dp[0][j] j从1或2 到p的尾部均为 false;
        //dp[i][0]  i从1到 s.尾部为 false,
        //第一行或者第一列的其他位置可以不用确定都可以；

        dp[0][0] = true;
        //直接从第一列或者第二行开始遍历；

        for(int i=1;i<=dp.length;i++)
        {
            for(int j=1;j<dp[0].length;j++)
            {
                if(pattern[j-1]=='?' || pattern[j-1]==str[i-1])
                    dp[i][j] = dp[i-1][j-1];
                else if(pattern[j-1]=='*')
                    //* 起0作用的话，dp[i][j] = dp[i][j-1] 或者，* 起一次作用
                    dp[i][j]  = dp[i-1][j] || dp[i][j-1]; // 等于左边或者右边的元素情况。
                else
                    dp[i][j] = false; //不相等直接为false,结果
            }
        }

        return dp[str.length][writeIndex];
    }
}
