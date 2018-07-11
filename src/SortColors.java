public class SortColors {
    //[2,1,1,0,2,0,1] 变成[0,0,1,1,2,2]
    public void solution(int [] nums)
    {
        int n = nums.length;
        int index=0 ,high = n-1; //index指向左边已经相等的元素
        for(int i=0;i<high+1;++i)
        {
            while(nums[i]==2 && i<high) swap(nums[i],nums[high--]);
            while(nums[i]==0 && i>index) swap(nums[i],nums[index++]);
        }
    }
    private void swap(int a,int b)
    {
        int temp = a;
        a = b;
        b = temp;
    }
    public static void main(String[] args) {
        SortColors test = new SortColors();
        int [] arr = {2,0,2,1,1,0};
        test.solution(arr);
        for(int i: arr)
        {
            System.out.println(i);
        }
    }

}
