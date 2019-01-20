package SortedArray;

import java.util.LinkedList;
import java.util.List;

/**
 * @ClassName spiralOrder54
 * @Description TODO
 * @Author coderlau
 * @Date 2018/12/26 9:25 PM
 * @Version 1.0
 **/


public class spiralOrder54 {
    public List<Integer> twoDprint(int[][] matrix){
        List<Integer> result =new LinkedList<>();
        if(matrix==null|| matrix.length==0||matrix[0]==null){
            return result;
        }
        // 顶一个
        int column_first = 0;
        int column_last= matrix.length-1;
        int row_last = matrix[0].length-1;
        int row_first = 0;

       while(row_first<=row_last && column_first<=column_last)
        {
            // 从左到右的情况，
            for(int j=row_first;j<=row_last;j++){
                result.add(matrix[column_first][j]);
            }
            //一行减少该种情况
            column_first++;
            //行变化,从上到下
            for(int i=column_first;i<=column_last;i++){
                result.add(matrix[i][row_last]);
            }
            //从下面到上面的基本情况
            --row_last;
            if(row_last>=row_first){
                for(int j=row_last;j>=row_first;j--)
                { result.add(matrix[column_last][j]);}

            }
            column_last--;
            if(column_last>=column_first){
                for(int i=column_last;i>=column_first;i--)
                { result.add(matrix[i][row_first]);}
            }
            row_first++;
        }

        return result;

    }


}
