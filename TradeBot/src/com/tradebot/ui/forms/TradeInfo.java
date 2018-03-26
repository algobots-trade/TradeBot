package com.tradebot.ui.forms;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.KeyEventDispatcher;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.sql.Connection;
//import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.h2.jdbcx.JdbcDataSource;
import org.pmw.tinylog.Logger;

import java.awt.BorderLayout;
import javax.swing.JSeparator;
import javax.swing.JButton;

public class TradeInfo {

	private JFrame frame;
	private JLabel lblScrib;
	private JLabel lblbuytime;
	private JLabel lblselltime;
	private JLabel lblB;
	private JLabel lblS;
	private JLabel lblbuyprice;
	private JLabel lblsellprice;
	private JLabel lblLowOrhigh;
	private JLabel lblFlagPrice;
	public static String url = "jdbc:h2:"+System.getProperty("user.dir")+File.separator+"ovvi_Market_bot;AUTO_SERVER=TRUE";
	public static String USER="admin", PASS="test123";
    public static String dbName= System.getProperty("user.dir")+File.separator+"ovvi_Market_bot;AUTO_SERVER=TRUE";
   

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TradeInfo window = new TradeInfo("" ,"");
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public TradeInfo(String strscrib, String strformula) 
	{
		initialize(strscrib,strformula);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(String scrip , String fname) {
		
		frame = new JFrame();
		frame.setBounds(100, 100, 445, 208);
		frame.setBackground(new Color(36,34,29));
		frame.getContentPane().setLayout(null);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setFocusable(true);
		frame.setVisible(true);
		frame.addKeyListener(new KeyAdapter()
	    {
	        @Override
	        public void keyPressed(java.awt.event.KeyEvent evt)
	        {
	        		if (evt.getKeyCode() == KeyEvent.VK_ESCAPE){
	               frame.dispose();
	            }
	        }
	    });
		
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 445, 188);
		panel.setBackground(new Color(51,51,51));
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		
		lblScrib = new JLabel();
		lblScrib.setHorizontalAlignment(SwingConstants.CENTER);
		lblScrib.setBounds(114, 6, 212, 27);
		lblScrib.setFont(new Font("Verdana", Font.PLAIN, 22));
		lblScrib.setForeground(new Color(255, 220, 135));
		panel.add(lblScrib);
		
		JSeparator separator = new JSeparator();
		separator.setForeground(new Color(173, 255, 47));
		separator.setBounds(25, 76, 385, 12);
		panel.add(separator);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setForeground(new Color(173, 255, 47));
		separator_1.setBounds(25, 126, 385, 12);
		panel.add(separator_1);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setForeground(new Color(173, 255, 47));
		separator_2.setOrientation(SwingConstants.VERTICAL);
		separator_2.setBounds(268, 40, 23, 91);
		panel.add(separator_2);
		
		JSeparator separator_3 = new JSeparator();
		separator_3.setForeground(new Color(173, 255, 47));
		separator_3.setOrientation(SwingConstants.VERTICAL);
		separator_3.setBounds(163, 41, 23, 136);
		panel.add(separator_3);
		
		lblbuytime = new JLabel();
		lblbuytime.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblbuytime.setForeground(Color.WHITE);
		lblbuytime.setHorizontalAlignment(SwingConstants.CENTER);
		lblbuytime.setBounds(25, 40, 140, 35);
		panel.add(lblbuytime);
		
		lblselltime = new JLabel();
		lblselltime.setHorizontalAlignment(SwingConstants.CENTER);
		lblselltime.setForeground(Color.WHITE);
		lblselltime.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblselltime.setBounds(25, 87, 140, 35);
		panel.add(lblselltime);
		
		lblB = new JLabel("BUY");
		lblB.setHorizontalAlignment(SwingConstants.CENTER);
		lblB.setForeground(new Color(102, 255, 255));
		lblB.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblB.setBounds(177, 30, 85, 58);
		panel.add(lblB);
		
		lblS = new JLabel("SELL");
		lblS.setHorizontalAlignment(SwingConstants.CENTER);
		lblS.setForeground(new Color(255, 204, 204));
		lblS.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblS.setBounds(177, 80, 85, 58);
		panel.add(lblS);
		
		lblbuyprice = new JLabel();
		lblbuyprice.setHorizontalAlignment(SwingConstants.TRAILING);
		lblbuyprice.setForeground(Color.WHITE);
		lblbuyprice.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblbuyprice.setBounds(274, 40, 132, 35);
		panel.add(lblbuyprice);
		
		lblsellprice = new JLabel();
		lblsellprice.setHorizontalAlignment(SwingConstants.TRAILING);
		lblsellprice.setForeground(Color.WHITE);
		lblsellprice.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblsellprice.setBounds(274, 87, 132, 35);
		panel.add(lblsellprice);
		
		lblLowOrhigh = new JLabel();
		lblLowOrhigh.setHorizontalAlignment(SwingConstants.CENTER);
		lblLowOrhigh.setForeground(new Color(230, 230, 250));
		lblLowOrhigh.setFont(new Font("Verdana", Font.PLAIN, 18));
		lblLowOrhigh.setBounds(25, 135, 140, 42);
		panel.add(lblLowOrhigh);
		
		lblFlagPrice = new JLabel();
		lblFlagPrice.setHorizontalAlignment(SwingConstants.LEFT);
		lblFlagPrice.setForeground(Color.WHITE);
		lblFlagPrice.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblFlagPrice.setBounds(173, 134, 237, 45);
		panel.add(lblFlagPrice);
		
		getTradeInfo(scrip, fname);
	}
	
 	public void getTradeInfo(String strScrib, String Formula)
	{
 		Connection conn = null; 
		Statement stmt = null;
 		try
 		{
 			lblScrib.setText(strScrib.trim().toUpperCase());
// 		    String dbName="~/ovvi_Market_bot;AUTO_SERVER=TRUE";
// 		    String USER="admin";
// 			String PASS="test123";
 			String tbl_formula="";
 			switch (Formula) {
			case "F1":	
				tbl_formula ="TBL_F1_TRADEINFO";
				break;		
			case "F2":		
				tbl_formula ="TBL_F2_TRADEINFO";
				break;
			case "F3":
				tbl_formula ="TBL_F3_TRADEINFO";
				break;
			case "F4":			
				tbl_formula ="TBL_F4_TRADEINFO";
				break;
			case "F5":
				tbl_formula ="TBL_F5_TRADEINFO";
				break;
			default:
				break;
			}
 			JdbcDataSource ds = new JdbcDataSource();
			String to_print="";
	        ds.setURL("jdbc:h2:"+dbName);
	        conn = ds.getConnection(USER,PASS);
	        stmt = conn.createStatement();
	        stmt.execute("select ISSHOTSELL ,ENTRYTIME ,EXITTIME ,BUYPRICE ,SELLPRICE ,EXITCONDITION, HIGH, LOW  from "+tbl_formula+"  where SYMBOL ='"+strScrib+"'");
	        ResultSet rs =stmt.getResultSet(); 
	        SimpleDateFormat toFullDate = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy");
	        SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
	        DecimalFormat f = new DecimalFormat("##.00");
	         while (rs.next()) {
	        	    
	        	   if (rs.getBoolean("ISSHOTSELL") == true)
	        	   {
	        		   lblselltime.setText(time.format(toFullDate.parse(rs.getString("ENTRYTIME"))));
	        		   lblbuytime.setText(time.format(toFullDate.parse(rs.getString("EXITTIME"))));
	        		   lblLowOrhigh.setText("HIGH");
	        		   lblFlagPrice.setText(f.format(Double.valueOf(rs.getString("HIGH"))));
	        	   }
	        	   else if (rs.getBoolean("ISSHOTSELL") == false)
	        	   {   
	        		   lblbuytime.setText(time.format(toFullDate.parse(rs.getString("ENTRYTIME"))));
	        		   lblselltime.setText(time.format(toFullDate.parse(rs.getString("EXITTIME"))));
	        		   lblLowOrhigh.setText("LOW");
	        		   lblFlagPrice.setText(f.format(Double.valueOf(rs.getString("LOW"))));
	        	   }
	        	   lblbuyprice.setText(f.format( Double.valueOf(rs.getString("BUYPRICE"))));
	        	   lblsellprice.setText(f.format(Double.valueOf(rs.getString("SELLPRICE"))));
	        	 	break;
	        
	         }
	         
	         if (rs != null) {
	                rs.close();
	            }
 			
 			
 		}
 		catch(Exception ex)
 		{
 			Logger.error(ex);
 		}
 		finally
 		{
 			try {
	            
	            if (stmt != null) {
	                stmt.close();
	            }
	            if (conn != null) {
	                conn.close();
	            }
	        } catch (SQLException ex) {
	            Logger.error("Ignored", ex);
	        }	
 		}
 		
 		
	}
	
}