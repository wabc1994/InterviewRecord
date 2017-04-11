/**
 * Created by liuxiongcheng on 2017/4/10.
 */
public class Editdistance {

/*
*
* */
static int min(int x,int y,int z)
{
    if(x<y&&x<z)
        return x;
    else if(y<x&&y<z)
        return y;
    else
        return x;
}
     static int editDist(String str1,String str2,int m,int n)/*把第一个字符串变成第二字符串*/
     {
     if(m==0) return n;
     if(n==0) return m;
     if(str1.charAt(m-1)==str2.charAt(n-1)){
        return editDist(str1,str2,m-1,n-1);
        /*无论如何都要操作一次，三种操作中选出最小的数字,分别对应插入，替换，删除*/
    return 1+min(editDist(str1,str2,m,n-1),editDist(str1,str2,m-1,n-1),editDist(str1,str2,m-1,n));
    }
    
}
