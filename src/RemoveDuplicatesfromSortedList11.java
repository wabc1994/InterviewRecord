
public class RemoveDuplicatesfromSortedList11 {
    /**
     * 从有序链表中删除相同元素的元素，一个都不留
     * 1-2-2-3-3-4-5
     * 1-4-5
     * @param head
     * @return
     */
     public  static ListNode solution(ListNode head){
        if(head==null|| head.next==null) {
            return head;
        }
        ListNode dump = new ListNode(0);
        dump.next =  head;
        boolean mark  = false;
        ListNode current =  head, pre  = dump;
        while(current!=null && current.next!=null){
            //碰到有一系列相同的元素，找到最后一个相似的元素
            while(current!=null && current.next!=null && current.val==current.next.val){
                mark = true;
                current=current.next;
            }
            //清楚掉没用的元素
            if(mark){
                ListNode new_current = current.next;
                pre.next = new_current;
                current = new_current;
                mark = false;
            }
            //不需要作出任何改变，不需要删除任何元素,正常情况下的
            if( current!=null && current.next!=null && current.val!=current.next.val){
                pre = current;
                current = current.next;
            }
        }
        return dump.next;
    }

    public static void main(String[] args) {
        ListNode head= new ListNode(5);
        ListNode cur = head;
        int [] test ={1,1,2,2,4,5,5,6,6};

        for(int i=0;i<test.length;++i){
            ListNode new_node =new ListNode(test[i]);
            cur.next = new_node;
            cur = new_node;
        }
        RemoveDuplicatesfromSortedList11.solution(head);
    }
}
