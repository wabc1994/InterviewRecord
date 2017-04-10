# Leetcode2
leetcode solution in java by 刘雄成
This reposity include some solution for Leetcode problem 
Mainly implement in Java

三.最大公共
公共子序列，公共子串问题
公共子串
给出两个字符串，找到最长公共子串，并返回其长度
状态，字符串的每一位对应另一个字符串的每一个位置，因此通过一个二维数组来表示这每一个状态位，然后是找状态转移方程，转移方程即为其前一个位置的前一个的比对的结果累计当前的结果，如果相同则加1，否则为0。


