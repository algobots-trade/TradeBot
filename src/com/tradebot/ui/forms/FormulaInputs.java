package com.tradebot.ui.forms;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.tradebot.dbcommons.db_commons;

import com.tradebot.ui.forms.*;
import javax.swing.JToggleButton;
import javax.swing.JSlider; 

public class FormulaInputs {

	//private JFrame frame;
	private JFrame contentPane;
	private JTextField txtX;
	private JTextField txtY;
	private JTextField txtZ;
	private JTextField txtT1H;
	private JTextField txtT2H;
	private JTextField txtT3H;
	private JTextField txtT4H;
	private JTextField txtT1M;
	private JTextField txtT1S;
	private JTextField txtT2M;
	private JTextField txtT2S;
	private JTextField txtT3M;
	private JTextField txtT3S;
	private JTextField txtT4M;
	private JTextField txtT4S;
	private JTextField txtLcount;
	private JTextField txtStopL;
	private JLabel lblFTitle;
	private db_commons dbObj;
	public static String dbName= System.getProperty("user.dir")+File.separator+"ovvi_Market_bot;AUTO_SERVER=TRUE";
    public static String USER="admin", PASS="test123";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FormulaInputs window = new FormulaInputs("nill","nill");
					window.contentPane.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public FormulaInputs(String strScrib, String strFname) 
	{
		try
		{
			initialize(strScrib, strFname);
		}
		catch(Exception ex)
		{
			
		}
		finally
		{
			
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(String strScrib, String strFname) 
	{

		InputHandler keyhand = new InputHandler();
		contentPane = new JFrame();
		contentPane.getContentPane().setBackground(new Color(51, 51, 51));
		contentPane.setVisible(true);
		contentPane.setTitle("Formula Input For  "+strScrib+"-"+strFname);
		contentPane.setBounds(100, 100, 410, 682);
		contentPane.setBackground(new Color(36,34,29));
		contentPane.getContentPane().setLayout(null);
		contentPane.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		contentPane.addKeyListener(keyhand);
		

		lblFTitle = new JLabel(strFname);
		lblFTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblFTitle.setFont(new Font("Verdana", Font.BOLD, 22));
		lblFTitle.setForeground(new Color(255, 220, 135));
		lblFTitle.setBounds(6, 6, 398, 43);
		contentPane.getContentPane().add(lblFTitle);
		
		JPanel pnlInput = new JPanel();
		pnlInput.setForeground(Color.WHITE);
		pnlInput.setBounds(32, 46, 347, 592);
		pnlInput.setBackground(new Color(80,75,78));
		contentPane.getContentPane().add(pnlInput);
		pnlInput.setLayout(null);
		
		JLabel lblX = new JLabel("X  =");
		lblX.setBounds(90, 6, 73, 49);
		lblX.setHorizontalAlignment(SwingConstants.LEFT);
		lblX.setForeground(Color.WHITE);
		lblX.setFont(new Font("Verdana", Font.PLAIN, 22));
		pnlInput.add(lblX);
		
		JLabel lblX_1 = new JLabel("Y  =");
		lblX_1.setHorizontalAlignment(SwingConstants.LEFT);
		lblX_1.setForeground(Color.WHITE);
		lblX_1.setFont(new Font("Verdana", Font.PLAIN, 22));
		lblX_1.setBounds(90, 56, 65, 49);
		pnlInput.add(lblX_1);
		
		JLabel lblZ = new JLabel("Z  =");
		lblZ.setHorizontalAlignment(SwingConstants.LEFT);
		lblZ.setForeground(Color.WHITE);
		lblZ.setFont(new Font("Verdana", Font.PLAIN, 22));
		lblZ.setBounds(88, 106, 59, 49);
		pnlInput.add(lblZ);
		
		JLabel lblT = new JLabel("T1  =");
		lblT.setHorizontalAlignment(SwingConstants.LEFT);
		lblT.setForeground(Color.WHITE);
		lblT.setFont(new Font("Verdana", Font.PLAIN, 22));
		lblT.setBounds(74, 161, 81, 49);
		pnlInput.add(lblT);
		
		JLabel lblT_1 = new JLabel("T2  =");
		lblT_1.setHorizontalAlignment(SwingConstants.LEFT);
		lblT_1.setForeground(Color.WHITE);
		lblT_1.setFont(new Font("Verdana", Font.PLAIN, 22));
		lblT_1.setBounds(74, 215, 81, 45);
		pnlInput.add(lblT_1);
		
		JLabel lblT_2 = new JLabel("T3  =");
		lblT_2.setHorizontalAlignment(SwingConstants.LEFT);
		lblT_2.setForeground(Color.WHITE);
		lblT_2.setFont(new Font("Verdana", Font.PLAIN, 22));
		lblT_2.setBounds(74, 264, 81, 49);
		pnlInput.add(lblT_2);
		
		JLabel lblT_3 = new JLabel("T4  =");
		lblT_3.setHorizontalAlignment(SwingConstants.LEFT);
		lblT_3.setForeground(Color.WHITE);
		lblT_3.setFont(new Font("Verdana", Font.PLAIN, 22));
		lblT_3.setBounds(74, 311, 81, 49);
		pnlInput.add(lblT_3);
		
		JLabel lblLcount = new JLabel("LCOUNT  =");
		lblLcount.setHorizontalAlignment(SwingConstants.LEFT);
		lblLcount.setForeground(Color.WHITE);
		lblLcount.setFont(new Font("Verdana", Font.PLAIN, 22));
		lblLcount.setBounds(13, 364, 140, 49);
		pnlInput.add(lblLcount);
		
		JLabel lblStopl = new JLabel("STOPL  =");
		lblStopl.setHorizontalAlignment(SwingConstants.LEFT);
		lblStopl.setForeground(Color.WHITE);
		lblStopl.setFont(new Font("Verdana", Font.PLAIN, 22));
		lblStopl.setBounds(32, 416, 131, 49);
		pnlInput.add(lblStopl);
		
		txtX = new JTextField();
		txtX.setHorizontalAlignment(SwingConstants.RIGHT);
		txtX.setFont(new Font("Verdana", Font.PLAIN, 20));
		txtX.setBounds(162, 7, 104, 49);
		txtX.setBackground(new Color(36,34,29));
		txtX.setForeground(new Color(255,220,135));
		txtX.setCaretColor(Color.WHITE);
		pnlInput.add(txtX);
		txtX.setColumns(10);
		txtX.addKeyListener(keyhand);
		
		txtY = new JTextField();
		txtY.setHorizontalAlignment(SwingConstants.RIGHT);
		txtY.setForeground(new Color(255, 220, 135));
		txtY.setFont(new Font("Verdana", Font.PLAIN, 20));
		txtY.setColumns(10);
		txtY.setBackground(new Color(36, 34, 29));
		txtY.setBounds(162, 57, 104, 48);
		txtY.setCaretColor(Color.WHITE);
		txtY.addKeyListener(keyhand);
		pnlInput.add(txtY);
		
		txtZ = new JTextField();
		txtZ.setHorizontalAlignment(SwingConstants.RIGHT);
		txtZ.setForeground(new Color(255, 220, 135));
		txtZ.setFont(new Font("Verdana", Font.PLAIN, 20));
		txtZ.setColumns(10);
		txtZ.setBackground(new Color(36, 34, 29));
		txtZ.setBounds(162, 106, 104, 49);
		txtZ.setCaretColor(Color.WHITE);
		txtZ.addKeyListener(keyhand);
		pnlInput.add(txtZ);
		
		txtT1H = new JTextField();
		txtT1H.setHorizontalAlignment(SwingConstants.CENTER);
		txtT1H.setForeground(new Color(255, 220, 135));
		txtT1H.setFont(new Font("Verdana", Font.PLAIN, 20));
		txtT1H.setColumns(10);
		txtT1H.setBackground(new Color(36, 34, 29));
		txtT1H.setBounds(162, 168, 48, 42);
		txtT1H.setCaretColor(Color.WHITE);
		txtT1H.addKeyListener(keyhand);
		pnlInput.add(txtT1H);
		
		txtT2H = new JTextField();
		txtT2H.setHorizontalAlignment(SwingConstants.CENTER);
		txtT2H.setForeground(new Color(255, 220, 135));
		txtT2H.setFont(new Font("Verdana", Font.PLAIN, 20));
		txtT2H.setColumns(10);
		txtT2H.setBackground(new Color(36, 34, 29));
		txtT2H.setBounds(162, 217, 48, 42);
		txtT2H.addKeyListener(keyhand);
		txtT2H.setCaretColor(Color.WHITE);
		pnlInput.add(txtT2H);
		
		txtT3H = new JTextField();
		txtT3H.setHorizontalAlignment(SwingConstants.CENTER);
		txtT3H.setForeground(new Color(255, 220, 135));
		txtT3H.setFont(new Font("Verdana", Font.PLAIN, 20));
		txtT3H.setColumns(10);
		txtT3H.setBackground(new Color(36, 34, 29));
		txtT3H.setBounds(162, 268, 48, 42);
		txtT3H.addKeyListener(keyhand);
		txtT3H.setCaretColor(Color.WHITE);
		pnlInput.add(txtT3H);
		
		txtT4H = new JTextField();
		txtT4H.setHorizontalAlignment(SwingConstants.CENTER);
		txtT4H.setForeground(new Color(255, 220, 135));
		txtT4H.setFont(new Font("Verdana", Font.PLAIN, 20));
		txtT4H.setColumns(10);
		txtT4H.setBackground(new Color(36, 34, 29));
		txtT4H.setBounds(162, 318, 48, 42);
		txtT4H.addKeyListener(keyhand);
		txtT4H.setCaretColor(Color.WHITE);
		pnlInput.add(txtT4H);
		
		JLabel label = new JLabel(":");
		label.setFont(new Font("Lucida Grande", Font.PLAIN, 22));
		label.setForeground(Color.WHITE);
		label.setBounds(211, 162, 7, 49);
		pnlInput.add(label);
		
		txtT1M = new JTextField();
		txtT1M.setHorizontalAlignment(SwingConstants.CENTER);
		txtT1M.setForeground(new Color(255, 220, 135));
		txtT1M.setFont(new Font("Verdana", Font.PLAIN, 20));
		txtT1M.setColumns(10);
		txtT1M.setBackground(new Color(36, 34, 29));
		txtT1M.setBounds(218, 168, 48, 42);
		txtT1M.setCaretColor(Color.WHITE);
		txtT1M.addKeyListener(keyhand);
		pnlInput.add(txtT1M);
		
		txtT1S = new JTextField();
		txtT1S.setHorizontalAlignment(SwingConstants.CENTER);
		txtT1S.setForeground(new Color(255, 220, 135));
		txtT1S.setFont(new Font("Verdana", Font.PLAIN, 20));
		txtT1S.setColumns(10);
		txtT1S.setCaretColor(Color.WHITE);
		txtT1S.setBackground(new Color(36, 34, 29));
		txtT1S.setBounds(273, 168, 48, 42);
		txtT1S.addKeyListener(keyhand);
		pnlInput.add(txtT1S);
		
		JLabel label_1 = new JLabel(":");
		label_1.setForeground(Color.WHITE);
		label_1.setFont(new Font("Lucida Grande", Font.PLAIN, 22));
		label_1.setBounds(266, 161, 7, 49);
		pnlInput.add(label_1);
		
		JLabel label_2 = new JLabel(":");
		label_2.setForeground(Color.WHITE);
		label_2.setFont(new Font("Lucida Grande", Font.PLAIN, 22));
		label_2.setBounds(211, 211, 7, 49);
		pnlInput.add(label_2);
		
		txtT2M = new JTextField();
		txtT2M.setHorizontalAlignment(SwingConstants.CENTER);
		txtT2M.setForeground(new Color(255, 220, 135));
		txtT2M.setFont(new Font("Verdana", Font.PLAIN, 20));
		txtT2M.setColumns(10);
		txtT2M.setBackground(new Color(36, 34, 29));
		txtT2M.setCaretColor(Color.WHITE);
		txtT2M.addKeyListener(keyhand);
		txtT2M.setBounds(218, 217, 48, 42);
		pnlInput.add(txtT2M);
		
		JLabel label_3 = new JLabel(":");
		label_3.setForeground(Color.WHITE);
		label_3.setFont(new Font("Lucida Grande", Font.PLAIN, 22));
		label_3.setBounds(266, 210, 7, 49);
		pnlInput.add(label_3);
		
		txtT2S = new JTextField();
		txtT2S.setHorizontalAlignment(SwingConstants.CENTER);
		txtT2S.setForeground(new Color(255, 220, 135));
		txtT2S.setFont(new Font("Verdana", Font.PLAIN, 20));
		txtT2S.setColumns(10);
		txtT2S.setBackground(new Color(36, 34, 29));
		txtT1S.setCaretColor(Color.WHITE);
		txtT1S.addKeyListener(keyhand);
		txtT2S.setBounds(273, 217, 48, 42);
		pnlInput.add(txtT2S);
		
		JLabel label_4 = new JLabel(":");
		label_4.setForeground(Color.WHITE);
		label_4.setFont(new Font("Lucida Grande", Font.PLAIN, 22));
		label_4.setBounds(211, 265, 7, 49);
		pnlInput.add(label_4);
		
		txtT3M = new JTextField();
		txtT3M.setHorizontalAlignment(SwingConstants.CENTER);
		txtT3M.setForeground(new Color(255, 220, 135));
		txtT3M.setFont(new Font("Verdana", Font.PLAIN, 20));
		txtT3M.setColumns(10);
		txtT3M.setBackground(new Color(36, 34, 29));
		txtT3M.setCaretColor(Color.WHITE);
		txtT3M.addKeyListener(keyhand);
		txtT3M.setBounds(218, 271, 48, 42);
		pnlInput.add(txtT3M);
		
		JLabel label_5 = new JLabel(":");
		label_5.setForeground(Color.WHITE);
		label_5.setFont(new Font("Lucida Grande", Font.PLAIN, 22));
		label_5.setBounds(266, 264, 7, 49);
		pnlInput.add(label_5);
		
		txtT3S = new JTextField();
		txtT3S.setHorizontalAlignment(SwingConstants.CENTER);
		txtT3S.setForeground(new Color(255, 220, 135));
		txtT3S.setFont(new Font("Verdana", Font.PLAIN, 20));
		txtT3S.setColumns(10);
		txtT3S.setBackground(new Color(36, 34, 29));
		txtT3S.setCaretColor(Color.WHITE);
		txtT3S.addKeyListener(keyhand);
		txtT3S.setBounds(273, 271, 48, 42);
		pnlInput.add(txtT3S);
		
		JLabel label_6 = new JLabel(":");
		label_6.setForeground(Color.WHITE);
		label_6.setFont(new Font("Lucida Grande", Font.PLAIN, 22));
		label_6.setBounds(211, 312, 7, 49);
		pnlInput.add(label_6);
		
		txtT4M = new JTextField();
		txtT4M.setHorizontalAlignment(SwingConstants.CENTER);
		txtT4M.setForeground(new Color(255, 220, 135));
		txtT4M.setFont(new Font("Verdana", Font.PLAIN, 20));
		txtT4M.setColumns(10);
		txtT4M.setBackground(new Color(36, 34, 29));
		txtT4M.setBounds(218, 318, 48, 42);
		txtT4M.addKeyListener(keyhand);
		txtT4M.setCaretColor(Color.WHITE);
		pnlInput.add(txtT4M);
		
		JLabel label_7 = new JLabel(":");
		label_7.setForeground(Color.WHITE);
		label_7.setFont(new Font("Lucida Grande", Font.PLAIN, 22));
		label_7.setBounds(266, 311, 7, 49);
		pnlInput.add(label_7);
		
		txtT4S = new JTextField();
		txtT4S.setHorizontalAlignment(SwingConstants.CENTER);
		txtT4S.setForeground(new Color(255, 220, 135));
		txtT4S.setFont(new Font("Verdana", Font.PLAIN, 20));
		txtT4S.setColumns(10);
		txtT4S.setBackground(new Color(36, 34, 29));
		txtT4S.setBounds(273, 318, 48, 42);
		txtT4S.setCaretColor(Color.WHITE);
		txtT4S.addKeyListener(keyhand);
		pnlInput.add(txtT4S);
		
		txtLcount = new JTextField();
		txtLcount.setHorizontalAlignment(SwingConstants.RIGHT);
		txtLcount.setForeground(new Color(255, 220, 135));
		txtLcount.setFont(new Font("Verdana", Font.PLAIN, 20));
		txtLcount.setColumns(10);
		txtLcount.setBackground(new Color(36, 34, 29));
		txtLcount.setBounds(162, 364, 104, 49);
		txtLcount.setCaretColor(Color.WHITE);
		txtLcount.addKeyListener(keyhand);
		pnlInput.add(txtLcount);
		
		txtStopL = new JTextField();
		txtStopL.setHorizontalAlignment(SwingConstants.RIGHT);
		txtStopL.setForeground(new Color(255, 220, 135));
		txtStopL.setFont(new Font("Verdana", Font.PLAIN, 20));
		txtStopL.setColumns(10);
		txtStopL.setBackground(new Color(36, 34, 29));
		txtStopL.setCaretColor(Color.WHITE);
		txtStopL.setBounds(162, 417, 104, 49);
		txtStopL.addKeyListener(keyhand);
		pnlInput.add(txtStopL);
		
		JLabel label_8 = new JLabel("%");
		label_8.setHorizontalAlignment(SwingConstants.LEFT);
		label_8.setForeground(Color.WHITE);
		label_8.setFont(new Font("Verdana", Font.PLAIN, 22));
		label_8.setBounds(266, 6, 31, 49);
		pnlInput.add(label_8);
		
		JLabel label_9 = new JLabel("%");
		label_9.setHorizontalAlignment(SwingConstants.LEFT);
		label_9.setForeground(Color.WHITE);
		label_9.setFont(new Font("Verdana", Font.PLAIN, 22));
		label_9.setBounds(266, 56, 31, 49);
		pnlInput.add(label_9);
		
		JLabel label_10 = new JLabel("%");
		label_10.setHorizontalAlignment(SwingConstants.LEFT);
		label_10.setForeground(Color.WHITE);
		label_10.setFont(new Font("Verdana", Font.PLAIN, 22));
		label_10.setBounds(266, 106, 31, 49);
		pnlInput.add(label_10);
		
		JButton btnSave = new JButton("SAVE");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//saveFormula();
			}
		});
		btnSave.setBounds(13, 541, 321, 45);
		pnlInput.add(btnSave);
		
		JPanel tradetoggle = new JPanel();
		tradetoggle.setBackground(new Color(51, 51, 51));
		tradetoggle.setBounds(13, 473, 321, 64);
		tradetoggle.setLayout(null);
        
		JLabel lbltoggle = new JLabel("BUY & SELL");
		lbltoggle.setBackground(Color.white);//new Color(51, 51, 51));
		lbltoggle.setFont(new Font("Verdana", Font.PLAIN, 18));
		//lbltoggle.setBounds(986, 6105, 28);
		tradetoggle.add(lbltoggle);
		
		pnlInput.add(tradetoggle);
		
		JLabel lblBuy = new JLabel("Buy/Sell");
		lblBuy.setHorizontalAlignment(SwingConstants.CENTER);
		lblBuy.setForeground(new Color(255, 220, 135));
		lblBuy.setFont(new Font("Verdana", Font.PLAIN, 22));
		lblBuy.setBounds(90, 0, 131, 28);
		tradetoggle.add(lblBuy);
		
		JSlider slider = new JSlider();
		slider.setSnapToTicks(true);
		slider.setMinorTickSpacing(1);
		slider.setMaximum(1);
		slider.setValue(0);
		slider.setBounds(0, 31, 321, 27);
		tradetoggle.add(slider);
		
		JLabel lblOn = new JLabel("ON");
		lblOn.setHorizontalAlignment(SwingConstants.CENTER);
		lblOn.setForeground(new Color(255, 0, 0));
		lblOn.setFont(new Font("Verdana", Font.PLAIN, 18));
		lblOn.setBounds(273, 9, 60, 28);
		tradetoggle.add(lblOn);
		
		JLabel lblOff = new JLabel("OFF");
		lblOff.setHorizontalAlignment(SwingConstants.CENTER);
		lblOff.setForeground(Color.GREEN);
		lblOff.setFont(new Font("Verdana", Font.PLAIN, 18));
		lblOff.setBounds(-11, 9, 60, 28);
		tradetoggle.add(lblOff);
	
		
		
		
	}
	
	class InputHandler implements KeyListener
	{
	        public void keyTyped(KeyEvent kt)
	        {
	        }
	        
		        public void keyPressed(java.awt.event.KeyEvent evt)
		        {
		        		if (evt.getKeyCode() == KeyEvent.VK_ESCAPE)
		        		{
		        			contentPane.dispose();
		            }
		        }
	        
	        public void keyReleased(KeyEvent kr)
	        {
	        }
	  }
}

	