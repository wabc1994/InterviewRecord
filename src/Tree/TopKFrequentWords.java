package Tree;

import java.util.*;

class Pair{
    private String word;
    private int freqency;

    public Pair(String word, int freqency) {
        this.word = word;
        this.freqency = freqency;
    }

    public String getWord() {
        return word;
    }

    public int getFreqency() {
        return freqency;
    }
}

public class TopKFrequentWords {

    public List<String> topKFrequent_1(String[] words, int k){
        if(words==null || words.length==0 || k<=0)
        {return null;}
        List<String> result=new LinkedList<>();
        Map<String,Integer> map =new HashMap<>();
        //最大堆

        for(int i=0;i<words.length;i++){
           if(!map.containsKey(words[i]))
           {
               map.put(words[i],1);
           }
           else
           {
               map.put(words[i],map.get(words[i]+1));
           }
       }

        PriorityQueue<Pair> queue = new PriorityQueue<>(k, new Comparator<Pair>() {
            @Override
            public int compare(Pair o1, Pair o2) {
                //如果频率不想等的情况, 构造词频构造最大堆
                if(o1.getFreqency()!=o2.getFreqency()){
                    return o2.getFreqency()-o1.getFreqency();
                }
                //开始位置，最小堆
                return o1.getWord().compareTo(o2.getWord());
            }
        });

        for(Map.Entry<String,Integer> entry: map.entrySet()){
            Pair pair = new Pair(entry.getKey(),entry.getValue());
            if(pair!=null)
            {
                queue.add(pair);
            }
        }
        while(!queue.isEmpty()){
            result.add(queue.poll().getWord());
        }
        return result;
    }
    public List<String> topKFrequent_2(String[] words, int k){
        if(words==null || words.length==0 || k<=0)
        {return null;}
        List<String> result=new LinkedList<>();
        Map<String,Integer> map =new HashMap<>();


        //最大堆

        for(int i=0;i<words.length;i++){
            if(!map.containsKey(words[i]))
            {
                map.put(words[i],1);
            }
            else
            {
                map.put(words[i],map.get(words[i]+1));
            }
        }

        PriorityQueue<Map.Entry<String,Integer>> queue = new PriorityQueue<>(k, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                 if(o1.getValue().equals(o2.getValue())){
                     return o2.getValue()-o1.getValue();
                 }
                 return o1.getKey().compareTo(o2.getKey());
            }
        });
        for(Map.Entry<String,Integer> element:map.entrySet()){
            queue.offer(element);
            if(queue.size()>k)
            {
                queue.poll();
            }


        }
        //result基于链表可以在头位置插入
        while(!queue.isEmpty()){
            result.add(0,queue.poll().getKey());
        }
        return result;
    }

    public static void main(String[] args) {
        TopKFrequentWords test =new TopKFrequentWords();
        String [] string = {"i", "love", "leetcode", "i", "love", "coding"};

        List<String> result =test.topKFrequent_1(string,2);

        Iterator it = result.iterator();

        while(it.hasNext()){
            System.out.println(it.next());
        }
        for(String s:result){
            System.out.println(s);
        }
    }
}

