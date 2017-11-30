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
import java.awt.event.ActionEvent;

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
	
	private int colid=0,colhname=1,colfeedsecid=2,colplayersecid =3, colscrib=4,colmtype=5,colexpdate=6,colprice=7,colrights=8,colinfo=9;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Player window = new Player();
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
	public Player() {
		tradelogpath = utils.configlogfile("TRADEBOT_LOG");
		initialize();
		
		//initialLoad("1213");
	}
	public void initialLoad(String playersecid)
	{
		
		try
		{
			loadExistingData("" , playersecid);
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
	
	public void loadExistingData(String Hname, String psecid)
	{
		String [][] existingdata;
		try
		{	
			int count = 0;
			count = dbobj.getRowCount("SELECT * FROM TBL_PLAYERS WHERE FEEDSUBJECTID = '"+psecid+"'");
			if (count != 0)
			{
				existingdata = dbobj.getMultiColumnRecords("SELECT * FROM TBL_PLAYERS WHERE FEEDSUBJECTID = '"+psecid+"'");
				Logger.info(existingdata.toString());
				if (existingdata[0][colplayersecid] != null)
				{
			
					if ((existingdata[0][colscrib] != null )&&(existingdata[0][colmtype] !=null))
					{
						txtscrib.setText(existingdata[0][colscrib]);
						cmbmarkettype.setSelectedItem(existingdata[0][colmtype].toString().trim());
						txtsecId.setText(existingdata[0][colplayersecid]);
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
		playerframe.setTitle("Add Player");
		playerframe.setBounds(100, 100, 665, 404);
		playerframe.getContentPane().setLayout(null);
		playerframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		playerframe.getContentPane().setBackground(new Color(51, 51, 51));
		
		JPanel innerpanel = new JPanel();
		innerpanel.setBounds(26, 61, 622, 299);
		innerpanel.setBackground(new Color(80,75,78));
		playerframe.getContentPane().add(innerpanel);
		innerpanel.setLayout(null);
		
		txtscrib = new JTextField();
		txtscrib.setBounds(77, 6, 138, 49);
		innerpanel.add(txtscrib);
		txtscrib.setHorizontalAlignment(SwingConstants.LEFT);
		txtscrib.setForeground(new Color(255, 220, 135));
		txtscrib.setFont(new Font("Verdana", Font.PLAIN, 20));
		txtscrib.setColumns(10);
		txtscrib.setCaretColor(Color.WHITE);
		txtscrib.setBackground(new Color(36, 34, 29));
		
		JLabel lblScrib = new JLabel("SCRIB");
		lblScrib.setBounds(6, 6, 74, 49);
		innerpanel.add(lblScrib);
		lblScrib.setHorizontalAlignment(SwingConstants.LEFT);
		lblScrib.setForeground(Color.WHITE);
		lblScrib.setFont(new Font("Verdana", Font.PLAIN, 20));
		
		cmbmarkettype = new JComboBox<Object>(new String[] {"—SELECT—", "STOCK", "FUTURE", "OPTIONS", "INDEX"});
		//cmbmarkettype.setModel(new DefaultComboBoxModel<Object>(new String[] {"—SELECT—", "STOCK", "FUTURE", "OPTIONS", "INDEX"}));
		cmbmarkettype.setFont(new Font("Verdana", Font.PLAIN, 18));
		cmbmarkettype.setBounds(210, 7, 160, 49);
		//cmbmarkettype.addItem("STOCK");
		//cmbmarkettype.addItem("FUTURE");
		//cmbmarkettype.addItem("OPTIONS");
		//cmbmarkettype.addItem("INDEX");
		
		
		innerpanel.add(cmbmarkettype);
		
		futopt_panel = new JPanel();
		futopt_panel.setBackground(Color.DARK_GRAY);
		futopt_panel.setBorder(null);
		futopt_panel.setBounds(16, 67, 573, 89);
		innerpanel.add(futopt_panel);
		futopt_panel.setLayout(null);
		
		JLabel lblDate = new JLabel("DATE");
		lblDate.setBounds(78, 6, 64, 26);
		lblDate.setHorizontalAlignment(SwingConstants.LEFT);
		lblDate.setForeground(Color.WHITE);
		lblDate.setFont(new Font("Verdana", Font.PLAIN, 20));
		futopt_panel.add(lblDate);
		
		txtExpdd = new JTextField();
		txtExpdd.setText("DD");
		txtExpdd.setPreferredSize(new Dimension(60, 50));
		txtExpdd.setHorizontalAlignment(SwingConstants.CENTER);
		txtExpdd.setForeground(Color.WHITE);
		txtExpdd.setColumns(4);
		txtExpdd.setCaretColor(Color.WHITE);
		txtExpdd.setBackground(new Color(80, 75, 78));
		txtExpdd.setBounds(17, 32, 49, 43);
		futopt_panel.add(txtExpdd);
		
		txtExpmm = new JTextField();
		txtExpmm.setText("MM");
		txtExpmm.setPreferredSize(new Dimension(60, 50));
		txtExpmm.setHorizontalAlignment(SwingConstants.CENTER);
		txtExpmm.setForeground(Color.WHITE);
		txtExpmm.setColumns(4);
		txtExpmm.setCaretColor(Color.WHITE);
		txtExpmm.setBackground(new Color(80, 75, 78));
		txtExpmm.setBounds(69, 32, 49, 43);
		futopt_panel.add(txtExpmm);
		
		txtExpyyyy = new JTextField();
		txtExpyyyy.setText("YYYY");
		txtExpyyyy.setPreferredSize(new Dimension(80, 50));
		txtExpyyyy.setHorizontalAlignment(SwingConstants.CENTER);
		txtExpyyyy.setForeground(Color.WHITE);
		txtExpyyyy.setColumns(6);
		txtExpyyyy.setCaretColor(Color.WHITE);
		txtExpyyyy.setBackground(new Color(80, 75, 78));
		txtExpyyyy.setBounds(121, 32, 74, 43);
		futopt_panel.add(txtExpyyyy);
		
		lblPrice = new JLabel("PRICE");
		lblPrice.setHorizontalAlignment(SwingConstants.LEFT);
		lblPrice.setForeground(Color.WHITE);
		lblPrice.setFont(new Font("Verdana", Font.PLAIN, 20));
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
		txtprice.setBounds(230, 32, 168, 43);
		futopt_panel.add(txtprice);
		
		lblRight = new JLabel("RIGHT");
		lblRight.setHorizontalAlignment(SwingConstants.LEFT);
		lblRight.setForeground(Color.WHITE);
		lblRight.setFont(new Font("Verdana", Font.PLAIN, 20));
		lblRight.setBounds(465, 6, 80, 26);
		futopt_panel.add(lblRight);
		
		cmbright = new JComboBox<Object>((new String[] {"——", "PUT", "CALL"}));
		//cmbright.setModel(new DefaultComboBoxModel<Object>(new String[] {"——", "PUT", "CALL"}));
		cmbright.setFont(new Font("Verdana", Font.PLAIN, 18));
		cmbright.setBounds(442, 27, 114, 49);
		futopt_panel.add(cmbright);
		
		JButton btnDelete = new JButton("DELETE");
		btnDelete.setPreferredSize(new Dimension(180, 50));
		btnDelete.setBounds(77, 241, 166, 37);
		innerpanel.add(btnDelete);
		
		JButton btnSave = new JButton("SAVE");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveformula();
			}
		});
		btnSave.setPreferredSize(new Dimension(180, 50));
		btnSave.setBounds(364, 241, 166, 37);
		innerpanel.add(btnSave);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.DARK_GRAY);
		panel_1.setBorder(new LineBorder(Color.GREEN, 3, true));
		panel_1.setBounds(43, 168, 530, 49);
		innerpanel.add(panel_1);
		panel_1.setLayout(null);
		
		loadHeadCombo();
		cmbheadfeed.setBounds(162, 6, 362, 37);
		panel_1.add(cmbheadfeed);
		cmbheadfeed.setFont(new Font("Verdana", Font.PLAIN, 18));
		
		JLabel lblHeadFeed = new JLabel("HEAD FEED");
		lblHeadFeed.setHorizontalAlignment(SwingConstants.LEFT);
		lblHeadFeed.setForeground(Color.WHITE);
		lblHeadFeed.setFont(new Font("Verdana", Font.PLAIN, 20));
		lblHeadFeed.setBounds(23, 10, 127, 26);
		panel_1.add(lblHeadFeed);
		
		JLabel label = new JLabel("SEC-ID");
		label.setHorizontalAlignment(SwingConstants.LEFT);
		label.setForeground(Color.WHITE);
		label.setFont(new Font("Verdana", Font.PLAIN, 20));
		label.setBounds(383, 6, 82, 49);
		innerpanel.add(label);
		
		txtsecId = new JTextField();
		txtsecId.setHorizontalAlignment(SwingConstants.LEFT);
		txtsecId.setForeground(new Color(255, 220, 135));
		txtsecId.setFont(new Font("Verdana", Font.PLAIN, 20));
		txtsecId.setColumns(10);
		txtsecId.setCaretColor(Color.WHITE);
		txtsecId.setBackground(new Color(36, 34, 29));
		txtsecId.setBounds(459, 6, 148, 49);
		innerpanel.add(txtsecId);
		
		JLabel lblHead = new JLabel("<dynamic>");
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
			String hname = "";
			String feedsecId ="";
			switch (markettype) {
			case "STOCK":
				if (STKvalidations())
				{
					dbobj.executeNonQuery("UPDATE TBL_PLAYERS SET HEADNAME = '"+hname+"', FEEDSUBJECTID = '"+feedsecId+"', TRADESUBJECTID = '"+playerid+"', SCRIB = '"+txtscrib.getText()+"',");
					JOptionPane.showMessageDialog(playerframe,"Record Saved !!", "Success",JOptionPane.WARNING_MESSAGE);				
				}
				break;
			case "FUTURE":
				if(FUTvalidations())
				{
					String date = txtExpdd.getText()+"-"+txtExpmm.getText()+"-"+txtExpyyyy.getText();
					dbobj.executeNonQuery("UPDATE TBL_PLAYERS SET FEEDSUBJECTID = '"+txtsecId.getText().toString()+"', SCRIB = '"+txtscrib.getText().toString()+"' ,"
							+ "MARKETTYPE ='"+cmbmarkettype.getSelectedItem().toString()+"', EXPDATE = '"+date+"',PRICE=null,RIGHTS=null where HEADNAME ='"+headname+"'");
						JOptionPane.showMessageDialog(playerframe,"Record Saved !!", "Success",JOptionPane.WARNING_MESSAGE);	
				}
				break;
			case "OPTIONS":
				if(OPTvalidations())
				{
					String date = txtExpdd.getText()+"-"+txtExpmm.getText()+"-"+txtExpyyyy.getText();
					dbobj.executeNonQuery("UPDATE TBL_PLAYERS SET FEEDSUBJECTID = '"+txtsecId.getText().toString()+"', SCRIB = '"+txtscrib.getText().toString()+"' ,"
							+ "MARKETTYPE ='"+cmbmarkettype.getSelectedItem().toString()+"', EXPDATE = '"+date+"', PRICE="+Double.parseDouble(txtprice.getText())+",RIGHTS='"+cmbright.getSelectedItem().toString()+"' where HEADNAME ='"+headname+"'");
						JOptionPane.showMessageDialog(playerframe,"Record Saved !!", "Success",JOptionPane.WARNING_MESSAGE);	
				}
				break;
			case "INDEX":
				if(INDvalidations())
				{
					dbobj.executeNonQuery("UPDATE TBL_PLAYERS SET FEEDSUBJECTID = '"+txtsecId.getText().toString()+"', SCRIB = '"+txtscrib.getText().toString()+"' ,"
							+ "MARKETTYPE ='"+cmbmarkettype.getSelectedItem().toString()+"', EXPDATE=null,PRICE=null,RIGHTS=null where HEADNAME ='"+headname+"'");
						JOptionPane.showMessageDialog(playerframe,"Record Saved !!", "Success",JOptionPane.WARNING_MESSAGE);	
				}
				break;
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
		txtsecId.setText("");
		cmbright.setSelectedIndex(0);
		cmbheadfeed.setSelectedIndex(0);
	}
	
}
