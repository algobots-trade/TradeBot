package com.tradebot.ui.forms;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Label;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimerTask;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.h2.jdbcx.JdbcDataSource;
import org.pmw.tinylog.Logger;

import com.tradebot.dbcommons.ClockLabel;
import com.tradebot.dbcommons.db_commons;
import com.tradebot.dbcommons.tradebot_utility;
import com.tradebot.presto.presto_commons;
import com.tradebot.presto.presto_data_feeder;
import com.tradebot.ui.forms.*;
//import Forms.FormulaInputs;
//import Forms.SymbolMgmt;
//import Forms.TradeInfo;
//import Forms.ResearchDashboard.PLTableModel;
//import Forms.ResearchDashboard.PLTableModel.Pldata;


import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.SystemColor;
import java.awt.GridLayout;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

public class TradeBoard {

	private JFrame frmTradeBoard;
	private JScrollPane scrollPane; 
	private ClockLabel timeLable;
	private ClockLabel dateLable;
	private ClockLabel dayLable;
	private JPanel mainTable;
	private Connection con;
	//C:\Users\admin\Desktop\Workspace\TradeBot\resource\DB_TRADE_BOT;AUTO_SERVER=TRUE
	public static String dbName= System.getProperty("user.dir")+"/resource/DB_TRADE_BOT;AUTO_SERVER=TRUE";
	public static String url = "jdbc:h2:"+System.getProperty("user.dir")+File.separator+"/resource/DB_TRADE_BOT;AUTO_SERVER=TRUE";
	private String configprop=System.getProperty("user.dir")+File.separator+"resource"+File.separator+"config.properties";
	public static String USER="admin", PASS="test123"; 
	private JPanel totalpanel;
	private Label lbltotal;
	private JButton btnRun,btnDcsv,btnClear;
	JLabel lblPandL;
	private Label f1PL,f2PL,f3PL,f4PL,f5PL,f6PL,f7PL;
	String tradelogpath;
	db_commons dbobj=new db_commons();
	tradebot_utility utils = new tradebot_utility(); 
	private JButton btnstop;
	private String [][] headfeeditems;
	presto_data_feeder pfd;
	
	presto_commons objPresto;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TradeBoard window = new TradeBoard();
					window.frmTradeBoard.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
   public void dummyclean()
   {
     try
     {
    	 String [] cleanstmts = new String[4];
    	 cleanstmts[0]="Update TBL_F1_TRADES set Buyprice = null , sellprice=null where tradesubjectid=11536;";
    	 cleanstmts[1]="Update TBL_F1_TRADES set Buyprice = null , sellprice=null where tradesubjectid=57025;";
    	 cleanstmts[2]="update TBL_TRADEBOARD set F1PC = null, F1TC=null, f1pl=null where tradesecid=11536;";
    	 cleanstmts[3]="update TBL_TRADEBOARD set F1PC = null, F1TC=null, f1pl=null where tradesecid=57025;";
    	 dbobj.executeBatchStatement(cleanstmts);
     }
     catch(Exception ex)
     {
    	 
     }
   }
	/**
	 * Create the application.
	 */
	public TradeBoard() 
	{
		//frmTradeBoard.setVisible(true);
		//dummyclean();
		tradelogpath = utils.configlogfile("TRADEBOT_LOG");
		USER = utils.readconfigprop("DB_USER");
		PASS = utils.readconfigprop("DB_PASS");
		dbName =System.getProperty("user.dir")+ utils.readconfigprop("DB_HOST_PATH");
		objPresto = new presto_commons();
		url= "jdbc:h2:"+System.getProperty("user.dir")+ utils.readconfigprop("DB_HOST_PATH");
		headfeeditems = dbobj.getMultiColumnRecords("SELECT  TBL_HEADFEEDS.FEEDSUBJECTID, TBL_HEADFEEDS.SCRIB FROM TBL_HEADFEEDS " + 
				"INNER JOIN TBL_PLAYERS ON TBL_HEADFEEDS.FEEDSUBJECTID = TBL_PLAYERS.FEEDSUBJECTID;");
		try {
			initialize();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws SQLException 
	 */
	private void initialize() throws SQLException {
		
		frmTradeBoard = new JFrame();
		frmTradeBoard.setTitle("TRADE BOT DASHBOARD");
		frmTradeBoard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTradeBoard.setBackground(new Color(36,34,29));
		frmTradeBoard.getContentPane().setBackground(new Color(51, 51, 51));
		
		JdbcDataSource ds = new JdbcDataSource();
		ds.setURL(url);
        con = ds.getConnection(USER,PASS);	
		
		//maximize the window 
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    double width = screenSize.getWidth();
	    double height = screenSize.getHeight();
	    frmTradeBoard.setSize((int)width, (int)height-40);
	    
	    JPanel pnlbody = new JPanel();
	    pnlbody.setBackground(new Color(36,34,29));
		frmTradeBoard.getContentPane().add(pnlbody, BorderLayout.CENTER);
		pnlbody.setLayout(new BorderLayout(0, 0));
		
		//Heading Panel
		JPanel pnlHead = new JPanel();
		pnlHead.setBackground(new Color(36,34,29));
		pnlHead.setLayout(new BorderLayout());
		
		JLabel lblLiveTradeDashboard = new JLabel("\t\t                          Mathart'z Trade Board");
		lblLiveTradeDashboard.setHorizontalAlignment(SwingConstants.CENTER);
		lblLiveTradeDashboard.setFont(new Font("Tsukushi A Round Gothic", Font.BOLD, 26));
		lblLiveTradeDashboard.setForeground(new Color(240,159,108));
		pnlHead.add(lblLiveTradeDashboard);
		
		lblPandL = new JLabel("+ 000.00");
		lblPandL.setFont(new Font("Tsukushi A Round Gothic", Font.BOLD, 22));
		//pnlHead.add(lblPandL, BorderLayout.WEST);
		//lblPandL.setVisible(false);
		
		
		dateLable = new ClockLabel("date");
		dateLable.setForeground(new Color(210, 180, 140));
	    timeLable = new ClockLabel("time");
	    timeLable.setFont(new Font("American Typewriter", Font.PLAIN, 24));
	    
	    dayLable = new ClockLabel("day");
	    dayLable.setForeground(new Color(210, 180, 140));
		
	    JPanel pnllivetime=new JPanel();
	    pnllivetime.setBackground(new Color(36,34,29));
	    JPanel toprightcenter = new JPanel();
	    toprightcenter.setBackground(new Color(36,34,29));
	    toprightcenter.setLayout(new BorderLayout(0, 0));
	    pnllivetime.add(toprightcenter, BorderLayout.CENTER);
	    toprightcenter.add(timeLable,BorderLayout.CENTER);
	    
	    
 	    JPanel toprightright = new JPanel();
 	    toprightright.setBackground(new Color(36,34,29));
	    toprightright.setLayout(new BorderLayout(2, 2));
	    pnllivetime.add(toprightright, BorderLayout.EAST);	    
	    toprightright.add(dateLable,BorderLayout.EAST);	
	    toprightright.add(dayLable,BorderLayout.SOUTH);	
		
	    pnlHead.add(pnllivetime, BorderLayout.EAST);
		
		pnlbody.add(pnlHead,BorderLayout.NORTH);
		
		//Center panel for grid 
		JPanel pnlgridpanel = new JPanel();
		pnlgridpanel.setBackground(new Color(36,34,29));
		mainTable = new JPanel();
		pnlgridpanel.add(mainTable, BorderLayout.NORTH);
		mainTable.setBackground(new Color(36,34,29));
		EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    ex.printStackTrace();
                }
                Outerpane grid = new Outerpane();	                    
        		    grid.setLayout(new FlowLayout());
        		    grid.setBackground(new Color(36,34,29));
        		    
        		    mainTable.add(grid);
        		    mainTable.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        		    mainTable.setBackground(new Color(36,34,29));
    	    	
         }
        });
		
		totalpanel = new JPanel();
		pnlgridpanel.add(totalpanel);
		totalpanel.setBackground(new Color(61, 57, 54));
		totalpanel.setPreferredSize(new Dimension((int)width-20, 40));
		totalpanel.setLayout(new BorderLayout(0, 0));

		
		JPanel pnltotvalues =new JPanel();
		pnltotvalues.setBackground(new Color(61, 57, 54));
		totalpanel.add(pnltotvalues,BorderLayout.CENTER);
		pnltotvalues.setLayout(new BoxLayout(pnltotvalues, BoxLayout.X_AXIS));
		
		
		f1PL = new Label("00.00");
		f1PL.setAlignment(Label.CENTER);
		f1PL.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		f1PL.setForeground(new Color(255,220,135));
		//f1PL.setSize(119, 30);
		pnltotvalues.add(f1PL);
		
		f2PL = new Label("00.00");
		f2PL.setAlignment(Label.CENTER);
		f2PL.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		f2PL.setForeground(new Color(255,220,135));
		pnltotvalues.add(f2PL);
		
		f3PL = new Label("00.00");
		f3PL.setAlignment(Label.CENTER);
		f3PL.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		f3PL.setForeground(new Color(255,220,135));
		pnltotvalues.add(f3PL);
		
		f4PL = new Label("00.00");
		f4PL.setAlignment(Label.CENTER);
		f4PL.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		f4PL.setForeground(new Color(255,220,135));
		pnltotvalues.add(f4PL);
		
		f5PL = new Label("00.00");
		f5PL.setAlignment(Label.CENTER);
		f5PL.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		f5PL.setForeground(new Color(255,220,135));
		pnltotvalues.add(f5PL);
		
		f6PL = new Label("00.00");
		f6PL.setAlignment(Label.CENTER);
		f6PL.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		f6PL.setForeground(new Color(255,220,135));
		pnltotvalues.add(f6PL);
		
		f7PL = new Label("00.00");
		f7PL.setAlignment(Label.CENTER);
		f7PL.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
		f7PL.setForeground(new Color(255,220,135));
		pnltotvalues.add(f7PL);
		
		JPanel pnlControls=new JPanel();
		pnlControls.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		btnRun = new JButton("RUN");
		btnRun.setPreferredSize(new Dimension(150, 35));
		pnlControls.add(btnRun);
		btnRun.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{    		
				//dummyExecution();
				// Original Code Will Enable later
				pfd = new presto_data_feeder();
			    Thread pfdsubscriber = new Thread(new Runnable() {
			         public void run() {
			        	 pfd.presto_start_data_feeder(headfeeditems);
			         }
			    });  
			    pfdsubscriber.start();
			}
		});
		btnDcsv = new JButton("D-CSV");
		btnDcsv.setPreferredSize(new Dimension(150, 35));
		btnDcsv.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try
				{
					db_commons dbobj=new db_commons();
					String [][] recs = dbobj.getMultiColumnRecords("SELECT TSCRIB as SCRIB , Feedsecid, Tradesecid" +
							", F1PL as \"F1 P/L\", F1PC as \"F1 %\", F1TC as \"F1 COUNT\"" + 
							", F2PL as \"F2 P/L\",F2PC as \"F2 %\", F2TC as \"F2 COUNT\"" + 
							", F3PL as \"F3 P/L\",F3PC as \"F3 %\", F3TC as \"F3 COUNT\"" + 
							", F4PL as \"F4 P/L\",F4PC as \"F4 %\", F4TC as \"F4 COUNT\"" + 
							", F5PL as \"F5 P/L\",F5PC as \"F5 %\", F5TC as \"F5 COUNT\"" + 
							", F6PL as \"F6 P/L\", F6PC as \"F6 %\", F6TC as \"F6 COUNT\"" + 
							",F7PL as \"F7 P/L\", F7PC as \"F7 %\", F7TC as \"F7 COUNT\"" + 
							"from TBL_TRADEBOARD order by ID ASC ;");
					dCSV(recs);	
					JOptionPane.showMessageDialog(frmTradeBoard,"Trade Dashboard Exported Successfully !!","Data Exported",JOptionPane.INFORMATION_MESSAGE);
					
				}
				catch(Exception ex)
				{
					
				}
				finally
				{
					
				}
								
			}
		});
		pnlControls.add(btnDcsv);
		pnlControls.setBackground(new Color(36,34,29));
		
		
		pnlbody.add(pnlControls, BorderLayout.SOUTH);
		btnClear = new JButton("Clear");
		btnClear.setPreferredSize(new Dimension(150, 35));
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
			});
		pnlControls.add(btnClear);
		
		btnstop = new JButton("STOP");
		btnstop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				
				Thread pfdunsubscriber = new Thread(new Runnable() {
			         public void run() {
			        	 pfd.presto_stop_data_feeder(headfeeditems);
			         }
			    });  
				pfdunsubscriber.start();
				
			}
		});
		btnstop.setPreferredSize(new Dimension(150, 35));
		pnlControls.add(btnstop);
		pnlbody.add(pnlgridpanel);
		pnlbody.setBackground(new Color(36,34,29));
		
    
	}
	///////////////////Dummy Execution Startss /////////////////
	public void dummyExecution()
	{
		// RELIANCE-CM, ACC-FUT, NIFTY-FUT
		try
		{
			String strScrib1 = "RELIANCE", strScrib2 = "ACC",  strScrib3 = "NIFTY";
			int   intSecid1 = 2885, intsecid2 = 57060, intsecid3 = 57025 ;
			double dblBuy1 = 500.99 ,dblBuy2 = 400.99 , dblBuy3 = 2500.45;
			double dblSell1 = 500.99 ,dblSell2 = 400.99 , dblSell3 = 2540.45;
			Date date = new Date();
			System.out.print("/n Watching Started:" + date);
			new java.util.Timer().schedule( 
			        new java.util.TimerTask() {
			            @Override
			            public void run() {
			            	Date date1 = new Date();
			                System.out.print("Logging into Broker ...");
			                objPresto.checkandLoginFinvasia();
			            	System.out.print("\n Triggered First Buy at :" + date1);
			                //firstBuy();
			                //secandBuy();
			                secandSell();
			            }
			            
			        }, 
			        3000 
			);
		}
		catch(Exception ex)
		{
			Logger.error(ex);
		}
	}
	
	public void firstBuy()
	{
		String strScrib1 = "RELIANCE";
		int   intSecid1 = 11536;
		double dblBuy1 = 925.50;
		double dblSell1 = 924.20;
		int Qty1 = 75;
				
		try
		{

            Thread.sleep(4000);
        	Date date = new Date();
        	objPresto.PlaceOrder("omnesys","CM",strScrib1,String.valueOf(intSecid1) ,"22-02-2018","A29",String.valueOf(Qty1),"0.0","0.0","na","0.0","MARKET","Presto_Mathsartz_Strategy","TestOrders","DAY","BUY");
            System.out.print("\n First Buy Order Placed at :" + date);
            dbobj.executeNonQuery("INSERT INTO TBL_F1_TRADES (FEEDSUBJECTID,TRADESUBJECTID,BUYPRICE) VALUES ('"+String.valueOf(intSecid1)+"','"+String.valueOf(intSecid1)+"','"+String.valueOf(dblBuy1)+"') ");
            firstSell();

		}
		catch(Exception ex)
		{
			Logger.error(ex);
		}
		
	}
	
	public void firstSell()
	{
		String strScrib1 = "RELIANCE";
		int   intSecid1 = 11536;
		double dblBuy1 = 925.50;
		double dblSell1 = 924.20;
		int Qty2 = 75;
		try
		{
			Thread.sleep(3000);
        	Date date = new Date();
        	objPresto.PlaceOrder("omnesys","CM",strScrib1,String.valueOf(intSecid1) ,"22-09-2018","A29",String.valueOf(Qty2),"0.0","0.0","na","0.0","MARKET","Presto_Mathsartz_Strategy","TestOrder2","DAY","SELL");
            dbobj.executeNonQuery("UPDATE TBL_F1_TRADES set SELLPRICE ='"+String.valueOf(dblSell1)+"' WHERE TRADESUBJECTID='"+String.valueOf(intSecid1)+"'");
            DecimalFormat df = new DecimalFormat();
        	df.setMaximumFractionDigits(2);
            double F1PC=0.0, F1TC=2, F1PL=0.01 ;
        	F1PL = dblSell1 - dblBuy1;
        	double avg = (dblSell1+dblBuy1) / 2;
        	F1PC = (F1PL * 100) / avg;
        	dbobj.executeNonQuery("UPDATE TBL_TRADEBOARD SET F1PC="+df.format(F1PC)+" , F1TC= "+F1TC+", F1PL="+df.format(F1PL)+"  WHERE TRADESECID ='"+String.valueOf(intSecid1)+"'");
            System.out.print("\n First Sell Order Placed at :" + date);
            //secandBuy();
		}
		catch(Exception ex)
		{
			Logger.error(ex);
		}
		
	}
	
	public void secandBuy()
	{
		String strScrib2 = "NIFTY";
		int   intSecid2 = 57025;
		double dblBuy2 = 10780.60;
		int lotsize = 1;
		try
		{
			Thread.sleep(3000);
        	Date date = new Date();
        	objPresto.PlaceOrder("omnesys","FUT",strScrib2,String.valueOf(intSecid2) ,"22-02-2018","A29",String.valueOf(lotsize),"0.0","0.0","na","0.0","MARKET","Presto_Mathsartz_Strategy","TestOrder3","DAY","BUY");
            dbobj.executeNonQuery("INSERT INTO TBL_F1_TRADES (FEEDSUBJECTID,TRADESUBJECTID,BUYPRICE) VALUES ('"+String.valueOf(intSecid2)+"','"+String.valueOf(intSecid2)+"','"+String.valueOf(dblBuy2)+"') ");           
        	System.out.print("\n Secand Buy Order Placed at :" + date);
            secandSell();
		}
		catch(Exception ex)
		{
			Logger.error(ex);
		}
		
	}
	
	public void secandSell()
	{
		String strScrib2 = "NIFTY";
		int   intSecid2 = 57025;
		double dblBuy2 = 10780.60;
		double dblSell2 = 10784.91;
		int lotsize = 1;
		try
		{
			Thread.sleep(3000);
        	Date date = new Date();
        	objPresto.PlaceOrder("omnesys","FUT",strScrib2,String.valueOf(intSecid2) ,"22-02-2018","A29",String.valueOf(lotsize),"0.0","0.0","na","0.0","MARKET","Presto_Mathsartz_Strategy","TestOrder4","DAY","SELL");
        	dbobj.executeNonQuery("UPDATE TBL_F1_TRADES set SELLPRICE ='"+String.valueOf(dblSell2)+"' WHERE TRADESUBJECTID='"+String.valueOf(intSecid2)+"'");	            	
        	DecimalFormat df = new DecimalFormat();
        	df.setMaximumFractionDigits(2);
        	double F1PC=0.0, F1TC=2, F1PL=0.01 ;
        	F1PL = dblSell2 - dblBuy2;

        	double avg = (dblSell2+dblBuy2) / 2;
        	F1PC = (F1PL * 100) / avg;
        	dbobj.executeNonQuery("UPDATE TBL_TRADEBOARD SET F1PC="+df.format(F1PC)+" , F1TC= "+F1TC+", F1PL="+df.format(F1PL)+"  WHERE TRADESECID ='"+String.valueOf(intSecid2)+"'");
        	System.out.print("\n Secand Sell Order Placed at :" + date);         
		}
		catch(Exception ex)
		{
			Logger.error(ex);
		}
		
	}
	
	
	///////////////////Dummy Execution Ends ///////////////////////
	
	
	private void dCSV(String [][] recs)
    {
	    	String dir = System.getProperty("user.dir");
			String Sep= System.getProperty("file.separator");
	        BufferedWriter bWrite1 = null;
	        String timeStamp = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss", Locale.ENGLISH).format(new Date());
	        File myFile = new File(dir+Sep+timeStamp+".csv"); 
			try
			{
				 if(!(myFile.exists()))
				 { 
			            myFile.createNewFile(); 
			            System.out.println("New File created...."); 
			     }
				 else
				 {
					 myFile.delete();
					 myFile.createNewFile();
				 }
				 bWrite1 = new BufferedWriter(new java.io.FileWriter(myFile));
				 for(int i=0; i < recs.length; i++)
				 {
					 if (i==0)
					 {
						 bWrite1.write("SCRIB, Feedsecid , Tradesecid, F1 P/L,F1 %, F1 COUNT, F2 P/L,F2 %, F2 COUNT, F3 P/L,F3 %, F3 COUNT, F4 P/L,F4 %, F4 COUNT, F5 P/L,F5 %, F5 COUNT, F6 P/L,F6 %, F6 COUNT, F7 P/L,F7 %, F7 COUNT");
						 bWrite1.newLine();
					 }
					 bWrite1.write(recs[i][0]+","+ recs[i][1]+","+recs[i][2]+","+recs[i][3]+","+recs[i][4]+","+recs[i][5]+","+recs[i][6]+","+recs[i][7]+","+recs[i][8]+","+recs[i][9]+","+recs[i][10]+","+recs[i][11]+","+recs[i][12]+","+recs[i][13]+","+recs[i][14]+","+recs[i][15]+","+recs[i][16]+","+recs[i][17]+","+recs[i][18]+","+recs[i][19]+","+recs[i][20]+","+recs[i][21]+","+recs[i][22]+","+recs[i][23]);
					 bWrite1.newLine();
				 }
				 bWrite1.flush();
				 
			}
			catch(Exception ex)
			{
				if (bWrite1 != null) try {
					bWrite1.close();
					
				 } catch (IOException ioe2) {
				    // just ignore it
				 }
			}
			finally
			{
				
			}
    }


				/*
				 * Outerpane class is for handling all main tradegrid functionality
				 */
				
				public class Outerpane extends JPanel implements KeyListener
				{
					JTable table;
					PLTableModel model;
				    public Outerpane() {
				    	 model = new PLTableModel();
				    	 table = new JTable(model) {
				    		 
								public Component prepareRenderer(TableCellRenderer renderer, int row, int column)
								{
									Component c = super.prepareRenderer(renderer, row, column);
									c.setSize(5,100);
									
									if ( (column % 3 != 0) && (column > 0))
									{
										if (model.getValueAt(row, column).toString() !="")
										{
				    						double value = Double.parseDouble(model.getValueAt(row, column).toString());
					    						
					    						if (value > 0)
					    						{
					    						c.setForeground(new Color(255,220,135));
					    						c.setFont(new Font("Tahoma", Font.BOLD, 12));
					    						}
					    						else if (value == 0)
					    						{
					    							c.setFont(new Font("Tahoma", Font.PLAIN, 0));
					    						}
					    						else
					    						{
					    						c.setForeground(new Color(103,186,233));
					    						c.setFont(new Font("Tahoma", Font.BOLD, 12));
					    						}
										}
										
									}
									else
									{
										c.setForeground(Color.WHITE);
										c.setFont(new Font("Tahoma", Font.BOLD, 12));
										if (model.getValueAt(row, column).toString().equals("0"))
										{
											c.setFont(new Font("Tahoma", Font.PLAIN, 0));
										}
									}
									
									if (column==0)
									{
									((JLabel) c).setHorizontalAlignment(JLabel.LEFT);
									
									}
									else
									{
										((JLabel) c).setHorizontalAlignment(JLabel.RIGHT);
									}
									if (!isRowSelected(row))
										if (row % 2 == 0)
										{
										c.setBackground(new Color(58,54,51));
										}
										else
										{
										c.setBackground(new Color(79,75,72));
										}
									return c;
								}    
							};
				        
				        
						table.changeSelection(0, 0, false, false);
						table.setBorder(null);
						table.setGridColor(new Color(90,86,83));
						table.setForeground(Color.WHITE);
						table.setDefaultEditor(Object.class, null);
						table.addKeyListener(this);
						table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
						//table.setBackground(new Color(36,34,29));
						table.addMouseListener(new MouseAdapter(){
						     public void mouseClicked(MouseEvent e){
						      if (e.getClickCount() == 2){
						    	  Player pl=new Player((table.getValueAt(table.getSelectedRow(), 0).toString().split("-")[2]));
						         //loadExistingData(table.getValueAt(table.getSelectedRow(), 0).toString());
						         }
						      }
						     } );
						
						JTableHeader header = table.getTableHeader();
						//header.setBackground(new Color(36,34,29));
					    header.setForeground(new Color(36,34,29));
					    header.setFont(new Font("Tahoma", Font.BOLD, 13));
					   // table.add(header);
					    
					    
					    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
					    double width = frmTradeBoard.getWidth();
					    double height = frmTradeBoard.getHeight();
					    
					    scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
						scrollPane.setEnabled(false);
						scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
						scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
						//scrollPane.setViewportBorder(null);
						//scrollPane.setBorder(null);
						scrollPane.setPreferredSize(new Dimension((int)width-25, (int)height-200));
						scrollPane.setBackground(new Color(36,34,29));
						
				        add(scrollPane,BorderLayout.CENTER);
				
				        SwingUtilities.invokeLater(new Runnable() {
				            @Override
				            public void run() {
				            	try {
				                	
				                	Timer timer = new Timer(0, new ActionListener() {
				
				                		   @Override
				                		   public void actionPerformed(ActionEvent e) {
				                			   try {
				                				   int selectedrow = table.getSelectedRow();
				                		            if (selectedrow != -1)
				                		            {
				                		            		selectedrow = table.getSelectedRow();
				                		            }
				                				   model.clear();
											   model.refresh();
											   TableColumn column = table.getColumnModel().getColumn(0);
											    column.setPreferredWidth(225);
											   model.SumPL();
											    if (selectedrow != -1)
									            {
											    	 if (model.getRowCount() > 0)
											    	 {
									            		table.setRowSelectionInterval(selectedrow, selectedrow);
											    	 }
									            } 
											} catch (SQLException ex) {
												// TODO Auto-generated catch block
												//ex.printStackTrace();
											}
				                		   }
				                		});
				
				                		timer.setDelay(8000); // delay for 30 seconds
				                		timer.start();
				                	
				                   
				                }  catch (Exception ex) {
				                    ex.printStackTrace();
				                }
				            }
				        });
				        
				    }
				    
				
				
					@Override
					public void keyTyped(KeyEvent e) {
						// TODO Auto-generated method stub
				
					}
				
				
					@Override
					public void keyPressed(KeyEvent e) {
						// TODO Auto-generated method stub
						
						if (e.isControlDown() && e.getKeyCode() == 72) 
						{
							// CTRL + h
							HeadFeeds hf=new HeadFeeds(null);
			            }
						else if (e.isControlDown() && e.getKeyCode() == 80)
						{
							// CTRL + P
							// Add new player
							Player pl=new Player(null);
						}
						else if (e.isControlDown() && e.getKeyCode() == 68)
						{
							// CTRL + D 
							// Delete Existing player
							String playerid = table.getValueAt(table.getSelectedRow(), 0).toString().split("-")[2];
							int opcion = JOptionPane.showConfirmDialog(null, "Are you sure ?", "Delete Player", JOptionPane.YES_NO_OPTION);
							if (opcion == 0) { //The ISSUE is here
								dbobj.executeNonQuery("DELETE FROM TBL_PLAYERS WHERE TRADESUBJECTID='"+playerid+"'");
								dbobj.executeNonQuery("DELETE FROM TBL_TRADEBOARD WHERE TRADESECID='"+playerid+"'");
							} 
						}
						else if (e.isControlDown() && e.getKeyCode() == 49)
						{
							// CTRL + 1
							String feedid = table.getValueAt(table.getSelectedRow(), 0).toString().split("-")[0];
							String playerid = table.getValueAt(table.getSelectedRow(), 0).toString().split("-")[2];
							FormulaInputs fobj=new FormulaInputs(feedid, playerid, "F1");
						}
						else if (e.isControlDown() && e.getKeyCode() == 50)
						{
							// CTRL + 2
							String feedid = table.getValueAt(table.getSelectedRow(), 0).toString().split("-")[0];
							String playerid = table.getValueAt(table.getSelectedRow(), 0).toString().split("-")[2];
							FormulaInputs fobj=new FormulaInputs(feedid, playerid, "F2");
							
						}
						else if (e.isControlDown() && e.getKeyCode() == 51)
						{
							// CTRL + 3
							String feedid = table.getValueAt(table.getSelectedRow(), 0).toString().split("-")[0];
							String playerid = table.getValueAt(table.getSelectedRow(), 0).toString().split("-")[2];
							FormulaInputs fobj=new FormulaInputs(feedid, playerid, "F3");
							
						}
						else if (e.isControlDown() && e.getKeyCode() == 52)
						{
							// CTRL + 4
							String feedid = table.getValueAt(table.getSelectedRow(), 0).toString().split("-")[0];
							String playerid = table.getValueAt(table.getSelectedRow(), 0).toString().split("-")[2];
							FormulaInputs fobj=new FormulaInputs(feedid, playerid, "F4");
						}
						else if (e.isControlDown() && e.getKeyCode() == 53)
						{
							// CTRL + 5
							String feedid = table.getValueAt(table.getSelectedRow(), 0).toString().split("-")[0];
							String playerid = table.getValueAt(table.getSelectedRow(), 0).toString().split("-")[2];
							FormulaInputs fobj=new FormulaInputs(feedid, playerid, "F5");
						}
						else if (e.isControlDown() && e.getKeyCode() == 54)
						{
							// CTRL + 6
							String feedid = table.getValueAt(table.getSelectedRow(), 0).toString().split("-")[0];
							String playerid = table.getValueAt(table.getSelectedRow(), 0).toString().split("-")[2];
							FormulaInputs fobj=new FormulaInputs(feedid, playerid, "F6");
						}
						else if (e.isControlDown() && e.getKeyCode() == 55)
						{
							// CTRL + 7
							String feedid = table.getValueAt(table.getSelectedRow(), 0).toString().split("-")[0];
							String playerid = table.getValueAt(table.getSelectedRow(), 0).toString().split("-")[2];
							FormulaInputs fobj=new FormulaInputs(feedid, playerid, "F7");
						}
					}
				
					@Override
					public void keyReleased(KeyEvent e) {
						
					}
				
				}
				
				public class PLTableModel extends AbstractTableModel {

			        private List<Pldata> Pldata = new ArrayList<>(100);
			        private List<String> columnNames = new ArrayList<>(100);
			        
			        public void clear()
		            {
		            	Pldata.clear();
		            }
			        
			        public void SumPL()
		            {
			        		Connection conn = null; 
			        		Statement stmt = null;
			        		DecimalFormat f = new DecimalFormat("##.00");
			        		try
			        		{
			        			 JdbcDataSource ds = new JdbcDataSource();
			        	         ds.setURL(url);
			        	         conn = ds.getConnection(USER,PASS);
			       	         stmt = conn.createStatement();
			       	         stmt.execute("SELECT SUM(F1PL) as F1Sum, SUM(F2PL) as F2Sum, SUM(F3PL) as F3Sum, SUM(F4PL) as F4Sum, SUM(F5PL) as F5Sum" + 
			       	         		", SUM(F6PL) as F6Sum, SUM(F7PL) as F7Sum FROM TBL_TRADEBOARD; ");
			       	         ResultSet rs =stmt.getResultSet(); 
			       	         rs.next();
			       	         if (rs !=null)
			       	         {
			       	        	    double overallpl = Double.valueOf(f.format(rs.getDouble("F1Sum"))) +Double.valueOf(f.format(rs.getDouble("F2Sum")))+Double.valueOf(f.format(rs.getDouble("F3Sum")))
			       	        	    +Double.valueOf(f.format(rs.getDouble("F4Sum")))+Double.valueOf(f.format(rs.getDouble("F5Sum")))+Double.valueOf(f.format(rs.getDouble("F6Sum")))+Double.valueOf(f.format(rs.getDouble("F7Sum")));
			       	        	    if (overallpl > 0)
			       	        	    {
			       	        	    		lblPandL.setForeground(Color.GREEN);
			       	        	    		lblPandL.setText("++ " + String.valueOf(overallpl));
			       	        	    }
			       	        	    else
			       	        	    {
			       	        	    		lblPandL.setForeground(Color.RED);
			       	        	    		lblPandL.setText("-- " + String.valueOf(overallpl));
			       	        	    }
			       	        	    
			       	        	    
			       	        	 	f1PL.setText(Double.valueOf(f.format(rs.getDouble("F1Sum"))).toString());
		                         if (Double.valueOf(f.format(rs.getDouble("F1Sum"))) < 0) 
		                         {
		                        	 	f1PL.setForeground(new Color(103,186,233));
		                        	}       
		                         else
		                         {
		                        	 f1PL.setForeground(new Color(255,220,135));
		                         }
			       	        	 	f2PL.setText(Double.valueOf(f.format(rs.getDouble("F2Sum"))).toString());
			       	        	 	if (Double.valueOf(f.format(rs.getDouble("F2Sum")))<0)
		                         {
			       	        	 		f2PL.setForeground(new Color(103,186,233));
		                        	}  
			       	        	 	else
		                         {
			       	        	 	f2PL.setForeground(new Color(255,220,135));
		                         }
			       	        	 	f3PL.setText(Double.valueOf(f.format(rs.getDouble("F3Sum"))).toString());
			       	        	 	if (Double.valueOf(f.format(rs.getDouble("F3Sum")))<0)
		                         {
			       	        	 		f3PL.setForeground(new Color(103,186,233));
		                        	}  
			       	        	 	else
		                         {
			       	        	 	f3PL.setForeground(new Color(255,220,135));
		                         }
			       	        	 	f4PL.setText(Double.valueOf(f.format(rs.getDouble("F4Sum"))).toString());
			       	        	 	if (Double.valueOf(f.format(rs.getDouble("F4Sum")))<0)
		                         {
			       	        	 		f4PL.setForeground(new Color(103,186,233));
		                        	}  
			       	        	 	else
		                         {
			       	        	 	f4PL.setForeground(new Color(255,220,135));
		                         }
			       	        	 	f5PL.setText(Double.valueOf(f.format(rs.getDouble("F5Sum"))).toString());
			       	        	 	if (Double.valueOf(f.format(rs.getDouble("F5Sum")))<0)
		                         {
			       	        	 		f5PL.setForeground(new Color(103,186,233));
		                        	}  
			       	        	 	else
		                         {
			       	        	 	f5PL.setForeground(new Color(255,220,135));
		                         }
			       	        	 	
			       	        	 	f6PL.setText(Double.valueOf(f.format(rs.getDouble("F6Sum"))).toString());
			       	        	 	if (Double.valueOf(f.format(rs.getDouble("F6Sum")))<0)
		                         {
			       	        	 		f6PL.setForeground(new Color(103,186,233));
		                        	}  
			       	        	 	else
		                         {
			       	        	 	f6PL.setForeground(new Color(255,220,135));
		                         }
			       	        	 	
			       	        	 	f7PL.setText(Double.valueOf(f.format(rs.getDouble("F7Sum"))).toString());
			       	        	 	if (Double.valueOf(f.format(rs.getDouble("F7Sum")))<0)
		                         {
			       	        	 		f7PL.setForeground(new Color(103,186,233));
		                        	}  
			       	        	 	else
		                         {
			       	        	 	f7PL.setForeground(new Color(255,220,135));
		                         }
			       	         }
			       	         rs.close();
			        		}
			        		catch(Exception ex)
			        		{
			        			Logger.error(ex);
			        		}
			        		finally
			        		{
			        			try { 
			        	            if(stmt!=null) stmt.close(); 
			        	         } catch(SQLException se2) { 
			        	         } 
			        	         try { 
			        	            if(conn!=null) conn.close(); 
			        	         } catch(SQLException se) { 
			        	        	 	Logger.error(se);
			        	         } 	
			        		}
		            }
			        

			        @Override
			        public int getRowCount() {
			            return Pldata.size();
			        }

			        @Override
			        public int getColumnCount() {
			            return columnNames.size();
			        }

			        @Override
			        public String getColumnName(int column) {
			            return columnNames.get(column);
			        }

			        @Override
			        public Object getValueAt(int rowIndex, int columnIndex) {
			            Pldata rowValue = Pldata.get(rowIndex);
			            Object value = null;
			            switch (columnIndex) {
			                case 0:
			                    value = rowValue.getSymbol();
			                    break;
			                case 1:
			                    value = rowValue.getf1PL();
			                    break;
			                case 2:
			                    value = rowValue.getf1Percent();
			                    break;
			                case 3:
			                    value = rowValue.getf1TradeCount();
			                    break;
			                case 4:
			                    value = rowValue.getf2PL();
			                    break;
			                case 5:
			                    value = rowValue.getf2Percent();
			                    break;
			                case 6:
			                    value = rowValue.getf2TradeCount();
			                    break;
			                case 7:
			                    value = rowValue.getf3PL();
			                    break;
			                case 8:
			                    value = rowValue.getf3Percent();
			                    break;
			                case 9:
			                    value = rowValue.getf3TradeCount();
			                    break;
			                case 10:
			                    value = rowValue.getf4PL();
			                    break;
			                case 11:
			                    value = rowValue.getf4Percent();
			                    break;
			                case 12:
			                    value = rowValue.getf4TradeCount();
			                    break;
			                case 13:
			                    value = rowValue.getf5PL();
			                    break;
			                case 14:
			                    value = rowValue.getf5Percent();
			                    break;
			                case 15:
			                    value = rowValue.getf5TradeCount();
			                    break;
			                case 16:
			                    value = rowValue.getf6PL();
			                    break;
			                case 17:
			                    value = rowValue.getf6Percent();
			                    break;
			                case 18:
			                    value = rowValue.getf6TradeCount();
			                    break;
			                case 19:
			                    value = rowValue.getf7PL();
			                    break;
			                case 20:
			                    value = rowValue.getf7Percent();
			                    break;
			                case 21:
			                    value = rowValue.getf7TradeCount();
			                    break;		                
			            }
			            return value;
			        }

			        public void refresh() throws SQLException 
			        {
			            List<String> values = new ArrayList<>(100);
			            try (PreparedStatement ps = con.prepareStatement("SELECT concat(FEEDSECID,'-',TSCRIB,'-',TRADESECID) as SCRIB" + 
			            		", F1PL as \"F1 P/L\",F1PC as \"F1 %\", F1TC as \"F1 COUNT\"" + 
			            		", F2PL as \"F2 P/L\",F2PC as \"F2 %\", F2TC as \"F2 COUNT\"" + 
			            		", F3PL as \"F3 P/L\",F3PC as \"F3 %\", F3TC as \"F3 COUNT\"" + 
			            		", F4PL as \"F4 P/L\",F4PC as \"F4 %\", F4TC as \"F4 COUNT\"" + 
			            		", F5PL as \"F5 P/L\",F5PC as \"F5 %\", F5TC as \"F5 COUNT\"" + 
			            		", F6PL as \"F6 P/L\",F6PC as \"F6 %\", F6TC as \"F6 COUNT\"" + 
			            		",F7PL as \"F7 P/L\", F7PC as \"F7 %\", F7TC as \"F7 COUNT\"" + 
			            		"from TBL_TRADEBOARD order by ID ASC;")) {
			                try (ResultSet rs = ps.executeQuery()) {
			                    ResultSetMetaData md = rs.getMetaData();
			                    for (int col = 0; col < md.getColumnCount(); col++) 
			                    {
			                        values.add(md.getColumnLabel(col + 1));
			                    }
			                    while (rs.next()) {
			                    		Pldata list = new Pldata(rs.getString("SCRIB"), rs.getDouble("F1PL"), rs.getDouble("F1PC"),rs.getInt("F1TC")
			                    					  ,rs.getDouble("F2PL"), rs.getDouble("F2PC"),rs.getInt("F2TC"), rs.getDouble("F3PL"), rs.getDouble("F3PC"),rs.getInt("F3TC")
			                    					  ,rs.getDouble("F4PL"), rs.getDouble("F4PC"),rs.getInt("F4TC"), rs.getDouble("F5PL"), rs.getDouble("F5PC"),rs.getInt("F5TC")
			                    					  ,rs.getDouble("F6PL"), rs.getDouble("F6PC"),rs.getInt("F6TC"), rs.getDouble("F7PL"), rs.getDouble("F7PC"),rs.getInt("F7TC")
			                    					  );
			                        Pldata.add(list);
			                    }
			                }
			            } finally {
			                if (columnNames.size() != values.size()) {
			                    columnNames = values;
			                    fireTableStructureChanged();
			                } else {
			                    fireTableDataChanged();
			                }
			            }

			        }

			        public class Pldata {

			            private long id;
			            private String symbol;
			            private int f1Trade,f2Trade,f3Trade,f4Trade, f5Trade, f6Trade, f7Trade;
			            private double f1PL,f1Percent,f2PL,f2Percent,f3PL,f3Percent,f4PL,f4Percent,f5PL,f5Percent,f6PL,f6Percent,f7PL,f7Percent,first,last;

			            public Pldata(String symbol, double f1PL, double f1Percent, int f1Trade, double f2PL, double f2Percent, int f2Trade, double f3PL, double f3Percent, int f3Trade, 
								double f4PL, double f4Percent, int f4Trade, double f5PL, double f5Percent, int f5Trade, double f6PL, double f6Percent, int f6Trade, double f7PL, double f7Percent, int f7Trade) {
			                //this.id = id;
			                this.symbol = symbol;
			                
			                this.f1PL = f1PL;
			                this.f1Percent = f1Percent;
			                this.f1Trade = f1Trade;
			                
			                this.f2PL = f2PL;
			                this.f2Percent = f2Percent;
			                this.f2Trade = f2Trade;
			                
			                this.f3PL = f3PL;
			                this.f3Percent = f3Percent;
			                this.f3Trade = f3Trade;
			                
			                this.f4PL = f4PL;
			                this.f4Percent = f4Percent;
			                this.f4Trade = f4Trade;
			                
			                this.f5PL = f5PL;
			                this.f5Percent = f5Percent;
			                this.f5Trade = f5Trade;
			                
			                this.f6PL = f7PL;
			                this.f6Percent = f7Percent;
			                this.f6Trade = f7Trade;
			                
			                this.f7PL = f7PL;
			                this.f7Percent = f7Percent;
			                this.f7Trade = f7Trade;
			               
			            }



					//public long getId() {return id;}
			           
			           public String getSymbol() {return symbol;}

			           public double getf1PL() {return f1PL;}
			           public double getf1Percent() {return f1Percent;}
			           public int getf1TradeCount() {return f1Trade;}
			           
			           public double getf2PL() {return f2PL;}
			           public double getf2Percent() {return f2Percent;}
			           public int getf2TradeCount() {return f2Trade;}
			           
			           public double getf3PL() {return f3PL;}
			           public double getf3Percent() {return f3Percent;}
			           public int getf3TradeCount() {return f3Trade;}
			           
			           public double getf4PL() {return f4PL;}
			           public double getf4Percent() {return f4Percent;}
			           public int getf4TradeCount() {return f4Trade;}
			           
			           public double getf5PL() {return f5PL;}
			           public double getf5Percent() {return f5Percent;}
			           public int getf5TradeCount() {return f5Trade;}
			           
			           public double getf6PL() {return f6PL;}
			           public double getf6Percent() {return f6Percent;}
			           public int getf6TradeCount() {return f6Trade;}
			           
			           public double getf7PL() {return f7PL;}
			           public double getf7Percent() {return f7Percent;}
			           public int getf7TradeCount() {return f7Trade;}
			           
			           public double getFirst() {return first;}
			           public double getLast() {return last;}
			    	
			    	}

			    }
}



