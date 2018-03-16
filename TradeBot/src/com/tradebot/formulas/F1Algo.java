package com.tradebot.formulas;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.LookAndFeel;

import org.pmw.tinylog.Logger;

import com.tradebot.dbcommons.db_commons;
import com.tradebot.dbcommons.tradebot_utility;
import com.tradebot.presto.presto_commons;

public class F1Algo {
	public double x,y,z;
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
    int tColid=0, tfeedid=1, ttradeid=2, tentrytime=3, tbuyprice=4, tsellprice=5, texittime=6, tisshotsell=7, thigh=8,tlow=9, tisbought=10, tissell=11, tmpoint =12, texitcon =13 , ttcount=14, tisbuyselldone=15;
    public static SimpleDateFormat datefmt;
    
    public String tradeid , lasttradeprice, lasttradetime;
    public int askvolume, bidvolume;
    presto_commons objPresto;
	
	public F1Algo(presto_commons objconnect,  String feedid, double tradeprice, int asksize,int bidsize ,String tickdatetime) 
	{
		// TODO Auto-generated constructor stub
		 tradelogpath = utils.configlogfile("TRADEBOT_LOG");
		 askvolume = asksize;
		 bidvolume = bidsize;		 
		 objPresto =objconnect;
		 try
		 {
			 ArrayList<String> tradeplayers = dbObj.getSingleColumnRecords("SELECT TRADESUBJECTID FROM TBL_PLAYERS WHERE FEEDSUBJECTID = '"+feedid+"';");	
			 datefmt=new SimpleDateFormat("yyyyMMdd HH:mm:ss");
			 for(int i =0; i < tradeplayers.size(); i++)
			 {
				 if (dbObj.getRowCount("SELECT * FROM TBL_F1_TRADES  WHERE FEEDSUBJECTID='"+feedid+"' and TRADESUBJECTID='"+tradeplayers.get(i)+"' and ISBUYSELLDONE ='false'") == 0)
				 {
					 dbObj.executeNonQuery("insert into TBL_F1_TRADES (FEEDSUBJECTID, TRADESUBJECTID,ENTRYTIME,BUYPRICE,SELLPRICE,EXITTIME,ISSHOTSELL,HIGH,LOW,ISBOUGHT, ISSELL,MPOINT,EXITCONDITION,TCOUNT,ISBUYSELLDONE ) values "
					 		+ "('"+feedid+"', '"+tradeplayers.get(i)+"', null, 0.0,0.0,null,'false',0.0,0.0,'false','false',0.0,null,0,'false')");
				 }
				 
				 assginF1variables(feedid, tradeplayers.get(i),tickdatetime);
				 
				 int tradec =Integer.parseInt(dbObj.getSingleCell("SELECT SUM(TCOUNT) as \"count\" FROM TBL_F1_TRADES  WHERE FEEDSUBJECTID='"+feedid+"' and TRADESUBJECTID='"+tradeplayers.get(i)+"'"));
				 TCount = tradec;
				 if (tradec < maxtradecount)
				 {

					 CalculateF1(feedid, tradeplayers.get(i),Double.valueOf(tradeprice), datefmt.parse(tickdatetime));
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
	}
	
	public void assginF1variables(String feedid, String tradeid, String livedate)
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
			F1tradedata = dbObj.getMultiColumnRecords("SELECT * FROM TBL_F1_TRADES  where FEEDSUBJECTID='"+feedid+"' and TRADESUBJECTID='"+tradeid+"' and ISBUYSELLDONE='false'");
			if (F1tradedata != null)
			{
				isBought=((F1tradedata[0][tisbought] == null ? false : Boolean.parseBoolean(F1tradedata[0][tisbought])));
				isSell=((F1tradedata[0][tissell] == null ? false : Boolean.parseBoolean(F1tradedata[0][tissell])));
				isShotsell=((F1tradedata[0][tisshotsell] == null ? false : Boolean.parseBoolean(F1tradedata[0][tisshotsell])));
				buyPrice = ((F1tradedata[0][tbuyprice] == null) ? 0.0 : Double.parseDouble(F1tradedata[0][tbuyprice]));
				sellPrice = ((F1tradedata[0][tsellprice] == null) ? 0.0 : Double.parseDouble(F1tradedata[0][tsellprice]));
				low =  ((F1tradedata[0][tlow] == null) ? 0.0 : Double.parseDouble(F1tradedata[0][tlow]));
				high =  ((F1tradedata[0][thigh] == null) ? 0.0 : Double.parseDouble(F1tradedata[0][thigh]));;
				Mpoint = ((F1tradedata[0][tmpoint] == null) ? 0.0 : Double.parseDouble(F1tradedata[0][tmpoint]));;
			}
			
			
		}
		catch(Exception ex)
		{
			Logger.error(ex);
		}
		
	}
	
	public void CalculateF1(String feedid, String tradeid, Double tickprice, Date ticktime)
	{
		try
		{
			
			if (TCount <= maxtradecount)
			{
				if (ticktime.after(t1))
				{
					if ((low == 0.0) && (high == 0.0))
		    	    {
		    	    	low = tickprice;
		    	    	high =tickprice;
		    	    	dbObj.executeNonQuery("UPDATE TBL_F1_TRADES SET LOW = "+low+" , HIGH ="+ high +" WHERE FEEDSUBJECTID='"+feedid+"' and TRADESUBJECTID ='"+tradeid+"' and ISBUYSELLDONE ='false'");
		    	    }
					if ((isBought==false)&&(isSell==false))
					{
						if(tickprice > high){high = tickprice;dbObj.executeNonQuery("UPDATE TBL_F1_TRADES  SET HIGH ="+ high +" WHERE FEEDSUBJECTID='"+feedid+"' and TRADESUBJECTID ='"+tradeid+"' and ISBUYSELLDONE ='false'");}
						if(tickprice < low) {low = tickprice;dbObj.executeNonQuery("UPDATE TBL_F1_TRADES  SET LOW ="+ low +" WHERE FEEDSUBJECTID='"+feedid+"' and TRADESUBJECTID ='"+tradeid+"' and ISBUYSELLDONE ='false'");}   
					}
					if (isBought == true)
			    	{
	    	    		if (tickprice > Mpoint + (Mpoint*(y/100)))
	    	    		{
	    	    			//sell command
	    	    			sellPrice = tickprice;
	    	    			fst2 = ticktime;
	    	    			Logger.info("long buy and sell Condition1 :"+ticktime);
	    	    			TCount =TCount +1; 
	    	    			if (istradeswitch ==true)
        	    			{
	    	    				if (bidvolume >= 1)
	        	    			{
	    	    					objPresto.PlaceOrder("omnesys","CM",tradeid,String.valueOf(tradeid) ,"22-02-2018","FA9749","1","0.0","0.0","na","0.0","MARKET","Presto_Mathsartz_Strategy","TestOrders","DAY","BUY");
	        	    			}
        	    			}
	    	    			dbObj.executeNonQuery("UPDATE TBL_F1_TRADES  SET EXITCONDITION='PROFIT',EXITTIME='"+ticktime.toString()+"',SELLPRICE ="+sellPrice+", Tcount="+TCount+", ISBUYSELLDONE = 'true' WHERE FEEDSUBJECTID='"+feedid+"' and TRADESUBJECTID ='"+tradeid+"'and ISBUYSELLDONE ='false'");
	    	    			calculatefigure(feedid,tradeid);
	    	    			//break;
	    	    		}
	    	    		else if(tickprice < Mpoint - (Mpoint*(z/100)))
	    	    		{
	    	    			//sell command
	    	    			sellPrice = tickprice;
	    	    			fst2 = ticktime;
	    	    			Logger.info("long buy and sell Condition2 :"+ticktime);
	    	    			TCount =TCount +1;
	    	    			if (istradeswitch ==true)
        	    			{
	    	    				if (bidvolume >= 1)
	        	    			{
	    	    					objPresto.PlaceOrder("omnesys","CM",tradeid,String.valueOf(tradeid) ,"22-02-2018","FA9749","1","0.0","0.0","na","0.0","MARKET","Presto_Mathsartz_Strategy","TestOrders","DAY","BUY");
	        	    			}
        	    			}
	    	    			dbObj.executeNonQuery("UPDATE TBL_F1_TRADES  SET EXITCONDITION='LOSS',EXITTIME='"+ticktime.toString()+"',SELLPRICE ="+sellPrice+", Tcount="+TCount+", ISBUYSELLDONE = 'true' WHERE FEEDSUBJECTID='"+feedid+"' and TRADESUBJECTID ='"+tradeid+"' and ISBUYSELLDONE ='false'");
	    	    			calculatefigure(feedid,tradeid);
	    	    			//break;
	    	    		}
	    	    		else if(ticktime.after(t4))
	    	    		{				    	    			
	    	    			//sell command
	    	    			sellPrice =tickprice;				    	    			
	    	    			fst2 = ticktime;
	    	    			Logger.info("long buy and sell Condition3:"+ticktime);
	    	    			TCount =TCount +1;
	    	    			if (istradeswitch ==true)
        	    			{
	    	    				if (bidvolume >= 1)
	        	    			{
	    	    					objPresto.PlaceOrder("omnesys","CM",tradeid,String.valueOf(tradeid) ,"22-02-2018","FA9749","1","0.0","0.0","na","0.0","MARKET","Presto_Mathsartz_Strategy","TestOrders","DAY","BUY");
	        	    			}
        	    			}
	    	    			dbObj.executeNonQuery("UPDATE TBL_F1_TRADES  SET EXITCONDITION='Validity Expires',EXITTIME='"+ticktime.toString()+"',SELLPRICE ="+sellPrice+", Tcount="+TCount+", ISBUYSELLDONE = 'true' WHERE FEEDSUBJECTID='"+feedid+"' and TRADESUBJECTID ='"+tradeid+"' and ISBUYSELLDONE ='false'");
	    	    			calculatefigure(feedid,tradeid);
	    	    			//break;	
	    	    		}
			    	    		
			    	}
					else if (isSell == true)
		    	    {
		        	    	if (tickprice < Mpoint - (Mpoint*(y/100)))
		    	    		{
		        	    		//buy command
		        	    		buyPrice =tickprice; 				        	    		
		        	    		fst2 = ticktime;
		        	    		TCount =TCount +1;
		        	    		Logger.info("short sell and buy Condition1 :"+ticktime);
		        	    		if (istradeswitch ==true)
	        	    			{
		        	    			if (askvolume >= 1)
		        	    			{
		        	    				objPresto.PlaceOrder("omnesys","CM",tradeid,String.valueOf(tradeid) ,"22-02-2018","FA9749","1","0.0","0.0","na","0.0","MARKET","Presto_Mathsartz_Strategy","TestOrders","DAY","BUY");
		        	    			}
	        	    			}
		        	    		dbObj.executeNonQuery("UPDATE TBL_F1_TRADES  SET EXITCONDITION='PROFIT',EXITTIME='"+ticktime.toString()+"',BUYPRICE ="+buyPrice+", Tcount="+TCount+", ISBUYSELLDONE = 'true' WHERE FEEDSUBJECTID='"+feedid+"' and TRADESUBJECTID ='"+tradeid+"' and ISBUYSELLDONE ='false'");
		        	    		calculatefigure(feedid,tradeid);
		    	    		}
		    	    		else if(tickprice > Mpoint + (Mpoint*(z/100)))
		    	    		{
		    	    			//buy command
		    	    			buyPrice =tickprice;				        	    		
		        	    		fst2 = ticktime;
		        	    		Logger.info("short sell and buy Condition2 :"+ticktime);
		        	    		TCount =TCount +1;
		        	    		if (istradeswitch ==true)
	        	    			{
		        	    			if (askvolume >= 1)
		        	    			{
		        	    				objPresto.PlaceOrder("omnesys","CM",tradeid,String.valueOf(tradeid) ,"22-02-2018","FA9749","1","0.0","0.0","na","0.0","MARKET","Presto_Mathsartz_Strategy","TestOrders","DAY","BUY");
		        	    			}
	        	    			}
		        	    		dbObj.executeNonQuery("UPDATE TBL_F1_TRADES  SET EXITCONDITION='LOSS',EXITTIME='"+ticktime.toString()+"',BUYPRICE ="+buyPrice+", Tcount="+TCount+", ISBUYSELLDONE = 'true' WHERE FEEDSUBJECTID='"+feedid+"' and TRADESUBJECTID ='"+tradeid+"' and ISBUYSELLDONE ='false'");
		        	    		calculatefigure(feedid,tradeid);
		    	    		}
		    	    		else if(ticktime.after(t4))
		    	    		{
		    	    			//buy command
		    	    			buyPrice =tickprice;				        	    		
		        	    		fst2 = ticktime;
		        	    		Logger.info("short sell and buy Condition3 :"+ticktime);
		        	    		TCount =TCount +1;
		        	    		if (istradeswitch ==true)
	        	    			{
		        	    			if (askvolume >= 1)
		        	    			{
		        	    				objPresto.PlaceOrder("omnesys","CM",tradeid,String.valueOf(tradeid) ,"22-02-2018","FA9749","1","0.0","0.0","na","0.0","MARKET","Presto_Mathsartz_Strategy","TestOrders","DAY","BUY");
		        	    			}
	        	    			}
		        	    		dbObj.executeNonQuery("UPDATE TBL_F1_TRADES  SET EXITCONDITION='Validity Expires',EXITTIME='"+ticktime.toString()+"',BUYPRICE ="+buyPrice+", Tcount="+TCount+", ISBUYSELLDONE = 'true' WHERE FEEDSUBJECTID='"+feedid+"' and TRADESUBJECTID ='"+tradeid+"' and ISBUYSELLDONE ='false'");
		    	    			calculatefigure(feedid,tradeid);
		    	    		}	
		    	    }
					else if ((isBought==false)&&(isSell==false))
		    	    {
		        	    double buylegprice = low + (low*(x/100)), sellegprice = high - (high*(x/100));
		        	    
			        	    if (tickprice > buylegprice)
			        	    {
			        	    	   
			        	    		fst1 = ticktime;
			        	    		if (fst1.after(t2))
			        	    		{
			        	    			//break;
			        	    			//goto end;
			        	    		}
			        	    		else {
			        	    			
				        	    			isShotsell = false;
				        	    			//buy command later
				        	    			buyPrice = tickprice;
				        	    			Mpoint = low + (low*(x/100));
				        	    			isBought=true;
				        	    			Logger.info("Long buy :"+ticktime);
				        	    			Logger.info("HIGH : "+high+", LOW : "+low);
				        	    			TCount=1;
				        	    			if (istradeswitch ==true)
				        	    			{
				        	    				if (askvolume >= 1)
					        	    			{
				        	    					objPresto.PlaceOrder("omnesys","CM",tradeid,String.valueOf(tradeid) ,"22-02-2018","FA9749","1","0.0","0.0","na","0.0","MARKET","Presto_Mathsartz_Strategy","TestOrders","DAY","BUY");
					        	    			}
					        	    		}
				        	    			dbObj.executeNonQuery("UPDATE TBL_F1_TRADES  SET ISSHOTSELL='"+isShotsell+"', ENTRYTIME = '"+ticktime.toString()+"',BUYPRICE="+buyPrice+", Mpoint="+Mpoint+", isBought='"+isBought+"',Tcount="+TCount+" WHERE FEEDSUBJECTID='"+feedid+"' and TRADESUBJECTID ='"+tradeid+"' and ISBUYSELLDONE ='false'");
				        	    			//executequery("update TBL_F1_TRADEINFO  set ISSHOTSELL='"+isShotsell+"', ENTRYTIME = '"+ticktime.toString()+"',BUYPRICE="+buyPrice+", Mpoint="+Mpoint+", isBought='"+isBought+"',Tcount="+TCount+" where symbol='"+instrument+"'");
			        	    			
			        	    		}
			        	    		
			        	    }
			        	    else if (tickprice < sellegprice)
			        	    {
			        	    		
			        	    		fst1 = ticktime;
			        	    		if (fst1.after(t2))
			        	    		{
			        	    			//isDataGot=true;
			        	    			//break;
			        	    			//goto end;
			        	    		}
			        	    		else {
			        	    			//sell command later
			        	    			
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
				        	    					objPresto.PlaceOrder("omnesys","CM",tradeid,String.valueOf(tradeid) ,"22-02-2018","FA9749","1","0.0","0.0","na","0.0","MARKET","Presto_Mathsartz_Strategy","TestOrders","DAY","BUY");
				        	    			
					        	    			}
				        	    			}
				        	    			dbObj.executeNonQuery("UPDATE TBL_F1_TRADES  SET ISSHOTSELL='"+isShotsell+"', ENTRYTIME='"+ticktime.toString()+"',SELLPRICE="+sellPrice+",Mpoint="+Mpoint+", isSell='"+isSell+"',Tcount="+TCount+" WHERE FEEDSUBJECTID='"+feedid+"' and TRADESUBJECTID ='"+tradeid+"' and ISBUYSELLDONE ='false'");
				        	    			//executequery("update TBL_F1_TRADEINFO set ISSHOTSELL='"+isShotsell+"', ENTRYTIME='"+ticktime.toString()+"',SELLPRICE="+sellPrice+",Mpoint="+Mpoint+", isSell='"+isSell+"',Tcount="+TCount+"  where symbol='"+instrument+"'");
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
			dbObj.executeNonQuery("UPDATE TBL_F1_TRADES  SET SELLPRICE="+sellPrice+", BUYPRICE ="+buyPrice+" WHERE FEEDSUBJECTID='"+feedid+"' and TRADESUBJECTID ='"+tradeid+"' and ISBUYSELLDONE ='true'");
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
			pldata = dbObj.getMultiColumnRecords("SELECT SUM(SELLPRICE) as \"TOTAL_SELL\",SUM(BUYPRICE) as \"TOTAL_BUY\",  SUM(TCOUNT) as \"TOTAL_TRADE\" FROM TBL_F1_TRADES  WHERE FEEDSUBJECTID='"+feedid+"' and TRADESUBJECTID='"+tradeid+"' and ISBUYSELLDONE='true';");
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
