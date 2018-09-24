package String;

public class LongestPalindromicSubstring {
    //分别记录字符串中的最大串的qishi
    public String longestPalindrome(String s) {
    int maxPalinLength = 0;
    String longestPalindrome = null;
    int length = s.length();
        for (int i = 0; i < length; i++) {
        for (int j = i + 1; j < length; j++) {
            int len = j - i;
            String curr = s.substring(i, j + 1);
            if (isPalindrome(curr)) {
                if (len > maxPalinLength) {
                    longestPalindrome = curr;
                    maxPalinLength = len;
                }
            }
        }
    }

		return longestPalindrome;
}

    public static boolean isPalindrome(String s) {
        //对一个字符能够走到最后的一种情况下如何做
        for (int i = 0; i < s.length() - 1; i++) {
            if (s.charAt(i) != s.charAt(s.length() - 1 - i)) {
                return false;
            }
        }

        return true;
    }
}
