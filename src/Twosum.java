import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuxiongcheng on 2016/12/3.
 */
public class Twosum {
     public int[] twosum(int [] nums,int target)
     {
         int [] res=new int[2];
         if(nums.length<2)
             return null;
         Map<Integer,Integer>mp=new HashMap<>();
         for(int i=0;i<nums.length;i++)
         {
             Integer diff=target-nums[i];
             if(mp.containsKey(diff))
             {
                 res[0]=mp.get(diff);
                 res[1]=i;
                 return res;
             }else
             {
                 mp.put(nums[i],i);
             }
         }
         return null;
     }

    public static void main(String[] args) {
        int[] test1={1,3,4,5};
        Twosum test2=new Twosum();
        int res1[]=test2.twosum(test1,6);
        System.out.println(Arrays.toString(res1));

    }

}
