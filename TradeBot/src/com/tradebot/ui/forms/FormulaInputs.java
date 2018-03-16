
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.pmw.tinylog.Logger;

import com.tradebot.dbcommons.db_commons;
import com.tradebot.dbcommons.tradebot_utility;
import com.tradebot.ui.forms.*;
import javax.swing.JToggleButton;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent; 

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
	private JSlider slitradeSwitch;
	public static String dbName= System.getProperty("user.dir")+File.separator+"ovvi_Market_bot;AUTO_SERVER=TRUE";
    public static String USER="admin", PASS="test123";
    private JTextField txtlotsize;
    
    private String configprop=System.getProperty("user.dir")+File.separator+"resource"+File.separator+"config.properties";
	String tradelogpath;
    tradebot_utility utils = new tradebot_utility(); 
	
	
	private int colid=0, colfeedsecid=1,colplayersecid=2,colFname=3, colX=4, colY=5, colZ=6, colT1=7, colT2=8,colT3=9,colT4=10, colLcount=11,
			colStopL=12, collotsize=13, coltradeswitch=14;
    private String feedsecid,playersecid,Fname;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FormulaInputs window = new FormulaInputs("","","");
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
	public FormulaInputs(String strfeedsecId, String strplayersecid, String strFname) 
	{
		try
		{
			feedsecid = strfeedsecId.trim(); 
			playersecid= strplayersecid.trim(); 
			Fname=strFname.trim();
			dbObj=new db_commons();
			tradelogpath = utils.configlogfile("TRADEBOT_LOG");
			initialize(strplayersecid, strFname);
			loadExistingData(strfeedsecId.trim(), strplayersecid.trim(), strFname.trim());
		}
		catch(Exception ex)
		{
			
		}
		finally
		{
			
		}
	}
	public void loadExistingData(String feedid, String playersecid, String FName)
	{
		String [][] existingdata;
		try
		{
			int count = 0;
			count = dbObj.getRowCount("SELECT * FROM TBL_FORMULA WHERE FORMULANAME='"+FName+"' and TRADESUBJECTID = '"+playersecid+"' and FEEDSUBJECTID = '"+feedid+"'");
			if (count != 0)
			{
					existingdata = dbObj.getMultiColumnRecords("SELECT * FROM TBL_FORMULA WHERE FORMULANAME='"+FName+"' and TRADESUBJECTID = '"+playersecid+"' and FEEDSUBJECTID = '"+feedid+"'");
					Logger.info(existingdata.toString());
					if (existingdata[0][colFname] != null)
					{
						txtX.setText(existingdata[0][colX] == null ? "":existingdata[0][colX]);
						txtY.setText(existingdata[0][colY] == null ? "":existingdata[0][colY]);
						txtZ.setText(existingdata[0][colZ] == null ? "":existingdata[0][colZ]);
						txtLcount.setText(existingdata[0][colLcount] == null ? "":existingdata[0][colLcount]);
						txtStopL.setText(existingdata[0][colStopL] == null ? "":existingdata[0][colStopL]);
						txtlotsize.setText(existingdata[0][collotsize] == null ? "":existingdata[0][collotsize]);
						if (existingdata[0][colT1] != null)
						{
							txtT1H.setText(existingdata[0][colT1].split(":")[0]);
							txtT1M.setText(existingdata[0][colT1].split(":")[1]);
							txtT1S.setText(existingdata[0][colT1].split(":")[2]);
						}
						else
						{
							txtT1H.setText("");
							txtT1M.setText("");
							txtT1S.setText("");
						}
						
						if (existingdata[0][colT2] != null)
						{
							txtT2H.setText(existingdata[0][colT2].split(":")[0]);
							txtT2M.setText(existingdata[0][colT2].split(":")[1]);
							txtT2S.setText(existingdata[0][colT2].split(":")[2]);
						}
						else
						{
							txtT2H.setText("");
							txtT2M.setText("");
							txtT2S.setText("");
						}
						
						if (existingdata[0][colT3] != null)
						{
							txtT3H.setText(existingdata[0][colT3].split(":")[0]);
							txtT3M.setText(existingdata[0][colT3].split(":")[1]);
							txtT3S.setText(existingdata[0][colT3].split(":")[2]);
						}
						else
						{
							txtT3H.setText("");
							txtT3M.setText("");
							txtT3S.setText("");
						}
						
						if (existingdata[0][colT4] != null)
						{
							txtT4H.setText(existingdata[0][colT4].split(":")[0]);
							txtT4M.setText(existingdata[0][colT4].split(":")[1]);
							txtT4S.setText(existingdata[0][colT4].split(":")[2]);
						}
						else
						{
							txtT4H.setText("");
							txtT4M.setText("");
							txtT4S.setText("");
						}
						if (Boolean.parseBoolean(existingdata[0][coltradeswitch]) == true)
						{
							slitradeSwitch.setValue(1);
						}
						else
						{
							slitradeSwitch.setValue(0);
						}
					}
			}
			else
			{
				
					txtX.setText("");
					txtY.setText("");
					txtZ.setText("");
					txtT1H.setText("");
					txtT1M.setText("");
					txtT1S.setText("");
					txtT2H.setText("");
					txtT2M.setText("");
					txtT2S.setText("");
					txtT3H.setText("");
					txtT3M.setText("");
					txtT3S.setText("");
					txtT4S.setText("");
					txtT4H.setText("");
					txtT4M.setText("");
					txtT4S.setText("");
					txtLcount.setText("");
					txtStopL.setText("");
					txtlotsize.setText("");
					slitradeSwitch.setValue(0);
					
				
			}
			
			
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
		contentPane.setBounds(100, 100, 379, 774);
		contentPane.setBackground(new Color(36,34,29));
		contentPane.getContentPane().setLayout(null);
		contentPane.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		contentPane.addKeyListener(keyhand);
		
		

		lblFTitle = new JLabel(strFname);
		lblFTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblFTitle.setFont(new Font("Verdana", Font.BOLD, 22));
		lblFTitle.setForeground(new Color(255, 220, 135));
		lblFTitle.setBounds(6, 6, 377, 43);
		contentPane.getContentPane().add(lblFTitle);
		
		JPanel pnlInput = new JPanel();
		pnlInput.setForeground(Color.WHITE);
		pnlInput.setBounds(16, 46, 347, 690);
		pnlInput.setBackground(new Color(80,75,78));
		contentPane.getContentPane().add(pnlInput);
		pnlInput.setLayout(null);
		
		JLabel lblX = new JLabel("X  =");
		lblX.setBounds(104, 6, 73, 49);
		lblX.setHorizontalAlignment(SwingConstants.LEFT);
		lblX.setForeground(Color.WHITE);
		lblX.setFont(new Font("Verdana", Font.PLAIN, 22));
		pnlInput.add(lblX);
		
		JLabel lblX_1 = new JLabel("Y  =");
		lblX_1.setHorizontalAlignment(SwingConstants.LEFT);
		lblX_1.setForeground(Color.WHITE);
		lblX_1.setFont(new Font("Verdana", Font.PLAIN, 22));
		lblX_1.setBounds(106, 56, 65, 49);
		pnlInput.add(lblX_1);
		
		JLabel lblZ = new JLabel("Z  =");
		lblZ.setHorizontalAlignment(SwingConstants.LEFT);
		lblZ.setForeground(Color.WHITE);
		lblZ.setFont(new Font("Verdana", Font.PLAIN, 22));
		lblZ.setBounds(104, 106, 59, 49);
		pnlInput.add(lblZ);
		
		JLabel lblT = new JLabel("T1  =");
		lblT.setHorizontalAlignment(SwingConstants.LEFT);
		lblT.setForeground(Color.WHITE);
		lblT.setFont(new Font("Verdana", Font.PLAIN, 22));
		lblT.setBounds(90, 167, 81, 49);
		pnlInput.add(lblT);
		
		JLabel lblT_1 = new JLabel("T2  =");
		lblT_1.setHorizontalAlignment(SwingConstants.LEFT);
		lblT_1.setForeground(Color.WHITE);
		lblT_1.setFont(new Font("Verdana", Font.PLAIN, 22));
		lblT_1.setBounds(90, 215, 81, 45);
		pnlInput.add(lblT_1);
		
		JLabel lblT_2 = new JLabel("T3  =");
		lblT_2.setHorizontalAlignment(SwingConstants.LEFT);
		lblT_2.setForeground(Color.WHITE);
		lblT_2.setFont(new Font("Verdana", Font.PLAIN, 22));
		lblT_2.setBounds(90, 264, 81, 49);
		pnlInput.add(lblT_2);
		
		JLabel lblT_3 = new JLabel("T4  =");
		lblT_3.setHorizontalAlignment(SwingConstants.LEFT);
		lblT_3.setForeground(Color.WHITE);
		lblT_3.setFont(new Font("Verdana", Font.PLAIN, 22));
		lblT_3.setBounds(90, 314, 81, 49);
		pnlInput.add(lblT_3);
		
		JLabel lblLcount = new JLabel("Loop  =");
		lblLcount.setHorizontalAlignment(SwingConstants.LEFT);
		lblLcount.setForeground(Color.WHITE);
		lblLcount.setFont(new Font("Verdana", Font.PLAIN, 22));
		lblLcount.setBounds(67, 363, 95, 49);
		pnlInput.add(lblLcount);
		
		JLabel lblStopl = new JLabel("STOPL  =");
		lblStopl.setHorizontalAlignment(SwingConstants.LEFT);
		lblStopl.setForeground(Color.WHITE);
		lblStopl.setFont(new Font("Verdana", Font.PLAIN, 22));
		lblStopl.setBounds(46, 416, 131, 49);
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
				try
				{
					saveFormula();
				}
				catch(Exception ex)
				{
					Logger.error(ex);
				}
				finally 
				{
					
				}
			}
		});
		btnSave.setBounds(182, 634, 152, 45);
		pnlInput.add(btnSave);
		
		JPanel tradetoggle = new JPanel();
		tradetoggle.setBackground(new Color(51, 51, 51));
		tradetoggle.setBounds(13, 527, 321, 86);
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
		lblBuy.setBounds(95, 21, 131, 28);
		tradetoggle.add(lblBuy);
		
		slitradeSwitch = new JSlider();
//		slitradeSwitch.addChangeListener(new ChangeListener() {
//			public void stateChanged(ChangeEvent arg0) {
//				int option = JOptionPane.showConfirmDialog(null, "Are you sure, Want to turn on the Trade ", "Buy/Sell Switch Confirmation", JOptionPane.YES_NO_OPTION);
//				if (option == 0)
//				{
//					
//				}
//				else
//				{
//					
//				}
//			}
//		});
		slitradeSwitch.setSnapToTicks(true);
		slitradeSwitch.setMinorTickSpacing(1);
		slitradeSwitch.setMaximum(1);
		slitradeSwitch.setValue(0);
		slitradeSwitch.setBounds(0, 52, 321, 27);
		tradetoggle.add(slitradeSwitch);
		
		JLabel lblOn = new JLabel("ON");
		lblOn.setHorizontalAlignment(SwingConstants.CENTER);
		lblOn.setForeground(new Color(255, 0, 0));
		lblOn.setFont(new Font("Verdana", Font.PLAIN, 18));
		lblOn.setBounds(261, 22, 60, 28);
		tradetoggle.add(lblOn);
		
		JLabel lblOff = new JLabel("OFF");
		lblOff.setHorizontalAlignment(SwingConstants.CENTER);
		lblOff.setForeground(Color.GREEN);
		lblOff.setFont(new Font("Verdana", Font.PLAIN, 18));
		lblOff.setBounds(0, 22, 60, 28);
		tradetoggle.add(lblOff);
		
		JLabel lblLotSize = new JLabel("SIZE/LOT  =");
		lblLotSize.setHorizontalAlignment(SwingConstants.LEFT);
		lblLotSize.setForeground(Color.WHITE);
		lblLotSize.setFont(new Font("Verdana", Font.PLAIN, 22));
		lblLotSize.setBounds(10, 474, 143, 42);
		pnlInput.add(lblLotSize);
		
		txtlotsize = new JTextField();
		txtlotsize.setText("1");
		txtlotsize.setHorizontalAlignment(SwingConstants.RIGHT);
		txtlotsize.setForeground(new Color(255, 220, 135));
		txtlotsize.setFont(new Font("Verdana", Font.PLAIN, 20));
		txtlotsize.setColumns(10);
		txtlotsize.setCaretColor(Color.WHITE);
		txtlotsize.setBackground(new Color(36, 34, 29));
		txtlotsize.setBounds(162, 476, 104, 41);
		pnlInput.add(txtlotsize);
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try
				{
					dbObj.executeNonQuery("DELETE FROM TBL_FORMULA WHERE FORMULANAME ='"+Fname+"' and TRADESUBJECTID = '"+playersecid+"' and FEEDSUBJECTID = '"+feedsecid+"'");
					int count = dbObj.getRowCount("select * from TBL_FORMULA where FORMULANAME='"+Fname+"'and TRADESUBJECTID = '"+playersecid+"' and FEEDSUBJECTID = '"+feedsecid+"'");
					if (count == 0)
					{
						JOptionPane.showMessageDialog(contentPane,"Formula Deleted Successfully !!", "Message",JOptionPane.INFORMATION_MESSAGE);
						Logger.info("Player "+playersecid+" Formula data Deleted Successfully for feed "+feedsecid);
					}
					else
					{
						JOptionPane.showMessageDialog(contentPane,"Unsuccessfull find the log", "Message",JOptionPane.ERROR_MESSAGE);	
					}
					loadExistingData(feedsecid,playersecid, Fname);
					
				}
				catch(Exception ex)
				{
					Logger.error(ex);
				}
				finally
				{
					
				}
				
			}
		});
		btnDelete.setBounds(13, 634, 152, 45);
		pnlInput.add(btnDelete);
	
		
		
		
	}
	public void saveFormula()
	{ 
		String[] colvalue = new String[2];
		colvalue = validateFormulaData();
		try	{
			if (colvalue[0] == null)
			{
				JOptionPane.showMessageDialog(contentPane,"No Value Entered, Empty Record !!", "Empty Record Alert",JOptionPane.WARNING_MESSAGE);
			}
			else
			{
				int count = dbObj.getRowCount("select * from TBL_FORMULA where FORMULANAME='"+Fname+"'and TRADESUBJECTID = '"+playersecid+"' and FEEDSUBJECTID = '"+feedsecid+"'");
				if (count == 0)
				{
					boolean isSucess = dbObj.executeNonQuery("insert into TBL_FORMULA ("+colvalue[0]+") values ("+colvalue[1]+")");
					if (isSucess ==true)
					{
						JOptionPane.showMessageDialog(contentPane,"Formula Added Successfully !!", "Message",JOptionPane.INFORMATION_MESSAGE);
						//contentPane.dispose();
					}
					else
					{
						JOptionPane.showMessageDialog(contentPane,"Unsuccessfull find the log", "Message",JOptionPane.ERROR_MESSAGE);	
					}
				}
				else 
				{
					dbObj.executeNonQuery("DELETE FROM TBL_FORMULA WHERE FORMULANAME ='"+Fname+"' and TRADESUBJECTID = '"+playersecid+"' and FEEDSUBJECTID = '"+feedsecid+"'");
					boolean isSucess = dbObj.executeNonQuery("insert into TBL_FORMULA ("+colvalue[0]+") values ("+colvalue[1]+")");
					if (isSucess ==true)
					{
						JOptionPane.showMessageDialog(contentPane,"Formula Updated Successfully !!", "Message",JOptionPane.INFORMATION_MESSAGE);
						contentPane.dispose();
					}
					else
					{
						JOptionPane.showMessageDialog(contentPane,"Unsuccessfull find the log", "Message",JOptionPane.ERROR_MESSAGE);	
					}
				}
			}			
		}
		catch(Exception ex)
		{
			Logger.error(ex);
		}
		finally
		{
			
		}
	}
	public String [] validateFormulaData()
	{
		String [] colval =new String[2];
		try {
			String colbuilder="", valueBuilder="";
			colbuilder=colbuilder+"FEEDSUBJECTID,";
			valueBuilder = valueBuilder + "'"+feedsecid.trim()+"'"+",";
			
			colbuilder=colbuilder+"TRADESUBJECTID,";
			valueBuilder = valueBuilder + "'"+playersecid.trim()+"'"+",";
			
			
			if (!txtX.getText().equals(""))
			{
				colbuilder=colbuilder+"X,";
				valueBuilder = valueBuilder + txtX.getText().trim()+",";
			}
			if (!txtY.getText().equals(""))
			{
				colbuilder=colbuilder+"Y,";
				valueBuilder = valueBuilder + txtY.getText().trim()+",";
			}
			if (!txtZ.getText().equals(""))
			{
				colbuilder=colbuilder+"Z,";
				valueBuilder = valueBuilder + txtZ.getText().trim()+",";
			}
			if ((!txtT1H.getText().equals("")) && (!txtT1M.getText().equals("")) && (!txtT1S.getText().equals("")))
			{
				colbuilder=colbuilder+"T1,";
				valueBuilder = valueBuilder +"'"+txtT1H.getText().trim()+":"+txtT1M.getText().trim()+":"+txtT1S.getText().trim()+"',";
			}
			if ((!txtT2H.getText().equals("")) && (!txtT2M.getText().equals("")) && (!txtT2S.getText().equals("")))
			{
				colbuilder=colbuilder+"T2,";
				valueBuilder = valueBuilder +"'"+txtT2H.getText().trim()+":"+txtT2M.getText().trim()+":"+txtT2S.getText().trim()+"',";
			}
			if ((!txtT3H.getText().equals("")) && (!txtT3M.getText().equals("")) && (!txtT3S.getText().equals("")))
			{
				colbuilder=colbuilder+"T3,";
				valueBuilder = valueBuilder +"'"+txtT3H.getText().trim()+":"+txtT3M.getText().trim()+":"+txtT3S.getText().trim()+"',";
			}
			if ((!txtT4H.getText().equals("")) && (!txtT4M.getText().equals("")) && (!txtT4S.getText().equals("")))
			{
				colbuilder=colbuilder+"T4,";
				valueBuilder = valueBuilder +"'"+txtT4H.getText().trim()+":"+txtT4M.getText().trim()+":"+txtT4S.getText().trim()+"',";
			}
			if (!txtLcount.getText().equals(""))
			{
				colbuilder=colbuilder+"LCOUNT,";
				valueBuilder = valueBuilder + txtLcount.getText().trim()+",";
			}
			if (!txtStopL.getText().equals(""))
			{
				colbuilder=colbuilder+"STOPL,";
				valueBuilder = valueBuilder + txtStopL.getText().trim()+",";
			}
			if(!txtlotsize.getText().equals(""))
			{
				colbuilder=colbuilder+"LOTSIZE,";
				valueBuilder = valueBuilder + txtlotsize.getText().trim()+",";
			}
			if(slitradeSwitch.getValue() == 0)
			{
				colbuilder=colbuilder+"TRADESWITCH,";
				valueBuilder = valueBuilder + "'false'"+",";
			}
			if(slitradeSwitch.getValue() == 1)
			{
				colbuilder=colbuilder+"TRADESWITCH,";
				valueBuilder = valueBuilder + "'true'"+",";
			}
			if (!colbuilder.equals(""))
			{
				colbuilder = colbuilder + "FORMULANAME";
				valueBuilder= valueBuilder + "'"+lblFTitle.getText().trim()+"'";
				colval[0]=colbuilder;
				colval[1]=valueBuilder;
			}
			
			
		}
		catch(Exception ex)
		{
			Logger.error(ex);
		}
		finally {
			
		}
		return colval;
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

	