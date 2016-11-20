package com.subway.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

/*
 * 										JDBC鍩虹宸ュ叿绫?
 * 						Java-Database connection basic utility classes
 * 									JDBCUtil - build by Z9P
 * 										at 2014.06.03
 */

public class JDBCUtil {
	
	private static String username = "root";				// user
	private static String password = "root";				// password
	private static String database = "db_weitie";				// database name
	private static int poolsize = 10;						// user defined size
	private static String url = "jdbc:mysql://localhost:3306/"+database + "?autoReconnect=true";
	private static Vector<Connection> pool = null;			// connection pool
	
	static {
		initializePool();
		System.out.println("JDBCUtil initialized.");
	}
	
	// Initialize Connection Pool
	public static void initializePool(){
		if(pool == null){
			pool = new Vector<Connection>();
		}
		for(int i = 0; i < poolsize; i++){
			try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				System.out.println("Error: Driver not found.");
			}
			Connection conn = null;
			try {
				conn = DriverManager.getConnection(url, username, password);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			pool.add(conn);
		}
	}
	
	// Distribute Connections
	public static Connection getConnection() throws Exception{
		Connection conn = null;
		if(pool.size() > 0){
			conn = pool.remove(0);
			System.out.println("conn.isValid(0)="+conn.isValid(0));
			// 如果连接未关闭但已不可用（未关闭但连接超时）
			if(!conn.isValid(0)){
				conn = DriverManager.getConnection(url, username, password);
				pool.add(conn);
			}
			
		} else {
			try{
				System.out.println("Remind: Connections in pool has been used up, expand capacity now. (JDBCUtil)");
				for(int i = pool.size(); i < poolsize; i ++){
					conn = DriverManager.getConnection(url, username, password);
					pool.add(conn);
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return conn;
	}
	
	// Execute Query Statement
	public static ResultSet executeQuery(Connection conn, String sql, Object objs[]) throws Exception{
		PreparedStatement stmt = conn.prepareStatement(sql);
		for(int i = 0; i < objs.length; i++){
			stmt.setObject(i+1, objs[i]);
		}
		return stmt.executeQuery();
	}
	
	// Execute Update Statement
	public static int executeUpdate(Connection conn, String sql, Object objs[]) throws Exception{
		PreparedStatement stmt = conn.prepareStatement(sql);
		for(int i = 0; i < objs.length; i++){
			stmt.setObject(i+1, objs[i]);
		}
		return stmt.executeUpdate();
	}
	
	// Release Resources
	public static void free(ResultSet rs, Statement stmt, Connection conn){
		try{
			if(rs != null){
				rs.close();
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try{
				if(stmt != null){
					stmt.close();
				}
			} catch(Exception e) {
				e.printStackTrace();
			} finally {
				try{
					if(conn != null){
						// back to the pool
						pool.add(conn);
					}
				} catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
	
	// Extra Functions
	public int getPoolSize(){
		return pool.size();
	}
}
