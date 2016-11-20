package com.subway.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

/*
 * 										JDBC基础工具�?
 * 						Java-Database connection basic utility classes
 * 									SQLUtil - build by Z9P
 * 										at 2014.06.04
 */

public class SQLUtil {
	
	static {
		System.out.println("SQLUtil initialized.");
	}
	
	// Create Table
	public static boolean createTable(String table_name, String[] field_names, String[] field_attrs){
		String sql = "create table " + table_name + " (";
		if(field_names.length != field_attrs.length || field_names.length == 0 || field_attrs.length == 0){
			System.out.println("Error: Table field description illegal (sql statement illegal).");
			return false;
		} else {
			int i;
			for(i = 0; i < field_names.length - 1; i ++){
				sql += field_names[i] + " " + field_attrs[i] + ", ";
			}
			sql += field_names[i] + " " + field_attrs[i] + ")";
			Connection conn = null;
			
			try {
				conn = JDBCUtil.getConnection();
				
				System.out.println("Connection Closed? = " + conn.isClosed());
				
				try{
					JDBCUtil.executeUpdate(conn, sql, new Object[]{});
					return true;
				} catch (Exception e) {
					System.out.println("Error: You have an error in your SQL syntax or the table \"" + table_name + "\" has already exist. (SQLUtil-createTable)");
				}
				
			} catch (Exception e) {
				System.out.println("Error: Get connection error. (SQLUtil-createTable)");
				e.printStackTrace();
			} finally {
				JDBCUtil.free(null, null, conn);
			}
		}
		
		return false;
	}
	
	// Drop Table
	public static boolean dropTable(String table_name){
		String sql = "drop table " + table_name;
		Connection conn = null;
		
		try {
			conn = JDBCUtil.getConnection();
			
			System.out.println("Connection Closed? = " + conn.isClosed());
			
			try{
				JDBCUtil.executeUpdate(conn, sql, new Object[]{});
				return true;
			} catch (Exception e) {
				System.out.println("Error: The table \"" + table_name + "\" doesn't exist. (SQLUtil-dropTable)");
			}
			
		} catch (Exception e) {
			System.out.println("Error: Get connection error. (SQLUtil-dropTable)");
			e.printStackTrace();
		} finally {
			JDBCUtil.free(null, null, conn);
		}
		
		return false;
	}
	
	// Truncate Table
	public static boolean truncateTable(String table_name){
		String sql = "truncate table " + table_name;
		Connection conn = null;
		
		try {
			conn = JDBCUtil.getConnection();
			
			System.out.println("Connection Closed? = " + conn.isClosed());
			
			try{
				JDBCUtil.executeUpdate(conn, sql, new Object[]{});
				return true;
			} catch (Exception e) {
				System.out.println("Error: The table \"" + table_name + "\" doesn't exist. (SQLUtil-truncateTable)");
			}
			
		} catch (Exception e) {
			System.out.println("Error: Get connection error. (SQLUtil-truncateTable)");
			e.printStackTrace();
		} finally {
			JDBCUtil.free(null, null, conn);
		}
		
		return false;
	}
	
	// Query By SQL Statement
	public static ResultSet executeQuery(String sql){
		Connection conn = null;
		ResultSet rs = null;
		
		try {
			conn = JDBCUtil.getConnection();
			
			System.out.println("Connection Closed? = " + conn.isClosed());
			
			try {
				rs = JDBCUtil.executeQuery(conn, sql, new Object[]{});
			} catch (Exception e) {
				System.out.println("Error: You have an error in your SQL syntax. (SQLUtil-executeQuery1)");
				e.printStackTrace();
			}
			
		} catch (Exception e) {
			System.out.println("Error: Get connection error. (SQLUtil-executeQuery)");
			e.printStackTrace();
		} finally {
			JDBCUtil.free(null, null, conn);
		}
		
		return rs;
	}
	
	// Query By SQL Statement And Get Keywords By "?"
	public static ResultSet executeQuery(String sql, Object[] objs){
		
		Connection conn = null;
		ResultSet rs = null;
		
		try {
			conn = JDBCUtil.getConnection();
			
			System.out.println(sql);
			System.out.println("Connection Closed? = " + conn.isClosed());
			
			try {
				if(objs.length == 0 && sql.contains("?")){
					System.out.println("Error: You have an error in your SQL syntax, cannot match the question mark. (SQLUtil-executeQuery2)");
					return rs;
				}
				else if(objs.length == 0 && !sql.contains("?")){
					rs = JDBCUtil.executeQuery(conn, sql, new Object[]{});
				} else {
					rs = JDBCUtil.executeQuery(conn, sql, objs);
				}
			} catch (Exception e) {
				System.out.println("Error: You have an error in your SQL syntax. (SQLUtil-executeQuery2)");
				e.printStackTrace();
			}
			
		} catch (Exception e) {
			System.out.println("Error: Get connection error. (SQLUtil-executeQuery)");
			e.printStackTrace();
		} finally {
			JDBCUtil.free(null, null, conn);
		}
		
		return rs;
	}
	
	// "select * from 'table_name'"
	public static ResultSet query(String table_name){
		String sql = "select * from " + table_name;
		Connection conn = null;
		ResultSet rs = null;
		
		try {
			conn = JDBCUtil.getConnection();
			
			System.out.println("Connection Closed? = " + conn.isClosed());
			
			try{
				rs = JDBCUtil.executeQuery(conn, sql, new Object[]{});
				return rs;
			} catch (Exception e) {
				System.out.println("Error: The table \"" + table_name + "\" doesn't exist. (SQLUtil-query)");
			}
			
		} catch (Exception e) {
			System.out.println("Error: Get connection error. (SQLUtil-query)");
			e.printStackTrace();
		} finally {
			JDBCUtil.free(null, null, conn);
		}
		
		return rs;
	}
	
	// "select * from 'table_name' where 'sql_condition'"
	public static ResultSet query(String table_name, String sql_condition){
		String sql = "select * from " + table_name + " where " + sql_condition;
		Connection conn = null;
		ResultSet rs = null;
		
		try {
			conn = JDBCUtil.getConnection();
			
			System.out.println("Connection Closed? = " + conn.isClosed());
			
			try{
				rs = JDBCUtil.executeQuery(conn, sql, new Object[]{});
				return rs;
			} catch (Exception e) {
				System.out.println("Error: You have an error in your SQL syntax or the table \"" + table_name + "\" doesn't exist. (SQLUtil-query)");
			}
			
		} catch (Exception e) {
			System.out.println("Error: Get connection error. (SQLUtil-query)");
			e.printStackTrace();
		} finally {
			JDBCUtil.free(null, null, conn);
		}
		
		return rs;
	}
	
	// "select 'column_names' from 'table_name' where 'sql_condition'"
	public static ResultSet query(String table_name, String[] column_names, String sql_condition){
		String sql = "select ";
		for(int i = 0; i < column_names.length; i ++){
			sql += column_names[i] + " ";
		}
		sql += "from " + table_name;
		if(sql_condition.length() != 0){
			sql += " where " + sql_condition;
		}
		Connection conn = null;
		ResultSet rs = null;
		
		try {
			conn = JDBCUtil.getConnection();
			
			System.out.println("Connection Closed? = " + conn.isClosed());
			
			try{
				rs = JDBCUtil.executeQuery(conn, sql, new Object[]{});
				return rs;
			} catch (Exception e) {
				System.out.println("Error: You have an error in your SQL syntax or the table \"" + table_name + "\" doesn't exist. (SQLUtil-query)");
			}
			
		} catch (Exception e) {
			System.out.println("Error: Get connection error. (SQLUtil-query)");
			e.printStackTrace();
		} finally {
			JDBCUtil.free(null, null, conn);
		}
		
		return rs;
	}
	
	// Update Table By SQL Statement And Get Keywords By "?" 
	public static int executeUpdate(String sql, Object[] objs){
		Connection conn = null;
		int returnvalue = 0;
		
		try {
			conn = JDBCUtil.getConnection();
			
			System.out.println("Connection Closed? = " + conn.isClosed());
			
			try {
				if(objs.length == 0 && sql.contains("?")){
					System.out.println("Error: You have an error in your SQL syntax, cannot match the question mark. (SQLUtil-executeUpdate)");
					return 0;
				}
				else if(objs.length == 0 && !sql.contains("?")){
					returnvalue = JDBCUtil.executeUpdate(conn, sql, new Object[]{});
				} else {
					returnvalue = JDBCUtil.executeUpdate(conn, sql, objs);
				}
			} catch (Exception e) {
				System.out.println("Error: You have an error in your SQL syntax. (SQLUtil-executeUpdate)");
				e.printStackTrace();
			}
			
		} catch (Exception e) {
			System.out.println("Error: Get connection error. (SQLUtil-executeUpdate)");
			e.printStackTrace();
		} finally {
			JDBCUtil.free(null, null, conn);
		}
		
		return returnvalue;
	}
	
	// "insert into 'table_name' set ? = 'column_values'"
	public static int insertIntoTable(String table_name, Object[] column_values) throws Exception{
		String sql_0 = "select * from " + table_name;
		Connection conn = null;
		ResultSet rs_0 = null;
		ResultSetMetaData rsmd = null;
		int columncount = 0;
		
		try {
			conn = JDBCUtil.getConnection();
			
			System.out.println("Connection Closed? = " + conn.isClosed());
			
			try {
				rs_0 = JDBCUtil.executeQuery(conn, sql_0, new Object[]{});
				rsmd = rs_0.getMetaData();
				columncount = rsmd.getColumnCount();
			} catch (Exception e) {
				System.out.println("Error: table \"" + table_name + "\" doesn't exist. (SQLUtil-insertIntoTable)");
				return 0;
			}
			
		} catch (Exception e) {
			System.out.println("Error: Get connection error. (SQLUtil-insertIntoTable)");
			e.printStackTrace();
		}
		
		String sql = "insert into " + table_name +" set ";
		int returnvalue = 0;
		if(columncount != column_values.length){
			System.out.println("Error: You have an error in your SQL syntax. (SQLUtil-insertIntoTable)");
			return 0;
		} else {
			int i;
			for(i = 0; i < columncount - 1; i ++){
				sql += rsmd.getColumnName(i + 1) + " = ?, ";
			}
			sql += rsmd.getColumnName(i + 1) + " = ?";
		}
		
		try {
			conn = JDBCUtil.getConnection();
			
			System.out.println("Connection Closed? = " + conn.isClosed());
			
			try {
				returnvalue = JDBCUtil.executeUpdate(conn, sql, column_values);
			} catch (Exception e) {
				e.printStackTrace();
				return 0;
			}
			
		} catch (Exception e) {
			System.out.println("Error: Get connection error. (SQLUtil-insertIntoTable)");
			e.printStackTrace();
		} finally {
			JDBCUtil.free(null, null, conn);
		}
		
		return returnvalue;
	}
	
	// "insert into 'table_name' set 'column_names' = 'column_values'"
	public static int insertIntoTable(String table_name, String[] column_names, Object[] column_values){
		String sql = "insert into " + table_name +" set ";
		int returnvalue = 0;
		if(column_names.length != column_values.length){
			System.out.println("Error: You have an error in your SQL syntax. (SQLUtil-insertIntoTable)");
			return 0;
		} else {
			int i;
			for(i = 0; i < column_names.length - 1; i ++){
				sql += column_names[i] + " = ?, ";
			}
			sql += column_names[i] + " = ?";
		}
		System.out.println(sql);
		Connection conn = null;
		
		try {
			conn = JDBCUtil.getConnection();
			
			System.out.println("Connection Closed? = " + conn.isClosed());
			
			try {
				returnvalue = JDBCUtil.executeUpdate(conn, sql, column_values);
			} catch (Exception e) {
				e.printStackTrace();
				return 0;
			}
			
		} catch (Exception e) {
			System.out.println("Error: Get connection error. (SQLUtil-insertIntoTable)");
			e.printStackTrace();
		} finally {
			JDBCUtil.free(null, null, conn);
		}
		
		return returnvalue;
	}
	
	// "delete from 'table_name' where 'sql_condition'"
	public static int deleteFromTable(String table_name, String sql_condition){
		String sql = "delete from " + table_name + " where " + sql_condition;
		Connection conn = null;
		int returnvalue = 0;
		
		try {
			conn = JDBCUtil.getConnection();
			
			System.out.println("Connection Closed? = " + conn.isClosed());
			
			try {
				returnvalue = JDBCUtil.executeUpdate(conn, sql, new Object[]{});
			} catch (Exception e) {
				System.out.println("Error: You have an error in your SQL syntax. (SQLUtil-executeUpdate)");
				e.printStackTrace();
				return 0;
			}
			
		} catch (Exception e) {
			System.out.println("Error: Get connection error. (SQLUtil-insertIntoTable)");
			e.printStackTrace();
		} finally {
			JDBCUtil.free(null, null, conn);
		}
		
		return returnvalue;
	}

	// "alter table 'table_name' add column 'column_name' 'column_attr'"
	public static int addColumn(String table_name, String column_name, String column_attr){
		String sql = "alter table " + table_name + " add column " + column_name + " " + column_attr;
		Connection conn = null;
		int returnvalue = 0;
		
		try {
			conn = JDBCUtil.getConnection();
			
			System.out.println("Connection Closed? = " + conn.isClosed());
			
			try {
				returnvalue = JDBCUtil.executeUpdate(conn, sql, new Object[]{});
			} catch (Exception e) {
				System.out.println("Error: You have an error in your SQL syntax. (SQLUtil-addColumn)");
				e.printStackTrace();
				return 0;
			}
			
		} catch (Exception e) {
			System.out.println("Error: Get connection error. (SQLUtil-addColumn)");
			e.printStackTrace();
		} finally {
			JDBCUtil.free(null, null, conn);
		}
		
		return returnvalue;
	}

	// "alter table 'table_name' drop column 'column_name'"
	public static int deleteColumn(String table_name, String column_name){
		String sql = "alter table " + table_name + " drop column " + column_name;
		Connection conn = null;
		int returnvalue = 0;
		
		try {
			conn = JDBCUtil.getConnection();
			
			System.out.println("Connection Closed? = " + conn.isClosed());
			
			try {
				returnvalue = JDBCUtil.executeUpdate(conn, sql, new Object[]{});
			} catch (Exception e) {
				System.out.println("Error: You have an error in your SQL syntax. (SQLUtil-deleteColumn)");
				e.printStackTrace();
				return 0;
			}
			
		} catch (Exception e) {
			System.out.println("Error: Get connection error. (SQLUtil-deleteColumn)");
			e.printStackTrace();
		} finally {
			JDBCUtil.free(null, null, conn);
		}
		
		return returnvalue;
	}
	
	// Extra Functions
	public static ResultSet queryNow(){
		String sql = "select now()";
		Connection conn = null;
		ResultSet rs = null;
		
		try {
			conn = JDBCUtil.getConnection();
			
			System.out.println("Connection Closed? = " + conn.isClosed());
			
			try {
				rs = JDBCUtil.executeQuery(conn, sql, new Object[]{});
			} catch (Exception e) {
				System.out.println("Error: You have an error in your SQL syntax. (SQLUtil-queryNow)");
				e.printStackTrace();
			}
			
		} catch (Exception e) {
			System.out.println("Error: Get connection error. (SQLUtil-queryNow)");
			e.printStackTrace();
		} finally {
			JDBCUtil.free(null, null, conn);
		}
		
		return rs;
	}
	
	public static ResultSet queryTables(){
		String sql = "show tables";
		Connection conn = null;
		ResultSet rs = null;
		
		try {
			conn = JDBCUtil.getConnection();
			
			System.out.println("Connection Closed? = " + conn.isClosed());
			
			try {
				rs = JDBCUtil.executeQuery(conn, sql, new Object[]{});
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		} catch (Exception e) {
			System.out.println("Error: Get connection error. (SQLUtil-queryTables)");
			e.printStackTrace();
		} finally {
			JDBCUtil.free(null, null, conn);
		}
		
		return rs;
	}
	
	public static ResultSet queryTableDescription(String table_name){
		String sql = "desc " + table_name;
		Connection conn = null;
		ResultSet rs = null;
		
		try {
			conn = JDBCUtil.getConnection();
			
			System.out.println("Connection Closed? = " + conn.isClosed());
			
			try {
				rs = JDBCUtil.executeQuery(conn, sql, new Object[]{});
			} catch (Exception e) {
				System.out.println("Error: The table \"" + table_name + "\" doesn't exist. (SQLUtil-queryTableDescription)");
			}
			
		} catch (Exception e) {
			System.out.println("Error: Get connection error. (SQLUtil-queryTableDescription)");
			e.printStackTrace();
		} finally {
			JDBCUtil.free(null, null, conn);
		}
		
		return rs;
	}
}
