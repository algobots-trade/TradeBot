package com.tradebot.ui.forms;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Dialog.ModalExclusionType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import java.awt.Color;
import org.pmw.tinylog.*;
import org.pmw.tinylog.writers.FileWriter;

import com.tradebot.dbcommons.tradebot_utility;

public class LogIn {

	private JFrame LoginFrame;
	private JPasswordField passwordField;
	private String logopath;
	String tradelogpath;
	tradebot_utility utils = new tradebot_utility(); 
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LogIn window = new LogIn();
					window.LoginFrame.setVisible(true);
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
			logopath = System.getProperty("user.dir") + utils.readconfigprop("LOGO_PATH").replace("/", File.separator);
			utils.configlogfile("TRADEBOT_LOG");
			Logger.info("Log In Window Triggered");
		}
		catch(Exception ex)
		{
			
		}
		finally 
		{
			
		}
		
	}
	/**
	 * Create the application.
	 */
	public LogIn() {
		try
		{
			initialLoad();
			initialize();
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
		logindesign();
		Logger.info("Log-In Window Loaded");
	}
	
	private void logindesign()
	{
		try
		{
			Logger.info("Inizilized Log-In Form"); 
			//Log -in Main Frame Design
			LoginFrame = new JFrame();
			LoginFrame.setTitle("TradeBot - Login");
			LoginFrame.setResizable(false);
			LoginFrame.setBackground(Color.BLACK);
			LoginFrame.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
			LoginFrame.getContentPane().setBackground(Color.BLACK);
			LoginFrame.setBounds(100, 100, 558, 298);
			LoginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			LoginFrame.getContentPane().setLayout(null);
			
			// Inner Componnet Desings
			
			JLabel lblNewLabel = new JLabel("New label");
			lblNewLabel.setIcon(new ImageIcon(logopath));
			lblNewLabel.setBounds(18, 52, 204, 171);
			LoginFrame.getContentPane().add(lblNewLabel);
			
			JLabel lblCopyrightInteractiveBrokers = new JLabel("©  2017 Copyright Trade Bots - Beta V.0.1");
			lblCopyrightInteractiveBrokers.setFont(new Font("Verdana", Font.PLAIN, 11));
			lblCopyrightInteractiveBrokers.setForeground(Color.WHITE);
			lblCopyrightInteractiveBrokers.setBounds(147, 247, 294, 16);
			LoginFrame.getContentPane().add(lblCopyrightInteractiveBrokers);
			
			JLabel lblOvviResearchBot = new JLabel("Trade Bot");
			lblOvviResearchBot.setHorizontalAlignment(SwingConstants.LEFT);
			lblOvviResearchBot.setFont(new Font("Tahoma", Font.PLAIN, 16));
			lblOvviResearchBot.setForeground(Color.WHITE);
			lblOvviResearchBot.setBounds(21, 6, 269, 40);
			LoginFrame.getContentPane().add(lblOvviResearchBot);
			
			JPanel panel = new JPanel();
			panel.setBorder(null);
			panel.setBackground(Color.DARK_GRAY);
			panel.setBounds(285, 58, 238, 165);
			LoginFrame.getContentPane().add(panel);
			panel.setLayout(null);
			
			passwordField = new JPasswordField();
			passwordField.setForeground(Color.GREEN);
			passwordField.setBorder(javax.swing.BorderFactory.createEmptyBorder());
			passwordField.setBackground(Color.DARK_GRAY);
			passwordField.setBounds(21, 41, 199, 33);
			passwordField.setCaretColor(Color.WHITE);
			panel.add(passwordField);
			passwordField.addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent e) 
				{
					if (e.getKeyChar()==KeyEvent.VK_ENTER)
					{
						validateLogin(new String(passwordField.getPassword()));
					}		
				}
			});
			passwordField.setToolTipText("Enter Piasscode & hit Enter");
			passwordField.setEchoChar('*');
			passwordField.setFont(new Font("Verdana", Font.BOLD, 20));
			
			JLabel lblPassCode = new JLabel("Enter The Passcode");
			lblPassCode.setBounds(21, 0, 184, 40);
			panel.add(lblPassCode);
			lblPassCode.setFont(new Font("Tahoma", Font.PLAIN, 16));
			lblPassCode.setForeground(Color.WHITE);
			
			JSeparator separator = new JSeparator();
			separator.setBounds(21, 75, 211, 16);
			panel.add(separator);
			separator.setForeground(Color.WHITE);
			
			JButton btnGetIn = new JButton("Get In");
			btnGetIn.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) 
				{
					try
					{
						validateLogin(new String(passwordField.getPassword()));
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
			btnGetIn.setFont(new Font("Tahoma", Font.PLAIN, 13));
			btnGetIn.setBackground(Color.BLACK);
			btnGetIn.setBounds(69, 98, 98, 40);
			panel.add(btnGetIn);
			
		}
		catch(Exception ex)
		{
			Logger.error(ex.toString());
		}
		finally
		{
			 
		}
		
	}
	
	private void validateLogin(String passcode)
	{
		try
		{
			com.tradebot.dbcommons.db_commons dbObj=new com.tradebot.dbcommons.db_commons();
			int count = dbObj.getRowCount(null,"select * from tbl_passcode where passcode='"+passcode.toString()+"'");
			if (count == 1)
			{
				Logger.info("Logged in Sucessfully !!");
				LoginFrame.dispose();
				TradeBoard tbd=new TradeBoard();
			}
			else
			{
				Logger.warn("Wrong Passcode Attempt.. with passcode --> " + passcode.toString());
			    JOptionPane.showMessageDialog(LoginFrame,"Invalid Passcode !!", "Authentication Violation",JOptionPane.WARNING_MESSAGE);
			}	
		}
		catch(Exception Ex)
		{
			Logger.error(Ex);
		}
		finally 
		{
			
		}
	}

}
