package cn.cst;

import java.sql.*;

// https://www.runoob.com/docker/docker-install-mysql.html
// mysql -h localhost -u root -p
// create database hello_database;
// use hello_database;
// create table table_user(in varchar(255),name varchar(255),password varchar(255));
// insert into table_user values("1001","WangWu","123456")
public class Main {
  private static final String URL = "jdbc:mysql://localhost:3306/hello_database";
  private static final String USER = "root";
  private static final String PASSWORD = "123456";

  public static void main(String[] args) throws ClassNotFoundException, SQLException {
    Class.forName("com.mysql.jdbc.Driver");
    Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
    // 3.操作数据库，实现增删改查
    Statement stmt = conn.createStatement();
    ResultSet rs = stmt.executeQuery("SELECT * FROM table_user");
    // 如果有数据，rs.next()返回true
    while (rs.next()) {
      System.out.println(
          rs.getString("id") + "--" + rs.getString("name") + "--" + rs.getString("password"));
    }
  }
}
