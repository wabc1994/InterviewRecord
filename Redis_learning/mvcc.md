# MySQL 当中的mvcc

全称是 multic-version-concurrency-control 多版本并发控制

1. **实现目的**


  通过不加锁提供并发控制

----------
2. **设计场景**

 
  多读少写的基本情况

----------
3. **提出背景**

4. **对比学习Java读写锁**

Java也有一个读写锁，读写锁是
5. **过去场景**
  
   我们实现了读读是共享锁，读写是互斥的，写写也是互斥的，如果是  针对多读少写的情况，并发性很低。



6. **存在的问题**

   大部分应用是多读少写的基本情况，如果在这情况也是要加锁的话，并 发性会降低

7. **考虑如何提升**

   不加锁
   
   但是我们如何实现读写也是不加锁，进一步提高并发性能，在这种情况下MVCC就是实现这个东西的，在这种情况就是不加锁如何进行实现该种情况，这就是该种情况，不加锁也就是乐观锁。


8. **mvcc**

innoDB is a multi-versioned storage engine: 保存了修改行版本的相关信息，以支持事务管理相关的并发控制，比如rollback 或者undo

	- 保证各个事务之间互不影响，通过版本号来比较对锁的竞争

删除和更新被定义为一个东西，那就是update，

> Also, a deletion is treated internally as an update where a special bit in the row is set to mark it as deleted.

   

# 乐观锁


乐观锁是mvcc当中的一种思想，如何实现不加锁提高并发性，


乐观锁的本质是消除锁定，理想MVCC难以实现的根本原因在于企图通过乐观锁代替二段提交。

**实现关键**
undo-log 文件
**内部实现**

innodb存储引擎为每行记录保存了三个字段

1. 6字节DB_TRX_ID：标记了最新更新这条行记录的transaction id，每处理一个事务，其值自动+1 
>A 6-byte DB_TRX_ID field indicates the transaction identifier for the last transaction that inserted or updated the row

2. 7个字节DB_ROW_PTR代表roll pointer：指向当前记录项的rollback segment的undo log记录，找之前版本的数据就是通过这个指针

如=
 
3. 6字节的DB_ROW_ID，当由innodb自动产生聚集索引时，聚集索引包括这个DB_ROW_ID的值，否则聚集索引中不包括这个值.，这个用于索引当中


关键

# mvcc使用场景

innodb 存储引擎只有在数据库的第二和第三个隔离级别才会使用mvcc, 

1. 读提交
2. 可重复读

read_commit 由于是读未提交的，所以不存在版本号问题
而serializable 则会对所有读取的行加锁


# 参考链接
[InnoDB存储引擎MVCC实现原理 | 刘正阳](https://liuzhengyang.github.io/2017/04/18/innodb-mvcc/)

[InnoDB Multi-Versioning](https://dev.mysql.com/doc/refman/8.0/en/innodb-multi-versioning.html)