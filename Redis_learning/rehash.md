# redis 重新rehash

Redis 当中的hash 键值对采用字典存储，然后字典里面具有两个dictht， hashtable0 和
hashtable1 **(在刚开始的时候是没有分配空间的，)**， 其中hashtabl1 主要是为了扩容的时候使用，如果元素过多的话，我们要对hash 进行扩容，
将hashtable1里面的元素迁移到hashtable 2里面


**扩容为两倍，说白了就是该种机制，扩容为两倍的情况**

**rehashindex**
>再hash的索引情况

这样情况下就出现了两个问题

1. 如果hashtable0 里面的元素比较少的话，我们可以直接一次进行元素的迁移
2. 如果hashtable0 里面的元素比较多的