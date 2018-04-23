package com.tradebot.ui.forms;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;

import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
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
import java.io.File;
import java.awt.event.ActionEvent;
import org.pmw.tinylog.*;
import com.tradebot.dbcommons.db_commons;
import com.tradebot.dbcommons.tradebot_utility;
import com.tradebot.presto.presto_commons;

import javax.swing.border.LineBorder;
import javax.swing.JCheckBox;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

public class HeadFeeds {

	private JFrame headFeed;
	private JTextField txtscrib;
	private JTextField txtExpmm;
	private JTextField txtExpyyyy;
	private JPanel innerpanel;
	private JLabel lblScrib,lblDate,lblRight,lblHead;
	private JComboBox<?> cmbsegment,cmbright, cmbexchange ; 
	JComboBox<String> cmbinstrument;
	private JButton btnDelete;
	private JCheckBox chksameplayer;
	private JTable table;
	String records[][];
	String col[]= {"FEED-ID","SYMBOL","EXCHANGE","INSTRUMENTS","LOT-SIZE","TICK-SIZE","EXPIRY-DD","EXPIRY-MMMYY","OPT-TYPE","STRIKE"};
	
	private String configprop=System.getProperty("user.dir")+File.separator+"resource"+File.separator+"config.properties";
	String tradelogpath;
	tradebot_utility utils = new tradebot_utility(); 
	db_commons dbobj=new db_commons();
	
	private int colid=0,colsecid=1,colscrib=2,colmtype=3,colexpdate=4,colprice=5,colrights=6, colinfo =7;
	private JLabel lblMarket;
	private JScrollBar scrollBar;
	presto_commons objPresto;

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
	
	/**
	 * Create the application.
	 */
	public HeadFeeds(presto_commons Presto) 
	{
		if (Presto !=null)
		{
			objPresto = Presto;
		}
		else
		{
			objPresto = new presto_commons();
		}
		tradelogpath = utils.configlogfile("TRADEBOT_LOG");
		initialize();
	}
	/**
	 * set proper control visibility according to the market type selection
	 */
	public void builtFUTControls()
	{
		try
		{
			lblDate.setVisible(false);
			txtExpmm.setVisible(false);
			txtExpyyyy.setVisible(false);
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
			
			lblDate.setVisible(true);
			txtExpmm.setVisible(true);
			txtExpyyyy.setVisible(true);
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
			lblDate.setVisible(false);
			txtExpmm.setVisible(false);
			txtExpyyyy.setVisible(false);
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
	public void builtINDControls()
	{
		try
		{
			lblDate.setVisible(false);
			txtExpmm.setVisible(false);
			txtExpyyyy.setVisible(false);
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
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() 
	{
		
		headFeed = new JFrame();
		headFeed.setTitle("HEAD FEED DETAILS");
		headFeed.setBounds(100, 100, 1039, 662);
		headFeed.getContentPane().setLayout(null);
		headFeed.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		headFeed.getContentPane().setBackground(new Color(51, 51, 51));
		headFeed.setVisible(true);
		
		innerpanel = new JPanel();
		innerpanel.setBounds(10, 53, 1003, 572);
		innerpanel.setBackground(new Color(80,75,78));
		headFeed.getContentPane().add(innerpanel);
		innerpanel.setLayout(null);
		
		txtscrib = new JTextField();
		txtscrib.setBounds(133, 107, 169, 31);
		innerpanel.add(txtscrib);
		txtscrib.setHorizontalAlignment(SwingConstants.LEFT);
		txtscrib.setForeground(new Color(255, 220, 135));
		txtscrib.setFont(new Font("Verdana", Font.PLAIN, 20));
		txtscrib.setColumns(10);
		txtscrib.setCaretColor(Color.WHITE);
		txtscrib.setBackground(new Color(36, 34, 29));
		txtscrib.addKeyListener(new KeyAdapter() {

			  public void keyTyped(KeyEvent e) {
			    char keyChar = e.getKeyChar();
			    if (Character.isLowerCase(keyChar)) {
			      e.setKeyChar(Character.toUpperCase(keyChar));
			    }
			  }

			});
		
		lblScrib = new JLabel("SCRIB");
		lblScrib.setBounds(28, 100, 82, 49);
		innerpanel.add(lblScrib);
		lblScrib.setHorizontalAlignment(SwingConstants.LEFT);
		lblScrib.setForeground(Color.WHITE);
		lblScrib.setFont(new Font("Verdana", Font.PLAIN, 16));
		cmbsegment = new JComboBox(new DefaultComboBoxModel(new String[] {"--Select--", "CM", "FO"}));
		cmbsegment.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Logger.info("Market type set to --> "+cmbsegment.getSelectedItem().toString());
			
				switch (cmbsegment.getSelectedItem().toString()) {
				case "CM":
					String [] CMValues = new String[] {"--Select--","Equities"};
					DefaultComboBoxModel cmmodel = new DefaultComboBoxModel(CMValues);
					cmbinstrument.setModel( cmmodel );
					builtSTKControls();
					break;
				case "FO":
					String [] FOValues = new String[] {"--Select--","FUTIDX","FUTSTK","OPTIDX","OPTSTK","FUTCOM"};
					DefaultComboBoxModel fomodel = new DefaultComboBoxModel(FOValues);
					cmbinstrument.setModel( fomodel );
					builtFUTControls();
					break;
				default:
					builtSTKControls();
					break;
				}
			}
		});
		cmbsegment.setFont(new Font("Verdana", Font.PLAIN, 18));
		cmbsegment.setBounds(468, 48, 178, 31);
		
		innerpanel.add(cmbsegment);
		
		lblMarket = new JLabel("SEGMENT");
		lblMarket.setHorizontalAlignment(SwingConstants.LEFT);
		lblMarket.setForeground(Color.WHITE);
		lblMarket.setFont(new Font("Verdana", Font.PLAIN, 16));
		lblMarket.setBounds(352, 40, 116, 49);
		innerpanel.add(lblMarket);
		
		chksameplayer = new JCheckBox("Trade On Same Player");
		chksameplayer.setBounds(396, 7, 214, 23);
		chksameplayer.setForeground(Color.WHITE);
		chksameplayer.setBackground(new Color(80,75,58));
		chksameplayer.setFont(new Font("Verdana", Font.PLAIN, 16));
		innerpanel.add(chksameplayer);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 216, 983, 20);
		innerpanel.add(separator);
		
		records =dbobj.getMultiColumnRecords("SELECT FEEDSECID,SYMBOL,EXCHANGE,INSTTYPE,LOTSIZE,TICKSIZE,EXPIRYDD,EXPIRYMMMYY,OPTTYPE,STRIKEPRICE FROM TBL_HEAD ;");
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
		table.setBackground(new Color(51, 51, 51));
		table.setFillsViewportHeight(true);
		table.setModel(model);	
		JTableHeader header = table.getTableHeader();
		header.setForeground(new Color(36,34,29));
	    header.setFont(new Font("Tahoma", Font.BOLD, 13));
	    JScrollPane scrollPane = new JScrollPane(table);
	    scrollPane.setBounds(10, 237, 983, 318);
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
		btnclear.setBounds(131, 168, 279, 37);
		innerpanel.add(btnclear);
		
		cmbexchange = new JComboBox(new DefaultComboBoxModel(new String[] {"--Select--", "NSEFO", "NSECM", "NSECD", "MCX"}));
		cmbexchange.setFont(new Font("Verdana", Font.PLAIN, 18));
		cmbexchange.setBounds(132, 48, 170, 31);
		innerpanel.add(cmbexchange);
		cmbinstrument = new JComboBox<String>();
		cmbinstrument.setFont(new Font("Verdana", Font.PLAIN, 18));
		cmbinstrument.setBounds(815, 48, 178, 31);
		innerpanel.add(cmbinstrument);
		
		cmbinstrument.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Logger.info("Market type set to --> "+cmbinstrument.getSelectedItem().toString());
				switch (cmbinstrument.getSelectedItem().toString()) {
				case "FUTIDX":
				case "FUTSTK":
					builtFUTControls();
					break;
				case "OPTIDX":
				case "OPTSTK":
					builtOPTControls();
					break;
				case "Equities":
					builtSTKControls();
					break;
				case "FUTCOM":
					builtFUTControls();
					break;
				default:
					builtSTKControls();
					break;
				}
			}
		});
		
		
		JLabel lblExchange = new JLabel("EXCHANGE");
		lblExchange.setHorizontalAlignment(SwingConstants.LEFT);
		lblExchange.setForeground(Color.WHITE);
		lblExchange.setFont(new Font("Verdana", Font.PLAIN, 16));
		lblExchange.setBounds(28, 40, 116, 49);lblExchange.setVisible(true);
		
		innerpanel.add(lblExchange);
		
		JLabel lblInsttype = new JLabel("INST-TYPE");
		lblInsttype.setHorizontalAlignment(SwingConstants.LEFT);
		lblInsttype.setForeground(Color.WHITE);
		lblInsttype.setFont(new Font("Verdana", Font.PLAIN, 16));
		lblInsttype.setBounds(687, 40, 116, 49);
		innerpanel.add(lblInsttype);
		
		lblDate = new JLabel("DATE");
		lblDate.setBounds(352, 111, 64, 26);
		innerpanel.add(lblDate);
		lblDate.setHorizontalAlignment(SwingConstants.LEFT);
		lblDate.setForeground(Color.WHITE);
		lblDate.setFont(new Font("Verdana", Font.PLAIN, 16));
		
		txtExpmm = new JTextField();
		txtExpmm.setBounds(470, 110, 93, 35);
		innerpanel.add(txtExpmm);
		txtExpmm.setText("MMM");
		txtExpmm.setPreferredSize(new Dimension(60, 50));
		txtExpmm.setHorizontalAlignment(SwingConstants.CENTER);
		txtExpmm.setForeground(new Color(255, 220, 135));
		txtExpmm.setColumns(4);
		txtExpmm.setCaretColor(Color.WHITE);
		txtExpmm.setBackground(new Color(36, 34, 29));
		txtExpmm.addKeyListener(new KeyAdapter() {

			  public void keyTyped(KeyEvent e) {
			    char keyChar = e.getKeyChar();
			    if (Character.isLowerCase(keyChar)) {
			      e.setKeyChar(Character.toUpperCase(keyChar));
			    }
			  }

			});
		
		txtExpyyyy = new JTextField();
		txtExpyyyy.setBounds(582, 110, 64, 35);
		innerpanel.add(txtExpyyyy);
		txtExpyyyy.setText("YY");
		txtExpyyyy.setPreferredSize(new Dimension(80, 50));
		txtExpyyyy.setHorizontalAlignment(SwingConstants.CENTER);
		txtExpyyyy.setForeground(new Color(255, 220, 135));
		txtExpyyyy.setColumns(2);
		txtExpyyyy.setCaretColor(Color.WHITE);
		txtExpyyyy.setBackground(new Color(36, 34, 29));
		
		lblRight = new JLabel("RIGHT");
		lblRight.setBounds(688, 111, 70, 26);
		innerpanel.add(lblRight);
		lblRight.setHorizontalAlignment(SwingConstants.LEFT);
		lblRight.setForeground(Color.WHITE);
		lblRight.setFont(new Font("Verdana", Font.PLAIN, 16));
		
		cmbright = new JComboBox();
		cmbright.setBounds(805, 105, 188, 37);
		innerpanel.add(cmbright);
		cmbright.setModel(new DefaultComboBoxModel(new String[] {"--Select--", "PE", "CE"}));
		cmbright.setFont(new Font("Verdana", Font.PLAIN, 18));
		
		JButton btnVerfiy = new JButton("Validate");
		btnVerfiy.setBounds(581, 168, 279, 37);
		innerpanel.add(btnVerfiy);
		
		
		lblHead = new JLabel(" HEAD FEED ");
		lblHead.setBounds(10, -1, 1016, 43);
		headFeed.getContentPane().add(lblHead);
		lblHead.setHorizontalAlignment(SwingConstants.CENTER);
		lblHead.setForeground(new Color(255, 220, 135));
		lblHead.setFont(new Font("Verdana", Font.BOLD, 22));
		btnVerfiy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				try
				{
					
					switch (cmbinstrument.getSelectedItem().toString()) {
					case "FUTIDX":
					case "FUTSTK":
						if (FUTvalidations())
						{
							String [][] MatchSecurities = objPresto.getMatchedScrib_CM_FUT(cmbexchange.getSelectedItem().toString().trim(), 
									txtscrib.getText().trim(), cmbsegment.getSelectedItem().toString().trim(), cmbinstrument.getSelectedItem().toString().trim());
							showMatchedSecIds(MatchSecurities);		
						}
						break;
					case "OPTIDX":
					case "OPTSTK":
						if (OPTvalidations())
						{
							String exdate = txtExpmm.getText().trim()+txtExpyyyy.getText().trim();
							String [][] MatchSecurities = objPresto.getMatchedScrib_OPT(cmbexchange.getSelectedItem().toString().trim(), txtscrib.getText().trim(), exdate, cmbright.getSelectedItem().toString());
							showMatchedSecIds(MatchSecurities);
						}
						break;
					case "Equities":
						if (STKvalidations())
						{
							String [][] MatchSecurities = objPresto.getMatchedScrib_CM_FUT(cmbexchange.getSelectedItem().toString().trim(), 
									txtscrib.getText().trim(), cmbsegment.getSelectedItem().toString().trim(), cmbinstrument.getSelectedItem().toString().trim());
							showMatchedSecIds(MatchSecurities);					
							
						}
						break;
					case "FUTCOM":
						if (FUTvalidations())
						{
							
						}
						break;
					}
				}
				catch(Exception ex)
				{
					Logger.error(ex);
				}
			}
		});
		txtExpyyyy.addFocusListener(new FocusListener() {
		    public void focusGained(FocusEvent e) {
		    	txtExpyyyy.setText("");
		    }
		    public void focusLost(FocusEvent e) {
		    	    if(txtExpyyyy.getText().equals(""))
		    	    {
		    	    	txtExpyyyy.setText("YY");
		    	    }
		    }
		});
		txtExpmm.addFocusListener(new FocusListener() {
		    public void focusGained(FocusEvent e) {
		    	txtExpmm.setText("");
		    }

		    public void focusLost(FocusEvent e) {
		    	    if(txtExpmm.getText().equals(""))
		    	    {
		    	    	txtExpmm.setText("MMM");
		    	    }
		    }
		});
//		table.addMouseListener(new MouseAdapter(){
//		     public void mouseClicked(MouseEvent e){
//		      if (e.getClickCount() == 2){
//		         System.out.println(" double click" );
//		         loadExistingData(table.getValueAt(table.getSelectedRow(), 0).toString());
//		        
//		         }
//		      }
//		     } );
		table.addKeyListener(new KeyAdapter() {
		      @Override
		      public void keyPressed(KeyEvent e){
		        if (e.isControlDown() && e.getKeyCode() == 68)
		        {
		        	int opcion = JOptionPane.showConfirmDialog(null, "Are you sure ?", "Delete Player", JOptionPane.YES_NO_OPTION);
					if (opcion == 0) 
					{ 
				          dbobj.executeNonQuery("DELETE FROM TBL_HEAD WHERE FEEDSECID ='"+table.getValueAt(table.getSelectedRow(), 0)+"'");
				          dbobj.executeNonQuery("DELETE FROM TBL_TRADERS WHERE FEEDSECID ='"+table.getValueAt(table.getSelectedRow(), 0)+"'");
				          dbobj.executeNonQuery("DELETE FROM TBL_TRADEBOARD WHERE FEEDSECID='"+table.getValueAt(table.getSelectedRow(), 0)+"'");
				          resetfields();
				          JOptionPane.showMessageDialog(headFeed,"Head Feed Deleted & Corrsponding Player Got Removed!!", "Success",JOptionPane.WARNING_MESSAGE);	
				          records=null;
						  records = dbobj.getMultiColumnRecords("SELECT FEEDSECID,SYMBOL,EXCHANGE,INSTTYPE,LOTSIZE,TICKSIZE,EXPIRYDD,EXPIRYMMMYY,OPTTYPE,STRIKEPRICE FROM TBL_HEAD ;");
				          TableModel newmodel = new DefaultTableModel(records, col);
						  table.setModel(newmodel);
						  table.clearSelection();
					}
		        }
		        }});
		headFeed.setVisible(true);
		builtSTKControls();
	}
	

	public Boolean STKvalidations()
	{
		Boolean Isvalid=false;
		
		if ((cmbexchange.getSelectedIndex() !=0 )||(cmbsegment.getSelectedIndex() !=0 )||(cmbinstrument.getSelectedIndex() !=0 ) || (!txtscrib.getText().equals("")))
		{
			Isvalid=true;
		}
		else
		{
			JOptionPane.showMessageDialog(headFeed,"Invalid Data, Check Input", "Error",JOptionPane.ERROR_MESSAGE);	
		}
		
		return Isvalid;
	}
	public Boolean FUTvalidations()
	{
		Boolean Isvalid=false;
		if ((cmbexchange.getSelectedIndex() !=0 )||(cmbsegment.getSelectedIndex() !=0 )||(cmbinstrument.getSelectedIndex() !=0 ) || (!txtscrib.getText().equals("")) )
		{
			Isvalid=true;
		}
		else
		{
			JOptionPane.showMessageDialog(headFeed,"Invalid Data, Check Input", "Error",JOptionPane.ERROR_MESSAGE);	
		}
		return Isvalid;
	}
	public Boolean OPTvalidations()
	{
		Boolean Isvalid=false;
		if ((cmbexchange.getSelectedIndex() !=0 )||(cmbsegment.getSelectedIndex() !=0 )||(cmbinstrument.getSelectedIndex() !=0 ) || (!txtscrib.getText().equals("")) || (txtExpmm.getText() != "MMM") || (txtExpyyyy.getText() != "YY") || (cmbright.getSelectedIndex() !=0))
		{
			Isvalid=true;
		}
		else
		{
			JOptionPane.showMessageDialog(headFeed,"Invalid Data, Check Input", "Error",JOptionPane.ERROR_MESSAGE);	
		}
		return Isvalid;
	}
	public Boolean INDvalidations()
	{
		Boolean Isvalid=false;
		if ((cmbexchange.getSelectedIndex() !=0 )||(cmbsegment.getSelectedIndex() !=0 )||(cmbinstrument.getSelectedIndex() !=0 ) || (!txtscrib.getText().equals("")))
		{
			Isvalid=true;
		}
		else
		{
			JOptionPane.showMessageDialog(headFeed,"Invalid Data, Check Input", "Error",JOptionPane.ERROR_MESSAGE);	
			
		}
		return Isvalid;
	}
	
	public void showMatchedSecIds(String [][] Secs)
	{
		try
		{	
				if (Secs.length !=0)
				{
					JPanel secPanel = new JPanel();
					innerpanel.setVisible(false);
					secPanel.setVisible(true);
					secPanel.setBackground(new Color(51, 51, 51));
					secPanel.setBounds(10, 53, 1003, 800);
					headFeed.getContentPane().add(secPanel, BorderLayout.CENTER);
					
					JTable sectable;
					
					TableModel model = new DefaultTableModel(Secs, col);
					//TableModel model = null;
					sectable = new JTable(model){
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
					sectable.setBorder(new LineBorder(new Color(51, 51, 51)));
					sectable.setBackground(new Color(51, 51, 51));
					sectable.setSurrendersFocusOnKeystroke(true);
					sectable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
					sectable.setFillsViewportHeight(true);
					sectable.setFont(new Font("Tahoma", Font.PLAIN, 15));
					sectable.setModel(model);	
					
					JTableHeader header = sectable.getTableHeader();
					header.setForeground(new Color(36,34,29));
				    header.setFont(new Font("Tahoma", Font.BOLD, 13));
				    secPanel.setLayout(null);
				    JScrollPane scrollPane = new JScrollPane(sectable);
				    scrollPane.setBounds(10, 45, 980, 400);	    
				    scrollPane.setEnabled(false);
					scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
					scrollPane.setViewportBorder(null);
					scrollPane.setBorder(null);
					
					
					secPanel.add(scrollPane);
					
					JLabel lblMatchedSeciss = new JLabel("Matched Securities");
					lblMatchedSeciss.setHorizontalAlignment(SwingConstants.CENTER);
					lblMatchedSeciss.setForeground(new Color(255, 220, 135));
					lblMatchedSeciss.setFont(new Font("Verdana", Font.BOLD, 22));
					lblMatchedSeciss.setBounds(20, 11, 950, 23);
					secPanel.add(lblMatchedSeciss);
					
					JButton btnCancel = new JButton("Cancel");
					btnCancel.setBounds(350, 475, 89, 23);
					secPanel.add(btnCancel);
					btnCancel.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) 
						{
							try 
							{
								innerpanel.setVisible(true);
								secPanel.setVisible(false);
								records =dbobj.getMultiColumnRecords("SELECT FEEDSECID,SYMBOL,EXCHANGE,INSTTYPE,LOTSIZE,TICKSIZE,EXPIRYDD,EXPIRYMMMYY,OPTTYPE,STRIKEPRICE FROM TBL_HEAD ;");
								TableModel model = new DefaultTableModel(records, col);
								table.setModel(model);
							}
							catch(Exception ex)
							{
							}
							finally
							{
								
							}
						
						}
						});
					
					JButton btnSave = new JButton("Save");
					btnSave.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) 
						{
							try
							{
							
								if (sectable.getSelectedRowCount() != 0)
								{
									
										dbobj.executeNonQuery("INSERT INTO TBL_HEAD (FEEDSECID,SYMBOL,EXCHANGE,INSTTYPE,LOTSIZE,TICKSIZE,EXPIRYDD,EXPIRYMMMYY,OPTTYPE,STRIKEPRICE) VALUES ('"+Secs[0][0]+"','"+Secs[0][1]+"','"+Secs[0][2]+"','"+Secs[0][3]+"','"+Secs[0][4]+"','"+Secs[0][5]+"','"+Secs[0][6]+"','"+Secs[0][7]+"','"+Secs[0][8]+"','"+Secs[0][9]+"');");
										innerpanel.setVisible(true);
										if (chksameplayer.isSelected())
										{
											dbobj.executeNonQuery("INSERT INTO TBL_TRADERS (FEEDSECID,TRADESECID,SYMBOL,EXCHANGE,INSTTYPE,LOTSIZE,TICKSIZE,EXPIRYDD,EXPIRYMMMYY,OPTTYPE,STRIKEPRICE) VALUES ('"+Secs[0][0]+"','"+Secs[0][0]+"','"+Secs[0][1]+"','"+Secs[0][2]+"','"+Secs[0][3]+"','"+Secs[0][4]+"','"+Secs[0][5]+"','"+Secs[0][6]+"','"+Secs[0][7]+"','"+Secs[0][8]+"','"+Secs[0][9]+"');");
											dbobj.executeNonQuery("INSERT INTO TBL_TRADEBOARD (TSCRIB,FEEDSECID,TRADESECID) VALUES ('"+Secs[0][1]+"','"+Secs[0][0]+"','"+Secs[0][0]+"');");
											JOptionPane.showMessageDialog(headFeed,"Created Head & Associate player !!", "Info",JOptionPane.INFORMATION_MESSAGE);	
											
										}
										else
										{
											JOptionPane.showMessageDialog(headFeed,"Created Head feed !!", "Info",JOptionPane.INFORMATION_MESSAGE);	
										}
										secPanel.setVisible(false);
										
										records =dbobj.getMultiColumnRecords("SELECT FEEDSECID,SYMBOL,EXCHANGE,INSTTYPE,LOTSIZE,TICKSIZE,EXPIRYDD,EXPIRYMMMYY,OPTTYPE,STRIKEPRICE FROM TBL_HEAD ;");
										TableModel model = new DefaultTableModel(records, col);
										table.setModel(model);
								}
								else
								{
									JOptionPane.showMessageDialog(headFeed,"No Security Selected !! Atleast select one items !!", "Error",JOptionPane.ERROR_MESSAGE);	
								}
								
							}
							catch(Exception ex)
							{
							}
							finally
							{
							}
						}
					});
					btnSave.setBounds(550, 475, 89, 23);
					secPanel.add(btnSave);
			}
			else
			{
				JOptionPane.showMessageDialog(headFeed,"No Mathched Security Find !! Please Recheck the input !!", "Error",JOptionPane.ERROR_MESSAGE);	
			}
			
		}
		catch(Exception ex)
		{
			
		}
		finally
		{
			
		}
	}
	
	public void resetfields()
	{
		
		txtscrib.setText("");
		cmbsegment.setSelectedIndex(0);
		cmbexchange.setSelectedIndex(0);
		//cmbinstrument.setSelectedIndex(0);
		txtExpmm.setText("MMM");
		txtExpyyyy.setText("YY");
		chksameplayer.setSelected(false);
		chksameplayer.setEnabled(true);
		cmbright.setSelectedIndex(0);
		table.clearSelection();
		
	}
}
