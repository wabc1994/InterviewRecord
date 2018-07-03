import java.util.HashMap;
import java.util.Map;
import java.util.Set;
class Double_Node{
    int key;
    int value;
    Double_Node next;
    Double_Node prev;
    public Double_Node() {
    }
    public Double_Node(int key, int value)
    {
        this.key = key;
        this.value = value;
    }
}
//不要使用map.size()==
public class LRU {
    private int capacity ;
    private int numbers = 0;
    Map<Integer, Double_Node> map = null;
    //map 中记录的是无顺序的，跟插入顺序是无关的。
    Double_Node head = null;


    public LRU(int capacity) {
        //构造函数，初
        this.capacity = capacity;
        map = new HashMap<>();
        //创建一个空的头结点，key 和value 均为空，其中
        head = new Double_Node(); //head ==null 与new Double_Node()
        //不给初始值是一样的,双向循环链表
        head.next = head.prev=head;

    }

    public int get(int key) {
        if (!map.containsKey(key))
            return -1;
        //对某个元素进行访问的话，need to do two things one is return the value;
        //the other is to get the value to the doublelinkedlist  the first node;
        moveToFront(map.get(key));
        return map.get(key).value;
    }

    public void put(int key, int value) {

        //if the key have already, we just need to update the value within the
        // key ,  and then move the node to the first place;
        if (map.containsKey(key))
        {
            //just update value;
            map.get(key).value = value;
            moveToFront(map.get(key));
        }
        //if map does not contain the key ,then we
        // use the construct to get a newNode,and then put the node into
        // the map , and finally move the node to Doublelinkedlist 's first place;
        else {
            //insert a new node into the map, we need
            // insure the map has the needing space;
            freespace();
            Double_Node node = new Double_Node(key, value);
            ++this.numbers;
            map.put(key, node);
            add_to_first(node);
            }
    }
    // the main function

    private void freespace() {
        //delete the final Double_Node, head.prev.prev 代表到数第二个元素，
        //head.prev 代表到数第一结点，也是要删除的结点
        if (this.numbers == capacity) {
            map.remove(head.prev.key);
            Double_Node tail = head.prev.prev;
            tail.next = head;
            head.prev =tail;
            //remove a node from double_linked_list,and the numbers should
            --this.numbers;

        }
    }


    private void moveToFront(Double_Node newNode) {
        //删除结点之间的连接关系,分两块处理，先删除关系
        Double_Node pre = newNode.prev;
        Double_Node next = newNode.next;
        pre.next = next;
        next.prev = pre;
        add_to_first(newNode);
    }

    private void add_to_first(Double_Node newNode) {
        //get a node to the first place, two kind situation, update /insert
        //移动到前面的结点关系
        //先处理右边的关系，
        newNode.next = head.next;
        head.next.prev = newNode;
        //再处理左边的元素情况
        head.next = newNode;
        newNode.prev = head;
    }



    public static void main(String[] args)
    {
        LRU cache = new LRU(2);
        cache.put(1, 1);
        cache.put(2, 2);
        System.out.println(cache.get(1));      // returns 1
        cache.put(3, 3);    // evicts key 2
        System.out.println(cache.get(2));
        cache.put(4, 4);    // evicts key 1
        Set<Integer>is = cache.map.keySet();
        System.out.println(is);
        System.out.println(cache.get(1));
        System.out.println(cache.get(3));
        System.out.println(cache.get(4));
        System.out.println(cache.get(0));
        }
}

