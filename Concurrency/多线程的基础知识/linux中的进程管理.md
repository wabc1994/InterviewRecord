# 子进程
父进程fork() 后创建一个子进程，得到父亲一样的资源 ，文件描述符等情况，子进程*完成任务后进入dead状态*；但此时子进程并没有完全从进程树中清除掉，还是占用着系统的资源，比如进程号pid； 子进程向父亲进程发送信号，等待确认；如果父亲进程确定子进程的终止信息，然后子进程才真正算是从进程树中完成清楚掉，释放占用的资源等情况。如果父亲进程选择忽略子进程发送过来的确认信号，或者没有调用wait() 或者 waitid（） , 子进程就变成来僵尸进程（linux系统默认的进程号是 0到 1023，容量是 1024） 
# 僵尸进程定义？
查看僵尸进程，利用命令ps，可以看到有标记为Z(zombie)的进程就是僵尸进程。
     >To understand what a zombie process is and what causes zombie processes to appear, you’ll need to understand a bit about how processes work on Linux.
     When a process dies on Linux, it isn’t all removed from memory immediately — its process descriptor stays in memory (the process descriptor only takes a tiny amount of memory). The process’s status becomes EXIT_ZOMBIE and the process’s parent is notified that its child process has died with the SIGCHLD signal. The parent process is then supposed to execute the wait() system call to read the dead process’s exit status and other information. This allows the parent process to get information from the dead process. After wait() is called, the zombie process is completely removed from memory.

>This normally happens very quickly, so you won’t see zombie processes accumulating on your system. However, if a parent process isn’t programmed properly and never calls wait(), its zombie children will stick around in memory until they’re cleaned up.
Utilities like GNOME System Monitor, the top command, and the ps command display zombie processes.



# 僵尸进程的危害？
僵尸进程会占用系统资源，如果很多，则会严重影响服务器的性能；
孤儿进程不会
>Zombie processes don’t use up any system resources. (Actually, each one uses a very tiny amount of system memory to store its process descriptor.) However, each zombie process retains its process ID (PID). Linux systems have a finite number of process IDs – 32767 by default on 32-bit systems. If zombies are accumulating at a very quick rate – for example, if improperly programmed server software is creating zombie processes under load — the entire pool of available PIDs will eventually become assigned to zombie processes, preventing other processes from launching.
However, a few zombie processes hanging around are no problem – although they do indicate a bug with their parent process on your system.
# 僵尸进程产生的原因?

- 子进程结束后向父进程发出SIGCHLD信号，父进程默认忽略了它
- 父进程没有调用wait()或waitpid()函数来确认子进程的结束
- 网络原因有时会引起僵尸进程；
>To understand what a zombie process is and what causes zombie processes to appear, you’ll need to understand a bit about how processes work on Linux.
            When a process dies on Linux, it isn’t all removed from memory immediately — its process descriptor stays in memory (the process descriptor only takes a tiny amount of memory). The process’s status becomes EXIT_ZOMBIE and the process’s parent is notified that its child process has died with the SIGCHLD signal. The parent process is then supposed to execute the wait() system call to read the dead process’s exit status and other information. This allows the parent process to get information from the dead process. After wait() is called, the zombie process is completely removed from memory.

>This normally happens very quickly, so you won’t see zombie processes accumulating on your system. However, if a parent process isn’t programmed properly and never calls wait(), its zombie children will stick around in memory until they’re cleaned up.
Utilities like GNOME System Monitor, the top command, and the ps command display zombie processes
# 如何杀死僵尸进程？
僵尸进程用kill命令是无法杀掉的，但是我们可以结果掉僵尸进程的爸爸，*僵尸daddy挂了之后*，僵尸进程就成了孤儿进程，孤儿进程不会占用系统资源，会被init程序收养，然后init程序将其回收。
（补充：init是linux系统中的根进程，linux中的进程采用树结构来划分）
僵尸进程解决办法
1. 通过信号机制
　　子进程退出时向父进程发送SIGCHILD信号，父进程处理SIGCHILD信号。在信号处理函数中调用wait进行处理僵尸进程。测试程序如下所示：

>You can’t kill zombie processes as you can kill normal processes with the SIGKILL signal — zombie processes are already dead. Bear in mind that you don’t need to get rid of zombie processes unless you have a large amount on your system – a few zombies are harmless. However, there are a few ways you can get rid of zombie processes.
One way is by sending the SIGCHLD signal to the parent process. This signal tells the parent process to execute the wait() system call and clean up its zombie children. Send the signal with the kill command, replacing pid in the command below with the parent process’s PID:

>kill -s SIGCHLD pid

>However, if the parent process isn’t programmed properly and is ignoring SIGCHLD signals, this won’t help. You’ll have to kill or close the zombies’ parent process. When the process that created the zombies ends, init inherits the zombie processes and becomes their new parent. (init is the first process started on Linux at boot and is assigned PID 1.) init periodically executes the wait() system call to clean up its zombie children, so init will make short work of the zombies. You can restart the parent process after closing it.
If a parent process continues to create zombies, it should be fixed so that it properly calls wait() to reap its zombie children. File a bug report if a program on your system keeps creating zombies.
2. 杀死父进程
　　僵尸进程用kill命令是无法杀掉的，但是我们可以结果掉僵尸进程的爸爸，僵尸daddy挂了之后，僵尸进程就成了孤儿进程，孤儿进程不会占用系统资源，会被init程序收养，然后init程序将其回收

[英文参考文献](https://www.howtogeek.com/119815/htg-explains-what-is-a-zombie-process-on-linux/)

详细文件保存在evernote Linux 笔记本：父进程和子进程，僵尸进程，孤儿进程
