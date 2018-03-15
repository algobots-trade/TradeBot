package com.tradebot.ui.forms;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.border.MatteBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import java.awt.Dimension;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import javax.swing.JCheckBox;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

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
	private JCheckBox chksameplayer;
	private JTable table;
	String records[][];
	String col[]= {"FEED-ID","SCRIB","MARKET","EXP-DATE","PRICE","RIGHTS"};
	
	private String configprop=System.getProperty("user.dir")+File.separator+"resource"+File.separator+"config.properties";
	String tradelogpath;
	tradebot_utility utils = new tradebot_utility(); 
	db_commons dbobj=new db_commons();
	
	private int colid=0,colsecid=1,colscrib=2,colmtype=3,colexpdate=4,colprice=5,colrights=6, colinfo =7;
	private JTextField txtsecId;
	private JLabel lblMarket;
	private JScrollBar scrollBar;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					HeadFeeds window = new HeadFeeds(null);
					window.headFeed.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	

//	public void initialLoad(String Headsecid)
//	{
//		
//		try
//		{
//			if (Headsecid != null)
//			{
//				loadExistingData(Headsecid);
//			}
//		}
//		catch(Exception ex)
//		{
//			Logger.error(ex.getMessage());
//		}
//		finally
//		{
//			
//		}
//	}
	
	public void loadExistingData(String headsecid)
	{
		String [][] existingdata;
		try
		{			
			existingdata = dbobj.getMultiColumnRecords("SELECT * FROM TBL_HEADFEEDS WHERE FEEDSUBJECTID ='"+headsecid+"'");
			
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
	public HeadFeeds(String Headsecid) 
	{
		tradelogpath = utils.configlogfile("TRADEBOT_LOG");
		initialize(Headsecid);
		//initialLoad(Headsecid);
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
	private void initialize(String headsecid) 
	{
		
		headFeed = new JFrame();
		headFeed.setTitle("HEAD FEED DETAILS");
		headFeed.setBounds(100, 100, 671, 792);
		headFeed.getContentPane().setLayout(null);
		headFeed.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		headFeed.getContentPane().setBackground(new Color(51, 51, 51));
		headFeed.setVisible(true);
		
		innerpanel = new JPanel();
		innerpanel.setBounds(26, 53, 616, 689);
		innerpanel.setBackground(new Color(80,75,78));
		headFeed.getContentPane().add(innerpanel);
		innerpanel.setLayout(null);
		
		txtscrib = new JTextField();
		txtscrib.setBounds(18, 77, 170, 31);
		innerpanel.add(txtscrib);
		txtscrib.setHorizontalAlignment(SwingConstants.LEFT);
		txtscrib.setForeground(new Color(255, 220, 135));
		txtscrib.setFont(new Font("Verdana", Font.PLAIN, 20));
		txtscrib.setColumns(10);
		txtscrib.setCaretColor(Color.WHITE);
		txtscrib.setBackground(new Color(36, 34, 29));
		
		lblScrib = new JLabel("SCRIB");
		lblScrib.setBounds(72, 35, 82, 49);
		innerpanel.add(lblScrib);
		lblScrib.setHorizontalAlignment(SwingConstants.LEFT);
		lblScrib.setForeground(Color.WHITE);
		lblScrib.setFont(new Font("Verdana", Font.PLAIN, 16));
		
		cmbmarkettype = new JComboBox(new DefaultComboBoxModel(new String[] {"--Select--", "STOCK", "FUTURE","OPTIONS","INDEX"}));
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
				case "--Select--":
					builtSTKControls();
					break;
				default:
					builtSTKControls();
					break;
				}
			}
		});
		cmbmarkettype.setFont(new Font("Verdana", Font.PLAIN, 18));
		cmbmarkettype.setBounds(219, 78, 170, 31);
		
		
		
		innerpanel.add(cmbmarkettype);
		
		futopt_panel = new JPanel();
		futopt_panel.setBackground(Color.DARK_GRAY);
		futopt_panel.setBorder(new LineBorder(Color.LIGHT_GRAY));
		futopt_panel.setBounds(18, 126, 588, 89);
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
		txtExpdd.setBounds(17, 32, 49, 35);
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
		txtExpmm.setBounds(69, 32, 49, 35);
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
		txtExpyyyy.setBounds(121, 32, 74, 35);
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
		lblPrice.setBounds(272, 6, 64, 26);
		futopt_panel.add(lblPrice);
		
		txtprice = new JTextField();
		txtprice.setText("0.0");
		txtprice.setPreferredSize(new Dimension(80, 50));
		txtprice.setHorizontalAlignment(SwingConstants.CENTER);
		txtprice.setForeground(Color.WHITE);
		txtprice.setColumns(6);
		txtprice.setCaretColor(Color.WHITE);
		txtprice.setBackground(new Color(80, 75, 78));
		txtprice.setBounds(220, 32, 168, 35);
		futopt_panel.add(txtprice);
		
		lblRight = new JLabel("RIGHT");
		lblRight.setHorizontalAlignment(SwingConstants.LEFT);
		lblRight.setForeground(Color.WHITE);
		lblRight.setFont(new Font("Verdana", Font.PLAIN, 16));
		lblRight.setBounds(464, 6, 80, 26);
		futopt_panel.add(lblRight);
		
		cmbright = new JComboBox();
		cmbright.setModel(new DefaultComboBoxModel(new String[] {"--Select--", "PUT", "CALL"}));
		cmbright.setFont(new Font("Verdana", Font.PLAIN, 18));
		cmbright.setBounds(423, 32, 155, 35);
		futopt_panel.add(cmbright);
		
		btnDelete = new JButton("DELETE");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetfields();
				//dbobj.executeNonQuery("UPDATE TBL_HEADFEEDS SET FEEDSUBJECTID=null, SCRIB=null, MARKETTYPE=null,EXPDATE=null,PRICE=null,RIGHTS=null where HEADNAME ='"+headname+"'");
				dbobj.executeNonQuery("DELETE FROM TBL_HEADFEEDS WHERE FEEDSUBJECTID ='"+headsecid+"'");
				JOptionPane.showMessageDialog(headFeed,"Record Deleted !!", "Success",JOptionPane.WARNING_MESSAGE);				
						
			}
		});
		btnDelete.setPreferredSize(new Dimension(180, 50));
		btnDelete.setBounds(78, 227, 166, 37);
		btnDelete.setVisible(false);
		//innerpanel.add(btnDelete);
		
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
					saveHeadFeedData(cmbmarkettype.getSelectedItem().toString(), headsecid);
					
				}
			}
		});
		btnSave.setPreferredSize(new Dimension(180, 50));
		btnSave.setBounds(356, 230, 250, 37);
		innerpanel.add(btnSave);
		
		JLabel lblSecid = new JLabel("SEC-ID");
		lblSecid.setHorizontalAlignment(SwingConstants.LEFT);
		lblSecid.setForeground(Color.WHITE);
		lblSecid.setFont(new Font("Verdana", Font.PLAIN, 16));
		lblSecid.setBounds(461, 35, 82, 49);
		innerpanel.add(lblSecid);
		
		txtsecId = new JTextField();
		txtsecId.setToolTipText("Once Saved Cannot be Edited, Need to Delete and recreate it !!");
		txtsecId.setHorizontalAlignment(SwingConstants.LEFT);
		txtsecId.setForeground(new Color(255, 220, 135));
		txtsecId.setFont(new Font("Verdana", Font.PLAIN, 20));
		txtsecId.setColumns(10);
		txtsecId.setCaretColor(Color.WHITE);
		txtsecId.setBackground(new Color(36, 34, 29));
		txtsecId.setBounds(420, 77, 123, 31);
		innerpanel.add(txtsecId);
		
		lblMarket = new JLabel("MARKET");
		lblMarket.setHorizontalAlignment(SwingConstants.LEFT);
		lblMarket.setForeground(Color.WHITE);
		lblMarket.setFont(new Font("Verdana", Font.PLAIN, 16));
		lblMarket.setBounds(280, 35, 116, 49);
		innerpanel.add(lblMarket);
		
		chksameplayer = new JCheckBox("Trade On Same Player");
		chksameplayer.setBounds(219, 7, 214, 23);
		chksameplayer.setForeground(Color.WHITE);
		chksameplayer.setBackground(new Color(80,75,58));
		chksameplayer.setFont(new Font("Verdana", Font.PLAIN, 16));
		innerpanel.add(chksameplayer);
		
		
		//JScrollPane scrollPane = new JScrollPane();
		//scrollPane.setBounds(10, 292, 596, 386);
		//innerpanel.add(scrollPane);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 278, 596, 21);
		innerpanel.add(separator);
		
		records =dbobj.getMultiColumnRecords("SELECT FEEDSUBJECTID, SCRIB, MARKETTYPE, EXPDATE, PRICE, RIGHTS FROM TBL_HEADFEEDS;");
		//records =dbobj.getMultiColumnRecords("SELECT FEEDSUBJECTID as \"FEED-ID\", SCRIB, MARKETTYPE as \"MARTKET\", EXPDATE as \"EXP-DATE\", PRICE, RIGHTS FROM TBL_HEADFEEDS ;");
		TableModel model = new DefaultTableModel(records, col);
		table = new JTable(model){
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
		table.setModel(model);	
		JTableHeader header = table.getTableHeader();
	    //header.setBackground(new Color(36,34,29));
	    //header.setForeground(new Color(255,220,135));
		header.setForeground(new Color(36,34,29));
	    header.setFont(new Font("Tahoma", Font.BOLD, 13));
	    JScrollPane scrollPane = new JScrollPane(table);
	    scrollPane.setBounds(10, 291, 596, 387);
	    innerpanel.add(scrollPane);
	    scrollPane.setEnabled(false);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setViewportBorder(null);
		scrollPane.setBorder(null);
		
		JButton btnclear = new JButton("CLEAR");
		btnclear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				resetfields();
			}
		});
		btnclear.setPreferredSize(new Dimension(180, 50));
		btnclear.setBounds(18, 230, 250, 37);
		innerpanel.add(btnclear);
		
		JButton btnFind = new JButton("Check");
		btnFind.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				try
				{
					txtsecId.setText("-");
					switch (cmbmarkettype.getSelectedItem().toString()) {
					case "STOCK":
						if (STKvalidations())
						{
							
						}
						break;
					case "FUTURE":
						if(FUTvalidations())
						{
							String date = txtExpdd.getText()+"-"+txtExpmm.getText()+"-"+txtExpyyyy.getText();
							
						}
						break;
					case "OPTIONS":
						if(OPTvalidations())
						{
							String date = txtExpdd.getText()+"-"+txtExpmm.getText()+"-"+txtExpyyyy.getText();
							
						}
						break;
					case "INDEX":
						if(INDvalidations())
						{
							
						}
						break;
					}
				}
				catch(Exception ex)
				{
					
				}
			}
		});
		btnFind.setBounds(545, 77, 61, 31);
		innerpanel.add(btnFind);
		table.addMouseListener(new MouseAdapter(){
		     public void mouseClicked(MouseEvent e){
		      if (e.getClickCount() == 2){
		         System.out.println(" double click" );
		         loadExistingData(table.getValueAt(table.getSelectedRow(), 0).toString());
		         btnSave.setEnabled(false);
		         }
		      }
		     } );
//		table.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
//	        public void valueChanged(ListSelectionEvent event) {
//	        	//if (table.getSelectedRow() !=null)
//				//{		
//	        		//loadExistingData(table.getValueAt(table.getSelectedRow(), 0).toString());
//				//}
//	        	btnSave.setEnabled(false);
//	        	//table.clearSelection();
//	            //System.out.println(table.getValueAt(table.getSelectedRow(), 0).toString());
//	        }
//	    });
		
		table.addKeyListener(new KeyAdapter() {
		      @Override
		      public void keyPressed(KeyEvent e){
		        if (e.isControlDown() && e.getKeyCode() == 68)
		        {
		        	int opcion = JOptionPane.showConfirmDialog(null, "Are you sure ?", "Delete Player", JOptionPane.YES_NO_OPTION);
					if (opcion == 0) 
					{ 
				          dbobj.executeNonQuery("DELETE FROM TBL_HEADFEEDS WHERE FEEDSUBJECTID ='"+table.getValueAt(table.getSelectedRow(), 0)+"'");
				          dbobj.executeNonQuery("DELETE FROM TBL_PLAYERS WHERE FEEDSUBJECTID ='"+table.getValueAt(table.getSelectedRow(), 0)+"'");
				          dbobj.executeNonQuery("DELETE FROM TBL_TRADEBOARD WHERE FEEDSECID='"+table.getValueAt(table.getSelectedRow(), 0)+"'");
				          resetfields();
				          JOptionPane.showMessageDialog(headFeed,"Head Feed Deleted & Corrsponding Player Got Removed!!", "Success",JOptionPane.WARNING_MESSAGE);	
				          records=null;
						  records = dbobj.getMultiColumnRecords("SELECT FEEDSUBJECTID, SCRIB, MARKETTYPE, EXPDATE, PRICE, RIGHTS FROM TBL_HEADFEEDS;");
				          TableModel newmodel = new DefaultTableModel(records, col);
						  table.setModel(newmodel);
						  table.clearSelection();
					}
		        }
		        }});
		
		
		lblHead = new JLabel(" HEAD FEED ");
		lblHead.setHorizontalAlignment(SwingConstants.CENTER);
		lblHead.setForeground(new Color(255, 220, 135));
		lblHead.setFont(new Font("Verdana", Font.BOLD, 22));

		lblHead.setBounds(6, -2, 653, 43);
		headFeed.getContentPane().add(lblHead);
		headFeed.setVisible(true);
		
		
		builtSTKControls();
	}
	
	public Boolean saveHeadFeedData(String markettype, String headsecid)
	{
		Boolean Isvalid=false;
		try
		{
			if ( 0 == dbobj.getRowCount("SELECT * FROM TBL_HEADFEEDS WHERE FEEDSUBJECTID ='"+txtsecId.getText().toString()+"'"))
			{
					switch (markettype) {
					case "STOCK":
						if (STKvalidations())
						{
							dbobj.executeNonQuery("INSERT INTO TBL_HEADFEEDS (FEEDSUBJECTID,SCRIB,MARKETTYPE,EXPDATE,PRICE,RIGHTS) VALUES ('"+txtsecId.getText().toString()+"','"+txtscrib.getText().toString()+"'"
									+ ",'"+cmbmarkettype.getSelectedItem().toString()+"',null,null,null);");
							JOptionPane.showMessageDialog(headFeed,"Record Saved !!", "Success",JOptionPane.INFORMATION_MESSAGE);	
							if (chksameplayer.isSelected() == true)
							{
								String [] sqlstmts = new String[2];
								sqlstmts[0] = "INSERT INTO TBL_PLAYERS (FEEDSUBJECTID,TRADESUBJECTID,SCRIB,MARKETTYPE,EXPDATE,PRICE,RIGHTS,CONTRACTINFO)"
										+ " VALUES ('"+txtsecId.getText().toString()+"','"+txtsecId.getText().toString()+"','"+txtscrib.getText().toString()+"','"+cmbmarkettype.getSelectedItem().toString()+"',null,null,null,null)";
								sqlstmts[1] = "INSERT INTO TBL_TRADEBOARD (TSCRIB, FEEDSECID, TRADESECID) VALUES ('"+txtscrib.getText()+"', '"+txtsecId.getText()+"', '"+txtsecId.getText()+"')";
								dbobj.executeBatchStatement(sqlstmts);
								JOptionPane.showMessageDialog(headFeed,"Created and Mapped the Player !!", "Success",JOptionPane.INFORMATION_MESSAGE);
							}
						}
						break;
					case "FUTURE":
						if(FUTvalidations())
						{
							String date = txtExpdd.getText()+"-"+txtExpmm.getText()+"-"+txtExpyyyy.getText();
							dbobj.executeNonQuery("INSERT INTO TBL_HEADFEEDS (FEEDSUBJECTID,SCRIB,MARKETTYPE,EXPDATE,PRICE,RIGHTS) VALUES ('"+txtsecId.getText().toString()+"','"+txtscrib.getText().toString()+"'"
									+ ",'"+cmbmarkettype.getSelectedItem().toString()+"','"+date+"',null,null);");
								JOptionPane.showMessageDialog(headFeed,"Record Saved !!", "Success",JOptionPane.INFORMATION_MESSAGE);	
								if (chksameplayer.isSelected() == true)
								{
									String [] sqlstmts = new String[2];
									sqlstmts[0] = "INSERT INTO TBL_PLAYERS (FEEDSUBJECTID,TRADESUBJECTID,SCRIB,MARKETTYPE,EXPDATE,PRICE,RIGHTS,CONTRACTINFO)"
											+ " VALUES ('"+txtsecId.getText().toString()+"','"+txtsecId.getText().toString()+"','"+txtscrib.getText().toString()+"','"+cmbmarkettype.getSelectedItem().toString()+"','"+date+"',null,null,null)";
									sqlstmts[1] = "INSERT INTO TBL_TRADEBOARD (TSCRIB, FEEDSECID, TRADESECID) VALUES ('"+txtscrib.getText()+"', '"+txtsecId.getText()+"', '"+txtsecId.getText()+"')";
									dbobj.executeBatchStatement(sqlstmts);
									JOptionPane.showMessageDialog(headFeed,"Created and Mapped the Player !!", "Success",JOptionPane.INFORMATION_MESSAGE);
								}
						}
						break;
					case "OPTIONS":
						if(OPTvalidations())
						{
							String date = txtExpdd.getText()+"-"+txtExpmm.getText()+"-"+txtExpyyyy.getText();
							dbobj.executeNonQuery("INSERT INTO TBL_HEADFEEDS (FEEDSUBJECTID,SCRIB,MARKETTYPE,EXPDATE,PRICE,RIGHTS) VALUES ('"+txtsecId.getText().toString()+"','"+txtscrib.getText().toString()+"'"
									+ ",'"+cmbmarkettype.getSelectedItem().toString()+"','"+date+"',"+Double.parseDouble(txtprice.getText())+",'"+cmbright.getSelectedItem().toString()+"');");
							JOptionPane.showMessageDialog(headFeed,"Record Saved !!", "Success",JOptionPane.INFORMATION_MESSAGE);	
							if (chksameplayer.isSelected() == true)
							{
								String [] sqlstmts = new String[2];
								sqlstmts[0] = "INSERT INTO TBL_PLAYERS (FEEDSUBJECTID,TRADESUBJECTID,SCRIB,MARKETTYPE,EXPDATE,PRICE,RIGHTS,CONTRACTINFO)"
										+ " VALUES ('"+txtsecId.getText().toString()+"','"+txtsecId.getText().toString()+"','"+txtscrib.getText().toString()+"','"+cmbmarkettype.getSelectedItem().toString()+"','"+date+"',"+Double.parseDouble(txtprice.getText())+",'"+cmbright.getSelectedItem().toString()+"',null);";
								sqlstmts[1] = "INSERT INTO TBL_TRADEBOARD (TSCRIB, FEEDSECID, TRADESECID) VALUES ('"+txtscrib.getText()+"', '"+txtsecId.getText()+"', '"+txtsecId.getText()+"')";
								dbobj.executeBatchStatement(sqlstmts);
								JOptionPane.showMessageDialog(headFeed,"Created and Mapped the Player !!", "Success",JOptionPane.INFORMATION_MESSAGE);
							}
						}
						break;
					case "INDEX":
						if(INDvalidations())
						{
							dbobj.executeNonQuery("INSERT INTO TBL_HEADFEEDS (FEEDSUBJECTID,SCRIB,MARKETTYPE,EXPDATE,PRICE,RIGHTS) VALUES ('"+txtsecId.getText().toString()+"','"+txtscrib.getText().toString()+"'"
									+ ",'"+cmbmarkettype.getSelectedItem().toString()+"',null,null,null);");
								JOptionPane.showMessageDialog(headFeed,"Record Saved !!", "Success",JOptionPane.INFORMATION_MESSAGE);	
								if (chksameplayer.isSelected() == true)
								{
									String [] sqlstmts = new String[2];
									sqlstmts[0] = "INSERT INTO TBL_PLAYERS (FEEDSUBJECTID,TRADESUBJECTID,SCRIB,MARKETTYPE,EXPDATE,PRICE,RIGHTS,CONTRACTINFO)"
											+ " VALUES ('"+txtsecId.getText().toString()+"','"+txtsecId.getText().toString()+"','"+txtscrib.getText().toString()+"','"+cmbmarkettype.getSelectedItem().toString()+"',null,null,null,null)";
									sqlstmts[1] = "INSERT INTO TBL_TRADEBOARD (TSCRIB, FEEDSECID, TRADESECID) VALUES ('"+txtscrib.getText()+"', '"+txtsecId.getText()+"', '"+txtsecId.getText()+"')";
									dbobj.executeBatchStatement(sqlstmts);
									JOptionPane.showMessageDialog(headFeed,"Created and Mapped the Player !!", "Success",JOptionPane.INFORMATION_MESSAGE);
								}
						}
						break;
					}
			}
			else
			{
				//int opcion = JOptionPane.showConfirmDialog(null, "Head feed ID is already exist, Do you want to update ? ('Yes' will update & 'No' will Load existing record)", "Confrimation", JOptionPane.YES_NO_OPTION);
				JOptionPane.showMessageDialog(headFeed,"Head feed ID is already exists", "Success",JOptionPane.INFORMATION_MESSAGE);
//				if (opcion == 0) {
//					JOptionPane.showMessageDialog(headFeed,"Head Feed Updated !! ", "Success",JOptionPane.WARNING_MESSAGE);	
//				} 
//				else {
//					loadExistingData(txtsecId.getText());
//				}
	
			}
			 records = dbobj.getMultiColumnRecords("SELECT FEEDSUBJECTID, SCRIB, MARKETTYPE, EXPDATE, PRICE, RIGHTS FROM TBL_HEADFEEDS;");
	         TableModel newmodel = new DefaultTableModel(records, col);
			 table.setModel(newmodel);
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
		//records = dbobj.getMultiColumnRecords("SELECT FEEDSUBJECTID, SCRIB, MARKETTYPE, EXPDATE, PRICE, RIGHTS FROM TBL_HEADFEEDS;");
        //TableModel newmodel = new DefaultTableModel(records, col);
		//table.setModel(newmodel);
		btnSave.setEnabled(true);
		txtscrib.setText("");
		cmbmarkettype.setSelectedIndex(0);
		txtExpdd.setText("DD");
		txtExpmm.setText("MM");
		txtExpyyyy.setText("YYYY");
		txtprice.setText("0.0");
		txtsecId.setText("");
		chksameplayer.setSelected(false);
		chksameplayer.setEnabled(true);
		cmbright.setSelectedIndex(0);
		table.clearSelection();
		
	}
}
