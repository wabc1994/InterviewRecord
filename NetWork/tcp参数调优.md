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