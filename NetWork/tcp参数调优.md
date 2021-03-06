# tcp参数调优该种情况

在tcp设计当中我们可以对下面的参数进设计

- 开启SYN Cookies。当出现SYN等待队列溢出时，启用cookies来处理，可防范少量SYN攻击，默认为0，表示关闭；
net.ipv4.tcp_syncookies = 1

- 开启重用。允许将TIME-WAIT sockets重新用于新的TCP连接，默认为0，表示关闭；
net.ipv4.tcp_tw_reuse = 1

- 开启TCP连接中TIME-WAIT sockets的快速回收，默认为0，表示关闭；
net.ipv4.tcp_tw_recycle = 1

- 系统默认的TIMEOUT时间。
net.ipv4.tcp_fin_timeout = 5

- 当keepalive起用的时候，TCP发送keepalive消息的频度。缺省是2小时，改为20分钟(20*60s)
net.ipv4.tcp_keepalive_time = 1200

- 表示用于向外连接的端口范围。缺省情况下很小：32768到61000，改为10000到65000
net.ipv4.ip_local_port_range = 10000 65000

- SYN队列的长度，默认为1024，加大队列长度为8192，可以容纳更多等待连接的网络连接数
net.ipv4.tcp_max_syn_backlog = 8192


- 系统同时保持TIME_WAIT的最大数量，如果超过这个数字，TIME_WAIT将立刻被清除并打印警告信息。默认为180000，改为5000
net.ipv4.tcp_max_tw_buckets = 5000

1. net.ipv4.tcp_keepalive= 7200 保持长连接状况下的信息等情况

tcp_abort_on_overflow 连接队列满了怎么办在该种情况下面，


tcp_keepalive_intvl/:75
默认值为75
探测消息发送的频率，乘以tcp_keepalive_probes就得到对于从开始探测以来没有响应的连接杀除的时间。默认值为75秒，
也就是没有活动的连接将在**大约11分钟**以后将被丢弃。(对于普通应用来说,这个值有一些偏大,可以根据需要改小.特别是web类服务器需要改小该值,
**15是个比较合适的值。**

![](https://github.com/wabc1994/InterviewRecord/blob/master/NetWork/pic/tcp:ip%20%E5%8F%82%E6%95%B0%E8%B0%83%E4%BC%98.png)

![](https://github.com/wabc1994/InterviewRecord/blob/master/NetWork/pic/tcp%E8%BF%9E%E6%8E%A5%E5%8F%82%E6%95%B0%E8%B0%83%E4%BC%98.png)
