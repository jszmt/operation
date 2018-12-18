package com.wugz.oper.utils;

import java.sql.*;
import java.util.*;

/**
 * @ClassName DBUtils
 * @Description TODO
 * @Author wuguizhen
 * @Date 2018/9/13 10:41
 */
public class DBUtils {

    public static final String DATABASE_MYSQL = "mysql";

    public static final String DATABASE_ORACLE = "oracle";

    private static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";

    private static final String ORACLE_DRIVER = "oracle.jdbc.driver.OracleDriver";

    /***
     * 执行批量语句
     * @param connection
     * @param sqls
     * @return
     * @throws SQLException
     */
    public static int batchExecute(Connection connection,List<String> sqls) throws SQLException {
        Statement sm = null;
        int updateCount =0;
        try {
            sm = connection.createStatement();
            for(String sql : sqls){
                sm.addBatch(sql);
            }
            sm.executeBatch();
            updateCount = sm.getUpdateCount();
        } finally {
            sm.close();
        }
        return updateCount;
    }
    
    /***
     * @author wuguizhen
     * @Description 执行单条语句
     * @date 2018/12/12 18:39
     * @param [connection, sql]
     * @return int
     */
    public static int execute(Connection connection,String sql) throws SQLException {
        Statement sm = null;
        int updateCount =0;
        try {
            sm = connection.prepareStatement(sql);
            ((PreparedStatement) sm).execute();
            updateCount = sm.getUpdateCount();
        } finally {
            sm.close();
        }
        return updateCount;
    }
    
    /*
     * @author wuguizhen
     * @Description 判断能否连接数据库
     * @date 2018/10/20 22:04
     * @param [databaseType, url, user, password]
     * @return boolean
     */
    public static boolean canConnect(String databaseType,String url,String user,String password){
        try {
             if(getConnection(databaseType,url,user,password,true) != null){
                 return true;
             }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    /*
     * @author wuguizhen
     * @Description 查询数据库
     * @date 2018/9/13 11:39
     * @param [conn, sql]
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     */
    public static List<Map<String,String>> query(Connection conn,String sql) throws Exception{
        Statement stat = null;
        ResultSet rs = null;
        List<Map<String,String>> list = new ArrayList<>();
        try {
            stat = conn.createStatement();
            rs = stat.executeQuery(sql);
            ResultSetMetaData md = rs.getMetaData(); //获得结果集结构信息,元数据
            int columnCount = md.getColumnCount();   //获得列数
            while (rs.next()) {
                Map<String,String> rowData = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String value = rs.getObject(i) !=null?String.valueOf(rs.getObject(i)):null;
                    rowData.put(md.getColumnName(i), value);
                }
                list.add(rowData);

            }
        } finally {
            System.out.println("查询结束关闭流。。。sql语句："+sql);
            close(rs, stat, conn);
        }
        return list;
    }


    /*
     * @author wuguizhen
     * @Description 获取数据库连接
     * @date 2018/9/13 11:13
     * @param [url, user, password]
     * @return java.sql.Connection
     */
    public static Connection getConnection(String databaseType,String url,String user,String password,boolean autoCommit)throws Exception{
        Connection conn = null;
        String driver = DATABASE_MYSQL.equals(databaseType)?MYSQL_DRIVER:ORACLE_DRIVER;
        //加载数据库驱动
        Class.forName(driver);
        conn = DriverManager.getConnection(url, user, password);
        conn.setAutoCommit(autoCommit);
        return conn;
    }

    private static void close(ResultSet rs, Statement stat, Connection conn) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (stat != null) {
            try {
                stat.close();
            } catch (SQLException e) {
                e.printStackTrace();
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
}
