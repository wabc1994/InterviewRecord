
# Shuffle 洗牌算法
也可以这样理解， Shuffle描述着数据从map task输出到reduce task输入的这段过程。 


整个mapreduce 的过程可以分为

MAP-- shuffle(排序)-- combine(组合)--Reduce

主要完成三个功能吧

多个map task 分布在不同节点情况，

1. 完整地从map task 端拉取数据到reduce
2. 在跨节点拉取数据时，尽可能减少对带宽的不必要消耗
3. 减少磁盘I/0 对task执行的影响。采用环形缓冲区的情况


将map端的结果移动到reduce端是图通过http 完成的

## map 端过程shuffle
在Map端的shuffle过程是对Map的结果进行分区、排序、分割，然后将属于同一划分（分区）的输出合并在一起并写在磁盘上，最终得到一个分区有序的文件，分区有序的含义是map输出的键值对按分区进行排列，具有相同partition值的键值对存储在一起，每个分区里面的键值对又按key值进行升序排列（默认）。



2. 写入环形缓冲区，
3. 执行溢出写spill,达到阈值之后开始spill，在该过程当中进行分区，排序(partitions,sort, and spill to disk)
4. 归并mergez(merger on disk)



**涉及到的排序算法**
1. 快速排序
2. 归并排序算


## reduce端shuffle

在Reduce端，shuffle主要分为复制Map输出、排序合并两个阶段。
1. 复制
2. 归并merge，合并溢写的文件，采用堆排序的算法，生成一些更大的文件，生成一些更大的文件
3. reduce
