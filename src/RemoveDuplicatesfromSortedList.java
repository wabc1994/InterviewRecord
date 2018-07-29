public class RemoveDuplicatesfromSortedList {
    /**
     * 从有序链表中删除相同多余的元素 1-1-2-2-2 -3 -4-4 -5
     * 得到发1-2-3-4-5
     * @param head
     * @return
     */
    public ListNode solution_1(ListNode head){
     if(head==null|| head.next==null) {
        return head;
    }

    ListNode dump = new ListNode(0);
    dump.next =  head;
    ListNode current =  head, pre  = dump;
        while(current!=null &&current.next!=null ) {
            if (current.val == current.next.val) {
                pre.next = current.next;
                current = pre.next;
            } else {
                pre = current;
                current = current.next;
            }
        }
        return dump.next;
    }

    public ListNode solution_2(ListNode head){
        if(head==null||head.next==null) return head;
        head.next = solution_2(head.next);
        return head.val==head.next.val ? head.next:head;

    }
}
