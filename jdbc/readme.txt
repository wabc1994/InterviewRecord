https://blog.csdn.net/u013064109/article/details/51865433


首先，大家应该对基于JDBC对数据库(这里以mysql数据库为例)一些增删改查的操作都很清楚和熟悉吧，那么我们先来看下最原始的JDBC的操纵步骤：

          第一步：利用java反射原理注册数据库驱动

                            Class.forName(“com.mysql.jdbc.Driver”);

          第二步：获取Connection连接对象

                                Connection conn=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/cms","root","root")

          第三步:预处理SQL语句，并返回一个PreparedStatement预处理对象。

                                PreparedStatement pstmt=conn.prepareStatement(sql);

          第四步:如果有占位符使用预处理对象通过一些set方法给占位符赋值

                              pstmt.setString("name",username)

          第五步:执行SQL语句，执行SQL语句方法有两种一种是没有结果集返回(一般用于:增、删、改)，另一种是有结果集(查询)返回的

                             int executeUpdate();增 删  该
             Result executeQuery();查

          第六步:有结果ResultSet处理结果集

          第七步:释放资源主要需要关闭Connection连接对象，PreparedStatement预处理SQL语句对象，ResultSet结果集

                          注意关闭的顺序：先创建的后关闭

                             rs.close();

                             pstmt.close();

                             conn.close();
