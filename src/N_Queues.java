import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class N_Queues {
    /**
     * 八皇后问题，每一行一个字符，相邻两行的字符不能相接触，不能在一条斜线上和对角线上，同时不能在同一列
     * @param n
     * @return
     */
     public List<List<String>> solution(int n){
        char[][] board = new char[n][n];
        for(int i=0;i<n;++i){
            for(int j=0;j<n;++j){
                board[i][j] = '.';
            }
        }
        List<List<String>> res = new ArrayList<>();
        dfs(board,0,res);
        //正常的回溯算法，一个结果状态res,,回溯起始位置0(第一列)，回溯的结果表board
        return res;
    }
//i代表处理的列，i在这里表示的处理开始起始位置，从第一列开始到第八列，
    private void dfs(char[][] board, int colunm_index, List<List<String>> res) {
        //回溯的结束条件，添加结果列表,如果不能达到最后一列结果时，则不会时满足题意的条件，不会发生添加这种情况
        if (colunm_index == board.length) {
            res.add(constructor(board));
            return;//程序到这里为止
        }
        //每一列回溯有八个可选择的，所有for 循环为八重关系
        for (int i = 0; i < board.length; ++i) {
            if (validate(board, i, colunm_index)) {
                //for循环里面三部曲，
                //如果该位置可以放置一个皇后，继续下一列，加入操纵
                board[i][colunm_index] = 'Q'; // 类似于 subset 中的 tmp.pu(i),
                //深入下去
                dfs(board, colunm_index + 1, res);
               //回退操作，
                board[i][colunm_index] = '.';    // tempList.remove

                /*  tempList.add(nums[i]);
                backtrack(list, tempList, nums, i + 1);
                /*清空所有的路径中的内容*/
               // tempList.remove(tempList.size() - 1);
               // */
            }

        }
    }

    private  boolean validate(char[][] board, int x,int y){
        for(int i=0;i<board.length;i++){
            for(int j=0;j<y;++j){
                //如何判断在同一条对角线上的元素 board[i][j] = 'Q' && x+i = y+j(对角线上的元素)， x+y=i+j(是斜对角线上的情况)，
                if(board[i][j]=='Q' && (x+j==y+i) ||x+y==i+j||x==i)
                    return false;
            }
        }
        return true;
    }

    private List<String> constructor(char[][]board){
        //重新构造每一列的情况
        //board[i]代表以为以为数组中的一个数组
        List<String> res = new LinkedList<>();
        for(int i=0;i<board.length;i++){
            String s= new String(board[i]);
            //使用一个char[]来新建一个string也是可以的
            res.add(s);
        }
        return res;
    }

    public static void main(String[] args) {
        char bord[][] = {{'0', '1', '0', '1'},
                {'1', '0', '0', '1'},
                {'1', '0', '0', '1'},
                {'1', '0', '0', '1'}};
        List<String> res = new LinkedList<>();
        for (int i = 0; i < bord.length; ++i) {
            String s = new String(bord[i]);
            res.add(s);
        }
        for (String s :res) System.out.println(s);
        char b[] = {'1','2'};
        //使用一个char[] 构造一个字符串
        String s =new String(b);
        System.out.println(s);

    }
}
