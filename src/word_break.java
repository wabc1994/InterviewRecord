import java.util.Set;

public class word_break {
    public boolean soultion(String s, Set<String>dict)
    {

        boolean[] f = new boolean[s.length()+1];
        f[0] = true;
        for(int i=1;i<=s.length();i++)
        {
            for(String str: dict)
            {
                if(str.length()<=i)
                {
                    if(f[i-str.length()])
                    {
                        if(s.substring(i-str.length(),i).equals(str))
                        {
                            f[i] = true;
                            break;
                        }
                    }
                }
            }
        }
        return f[s.length()];
    }
    //   解法解释： https://blog.csdn.net/DERRANTCM/article/details/47774547
    //second dp 基础的动态规划问题，将问题分解为
    public boolean solution_2(String s, Set<String>dict)
    {
        boolean[] f = new boolean[s.length()+1];
        f[0] = true;
        for(int i =1;i<s.length();i++)
        {
            for(int j=0;j<i;j++)
            {
                if(f[j]  && dict.contains(s.substring(j,i+1)))
                 f[i] = true;
                break;
            }
        }
        return f[s.length()];
    }

}
