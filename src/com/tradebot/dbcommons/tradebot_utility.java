package com.tradebot.dbcommons;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import org.pmw.tinylog.Configurator;
import org.pmw.tinylog.Logger;
import org.pmw.tinylog.writers.FileWriter;

public class tradebot_utility 
{
	private String configprop=System.getProperty("user.dir")+File.separator+"resource"+File.separator+"config.properties";
	
		public String readconfigprop(String propkey)
		{
			String strResult= null;
			Properties prop = new Properties();
			InputStream input = null;
			String path = null;
			try
			{
				input =new FileInputStream(configprop);
				prop.load(input);
				strResult=prop.getProperty(propkey);
			}
			catch(Exception ex)
			{
				Logger.error(ex.toString());
			}
			finally
			{
				if (input != null) {
					try {
						input.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			return strResult;
		}
	    /*
		* According to the log name and path defined in config.propertise file, it will configure tiny log and return the file path
		*/
		public String configlogfile(String logname)
		{
			String path = null;
			try 
			{
				path = System.getProperty("user.dir") + readconfigprop(logname).replace("/", File.separator);
				Configurator.defaultConfig().writer(new FileWriter(path,false,true)).activate();
				//Logger.info("Log Initiated --> "+path);
			
			}
			catch(Exception ex)
			{
				Logger.error(ex.toString());
			}
			finally 
			{
				
			}
			return path;
		}
		
		public static boolean isNumeric(String str)  
		{  
		  try  
		  {  
		    double d = Double.parseDouble(str);  
		  }  
		  catch(NumberFormatException nfe)  
		  {  
		    return false;  
		  }  
		  return true;  
		}
		
		public void setmessage(JLabel lbl, String msgtype, String message)
		{
			try
			{
				switch (msgtype) {
				case "infp":
					
					lbl.setText(message);
					lbl.setForeground(new Color(124, 252, 0));
					break;

				case "warn":
					lbl.setText(message);
					lbl.setForeground(Color.red);
					break;
				default:
					break;
				}
				lbl.setFont(new Font("Tahoma", Font.BOLD, 15));
				lbl.setHorizontalAlignment(SwingConstants.CENTER);
			}
			catch(Exception ex)
			{
				Logger.error(ex.toString());
			}
			
		}

}
