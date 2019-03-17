# mysql日志系统

主要分为服务器层面的日志和存储引擎层的日志，先写日志后落入磁盘的

## binlog

binlog(二进制日志)主要是为了mysql服务器层面,二进制日志主要记录数据库的变化情况，因此可以用作主从库的同步。内容主要包括数据库所有的更新操作，use语句、insert语句、delete语句、update语句、create语句、alter语句、drop语句。用一句更简洁易懂的话概括就是：所有涉及数据变动的操作，都要记录进二进制日志中。

>主要作用是主从复制方面，配合relay-log 中继日志

## redo重做日志

主要是为了异常重启后的恢复

## 参考链接

[redo log 与 binlog](https://www.linuxidc.com/Linux/2018-11/155431.htm)