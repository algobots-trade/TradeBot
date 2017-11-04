package com.tradebot.dbcommons;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.pmw.tinylog.*;

import org.h2.jdbcx.JdbcDataSource;

public class db_commons {
	Connection conn = null; 
    Statement stmt = null;
    private String configprop=System.getProperty("user.dir")+File.separator+"resource"+File.separator+"config.properties";
    public static String dbName;
    public static String USER, PASS;

	public db_commons() {
		Properties prop = new Properties();
		InputStream input = null;
		try
		{
			input =new FileInputStream(configprop);
			prop.load(input);
			dbName = System.getProperty("user.dir")+prop.getProperty("DB_HOST_PATH").replace("/", File.separator);
			USER = prop.getProperty("DB_USER");
			PASS = prop.getProperty("DB_PASS");
		}
		catch(Exception ex)
		{
			
		}
		finally
		{
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public int getRowCount(String Query)
	{
		int rowCount=0;
		try {
			JdbcDataSource ds = new JdbcDataSource();
	        ds.setURL("jdbc:h2:"+dbName);
	         conn = ds.getConnection(USER,PASS);
	         stmt = conn.createStatement(); 
	         ResultSet rs = stmt.executeQuery(Query); 
	         rs.last();
	         rowCount = rs.getRow();
	         rs.close();
		}
		catch(Exception ex){
			Logger.error(ex);
			
			
		}
		finally {
			try { 
	            if(stmt!=null) stmt.close(); 
	         } catch(SQLException se2) { 
	         } // nothing we can do 
	         try { 
	            if(conn!=null) conn.close(); 
	         } catch(SQLException se) { 
	        	 	Logger.error(se);
	         } // end finally try 		
		}
		return rowCount;
	}
	
	public List<FormulaData> getFormulaData(String Querystr)
	{
		
		List<FormulaData> set=new ArrayList<FormulaData>();  
		try {
			JdbcDataSource ds = new JdbcDataSource();
	        ds.setURL("jdbc:h2:"+dbName);
	         conn = ds.getConnection(USER,PASS);
	         stmt = conn.createStatement();
	         stmt.execute(Querystr);
	         ResultSet rs =stmt.getResultSet(); 
	         while (rs.next()) {
	        	 FormulaData record = new FormulaData(rs.getInt("id"),rs.getDouble("X"),rs.getDouble("Y"),rs.getDouble("Z"),rs.getString("t1"),rs.getString("t2"),rs.getString("t3"),rs.getString("t4"),rs.getDouble("lcount"),rs.getDouble("stopl"));
	             set.add(record);
	         }
	         
	         if (rs != null) {
	                rs.close();
	            }
	        
		}
		catch(Exception ex){
			Logger.error(ex);
		}
		finally {
			try {
	            
	            if (stmt != null) {
	                stmt.close();
	            }
	            if (conn != null) {
	                conn.close();
	            }
	        } catch (SQLException e) {
	            Logger.error("Ignored", e);
	        }	
		}
		return set;
	}
	
	public ArrayList<String> getSingleColumnRecords(String Querystr)
	{
		ArrayList<String> data = new ArrayList<String>();
		//String[][] result=null;
		try
		{
			JdbcDataSource ds = new JdbcDataSource();
	        ds.setURL("jdbc:h2:"+dbName);
	         conn = ds.getConnection(USER,PASS);
	         stmt = conn.createStatement();
	         stmt.execute(Querystr);
	         ResultSet rs =stmt.getResultSet(); 
	         while (rs.next()) {
	        	 
	        	 data.add(rs.getString(1));
	        
	         }
	         
	         if (rs != null) {
	                rs.close();
	            }
		}
		catch(Exception ex)
		{
			
		}
		finally {
			try {
	            
	            if (stmt != null) {
	                stmt.close();
	            }
	            if (conn != null) {
	                conn.close();
	            }
	        } catch (SQLException e) {
	            Logger.error("Ignored", e);
	        }	
		}
		return data;
	}
	
	public String getSingleCell(String Querystr)
	{
		String data ="";
		//String[][] result=null;
		try
		{
			JdbcDataSource ds = new JdbcDataSource();
	        ds.setURL("jdbc:h2:"+dbName);
	         conn = ds.getConnection(USER,PASS);
	         stmt = conn.createStatement();
	         stmt.execute(Querystr);
	         ResultSet rs =stmt.getResultSet(); 
	         while (rs.next()) {
	        	 
	        	 data = rs.getString(1);
	        	 break;
	        
	         }
	         
	         if (rs != null) {
	                rs.close();
	            }
		}
		catch(Exception ex)
		{
			
		}
		finally {
			try {
	            
	            if (stmt != null) {
	                stmt.close();
	            }
	            if (conn != null) {
	                conn.close();
	            }
	        } catch (SQLException e) {
	            Logger.error("Ignored", e);
	        }	
		}
		return data;
	}
	
	
	
	public String[][] getMultiColumnRecords(String Querystr)
	{
		String [][] data=null;
		//String[][] result=null;
		try
		{
			JdbcDataSource ds = new JdbcDataSource();
	        ds.setURL("jdbc:h2:"+dbName);
	         conn = ds.getConnection(USER,PASS);
	         stmt = conn.createStatement();
	         stmt.execute(Querystr);
	         ResultSet rs =stmt.getResultSet(); 
	         rs.last();
	         int rowSize = rs.getRow();
	         //rs.first();
	         ResultSetMetaData rsmd = rs.getMetaData();
	         int columnSize = rsmd.getColumnCount();
	         int i =0;
	         data = new String[rowSize][columnSize];
	         stmt.execute(Querystr);
	         rs =stmt.getResultSet(); 
	         while(rs.next() && i != rowSize)
	         {
	             for(int j=0;j<columnSize;j++){
	            	 data[i][j] = rs.getString(j+1);
	             }
	             i++;                    
	         }
	         
	         if (rs != null) {
	                rs.close();
	            }
		}
		catch(Exception ex)
		{
			Logger.error(ex);
		}
		finally {
			try {
	            
	            if (stmt != null) {
	                stmt.close();
	            }
	            if (conn != null) {
	                conn.close();
	            }
	        } catch (SQLException e) {
	            Logger.error("Ignored", e);
	        }	
		}
		return data;
	}
	
	public boolean executeNonQuery(String Query)
	{
		boolean isSucess = true;
		try {
			JdbcDataSource ds = new JdbcDataSource();
	        ds.setURL("jdbc:h2:"+dbName);
	         conn = ds.getConnection(USER,PASS);
	         stmt = conn.createStatement(); 
	         stmt.execute(Query); 
		}
		catch(Exception ex)
		{
			isSucess = false;
			Logger.error(ex);
		}
		finally
		{
			try { 
	            if(stmt!=null) stmt.close(); 
	         } 
			catch(SQLException se2) { 
	         } // nothing we can do 
	         try { 
	            if(conn!=null) conn.close(); 
	         } catch(SQLException se) { 
	        	 	Logger.error(se);
	         } // end finally try 	
		}
		return isSucess;
	}
	public int executeBatchStatement(String [] statements){
        Connection connection = getDBConnection();
        Statement stmt = null;
        int stmtscount=0;
        try {
            connection.setAutoCommit(false);
            stmt = connection.createStatement();
            for(int i=0; i<statements.length; i++)
            {
            		stmt.addBatch(statements[i]);
            }          
            int[] countWithoutException = stmt.executeBatch();
            System.out.println("Inserted = " + countWithoutException.length);
            connection.commit();
            stmtscount = countWithoutException.length;
        } catch (BatchUpdateException e) {
            System.out.println("Exception Message " + e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
				stmt.close();
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
        }
        return stmtscount;
    }
	
	private static Connection getDBConnection() {
        Connection dbConnection = null;
        try {
        		JdbcDataSource ds = new JdbcDataSource();
	        ds.setURL("jdbc:h2:"+dbName);
	        dbConnection = ds.getConnection(USER,PASS);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
        return dbConnection;
    }
}