# tcp 的半连接和全连接

三次握手分别对应服务器和客户端的三次什么连接，socket 编程之函数系列

之前的准备工作函数 建立一个套接字socket(AF_INTE,SOCK_STREAM,0) 设置客户端和服务器端的地址

struct sockaddr_in server_addr 


1. 客户端connect() 对应发送一个syn 

2. listen(int fd, int backlog) 服务器端在某socket 上面进行监听，backlog指明accept queue队列的大小， 半连接队列的大小是查看
>cat /proc/sys/net/ipv4/tcp_max_syn_backlog 可以查看linux tcp/ip协议参数，默认是1024个，如果服务器负载比较重的话，增加该值显然是有好处的，可调整到2048

在真实的代码环境当中

2. accept()不参与三次握手连接的，是从建立连接后的队列(accept queue)里面进行一个取出一个连接

# linux内核tcp/ip 参数
- net.ipv4.tcp_syncookies = 1
>表示开启SYN Cookies。当出现SYN等待队列溢出时，启用cookies来处理，可防范少量SYN攻击，默认为0，表示关闭； 

- cat /proc/sys/net/ipv4/tcp_abort_on_overflow 
>tcp_abort_on_overflow 为0表示如果三次握手第三步的时候全连接队列满了那么server扔掉client 发过来的ack，1 表示发送 RST 通知 client，相应的，client 则会分别返回 read timeout 或者 connection reset by peer


# 为何是三次连接而不是两次或者四次

- SYN 同步序号
- ACK 同步序号

为何需要序号，从可靠性这个角度来回答？

> tcp 的可靠含义： 接受方收到的数据是完整的，有序的，无差错的
> udp 不可靠的含义： 接受方收到的数据可能存在部分丢失，顺序也不一定保证


## 为何不是两次

1. 假设是两次握手的出现报文消失，导致重新发送，然后正常连接，这样client就发送了两次报文，只有一次连接

2. 假设已经失效的报文没有消失，只是网络发生超时，然后再次到达服务器，服务器以为又是一次正常连接，然后为这次连接分配资源等，但是客户端又迟迟不发送数据过来，导致出现

[这主要是为了防止已失效的连接请求报文段突然又传送到了B(服务器端)，因而产生错误。](https://blog.csdn.net/kzadmxz/article/details/73998537)

## 为何不是四次

三次握手

# 为何是四次挥手
1. 首先要理解是全双工通信，双方都可以发送数据，一个来回只能确认关闭一方不能发送数据，关闭客户端需要两次挥手，关闭服务器同样需要两次挥手，那么加起来就是四次，这样就可以安全得关闭两方，都不再发送数据了。
那四次分手又是为何呢？

- TCP协议是一种面向连接的、可靠的、基于字节流的运输层通信协议。

- TCP是全双工模式，这就意味着，当主机1发出FIN报文段时，只是表示主机1已经没有数据要发送了，主机1告诉主机2，它的数据已经全部发送完毕了；

- 但是，这个时候主机1还是可以接受来自主机2的数据；当主机2返回ACK报文段时，表示它已经知道主机1没有数据发送了，但是主机2还是可以发送数据到主机1的；当主机2也发送了FIN报文段时，这个时候就表示主机2也没有数据要发送了，就会告诉主机1，我也没有数据要发送了，之后彼此就会愉快的中断这次TCP连接。

# SYN cookies 抵御SYN flood 攻击

SYN Cookie， 
 

SYN Cookie原理由D.J. Bernstain和Eric Schenk提出。

SYN Cookie是对TCP服务器端的三次握手做一些修改，专门用来防范SYN Flood攻击的一种手段。它的原理是，在TCP服务器

接收到TCP SYN包并返回TCP SYN + ACK包时，不分配一个专门的数据区，而是根据这个SYN包计算出一个cookie值。这个

cookie作为将要返回的SYN ACK包的初始序列号。当客户端返回一个ACK包时，根据包头信息计算cookie，与返回的确认序列
号(初始序列号 + 1)进行对比，如果相同，则是一个正常连接，然后，分配资源，建立连接。

**核心**

服务器为SYN+ACK 分配数据区等资源变成计算一个cookies作为初始序号发送回给客户端，客户端发送ack 要有一个cookies+1的序号作为确认，服务器才认为是一个有效的链接

# 链接

[关于TCP 半连接队列和全连接队列](http://jm.taobao.org/2017/05/25/525-1/)

[大并发下listen的连接完成对列backlog太小导致客户超时，服务器效率低下](https://blog.csdn.net/lizhi200404520/article/details/6981272)

[理解 Linux backlog/somaxconn 内核参数](https://jaminzhang.github.io/linux/understand-Linux-backlog-and-somaxconn-kernel-arguments/)

[listen() 函数 backlog 的含义](https://blog.csdn.net/yangbodong22011/article/details/60399728)

[How TCP backlog works in Linux](http://veithen.io/2014/01/01/how-tcp-backlog-works-in-linux.html)