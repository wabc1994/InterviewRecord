import java.util.*;

public class TopKFrequentElements {
    public List<Integer> solution(int [] nums, int k){
        Map<Integer,Integer> map = new HashMap<>();
        //统计频率的写法,
        for(int n:nums){
            map.put(n,map.getOrDefault(n,0) + 1);
        }
        //设置一个最小堆，最小的元素放在堆顶，如果堆中的元素少于k个，直接往堆中添加元素，在这个过程中始终维护一个最小堆，
        Queue<Map.Entry<Integer,Integer>> minheap =new PriorityQueue<>(k,(a,b)->(a.getValue()-b.getValue()));
        //k为堆的容量大小，
        for(Map.Entry<Integer,Integer> entry: map.entrySet()) {
            //逐渐构建堆的过程，如果堆中的元素比较少于k个，直接添加

            if (minheap.size() < k)
                minheap.add(entry);
            else {
                //堆已满，此时如果要新加入的元素value大于堆中的最小值，则添加
                Map.Entry<Integer, Integer> min = minheap.peek();
                if (entry.getValue() > min.getValue()) {
                    minheap.poll();
                    minheap.add(entry);
                }
            }
        }

        List<Integer> res =new ArrayList<>();
        while(!minheap.isEmpty()){
            Map.Entry<Integer,Integer> entry = minheap.poll();
            res.add(entry.getKey());
        }

        return res;
        }

}
