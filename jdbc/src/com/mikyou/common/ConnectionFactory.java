package com.mikyou.common;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectionFactory {
    /**
     * 获取连接
     * */
    private static String driver;
    private static String url;
    private static String user;
    private static String password;
    /**
     * @
     * 使用静态代码块，来提前初始化一些参数，
     * 我们都知道静态代码块中的代码是在ConnectionFactory类在方法区中加载的时候，就开始加载
     * 所以可以起到一个预加载和与初始化的一个作用
     * */
    static{//为了扩展
        driver="com.mysql.jdbc.Driver";
        url="jdbc:mysql://127.0.0.1:3306/userinfo";
        user="root";
        password="123456";
    }
    /**
     * @Mikyou
     * 获取连接对象
     * */
    public static  Connection getConnection() throws Exception{
        Class.forName(driver);
        return DriverManager.getConnection(url,user,password);
    }
    /**
     * @Mikyou
     * 用于释放资源
     * @param rs
     * @param pstmt
     * @param conn
     * 首先需要判断这些参数是否为空，因为有些操作并且全都涉及这三个对象
     * 例如增加操作就不涉及ResultSet rs
     * */
    public static void close(ResultSet rs,PreparedStatement pstmt,Connection conn) throws SQLException{
        if (rs!=null) {
            rs.close();
        }
        if (pstmt!=null) {
            pstmt.close();
        }
        if (conn!=null) {
            conn.close();
        }
    }

}
