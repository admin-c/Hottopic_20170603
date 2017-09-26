package com.javy.database;

import java.sql.*;

import com.javy.utils.Constant;

public class SQLJDBC {
    public Connection getConnection(){
//    	包含停用词
    	String url = "jdbc:mysql://localhost:3306/" + Constant.database_name;  
        String name = "com.mysql.jdbc.Driver";  
        String user = "root";  
        String password = Constant.database_password; 
    	Connection con = null;
        //加载驱动程序
        try{
            Class.forName(name);
            //1.getConnection()方法，连接MySQL数据库！！
            con = DriverManager.getConnection(url,user,password);
            if(!con.isClosed())
                System.out.println("Succeeded connecting to the Database!");
        }
        catch(Exception e){
        	e.printStackTrace();
        }
        return con;
    }
    
   
}
