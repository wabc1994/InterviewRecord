# 最小堆和HashMap

## HashMap
先使用HashMap进行遍历元素进行统计该中情况，

HashMap 存储有两种形式 

c++ 中的另一种方式
     - unordered_map<int,int> map 其中map可以直接访问map[]
     - priority_queue<>

- Map<key,<key, frequence>> 
- Map<key,frequence>

```java
word是保持数组的情况
for(int i=0; i<words.length; i++)
        {
            if(map.containsKey(words[i]))
                map.put(words[i], map.get(words[i])+1);
            else
                map.put(words[i], 1);
        }
```
遍历HashMap中的元素情况



```java
事先确定好k的大小即可
for(Map.Entry<key,value> element:map){
    queue.offer(element);
    //或者两种情况
}
//固定好堆的大小，就不用
```

[347. Top K Frequent Elements](https://leetcode.com/problemset/all/?search=top%20k)
[692. Top K Frequent Words](https://leetcode.com/problems/top-k-frequent-words/discuss/108346/My-simple-Java-solution-using-HashMap-and-PriorityQueue-O(nlogk)-time-and-O(n)-space)
## 最小堆的情况
主要是构造优先级队列和堆，主要是查看最top_k 问题求解
- 最大堆 （o1,o2)->o1-o2
- 最小堆  (o1,o2)->o2-o1



