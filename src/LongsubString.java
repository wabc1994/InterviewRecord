//求出两个字符串的最大公共子串的长度，采用dynamic programming， 一个
/*public class CompareByRight {
    static int longestCommonSubstring(String A,String B)
    {
        if(A==null||B==null||A.length()==0||B.length()==0)
            return 0;
        int lenofA=A.length();
        int lenofB=B.length();
        int [][] longSubString=new int[lenofA][lenofB];//状态记录结构，默认初始化为0
        int max=0;                                                //j
      //经过第一个
        for(int i=0;i<lenofA;i++)                       //A='kkabcd'  和‘B=abeabcd’  i
        {
            //    k                                             a  b  e a b  c  d    (B)
            if(B.charAt(0)==A.charAt(i))
                                                       // A  k  0

            {                                    //          k  0
                longSubString[0][i]=i;           //          a  1       1
                max=1;                           //       3  b  0  2      2
            }                                    //          c  0           3
        }                           //                       d  0               4           max=1;
        for(int i=1;i<lenofB;i++)
        {
            for(int j=0;j<lenofA;j++)
            {
                if(B.charAt(i)==A.charAt(j)) {
                    if (j - 1 > 0)  //
                        longSubString[j][i] = longSubString[j- 1][i - 1] + 1;      //
                    else   //  j=1的情况
                        longSubString[j][i] = 1;     //包括第一次的
                        max = Max(longSubString[j][i], max);   //每一次执行完后都比较一次
                }

            }
        }
        return max;
    }
    static int Max(int a,int b)
    {
        if(a>b)
            return a;
        else return b;

    }
//test part
    public static void main(String[] args) {
        System.out.println(longestCommonSubstring("kkabbed","abcabbed"));
    }
}*/

