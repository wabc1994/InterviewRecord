 class ListNode {
       int val;
      ListNode next;
      ListNode(int x) { val = x; }
}
public class merge_k_linked {
    public   ListNode solution_1(ListNode [] nums)
    {
         int n_num = nums.length;
         return binary(nums, 0, n_num-1);
    }
    ListNode binary(ListNode[] nums ,int start,int last)
    {
        if(last==start) return nums[start];
        if(start<last)
        {

            int mid = (start+last)/2;
            ListNode L1= binary(nums,start,mid);
            ListNode L2=binary(nums,mid+1,last);
             return merge(L1,L2);
        }else
            return null;
    }
    ListNode merge(ListNode l1,ListNode l2)
    {
        ListNode result =new ListNode(0);
        ListNode r= result;
        while(l1!=null &&l2!=null)
        {
            if(l1.val<l2.val)
            {
                r.next=l1;

                l1=l1.next;
            }
            else
            {
                r.next = l2;
                l2= l2.next;

            }
            r=r.next;
        }
        if(l1!=null)
            r.next= l1;
        if(l2!=null)
            r.next =l2;
        return result.next;

    }
}
