# 子进程
父进程fork() 后创建一个子进程(有自己独立的地址空间数据空间、堆和栈)，得到父亲一样的资源 ，文件描述符等情况，子进程*完成任务后进入dead状态*；但此时子进程并没有完全从进程树中清除掉，还是占用着系统的资源，比如进程号pid； 子进程向父亲进程发送信号，等待确认；如果父亲进程确定子进程的终止信息，然后子进程才真正算是从进程树中完成清楚掉，释放占用的资源等情况。如果父亲进程选择忽略子进程发送过来的确认信号，或者没有调用wait() 或者 waitid（） , 子进程就变成来僵尸进程（linux系统默认的进程号是 0到 1023，容量是 1024） 
fork()、exec（一个进程真正终止）、waitpid(父进程通过该函数得到子进程的之心信息)
>A process in Unix or Unix-like operating systems becomes a zombie process when it has completed execution but one or some of its entries are still in the process table. If a process is ended by an "exit" call, all memory associated with it is reallocated to a new process; in this way, the system saves memory. But the process’ entry in the process table remains until the parent process acknowledges its execution, after which it is removed. The time between the execution and the acknowledgment of the process is the period when the process is in a zombie state.
# 僵尸进程定义？
僵死进程：一个已经终止，但是父进程尚未对其进行善后处理（获取终止进程相关信息，释放它仍占用的资源）的进程。
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


***
## wait()、 waitpid()、 exec()、fork()
### wait()、waitpid()
当一个子进程正常或异常终止的时候，内核就像其父进程发送*SIGCHLD信号*，因为子进程是个一步事件，所以这种信号也是内核系那个父进程发的异步通知。 
 1. 父进程可以选择忽略该信号;
 2. 或者提供一个该信号发生时即被调用执行的函数。
**对于这种信号的系统默认动作是忽略它。(默认是忽略)**
 
 现在要知道调用wait或waitpid的进程可能会发生什么情况：
*  如果其所有子进程都在运行，则阻塞。
*  如果一个子进程已经终止，正在得带的父进程获取到终止状态，则取得该子进程的终止状态立即返回。
*  如果他没有任何子进程，则立即出错返回。

#### wait()函数：结束(中断)进程函数(常用)
>pid_t wait (int * status); status 代表子进程的状态，dead 

函数说明：wait()会暂时停止目前进程的执行, 直到有信号来到或子进程结束. 如果在调用wait()时子进程已经结束, 则wait()会立即返回子进程结束状态值. 子进程的结束状态值会由参数status 返回, 而子进程的进程识别码也会一快返回. 如果不在意结束状态值, 则参数 status 可以设成NULL. 子进程的结束状态值请参考waitpid().
返回值：如果执行成功则返回子进程识别码(PID), 如果有错误发生则返回-1. 失败原因存于errno 中.

### fork 函数
进程标识符

    记住2个函数，可以获得自身进程号和父进程号：

    pit_t getpid(void);

    pie_t getppid(void);

1. fock函数调用一次，但是返回两次，在子进程中返回0，父进程中返回子进程ID。由于进程ID不可能为0（ID为0的为系统进程），且一个子进程只可以有一个父进程调用getppid获得，所以可以**通过<0判断fock失败；==0判断为子进程；>0判断为父进程**。
2. 子进程和父进程继续执行fock后的指令。子进程是父进程的副本。例如，子进程获得父进程的数据空间、堆和栈的副本。注意，这是子进程所拥有的副本(两份)。父、子进程并不共享这些存储空间部分（共享的话是一份）。父、子进程共享正文段。APUE中的8-1程序清单需要好好理解，尤其是在控制台运行和重定向时I/O缓冲区的刷新问题。
3. 一般来说，在fock之后是父进程先执行还是子进程先执行是不确定的。可以使用进程控制和同步代码来控制进程执行顺序。
4. ock失败的原因只有2个：一是已经有了太多的进程，例如工作中遇到的内存不足、句柄泄露等资源不足引起的问题；二是该实际用户ID的进程总数超过了系统限制。CHILD_MAX来规定。
5. fock的2种用法：一是一个父进程希望复制自己，使父、子进程同时执行不同的代码段。在网络服务进程中常用——父进程等待客户端的服务请求，当请求到达时，父进程调用fock，是子进程处理请求，父进程继续等待下一个服务；二是一个进程要执行不同程序，例如shell。

### exec 函数
 fork创建子进程后，子进程往往要调用一种exec函数以执行另一个程序。当进程调用一种exec时，该进程执行的程序完全替换为新的程序，而新的程序则从其main函数开始执行。**因为exec并不创建新进程，所以前后的进程ID并未改变。exec知识用一个全新的程序替换了当前的正文、数据、堆和栈段** 
 在fork后的子进程中使用exec函数族，可以装入和运行其它程序（子进程替换原有进程，和父进程做不同的事）。
 >exec函数族可以根据指定的文件名或目录名找到可执行文件，并用它来取代原调用进程的数据段、代码段和堆栈段。在执行完后，原调用进程的内容除了进程号外，其它全部被新程序的内容替换了。另外，这里的可执行文件既可以是二进制文件，也可以是Linux下任何可执行脚本文件。

#### 什么情况下使用exec函数
##### 在Linux中使用exec函数族主要有一下两种情况

* 当进程认为自己不能再为系统和用户做出任何贡献时，就可以调用任何exec函数族让自己重生；
* 如果一个进程想执行另外一个程序，那么它就可以调用fork函数新建一个进程，然后调用任何一个exec函数使子进程重生；
##### 六种exec函数
```c
int execl(const char* path, const char* arg, …)
int execlp(const char* file, const char* arg, …)
int execle(const char* path, const char* arg, …, char* const envp[])
int execv(const char* path, const char* argv[])
int execvp(const char* file, const char* argv[])
int execvpe(const char* file, const char* argv[], char *const envp[])
```


