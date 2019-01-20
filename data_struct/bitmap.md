# 位图
## Redis当中的bitmap
### 统计日活跃人数
传统数据库是怎样做的？
count 并进行group by 等才进行统计的
### 签到功能

512Mb= 2^29bytes =2^32bit

offset 代表偏移多少位的操作
offset/8/1024/1024)MB


[大量数据去重：Bitmap和布隆过滤器(Bloom Filter) - zdxiq000的专栏 - CSDN博客](https://blog.csdn.net/zdxiq000/article/details/57626464)