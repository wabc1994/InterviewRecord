package LinkedList;

public class SortList {
    public ListNode sortList(ListNode head) {
        if(head==null || head.next==null) {return head;}
        //split the listnode into two part ,the head part sorted, the p part awaiting for sorting
        //find the position in the sorted head part,  to insert;

        ListNode s = new ListNode(0);
        s.next =head;
        ListNode p = head.next;
        head.next = null;
        while(p!=null){
            //save the next waiting position in the unsorted part
            ListNode r = p.next;
            //find the position insert into
            ListNode  pre= s;

            //if we need to insert into the first position of the listnode
            if(p.val<=pre.next.val)
            {
                p.next = pre.next;
                pre.next = p;
            }
            // the resting   insert position besides the head place;
            while(pre.next!=null && pre.next.val<p.val)
            {
                //find the right the position to insert;
                pre =pre.next;
            }

            p.next= pre.next;
            pre.next= p;
            p = r;
        }
        return s.next;


    }

    void print(ListNode head){
        while(head!=null){
            System.out.println(head.val);
            head = head.next;
        }
    }
    public static void main(String[] args){
        SortList test = new SortList();

        ListNode head = new ListNode(4);
        head.next = new ListNode(2);
        head.next.next= new ListNode(1);
        head.next.next.next = new ListNode(3);
        test.print(head);
        System.out.println("进行排序后");
        ListNode  res =test.sortList(head);
        test.print(res);

    }
}
