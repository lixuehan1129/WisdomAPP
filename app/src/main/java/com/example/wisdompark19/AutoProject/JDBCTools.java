package com.example.wisdompark19.AutoProject;

import android.util.Log;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import java.sql.DriverManager;

import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Created by 最美人间四月天 on 2018/3/29.
 * 该页面有问题暂时不使用
 */

public class JDBCTools {
    // 关闭conn和statement的操作

    public static void release(java.sql.Statement statement, Connection conn) {
        if (statement != null) {
            try {
                statement.close();

            } catch (Exception e2) {
                // TODO: handle exception
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 1。获取连接的方法 通过读取配置文件从数据库服务器获取一个连接
     *
     * @author Administrator
     *
     */
    public static Connection getConnection() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");//动态加载类
        String url = "jdbc:mysql://60.205.140.219:3306/shequ";
        Log.d("a", "223");
        //上面语句中 60.205.140.219为你的mysql服务器地址 3306为端口号   public是你的数据库名 根据你的实际情况更改
        Connection conn = (Connection) DriverManager.getConnection(url, "shequ", "Zz123456");
        //使用 DriverManger.getConnection链接数据库  第一个参数为连接地址 第二个参数为用户名 第三个参数为连接密码  返回一个Connection对象
        Log.d("a", "224");
        if(conn!=null){ //判断 如果返回不为空则说明链接成功 如果为null的话则连接失败 请检查你的 mysql服务器地址是否可用 以及数据库名是否正确 并且 用户名跟密码是否正确
            Log.d("调试","连接成功");
            return conn;
        }else{
            Log.d("调试","连接失败");
            return null;
        }

    }

}
