## DFS和回溯
首先如何考虑往下走，深度下去， 然后回溯回来
代码结构体
  1. 函数体(要处理的结果集nums,函数处理的进口0，path， result)
  2. Dfs(最后结果result，中间结果path，处理的结点开始位置， 剪枝开始条件)
     - 剪枝条件 
     - 循环， 处理为i
         -  去重
         -  Path添加处理的元素 path.add(i)
         -  深度下去，考虑下一步 （是i还是i+1）
         -  回退代码体 path.remove(i)
[中间结果是怎样处理的](https://www.youtube.com/watch?v=irFtGMLbf-s)