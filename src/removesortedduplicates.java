import com.sun.org.apache.xpath.internal.SourceTree;

import java.util.Arrays;

/**
 * Created by liuxiongcheng on 2016/12/3.
 */
public class removesortedduplicates {    //第一种解法
   static int remove(int A[]) {
        int i = 0;
        int j=1;
        for ( j = 1; j < A.length; j++) {
            if (A[i] != A[j])//进行操作,临近点是两个数不相等 ，以为操作
                {
                    A[++i] = A[j];
                }
        }
            int[] B;
           B = Arrays.copyOf(A, i + 1);
            System.out.println(Arrays.toString(B));
            return i + 1;//元素的个数
    }


   public  static  int[] removeDuplicates(int []A)
     {
         if(A.length < 2)
             return A;
         int i = 0, j = 1;
         while (i < A.length&& j<A.length) {
             if (A[i] == A[j]) {
                 j++;//如果相等，继续比较下去
             } else {  //当找到不想等的数组时
                 i++;
                 A[i] = A[j];
             }
         }
         //将改变后的数组放到一个新的数组中去，
         int [] B= Arrays.copyOf(A,i+1); //copyof(旧的数组，新的数组的长度)
         return B;
     }

    public static void main(String[] args) {
        int [] B;
        int [] arr={1,2,2,3,3};
        B=removesortedduplicates.removeDuplicates(arr);
        System.out.println(Arrays.toString(B));
    }
}
