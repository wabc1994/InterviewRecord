package String;

public class PalindromeNumber {
    public boolean isPalindrome(int x) {
        //小于0的情况是一种情况, 判断左右是否相等的情况下如果进行下去
        if(x<0)
            return false;
        if(x!=0  && x%10==0) return false;
        int tmp=0;
        while(x>tmp){
            tmp = tmp *10 +x %10;
            x /=10;
        }
        return (x==tmp) || (x==tmp/10);
    }
}
