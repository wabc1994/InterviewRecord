package Dymaticprogramming;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

public class UglyNumberII {
    public int nthUglyNumber(int n) {
            int[] ugly = new int[n];
            ugly[0] = 1;
            int index2 = 0, index3 = 0, index5 = 0;
            int factor2 = 2, factor3 = 3, factor5 = 5;
            for(int i=1;i<n;i++){
                int min = Math.min(Math.min(factor2,factor3),factor5);
                ugly[i] = min;
                if(factor2 == min)
                    factor2 = 2*ugly[++index2];
                if(factor3 == min)
                    factor3 = 3*ugly[++index3];
                if(factor5 == min)
                    factor5 = 5*ugly[++index5];
            }
            return ugly[n-1];
        }
    public int solution_myself(int n) {
    //后验证的方法解决问
        if(n<=0) return 0;
        if(n==1) return 1;
    int [] result= new int [n];
    result[0] =1;
    int index2=1, index3=1,index5=1;
    int factor2 =2, factor3 =3, factor5= 5;
        for(int i =1;i<n;++i){
        int min = Math.min(Math.min(factor2,factor3), factor5);
        result[i] = min;
        //更新最后一个序列的数据
        if(result[i] == factor2)
            factor2 = (++index2) *2;
        if(result[i] ==factor3)
            factor3 = (++index3) *3;
        if(result[i]==factor5)
            factor5 = (++index5) *5;
    }
        return result[n-1];
}


}
