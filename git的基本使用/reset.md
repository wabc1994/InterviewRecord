# 撤销最近一次的commit但是未push历史

1. HEAD^ 代表最新一次的commit历史

2. HEAD 指向本地的local resposity 的master 

3. 我们对每次代码库的修改我们知道都需要进行一次常规的基础流程，第一是从workplace, 到index(stage)，再到HEAD


每个commit 都有一个hash 值，我们可以使用 

git log  查看commit的日志

然后 git reset  --hard

撤销修改其实就是版本回退

**reset有三种工作模式**

1. git reset --hard(要自己自定)

2. git reset -- mixed(默认的)

3. git reset --soft(要自己指定)

回退到特定的版本号
git reset --hard commit_id

比较常用的就是hard 模式


# hard、soft、mixed三种模式的基本区别


因为主要涉及到workplace index  head


对应两个命令行 从 add   commit

**soft**

如果我们使用soft 模式  只会撤销head 

那 index 本地的修改还是存在，我们还是继续选择将上一次commit 的历史进行再次commit

**mixed**

本次的修改还会保留在workplace， 如果选择再次进行commit，

保留本次的 修改的

>根据–soft –mixed –hard，会对working tree和index和HEAD进行重置:
     git reset --mixed：此为默认方式，不带任何参数的git reset，即时这种方式，它回退到某个版本，只保留源码，回退commit和index信息
     git reset --soft：回退到某个版本，只回退了commit的信息，不会恢复到index file一级。如果还要提交，直接commit即可
     git reset  --hard：彻底回退到某个版本，本地的源码也会变为上一个版本的内容，此命令 慎用！

# 已经commit并且push 到远程仓库

可以使用 git revert 

# 可以有多重挽救措施

对应的基本情况，但是同样要注意撤销对工作流程当中的三个东西的影响，撤销到什么级别

1. workplace tree
2. index(stage)
3. HEAD(master)

# 参考链接
[git reset的三种模式soft mixed hard - u010037020的博客 - CSDN博客](https://blog.csdn.net/u010037020/article/details/54954696)

[Git-撤销（回退）已经add，commit或push的提交](https://blog.csdn.net/YoungStunner/article/details/78696763)

[Git 如何将改动撤销？](https://zhuanlan.zhihu.com/p/42929114)

[git中reset与revert的区别](https://www.jianshu.com/p/0e1fe709dd97)

