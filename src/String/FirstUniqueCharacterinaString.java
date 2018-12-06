package String;

import java.util.*;

/**
 * @ClassName FirstUniqueCharacterinaString
 * @Description TODO
 * @Author coderlau
 * @Date 2018/12/5 11:25 PM
 * @Version 1.0
 **/
class Node{
      private int number;
      private int index;

    public Node(int number, int index) {
        this.number = number;
        this.index = index;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getNumber() {
        return number;
    }

    public int getIndex() {
        return index;
    }
}


public class FirstUniqueCharacterinaString {

    public int firstUniqChar(String s) {
        char[] chars = s.toCharArray();

        Map<Character, Node> map = new LinkedHashMap<Character, Node>();

        for (int i = 0; i < chars.length; i++) {
            if (map.containsKey(chars[i])) {
                // the first time to put the number
                Node tmp = map.get(chars[i]);
                int num = tmp.getNumber();
                int index = tmp.getIndex();
                num++;
                map.put(chars[i], new Node(num, index));
            } else {
                map.put(chars[i], new Node(1, i));
            }

        }
        for (Map.Entry<Character, Node> entry : map.entrySet()) {
            if (entry.getValue().getNumber() == 1) {
                return entry.getValue().getIndex();

            }
        }
        return -1;

    }

    //对上述的代码进行一定程度的优化即可

  public int solution_1(String s){

        if(s==null || s.length()==0){
            return -1;
      }
      Map<Character, Integer> record = new HashMap<>();
        // 第一次进行遍历主要是关注统计数目相关的情况等信息

      for(int i=0;i<s.length();i++){
            record.put(s.charAt(i), record.getOrDefault(s.charAt(i),0)+1);
        }
        //第二次遍历统计个数等于1的情况
      for(int i =0;i<s.length();i++){
          if(record.get(s.charAt(i))==1){
              return i;
          }
      }
      return -1;
  }


    // 终极做法, 也是构造一个hashtble, 类似于桶排序的思想，进行一个基本归为操作， 就是上面问题的一个改进版本 ， 就是讲hashmap 的大小固定下来的情况， 已经知道出现的都是26个字母的基本情况



    public  int solution_3(String s){

        if(s==null || s.length()==0){
            return -1;
        }
        int [] hashtable = new int[26];

        // 字母与数字之间的映射关系，就是第一个字母

        for( int i=0;i<s.length();i++){
            //  说白了就是利用数组坐标来标记一个字母吗，就类似于在hashmap 我们需要两个结构来标记一个数组的基本情况一样
            hashtable[s.charAt(i)-'a']++;
        }
        for(int i=0;i<s.length();i++){
            if(hashtable[s.charAt(i)-'a']==1)
                         {return i;}
        }
        return -1;
    }

    //队列的层次遍历思想来做


}
