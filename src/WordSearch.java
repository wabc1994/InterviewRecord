public class WordSearch {
    /**
     * board =
     * [
     *   ['A','B','C','E'],
     *   ['S','F','C','S'],
     *   ['A','D','E','E']
     * ]
     *
     * Given word = "ABCCED", return true.
     * Given word = "SEE", return true.
     * Given word = "ABCB", return false.
     */


    public boolean exist(char[][] board, String word){
        if(board.length==0 || board[0].length==0||word.length()==0)return false;
        boolean visited[][] = new boolean[board.length][board[0].length];
        //每一个位置处都开始一次的深度优先搜索，board中每一次位置都可以开始深度优先搜索
        for(int i=0;i<board.length;i++){
            for(int j=0;j<board[i].length;++j){
                dfs_find(board,visited, i,j,word,0);
            }
        }
        return false;
    }


    boolean dfs_find(char[][]board, boolean[][] visited, int row, int column,String word, int index){
        //可以匹配完一个完整的数组
        if(index==word.length())
            return true;
        //不符合条件的直接跳到下面一个字符处理的情况
        if(row <0 || column<0 ||column>=board[0].length||row>=board.length||board[row][column]!=word.indexOf(index)||visited[row][column])
            return false;

        visited[row][column] = true;
        dfs_find(board,visited,row+1,column,word,index+1);
        dfs_find(board,visited,row,column+1,word,index+1);
        dfs_find(board,visited,row-1,column,word,index+1);
        dfs_find(board, visited, row, column-1, word, index+1);
        visited[row][column] = false;
        return false;
    }
}
