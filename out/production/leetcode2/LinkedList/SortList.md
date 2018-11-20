[题目链接](https://leetcode.com/problems/sort-list/submissions/1)
[csdn](https://blog.csdn.net/qq_28350997/article/details/82918376)
# 题目描述
Sort a linked list in O(n log n) time using constant space complexity.
要求的时间复杂度为O(nlogn), 因此时间
Example 1:

Input: 4->2->1->3
Output: 1->2->3->4
Example 2:

Input: -1->5->3->4->0
Output: -1->0->3->4->5

# 分析
  常见排序方法有很多，插入排序，选择排序，堆排序，快速排序，冒泡排序，归并排序，桶排序等等。。它们的时间复杂度不尽相同，而这里题目限定了时间必须为O(nlgn)，符合要求只有**快速排序(不稳定情况)，归并排序(稳定情况)，堆排序（稳定情况）**，
- 根据单链表的特点，最适于用归并排序。
- 快速排序适用于
- 堆排序适用于树等结构，等大量的数据元素，从中选择小的情况

所以问题就在于如何创造两个合并要用的链表情况，将原始的一条链表分为两个部分
# 归并排序 

 其中归并排序分而治，递归方法情况
   - 分而治之： 将数组分递归处理，如果数组的长度大于等于二，分解开
   - 合并两边： 合并左边和右边的两种情况，
   - 二分查找，找出中间位置值， ** 左边 mid 右边 **
   - 如果左边的长度大于二(low<high), 继续上面的步骤(二分查找的情况，非递归遍历算法同样是 low<mid 就拆分)
   - 同样对右边的情况原始
   - 最后合并两边的情况, merge(左边的链表，中间结果+右边的链表)或者 merge(左边的链表+中间结果, 右边的链表情况)
   - 上面可以将中间的结果划分到左边的链表，或者划分到右边的链表
 ![从上到下](https://www.google.com.tw/search?q=merge+sort&safe=active&source=lnms&tbm=isch&sa=X&ved=0ahUKEwjT7eTlzuTdAhWDXbwKHUYZDCUQ_AUIECgD&biw=2560&bih=1301#imgrc=fvR6j-iI4nMPgM:)
 [二分查找的情况](https://leetcode.com/problems/search-insert-position/hints/)
[归并排序](https://github.com/wabc1994/algo4/blob/master/merger_sort.cpp)
[对归并算法的解释](https://www.hackerearth.com/practice/algorithms/sorting/merge-sort/tutorial/)
## 
## 链表归并排序标准代码

```Java
public ListNode merge(ListNode h1, ListNode h2) {
        ListNode hn = new ListNode(Integer.MIN_VALUE);
        ListNode c = hn;
        while (h1 != null && h2 != null) {
            if (h1.val < h2.val) {
                c.next = h1;
                h1 = h1.next;
            }
            else {
                c.next = h2;
                h2 = h2.next;
            }
            c = c.next;
        }
        if (h1 != null)
            c.next = h1;
        if (h2 != null)
            c.next = h2;
        return hn.next;
    }
}
```
#  将归并排序应用于链表排序
  
类似于归并排序应用于数组对情况，
** 将链表分为分为两半的情况，要考虑的问题如何分，查找中间节点的情况**
参考归并排序
将代码分为三部分情况
1.  寻找中间节点情况 ，*快慢指针*
2.  递归对链表的左右部分进行进行1
3.  合并两边对链表


##  1. 寻找中间节点情况 ，快满指针
快慢指针这种情况，类似于链表种找环的情况
### 注意递归出口

当只有一个节点时或者为空，是递归的出口 
>链表长度为1，或者为空  if(head.next==null|| head==null)

- [链表是否有环的情况](https://leetcode.com/problems/linked-list-cycle/description/)
- [链表环的起点问题](https://leetcode.com/problems/linked-list-cycle/description/)

>slow = head, fast =head
>slow = slow.next. fast = fast.next.next(一个走两步，一个走一个)
>然后把前半部末尾设置为NULL ， pre 是slow 的前驱，最后让 pre.next = null 
>head ->.......-> pre (前半部分链表)， slow ->........ fast 后半部分连标

![图片](https://img-blog.csdn.net/20181001195825582?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzI4MzUwOTk3/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

// step 1. cut the list to two halves
```java 
ListNode pre =null , slow =head, fast = head;
while(fast!=null && fast.next!=null){
     pre  = slow;
     slow = slow.next;
     fast  = fast.next.next;
}
pre,next = null;
```
## 2.递归对链表的左右部分进行判断
** 对 head->.... pre 左半部分执行step1 **

** 对 slow->..... pre 左半部分执行 step2**

```c++
// step 2. sort each half
    ListNode l1 = sortList(head);
    ListNode l2 = sortList(slow);
```


>ListNode  l1 = step1(head);
>ListNode  l2 = step2(slow);
## 3. 合并两边对链表 
merge(ListNode l1, ListNode l2);

```c++
// step 3. merge l1 and l2
    return merge(l1, l2);
```

回到上面以及查看合并代码段即可看到上述情况


#最终代码

````c++
public class Solution {
  
  public ListNode sortList(ListNode head) {
    if (head == null || head.next == null)
      return head;
        
    // step 1. cut the list to two halves
    ListNode prev = null, slow = head, fast = head;
    
    while (fast != null && fast.next != null) {
      prev = slow;
      slow = slow.next;
      fast = fast.next.next;
    }
    
    prev.next = null;
    
    // step 2. sort each half
    ListNode l1 = sortList(head);
    ListNode l2 = sortList(slow);
    
    // step 3. merge l1 and l2
    return merge(l1, l2);
  }
  
  ListNode merge(ListNode l1, ListNode l2) {
    ListNode l = new ListNode(0), p = l;
    
    while (l1 != null && l2 != null) {
      if (l1.val < l2.val) {
        p.next = l1;
        l1 = l1.next;
      } else {
        p.next = l2;
        l2 = l2.next;
      }
      p = p.next;
    }
    
    if (l1 != null)
      p.next = l1;
    
    if (l2 != null)
      p.next = l2;
    
    return l.next;
  }

}
```











