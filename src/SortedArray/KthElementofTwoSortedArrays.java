package SortedArray;

/**
 * 两个有序数组中第k小的元素情况,采用分而治之和递归的方法进行解决
 */
// csdn：
public class KthElementofTwoSortedArrays {
    public int solution(int A[], int B[], int k) {
        int m = A.length;
        int n = B.length;
        //不满足条件的情况下
        if (k > m + n || k < 1) {
            return -1;
        }
        //让A的元素个数尽量小于等于B
        if (m > n) {
            return solution(B, A, k);
        }
        //这是递归的出口程序
        if (k == 1) {
            return Math.min(A[0], B[0]);
        }
        //i,j 代表的是元素格式， i-1 和j-1代表在数组中的位置情况
        int i = Math.min(m, k << 1);
        int j = Math.min(n, k << 1);
        if (A[i - 1] > B[j - 1]) {
            return solution(A, B, k - j);
        } else {
            return solution(A, B, k - i);
        }
    }
}
