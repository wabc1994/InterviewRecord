package SortedArray;

public class ReversePair {
    public  int solution(int []num) {
        if (num == null || num.length == 0) {
            return 0;
        }
        return getCount(num, 0, num.length - 1);
    }

    private int getCount(int[] num, int start, int end){

        if(start>=end) {return 0;}
        int mid = (start+end)/2;
          //这是左边的结果，得到的结果是有序的情况
        int left = getCount(num, start,mid);
        //这是右边的结果情况，也是有序的情况

        int right =  getCount(num, mid+1,end);


        //计算两边的结果情况，进行查看进一步的情况
        int count = merger(num, start,mid, mid+1,end);

        return  right + left+count;




    }
    //在归并排序中，我们假设ls 到le， rs 到 re的元素都是有序的情况

    private int merger(int [] num, int ls,int le, int rs, int re){
        //都是已有的元素情况
        int mark_start_1 = ls;
        int len = re-ls+1;
        //标记开始初始的值
        int mark_start_2 =  rs;
         int count=0;
         int r = 0;
         int [] tmp  = new int [len];
         //这边都是有序的情况，n


        //如果是符合题意的情况， tmp 是有序数组的情况
         if(num[ls]>num[rs]>>1){
             //不单单只是有一次而已的情况发生，如果ls 比rs大的话，其实是比mark 到rs的值都大，都比前面的要大的情况
             count+= rs-mark_start_2+1;
            tmp[r++]=num[rs++];

         }
         //不满足题意情况
         else {
             tmp[r++] = num[rs++];
         }

         while(ls<=le && rs<=re){
             if(num[ls]<num[rs]){
                 tmp[r++] = num[ls++];
             }
             else {
                 tmp[r++] = num[rs++];
             }
         }
         //防止两边还有剩余的情况,有时候并不是都是2的整数，需要防止不是2的情况
        while(ls<=le){
            tmp[r++] = num[ls++];
        }

        while(rs<=re){
            tmp[r++] = num[rs++];
        }

        //将元素,在这里的情况是ls值已近改变了，需要重新标记下情况

         for(int i= 0;i<len;){
             num[i+mark_start_1]=tmp[i];
         }
         return count;


    }
    private void swap(int a, int b){
        int tmp = a;
        a = b;
        b =a;
      
    }
}
