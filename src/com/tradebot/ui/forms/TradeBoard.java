package com.tradebot.ui.forms;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;

import javax.swing.JFrame;

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
		frmTradeBoard.setBounds(100, 100, 450, 300);
		frmTradeBoard.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTradeBoard.setBackground(new Color(36,34,29));
		frmTradeBoard.getContentPane().setBackground(new Color(51, 51, 51));
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    double width = screenSize.getWidth();
	    double height = screenSize.getHeight();
	    frmTradeBoard.setSize((int)width, (int)height);
	    
	}

}
