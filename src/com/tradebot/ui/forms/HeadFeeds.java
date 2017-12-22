package com.tradebot.ui.forms;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.border.MatteBorder;
import java.awt.Dimension;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.awt.event.ActionEvent;
import org.pmw.tinylog.*;
import org.pmw.tinylog.writers.FileWriter;
import org.pmw.tinylog.writers.Writer;

import com.tradebot.dbcommons.DateValidator;
import com.tradebot.dbcommons.db_commons;
import com.tradebot.dbcommons.tradebot_utility;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

public class HeadFeeds {

	private JFrame headFeed;
	private JTextField txtscrib;
	private JTextField txtExpdd;
	private JTextField txtExpmm;
	private JTextField txtExpyyyy;
	private JTextField txtprice;
	private JPanel innerpanel;
	private JLabel lblScrib,lblPrice,lblDate,lblRight,lblHead;
	private JComboBox<?> cmbmarkettype,cmbright; 
	private JPanel futopt_panel;
	private JButton btnDelete,btnSave;
	
	private String configprop=System.getProperty("user.dir")+File.separator+"resource"+File.separator+"config.properties";
	String tradelogpath;
	tradebot_utility utils = new tradebot_utility(); 
	db_commons dbobj=new db_commons();
	
	private int colid=0,colhname=1,colsecid=2,colscrib=3,colmtype=4,colexpdate=5,colprice=6,colrights=7, colinfo =8;
	private JTextField txtsecId;
	private JLabel lblMarket;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HeadFeeds window = new HeadFeeds("H4");
					window.headFeed.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	

	public void initialLoad(String Hname)
	{
		
		try
		{
			loadExistingData(Hname);
		}
		catch(Exception ex)
		{
			Logger.error(ex.getMessage());
		}
		finally
		{
			
		}
	}
	
	public void loadExistingData(String Hname)
	{
		String [][] existingdata;
		try
		{			
			existingdata = dbobj.getMultiColumnRecords("SELECT * FROM TBL_HEADFEEDS WHERE HEADNAME='"+Hname+"'");
			
			Logger.info(existingdata.toString());
			if (existingdata[0][colsecid] != null)
			{
		
				if ((existingdata[0][colscrib] != null )&&(existingdata[0][colmtype] !=null))
				{
					txtscrib.setText(existingdata[0][colscrib]);
					cmbmarkettype.setSelectedItem(existingdata[0][colmtype].toString().trim());
					txtsecId.setText(existingdata[0][colsecid]);
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
		catch(Exception ex)
		{		
			Logger.error(ex);
		}
		finally
		{		
		}
	}

	/**
	 * Create the application.
	 */
	public HeadFeeds(String headFeedName) 
	{
		//HeadFeeds window = new HeadFeeds(headFeedName);
		//window.headFeed.setVisible(true);
		tradelogpath = utils.configlogfile("TRADEBOT_LOG");
		initialize(headFeedName);
		initialLoad(headFeedName);
		
	}
	/**
	 * set proper control visibility according to the market type selection
	 */
	public void builtFUTControls()
	{
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
	public void builtOPTControls()
	{
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
	public void builtSTKControls()
	{
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
	public void builtINDControls()
	{
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
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(String headname) 
	{
		
		headFeed = new JFrame();
		headFeed.setTitle(headname);
		headFeed.setBounds(100, 100, 665, 340);
		headFeed.getContentPane().setLayout(null);
		headFeed.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		headFeed.getContentPane().setBackground(new Color(51, 51, 51));
		headFeed.setVisible(true);
		
		innerpanel = new JPanel();
		innerpanel.setBounds(26, 53, 613, 245);
		innerpanel.setBackground(new Color(80,75,78));
		headFeed.getContentPane().add(innerpanel);
		innerpanel.setLayout(null);
		
		txtscrib = new JTextField();
		txtscrib.setBounds(18, 42, 179, 37);
		innerpanel.add(txtscrib);
		txtscrib.setHorizontalAlignment(SwingConstants.LEFT);
		txtscrib.setForeground(new Color(255, 220, 135));
		txtscrib.setFont(new Font("Verdana", Font.PLAIN, 20));
		txtscrib.setColumns(10);
		txtscrib.setCaretColor(Color.WHITE);
		txtscrib.setBackground(new Color(36, 34, 29));
		
		lblScrib = new JLabel("SCRIB");
		lblScrib.setBounds(75, 0, 82, 49);
		innerpanel.add(lblScrib);
		lblScrib.setHorizontalAlignment(SwingConstants.LEFT);
		lblScrib.setForeground(Color.WHITE);
		lblScrib.setFont(new Font("Verdana", Font.PLAIN, 16));
		
		cmbmarkettype = new JComboBox(new DefaultComboBoxModel(new String[] {"——", "STOCK", "FUTURE","OPTIONS","INDEX"}));
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
		cmbmarkettype.setBounds(219, 37, 205, 49);
		
		
		
		innerpanel.add(cmbmarkettype);
		
		futopt_panel = new JPanel();
		futopt_panel.setBackground(Color.DARK_GRAY);
		futopt_panel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		futopt_panel.setBounds(18, 91, 573, 89);
		innerpanel.add(futopt_panel);
		futopt_panel.setLayout(null);
		
		lblDate = new JLabel("DATE");
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
		txtExpdd.setBounds(17, 32, 49, 43);
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
		txtExpmm.setBounds(69, 32, 49, 43);
		txtExpmm.addFocusListener(new FocusListener() {
		    public void focusGained(FocusEvent e) {
		    	txtExpmm.setText("");
		    }

		    public void focusLost(FocusEvent e) {
		    	    if(txtExpmm.getText().equals(""))
		    	    {
		    	    	txtExpmm.setText("MM");
		    	    }
		        // nothing
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
		txtExpyyyy.setBounds(121, 32, 74, 43);
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
		txtprice.setBounds(230, 32, 168, 43);
		futopt_panel.add(txtprice);
		
		lblRight = new JLabel("RIGHT");
		lblRight.setHorizontalAlignment(SwingConstants.LEFT);
		lblRight.setForeground(Color.WHITE);
		lblRight.setFont(new Font("Verdana", Font.PLAIN, 16));
		lblRight.setBounds(453, 6, 80, 26);
		futopt_panel.add(lblRight);
		
		cmbright = new JComboBox();
		cmbright.setModel(new DefaultComboBoxModel(new String[] {"——", "PUT", "CALL"}));
		cmbright.setFont(new Font("Verdana", Font.PLAIN, 18));
		cmbright.setBounds(442, 27, 114, 49);
		futopt_panel.add(cmbright);
		
		btnDelete = new JButton("DELETE");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetfields();
				dbobj.executeNonQuery("UPDATE TBL_HEADFEEDS SET FEEDSUBJECTID=null, SCRIB=null, MARKETTYPE=null,EXPDATE=null,PRICE=null,RIGHTS=null where HEADNAME ='"+headname+"'");
				JOptionPane.showMessageDialog(headFeed,"Record Deleted !!", "Success",JOptionPane.WARNING_MESSAGE);				
						
			}
		});
		btnDelete.setPreferredSize(new Dimension(180, 50));
		btnDelete.setBounds(78, 192, 166, 37);
		innerpanel.add(btnDelete);
		
		btnSave = new JButton("SAVE");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				if ((txtscrib.getText().equals(""))||(cmbmarkettype.getSelectedIndex() == 0))
				{
					JOptionPane.showMessageDialog(headFeed,"Check scrib name field and market type.", "Invalid Inputs",JOptionPane.WARNING_MESSAGE);
				}
				else
				{
					saveHeadFeedData(cmbmarkettype.getSelectedItem().toString(), headname);
				}
			}
		});
		btnSave.setPreferredSize(new Dimension(180, 50));
		btnSave.setBounds(365, 192, 166, 37);
		innerpanel.add(btnSave);
		
		JLabel lblSecid = new JLabel("SEC-ID");
		lblSecid.setHorizontalAlignment(SwingConstants.LEFT);
		lblSecid.setForeground(Color.WHITE);
		lblSecid.setFont(new Font("Verdana", Font.PLAIN, 16));
		lblSecid.setBounds(479, 0, 82, 49);
		innerpanel.add(lblSecid);
		
		txtsecId = new JTextField();
		txtsecId.setHorizontalAlignment(SwingConstants.LEFT);
		txtsecId.setForeground(new Color(255, 220, 135));
		txtsecId.setFont(new Font("Verdana", Font.PLAIN, 20));
		txtsecId.setColumns(10);
		txtsecId.setCaretColor(Color.WHITE);
		txtsecId.setBackground(new Color(36, 34, 29));
		txtsecId.setBounds(443, 44, 148, 31);
		innerpanel.add(txtsecId);
		
		lblMarket = new JLabel("MARKET");
		lblMarket.setHorizontalAlignment(SwingConstants.LEFT);
		lblMarket.setForeground(Color.WHITE);
		lblMarket.setFont(new Font("Verdana", Font.PLAIN, 16));
		lblMarket.setBounds(273, 0, 116, 49);
		innerpanel.add(lblMarket);
		
		lblHead = new JLabel("HEAD FEED - "+headname);
		lblHead.setHorizontalAlignment(SwingConstants.CENTER);
		lblHead.setForeground(new Color(255, 220, 135));
		lblHead.setFont(new Font("Verdana", Font.BOLD, 22));
		lblHead.setBounds(6, -2, 653, 43);
		headFeed.getContentPane().add(lblHead);
		headFeed.setVisible(true);
		
		
		builtSTKControls();
	}
	
	public Boolean saveHeadFeedData(String markettype, String headname)
	{
		Boolean Isvalid=false;
		try
		{
			switch (markettype) {
			case "STOCK":
				if (STKvalidations())
				{
					dbobj.executeNonQuery("UPDATE TBL_HEADFEEDS SET FEEDSUBJECTID = '"+txtsecId.getText().toString()+"', SCRIB = '"+txtscrib.getText().toString()+"' ,"
						+ "MARKETTYPE ='"+cmbmarkettype.getSelectedItem().toString()+"', EXPDATE=null,PRICE=null,RIGHTS=null where HEADNAME ='"+headname+"'");
					JOptionPane.showMessageDialog(headFeed,"Record Saved !!", "Success",JOptionPane.WARNING_MESSAGE);				
				}
				break;
			case "FUTURE":
				if(FUTvalidations())
				{
					String date = txtExpdd.getText()+"-"+txtExpmm.getText()+"-"+txtExpyyyy.getText();
					dbobj.executeNonQuery("UPDATE TBL_HEADFEEDS SET FEEDSUBJECTID = '"+txtsecId.getText().toString()+"', SCRIB = '"+txtscrib.getText().toString()+"' ,"
							+ "MARKETTYPE ='"+cmbmarkettype.getSelectedItem().toString()+"', EXPDATE = '"+date+"',PRICE=null,RIGHTS=null where HEADNAME ='"+headname+"'");
						JOptionPane.showMessageDialog(headFeed,"Record Saved !!", "Success",JOptionPane.WARNING_MESSAGE);	
				}
				break;
			case "OPTIONS":
				if(OPTvalidations())
				{
					String date = txtExpdd.getText()+"-"+txtExpmm.getText()+"-"+txtExpyyyy.getText();
					dbobj.executeNonQuery("UPDATE TBL_HEADFEEDS SET FEEDSUBJECTID = '"+txtsecId.getText().toString()+"', SCRIB = '"+txtscrib.getText().toString()+"' ,"
							+ "MARKETTYPE ='"+cmbmarkettype.getSelectedItem().toString()+"', EXPDATE = '"+date+"', PRICE="+Double.parseDouble(txtprice.getText())+",RIGHTS='"+cmbright.getSelectedItem().toString()+"' where HEADNAME ='"+headname+"'");
						JOptionPane.showMessageDialog(headFeed,"Record Saved !!", "Success",JOptionPane.WARNING_MESSAGE);	
				}
				break;
			case "INDEX":
				if(INDvalidations())
				{
					dbobj.executeNonQuery("UPDATE TBL_HEADFEEDS SET FEEDSUBJECTID = '"+txtsecId.getText().toString()+"', SCRIB = '"+txtscrib.getText().toString()+"' ,"
							+ "MARKETTYPE ='"+cmbmarkettype.getSelectedItem().toString()+"', EXPDATE=null,PRICE=null,RIGHTS=null where HEADNAME ='"+headname+"'");
						JOptionPane.showMessageDialog(headFeed,"Record Saved !!", "Success",JOptionPane.WARNING_MESSAGE);	
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
				JOptionPane.showMessageDialog(headFeed,"Invalid Inputs.", "Invalid Inputs",JOptionPane.WARNING_MESSAGE);
				Logger.warn("Check scrib name field and market type.");
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
				JOptionPane.showMessageDialog(headFeed,"Invalid Inputs.", "Invalid Inputs",JOptionPane.WARNING_MESSAGE);
			}
			else if ((txtExpdd.getText().equals("DD"))||(txtExpmm.getText().equals("MM"))||(txtExpyyyy.getText().equals("YYYY")))
			{
				
				JOptionPane.showMessageDialog(headFeed,"Please Enter valid date (DD-MM-YYYY)", "Invalid Inputs",JOptionPane.WARNING_MESSAGE);
				
			}
			else if (!(dv.isThisDateValid(exdate, "dd-MM-yyyy"))||!(dv.isThisDateWeekend(exdate,  "dd-MM-yyyy")))
			{
				JOptionPane.showMessageDialog(headFeed,"Invalid Feed Date or It might falling on weekends !!","Alert",JOptionPane.ERROR_MESSAGE);
				  
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
				JOptionPane.showMessageDialog(headFeed,"Invalid Inputs.", "Invalid Inputs",JOptionPane.WARNING_MESSAGE);
			}
			else if ((txtExpdd.getText().equals("DD"))||(txtExpmm.getText().equals("MM"))||(txtExpyyyy.getText().equals("YYYY")))
			{
				JOptionPane.showMessageDialog(headFeed,"Please Enter valid date (DD-MM-YYYY)", "Invalid Inputs",JOptionPane.WARNING_MESSAGE);
			}
			else if (!(dv.isThisDateValid(exdate, "dd-MM-yyyy"))||!(dv.isThisDateWeekend(exdate,  "dd-MM-yyyy")))
			{
				JOptionPane.showMessageDialog(headFeed,"Invalid Feed Date or It might falling on weekends !!","Alert",JOptionPane.ERROR_MESSAGE);
				  
			}
			else if ((txtprice.getText().equals(""))||(cmbright.getSelectedIndex()==0))
			{
				JOptionPane.showMessageDialog(headFeed,"Price and Rights is Mondatory", "Invalid Inputs",JOptionPane.WARNING_MESSAGE);
				
			}
			else if (utils.isNumeric(txtprice.getText()) == false)
			{
				JOptionPane.showMessageDialog(headFeed,"price is invalid", "Invalid Inputs",JOptionPane.WARNING_MESSAGE);
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
				JOptionPane.showMessageDialog(headFeed,"Invalid Inputs.", "Invalid Inputs",JOptionPane.WARNING_MESSAGE);
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
	}
}
