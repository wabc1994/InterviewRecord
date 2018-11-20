# 从Redis的工作模式谈起
我们在使用Redis的时候，通常是多个客户端连接Redis服务器，然后各自发送命令请求(例如Get、Set)到Redis服务器，最后Redis处理这些请求返回结果。
Redis的Reactor模式:基于事件驱动
- 文件事件
- 时间事件



