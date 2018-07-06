import java.util.Arrays;
public class ValidSudoku {

    public boolean solution_1(char[][] board)
    {
        if(board.length<9 || board[0].length<9 ) return false;
        //三个数组记录，used1数组记录每一行的出现情况,used2数组1记录列的情况，used3记录块的情况
        //分为九行，九列，分为九块 k = i/3*3 +j/3这是在求某个块号的情况，
        //对于每一元素，i代表处理的是行， j代表处理的是列，
        boolean [][] used1  = new boolean[9][9];
        boolean [][] used2  = new boolean[9][9];
        boolean [][] used3   = new boolean [9][9];
        for(int i = 0; i < board.length; ++ i){
            for(int j = 0; j < board[i].length; ++ j)
                if(board[i][j] != '.')
                {
                    int num = board[i][j] - '0' - 1, k = i / 3 * 3 + j / 3;
                    if(used1[i][num] || used2[j][num] || used3[k][num])
                        return false;
                    used1[i][num] = used2[j][num] = used3[k][num] = true;
                }
    }
    return true;
        }

        //每一行，每一列分开处理的情况
        public boolean solution_2(char[][] board) {
            boolean[] used = new boolean[9];
            for (int i = 0; i < 9; ++i)
            {
                //检查行
                for (int j = 0; j < 9; ++j)
                    if (!check(board[i][j], used))
                        return false;

                //重置标志数组
                Arrays.fill(used, false);
               // 检查列
                for(int m =0; m<9;++m)
                    if(!check(board[m][i],used))
                        return false;

            }
            //检查块，
            for(int r=0;r<3;++r)
            {
                for(int c= 0;c<3;++c) {
                    Arrays.fill(used, false);
                    for (int i = r * 3; i < r * 3 + 3; ++i)
                        for (int j = c * 3; j < c * 3 + 3; ++j)
                            if (!check(board[i][j], used))
                                return false;
                }
            }

        return true;
    }

        private boolean check(char ch, boolean[] use1){
                if(ch=='.') return true;
                if(!use1[ch-'1'])
                    use1[ch-'1'] = true;
                else
                    return false;
                return true;
            }

}
