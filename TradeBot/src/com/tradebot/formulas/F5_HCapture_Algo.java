package com.tradebot.formulas;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.pmw.tinylog.Logger;

import com.tradebot.dbcommons.db_commons;
import com.tradebot.dbcommons.tradebot_utility;
import com.tradebot.presto.presto_commons;

public class F5_HCapture_Algo {

	public double x,y,z,C=0,LC=0,S=0;
	public Date t1,t2,t3,t4;
	public int maxtradecount =0, TCount=0, Lcount=0;
	public boolean isBought =false, isSell = false, isShotsell=false, istradeswitch=false;
	public double buyPrice,sellPrice,low=0.0,high=0.0,Mpoint,stopl=0.0;
	public Date fst1, fst2;
	final static String Fname="F5";
	private String configprop=System.getProperty("user.dir")+File.separator+"resource"+File.separator+"config.properties";
	String tradelogpath;
    tradebot_utility utils = new tradebot_utility();
    db_commons dbObj=new db_commons();
    //f1 formula data coloumns sequence
    int colid=0,colfeedid=1,coltradeid=2,colfname=3,colx=4,coly=5,colz=6,colt1=7,colt2=8,colt3=9,colt4=10,collount=11,colstopl=12,colotsize=13,coltradeswitch=14;
    //F1 trade data sequence id
    int tColid=0, tfeedid=1, ttradeid=2, tentrytime=3, tbuyprice=4, tsellprice=5, texittime=6, tisshotsell=7, thigh=8,tlow=9, tisbought=10,
    		tissell=11, tmpoint =12, texitcon =13 , ttcount=14, tisbuyselldone=15, tentryid=16, texitid=17, tC=18, tLC=19,tS=20;
    public static SimpleDateFormat datefmt;
    
    public String tradeid , lasttradeprice, lasttradetime;
    public int askvolume, bidvolume;
    presto_commons objPresto;

	public F5_HCapture_Algo(presto_commons objconnect,  String feedid, double tradeprice, int asksize,int bidsize ,String tickdatetime) 
	{
		tradelogpath = utils.configlogfile("F4HCAP_LOG");
		askvolume = asksize;
		bidvolume = bidsize;		 
		objPresto =objconnect;
		 try
		 {
			 ArrayList<String> tradeplayers = dbObj.getSingleColumnRecords("SELECT TRADESECID FROM TBL_TRADERS WHERE FEEDSECID = '"+feedid+"';");	
			 datefmt=new SimpleDateFormat("yyyyMMdd HH:mm:ss");
			 for(int i =0; i < tradeplayers.size(); i++)
			 {
				 if ((dbObj.getRowCount("SELECT * FROM TBL_FORMULA WHERE FEEDSECID ='"+feedid+"' AND TRADESECID ='"+tradeplayers.get(i)+"'") !=0 )&&(dbObj.getSingleCell("SELECT ISEND FROM TBL_FORMULA WHERE FEEDSECID ='"+feedid+"' AND TRADESECID ='"+tradeplayers.get(i)+"' AND FORMULANAME ='"+Fname+"'").trim() != "true"))
				 {
					 if (dbObj.getRowCount("SELECT * FROM TBL_F5_HCAPTURE_TRADES   WHERE FEEDSECID='"+feedid+"' and TRADESECID='"+tradeplayers.get(i)+"' and ISBUYSELLDONE ='false'") == 0)
					 {
						 dbObj.executeNonQuery("insert into TBL_F5_HCAPTURE_TRADES  (FEEDSECID, TRADESECID,ENTRYTIME,BUYPRICE,SELLPRICE,EXITTIME,ISSHOTSELL,HIGH,LOW,ISBOUGHT, ISSELL,MPOINT,EXITCONDITION,TCOUNT,ISBUYSELLDONE,ENTRYID,EXITID,C,LC,S) values "
						 		+ "('"+feedid+"', '"+tradeplayers.get(i)+"', null, 0.0,0.0,null,'false',0.0,0.0,'false','false',0.0,null,0,'false',null,null,0,0,0)");
					 }
					 else if(dbObj.getRowCount("SELECT * FROM TBL_F5_HCAPTURE_TRADES   WHERE FEEDSECID='"+feedid+"' and TRADESECID='"+tradeplayers.get(i)+"' and ISBUYSELLDONE ='false'") > 0)
					 {
						String[][] lasttransac =  dbObj.getMultiColumnRecords("SELECT high, low, mpoint, C, LC, S FROM TBL_F5_HCAPTURE_TRADES  WHERE id = (SELECT MAX(id) FROM TBL_F5_HCAPTURE_TRADES  ) and FEEDSECID='"+feedid+"' and TRADESECID='"+tradeplayers.get(i)+"';");
						
						dbObj.executeNonQuery("insert into TBL_F5_HCAPTURE_TRADES  (FEEDSECID, TRADESECID,ENTRYTIME,BUYPRICE,SELLPRICE,EXITTIME,ISSHOTSELL,HIGH,LOW,ISBOUGHT, ISSELL,MPOINT,EXITCONDITION,TCOUNT,ISBUYSELLDONE,ENTRYID,EXITID,C,LC,S) values "
						 		+ "('"+feedid+"', '"+tradeplayers.get(i)+"', null, 0.0,0.0,null,'false',"+Integer.parseInt(lasttransac[0][0])+","+Integer.parseInt(lasttransac[0][1])+",'false','false',"+Integer.parseInt(lasttransac[0][2])+",null,0,'false',null,null,"+Integer.parseInt(lasttransac[0][3])+","+Integer.parseInt(lasttransac[0][4])+","+Integer.parseInt(lasttransac[0][5])+")");
					
					 }
					 assginFVariables(feedid, tradeplayers.get(i),tickdatetime);
					 
					 int tradec =Integer.parseInt(dbObj.getSingleCell("SELECT SUM(TCOUNT) as \"count\" FROM TBL_F5_HCAPTURE_TRADES   WHERE FEEDSECID='"+feedid+"' and TRADESECID='"+tradeplayers.get(i)+"'"));
					 TCount = tradec;
					 if (tradec < maxtradecount)
					 {
						 CalculateHRun(feedid, tradeplayers.get(i),Double.valueOf(tradeprice), datefmt.parse(tickdatetime));
					 }
					 else
					 {
						 System.out.print("Max trade Count achieved...");
					 }
			 	}
			 }
		 }
		 catch(Exception ex)
		 {
			 
		 }
		 finally 
		 {
			 
		 }
		 
	}
	public String getseccode(String sectype)
	{
		String secCode=null;
		try
		{
			switch (sectype) {
			case "Equities":
				secCode = "CM";
				break;
			case "FUTIDX":
			case "FUTSTK":
			case "OPTIDX":
			case "OPTSTK":
			case "FUTCOM":
				secCode = "FO";
				break;

			default:
				secCode = "CM";
				break;
			}
		}
		catch(Exception ex)
		{
		   	
		}
		return secCode;
	}
	public String LoadDataandOrder(String feedid, String tradeid, String order)
	{
		String OrderId = null;
		try
		{
			//objPresto.PlaceOrder("omnesys","CM",tradeid,String.valueOf(tradeid) ,"22-02-2018","FA9749","1","0.0","0.0","na","0.0","MARKET",
			//		"Presto_Mathsartz_Strategy","TestOrders","DAY","BUY");
			String ESB_EXCHANGE, SECURITY_TYPE, ESB_SYMBOL, ESB_SECURITYID,
			 EXP_DATE, ESB_ACCOUNT, QUAN_TITY, T_PRICE, STOP_PRICE,	
			 ESB_OPTIONTYPE, ESB_STRIKEPPRICE, ORDER_TYPE, INSTANCEID_CUSTOMFIELD, T_REMARK,
			 TIME_INFORCE, T_SIDE;
			
			ESB_EXCHANGE = "omnesys";
			String [][] playerdet = dbObj.getMultiColumnRecords("SELECT FEEDSECID ,TRADESECID ,SYMBOL ,INSTTYPE ,EXPIRYDD ,EXPIRYMMMYY ,OPTTYPE ,STRIKEPRICE FROM TBL_TRADERS; WHERE TRADESECID='"+tradeid+"' AND FEEDSECID ='"+feedid+"' ;");
			SECURITY_TYPE = getseccode(playerdet[0][3].toString().trim());
			ESB_SYMBOL = playerdet[0][2].toString().trim();
			ESB_SECURITYID = tradeid;
			
			if (playerdet[0][4].toString().trim().equals("0"))
			{
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				 Date date = new Date();
				String formattedDate = formatter.format(date);
				EXP_DATE = formattedDate;
			}
			else
			{
				SimpleDateFormat format1 = new SimpleDateFormat("ddMMMyy");
			    SimpleDateFormat format2 = new SimpleDateFormat("dd-MM-yy");
			    Date date = format1.parse(playerdet[0][4].trim()+playerdet[0][5].trim());
			    System.out.println(format2.format(date));
				EXP_DATE = format2.format(date).toString();
			}
			ESB_ACCOUNT = "FA9749";
			QUAN_TITY = dbObj.getSingleCell("SELECT LOTSIZE FROM TBL_FORMULA WHERE TRADESECID = '"+tradeid+"' AND FEEDSECID= '"+feedid+"'");
			T_PRICE="0.0";
			STOP_PRICE="0.0";
			ESB_OPTIONTYPE=playerdet[0][6].toString().trim();
			ESB_STRIKEPPRICE =playerdet[0][7].toString().trim();
			ORDER_TYPE="MARKET";
			INSTANCEID_CUSTOMFIELD="Presto_Mathsartz_Strategy";
			T_REMARK="Test";
			TIME_INFORCE="DAY"; 
			T_SIDE=order.toUpperCase();
			try 
			{
				OrderId =  objPresto.PlaceOrder(ESB_EXCHANGE, SECURITY_TYPE, ESB_SYMBOL, ESB_SECURITYID,
						 EXP_DATE, ESB_ACCOUNT, QUAN_TITY, T_PRICE, STOP_PRICE,	
						 ESB_OPTIONTYPE, ESB_STRIKEPPRICE, ORDER_TYPE, INSTANCEID_CUSTOMFIELD, T_REMARK,
						 TIME_INFORCE, T_SIDE);
			}
			catch (Exception e) {
				// TODO: handle exception
			}
			
		}
		catch(Exception ex)
		{
			Logger.error(ex);
		}
		return OrderId;
	}
  	public void assginFVariables(String feedid, String tradeid, String livedate)
	{
		String [][] Finputdata;
		String [][] Ftradedata;
		try
		{
			Finputdata = dbObj.getMultiColumnRecords("SELECT * FROM TBL_FORMULA WHERE FEEDSECID='"+feedid+"' and TRADESECID='"+tradeid+"' and FORMULANAME ='"+Fname+"'");
			if (Finputdata !=null)
			{
				x = ((Finputdata[0][colx] == null) ? 0.0 : Double.parseDouble(Finputdata[0][colx]));
				y = ((Finputdata[0][coly] == null) ? 0.0 : Double.parseDouble(Finputdata[0][coly]));
				z = ((Finputdata[0][colz] == null) ? 0.0 : Double.parseDouble(Finputdata[0][colz]));
				datefmt=new SimpleDateFormat("yyyyMMdd HH:mm:ss");
				t1 = ((Finputdata[0][colt1] == null) ? null : datefmt.parse(livedate.split(" ")[0]+" "+Finputdata[0][colt1]));
				t2 = ((Finputdata[0][colt2] == null) ? null : datefmt.parse(livedate.split(" ")[0]+" "+Finputdata[0][colt2]));
				t3 = ((Finputdata[0][colt3] == null) ? null : datefmt.parse(livedate.split(" ")[0]+" "+Finputdata[0][colt3]));
				t4 = ((Finputdata[0][colt4] == null) ? null : datefmt.parse(livedate.split(" ")[0]+" "+Finputdata[0][colt4]));
				maxtradecount = ((Finputdata[0][collount] == null ? 2 : Integer.parseInt(Finputdata[0][collount])));
				stopl=((Finputdata[0][colstopl] == null ? 0.0 : Double.parseDouble(Finputdata[0][colstopl])));
				istradeswitch=((Finputdata[0][coltradeswitch] == null ? false : Boolean.parseBoolean(Finputdata[0][coltradeswitch])));
			 }
			Ftradedata = dbObj.getMultiColumnRecords("SELECT * FROM TBL_F5_HCAPTURE_TRADES  where FEEDSECID='"+feedid+"' and TRADESECID='"+tradeid+"' and ISBUYSELLDONE='false'");
			if (Ftradedata != null)
			{
				isBought=((Ftradedata[0][tisbought] == null ? false : Boolean.parseBoolean(Ftradedata[0][tisbought])));
				isSell=((Ftradedata[0][tissell] == null ? false : Boolean.parseBoolean(Ftradedata[0][tissell])));
				isShotsell=((Ftradedata[0][tisshotsell] == null ? false : Boolean.parseBoolean(Ftradedata[0][tisshotsell])));
				buyPrice = ((Ftradedata[0][tbuyprice] == null) ? 0.0 : Double.parseDouble(Ftradedata[0][tbuyprice]));
				sellPrice = ((Ftradedata[0][tsellprice] == null) ? 0.0 : Double.parseDouble(Ftradedata[0][tsellprice]));
				low =  ((Ftradedata[0][tlow] == null) ? 0.0 : Double.parseDouble(Ftradedata[0][tlow]));
				high =  ((Ftradedata[0][thigh] == null) ? 0.0 : Double.parseDouble(Ftradedata[0][thigh]));
				Mpoint = ((Ftradedata[0][tmpoint] == null) ? 0.0 : Double.parseDouble(Ftradedata[0][tmpoint]));
				C = ((Ftradedata[0][tC] == null) ? 0.0 : Integer.parseInt(Ftradedata[0][tC]));
				LC= ((Ftradedata[0][tLC] == null) ? 0.0 : Integer.parseInt(Ftradedata[0][tLC]));
				S= ((Ftradedata[0][tS] == null) ? 0.0 : Integer.parseInt(Ftradedata[0][tS]));
			}
			
			
		}
		catch(Exception ex)
		{
			Logger.error(ex);
		}
		
	}
	
	public void CalculateHRun(String feedid, String tradeid, Double tickprice, Date ticktime)
	{
	
		try
		{
			int alreadyEntered = 0;
			if (ticktime.after(t1))
			{
				alreadyEntered = dbObj.getRowCount("SELECT * FROM TBL_F5_HCAPTURE_TRADES  WHERE FEEDSECID = '"+feedid+"' and TRADESECID = '"+tradeid+"' and ISBUYSELLDONE ='true'");
				if (alreadyEntered == 0)
				{
					if ((low == 0.0) && (high == 0.0))
		    	    {
		    	    	low = tickprice;
		    	    	high =tickprice;
		    	    	dbObj.executeNonQuery("UPDATE TBL_F5_HCAPTURE_TRADES  SET LOW = "+low+" , HIGH ="+ high +""
		    	    			+ " WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"' and ISBUYSELLDONE ='false'");
		    	    }
				}
				if ((isBought==false)&&(isSell==false))
				{
					if(tickprice > high){high = tickprice;dbObj.executeNonQuery("UPDATE TBL_F5_HCAPTURE_TRADES   SET HIGH ="+ high +" WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"' and ISBUYSELLDONE ='false'");}
					if(tickprice < low) {low = tickprice;dbObj.executeNonQuery("UPDATE TBL_F5_HCAPTURE_TRADES   SET LOW ="+ low +" WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"' and ISBUYSELLDONE ='false'");}   
				}
				
				if (isBought == true)
				{
					// Box 2
					if(ticktime.after(t4))
					{
						//sell order
						String orderid = null;
						sellPrice =tickprice;				    	    			
    	    			fst2 = ticktime;
    	    			if (C >= 1)
    	    			{
    	    				//Ending Execution for HEAD FEED and trade id
        	    			dbObj.executeNonQuery("UPDATE TBL_FORMULA  SET ISEND='true' WHERE FEESECID='"+feedid+"' AND TRADESECID='"+tradeid+"' AND FORMULANAME = '"+Fname+"';"); 
        	    			Logger.info("Head Feed - "+feedid+" & Trade ID - "+tradeid+" && Formula Name - "+Fname+" Achived End point.");
    	    			}
    	    			else 
    	    			{
	    	    			Logger.info("long buy and sell Condition3:"+ticktime);
	    	    			TCount =TCount +1;
	    	    			if (istradeswitch ==true)
	    	    			{
	    	    				if (bidvolume >= 1)
	        	    			{
	    	    					orderid = LoadDataandOrder(feedid, tradeid, "SELL");
	    	    					dbObj.executeNonQuery("UPDATE TBL_F5_HCAPTURE_TRADES   SET EXITCONDITION='Validity Expires',EXITTIME='"+ticktime.toString()+"',SELLPRICE ="+sellPrice+", "
	    	    							+ "Tcount="+TCount+", ISBUYSELLDONE = 'true',LOW ="+low+", HIGH="+high+", Mpoint="+Mpoint+",C="+C+", LC="+LC+", S="+S+" , EXITID='"+orderid+"' WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"' and ISBUYSELLDONE ='false'");
	    	    					Logger.info("Trade Switch is ON, Order Palced : "+orderid);
	    	    				}
	    	    			}
	    	    			else 
	    	    			{
	    	    				dbObj.executeNonQuery("UPDATE TBL_F5_HCAPTURE_TRADES   SET EXITCONDITION='Validity Expires',EXITTIME='"+ticktime.toString()+"',SELLPRICE ="+sellPrice+", Tcount="+TCount+", ISBUYSELLDONE = 'true', LOW ="+low+", HIGH="+high+", Mpoint="+Mpoint+",C="+C+", LC="+LC+", S="+S+" , WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"' and ISBUYSELLDONE ='false'");
	    	    			}
	    	    			calculatefigure(feedid,tradeid);
	    	    			//Ending formula
	    	    			dbObj.executeNonQuery("UPDATE TBL_FORMULA  SET ISEND='true' WHERE FEESECID='"+feedid+"' AND TRADESECID='"+tradeid+"' AND FORMULANAME = '"+Fname+"';"); 
        	    			Logger.info("Head Feed - "+feedid+" & Trade ID - "+tradeid+" && Formula Name - "+Fname+" Achived End point.");
    	    			}
    	    		}
					// Box 3
					else if(tickprice < Mpoint - (Mpoint*(y/100)))
					{
						String orderid = null;
						sellPrice =tickprice;				    	    			
    	    			fst2 = ticktime;
    	    			Logger.info("long buy and sell Condition3:"+ticktime);
    	    			if (C == 0)
    	    			{
	    	    			if (istradeswitch ==true)
	    	    			{
	    	    				if (bidvolume >= 1)
	        	    			{
	    	    					orderid = LoadDataandOrder(feedid, tradeid, "SELL");
	    	    					dbObj.executeNonQuery("UPDATE TBL_F5_HCAPTURE_TRADES   SET EXITCONDITION='2nd Condition',EXITTIME='"+ticktime.toString()+"',SELLPRICE ="+sellPrice+", "
	    	    							+ "Tcount="+TCount+", ISBUYSELLDONE = 'true' , LOW ="+low+", HIGH="+high+", Mpoint="+Mpoint+",C="+C+", LC="+LC+", S="+S+",  EXITID='"+orderid+"' WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"' and ISBUYSELLDONE ='false'");
	    	    					Logger.info("Trade Switch is ON, Order Palced : "+orderid);
	    	    				}
	    	    			}
	    	    			else 
	    	    			{
	    	    				dbObj.executeNonQuery("UPDATE TBL_F5_HCAPTURE_TRADES   SET EXITCONDITION='Validity Expires',EXITTIME='"+ticktime.toString()+"',SELLPRICE ="+sellPrice+", Tcount="+TCount+", ISBUYSELLDONE = 'true', LOW ="+low+", HIGH="+high+", Mpoint="+Mpoint+",C="+C+", LC="+LC+", S="+S+", WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"' and ISBUYSELLDONE ='false'");
	    	    			}
	    	    			calculatefigure(feedid,tradeid);
    	    			}
    	    			TCount =TCount +1;
    	    			Mpoint = Mpoint - (Mpoint*(y/100));
    	    			C = C - 1;
    	    			if (C == -1)
    	    			{
    	    				LC = LC + 1;
    	    			}
    	    			if(LC == Lcount )
    	    			{
    	    				// goto end
    	    				//Ending Execution for HEAD FEED
        	    			dbObj.executeNonQuery("UPDATE TBL_FORMULA  SET ISEND='true' WHERE FEESECID='"+feedid+"' AND TRADESECID='"+tradeid+"' AND FORMULANAME = '"+Fname+"';"); 
        	    			Logger.info("Head Feed - "+feedid+" & Trade ID - "+tradeid+" && Formula Name - "+Fname+" Achived End point.");
    	    			}
    	    			else
    	    			{
    	    				S = S +1;
    	    			}
    	    			if(S == stopl)
    	    			{
    	    				// goto end
    	    				//Ending Execution for HEAD FEED
        	    			dbObj.executeNonQuery("UPDATE TBL_FORMULA  SET ISEND='true' WHERE FEESECID='"+feedid+"' AND TRADESECID='"+tradeid+"' AND FORMULANAME = '"+Fname+"';"); 
        	    			Logger.info("Head Feed - "+feedid+" & Trade ID - "+tradeid+" && Formula Name - "+Fname+" Achived End point.");
    	    			}	
    	    			else
    	    			{
    	    				C=0;
    	    			}
    	    			
    	    			
    	    			
					}
					// Box 4
					else if(tickprice >= Mpoint + (Mpoint*(z/100)))
					{
						String orderid = null;
						Mpoint= Mpoint + (Mpoint*(z/100));
						C=C+1;
						dbObj.executeNonQuery("UPDATE TBL_F5_HCAPTURE_TRADES  SET LOW ="+low+", HIGH="+high+", Mpoint="+Mpoint+",C="+C+", LC="+LC+", S="+S+", WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"' and ISBUYSELLDONE ='false'");
    	    			if (C == 1)
    	    			{
    	    				if (istradeswitch ==true)
	    	    			{
	    	    				if (bidvolume >= 1)
	        	    			{
	    	    					orderid = LoadDataandOrder(feedid, tradeid, "SELL");
	    	    					dbObj.executeNonQuery("UPDATE TBL_F5_HCAPTURE_TRADES   SET EXITCONDITION='2nd Condition',EXITTIME='"+ticktime.toString()+"',SELLPRICE ="+sellPrice+", "
	    	    							+ "Tcount="+TCount+", ISBUYSELLDONE = 'true' , LOW ="+low+", HIGH="+high+", Mpoint="+Mpoint+",C="+C+", LC="+LC+", S="+S+",  EXITID='"+orderid+"' WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"' and ISBUYSELLDONE ='false'");
	    	    					Logger.info("Trade Switch is ON, Order Palced : "+orderid);
	    	    				}
	    	    			}
	    	    			else 
	    	    			{
	    	    				dbObj.executeNonQuery("UPDATE TBL_F5_HCAPTURE_TRADES   SET EXITCONDITION='Validity Expires',EXITTIME='"+ticktime.toString()+"',SELLPRICE ="+sellPrice+", Tcount="+TCount+", ISBUYSELLDONE = 'true', LOW ="+low+", HIGH="+high+", Mpoint="+Mpoint+",C="+C+", LC="+LC+", S="+S+", WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"' and ISBUYSELLDONE ='false'");
	    	    			}
	    	    			calculatefigure(feedid,tradeid);
    	    			}
					}
					
				}
				else if (isSell == true)
	    	    {
					if (tickprice <= (Mpoint - (Mpoint*(z/100))))
					{
						String orderid = null;
						Mpoint = Mpoint - (Mpoint*(z/100));
						C=C+1;
						dbObj.executeNonQuery("UPDATE TBL_F5_HCAPTURE_TRADES  SET LOW ="+low+", HIGH="+high+", Mpoint="+Mpoint+",C="+C+", LC="+LC+", S="+S+", WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"' and ISBUYSELLDONE ='false'");
						
						if (C == 1)
						{
							if (istradeswitch ==true)
	    	    			{
	    	    				if (askvolume >= 1)
	        	    			{
	    	    					orderid = LoadDataandOrder(feedid, tradeid, "BUY");
	    	    					dbObj.executeNonQuery("UPDATE TBL_F5_HCAPTURE_TRADES   SET EXITCONDITION='2nd Condition',EXITTIME='"+ticktime.toString()+"',BUYPRICE ="+buyPrice+", "
	    	    							+ "Tcount="+TCount+", ISBUYSELLDONE = 'true' , LOW ="+low+", HIGH="+high+", Mpoint="+Mpoint+",C="+C+", LC="+LC+", S="+S+",  EXITID='"+orderid+"' WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"' and ISBUYSELLDONE ='false'");
	    	    					Logger.info("Trade Switch is ON, Order Palced : "+orderid);
	    	    				}
	    	    			}
	    	    			else 
	    	    			{
	    	    				dbObj.executeNonQuery("UPDATE TBL_F5_HCAPTURE_TRADES   SET EXITCONDITION='Validity Expires',EXITTIME='"+ticktime.toString()+"',BUYPRICE ="+buyPrice+", Tcount="+TCount+", ISBUYSELLDONE = 'true', LOW ="+low+", HIGH="+high+", Mpoint="+Mpoint+",C="+C+", LC="+LC+", S="+S+", WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"' and ISBUYSELLDONE ='false'");
	    	    			}
	    	    			calculatefigure(feedid,tradeid);
						}
					}
					else if(tickprice > (Mpoint + (Mpoint*(y/100))))
					{
						String orderid = null;
						buyPrice =tickprice;				    	    			
    	    			fst2 = ticktime;
    	    			Logger.info("short sell buy Condition2:"+ticktime);
    	    			if (C == 0)
    	    			{
	    	    			if (istradeswitch ==true)
	    	    			{
	    	    				if (askvolume >= 1)
	        	    			{
	    	    					orderid = LoadDataandOrder(feedid, tradeid, "BUY");
	    	    					dbObj.executeNonQuery("UPDATE TBL_F5_HCAPTURE_TRADES   SET EXITCONDITION='2nd Condition',EXITTIME='"+ticktime.toString()+"',BUYPRICE ="+buyPrice+", "
	    	    							+ "Tcount="+TCount+", ISBUYSELLDONE = 'true' , LOW ="+low+", HIGH="+high+", Mpoint="+Mpoint+",C="+C+", LC="+LC+", S="+S+",  EXITID='"+orderid+"' WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"' and ISBUYSELLDONE ='false'");
	    	    					Logger.info("Trade Switch is ON, Order Palced : "+orderid);
	    	    				}
	    	    			}
	    	    			else 
	    	    			{
	    	    				dbObj.executeNonQuery("UPDATE TBL_F5_HCAPTURE_TRADES   SET EXITCONDITION='Validity Expires',EXITTIME='"+ticktime.toString()+"',BUYPRICE ="+buyPrice+", Tcount="+TCount+", ISBUYSELLDONE = 'true', LOW ="+low+", HIGH="+high+", Mpoint="+Mpoint+",C="+C+", LC="+LC+", S="+S+", WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"' and ISBUYSELLDONE ='false'");
	    	    			}
	    	    			calculatefigure(feedid,tradeid);
    	    			}
    	    			TCount =TCount +1;
    	    			Mpoint = Mpoint + (Mpoint*(y/100));
    	    			C=C-1;
    	    			if (C == -1)
    	    			{
    	    				LC = LC + 1;
    	    			}
    	    			if (LC == Lcount)
    	    			{
    	    				//Ending Execution for HEAD FEED
        	    			dbObj.executeNonQuery("UPDATE TBL_FORMULA  SET ISEND='true' WHERE FEESECID='"+feedid+"' AND TRADESECID='"+tradeid+"' AND FORMULANAME = '"+Fname+"';"); 
        	    			Logger.info("Head Feed - "+feedid+" & Trade ID - "+tradeid+" && Formula Name - "+Fname+" Achived End point.");
    	    			}
    	    			else
    	    			{
    	    				S = S + 1;
    	    			}
    	    			if(S == stopl)
    	    			{
    	    				
    	    				dbObj.executeNonQuery("UPDATE TBL_FORMULA  SET ISEND='true' WHERE FEESECID='"+feedid+"' AND TRADESECID='"+tradeid+"' AND FORMULANAME = '"+Fname+"';"); 
        	    			Logger.info("Head Feed - "+feedid+" & Trade ID - "+tradeid+" && Formula Name - "+Fname+" Achived End point.");
    	    			}
    	    			else
    	    			{
    	    				C =0;
    	    			}
    	    			
					}
					else if(ticktime.after(t4))
					{
						String orderid = null;
						buyPrice =tickprice;				    	    			
    	    			fst2 = ticktime;
    	    			Logger.info("short buy Condition3:"+ticktime);
    	    			TCount =TCount +1;
    	    			if (C >= 1)
    	    			{
    	    				//Ending Execution for HEAD FEED
	    	    			dbObj.executeNonQuery("UPDATE TBL_FORMULA  SET ISEND='true' WHERE FEESECID='"+feedid+"' AND TRADESECID='"+tradeid+"' AND FORMULANAME = '"+Fname+"';"); 
	    	    			Logger.info("Head Feed - "+feedid+" & Trade ID - "+tradeid+" && Formula Name - "+Fname+" Achived End point.");
    	    			
    	    			}
    	    			else
    	    			{
	    	    			if (istradeswitch ==true)
	    	    			{
	    	    				if (askvolume >= 1)
	        	    			{
	    	    					orderid = LoadDataandOrder(feedid, tradeid, "BUY");
	    	    					dbObj.executeNonQuery("UPDATE TBL_F5_HCAPTURE_TRADES   SET EXITCONDITION='Validity Expires',EXITTIME='"+ticktime.toString()+"',BUYPRICE ="+buyPrice+", "
	    	    							+ "Tcount="+TCount+", ISBUYSELLDONE = 'true',LOW ="+low+", HIGH="+high+", Mpoint="+Mpoint+",C="+C+", LC="+LC+", S="+S+" , EXITID='"+orderid+"' WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"' and ISBUYSELLDONE ='false'");
	    	    					Logger.info("Trade Switch is ON, Order Palced : "+orderid);
	    	    				}
	    	    			}
	    	    			else 
	    	    			{
	    	    				dbObj.executeNonQuery("UPDATE TBL_F5_HCAPTURE_TRADES   SET EXITCONDITION='Validity Expires',EXITTIME='"+ticktime.toString()+"',BUYPRICE ="+buyPrice+", Tcount="+TCount+", ISBUYSELLDONE = 'true', LOW ="+low+", HIGH="+high+", Mpoint="+Mpoint+",C="+C+", LC="+LC+", S="+S+" , WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"' and ISBUYSELLDONE ='false'");
	    	    			}
	    	    			calculatefigure(feedid,tradeid);
	    	    			//Ending Execution for HEAD FEED
	    	    			dbObj.executeNonQuery("UPDATE TBL_FORMULA  SET ISEND='true' WHERE FEESECID='"+feedid+"' AND TRADESECID='"+tradeid+"' AND FORMULANAME = '"+Fname+"';"); 
	    	    			Logger.info("Head Feed - "+feedid+" & Trade ID - "+tradeid+" && Formula Name - "+Fname+" Achived End point.");
    	    			}
					}
	    	    }
				// branching Box 1  buyleg or shortleg box 1
				else if ((isBought==false)&&(isSell==false)&&(alreadyEntered==0))
	    	    {
					double buylegprice = low + (low*(x/100)), sellingprice = high - (high*(x/100));
					// buy leg box 1
					if (tickprice > buylegprice)
	        	    {
						fst1 = ticktime;
        	    		if (fst1.after(t2))
        	    		{
        	    			//Ending Execution for HEAD FEED
        	    			dbObj.executeNonQuery("UPDATE TBL_FORMULA  SET ISEND='true' WHERE FEESECID='"+feedid+"' AND TRADESECID='"+tradeid+"' AND FORMULANAME = '"+Fname+"';"); 
        	    			Logger.info("Head Feed - "+feedid+" & Trade ID - "+tradeid+" && Formula Name - "+Fname+" Achived End point.");
        	    			//goto end;
        	    			
        	    		}
        	    		else {
        	    			String orderid=null;
        	    			isShotsell = false;
        	    			//buy command later
        	    			buyPrice = tickprice;
        	    			Mpoint = low + (low*(x/100));
        	    			isBought=true;
        	    			Logger.info("Long buy :"+ticktime);
        	    			Logger.info("HIGH : "+high+", LOW : "+low);
        	    			TCount=1;
        	    			if (istradeswitch == true)
        	    			{
        	    				if (askvolume >= 1)
	        	    			{
        	    					orderid = LoadDataandOrder(feedid, tradeid, "BUY");
        	    					dbObj.executeNonQuery("UPDATE TBL_F5_HCAPTURE_TRADES   SET ENTRYTIME = '"+ticktime.toString()+"',"
        	    							+ "BUYPRICE="+buyPrice+",ISSHOTSELL='"+isShotsell+"',  isBought='"+isBought+"', LOW ="+low+", HIGH="+high+", Mpoint="+Mpoint+",C="+C+", LC="+LC+", S="+S+" ,Tcount="+TCount+", ENTRYID='"+orderid+"' WHERE FEEDSECID='"+feedid+"'"
        	    									+ " and TRADESECID ='"+tradeid+"' and ISBUYSELLDONE ='false'");   
        	    					Logger.info("Trade Switch is ON, Order Palced : "+orderid);
        	    				}
	        	    		}
        	    			else
        	    			{
        	    				Logger.info("Trade Switch is OFF, paper order");
        	    				dbObj.executeNonQuery("UPDATE TBL_F5_HCAPTURE_TRADES   SET ENTRYTIME = '"+ticktime.toString()+"',"
        	    						+"BUYPRICE="+buyPrice+",ISSHOTSELL='"+isShotsell+"',  isBought='"+isBought+"', LOW ="+low+", HIGH="+high+", Mpoint="+Mpoint+",C="+C+", LC="+LC+", S="+S+" ,Tcount="+TCount+" WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"' and ISBUYSELLDONE ='false'");
        	    			}
        	    		}
	        	    }
					// shot sell box 1
					else if  (tickprice < sellingprice)
	        	    {
        	    		
        	    		fst1 = ticktime;
        	    		if (fst1.after(t2))
        	    		{
        	    			//goto end;
        	    			//Ending Execution for HEAD FEED
        	    			dbObj.executeNonQuery("UPDATE TBL_FORMULA  SET ISEND='true' WHERE FEESECID='"+feedid+"' AND TRADESECID='"+tradeid+"' AND FORMULANAME = '"+Fname+"';"); 
        	    			Logger.info("Head Feed - "+feedid+" & Trade ID - "+tradeid+" && Formula Name - "+Fname+" Achived End point.");
        	    		}
        	    		else {
        	    			//sell command later
        	    				String orderid=null;
	        	    			isShotsell = true;
	        	    			sellPrice = tickprice;
	        	    			Mpoint = high - (high*(x/100));
	        	    			isSell=true;
	        	    			TCount=1;
	        	    			Logger.info("Short Sell :"+ticktime);
	        	    			Logger.info("HIGH : "+high+", LOW : "+low);
	        	    			if (istradeswitch ==true)
	        	    			{
	        	    				if (bidvolume >= 1)
		        	    			{
	        	    					orderid = LoadDataandOrder(feedid, tradeid, "SELL");
	        	    					dbObj.executeNonQuery("UPDATE TBL_F5_HCAPTURE_TRADES   SET ISSHOTSELL='"+isShotsell+"', ENTRYTIME='"+ticktime.toString()+"',SELLPRICE="+sellPrice+", "
	    	        	    					+ ", isSell='"+isSell+"',Tcount="+TCount+",LOW ="+low+", HIGH="+high+", Mpoint="+Mpoint+",C="+C+", LC="+LC+", S="+S+" , ENTRYID='"+orderid+"' WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"' "
	    	        	    							+ "and ISBUYSELLDONE ='false'");   
	        	    					Logger.info("Trade Switch is ON, Order Palced : "+orderid);
		        	    			}
	        	    			}
	        	    			else
	        	    			{
	        	    			dbObj.executeNonQuery("UPDATE TBL_F5_HCAPTURE_TRADES   SET ISSHOTSELL='"+isShotsell+"', ENTRYTIME='"+ticktime.toString()+"',SELLPRICE="+sellPrice+", "
	        	    					+ ", isSell='"+isSell+"',Tcount="+TCount+", LOW ="+low+", HIGH="+high+", Mpoint="+Mpoint+",C="+C+", LC="+LC+", S="+S+" , WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"' "
	        	    							+ "and ISBUYSELLDONE ='false'");
	        	    			}
	        	    		}
        	    		
        	    }
	    	    }
				
				else if((isBought==false)&&(isSell==false)&&(alreadyEntered > 0))
				{
					double buylegprice = Mpoint + (Mpoint*(x/100)), sellingprice = Mpoint - (Mpoint*(x/100));
					if (tickprice > buylegprice)
					{
						fst1 = ticktime;
        	    		if (fst1.after(t2))
        	    		{
        	    			//Ending Execution for HEAD FEED
        	    			dbObj.executeNonQuery("UPDATE TBL_FORMULA  SET ISEND='true' WHERE FEESECID='"+feedid+"' AND TRADESECID='"+tradeid+"' AND FORMULANAME = '"+Fname+"';"); 
        	    			Logger.info("Head Feed - "+feedid+" & Trade ID - "+tradeid+" && Formula Name - "+Fname+" Achived End point.");
        	    			//goto end;
        	    			
        	    		}
        	    		else {
        	    			String orderid=null;
        	    			isShotsell = false;
        	    			//buy command later
        	    			buyPrice = tickprice;
        	    			Mpoint =  Mpoint + (Mpoint*(x/100));
        	    			isBought=true;
        	    			Logger.info("Long buy :"+ticktime);
        	    			Logger.info("HIGH : "+high+", LOW : "+low);
        	    			TCount=1;
        	    			if (istradeswitch == true)
        	    			{
        	    				if (askvolume >= 1)
	        	    			{
        	    					orderid = LoadDataandOrder(feedid, tradeid, "BUY");
        	    					dbObj.executeNonQuery("UPDATE TBL_F5_HCAPTURE_TRADES   SET ENTRYTIME = '"+ticktime.toString()+"',"
        	    							+ "BUYPRICE="+buyPrice+",ISSHOTSELL='"+isShotsell+"',  isBought='"+isBought+"', LOW ="+low+", HIGH="+high+", Mpoint="+Mpoint+",C="+C+", LC="+LC+", S="+S+" ,Tcount="+TCount+", ENTRYID='"+orderid+"' WHERE FEEDSECID='"+feedid+"'"
        	    									+ " and TRADESECID ='"+tradeid+"' and ISBUYSELLDONE ='false'");   
        	    					Logger.info("Trade Switch is ON, Order Palced : "+orderid);
        	    				}
	        	    		}
        	    			else
        	    			{
        	    				Logger.info("Trade Switch is OFF, paper order");
        	    				dbObj.executeNonQuery("UPDATE TBL_F5_HCAPTURE_TRADES   SET ENTRYTIME = '"+ticktime.toString()+"',"
        	    						+"BUYPRICE="+buyPrice+",ISSHOTSELL='"+isShotsell+"',  isBought='"+isBought+"', LOW ="+low+", HIGH="+high+", Mpoint="+Mpoint+",C="+C+", LC="+LC+", S="+S+" ,Tcount="+TCount+" WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"' and ISBUYSELLDONE ='false'");
        	    			}
        	    		}
					}
					else if(tickprice < sellingprice)
					{
						fst1 = ticktime;
        	    		if (fst1.after(t2))
        	    		{
        	    			//goto end;
        	    			//Ending Execution for HEAD FEED
        	    			dbObj.executeNonQuery("UPDATE TBL_FORMULA  SET ISEND='true' WHERE FEESECID='"+feedid+"' AND TRADESECID='"+tradeid+"' AND FORMULANAME = '"+Fname+"';"); 
        	    			Logger.info("Head Feed - "+feedid+" & Trade ID - "+tradeid+" && Formula Name - "+Fname+" Achived End point.");
        	    		}
        	    		else {
        	    			//sell command later
        	    				String orderid=null;
	        	    			isShotsell = true;
	        	    			sellPrice = tickprice;
	        	    			Mpoint = Mpoint - (Mpoint*(x/100));
	        	    			isSell=true;
	        	    			TCount=1;
	        	    			Logger.info("Short Sell :"+ticktime);
	        	    			Logger.info("HIGH : "+high+", LOW : "+low);
	        	    			if (istradeswitch ==true)
	        	    			{
	        	    				if (bidvolume >= 1)
		        	    			{
	        	    					orderid = LoadDataandOrder(feedid, tradeid, "SELL");
	        	    					dbObj.executeNonQuery("UPDATE TBL_F5_HCAPTURE_TRADES   SET ISSHOTSELL='"+isShotsell+"', ENTRYTIME='"+ticktime.toString()+"',SELLPRICE="+sellPrice+", "
	    	        	    					+ ", isSell='"+isSell+"',Tcount="+TCount+",LOW ="+low+", HIGH="+high+", Mpoint="+Mpoint+",C="+C+", LC="+LC+", S="+S+" , ENTRYID='"+orderid+"' WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"' "
	    	        	    							+ "and ISBUYSELLDONE ='false'");   
	        	    					Logger.info("Trade Switch is ON, Order Palced : "+orderid);
		        	    			}
	        	    			}
	        	    			else
	        	    			{
	        	    			dbObj.executeNonQuery("UPDATE TBL_F5_HCAPTURE_TRADES   SET ISSHOTSELL='"+isShotsell+"', ENTRYTIME='"+ticktime.toString()+"',SELLPRICE="+sellPrice+", "
	        	    					+ ", isSell='"+isSell+"',Tcount="+TCount+", LOW ="+low+", HIGH="+high+", Mpoint="+Mpoint+",C="+C+", LC="+LC+", S="+S+" , WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"' "
	        	    							+ "and ISBUYSELLDONE ='false'");
	        	    			}
	        	    		}
					}
					
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
	public double CalculateTax(double price)
	{
		double finalcost =0.0, Brokerage=0.0,SST=0.0,TC=0.0,ST=0.0,SEBI=0.0,STAMP=0.0;
		try
		{
			Brokerage = (price*0.006)/100;
			SST=(price*0.0125)/100;
			TC=(price*0.00325)/100;
			ST = (Brokerage+TC)*18/100;
			SEBI=(price*0.002)/100;
			STAMP=(price*0.006)/100;
			finalcost = price - (Brokerage + SST + TC + ST + SEBI + STAMP);
		}
		catch(Exception ex)
		{
			Logger.error(ex);
		}
		return finalcost;
	}
	
	public void calculatefigure(String feedid, String tradeid)
	{
		
		try
		{
			sellPrice = CalculateTax(sellPrice);
			buyPrice = CalculateTax(buyPrice);
			dbObj.executeNonQuery("UPDATE TBL_F5_HCAPTURE_TRADES   SET SELLPRICE="+sellPrice+", BUYPRICE ="+buyPrice+" WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"' and ISBUYSELLDONE ='true'");
			UpdateTradeBoard(feedid, tradeid);
		}
		catch(Exception ex)
		{
			Logger.error(ex);
		}
	}
	public void UpdateTradeBoard(String feedid, String tradeid)
	{
		String [][] pldata;
		double fpl=0.00, fpercent=0;
		DecimalFormat f = new DecimalFormat("##.00");
		try
		{
			pldata = dbObj.getMultiColumnRecords("SELECT SUM(SELLPRICE) as \"TOTAL_SELL\",SUM(BUYPRICE) as \"TOTAL_BUY\",  SUM(TCOUNT) as \"TOTAL_TRADE\" FROM TBL_F5_HCAPTURE_TRADES   WHERE FEEDSECID='"+feedid+"' and TRADESECID='"+tradeid+"' and ISBUYSELLDONE='true';");
			if (pldata != null)
			{
				fpl = Double.parseDouble(pldata[0][0]) - Double.parseDouble(pldata[0][1]);
				double avg = (fpl*100) / (Integer.parseInt(pldata[0][2]));
				fpercent = (fpl*100)/avg;
				Logger.info("Selling Price : "+ pldata[0][0] );
	        	Logger.info("Buying Price : "+pldata[0][1] );
	        	Logger.info("Trade End of the Dau P&L : "+fpl +", Percentage % :"+fpercent +", Trade Count : "+pldata[0][2]);
	        	dbObj.executeNonQuery("UPDATE  TBL_TRADEBOARD SET F5PC="+f.format(fpercent)+", F5TC="+Integer.parseInt(pldata[0][2])+", F5PL="+ f.format(fpl)+" WHERE FEEDSECID ='"+feedid+"' and TRADESECID = '"+tradeid+"'");
			}
		}
		catch(Exception ex)
		{
			Logger.error(ex);
		}
	}



}
