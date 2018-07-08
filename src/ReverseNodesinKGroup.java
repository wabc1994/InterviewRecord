import java.util.List;

public class ReverseNodesinKGroup {
    public ListNode solution_1(ListNode head, int k) {
    //递归方法解决
        //下面的代码无法解决末尾的不足k的结点，
        //例如 1 2 3 4 5 k=3 逆转为 3 2 1 5 4 但正确的结果为 3 2 1 4 5
        if(head==null|| head.next==null|| k<2)
                return head;

        int count =0;
        //下面的代码是先构建一个空的结果链表，pre，current 下面的链表代表插入的结点
        ListNode current = head;
       //后面
        ListNode prev =null,next=null;

        //判断如果结点数目<k 的话不用进行下面的步骤，可以直接返回head即可
        if(!is_enough_k(head,k))
            return head;
//将链表分开成两部分，一部分是已经做好的了，一部分是接下来要处理的链表，其中next指向没处理的那部分链表，current代表要处理的那个结点
        //其中这里的步骤是先构造一个空的结点（代表有序的，然后在逐步添加current）头插入法
        while(count<k &&current!=null)
        {
            //保存current的后记结点
            next=current.next;
            current.next =prev;
            prev = current;
            current=next;
            ++count;
        }
        if(is_enough_k(next,k))
                //下面有足够长度的k个结点接上来
            head.next= solution_1(next,k);
        else
            head.next= next;


            //after the while now the， next point to the next-while, and head point to the tail of head
        return prev;
    }
    private boolean is_enough_k(ListNode next_group,int k)
    {
        //下面如果next_group为空的话直接不用做下下去了，直接return false
        if(next_group==null) return false;
        int count=0;
        while(next_group!=null&&count<k)
        {
            count++;
            next_group= next_group.next;
        }
        //代码进行简化，把if else 语句变成=?:
        return count ==k  ? true : false;

    }

    // try so solve the  the 4 5 problem


   //solution_2是solution_1的优化，其中每次找到每一个group后再逆转，不像solution_1每一结点就
  public ListNode solution_2(ListNode head, int k )
    {
        if(head==null|| head.next==null||k<2)
            return head;
        ListNode next_group = head;
        int count=0;
        while(count<k)
        {
            //不满足直接返回头结点
            if(next_group==null) return head;
            ++count;
            next_group = next_group.next;
        }
        ListNode pre = null, cur = head;
        while(cur!=next_group)
        {
            ListNode next = cur.next;
            cur.next = pre;
            pre =cur;
            cur = next;
        }
        //solution_3(next_group,k)==next_pre;
        head.next = solution_2(next_group,k);
    //pre代表每一次的头结点，第一个结点
        return pre;
    }
    //迭代版本如何做
}
