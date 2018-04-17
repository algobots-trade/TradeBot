package com.tradebot.ui.forms;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.io.File;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import org.pmw.tinylog.Logger;

import com.tradebot.dbcommons.DateValidator;
import com.tradebot.dbcommons.db_commons;
import com.tradebot.dbcommons.tradebot_utility;
import javax.swing.border.LineBorder;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class Player {

	private JFrame playerframe;
	private JTextField txtscrib;
	private JTextField txtExpdd;
	private JTextField txtExpmm;
	private JTextField txtExpyyyy;
	private JTextField txtprice;
	private JTextField txtsecId;
	private JComboBox<?> cmbmarkettype;
	private JComboBox<?> cmbright;
	private JComboBox cmbheadfeed;
	private JPanel futopt_panel;
	private JLabel lblRight, lblPrice;
	
	
	
	private String configprop=System.getProperty("user.dir")+File.separator+"resource"+File.separator+"config.properties";
	String tradelogpath;
	tradebot_utility utils = new tradebot_utility(); 
	db_commons dbobj=new db_commons();
	
	private int colid=0,colfeedsecid=1,colplayersecid =2, colscrib=3,colmtype=4,colexpdate=5,colprice=6,colrights=7,collotsize=8,colinfo=9;
	private JLabel lblSegment;
	private JComboBox<Object> comboBox;
	private JLabel lblInsttype;
	private JComboBox<Object> comboBox_1;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Player window = new Player("123123");
					window.playerframe.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Player(String playerId) {
		tradelogpath = utils.configlogfile("TRADEBOT_LOG");
		initialize();
		if (playerId !=null)
		{
			initialLoad(playerId);
		}
	}
	public void initialLoad(String playersecid)
	{
		
		try
		{
			loadExistingData(playersecid);
		}
		catch(Exception ex)
		{
			Logger.error(ex.getMessage());
		}
		finally
		{
			
		}
	}
	public void loadHeadCombo()
	{
		String [][] headdata;
		try
		{
			headdata = dbobj.getMultiColumnRecords("SELECT * FROM TBL_HEADFEEDS WHERE FEEDSUBJECTID IS NOT NULL ORDER BY ID");
			cmbheadfeed = new JComboBox();
			cmbheadfeed.addItem("--Select--");
			for(int i =0; i < headdata.length ; i++)
			{
				cmbheadfeed.addItem(headdata[i][1]+ " - " + headdata[i][2]+ " - " + headdata[i][3]+ " - " + headdata[i][4]);				
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
	
	public void loadExistingData(String psecid)
	{
		String [][] existingdata;
		try
		{	
			int count = 0;
			count = dbobj.getRowCount("SELECT * FROM TBL_PLAYERS WHERE TRADESUBJECTID = '"+psecid+"'");
			if (count != 0)
			{
				existingdata = dbobj.getMultiColumnRecords("SELECT * FROM TBL_PLAYERS WHERE TRADESUBJECTID = '"+psecid+"'");
				Logger.info(existingdata.toString());
				if (existingdata[0][colplayersecid] != null)
				{
			
					if ((existingdata[0][colscrib] != null )&&(existingdata[0][colmtype] !=null))
					{
						txtscrib.setText(existingdata[0][colscrib]);
						cmbmarkettype.setSelectedItem(existingdata[0][colmtype].toString().trim());
						txtsecId.setText(existingdata[0][colplayersecid]);
						for (int i=0; i< cmbheadfeed.getItemCount(); i++)
						{
							String item = cmbheadfeed.getItemAt(i).toString();
				            if (item.contains(existingdata[0][colfeedsecid]))
				            {
				                cmbheadfeed.setSelectedIndex(i);
				                break;
				            }
						}
					}
					switch (existingdata[0][colmtype].toString()) 
					{
						case "STOCK":
							builtSTKControls();
							break;
						case "FUTURE":
							builtFUTControls();
							if  (existingdata[0][colexpdate] != null)
							{
								String [] expdate =new String[3];
								expdate = existingdata[0][colexpdate].split("-");
								txtExpdd.setText(expdate[0]);
								txtExpmm.setText(expdate[1]);
								txtExpyyyy.setText(expdate[2]);
							}
							break;
						case "OPTIONS":
							builtOPTControls();
							if  ((existingdata[0][colexpdate] != null) && (existingdata[0][colprice] != null) && (existingdata[0][colrights] != null))
							{
								String [] expdate =new String[3];
								expdate = existingdata[0][colexpdate].split("-");
								txtExpdd.setText(expdate[0]);
								txtExpmm.setText(expdate[1]);
								txtExpyyyy.setText(expdate[2]);
								txtprice.setText(existingdata[0][colprice]);
								cmbright.setSelectedItem(existingdata[0][colrights]);
							}
							break;
						case "INDEX":
							builtINDControls();
							break;					
					}
				}
			}
			else
			{
				resetfields();
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

	private void builtINDControls() {
		// TODO Auto-generated method stub
		try
		{
			futopt_panel.setVisible(false);
		}
		catch(Exception ex)
		{
			Logger.error(ex.toString());
		}
		finally
		{
			
		}
		
	}

	private void builtOPTControls() {
		// TODO Auto-generated method stub
		try
		{
			futopt_panel.setVisible(true);
			lblPrice.setVisible(true);
			txtprice.setVisible(true);
			lblRight.setVisible(true);
			cmbright.setVisible(true);
		}
		catch(Exception ex)
		{
			Logger.error(ex.toString());
		}
		finally
		{
			
		}
		
	}

	private void builtFUTControls() {
		// TODO Auto-generated method stub
		try
		{
			futopt_panel.setVisible(true);
			lblPrice.setVisible(false);
			txtprice.setVisible(false);
			lblRight.setVisible(false);
			cmbright.setVisible(false);
			
		}
		catch(Exception ex)
		{
			Logger.error(ex.toString());
		}
		finally
		{
			
		}
		
	}

	private void builtSTKControls() {
		// TODO Auto-generated method stub
		try
		{
			futopt_panel.setVisible(false);
		}
		catch(Exception ex)
		{
			Logger.error(ex.toString());
		}
		finally
		{
			
		}
	}
	
	public void saveformula()
	{
		try
		{
			
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
	private void initialize() {
		playerframe = new JFrame();
		playerframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		playerframe = new JFrame();
		playerframe.setTitle("Player - Details");
		playerframe.setBounds(100, 100, 655, 489);
		playerframe.getContentPane().setLayout(null);
		playerframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		playerframe.getContentPane().setBackground(new Color(51, 51, 51));
		playerframe.setVisible(true);
		
		JPanel innerpanel = new JPanel();
		innerpanel.setBounds(6, 61, 626, 378);
		innerpanel.setBackground(new Color(80,75,78));
		playerframe.getContentPane().add(innerpanel);
		innerpanel.setLayout(null);
		
		txtscrib = new JTextField();
		txtscrib.setBounds(433, 17, 183, 32);
		innerpanel.add(txtscrib);
		txtscrib.setHorizontalAlignment(SwingConstants.LEFT);
		txtscrib.setForeground(new Color(255, 220, 135));
		txtscrib.setFont(new Font("Verdana", Font.PLAIN, 20));
		txtscrib.setColumns(10);
		txtscrib.setCaretColor(Color.WHITE);
		txtscrib.setBackground(new Color(36, 34, 29));
		
		JLabel lblScrib = new JLabel("SCRIB");
		lblScrib.setBounds(339, 9, 74, 49);
		innerpanel.add(lblScrib);
		lblScrib.setHorizontalAlignment(SwingConstants.LEFT);
		lblScrib.setForeground(Color.WHITE);
		lblScrib.setFont(new Font("Verdana", Font.PLAIN, 18));
		
		cmbmarkettype = new JComboBox<Object>(new String[] {"--Select--", "STOCK", "FUTURE", "OPTIONS", "INDEX"});
		cmbmarkettype.setModel(new DefaultComboBoxModel(new String[] {"--Select--", "NSEFO", "NSECM", "NSECD", "MCX"}));
		cmbmarkettype.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Logger.info("Market type set to --> "+cmbmarkettype.getSelectedItem().toString());
				switch (cmbmarkettype.getSelectedItem().toString()) {
				case "STOCK":
					builtSTKControls();
					break;
				case "FUTURE":
					builtFUTControls();
					break;
				case "OPTIONS":
					builtOPTControls();
					break;
				case "INDEX":
					builtINDControls();
					break;
				case "——":
					builtSTKControls();
					break;
				default:
					builtSTKControls();
					break;
				}
			}
		});
		cmbmarkettype.setFont(new Font("Verdana", Font.PLAIN, 18));
		cmbmarkettype.setBounds(129, 18, 183, 31);
		
		
		innerpanel.add(cmbmarkettype);
		
		futopt_panel = new JPanel();
		futopt_panel.setBackground(Color.DARK_GRAY);
		futopt_panel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		futopt_panel.setBounds(10, 163, 606, 89);
		innerpanel.add(futopt_panel);
		futopt_panel.setLayout(null);
		
		JLabel lblDate = new JLabel("DATE");
		lblDate.setBounds(78, 6, 64, 26);
		lblDate.setHorizontalAlignment(SwingConstants.LEFT);
		lblDate.setForeground(Color.WHITE);
		lblDate.setFont(new Font("Verdana", Font.PLAIN, 16));
		futopt_panel.add(lblDate);
		
		txtExpdd = new JTextField();
		txtExpdd.setText("DD");
		txtExpdd.setPreferredSize(new Dimension(60, 50));
		txtExpdd.setHorizontalAlignment(SwingConstants.CENTER);
		txtExpdd.setForeground(Color.WHITE);
		txtExpdd.setColumns(4);
		txtExpdd.setCaretColor(Color.WHITE);
		txtExpdd.setBackground(new Color(80, 75, 78));
		txtExpdd.setBounds(17, 32, 49, 34);
		txtExpdd.addFocusListener(new FocusListener() {
		    public void focusGained(FocusEvent e) {
		    	txtExpdd.setText("");
		    }

		    public void focusLost(FocusEvent e) {
		    	    if(txtExpdd.getText().equals(""))
		    	    {
		    	    	txtExpdd.setText("DD");
		    	    }
		        // nothing
		    }
		});
		futopt_panel.add(txtExpdd);
		
		txtExpmm = new JTextField();
		txtExpmm.setText("MM");
		txtExpmm.setPreferredSize(new Dimension(60, 50));
		txtExpmm.setHorizontalAlignment(SwingConstants.CENTER);
		txtExpmm.setForeground(Color.WHITE);
		txtExpmm.setColumns(4);
		txtExpmm.setCaretColor(Color.WHITE);
		txtExpmm.setBackground(new Color(80, 75, 78));
		txtExpmm.setBounds(69, 32, 49, 34);
		txtExpmm.addFocusListener(new FocusListener() {
		    public void focusGained(FocusEvent e) {
		    	txtExpmm.setText("");
		    }

		    public void focusLost(FocusEvent e) {
		    	    if(txtExpmm.getText().equals(""))
		    	    {
		    	    	txtExpmm.setText("MM");
		    	    }
		    }
		});
		futopt_panel.add(txtExpmm);
		
		txtExpyyyy = new JTextField();
		txtExpyyyy.setText("YYYY");
		txtExpyyyy.setPreferredSize(new Dimension(80, 50));
		txtExpyyyy.setHorizontalAlignment(SwingConstants.CENTER);
		txtExpyyyy.setForeground(Color.WHITE);
		txtExpyyyy.setColumns(6);
		txtExpyyyy.setCaretColor(Color.WHITE);
		txtExpyyyy.setBackground(new Color(80, 75, 78));
		txtExpyyyy.setBounds(121, 32, 74, 34);
		txtExpyyyy.addFocusListener(new FocusListener() {
		    public void focusGained(FocusEvent e) {
		    	txtExpyyyy.setText("");
		    }

		    public void focusLost(FocusEvent e) {
		    	    if(txtExpyyyy.getText().equals(""))
		    	    {
		    	    	txtExpyyyy.setText("YYYY");
		    	    }
		        // nothing
		    }
		});
		futopt_panel.add(txtExpyyyy);
		
		lblPrice = new JLabel("PRICE");
		lblPrice.setHorizontalAlignment(SwingConstants.LEFT);
		lblPrice.setForeground(Color.WHITE);
		lblPrice.setFont(new Font("Verdana", Font.PLAIN, 16));
		lblPrice.setBounds(286, 6, 64, 26);
		futopt_panel.add(lblPrice);
		
		txtprice = new JTextField();
		txtprice.setText("0.0");
		txtprice.setPreferredSize(new Dimension(80, 50));
		txtprice.setHorizontalAlignment(SwingConstants.CENTER);
		txtprice.setForeground(Color.WHITE);
		txtprice.setColumns(6);
		txtprice.setCaretColor(Color.WHITE);
		txtprice.setBackground(new Color(80, 75, 78));
		txtprice.setBounds(230, 32, 168, 34);
		futopt_panel.add(txtprice);
		
		lblRight = new JLabel("RIGHT");
		lblRight.setHorizontalAlignment(SwingConstants.LEFT);
		lblRight.setForeground(Color.WHITE);
		lblRight.setFont(new Font("Verdana", Font.PLAIN, 16));
		lblRight.setBounds(465, 6, 80, 26);
		futopt_panel.add(lblRight);
		
		cmbright = new JComboBox<Object>((new String[] {"--Select--", "PUT", "CALL"}));
		cmbright.setFont(new Font("Verdana", Font.PLAIN, 18));
		cmbright.setBounds(442, 33, 154, 34);
		futopt_panel.add(cmbright);
		
		JButton btnDelete = new JButton("DELETE");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				int count = 0;
				count = dbobj.getRowCount("SELECT * FROM TBL_PLAYERS WHERE TRADESUBJECTID = '"+txtsecId.getText()+"'");
				if (count != 0)
				{
					dbobj.executeNonQuery("DELETE FROM TBL_PLAYERS WHERE TRADESUBJECTID='"+txtsecId.getText()+"'");
					dbobj.executeNonQuery("DELETE FROM TBL_TRADEBOARD WHERE TRADESECID='"+txtsecId.getText()+"'");
					count = dbobj.getRowCount("SELECT * FROM TBL_PLAYERS WHERE TRADESUBJECTID = '"+txtsecId.getText()+"'");
					if (count !=0)
					{
						JOptionPane.showMessageDialog(playerframe,"Record not Deleted !! Please check Data !!", "Error",JOptionPane.ERROR_MESSAGE);	
					}
					else
					{
						JOptionPane.showMessageDialog(playerframe,"Player Deleted Sucessfully !!", "Sucess",JOptionPane.INFORMATION_MESSAGE);	
						resetfields();
					}
					
				}
				else
				{	
					JOptionPane.showMessageDialog(playerframe,"No Records to Delete", "Error",JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnDelete.setPreferredSize(new Dimension(180, 50));
		btnDelete.setBounds(76, 322, 166, 37);
		innerpanel.add(btnDelete);
		
		JButton btnSave = new JButton("SAVE");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ((txtscrib.getText().equals(""))||(cmbmarkettype.getSelectedIndex() == 0)||(txtscrib.getText().equals("")))
				{
					JOptionPane.showMessageDialog(playerframe,"Check scrib name field, sec-Id and market type.", "Invalid Inputs",JOptionPane.WARNING_MESSAGE);
				}
				else
				{
					saveTradeData(cmbmarkettype.getSelectedItem().toString(),txtsecId.getText().toString());
				}
				//if (STKvalidations())
				//{
				 //saveTradeData(cmbmarkettype.getSelectedItem().toString(),txtsecId.getText().toString());
				 
				//}
			}
		});
		btnSave.setPreferredSize(new Dimension(180, 50));
		btnSave.setBounds(373, 322, 166, 37);
		innerpanel.add(btnSave);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.DARK_GRAY);
		panel_1.setBorder(new LineBorder(new Color(192, 192, 192), 2, true));
		panel_1.setBounds(10, 262, 606, 49);
		innerpanel.add(panel_1);
		panel_1.setLayout(null);
		
		loadHeadCombo();
		cmbheadfeed.setBounds(185, 8, 362, 32);
		panel_1.add(cmbheadfeed);
		cmbheadfeed.setFont(new Font("Verdana", Font.PLAIN, 18));
		
		JLabel lblHeadFeed = new JLabel("HEAD FEED");
		lblHeadFeed.setHorizontalAlignment(SwingConstants.LEFT);
		lblHeadFeed.setForeground(Color.WHITE);
		lblHeadFeed.setFont(new Font("Verdana", Font.PLAIN, 18));
		lblHeadFeed.setBounds(48, 10, 127, 28);
		panel_1.add(lblHeadFeed);
		
		JLabel label = new JLabel("SEC-ID");
		label.setHorizontalAlignment(SwingConstants.LEFT);
		label.setForeground(Color.WHITE);
		label.setFont(new Font("Verdana", Font.PLAIN, 18));
		label.setBounds(339, 60, 82, 49);
		innerpanel.add(label);
		
		txtsecId = new JTextField();
		txtsecId.setHorizontalAlignment(SwingConstants.LEFT);
		txtsecId.setForeground(new Color(255, 220, 135));
		txtsecId.setFont(new Font("Verdana", Font.PLAIN, 20));
		txtsecId.setColumns(10);
		txtsecId.setCaretColor(Color.WHITE);
		txtsecId.setBackground(new Color(36, 34, 29));
		txtsecId.setBounds(433, 68, 183, 31);
		innerpanel.add(txtsecId);
		
		JLabel lblMarket = new JLabel("EXCHANGE");
		lblMarket.setHorizontalAlignment(SwingConstants.LEFT);
		lblMarket.setForeground(Color.WHITE);
		lblMarket.setFont(new Font("Verdana", Font.PLAIN, 18));
		lblMarket.setBounds(10, 9, 100, 49);
		innerpanel.add(lblMarket);
		
		JButton button = new JButton("Check");
		button.setBounds(338, 120, 278, 32);
		innerpanel.add(button);
		
		lblSegment = new JLabel("SEGMENT");
		lblSegment.setHorizontalAlignment(SwingConstants.LEFT);
		lblSegment.setForeground(Color.WHITE);
		lblSegment.setFont(new Font("Verdana", Font.PLAIN, 18));
		lblSegment.setBounds(10, 60, 100, 49);
		innerpanel.add(lblSegment);
		
		comboBox = new JComboBox<Object>(new Object[]{});
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"--Select--", "FUT", "OPT", "CM"}));
		comboBox.setFont(new Font("Verdana", Font.PLAIN, 18));
		comboBox.setBounds(129, 69, 183, 31);
		innerpanel.add(comboBox);
		
		lblInsttype = new JLabel("INST-TYPE");
		lblInsttype.setHorizontalAlignment(SwingConstants.LEFT);
		lblInsttype.setForeground(Color.WHITE);
		lblInsttype.setFont(new Font("Verdana", Font.PLAIN, 18));
		lblInsttype.setBounds(10, 111, 100, 49);
		innerpanel.add(lblInsttype);
		
		comboBox_1 = new JComboBox<Object>(new Object[]{});
		comboBox_1.setModel(new DefaultComboBoxModel(new String[] {"--Select--", "FUTIDX", "FUTSTK", "OPTIDX", "OPTSTK", "Equities", "FUTCOM"}));
		comboBox_1.setFont(new Font("Verdana", Font.PLAIN, 18));
		comboBox_1.setBounds(129, 120, 183, 31);
		innerpanel.add(comboBox_1);
		
		JLabel lblHead = new JLabel("Player Details");
		lblHead.setHorizontalAlignment(SwingConstants.CENTER);
		lblHead.setForeground(new Color(255, 220, 135));
		lblHead.setFont(new Font("Verdana", Font.BOLD, 22));
		lblHead.setBounds(6, 13, 653, 43);
		playerframe.getContentPane().add(lblHead);
		playerframe.setVisible(true);
	}
	
	public Boolean saveTradeData(String markettype, String playerid)
	{
		Boolean Isvalid=false;
		try
		{
			int count = 0;
			count = dbobj.getRowCount("SELECT * FROM TBL_PLAYERS WHERE TRADESUBJECTID = '"+playerid+"'");
			if (count != 0)
			{
				dbobj.executeNonQuery("DELETE FROM TBL_PLAYERS WHERE TRADESUBJECTID='"+txtsecId.getText()+"'");
				dbobj.executeNonQuery("DELETE FROM TBL_TRADEBOARD WHERE TRADESECID='"+txtsecId.getText()+"'");
				count = dbobj.getRowCount("SELECT * FROM TBL_PLAYERS WHERE TRADESUBJECTID = '"+playerid+"'");
				if (count !=0)
				{
					JOptionPane.showMessageDialog(playerframe,"Record not saved !! Please check Data !!", "Error",JOptionPane.ERROR_MESSAGE);	
				}
				
			}
			//String hname = cmbheadfeed.getSelectedItem().toString().split("-")[0].trim();
			String feedsecId =cmbheadfeed.getSelectedItem().toString().split("-")[0].trim();
			switch (markettype) {
			case "STOCK":
				if (STKvalidations())
				{
					dbobj.executeNonQuery("INSERT INTO TBL_PLAYERS (FEEDSUBJECTID,TRADESUBJECTID,SCRIB,MARKETTYPE) VALUES ('"+feedsecId+"','"+txtsecId.getText().trim()+"','"+txtscrib.getText().trim()+"','"+cmbmarkettype.getSelectedItem().toString().trim()+"')");
					dbobj.executeNonQuery("INSERT INTO TBL_TRADEBOARD (TSCRIB, FEEDSECID, TRADESECID) VALUES ('"+txtscrib.getText()+"', '"+cmbheadfeed.getSelectedItem().toString().split("-")[0].trim()+"', '"+txtsecId.getText().trim()+"');");
					JOptionPane.showMessageDialog(playerframe,"Record Saved !!", "Success",JOptionPane.WARNING_MESSAGE);				
				}
				break; 
			case "FUTURE":
				if(FUTvalidations())
				{
					String date = txtExpdd.getText()+"-"+txtExpmm.getText()+"-"+txtExpyyyy.getText();
					dbobj.executeNonQuery("INSERT INTO TBL_PLAYERS (FEEDSUBJECTID,TRADESUBJECTID,SCRIB,MARKETTYPE,EXPDATE) VALUES ('"+feedsecId+"','"+txtsecId.getText().trim()+"','"+txtscrib.getText().trim()+"','"+cmbmarkettype.getSelectedItem().toString().trim()+"','"+date+"')");
					dbobj.executeNonQuery("INSERT INTO TBL_TRADEBOARD (TSCRIB, FEEDSECID, TRADESECID) VALUES ('"+txtscrib.getText()+"', '"+cmbheadfeed.getSelectedItem().toString().split("-")[0].trim()+"', '"+txtsecId.getText().trim()+"');");
					
					JOptionPane.showMessageDialog(playerframe,"Record Saved !!", "Success",JOptionPane.WARNING_MESSAGE);	
				}
				break;
			case "OPTIONS":
				if(OPTvalidations())
				{
					String date = txtExpdd.getText()+"-"+txtExpmm.getText()+"-"+txtExpyyyy.getText();
					dbobj.executeNonQuery("INSERT INTO TBL_PLAYERS (FEEDSUBJECTID,TRADESUBJECTID,SCRIB,MARKETTYPE,EXPDATE,PRICE,RIGHTS) VALUES ('"+feedsecId.trim()+"','"+txtsecId.getText().trim()+"','"+txtscrib.getText().trim()+"','"+cmbmarkettype.getSelectedItem().toString().trim()+"','"+date+"',"+txtprice.getText().trim()+",'"+cmbright.getSelectedItem().toString().trim()+"')");
					dbobj.executeNonQuery("INSERT INTO TBL_TRADEBOARD (TSCRIB, FEEDSECID, TRADESECID) VALUES ('"+txtscrib.getText().trim()+"', '"+cmbheadfeed.getSelectedItem().toString().split("-")[0].trim()+"', '"+txtsecId.getText().trim()+"');");
					
					JOptionPane.showMessageDialog(playerframe,"Record Saved !!", "Success",JOptionPane.WARNING_MESSAGE);	
				}
				break;
			case "INDEX":
				if(INDvalidations())
				{
					dbobj.executeNonQuery("INSERT INTO TBL_PLAYERS (FEEDSUBJECTID,TRADESUBJECTID,SCRIB,MARKETTYPE) VALUES ('"+feedsecId+"','"+txtsecId.getText().trim()+"','"+txtscrib.getText().trim()+"','"+cmbmarkettype.getSelectedItem().toString().trim()+"')");
					dbobj.executeNonQuery("INSERT INTO TBL_TRADEBOARD (TSCRIB, FEEDSECID, TRADESECID) VALUES ('"+txtscrib.getText().trim()+"', '"+cmbheadfeed.getSelectedItem().toString().split("-")[0].trim()+"', '"+txtsecId.getText().trim()+"');");
					
					JOptionPane.showMessageDialog(playerframe,"Record Saved !!", "Success",JOptionPane.WARNING_MESSAGE);	
				}
				break;
			}
			resetfields();
		}
		catch(Exception ex)
		{
			Logger.error(ex.toString());
		}
		finally
		{
		}
		return Isvalid;
	}
	
	public Boolean STKvalidations()
	{
		Boolean Isvalid=false;
		try
		{
			if ((txtscrib.getText().equals(""))||(cmbmarkettype.getSelectedIndex() == 0)||(txtsecId.getText().equals("")))
			{
				JOptionPane.showMessageDialog(playerframe,"Invalid Inputs.", "Invalid Inputs",JOptionPane.WARNING_MESSAGE);
				Logger.warn("Check scrib name field and market type.");
			}
			else if(cmbheadfeed.getSelectedIndex() == 0)
			{
				JOptionPane.showMessageDialog(playerframe,"Select Head feed", "Invalid Inputs",JOptionPane.WARNING_MESSAGE);
			}
			else
			{
				// need to implement method to go and validate instrument type and get security id
				Isvalid = true;
				
			}
		}
		catch(Exception ex)
		{
			Logger.error(ex.toString());
		}
		finally
		{
		}
		return Isvalid;
	}
	public Boolean FUTvalidations()
	{
		Boolean Isvalid=false;
		String exdate = txtExpdd.getText()+"-"+txtExpmm.getText()+"-"+txtExpyyyy.getText();
		DateValidator dv=new DateValidator();
		try
		{
			if ((txtscrib.getText().equals(""))||(cmbmarkettype.getSelectedItem().equals("——")||(txtsecId.getText().equals(""))))
			{
				JOptionPane.showMessageDialog(playerframe,"Invalid Inputs.", "Invalid Inputs",JOptionPane.WARNING_MESSAGE);
			}
			else if ((txtExpdd.getText().equals("DD"))||(txtExpmm.getText().equals("MM"))||(txtExpyyyy.getText().equals("YYYY")))
			{
				
				JOptionPane.showMessageDialog(playerframe,"Please Enter valid date (DD-MM-YYYY)", "Invalid Inputs",JOptionPane.WARNING_MESSAGE);
				
			}
			else if (!(dv.isThisDateValid(exdate, "dd-MM-yyyy"))||!(dv.isThisDateWeekend(exdate,  "dd-MM-yyyy")))
			{
				JOptionPane.showMessageDialog(playerframe,"Invalid Feed Date or It might falling on weekends !!","Alert",JOptionPane.ERROR_MESSAGE);
				  
			}
			else if(cmbheadfeed.getSelectedIndex() == 0)
			{
				JOptionPane.showMessageDialog(playerframe,"Select Head feed", "Invalid Inputs",JOptionPane.WARNING_MESSAGE);
			}
			else
			{
				// need to implement method to go and validate instrument type and get security id
				Isvalid = true;
				
			}
		}
		catch(Exception ex)
		{
			Logger.error(ex.toString());
		}
		finally
		{
		}
		return Isvalid;
	}
	public Boolean OPTvalidations()
	{
		Boolean Isvalid=false;
		String exdate = txtExpdd.getText()+"-"+txtExpmm.getText()+"-"+txtExpyyyy.getText();
		DateValidator dv=new DateValidator();
		try
		{
			if ((txtscrib.getText().equals(""))||(cmbmarkettype.getSelectedItem().equals("——")||(txtsecId.getText().equals(""))))
			{
				JOptionPane.showMessageDialog(playerframe,"Invalid Inputs.", "Invalid Inputs",JOptionPane.WARNING_MESSAGE);
			}
			else if ((txtExpdd.getText().equals("DD"))||(txtExpmm.getText().equals("MM"))||(txtExpyyyy.getText().equals("YYYY")))
			{
				JOptionPane.showMessageDialog(playerframe,"Please Enter valid date (DD-MM-YYYY)", "Invalid Inputs",JOptionPane.WARNING_MESSAGE);
			}
			else if (!(dv.isThisDateValid(exdate, "dd-MM-yyyy"))||!(dv.isThisDateWeekend(exdate,  "dd-MM-yyyy")))
			{
				JOptionPane.showMessageDialog(playerframe,"Invalid Feed Date or It might falling on weekends !!","Alert",JOptionPane.ERROR_MESSAGE);
				  
			}
			else if ((txtprice.getText().equals(""))||(cmbright.getSelectedIndex()==0))
			{
				JOptionPane.showMessageDialog(playerframe,"Price and Rights is Mondatory", "Invalid Inputs",JOptionPane.WARNING_MESSAGE);
				
			}
			else if (utils.isNumeric(txtprice.getText()) == false)
			{
				JOptionPane.showMessageDialog(playerframe,"price is invalid", "Invalid Inputs",JOptionPane.WARNING_MESSAGE);
			}
			else if(cmbheadfeed.getSelectedIndex() == 0)
			{
				JOptionPane.showMessageDialog(playerframe,"Select Head feed", "Invalid Inputs",JOptionPane.WARNING_MESSAGE);
			}
			else
			{
				// need to implement method to go and validate instrument type and get security id
				Isvalid = true;
				
			}
		}
		catch(Exception ex)
		{
			Logger.error(ex.toString());
		}
		finally
		{
		}
		return Isvalid;
	}
	public Boolean INDvalidations()
	{
		Boolean Isvalid=false;
		try
		{
			if ((txtscrib.getText().equals(""))||(cmbmarkettype.getSelectedItem().equals("——")||(txtsecId.getText().equals(""))))
			{
				JOptionPane.showMessageDialog(playerframe,"Invalid Inputs.", "Invalid Inputs",JOptionPane.WARNING_MESSAGE);
			}
			else if(cmbheadfeed.getSelectedIndex() == 0)
			{
				JOptionPane.showMessageDialog(playerframe,"Select Head feed", "Invalid Inputs",JOptionPane.WARNING_MESSAGE);
			}
			else
			{
				// need to implement method to go and validate instrument type and get security id
				Isvalid = true;
				
			}
		}
		catch(Exception ex)
		{
			Logger.error(ex.toString());
		}
		finally
		{
		}
		return Isvalid;
	}
	
	public void resetfields()
	{
		txtscrib.setText("");
		cmbmarkettype.setSelectedIndex(0);
		txtExpdd.setText("DD");
		txtExpmm.setText("MM");
		txtExpyyyy.setText("YYYY");
		txtprice.setText("0.0");
		//txtlotsize.setText("0");
		txtsecId.setText("");
		cmbright.setSelectedIndex(0);
		cmbheadfeed.setSelectedIndex(0);
	}
}
