
Sds （Simple Dynamic String，简单动态字符串）是 Redis 底层所使用的字符串表示， 几乎所有的 Redis 模块中都用了 sds。
# 常规字符串
在 C 语言中，字符串可以用一个 \0 结尾的 char 数组来表示。

比如说， hello world 在 C 语言中就可以表示为 "hello world\0" 。
### 存在的问题
==这种简单的字符串表示，在大多数情况下都能满足要求，但是，它并不能高效地支持长度计算和追加（append）这两种操作：==
# SDS用途

Sds 在 Redis 中的主要作用有以下两个：
   -实现字符串对象（StringObject）；
   -在 Redis 程序内部用作 char* 类型的替代品


==用 sds 取代 C 默认的 char* 类型==
因为 char* 类型的功能单一， 抽象层次低， 并且不能高效地支持一些 Redis 常用的操作（比如追加操作和长度计算操作）， 所以在 Redis 程序内部， 绝大部分情况下都会使用 sds 而不是 char* 来表示字符串。

性能问题在稍后介绍 sds 定义的时候就会说到， 因为我们还没有了解过 Redis 的其他功能模块， 所以也没办法详细地举例说那里用到了 sds ， 不过在后面的章节中， 我们会经常看到其他模块（几乎每一个）都用到了 sds 类型值。

目前来说， 只要记住这个事实即可： 在 Redis 中， 客户端传入服务器的协议内容、 aof 缓存、 返回给客户端的回复， 等等， 这些重要的内容都是由 sds 类型来保存的。

## 原因
Redis 中的字符串
在 C 语言中，字符串可以用一个 \0 结尾的 char 数组来表示。

比如说， hello world 在 C 语言中就可以表示为 "hello world\0" 。

这种简单的字符串表示，在大多数情况下都能满足要求，但是，它并不能高效地支持长度计算和追加（append）这两种操作：

每次计算字符串长度（strlen(s)）的复杂度为 θ(N) 。
对字符串进行 N 次追加，必定需要对字符串进行 N 次内存重分配（realloc）。
在 Redis 内部， 字符串的追加和长度计算很常见， 而 APPEND 和 STRLEN 更是这两种操作，在 Redis 命令中的直接映射， 这两个简单的操作不应该成为性能的瓶颈。

另外， Redis 除了处理 C 字符串之外， 还需要处理单纯的字节数组， 以及服务器协议等内容， 所以为了方便起见， Redis 的字符串表示还应该是二进制安全的： 程序不应对字符串里面保存的数据做任何假设， 数据可以是以 \0 结尾的 C 字符串， 也可以是单纯的字节数组， 或者其他格式的数据。

考虑到这两个原因， Redis 使用 sds 类型替换了 C 语言的默认字符串表示： sds 既可高效地实现追加和长度计算， 同时是二进制安全的。
## Memory Allocation Strategy
==动态内存分配，类似于vector, 减少内存的分配次数==

==也就是说， 当大小小于 1MB 的字符串执行追加操作时， sdsMakeRoomFor 就为它们分配多于所需大小一倍的空间； 当字符串的大小大于 1MB ， 那么 sdsMakeRoomFor 就为它们额外多分配 1MB 的空间。==

It pre-allocates memory to reduce memory allocation times. Actually, it just simply doubles the original size when it is less than SDS_MAX_PREALLOC(1MB). This is similar to C++ vector allocation strategy when memory is not enough. This is why string append operation does not need to allocate memory every time.

### 内存释放Destroy
SDS memory (including sds header) is dynamically allocated. So just call free to release memory to system.

### 动态内存分配带来的缺点
这种分配策略会浪费内存吗？

执行过 APPEND 命令的字符串会带有额外的预分配空间， 这些预分配空间不会被释放， 除非该字符串所对应的键被删除， 或者等到关闭 Redis 之后， 再次启动时重新载入的字符串对象将不会有预分配空间。

因为执行 APPEND 命令的字符串键数量通常并不多， 占用内存的体积通常也不大， 所以这一般并不算什么问题。

另一方面， 如果执行 APPEND 操作的键很多， 而字符串的体积又很大的话， 那可能就需要修改 Redis 服务器， 让它定时释放一些字符串键的预分配空间， 从而更有效地使用内存。


# 总结
Redis 的字符串表示为 sds ，而不是 C 字符串（以 \0 结尾的 char*）。
对比 C 字符串， sds 有以下特性：

 - 可以高效地执行长度计算（strlen）；

- 可以高效地执行追加操作（append）；
- 二进制安全；
==sds 会为追加操作进行优化：加快追加操作的速度，并降低内存分配的次数，代价是多占用了一些内存，而且这些内存不会被主动释放。==


[参考一](https://redisbook.readthedocs.io/en/latest/internal-datastruct/sds.html)
[参考二](http://blog.wjin.org/posts/redis-internal-data-structure-sds.html)