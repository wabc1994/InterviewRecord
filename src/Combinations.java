import java.util.ArrayList;
import java.util.List;
public class Combinations {

    public List<List<Integer>> solution_1(int n, int k)
    {
        //nb
        List<List<Integer>> result = new ArrayList<>();
        List<Integer> temp = new ArrayList<>();
        if(k>n) return result;
        dfs(n,k,1,temp,result);
        return result;
    }
    //start 开始的数，cur是已经选择的数目

    private void dfs(int n, int k, int start,List<Integer>temp, List<List<Integer>> result)
    {
        //判断加入元素的个数， 列表的长度
        if(temp.size()==k)
        {
            result.add(new ArrayList<>(temp));
            //继续往下走
            //继续往下走，没有到底
        }

        for(int i=start;i<=n;i++)
        {
            if(!temp.contains(i)) {

                temp.add(i);//中间结果添加处理

                //向下操作，
                dfs(n, k, i + 1, temp, result);//只能往下走，第i个元素的下面的元素只能是i+1,num[i]下面操作只能是num[i+1]
                //全排列的第num[i]个下面的操作可以选择的是num[0], num[1],num[2] ,,,,num[length-1]

                //回退操作，返回操作
                 temp.remove(temp.size() - 1);
            }
        }
    }
}
