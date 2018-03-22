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

public class F1_HRun_Algo 
{
	public double x,y,z,C=0,LC=0,S=0;
	public Date t1,t2,t3,t4;
	public int maxtradecount =0, TCount=0;
	public boolean isBought =false, isSell = false, isShotsell=false, istradeswitch=false;
	public double buyPrice,sellPrice,low=0.0,high=0.0,Mpoint,stopl=0.0;
	public Date fst1, fst2;
	final static String Fname="F1";
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
    presto_commons objPresto ;

	public F1_HRun_Algo(presto_commons objconnect,  String feedid, double tradeprice, int asksize,int bidsize ,String tickdatetime) 
	{
		tradelogpath = utils.configlogfile("HRUN_LOG");
		 askvolume = asksize;
		 bidvolume = bidsize;		 
		 objPresto =objconnect;
		 try
		 {
			 ArrayList<String> tradeplayers = dbObj.getSingleColumnRecords("SELECT TRADESUBJECTID FROM TBL_PLAYERS WHERE FEEDSUBJECTID = '"+feedid+"';");	
			 datefmt=new SimpleDateFormat("yyyyMMdd HH:mm:ss");
			 for(int i =0; i < tradeplayers.size(); i++)
			 {
				 if (dbObj.getRowCount("SELECT * FROM TBL_F1_HRUN_TRADES  WHERE FEEDSUBJECTID='"+feedid+"' and TRADESUBJECTID='"+tradeplayers.get(i)+"' and ISBUYSELLDONE ='false'") == 0)
				 {
					 dbObj.executeNonQuery("insert into TBL_F1_HRUN_TRADES (FEEDSUBJECTID, TRADESUBJECTID,ENTRYTIME,BUYPRICE,SELLPRICE,EXITTIME,ISSHOTSELL,HIGH,LOW,ISBOUGHT, ISSELL,MPOINT,EXITCONDITION,TCOUNT,ISBUYSELLDONE,ENTRYID,EXITID,C,LC,S) values "
					 		+ "('"+feedid+"', '"+tradeplayers.get(i)+"', null, 0.0,0.0,null,'false',0.0,0.0,'false','false',0.0,null,0,'false',null,null,0,0,0)");
				 }
				 
				 assginF1Variables(feedid, tradeplayers.get(i),tickdatetime);
				 
				 int tradec =Integer.parseInt(dbObj.getSingleCell("SELECT SUM(TCOUNT) as \"count\" FROM TBL_F1_TRADES  WHERE FEEDSUBJECTID='"+feedid+"' and TRADESUBJECTID='"+tradeplayers.get(i)+"'"));
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
			case "STOCK":
				secCode = "CM";
				break;
			case "FUTURE":
				secCode = "FUT";
				break;
			case "OPTIONS":
				secCode = "OPT";
				break;
			case "INDEX":
				secCode = null;
				break;

			default:
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
			String [][] playerdet = dbObj.getMultiColumnRecords("SELECT * FROM TBL_PLAYERS WHERE TRADESUBJECTID='"+tradeid+"';");
			SECURITY_TYPE = getseccode(playerdet[0][4]);
			ESB_SYMBOL = playerdet[0][3];
			ESB_SECURITYID = tradeid;
			
			if (playerdet[0][5] ==null)
			{
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				 Date date = new Date();
				String formattedDate = formatter.format(date);
				EXP_DATE = formattedDate;
			}
			else
			{
				EXP_DATE = playerdet[0][5];
			}
			ESB_ACCOUNT = "FA9749";
			QUAN_TITY = dbObj.getSingleCell("SELECT LOTSIZE FROM TBL_FORMULA WHERE TRADESUBJECTID = '"+tradeid+"' AND FEEDSUBJECTID= '"+feedid+"'");
			T_PRICE="0.0";
			STOP_PRICE="0.0";
			ESB_OPTIONTYPE="null";
			ESB_STRIKEPPRICE ="null";
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
	public void assginF1Variables(String feedid, String tradeid, String livedate)
	{
		String [][] F1inputdata;
		String [][] F1tradedata;
		try
		{
			F1inputdata = dbObj.getMultiColumnRecords("SELECT * FROM TBL_FORMULA WHERE FEEDSUBJECTID='"+feedid+"' and TRADESUBJECTID='"+tradeid+"' and FORMULANAME ='"+Fname+"'");
			if (F1inputdata !=null)
			{
				x = ((F1inputdata[0][colx] == null) ? 0.0 : Double.parseDouble(F1inputdata[0][colx]));
				y = ((F1inputdata[0][coly] == null) ? 0.0 : Double.parseDouble(F1inputdata[0][coly]));
				z = ((F1inputdata[0][colz] == null) ? 0.0 : Double.parseDouble(F1inputdata[0][colz]));
				datefmt=new SimpleDateFormat("yyyyMMdd HH:mm:ss");
				t1 = ((F1inputdata[0][colt1] == null) ? null : datefmt.parse(livedate.split(" ")[0]+" "+F1inputdata[0][colt1]));
				t2 = ((F1inputdata[0][colt2] == null) ? null : datefmt.parse(livedate.split(" ")[0]+" "+F1inputdata[0][colt2]));
				t3 = ((F1inputdata[0][colt3] == null) ? null : datefmt.parse(livedate.split(" ")[0]+" "+F1inputdata[0][colt3]));
				t4 = ((F1inputdata[0][colt4] == null) ? null : datefmt.parse(livedate.split(" ")[0]+" "+F1inputdata[0][colt4]));
				maxtradecount = ((F1inputdata[0][collount] == null ? 2 : Integer.parseInt(F1inputdata[0][collount])));
				stopl=((F1inputdata[0][colstopl] == null ? 0.0 : Double.parseDouble(F1inputdata[0][colstopl])));
				istradeswitch=((F1inputdata[0][coltradeswitch] == null ? false : Boolean.parseBoolean(F1inputdata[0][coltradeswitch])));
			 }
			F1tradedata = dbObj.getMultiColumnRecords("SELECT * FROM TBL_F1_HRUN_TRADES where FEEDSUBJECTID='"+feedid+"' and TRADESUBJECTID='"+tradeid+"' and ISBUYSELLDONE='false'");
			if (F1tradedata != null)
			{
				isBought=((F1tradedata[0][tisbought] == null ? false : Boolean.parseBoolean(F1tradedata[0][tisbought])));
				isSell=((F1tradedata[0][tissell] == null ? false : Boolean.parseBoolean(F1tradedata[0][tissell])));
				isShotsell=((F1tradedata[0][tisshotsell] == null ? false : Boolean.parseBoolean(F1tradedata[0][tisshotsell])));
				buyPrice = ((F1tradedata[0][tbuyprice] == null) ? 0.0 : Double.parseDouble(F1tradedata[0][tbuyprice]));
				sellPrice = ((F1tradedata[0][tsellprice] == null) ? 0.0 : Double.parseDouble(F1tradedata[0][tsellprice]));
				low =  ((F1tradedata[0][tlow] == null) ? 0.0 : Double.parseDouble(F1tradedata[0][tlow]));
				high =  ((F1tradedata[0][thigh] == null) ? 0.0 : Double.parseDouble(F1tradedata[0][thigh]));
				Mpoint = ((F1tradedata[0][tmpoint] == null) ? 0.0 : Double.parseDouble(F1tradedata[0][tmpoint]));
				C = ((F1tradedata[0][tC] == null) ? 0.0 : Integer.parseInt(F1tradedata[0][tC]));
				LC= ((F1tradedata[0][tLC] == null) ? 0.0 : Integer.parseInt(F1tradedata[0][tLC]));
				S= ((F1tradedata[0][tS] == null) ? 0.0 : Integer.parseInt(F1tradedata[0][tS]));
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
			if (ticktime.after(t1))
			{
				if ((low == 0.0) && (high == 0.0))
	    	    {
	    	    	low = tickprice;
	    	    	high =tickprice;
	    	    	dbObj.executeNonQuery("UPDATE TBL_F1_HRUN_TRADES SET LOW = "+low+" , HIGH ="+ high +""
	    	    			+ " WHERE FEEDSUBJECTID='"+feedid+"' and TRADESUBJECTID ='"+tradeid+"' and ISBUYSELLDONE ='false'");
	    	    }
				if ((isBought==false)&&(isSell==false))
				{
					if(tickprice > high){high = tickprice;dbObj.executeNonQuery("UPDATE TBL_F1_HRUN_TRADES  SET HIGH ="+ high +" WHERE FEEDSUBJECTID='"+feedid+"' and TRADESUBJECTID ='"+tradeid+"' and ISBUYSELLDONE ='false'");}
					if(tickprice < low) {low = tickprice;dbObj.executeNonQuery("UPDATE TBL_F1_HRUN_TRADES  SET LOW ="+ low +" WHERE FEEDSUBJECTID='"+feedid+"' and TRADESUBJECTID ='"+tradeid+"' and ISBUYSELLDONE ='false'");}   
				}
				if (isBought == true)
				{
					if(ticktime.after(t4))
					{
						//sell order
						String orderid = null;
						sellPrice =tickprice;				    	    			
    	    			fst2 = ticktime;
    	    			Logger.info("long buy and sell Condition3:"+ticktime);
    	    			TCount =TCount +1;
    	    			if (istradeswitch ==true)
    	    			{
    	    				if (bidvolume >= 1)
        	    			{
    	    					orderid = LoadDataandOrder(feedid, tradeid, "SELL");
    	    					dbObj.executeNonQuery("UPDATE TBL_F1_TRADES  SET EXITCONDITION='Validity Expires',EXITTIME='"+ticktime.toString()+"',SELLPRICE ="+sellPrice+", "
    	    							+ "Tcount="+TCount+", ISBUYSELLDONE = 'true' ,EXITID='"+orderid+"' WHERE FEEDSUBJECTID='"+feedid+"' and TRADESUBJECTID ='"+tradeid+"' and ISBUYSELLDONE ='false'");
    	    					Logger.info("Trade Switch is ON, Order Palced : "+orderid);
    	    				}
    	    			}
    	    			else 
    	    			{
    	    				dbObj.executeNonQuery("UPDATE TBL_F1_TRADES  SET EXITCONDITION='Validity Expires',EXITTIME='"+ticktime.toString()+"',SELLPRICE ="+sellPrice+", Tcount="+TCount+", ISBUYSELLDONE = 'true' WHERE FEEDSUBJECTID='"+feedid+"' and TRADESUBJECTID ='"+tradeid+"' and ISBUYSELLDONE ='false'");
    	    			}
    	    			calculatefigure(feedid,tradeid);
					}
					
				}
				else if (isSell == true)
	    	    {
					
	    	    }
				else if ((isBought==false)&&(isSell==false) && (ticktime.before(t4)))
	    	    {
					double buylegprice = low + (low*(x/100)), sellingprice = high - (high*(x/100));
					if (tickprice > buylegprice)
	        	    {
						fst1 = ticktime;
        	    		if (fst1.after(t2))
        	    		{
        	    			
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
        	    					dbObj.executeNonQuery("UPDATE TBL_F1_HRUN_TRADES  SET ISSHOTSELL='"+isShotsell+"', ENTRYTIME = '"+ticktime.toString()+"',"
        	    							+ "BUYPRICE="+buyPrice+", Mpoint="+Mpoint+", isBought='"+isBought+"',Tcount="+TCount+", ENTRYID='"+orderid+"' WHERE FEEDSUBJECTID='"+feedid+"'"
        	    									+ " and TRADESUBJECTID ='"+tradeid+"' and ISBUYSELLDONE ='false'");   
        	    					Logger.info("Trade Switch is ON, Order Palced : "+orderid);
        	    				}
	        	    		}
        	    			else
        	    			{
        	    				Logger.info("Trade Switch is OFF, paper order");
        	    				dbObj.executeNonQuery("UPDATE TBL_F1_HRUN_TRADES  SET ISSHOTSELL='"+isShotsell+"', ENTRYTIME = '"+ticktime.toString()+"',BUYPRICE="+buyPrice+", Mpoint="+Mpoint+", isBought='"+isBought+"',Tcount="+TCount+" WHERE FEEDSUBJECTID='"+feedid+"' and TRADESUBJECTID ='"+tradeid+"' and ISBUYSELLDONE ='false'");
        	    			}
        	    		}
	        	    }
					else if  (tickprice < sellingprice)
	        	    {
        	    		
        	    		fst1 = ticktime;
        	    		if (fst1.after(t2))
        	    		{
        	    			
        	    			//goto end;
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
	        	    					dbObj.executeNonQuery("UPDATE TBL_F1_HRUN_TRADES  SET ISSHOTSELL='"+isShotsell+"', ENTRYTIME='"+ticktime.toString()+"',SELLPRICE="+sellPrice+", "
	    	        	    					+ "Mpoint="+Mpoint+", isSell='"+isSell+"',Tcount="+TCount+", ENTRYID='"+orderid+"' WHERE FEEDSUBJECTID='"+feedid+"' and TRADESUBJECTID ='"+tradeid+"' "
	    	        	    							+ "and ISBUYSELLDONE ='false'");   
	        	    					Logger.info("Trade Switch is ON, Order Palced : "+orderid);
		        	    			}
	        	    			}
	        	    			else
	        	    			{
	        	    			dbObj.executeNonQuery("UPDATE TBL_F1_HRUN_TRADES  SET ISSHOTSELL='"+isShotsell+"', ENTRYTIME='"+ticktime.toString()+"',SELLPRICE="+sellPrice+", "
	        	    					+ "Mpoint="+Mpoint+", isSell='"+isSell+"',Tcount="+TCount+" WHERE FEEDSUBJECTID='"+feedid+"' and TRADESUBJECTID ='"+tradeid+"' "
	        	    							+ "and ISBUYSELLDONE ='false'");
	        	    			}
	        	    		}
        	    		
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
			dbObj.executeNonQuery("UPDATE TBL_F1_HRUN_TRADES  SET SELLPRICE="+sellPrice+", BUYPRICE ="+buyPrice+" WHERE FEEDSUBJECTID='"+feedid+"' and TRADESUBJECTID ='"+tradeid+"' and ISBUYSELLDONE ='true'");
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
			pldata = dbObj.getMultiColumnRecords("SELECT SUM(SELLPRICE) as \"TOTAL_SELL\",SUM(BUYPRICE) as \"TOTAL_BUY\",  SUM(TCOUNT) as \"TOTAL_TRADE\" FROM TBL_F1_HRUN_TRADES  WHERE FEEDSUBJECTID='"+feedid+"' and TRADESUBJECTID='"+tradeid+"' and ISBUYSELLDONE='true';");
			if (pldata != null)
			{
				fpl = Double.parseDouble(pldata[0][0]) - Double.parseDouble(pldata[0][1]);
				double avg = (fpl*100) / (Integer.parseInt(pldata[0][2]));
				fpercent = (fpl*100)/avg;
				Logger.info("Selling Price : "+ pldata[0][0] );
	        	Logger.info("Buying Price : "+pldata[0][1] );
	        	Logger.info("Trade End of the Dau P&L : "+fpl +", Percentage % :"+fpercent +", Trade Count : "+pldata[0][2]);
	        	dbObj.executeNonQuery("UPDATE  TBL_TRADEBOARD SET F1PC="+f.format(fpercent)+", F1TC="+Integer.parseInt(pldata[0][2])+", F1PL="+ f.format(fpl)+" WHERE FEEDSECID ='"+feedid+"' and TRADESECID = '"+tradeid+"'");
			}
		}
		catch(Exception ex)
		{
			Logger.error(ex);
		}
	}

}