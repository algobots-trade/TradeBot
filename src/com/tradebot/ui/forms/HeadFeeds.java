package com.tradebot.ui.forms;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
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
import java.util.Properties;
import java.awt.event.ActionEvent;
import org.pmw.tinylog.*;
import org.pmw.tinylog.writers.FileWriter;
import org.pmw.tinylog.writers.Writer;

import com.tradebot.dbcommons.tradebot_utility;

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
	
	
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HeadFeeds window = new HeadFeeds("H1");
					window.headFeed.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	
	public void initialLoad()
	{
		
		try
		{
			tradelogpath = utils.configlogfile("TRADEBOT_LOG");
			Logger.info("Head Feed Triggered - Log Path --> "+tradelogpath);
		}
		catch(Exception ex)
		{
			Logger.error(ex.getMessage());
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
		initialLoad();
		initialize(headFeedName);
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
		headFeed.setBounds(100, 100, 665, 357);
		headFeed.getContentPane().setLayout(null);
		headFeed.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		headFeed.getContentPane().setBackground(new Color(51, 51, 51));
		
		innerpanel = new JPanel();
		innerpanel.setBounds(26, 68, 613, 230);
		innerpanel.setBackground(new Color(80,75,78));
		headFeed.getContentPane().add(innerpanel);
		innerpanel.setLayout(null);
		
		txtscrib = new JTextField();
		txtscrib.setBounds(97, 6, 248, 49);
		innerpanel.add(txtscrib);
		txtscrib.setHorizontalAlignment(SwingConstants.LEFT);
		txtscrib.setForeground(new Color(255, 220, 135));
		txtscrib.setFont(new Font("Verdana", Font.PLAIN, 20));
		txtscrib.setColumns(10);
		txtscrib.setCaretColor(Color.WHITE);
		txtscrib.setBackground(new Color(36, 34, 29));
		
		lblScrib = new JLabel("SCRIB");
		lblScrib.setBounds(16, 6, 104, 49);
		innerpanel.add(lblScrib);
		lblScrib.setHorizontalAlignment(SwingConstants.LEFT);
		lblScrib.setForeground(Color.WHITE);
		lblScrib.setFont(new Font("Verdana", Font.PLAIN, 20));
		
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
		cmbmarkettype.setBounds(354, 6, 235, 49);
		
		
		
		innerpanel.add(cmbmarkettype);
		
		futopt_panel = new JPanel();
		futopt_panel.setBackground(Color.DARK_GRAY);
		futopt_panel.setBorder(new MatteBorder(1, 1, 1, 1, (Color) Color.RED));
		futopt_panel.setBounds(16, 67, 573, 89);
		innerpanel.add(futopt_panel);
		futopt_panel.setLayout(null);
		
		lblDate = new JLabel("DATE");
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
		lblRight.setBounds(453, 6, 80, 26);
		futopt_panel.add(lblRight);
		
		cmbright = new JComboBox();
		cmbright.setModel(new DefaultComboBoxModel(new String[] {"——", "PUT", "CALL"}));
		cmbright.setFont(new Font("Verdana", Font.PLAIN, 18));
		cmbright.setBounds(442, 27, 114, 49);
		futopt_panel.add(cmbright);
		
		btnDelete = new JButton("DELETE");
		btnDelete.setPreferredSize(new Dimension(180, 50));
		btnDelete.setBounds(78, 181, 166, 37);
		innerpanel.add(btnDelete);
		
		btnSave = new JButton("SAVE");
		btnSave.setPreferredSize(new Dimension(180, 50));
		btnSave.setBounds(365, 181, 166, 37);
		innerpanel.add(btnSave);
		
		lblHead = new JLabel("HEAD FEED - "+headname);
		lblHead.setHorizontalAlignment(SwingConstants.CENTER);
		lblHead.setForeground(new Color(255, 220, 135));
		lblHead.setFont(new Font("Verdana", Font.BOLD, 22));
		lblHead.setBounds(6, 13, 653, 43);
		headFeed.getContentPane().add(lblHead);
		headFeed.setVisible(true);
		
		
		builtSTKControls();
	}
}
