import java.util.ArrayList;
import java.util.List;

public class Triangle {
    /**
     *[
     *      [2],
     *     [3,4],
     *    [6,5,7],
     *   [4,1,8,3]
     * ]
     * The minimum path sum from top to bottom is 11 (i.e., 2 + 3 + 5 + 1 = 11).
     */
    //最基础的胴体规划解法，定义一个二维数组，f[row+1][column+1]
    //第一行和第一列不用处理，然后逐步添加即可,从下到上面开始，逐层递增，结果

    public int solution_2(List<List<Integer>> nums)
    {
        int [] total = new int [nums.size()];
        int l = nums.size()-1;
        for(int i=l;i>=0;--i)
        {
            for(int j=0;j<nums.get(i).size();++j) {
               //处理第一层的数据，初始化，这个可以在外面初始化，然后 i从size()-2出发即可，
                if (i == nums.size() - 1) {
                    total[j] = nums.get(i).get(j);
                    continue;
                } //代表上一层中的值 total[j]和total[j], 选取xiao ng li na gxiaonglinag
                total[j] = Math.min(total[j], total[j + 1]) + nums.get(i).get(j);
            }
        }

        return total[0];
    }
}
