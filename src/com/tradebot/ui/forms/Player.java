package com.tradebot.ui.forms;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.MatteBorder;
import javax.swing.border.LineBorder;

public class Player {

	private JFrame playerframe;
	private JTextField txtscrib;
	private JTextField txtExpdd;
	private JTextField txtExpmm;
	private JTextField txtExpyyyy;
	private JTextField txtprice;

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
		initialize();
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
		innerpanel.setBounds(26, 61, 613, 299);
		innerpanel.setBackground(new Color(80,75,78));
		playerframe.getContentPane().add(innerpanel);
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
		
		JLabel lblScrib = new JLabel("SCRIB");
		lblScrib.setBounds(16, 6, 104, 49);
		innerpanel.add(lblScrib);
		lblScrib.setHorizontalAlignment(SwingConstants.LEFT);
		lblScrib.setForeground(Color.WHITE);
		lblScrib.setFont(new Font("Verdana", Font.PLAIN, 20));
		
		JComboBox cmbmarkettype = new JComboBox();
		cmbmarkettype.setModel(new DefaultComboBoxModel(new String[] {"—SELECT—", "STOCK", "FUTURE", "OPTIONS", "INDEX"}));
		cmbmarkettype.setFont(new Font("Verdana", Font.PLAIN, 18));
		cmbmarkettype.setBounds(354, 6, 235, 49);
		//cmbmarkettype.addItem("STOCK");
		//cmbmarkettype.addItem("FUTURE");
		//cmbmarkettype.addItem("OPTIONS");
		//cmbmarkettype.addItem("INDEX");
		
		
		innerpanel.add(cmbmarkettype);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.DARK_GRAY);
		panel.setBorder(new MatteBorder(1, 1, 1, 1, (Color) Color.RED));
		panel.setBounds(16, 67, 573, 89);
		innerpanel.add(panel);
		panel.setLayout(null);
		
		JLabel lblDate = new JLabel("DATE");
		lblDate.setBounds(78, 6, 64, 26);
		lblDate.setHorizontalAlignment(SwingConstants.LEFT);
		lblDate.setForeground(Color.WHITE);
		lblDate.setFont(new Font("Verdana", Font.PLAIN, 20));
		panel.add(lblDate);
		
		txtExpdd = new JTextField();
		txtExpdd.setText("DD");
		txtExpdd.setPreferredSize(new Dimension(60, 50));
		txtExpdd.setHorizontalAlignment(SwingConstants.CENTER);
		txtExpdd.setForeground(Color.WHITE);
		txtExpdd.setColumns(4);
		txtExpdd.setCaretColor(Color.WHITE);
		txtExpdd.setBackground(new Color(80, 75, 78));
		txtExpdd.setBounds(17, 32, 49, 43);
		panel.add(txtExpdd);
		
		txtExpmm = new JTextField();
		txtExpmm.setText("MM");
		txtExpmm.setPreferredSize(new Dimension(60, 50));
		txtExpmm.setHorizontalAlignment(SwingConstants.CENTER);
		txtExpmm.setForeground(Color.WHITE);
		txtExpmm.setColumns(4);
		txtExpmm.setCaretColor(Color.WHITE);
		txtExpmm.setBackground(new Color(80, 75, 78));
		txtExpmm.setBounds(69, 32, 49, 43);
		panel.add(txtExpmm);
		
		txtExpyyyy = new JTextField();
		txtExpyyyy.setText("YYYY");
		txtExpyyyy.setPreferredSize(new Dimension(80, 50));
		txtExpyyyy.setHorizontalAlignment(SwingConstants.CENTER);
		txtExpyyyy.setForeground(Color.WHITE);
		txtExpyyyy.setColumns(6);
		txtExpyyyy.setCaretColor(Color.WHITE);
		txtExpyyyy.setBackground(new Color(80, 75, 78));
		txtExpyyyy.setBounds(121, 32, 74, 43);
		panel.add(txtExpyyyy);
		
		JLabel lblPrice = new JLabel("PRICE");
		lblPrice.setHorizontalAlignment(SwingConstants.LEFT);
		lblPrice.setForeground(Color.WHITE);
		lblPrice.setFont(new Font("Verdana", Font.PLAIN, 20));
		lblPrice.setBounds(286, 6, 64, 26);
		panel.add(lblPrice);
		
		txtprice = new JTextField();
		txtprice.setText("0.0");
		txtprice.setPreferredSize(new Dimension(80, 50));
		txtprice.setHorizontalAlignment(SwingConstants.CENTER);
		txtprice.setForeground(Color.WHITE);
		txtprice.setColumns(6);
		txtprice.setCaretColor(Color.WHITE);
		txtprice.setBackground(new Color(80, 75, 78));
		txtprice.setBounds(230, 32, 168, 43);
		panel.add(txtprice);
		
		JLabel lblRight = new JLabel("RIGHT");
		lblRight.setHorizontalAlignment(SwingConstants.LEFT);
		lblRight.setForeground(Color.WHITE);
		lblRight.setFont(new Font("Verdana", Font.PLAIN, 20));
		lblRight.setBounds(465, 6, 80, 26);
		panel.add(lblRight);
		
		JComboBox cmbright = new JComboBox();
		cmbright.setModel(new DefaultComboBoxModel(new String[] {"——", "PUT", "CALL"}));
		cmbright.setFont(new Font("Verdana", Font.PLAIN, 18));
		cmbright.setBounds(442, 27, 114, 49);
		panel.add(cmbright);
		
		JButton btnDelete = new JButton("DELETE");
		btnDelete.setPreferredSize(new Dimension(180, 50));
		btnDelete.setBounds(77, 241, 166, 37);
		innerpanel.add(btnDelete);
		
		JButton btnSave = new JButton("SAVE");
		btnSave.setPreferredSize(new Dimension(180, 50));
		btnSave.setBounds(364, 241, 166, 37);
		innerpanel.add(btnSave);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(Color.DARK_GRAY);
		panel_1.setBorder(new LineBorder(Color.GREEN, 3, true));
		panel_1.setBounds(113, 168, 405, 49);
		innerpanel.add(panel_1);
		panel_1.setLayout(null);
		
		JComboBox cmbheadfeed = new JComboBox();
		cmbheadfeed.setBounds(162, 6, 237, 37);
		panel_1.add(cmbheadfeed);
		cmbheadfeed.setFont(new Font("Verdana", Font.PLAIN, 18));
		
		JLabel lblHeadFeed = new JLabel("HEAD FEED");
		lblHeadFeed.setHorizontalAlignment(SwingConstants.LEFT);
		lblHeadFeed.setForeground(Color.WHITE);
		lblHeadFeed.setFont(new Font("Verdana", Font.PLAIN, 20));
		lblHeadFeed.setBounds(23, 10, 127, 26);
		panel_1.add(lblHeadFeed);
		
		JLabel lblHead = new JLabel("<dynamic>");
		lblHead.setHorizontalAlignment(SwingConstants.CENTER);
		lblHead.setForeground(new Color(255, 220, 135));
		lblHead.setFont(new Font("Verdana", Font.BOLD, 22));
		lblHead.setBounds(6, 13, 653, 43);
		playerframe.getContentPane().add(lblHead);
		playerframe.setVisible(true);
	}

}
