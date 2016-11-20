package com.subway.db;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

/*
 * 										JDBC基础工具�?
 * 						Java-Database connection basic utility classes
 * 									QUERYUtil - build by Z9P
 * 										at 2014.06.05
 */

public class QUERYUtil {
	static {
		System.out.println("QUERYUtil initialized.");
	}
	
	// Convert ResultSet To Vector
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Vector convertToVector(ResultSet rs) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		Vector value = null;
		Vector rowvalue = null;
		value = new Vector();
		while(rs.next()){
			rowvalue = new Vector();
			for(int i = 1; i <= rsmd.getColumnCount(); i ++){
				rowvalue.add(rs.getObject(i));
			}
			value.add(rowvalue);
		}
		rs.first();
		rs.next();
		if(!rs.next()){
			rs.first();
			value = new Vector();
			for(int i = 1; i <= rsmd.getColumnCount(); i ++){
				value.add(rs.getObject(i));
			}
			rs.first();
		}
		return value;
	}
	
	// Convert ResultSet To ArrayList
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static ArrayList convertToArrayList(ResultSet rs) throws SQLException{
		ResultSetMetaData rsmd = rs.getMetaData();
		ArrayList value = null;
		ArrayList rowvalue = null;
		value = new ArrayList();
		while(rs.next()){
			rowvalue = new ArrayList();
			for(int i = 1; i <= rsmd.getColumnCount(); i ++){
				rowvalue.add(rs.getObject(i));
			}
			value.add(rowvalue);
		}
		return value;
	}
	
	// Get Row From ResultSet In Vector
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Vector getRowIntoVector(ResultSet rs, int row) throws SQLException{
		Vector value = null;
		int rowcount = getRowCount(rs);
		ResultSetMetaData rsmd = rs.getMetaData();
		if(row > rowcount || row < 1){
			return null;
		} else {
			value = new Vector();
			rs.first();
			for(int i = 0; i < row - 1; i ++){
				rs.next();
			}
			for(int i = 1; i <= rsmd.getColumnCount(); i++){
				value.add(rs.getObject(i));
			}
		}
		return value;
	}
	
	// Get Row From ResultSet In ArrayList
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static ArrayList getRowIntoArrayList(ResultSet rs, int row) throws SQLException{
		ArrayList value = null;
		int rowcount = getRowCount(rs);
		ResultSetMetaData rsmd = rs.getMetaData();
		if(row > rowcount || row < 1){
			return null;
		} else {
			value = new ArrayList();
			rs.first();
			for(int i = 0; i < row - 1; i ++){
				rs.next();
			}
			for(int i = 1; i <= rsmd.getColumnCount(); i++){
				value.add(rs.getObject(i));
			}
		}
		return value;
	}
	
	// Get Row Count
	public static int getRowCount(ResultSet rs) throws SQLException{
		int count = 0;
		rs.first();
		while(rs.next()){
			count ++;
		}
		rs.first();
		return count;
	}
	
	// Get Column Names From ResultSet
	public static String[] getColumnNames(ResultSet rs) throws SQLException{
		ResultSetMetaData rsmd = rs.getMetaData();
		String[] values = null;
		if(rsmd.getColumnCount() == 0){
			return values;
		} else {
			values = new String[rsmd.getColumnCount()];
			for(int i = 1; i <= rsmd.getColumnCount(); i ++){
				values[i - 1] = rsmd.getColumnName(i);
			}
		}
		return values;
	}
	
	// Get Column Name From ResultSet
	public static String getColumnName(ResultSet rs, int column) throws SQLException{
		ResultSetMetaData rsmd = rs.getMetaData();
		return rsmd.getColumnName(column);
	}

	// Get Column Count
	public static int getColumnCount(ResultSet rs) throws SQLException{
		ResultSetMetaData rsmd = rs.getMetaData();
		return rsmd.getColumnCount();
	}
	
	// Get Table Name
	public static String getTableName(ResultSet rs) throws SQLException{
		ResultSetMetaData rsmd = rs.getMetaData();
		return rsmd.getTableName(1);
	}
	
	// Get ColumnDisplaySize
	public static int getColumnDisplaySize(ResultSet rs, int column) throws SQLException{
		ResultSetMetaData rsmd = rs.getMetaData();
		return rsmd.getColumnDisplaySize(column);
	}
	
	// Judge If The Column Is AutoIncrement
	public static boolean isAutoIncrement(ResultSet rs, int column) throws SQLException{
		ResultSetMetaData rsmd = rs.getMetaData();
		return rsmd.isAutoIncrement(column);
	}
}
