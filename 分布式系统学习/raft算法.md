# Paxos 算法的缺点
1. 难以被理解
2. 难以在实际环境中实现

# raft 算法来源
《In search of an Understandable Consensus Algorithm 》
 寻找一种容易理解的一致性算法
 
 - Leader election 领导选择
 - Log replication 日志复制
 - Safety 安全性的问题
 
 每台机器都有可能有三种觉得，
  - follower 选民
  - leader 领导人
  - candidate  候选人