package com.tradebot.presto;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class JFilePicker extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String textFieldLabel;
	String buttonLabel;
	String buttonok;

	private JLabel labelPlace;
	private JTextField textBrowse;
	private JButton btnBrowse, btnPlaceOrder;
	private Border HIGHLIGHT_BORDER = BorderFactory
			.createLineBorder(java.awt.Color.RED);
	private JFileChooser fileChooser;

	public JFilePicker(String textFieldLabel, String buttonLabel) {
		this.textFieldLabel = textFieldLabel;
		this.buttonLabel = buttonLabel;

		fileChooser = new JFileChooser();

		setLayout(null);

		// creates the GUI
		labelPlace = new JLabel(textFieldLabel);
		textBrowse = new JTextField();
		btnBrowse = new JButton(buttonLabel);
		btnPlaceOrder = new JButton("Place Order");

		btnBrowse.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				buttonActionPerformed(evt);
			}
		});

		btnPlaceOrder.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				if (textBrowse.getText().trim().length() == 0) {
					textBrowse.setBorder(HIGHLIGHT_BORDER);
				} else {
				}
			}
		});

		labelPlace.setFont(new Font("Courier New", Font.PLAIN, 13));
		textBrowse.setFont(new Font("Courier New", Font.PLAIN, 13));
		btnBrowse.setFont(new Font("Courier New", Font.PLAIN, 13));
		btnPlaceOrder.setFont(new Font("Courier New", Font.PLAIN, 13));

		labelPlace.setBounds(10, 20, 110, 25);
		textBrowse.setBounds(120, 20, 310, 25);
		btnBrowse.setBounds(440, 20, 85, 25);
		btnPlaceOrder.setBounds(120, 60, 130, 25);

		add(labelPlace);
		add(textBrowse);
		add(btnBrowse);
		add(btnPlaceOrder);
	}

	private void buttonActionPerformed(ActionEvent evt) {

		if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			textBrowse.setText(fileChooser.getSelectedFile().getAbsolutePath());
			new FilePicker().setVisible(false);
		}
	}

	public void addFileTypeFilter(String extension, String description) {
		FileTypeFilter filter = new FileTypeFilter(extension, description);
		fileChooser.addChoosableFileFilter(filter);
	}

	public String getSelectedFilePath() {
		return textBrowse.getText();
	}

	public JFileChooser getFileChooser() {
		return this.fileChooser;
	}
}