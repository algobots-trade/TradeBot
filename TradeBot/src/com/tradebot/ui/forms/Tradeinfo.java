package com.tradebot.ui.forms;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import org.pmw.tinylog.Logger;

import com.tradebot.dbcommons.db_commons;
import javax.swing.JLabel;
import javax.swing.SwingConstants;


public class Tradeinfo {

	private JFrame TradeInfofrm;
	private JTable tbltradeinfo;
	db_commons dbobj=new db_commons();
	String records[][];
	String col[]= {"BUY TIME" ,"BUY PRICE","SELL TIME" ,"SELL PRICE"};
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Tradeinfo window = new Tradeinfo("3045","3045","F2");
					window.TradeInfofrm.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Tradeinfo(String feedid, String tradeid, String formulaname) {
		initialize(feedid,tradeid,formulaname);
	}

    public String gettradeinfoquery(String feedid, String tradeid, String formulaname )
    {
    	String Qstr="";
    	String strtblName="";
    	switch (formulaname) {
		case "F1":
			strtblName ="TBL_F1_HRUN_TRADES";
			break;
		case "F2":
			strtblName ="TBL_F2_HCAPTURE_TRADES";
			break;
		case "F3":
			strtblName = "TBL_F3_DUMMY_TRADES";
			break;	
		case "F4":
			strtblName = "TBL_F4_HRUN_TRADES"; 
			break;
		case "F5":
			strtblName = "TBL_F5_HCAPTURE_TRADES";
			break;
		case "F6":
			strtblName = "TBL_F6_HRUN_TRADES";
			break;	
		case "F7":
			strtblName = "TBL_F7_HCAPTURE_TRADES";
			break;

		
		}
    	return Qstr ="SELECT ENTRYTIME, BUYPRICE, SELLPRICE, EXITTIME, ISSHOTSELL, ENTRYID, EXITID  FROM "+strtblName+" WHERE FEEDSECID = '"+feedid+"' AND TRADESECID='"+tradeid+"' AND TCOUNT !=0" ;
    }
    
    public String [][] gettradeinfo(String feedid, String tradeid, String formulaname)
    {
    	String [][] info = null;
    	try
    	{
    		records = dbobj.getMultiColumnRecords(null,gettradeinfoquery(feedid,tradeid, formulaname));
    		info = new String[records.length][4];
    		for(int i=0; i<records.length; i++) 
    		{
    			info[i][1] = records[i][1];
    			info[i][3] = records[i][2];
    			//Thu May 03 12:23:40 IST 2018
    			
    			//Tue, 02 Jan 2018 18:07:59 IST  E, dd MMM yyyy HH:mm:ss z
    			SimpleDateFormat dt = new SimpleDateFormat("E MMM dd hh:mm:ss z yyyy"); 
				Date d = null;
				if (records[i][3] !=null)
				{
					dt.parse(records[i][3]); 
				}
				SimpleDateFormat dt1 = new SimpleDateFormat("hh:mm:ss");
				Date d1 = null;
				if (records[i][0] !=null)
				{
					dt.parse(records[i][0]); 
				}
    			if(records[i][4].equals("true"))
    			{
    				info[i][0] =dt1.format(d).toString();
    				info[i][2] =dt1.format(d1).toString();
    			}
    			else
    			{
    				info[i][0] = dt1.format(d1).toString();
    				info[i][2] = dt1.format(d).toString();
    			}
    		}	
    	}
    	catch(Exception ex)
    	{
    		Logger.error(ex);
    	}
    	return info;
    }
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(String feedid, String tradeid, String formulaname) {
		TradeInfofrm = new JFrame();
		TradeInfofrm.getContentPane().setBackground(new Color(36,34,29));
		TradeInfofrm.getContentPane().setLayout(null);
		TradeInfofrm.setVisible(true);
		JPanel mainpanel = new JPanel();
		mainpanel.setBackground(new Color(36,34,29));
		mainpanel.setBounds(0, 53, 449, 338);
		TradeInfofrm.getContentPane().add(mainpanel);
		//records =dbobj.getMultiColumnRecords(gettradeinfoquery(feedid,tradeid, formulaname));
		
		TableModel model = new DefaultTableModel(gettradeinfo(feedid, tradeid, formulaname), col);
		tbltradeinfo = new JTable(){
		    public Component prepareRenderer(TableCellRenderer renderer, int row, int column){
		        Component returnComp = super.prepareRenderer(renderer, row, column);
		        Color alternateColor = new Color(58,54,51);
		        Color whiteColor = new Color(79,75,72);
		        if (!returnComp.getBackground().equals(getSelectionBackground())){
		            Color bg = (row % 2 == 0 ? alternateColor : whiteColor);
		            returnComp .setBackground(bg);
		            returnComp.setForeground(Color.WHITE);
		            bg = null;
		        }
		        return returnComp;
		    }
		    @Override
		    public boolean isCellEditable(int i, int i1) {
		        return false; //To change body of generated methods, choose Tools | Templates.
		    }
		};
		tbltradeinfo.setFont(new Font("Tahoma", Font.PLAIN, 15));
		//mainpanel.add(tbltradeinfo);
		tbltradeinfo.setBackground(new Color(51, 51, 51));
		tbltradeinfo.setFillsViewportHeight(true);
		tbltradeinfo.setModel(model);	
		JTableHeader header = tbltradeinfo.getTableHeader();
		header.setForeground(new Color(36,34,29));
	    header.setFont(new Font("Tahoma", Font.BOLD, 13));
	    mainpanel.setLayout(null);
	    JScrollPane scrollPane = new JScrollPane(tbltradeinfo);
	    scrollPane.setBounds(10, 5, 424, 321);
	    mainpanel.add(scrollPane);
	    scrollPane.setEnabled(false);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setViewportBorder(null);
		scrollPane.setBorder(null);
		
		JLabel lblTradeInfo = new JLabel("TRADE INFO");
		lblTradeInfo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTradeInfo.setForeground(new Color(255, 220, 135));
		lblTradeInfo.setFont(new Font("Verdana", Font.BOLD, 22));
		lblTradeInfo.setBounds(0, 0, 466, 43);
		TradeInfofrm.getContentPane().add(lblTradeInfo);
		TradeInfofrm.setBounds(100, 100, 461, 429);
		TradeInfofrm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
	}
}
