package SortedArray;

import data_struture.PriorityQueueEamople;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * 如何在一个无序数组中查找一个第k大的元素， 数组可能是包含相同的元素情况
 */
public class KthLargestElementinanArray {

       public int solutionn_1(int [] array, int k){
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

        //使用快速排序算法中的,只不过这里要注意的是求第k大的元素情况，区别求k大和k小的不同情况
    //下面的代码写成来求一个元素中第k小的元素情况，就是从左边点起第k个元素，记住是

        public int solution_2(int array[], int k){
           int n = array.length;

           //为何是n-k ，最大k-largest 相对于是数组中n-k+1 smalleset
           return helper(array, 0,n-1, n-k);
       }

        private int helper(int []array, int low, int high, int k) {
            //递归的出口函数，只要满足这样的条件才能进行如下的递归调用情况
            //递归出口情况
            if (low == high) {
                return array[low];
            }
            int position = partation(array, low, high);
            //最终会满足这一步的条件情况
            //本来这里是position = k -1 对应 n-k+1
            if (position == k) {
                return array[position];
            } else if (position < k) {
                return helper(array, position + 1, high, k);
            } else {
                return helper(array, low, position - 1, k);
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


        public static void main(String[] args) {
        KthLargestElementinanArray test=new KthLargestElementinanArray();
        int [] a ={1,2,3,4,5,6};
        System.out.println(test.solution_2(a, 5));

    }

}
