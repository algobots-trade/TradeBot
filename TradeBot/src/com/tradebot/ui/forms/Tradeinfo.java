package com.tradebot.ui.forms;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import com.tradebot.dbcommons.db_commons;


public class Tradeinfo {

	private JFrame TradeInfofrm;
	private JTable tbltradeinfo;
	db_commons dbobj=new db_commons();
	String records[][];
	String col[]= {"FEEDSECID","TRADESECID","SYMBOL","EXCHANGE","INSTRUMENTS","LOT-SIZE","TICK-SIZE","EXPIRY-DD","EXPIRY-MMMYY","OPT-TYPE","STRIKE"};
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Tradeinfo window = new Tradeinfo();
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
	public Tradeinfo() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		TradeInfofrm = new JFrame();
		TradeInfofrm.getContentPane().setBackground(new Color(36,34,29));
		TradeInfofrm.getContentPane().setLayout(null);
		
		JPanel mainpanel = new JPanel();
		mainpanel.setBackground(new Color(36,34,29));
		mainpanel.setBounds(10, 53, 515, 507);
		TradeInfofrm.getContentPane().add(mainpanel);
		records =dbobj.getMultiColumnRecords("");
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
		mainpanel.add(tbltradeinfo);
		TradeInfofrm.setBounds(100, 100, 551, 609);
		TradeInfofrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
}
