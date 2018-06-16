public class search_2D {
    //solution_1
    /*
    [
            [1,   4,  7, 11, 15],
            [2,   5,  8, 12, 19],
            [3,   6,  9, 16, 22],
            [10, 13, 14, 17, 24],
            [18, 21, 23, 26, 30]
            ]
            */
    public boolean solution_1(int matrix[][],int target)
    {
        //采用二分查找的思想解决这道题目，其中rows和columns类似于
        // low 和high的关系，通过控制low 和high两者的取值进行类比
        if(matrix ==null || matrix.length==0)return false;
        int  m = matrix.length;
        int n = matrix[0].length;
        int rows =0, columns = n-1;
        while(rows<m && columns>=0)
        {
            if(matrix[rows][columns]==target)
                return true;
            else if(matrix[rows][columns] > target)
                columns--;
            else
                ++rows;
        }
        return false;
    }

    //讲二维数组展开为一位数组，其中找到二维数组 的下标与一位数组下标的关系，一维数组的座位为low =0，high  = m*n-1;
    //采用二分查找的办法，其中row = (一维坐标的位置/列数)，column = (一维坐标的位置%列数)
    // matrix[row][column]是元素在二维数组中的位置，在一维数组中的二分查找为mid
    //[
    //  [1,   3,  5,  7],
    //  [10, 11, 16, 20],
    //  [23, 30, 34, 50]
    //]两种方法是不一样的,对数组的要求是不一样的
    public boolean solution_2(int [][] matrix,  int target)
    {
        if(matrix ==null || matrix.length==0|| matrix[0].length==0)
            return false;
        int m = matrix.length, n = matrix[0].length;
        int low = 0, high = m*n -1;
        while(low<=high)
        {
            int mid = (high+low) / 2;
            int row = mid / n;
            int column = mid % n;
            if(matrix[row][column] ==target)
                return true;
            else if(matrix[row][column]>target)
                high = mid -1;
            else
                low = mid +1 ;
        }
        return false;
    }
}
