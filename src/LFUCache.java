import java.util.HashMap;
import java.util.LinkedHashSet;
public class LFUCache {

    private int cap;
    private int min=-1;
    private HashMap<Integer,Integer> value;
    private HashMap<Integer,Integer> frequently;
    private   HashMap<Integer,LinkedHashSet<Integer>> frequently_list;


    LFUCache(int capacity) {
        cap = capacity;
        value = new HashMap<>();
        frequently = new HashMap<>();
        frequently_list =new HashMap<>();
        //此时并没有包含新的元素，只是创建了新的而已
        frequently_list.put(1,new LinkedHashSet<>());
    }

    private void free_space(){
        //空间足够
        if(value.size()<cap)
            return;
        //空间不足够要从min(key)链表中取出一个元素并删除
        int key =(int) frequently_list.get(min).iterator().next();
        System.out.println("evict "+ key);
        /* 三个变量均要删除该变量， */
        value.remove(key);
        frequently.remove(key);
        frequently_list.get(min).remove(key);
    }

    public int get(int key) {
        /* 如果不包含的话 */
        if(!value.containsKey(key)) return -1;
        //如果包含的话，访问一次需要更新一次访问次数
        update(key);
        /* update frequently,and frequently_list;两个东西 */
        return value.get(key);

    }

    private void update(int key){
        int frequency = frequently.get(key);
        //覆盖操作即可
        frequently.put(key,frequency+1);
        //从原来的频率列表中删除，
        frequently_list.get(frequency).remove(key);
        //放进入新的元素列表，如果还没存在相应的频率列表，则应该新创建出来
        if(!frequently_list.containsKey(frequency+1))
            frequently_list.put(frequency+1,new LinkedHashSet<Integer>());
        frequently_list.get(frequency+1).add(key);
        if(frequently_list.get(frequency).size()==0 && frequency==min)
            ++min;
    }

    public void put(int key, int value_) {
        if(value.containsKey(key))
        {
            //执行更新value
            value.put(key,value_);
            //更新frequently 和frequently_list;,
            update(key);
            return;
        }
        //如果是第一次出现的
        //先确保又足够的空间
        free_space();
        value.put(key,value_);
        frequently.put(key,1);
        //第一次出现的情况，即使在这里
        frequently_list.get(1).add(key);
        min = 1;

    }

    public static void main(String[] args) {
        LFUCache cache = new LFUCache(2);

        cache.put(1, 1);
        cache.put(2, 2);
        System.out.println(cache.get(1));       // returns 1
        cache.put(3, 3);    // evicts key 2
        cache.get(2);       // returns -1 (not found)
        System.out.println(cache.get(3));
        cache.put(4, 4);    // evicts key 1.
        System.out.println(cache.get(1));
        System.out.println(cache.get(3));
        System.out.println(cache.get(4));
    }
}
