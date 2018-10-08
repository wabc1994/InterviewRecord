- 导读
  - 如何查找一个无序数组中的最大第k个数
  - 如何查找一个无序数组中的最小第k个数
  - 如何查找两个有序数组中的第k大数
  - 如何查找两个有序数组的中位数
# 如何在两个有序数组中找到第K的元素
[参考链接情况](https://www.geeksforgeeks.org/k-th-element-two-sorted-arrays/)
>example:
Input : Array 1 - 2 3 6 7 9
        Array 2 - 1 4 8 10
        k = 5
Output : 6
Explanation: The final sorted array would be -
1, 2, 3, 4, 6, 7, 8, 9, 10
The 5th element of this array is 6.
Input : Array 1 - 100 112 256 349 770
        Array 2 - 72 86 113 119 265 445 892
        k = 7
Output : 256
Explanation: Final sorted array is -
72, 86, 100, 112, 113, 119, 256, 265, 349, 445, 770, 892
7th element of this array is 256.
## 思路一：最简单的合并排序算法
时间复杂度O(m+n)，空间复杂度O(m+n) 

```Java
// Java Program to find kth element  
// from two sorted arrays 
  
class Main 
{ 
    static int kth(int arr1[], int arr2[], int m, int n, int k) 
    { 
        int[] sorted1 = new int[m + n]; 
        int i = 0, j = 0, d = 0; 
        while (i < m && j < n) 
        { 
            if (arr1[i] < arr2[j]) 
                sorted1[d++] = arr1[i++]; 
            else
                sorted1[d++] = arr2[j++]; 
        } 
        while (i < m) 
            sorted1[d++] = arr1[i++]; 
        while (j < n) 
            sorted1[d++] = arr2[j++]; 
        return sorted1[k - 1]; 
    } 
      
    // main function 
    public static void main (String[] args)  
    { 
        int arr1[] = {2, 3, 6, 7, 9}; 
        int arr2[] = {1, 4, 8, 10}; 
        int k = 5; 
        System.out.print(kth(arr1, arr2, 5, 4, k)); 
    } 
} 
```

## 思路二：Divide And Conquer Approach 
只需要O(k)的时间复杂度，不需要额外申请空间。
考虑两个都是有序数组情况,
>比较A[k/2]和B[k/2]，如果A[k/2]<B[k/2]那么A的前k/2个数一定都在前k-1小的数中，将A数组前k/2个数扔掉，反之扔掉B的前k/2个数。将k减小k/2。重复上述操作直到k=1。比较A和B的第一个数即可得到结果。时间复杂度O(logk)。
不断进行递归，缩小问题的规模， 每次都是缩小k/2个元素 ， 同时在A,B 两个数组里面淘汰掉K/2个元素的情况 ， 不断进行缩减，最后的情况就是 k=1 ， 然后在A, B 两个数组中查找第1小的元素，即可

- **即然是递归处理**
- **所以必然要定义递归的出口情况， 当K==1时是递归的出口**
- **假设数组A的长度为m, 数组B的长度为n**
- **边界条件排查： m,  k/2, 不能让 k/2大于m  否则没有意义**
___
i = min (m, k/2);
j = min(n, k/2);
上述都是代表元素个数情况
但在元素位置中比较==A[i-1] 和B[i-1]==
****
### 比如
[过程演示底下](https://www.geeksforgeeks.org/k-th-element-two-sorted-arrays/)
### 代码

```Java
package SortedArray;

/**
 * 两个有序数组中第k小的元素情况,采用分而治之和递归的方法进行解决
 */
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
```
# 215. Kth Largest Element in an Array
与上面的不一样，该问题是无序数组，并且有可能包含相同元素情况，
Example 1:

Input: [3,2,1,5,6,4] and k = 2
Output: 5
Example 2:

Input: [3,2,3,1,2,4,5,5,6] and k = 4
1 2 2 3 3 4 5 5 6 排在第4大是4 而不是 3 
Output: 4
## 思路：构造一个最大的优先级队列
优先级队列是可以包含相同元素的情况，构造一个包含 k 个最大优先级，然后让前面的k-1个元素先出去 ,最后只有剩下一个元素的情况
## 代码
```Java
package SortedArray;
import data_struture.PriorityQueueEamople;
import java.util.Comparator;
import java.util.PriorityQueue;
/**
 * 如何在一个无序数组中查找一个第k大的元素， 数组可能是包含相同的元素情况
 */
public class KthLargestElementinanArray {
    public int solutionn(int [] array, int k){
//构造大顶堆的情况，
        PriorityQueue<Integer> maxHeap=new PriorityQueue<Integer>(k, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2-o1;
            }
        });
        for(int i=0;i<array.length;i++) {
            if(!maxHeap.contains(array[i]))
                 {maxHeap.offer(array[i]);}
        }
        for (int j = 0; j < k - 1; j++) {
                maxHeap.poll();
            }
        return maxHeap.peek();
        }
        public static void main(String[] args) {
        KthLargestElementinanArray test=new KthLargestElementinanArray();
        int [] a ={3,2,3,1,2,4,5,5,6,7,9,0,-1};
        System.out.println(test.solutionn(a, 7));
    }
}
```
## 空间复杂度分析 
其中O(nlogk) ，k为堆的大小， n 为几趟 ， O(k)为空间复杂度情况
## 思路二：快速排序算法思想的应用
利用快速算法中的partation分区算法，每次从算法中选择一个在最终排序中正确的位置处理，  对比该元素在数组中的位置
## 代码
[参考链接](https://aaronice.gitbooks.io/lintcode/data_structure/kth_largest_element.html)
```java
class Solution {
    public int findKthLargest(int[] nums, int k) {
       if (nums == null || nums.length == 0 || k <= 0 || k > nums.length) {
            return 0;
        }
         return select(nums, 0, nums.length - 1, nums.length - k);
    }
    public int select(int[] nums, int left, int right, int k) {
        if(left==right) {return nums[left];}
        int pivotIndex = partition(nums, left, right);
        if (pivotIndex == k) {
            return nums[pivotIndex];
        } else if (pivotIndex < k) {
            //在这里还是不要变k ， 因为是从右变开始算起的， 如果是
            return select(nums, pivotIndex + 1, right, k);
        }  else {
            return select(nums, left, pivotIndex - 1, k);
        }
            
    }
    /**
    * 
* @param array 要查找的数组情况
* @param low
* @param high
* @param k 要查找的元素情况
* @return 
*/
    //上述写法可以搞成非递归方法形式情况,在这里完全就是二分查找的过程步骤，这里的k就是要查找的元素 
    public int select_non_recurive(int array, int low, int high, int k){
        while(low<high){
            int position = partition(array, low,left);
            if(position=k){
                return array[position];
            }
            else if(position<k){
                low = position+1;
            }
            else 
                {
                    high  = position-1;
                }
        }
    }
    //查询结果情况
    public int partition(int[] nums, int left, int right) {
        // Init pivot, better to be random
        int pivot = nums[left];
        // Begin partition
        while (left < right) {
            while (left < right && nums[right] >= pivot) { // skip nums[i] that equals pivot
                right--;
            }
            nums[left] = nums[right];
            while (left < right && nums[left] <= pivot) { // skip nums[i] that equals pivot
                left++;
            }
            nums[right] = nums[left];
        }

        // Recover pivot to array
        nums[left] = pivot;
        return left;
    }

}
```


# 两个有序数组的中位数情况
[参考情况](https://www.geeksforgeeks.org/median-two-sorted-arrays-different-sizes-ologminn-m/)

# 查找一个无序数组中第k小的元素
采用快速排序中的选择排序算法，partation
代码如下所示：
```java
public  class ktheElement {
public int solution_2(int array[], int k){
           int n = array.length;
           return helper(array, 0,n-1, k);
       }
 private int helper(int []array, int low, int high, int k) {
          //递归的出口函数，只要满足这样的条件才能进行如下的递归调用情况, 此时 
           if (low ==high） {return array[low];}
             int position = partation(array, low, high);
                //最终会满足这一步的条件情况
                if (position == k -1) {
                    return array[position];
                } else if (position < k - 1) {
                    return helper(array, position + 1, high, k - position - 1);
                } else {
                    return helper(array, low, position - 1, k);
                }
                 }
          }
   private int partation(int [] array,int low, int high) {
            int pivot = array[low];
            int i = low, j = high;
            while (i < j) {
                while (i < j && array[j] >= pivot) {
                    --j;
                }
                array[i] = array[j];
                while (i < j && array[i] <= pivot) {
                    ++i;
                }
                array[j] = array[i];
                // return partation
            }
            array[i] = pivot;
            //the position
            return i;
        }
}
```