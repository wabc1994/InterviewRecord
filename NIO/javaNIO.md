重点查看美团技术团队的这片文章
[Java NIO浅析](https://tech.meituan.com/nio.html)
[NIO](https://medium.com/@nilasini/java-nio-non-blocking-io-vs-io-1731caa910a2)
重要概念

# bio、NIO、AIO基本概念
[看这篇论文](http://loveshisong.cn/%E7%BC%96%E7%A8%8B%E6%8A%80%E6%9C%AF/2016-06-25-%E5%8D%81%E5%88%86%E9%92%9F%E4%BA%86%E8%A7%A3BIO-NIO-AIO.html)



# 什么叫流？
1. 流： 代表有任何能力产出数据的数据源对象或者有能力接受数据的接受端对象
2. 流的本质： 数据的传输，根据数据传输特性将流抽象为各种类，方便进行的直观抽象
3. 流的作用 ：

流分为两种字节流和字符流

**面向流的缺陷**

简单总结： 读写不够灵活,并且单方向的情况


''



# 一个io操作如下

```java

```
# 区别
 
 ## 面向流和缓冲区
 
 In stream-oriented I/O, you wrote data directly to, and read data directly from, Stream objects.
 
 1. 面向流， 数据在流中，IO是面向流的，NIO是面向缓冲区的。 Java IO面向流意味着每次从流中读一个或多个字节，直至读取所有字节，它们没有被缓存在任何地方。此外，它不能前后移动流中的数据。如果需要前后移动从流中读取的数据，需要先将它缓存到一个缓冲区。
 
 2. 面向缓冲区 Java NIO的缓冲导向方法略有不同。数据读取到一个它稍后处理的缓冲区，需要时可在缓冲区中前后移动。这就增加了处理过程中的灵活性。但是，还需要检查是否该缓冲区中包含所有您需要处理的数据。而且，需确保当更多的数据读入缓冲区时，不要覆盖缓冲区里尚未处理的数据
 
 - 是否可以前后移动(灵活性等特点情况)
 
 - 可以记录读取的位置
 
 ## 阻塞与非阻塞io
 1.Java IO的各种流是阻塞的。这意味着，当一个线程调用read() 或 write()时，该线程被阻塞，直到有一些数据被读取，或数据完全写入。该线程在此期间不能再干任何事情了。
 
 2. ava NIO的非阻塞模式，使一个线程从某通道发送请求读取数据，但是它仅能得到目前可用的数据，如果目前没有数据可用时，就什么都不会获取。而不是保持线程阻塞，所以直至数据变的可以读取之前，该线程可以继续做其他的事情。 非阻塞写也是如此。一个线程请求写入一些数据到某通道，但不需要等待它完全写入，这个线程同时可以去做别的事情。 线程通常将非阻塞IO的空闲时间用于在其它通道上执行IO操作，所以一个单独的线程现在可以管理多个输入和输出通道（channel）。
 
 # 选择器
 >Java NIO’s selectors allow a single thread to monitor multiple channels of input. You can register multiple channels with a selector, then use a single thread to “select” the channels that have input available for processing, or select the channels that are ready for writing. This selector mechanism makes it easy for a single thread to manage multiple channels.
 由于是非阻塞的，所以nio线程不阻塞，所以线程可以监控多个通道， 当某个通道准备好读或者写入时，选择器为线程选择一个准备好的通道。
 
选择这两个方式需要注意的事项

- The number of thread used to process the data.
- 
 

