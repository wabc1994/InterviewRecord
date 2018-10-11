package SortedArray;

public class ContainerWithMostWater {
    public  int solution(int [] heights){
        int result=0;
        int n = heights.length;
        int left=0, right =n-1;
        while(left<right){
            int tmp= (right-left) * Math.min(heights[left],heights[right]);
            result = Math.min(tmp,result);
            if(heights[left]<heights[right])
            {
                ++left;
            }
            else
            {
                --right;
            }
        }
        return result;
    }
    //快速排序算法该种情况,采用快速的毕竟算法情况
    public int solution_2(int [] heights){
        int result = 0;
        int n = heights.length;
        int left = 0 ,right = n-1;
        while(left<right){
            int  h = Math.min(heights[left],heights[right]);
            result = Math.max(h* (right-left), result);
            while (left<right&&heights[left]<=h) {++left;}
            while (left<right && heights[right]<=h){
                --right;
            }

        }
        return result;
    }
}
