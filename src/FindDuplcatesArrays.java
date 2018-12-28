public class FindDuplcatesArrays {
    /**
     * 查找数组中的重复元素情况，时间复杂度为o(n)， 空间复杂度为o(1)
     * Find duplicates in O(n) time and O(1) extra space.
     * Given an array of n elements which contains elements from 0 to n-1
     */
    public static void printDuplicates(int arr[], int n) {
        for (int i = 0; i < n; ++i) {
            //  当 >=0时，表示 是第一次访问该元素
            if (arr[Math.abs(arr[i])] >= 0)
                arr[Math.abs(arr[i])] = -arr[Math.abs(arr[i])];
            else
                //如果 arr[Math.abs(arr[i])<0]  表示第二次访问该元素
                System.out.println(Math.abs(arr[i]));
        }
    }

    public static void solution(int arr[])
    {
        int len = arr.length;
        //arr[i]!=arr[arr[i]]表示正确的位置上还没有，
        for(int i=0;i<len;i++){
            if(arr[i]>=len) {return;}
            while(arr[i]!=arr[arr[i]])

            //主要的改变点，将arr[i]!=1 改成arr[arr[i]]!=arr[i],

                {
                int temp =arr[arr[i]];
                arr[arr[i]] = arr[i];
                arr[i] =temp;

                }
        }

                       for(int i=0;i<len;++i){
                    if(arr[i]!=i)

            {System.out.println(arr[i]);}
        }
    }

    public static void main(String[] args) {
        int [] arr = new int [5];
        arr[0] = 2;
        arr[1] = 3;
        arr[2] = 1;
        arr[3] = 2;
        arr[4] = 3;
        FindDuplcatesArrays.printDuplicates(arr,5);
        FindDuplcatesArrays.solution(arr);
    }
}
