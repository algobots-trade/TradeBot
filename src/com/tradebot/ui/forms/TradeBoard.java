package com.tradebot.ui.forms;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import javax.swing.JLabel;

public class TradeBoard {

	private JFrame frmTradeBoard;

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

	/**
	 * Create the application.
	 */
	public TradeBoard() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frmTradeBoard = new JFrame();
		frmTradeBoard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTradeBoard.setBackground(new Color(36,34,29));
		frmTradeBoard.getContentPane().setBackground(new Color(51, 51, 51));
		
		
		//maximize the window 
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    double width = screenSize.getWidth();
	    double height = screenSize.getHeight();
	    frmTradeBoard.setSize((int)width, (int)height);
	    
	    JPanel pnltotal = new JPanel();
	    pnltotal.setBackground(new Color(36,34,29));
		frmTradeBoard.getContentPane().add(pnltotal, BorderLayout.CENTER);
		pnltotal.setLayout(new BorderLayout(0, 0));
		//heading label
		JLabel lblLiveTradeDashboard = new JLabel("Live Trade Dashboard");
		lblLiveTradeDashboard.setHorizontalAlignment(SwingConstants.CENTER);
		lblLiveTradeDashboard.setFont(new Font("Tsukushi A Round Gothic", Font.BOLD, 26));
		lblLiveTradeDashboard.setForeground(new Color(240,159,108));
		pnltotal.add(lblLiveTradeDashboard, BorderLayout.NORTH);
		
		//Center panel for grid 
		JPanel pnlgridpanel = new JPanel();
		pnlgridpanel.setBackground(new Color(36,34,29));
		pnltotal.add(pnlgridpanel, BorderLayout.CENTER);
		
		
		
		
		//Botom panel design		
		JPanel pnlbottom = new JPanel();
		pnlbottom.setBackground(Color.GREEN);
		pnltotal.add(pnlbottom, BorderLayout.SOUTH);
		
		
		
		
		
		
	    
	    
	}

}
