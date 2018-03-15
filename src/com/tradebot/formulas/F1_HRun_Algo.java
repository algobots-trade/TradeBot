package com.tradebot.formulas;

import java.io.File;
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
    presto_commons objPresto;

	public F1_HRun_Algo(presto_commons objconnect,  String feedid, double tradeprice, int asksize,int bidsize ,String tickdatetime) 
	{
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
	public void loadandOrder(String feedid, String tradeid, String order)
	{
		try
		{
			objPresto.PlaceOrder("omnesys","CM",tradeid,String.valueOf(tradeid) ,"22-02-2018","FA9749","1","0.0","0.0","na","0.0","MARKET","Presto_Mathsartz_Strategy","TestOrders","DAY","BUY");
			String ESB_EXCHANGE, SECURITY_TYPE, ESB_SYMBOL, ESB_SECURITYID,
			 EXP_DATE, ESB_ACCOUNT, QUAN_TITY, T_PRICE, STOP_PRICE,	
			 ESB_OPTIONTYPE, ESB_STRIKEPPRICE, ORDER_TYPE, INSTANCEID_CUSTOMFIELD, T_REMARK,
			 TIME_INFORCE, T_SIDE;
			
			ESB_EXCHANGE = "omnesys";
			SECURITY_TYPE = "";
			
		}
		catch(Exception ex)
		{
			Logger.error(ex);
		}
		
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
	    	    	dbObj.executeNonQuery("UPDATE TBL_F1_HRUN_TRADES SET LOW = "+low+" , HIGH ="+ high +" WHERE FEEDSUBJECTID='"+feedid+"' and TRADESUBJECTID ='"+tradeid+"' and ISBUYSELLDONE ='false'");
	    	    }
				if ((isBought==false)&&(isSell==false))
				{
					if(tickprice > high){high = tickprice;dbObj.executeNonQuery("UPDATE TBL_F1_HRUN_TRADES  SET HIGH ="+ high +" WHERE FEEDSUBJECTID='"+feedid+"' and TRADESUBJECTID ='"+tradeid+"' and ISBUYSELLDONE ='false'");}
					if(tickprice < low) {low = tickprice;dbObj.executeNonQuery("UPDATE TBL_F1_HRUN_TRADES  SET LOW ="+ low +" WHERE FEEDSUBJECTID='"+feedid+"' and TRADESUBJECTID ='"+tradeid+"' and ISBUYSELLDONE ='false'");}   
				}
				if (isBought == true)
				{
					 
				}
				else if (isSell == true)
	    	    {
					
	    	    }
				else if ((isBought==false)&&(isSell==false))
	    	    {
					double buylegprice = low + (low*(x/100)), sellingprice = high - (high*(x/100));
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

}
