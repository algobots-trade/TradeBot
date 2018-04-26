package com.tradebot.ui.forms;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import com.tradebot.dbcommons.db_commons;
import javax.swing.JLabel;
import javax.swing.SwingConstants;


public class Tradeinfo {

	private JFrame TradeInfofrm;
	private JTable tbltradeinfo;
	db_commons dbobj=new db_commons();
	String records[][];
	String col[]= {"FEEDSECID" ,"TRADESECID" ,"BUYPRICE" ,"SELLPRICE","ISSHOTSELL" ,"ENTRYTIME" ,"EXITTIME" ,"ENTRYID" ,"EXITID" ,"EXITCONDITION"};
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Tradeinfo window = new Tradeinfo("50496","52380","F1");
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
			strtblName ="TBL_F2_HRUN_TRADES";
			break;
		case "F3":
			strtblName = "TBL_F3_HRUN_TRADES";
			break;	
		case "F4":
			strtblName = "TBL_F4_HCAPTURE_TRADES"; 
			break;
		case "F5":
			strtblName = "TBL_F5_HCAPTURE_TRADES";
			break;
		case "F6":
			strtblName = "TBL_F6_HCAPTURE_TRADES";
			break;	
		case "F7":
			strtblName = "TBL_F7_DUMMY_TRADES";
			break;

		
		}
    	return Qstr ="SELECT FEEDSECID ,TRADESECID ,BUYPRICE ,SELLPRICE,ISSHOTSELL ,ENTRYTIME ,EXITTIME ,ENTRYID ,EXITID ,EXITCONDITION   FROM "+strtblName+" WHERE FEEDSECID = '"+feedid+"' AND TRADESECID='"+tradeid+"'" ;
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
		mainpanel.setBounds(10, 53, 1019, 452);
		TradeInfofrm.getContentPane().add(mainpanel);
		records =dbobj.getMultiColumnRecords(gettradeinfoquery(feedid,tradeid, formulaname));
		TableModel model = new DefaultTableModel(records, col);
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
		//mainpanel.add(tbltradeinfo);
		tbltradeinfo.setBackground(new Color(51, 51, 51));
		tbltradeinfo.setFillsViewportHeight(true);
		tbltradeinfo.setModel(model);	
		JTableHeader header = tbltradeinfo.getTableHeader();
		header.setForeground(new Color(36,34,29));
	    header.setFont(new Font("Tahoma", Font.PLAIN, 13));
	    mainpanel.setLayout(null);
	    JScrollPane scrollPane = new JScrollPane(tbltradeinfo);
	    scrollPane.setBounds(10, 5, 999, 439);
	    mainpanel.add(scrollPane);
	    scrollPane.setEnabled(false);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setViewportBorder(null);
		scrollPane.setBorder(null);
		
		JLabel lblTradeInfo = new JLabel("TRADE INFO");
		lblTradeInfo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTradeInfo.setForeground(new Color(255, 220, 135));
		lblTradeInfo.setFont(new Font("Verdana", Font.BOLD, 22));
		lblTradeInfo.setBounds(0, -1, 1039, 43);
		TradeInfofrm.getContentPane().add(lblTradeInfo);
		TradeInfofrm.setBounds(100, 100, 1055, 550);
		TradeInfofrm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
	}
}
