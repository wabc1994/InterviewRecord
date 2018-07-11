import java.util.HashMap;
import java.util.Map;
public class LongestSubstringWithoutRepeating {
    public int solution(String s)
    {
        //暴力解决方法，初始化为max =1,包含第一个字符串
        int index = 0,max = 1,len = s.length();
        if(len==0)
            return 0;
        if(len==1)
            return 1;
        for(int i=1;i<len;++i)
        {
            for(int j=i-1;j>=index;--j)
            {
                if(s.charAt(i)==s.charAt(j)) {
                    index = j + 1;//相等情况下，不考虑更新max
                    //0到index-1是不再需要考虑的元素
                    break;//跳出整个循环语句, 转到处理下一个字符i,前面的不能在博阿凯
                }
                else//不相等情况下如何
                    {
                    if(max<i-j+1)
                        max = i -j +1;
                }
            }

        }
        return max;
    }
    public int solution_2(String s)
    {
        //比上面更加情况
        if(s.length()==0) return 0;
        Map<Character, Integer> map =new HashMap<Character, Integer>();
        int max =0;
        for(int i=0,j=0;i<s.length();++i) {
            if (map.containsKey(s.charAt(i)))
                //j是同一个字符出现的位置前面上面的一点的位置
                //在这里 j是不相等前面的元素
                j = Math.max(j, map.get(s.charAt(i)) + 1);
            //如果存在，求出最大值

            //如果不存在的话，直接put到上面，更新操作，
            //如果是已经存在的话会,会执行重写，更新操作等即可，比如  a   a    a, 当处理第三个a时，map('a')等于第二个的下标，而不是第一个
            map.put(s.charAt(i), i);
            max = Math.max(max, i - j + 1);
        }
        return max;
        }

    public static void main(String[] args) {
        Map<Character,Integer> ma = new HashMap<>();
        ma.put('c',1);
        ma.put('c',2);
        System.out.println(ma.get('c'));
    }
}
