package com.tradebot.dbcommons;

import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.Timer;
import javax.swing.SwingConstants;
import java.util.*;
import java.text.*;

public class ClockLabel extends JLabel implements ActionListener {
	 
	  String type;
	  SimpleDateFormat sdf;
	 
	  public ClockLabel(String type) {
	    this.type = type;
	    setForeground(Color.green);
	 
	    switch (type) {
	      case "date" : sdf = new SimpleDateFormat("dd-MM-yyyy");
					    setFont(new Font("Tahoma", Font.PLAIN, 16));
						setForeground(new Color(248, 248, 255));
	                    setHorizontalAlignment(SwingConstants.LEFT);
	                    setBounds(1300, 3, 99, 48);
	                    break;
	      case "time" : sdf = new SimpleDateFormat("hh:mm:ss a");
	                    setFont(new Font("Tahoma", Font.PLAIN, 24));
	        			    setForeground(new Color(248, 248, 255));
	                    setHorizontalAlignment(SwingConstants.CENTER);
	                    setBounds(1566, 3, 99, 48);
	                    break;
	      case "day"  : sdf = new SimpleDateFormat("EEEE  ");
					    setFont(new Font("Tahoma", Font.PLAIN, 12));
						setForeground(new Color(248, 248, 255));
	                    setHorizontalAlignment(SwingConstants.LEFT);
	                    setBounds(1350, 3, 99, 48);
	                    break;
	      default     : sdf = new SimpleDateFormat();
	                    break;
	    }
	 
	    Timer t = new Timer(1000, this);
	    t.start();
	  }
	 
	public void actionPerformed(ActionEvent ae) {
	    Date d = new Date();
	    setText(sdf.format(d));
	  }

	}