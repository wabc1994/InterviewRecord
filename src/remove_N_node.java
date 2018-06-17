class Node{
    public int item;
  public   Node next;
    public Node(int x)
    {
        this.item = x;
        this.next = null;
    }

}
/*两个指针，最后一个指针最后为空，倒数第N个结点，则是走N-1步，当前面的指针为空的时候就停止
* */
public class remove_N_node {
    class Solution {
        public Node removeNthFromEnd(Node head, int n) {
            //为了避免空指针遗产的情况，
            if(head==null) return null;
            Node p=head, pre=null,q=head;
            int count = n-1;
            while(count>=0)
            {
                q=q.next;
                count--;

            }
            while(q!=null)
            {
                pre = p;
                p=p.next;
                q=q.next;
            }
            if(p.next!=null)
                pre.next=p.next;
            else
                pre.next=null;
            return head;


        }
    }
    public Node removeNthFromEnd(Node head, int n)
    {
        //像这样的问题为了避免空指针异常的情况，都是要进行一定的添加一个0的头结点
        Node start =new Node(0);
        Node slow = start, fast= start;
        slow.next = head;
        for(int i=1;i<=n+1;i++)
        {
            fast = fast.next;

        }
        while(fast!=null)
        {
            slow = slow.next;
            fast = fast.next;
        }
        slow.next =slow.next.next;
        return start.next;
    }
}
