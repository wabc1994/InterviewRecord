package LinkedList;

import java.util.List;

public class LinkedListCycle {

    public boolean hasCycle(ListNode head) {
        if(head==null) return false;
        ListNode slow = head;
        ListNode fast = head;
        while(fast.next.next!=null && fast.next!=null)
        {
            //如果没有环的话,逐步减少差距，总会在一个地方相遇到的情况
            slow = slow.next;
            fast = fast.next.next;
            if(head==slow)
                return true;

        }
        return false;
    }

    //判断环的起始位置，如果有环的话，如果没有环，则直接返回null即可
    public ListNode detectCycle(ListNode head) {
        if(head==null|| head.next==null) return null;
        ListNode walker= head, runner = head;
        boolean mark = false;
        while(runner.next!=null && runner.next.next!=null){
            walker = walker.next;
            runner= runner.next.next;
            if(runner==walker)
                mark = true;
        }
        //没有环的情况
        if(!mark) return null;
        //有环的情况下两者相遇在环上，让runner停留在第一次相遇的地方
        walker = head;
        while(walker!=runner){
            walker = walker.next;
            runner = runner.next;
        }
        return walker;
    }


}
