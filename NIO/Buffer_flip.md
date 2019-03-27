# flip 方法
将写入模式转换为读模式，重置limit和position

源码解析

```java
public final Buffer flip() {
        limit = position;
        position = 0;
        mark = -1;
        return this;
    }
```

用在读写转换之，反转缓冲区
```java
public class NioTest {
    public static void main(String[] args) {
        // 分配内存大小为10的缓存区
        IntBuffer buffer = IntBuffer.allocate(10);
        // 往buffer里写入数据
        for (int i = 0; i < 5; ++i) {
            int randomNumber = new SecureRandom().nextInt(20);
            buffer.put(randomNumber);
        }
        // 将Buffer从写模式切换到读模式（必须调用这个方法）
       // 
        buffer.flip();
        // 读取buffer里的数据
        while (buffer.hasRemaining()) {
            System.out.println(buffer.get());
        }
    }
}

```

# clear
、

与flip方法的区别是limit = capacity 而不是limit = position 这个点的基本区别

```java

public final Buffer clear() { 
         position = 0; //设置当前下标为0
         limit = capacity; //设置写越界位置与和Buffer容量相同
         mark = -1; //取消标记
         return this; 
}

```
方法



# 参考连接

[Buffer的flip()方法详解)](https://blog.csdn.net/u013096088/article/details/78638245)

[(7条消息)NIO之Buffer的clear()、rewind()、flip()方法的区别 ](https://blog.csdn.net/wanzaixiaoxinjiayou/article/details/43274597)
