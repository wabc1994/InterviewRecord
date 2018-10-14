package SortedArray;

import java.util.Arrays;

/**
 * 将二维数组旋转90度，顺时针方向旋转
 * /*  this is the solution_1 ,分为两步的情况来解决该种情况
 *  * clockwise rotate
 *  * first reverse up to down, then swap the symmetry
 *  * 1 2 3     7 8 9     7 4 1
 *  * 4 5 6  => 4 5 6  => 8 5 2
 *  * 7 8 9     1 2 3     9 6 3
 * */

public class RotateImage {
       public void  solution_1(int [][] array)

       {
           if(array == null || array.length == 0 || array[0].length == 0)
           {return;}
          int n =array.length;
          helper(array);
          for(int i=0;i<n;i++){
              for(int j=0;j<i;j++){
                  int tmp = array[i][j];
                  array[i][j]= array[j][i];
                  array[j][i] = tmp;
              }
          }



       }
       //
       private void helper(int [][] array){
           int n= array.length;
           int [] tmp = new int[n];
           for(int i=0;i<n/2;i++){
               //保存下来该种情况
               tmp= array[i];
               array[i] = array[n-i-1];
               array[n-i-1] =tmp;
           }

       }
    private void helper_2(int [][] array){
        int n= array.length;
        //前半行的情况，后半行的情况
        for(int i=0;i<n<<1;i++){
            for(int j =0;j<n;i++)
            {              //保存下来该种情况
                int  tmp= array[i][j];
                array[i][j] = array[n-i-1][j];
                array[n-i-1][j] =tmp;
            }
        }
    }
       //逆时政旋转90度
}
