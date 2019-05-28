# mysql 当中checkpoint机制

检查点主要有两个作用

1. 第一是fuzzy checkpointing, 将data buffer 当中的dirty page 刷新 磁盘当中data file，
checkpoint是为了定期将db buffer的内容刷新到data file。当遇到内存不足、db buffer已满等情况时，需要将db buffer中的内容/部分内容（特别是脏数据）转储到data file中。在转储时，会记录checkpoint发生的”时刻“

2.  Crash Recovery决定redo多少 redo log当中的文件，避免redo 所有的重做日志文件，在最近一次checkpoint之前的数据不进行redo, 在之后的数据才进行reod,



一个重做日志链 redo log file采用 LSN 日志序号问题

[![E8eQKS.md.jpg](https://s2.ax1x.com/2019/04/30/E8eQKS.md.jpg)](https://imgchr.com/i/E8eQKS)

最近的一次checkpoint设置为4,4之前的日志不需要重做，4之后的日志需要重做即可

checkpoint-lsn机制

当数据库发生宕机时，数据库不需要重做所有的日志，因为Checkpoint之前的页都已经刷新回磁盘。数据库只需对Checkpoint后的重做日志进行恢复，这样就大大缩短了恢复的时间。