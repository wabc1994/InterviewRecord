# linux问题排查 - 高cpu占用率的进程和线程
# 问题来源
[参考链接](https://blog.csdn.net/hrn1216/article/details/51426741)
# 1. 简介
 一个程序，完成它预设的功能，并不能说明它是一个优良的程序。好的程序，应该是对资源的合理利用，亦或是
用更少的资源（使用合理的算法），实现更多有效的产出。
      影响程序的资源一般而言分为4个：*CPU、内存、IO、网络(netstat)*。本文着重讲解一下在linux系统下，如何查看高CPU占用率的进程，线程。

ps -ax #查看所有的进程pid
kill -9 pid //杀死一个某个pid
查看线程的详细信息：cat /proc/进程号/task/线程号/status

# top 
# ps
ps -aux 常用形式
# jstack 

# linux问题排查 - 高cpu占用率的进程和线程
# 问题来源
[参考链接](https://blog.csdn.net/hrn1216/article/details/51426741)
## 1. 简介
 一个程序，完成它预设的功能，并不能说明它是一个优良的程序。好的程序，应该是对资源的合理利用，亦或是
用更少的资源（使用合理的算法），实现更多有效的产出。
      影响程序的资源一般而言分为4个：*CPU、内存、IO、网络(netstat)*。本文着重讲解一下在linux系统下，如何查看高CPU占用率的进程，线程。

ps -ax #查看所有的进程pid
kill -9 pid //杀死一个某个pid
查看线程的详细信息：cat /proc/进程号/task/线程号/status
## top
：*CPU、内存、IO、网络(netstat)*。 查看系统的总体性能
## ps
# 本文介绍linux如何查看端口被哪个进程占用的方法：
在Linux使用过程中，需要了解当前系统开放了哪些端口，并且要查看开放这些端口的具体进程和用户，可以通过netstat命令进行简单查询
   1.lsof -i:端口号（查看端口具体被那个进程占用）
   2.netstat -anp | grep protno
  都可以查看指定端口被哪个进程占用的情况
## 详细使用如下的情况

1.netstat -tunlp用于显示tcp，udp的端口和进程等相关情况，如下图：

##  如何关闭某个端口
1、通过杀掉进程的方法来关闭端口

每个端口都有一个守护进程，kill掉这个守护进程就可以了

每个端口都是一个进程占用着，

第一步、用下面命令

netstat -anp |grep 端口

找出占用这个端口的进程，
lsof -i 端口号

第二步、用下面命令

kill -9 PID 

杀掉就行了
## netstat 使用总结
1. 命令使用参数情况
>-t : 指明显示TCP端口
 -u : 指明显示UDP端口
 -l : 仅显示监听套接字(所谓套接字就是使应用程序能够读写与收发通讯协议(protocol)与资料的程序)
 -p : 显示进程标识符和程序名称，每一个套接字/端口都属于一个程序。
 -n : 不进行DNS轮询，显示IP(可以加速操作)

2. 显示当前服务器上所有端口及进程服务，与grep结合可查看某个具体端口及服务情况
>netstat -ntlp   //查看当前所有tcp端口·
 netstat -ntulp |grep 80   //查看所有80端口使用情况·
 netstat
 
 
 Linux内存，CPU ,网络等性能分析
 
 # top,ps,free 
 # ps
 ps aux 是其中常用的命令行参数
 
 [ps详解](https://blog.csdn.net/du_minchao/article/details/51697704)
 1. a 代表所有的
 # /proc/pid/status
 # /proc/meminfo
 # 参考链接
 
 [Linu