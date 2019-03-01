# other choice db
读写性能，redis、leveldb、rockdb是key-value，存储结构的不同
存储结构


## I/0随机和顺序

磁盘的顺序读写是快于随机读写的


## mysql
基于磁盘的B+树存储, 索引不同存储

首先来回答一个问题：为什么在磁盘中要使用b+树来进行文件存储呢？
原因还是因为树的高度低得缘故，磁盘本身是一个顺序读写快，随机读写慢的系统，那么如果想高效的从磁盘中找到数据，势必需要满足一个最重要的条件：减少寻道次数。

[深入理解 MySQL 底层实现](https://blog.csdn.net/GitChat/article/details/78787837)

## redis
本质基于内存的hash存储，

# 有了redis， 为何还需要leveldb?
与MySQL 相比，如果是大量的修改操作，比如删除和根更新和插入等，B+树性能会降低， 磁盘随机读增加，
##  leveldb
想法来源bigtable，大数据存储方面hbase方面等
Log-Structured Merge Tree(LMT)日志结构化合并树，核心是牺牲部分读性能， 增加写方面的性能


**特点**
1. key-value  <key,value>
2. write more than read
3. write to memory 
4. merge when read
5. sorted by key 树当中的每个节点都是有序的安装

**缺陷**
1. 同时只能有个线程操作这个数据库,单进程单线程模型
2. 
## rocksdb
为何要对leveldb进行改进?

Google公司的leveldb 存在什么问题


# log structure merge tree

几月
# 参考链接
[LSM树（Log-Structured Merge Tree）存储引擎 ](https://blog.csdn.net/u014774781/article/details/52105708)

