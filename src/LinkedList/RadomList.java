package LinkedList;

/**
 * @ClassName RadomList
 * @Description TODO
 * @Author coderlau
 * @Date 2018/12/8 4:35 PM
 * @Version 1.0
 **/

class RandomListNode {
      int label;
     RandomListNode next, random;
     RandomListNode(int x) { this.label = x; }
}

public class RadomList {
    public RandomListNode copyRandomList(RandomListNode head){
        if(head==null)
        { return null;}
        RandomListNode new_list= copy(head);

        // 新旧链表节点一同进行处理即可
        RandomListNode current = new_list;
        RandomListNode  current_old = head;
        while(current!=null){
            RandomListNode random = current_old.random;
            current.random=find(head, new_list, random);
            current= current.next;
            current_old = current_old.next;
        }
        return   new_list;
    }


    private RandomListNode copy(RandomListNode  old){
        // 采用头插入法建立一个新的链表情况
        RandomListNode  new_linklist =new RandomListNode(old.label);
        RandomListNode  r= new_linklist;
        RandomListNode current= old.next;
        while(current!=null){
            //中间结果层次的问题
            RandomListNode tmp = new RandomListNode(current.label);
            // 添加到尾巴的元素部分、
            r.next = tmp;
            // 更新尾巴部分的代码情况
            r = tmp;
        }
        return new_linklist;
    }

    // 在这里面的查找是从头开始查找的

    private RandomListNode find(RandomListNode old, RandomListNode  new_list , RandomListNode random){
        // 定义两个遍历情况
        RandomListNode current_old= old;
        RandomListNode current_new = new_list;
        while(current_old!=null && current_new!=null){
            if(current_new==random){
                return current_new;
            }
            current_new =  current_new.next;
            current_old = current_old.next;

        }
        return null;
    }


    //针对上述代码的一个优化方案情况

}

