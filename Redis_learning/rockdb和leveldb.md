# 线程方面

RocksDB本身有个很明显的优势，就是传统的 LevelDB 只是单线程处理，这样有大 value 的 key 的时候，性能会有明显衰减。但是 RocksDB 添加了多线程支持，在很多场景下性能会有明显提升。

Redis is a remote in-memory data store (similar to memcached). It is a server. A single Redis instance is very efficient, but totally non scalable (regarding CPU). A Redis cluster is scalable (regarding CPU).

RocksDB is an embedded key/value store (similar to BerkeleyDB or more exactly LevelDB). It is a library, supporting multi-threading and a persistence based on log-structured merge trees.

1. 多线程支持
2. 快速存储，比如SSD