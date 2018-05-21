package com.tradebot.ui.forms;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import com.tradebot.dbcommons.db_commons;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SecIDSelector {

	private JFrame SecFrame;
	private JTable sectable;
	db_commons dbobj=new db_commons();
	String records[][];
	String col[]= {"SEC-ID","SYMBOL","LOT-SIZE","TICK-SIZE","EXCHANGE","INSTRUMENTS","EXPIRY-DD","EXPIRY-MMMYY","OPT-TYPE","STRIKE"};

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SecIDSelector window = new SecIDSelector(null,null);
					window.SecFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SecIDSelector(String [][] Secs, String tablename) 
	{
		initialize(Secs, tablename);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(String [][] Secs, String tablename) 
	{
		SecFrame = new JFrame();
		SecFrame.setBounds(100, 100, 927, 369);
		SecFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SecFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		SecFrame.getContentPane().setBackground(new Color(51, 51, 51));
		
		JPanel secPanel = new JPanel();
		secPanel.setBackground(new Color(51, 51, 51));
		SecFrame.getContentPane().add(secPanel, BorderLayout.CENTER);
		SecFrame.setVisible(true);
		
		
		
		records = new String[][] {{"siva","siva","siva","siva","siva","siva","siva","siva","siva","siva"},{"anand","anand","anand","anand","anand","anand","anand","anand","anand","anand"}};
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
	    scrollPane.setBounds(10, 45, 891, 225);	    
	    scrollPane.setEnabled(false);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setViewportBorder(null);
		scrollPane.setBorder(null);
		
		
		secPanel.add(scrollPane);
		
		JLabel lblMatchedSeciss = new JLabel("Matched Securities");
		lblMatchedSeciss.setHorizontalAlignment(SwingConstants.CENTER);
		lblMatchedSeciss.setForeground(new Color(255, 220, 135));
		lblMatchedSeciss.setFont(new Font("Verdana", Font.BOLD, 22));
		lblMatchedSeciss.setBounds(20, 11, 881, 23);
		secPanel.add(lblMatchedSeciss);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(229, 297, 89, 23);
		secPanel.add(btnCancel);
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) 
			{
				try
				{
					
//					if (sectable.getSelectedRowCount() != 0)
//					{
//						if (tablename =="HEAD")
//						{
//							dbobj.executeNonQuery("INSERT INTO TBL_HEAD (FEEDSECID,SYMBOL,LOTSIZE,TICKSIZE,EXCHANGE,INSTTYPE,EXPIRYDD,EXPIRYMMMYY,OPTTYPE,STRIKEPRICE) VALUES ('"+Secs[0][0]+"','"+Secs[0][1]+"','"+Secs[0][2]+"','"+Secs[0][3]+"','"+Secs[0][4]+"','"+Secs[0][5]+"','"+Secs[0][6]+"','"+Secs[0][7]+"','"+Secs[0][8]+"','"+Secs[0][9]+"');");
//						}
//						else if(tablename =="PLAYER") 
//						{
//							dbobj.executeNonQuery("INSERT INTO TBL_PLAYERS (FEEDSECID,PLAYERSECID,SYMBOL,LOTSIZE,TICKSIZE,EXCHANGE,INSTTYPE,EXPIRYDD,EXPIRYMMMYY,OPTTYPE,STRIKEPRICE) VALUES ()");
//						}
//					}
//					else
//					{
//						JOptionPane.showMessageDialog(SecFrame,"No Security Selected !! Atleast select one items !!", "Error",JOptionPane.ERROR_MESSAGE);	
//					}
//					HeadFeeds h = new HeadFeeds(null, Secs[0][0]);
				}
				catch(Exception ex)
				{
				}
				finally
				{
				}
			}
		});
		btnSave.setBounds(600, 297, 89, 23);
		secPanel.add(btnSave);
		
		
		
	}
}
