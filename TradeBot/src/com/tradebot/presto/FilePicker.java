package com.tradebot.presto;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class FilePicker extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FilePicker() {
		super("Select File");

		setLayout(new GridLayout(2, 2));

		// set up a file picker component
		JFilePicker filePicker = new JFilePicker("Pick a file", "Browse");
		filePicker.addFileTypeFilter(".csv", "CSV File");
		filePicker.addFileTypeFilter(".txt", "Text File");

		// add the component to the frame
		add(filePicker);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600, 200);
		setResizable(false);
		setLocationRelativeTo(null);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new FilePicker().setVisible(true);
			}
		});
	}

}