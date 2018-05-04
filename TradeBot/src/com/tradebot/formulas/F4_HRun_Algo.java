package com.tradebot.formulas;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.pmw.tinylog.Logger;

import com.tradebot.dbcommons.db_commons;
import com.tradebot.dbcommons.tradebot_utility;
import com.tradebot.presto.presto_commons;

public class F4_HRun_Algo 
{
	public double x,y,z,C=0,LC=0,S=0;
	public Date t1,t2,t3,t4;
	public int maxtradecount =0, TCount=0, Lcount=0;
	public boolean isBought =false, isSell = false, isShotsell=false, istradeswitch=false;
	public double buyPrice,sellPrice,low=0.0,high=0.0,Mpoint,stopl=0.0;
	public Date fst1, fst2;
	final static String Fname="F4";
	private String configprop=System.getProperty("user.dir")+File.separator+"resource"+File.separator+"config.properties";
	String tradelogpath;
    tradebot_utility utils = new tradebot_utility();
    db_commons dbObj=new db_commons();
    int colid=0,colfeedid=1,coltradeid=2,colfname=3,colx=4,coly=5,colz=6,colt1=7,colt2=8,colt3=9,colt4=10,collount=11,colstopl=12,colotsize=13,coltradeswitch=14;
   
    int tColid=0, tfeedid=1, ttradeid=2, tentrytime=3, tbuyprice=4, tsellprice=5, texittime=6, tisshotsell=7, thigh=8,tlow=9, tisbought=10,
    		tissell=11, tmpoint =12, texitcon =13 , ttcount=14, tisbuyselldone=15, tentryid=16, texitid=17, tC=18, tLC=19,tS=20;
    public static SimpleDateFormat datefmt;
    
    public String tradeid , lasttradeprice, lasttradetime;
    public int askvolume, bidvolume;
    presto_commons objPresto;
    public Formula_Commons funcom=new Formula_Commons();
    int alreadyEntered = 0;
    public static String dbtable= "TBL_F4_HRUN_TRADES";

	public F4_HRun_Algo(presto_commons objconnect,  String feedid, double tradeprice, int asksize,int bidsize ,String tickdatetime) 
	{
		tradelogpath = utils.configlogfile("F4_LOG");
		askvolume = asksize;
		bidvolume = bidsize;		 
		objPresto =objconnect;
		 try
		 {
			 ArrayList<String> tradeplayers = dbObj.getSingleColumnRecords("SELECT TRADESECID FROM TBL_TRADERS WHERE FEEDSECID = '"+feedid+"';");	
			 datefmt=new SimpleDateFormat("yyyyMMdd HH:mm:ss");
			 for(int i =0; i < tradeplayers.size(); i++)
			 {
				 if ((dbObj.getRowCount("SELECT * FROM TBL_FORMULA WHERE FEEDSECID ='"+feedid+"' AND TRADESECID ='"+tradeplayers.get(i)+"'") !=0 )&&(dbObj.getSingleCell("SELECT ISEND FROM TBL_FORMULA WHERE FEEDSECID ='"+feedid+"' AND TRADESECID ='"+tradeplayers.get(i)+"' AND FORMULANAME ='"+Fname+"'").equals("false")))
				 {
					 if (dbObj.getRowCount("SELECT * FROM TBL_F4_HRUN_TRADES  WHERE FEEDSECID='"+feedid+"' and TRADESECID='"+tradeplayers.get(i)+"'") == 0)// and ISBUYSELLDONE ='false'
					 {
						 dbObj.executeNonQuery("insert into TBL_F4_HRUN_TRADES (FEEDSECID, TRADESECID,ENTRYTIME,BUYPRICE,SELLPRICE,EXITTIME,ISSHOTSELL,HIGH,LOW,ISBOUGHT, ISSELL,MPOINT,EXITCONDITION,TCOUNT,ISBUYSELLDONE,ENTRYID,EXITID,C,LC,S) values "
						 		+ "('"+feedid+"', '"+tradeplayers.get(i)+"', null, 0.0,0.0,null,'false',0.0,0.0,'false','false',0.0,null,0,'false',null,null,0,0,0)");
					 }
					 if((dbObj.getRowCount("SELECT * FROM TBL_F4_HRUN_TRADES  WHERE FEEDSECID='"+feedid+"' and TRADESECID='"+tradeplayers.get(i)+"' and ISBUYSELLDONE ='true'") > 0) && (dbObj.getRowCount("SELECT * FROM TBL_F4_HRUN_TRADES  WHERE FEEDSECID='"+feedid+"' and TRADESECID='"+tradeplayers.get(i)+"' and ISBUYSELLDONE ='false'") != 1 ))
					 {
						String[][] lasttransac =  dbObj.getMultiColumnRecords("SELECT high, low, mpoint, C, LC, S FROM TBL_F4_HRUN_TRADES WHERE id = (SELECT MAX(id) FROM TBL_F4_HRUN_TRADES ) and FEEDSECID='"+feedid+"' and TRADESECID='"+tradeplayers.get(i)+"';");
						
						dbObj.executeNonQuery("insert into TBL_F4_HRUN_TRADES (FEEDSECID, TRADESECID,ENTRYTIME,BUYPRICE,SELLPRICE,EXITTIME,ISSHOTSELL,HIGH,LOW,ISBOUGHT, ISSELL,MPOINT,EXITCONDITION,TCOUNT,ISBUYSELLDONE,ENTRYID,EXITID,C,LC,S) values "
						 		+ "('"+feedid+"', '"+tradeplayers.get(i)+"', null, 0.0,0.0,null,'false',"+Double.parseDouble(lasttransac[0][0])+","+Double.parseDouble(lasttransac[0][1])+",'false','false',"+Double.parseDouble(lasttransac[0][2])+",null,0,'false',null,null,"+Integer.parseInt(lasttransac[0][3])+","+Integer.parseInt(lasttransac[0][4])+","+Integer.parseInt(lasttransac[0][5])+")");
					
					 }
					 assginF1Variables(feedid, tradeplayers.get(i),tickdatetime);
					 CalculateHRun(feedid, tradeplayers.get(i),Double.valueOf(tradeprice), datefmt.parse(tickdatetime));
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
	
  	public void assginF1Variables(String feedid, String tradeid, String livedate)
	{
		String [][] F1inputdata;
		String [][] F1tradedata;
		try
		{
			F1inputdata = dbObj.getMultiColumnRecords("SELECT * FROM TBL_FORMULA WHERE FEEDSECID='"+feedid+"' and TRADESECID='"+tradeid+"' and FORMULANAME ='"+Fname+"'");
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
			F1tradedata = dbObj.getMultiColumnRecords("SELECT * FROM TBL_F4_HRUN_TRADES where FEEDSECID='"+feedid+"' and TRADESECID='"+tradeid+"' and ISBUYSELLDONE='false'");
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
				TCount = ((F1tradedata[0][ttcount] == null) ? 0 : Integer.parseInt(F1tradedata[0][ttcount]));
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
				alreadyEntered = dbObj.getRowCount("SELECT * FROM TBL_F4_HRUN_TRADES WHERE FEEDSECID = '"+feedid+"' and TRADESECID = '"+tradeid+"' and ISBUYSELLDONE ='true'");
				if (isBought == true)
				{
					// Box 2
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
    	    					orderid = funcom.LoadDataandOrder(objPresto,feedid, tradeid, "SELL");
    	    					dbObj.executeNonQuery("UPDATE TBL_F4_HRUN_TRADES  SET EXITCONDITION='N/A',EXITTIME='"+ticktime.toString()+"',SELLPRICE ="+sellPrice+", "
    	    							+ "Tcount="+TCount+", ISBUYSELLDONE = 'true',LOW ="+low+", HIGH="+high+", Mpoint="+Mpoint+",C="+C+", LC="+LC+", S="+S+" , EXITID='"+orderid+"' WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"' and ISBUYSELLDONE ='false'");
    	    					Logger.info("Trade Switch is ON, Order Palced : "+orderid);
    	    				}
    	    			}
    	    			else 
    	    			{
    	    				dbObj.executeNonQuery("UPDATE TBL_F4_HRUN_TRADES  SET EXITCONDITION='N/A',EXITTIME='"+ticktime.toString()+"',SELLPRICE ="+sellPrice+", Tcount="+TCount+", ISBUYSELLDONE = 'true', LOW ="+low+", HIGH="+high+", Mpoint="+Mpoint+",C="+C+", LC="+LC+", S="+S+" WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"' and ISBUYSELLDONE ='false'");
    	    			}
    	    			funcom.calculatefigure(sellPrice, buyPrice, feedid,tradeid, Fname,dbtable);
    	    			//Ending Execution for HEAD FEED
    	    			dbObj.executeNonQuery("UPDATE TBL_FORMULA  SET ISEND='true' WHERE FEEDSECID='"+feedid+"' AND TRADESECID='"+tradeid+"' AND FORMULANAME = '"+Fname+"';"); 
    	    			Logger.info("Head Feed - "+feedid+" & Trade ID - "+tradeid+" && Formula Name - "+Fname+" Achived End point.");
					}
					// Box 3
					else if(tickprice < Mpoint - (Mpoint*(y/100)))
					{
						String orderid = null;
						sellPrice =tickprice;				    	    			
    	    			fst2 = ticktime;
    	    			Logger.info("long buy and sell Condition3:"+ticktime);
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
        	    			dbObj.executeNonQuery("UPDATE TBL_FORMULA  SET ISEND='true' WHERE FEEDSECID='"+feedid+"' AND TRADESECID='"+tradeid+"' AND FORMULANAME = '"+Fname+"';"); 
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
        	    			dbObj.executeNonQuery("UPDATE TBL_FORMULA  SET ISEND='true' WHERE FEEDSECID='"+feedid+"' AND TRADESECID='"+tradeid+"' AND FORMULANAME = '"+Fname+"';"); 
        	    			Logger.info("Head Feed - "+feedid+" & Trade ID - "+tradeid+" && Formula Name - "+Fname+" Achived End point.");
    	    			}	
    	    			else
    	    			{
    	    				C=0;
    	    			}
    	    			if (istradeswitch ==true)
    	    			{
    	    				if (bidvolume >= 1)
        	    			{
    	    					orderid = funcom.LoadDataandOrder(objPresto,feedid, tradeid, "SELL");
    	    					dbObj.executeNonQuery("UPDATE TBL_F4_HRUN_TRADES  SET EXITCONDITION='N/A',EXITTIME='"+ticktime.toString()+"',SELLPRICE ="+sellPrice+", "
    	    							+ "Tcount="+TCount+", ISBUYSELLDONE = 'true' , LOW ="+low+", HIGH="+high+", Mpoint="+Mpoint+",C="+C+", LC="+LC+", S="+S+",  EXITID='"+orderid+"' WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"' and ISBUYSELLDONE ='false'");
    	    					Logger.info("Trade Switch is ON, Order Palced : "+orderid);
    	    				}
    	    			}
    	    			else 
    	    			{
    	    				dbObj.executeNonQuery("UPDATE TBL_F4_HRUN_TRADES  SET EXITCONDITION='N/A',EXITTIME='"+ticktime.toString()+"',SELLPRICE ="+sellPrice+", Tcount="+TCount+", ISBUYSELLDONE = 'true', LOW ="+low+", HIGH="+high+", Mpoint="+Mpoint+",C="+C+", LC="+LC+", S="+S+" WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"' and ISBUYSELLDONE ='false'");
    	    			}
    	    			funcom.calculatefigure(sellPrice, buyPrice, feedid,tradeid, Fname,dbtable);
    	    			//calculatefigure(feedid,tradeid);	
					}
					// Box 4
					else if(tickprice >= Mpoint + (Mpoint*(z/100)))
					{
						Mpoint= Mpoint + (Mpoint*(z/100));
						C=C+1;
						dbObj.executeNonQuery("UPDATE TBL_F4_HRUN_TRADES SET LOW ="+low+", HIGH="+high+", Mpoint="+Mpoint+",C="+C+", LC="+LC+", S="+S+" WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"' and ISBUYSELLDONE ='false'");
    	    			
					}
					
				}
				else if (isSell == true)
	    	    {
					if (tickprice <= (Mpoint - (Mpoint*(z/100))))
					{
						Mpoint = Mpoint - (Mpoint*(z/100));
						C=C+1;
						dbObj.executeNonQuery("UPDATE TBL_F4_HRUN_TRADES SET LOW ="+low+", HIGH="+high+", Mpoint="+Mpoint+",C="+C+", LC="+LC+", S="+S+" WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"' and ISBUYSELLDONE ='false'");
					}
					else if(tickprice > (Mpoint + (Mpoint*(y/100))))
					{
						String orderid = null;
						buyPrice =tickprice;				    	    			
    	    			fst2 = ticktime;
    	    			Logger.info("short sell buy Condition2:"+ticktime);
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
        	    			dbObj.executeNonQuery("UPDATE TBL_FORMULA  SET ISEND='true' WHERE FEEDSECID='"+feedid+"' AND TRADESECID='"+tradeid+"' AND FORMULANAME = '"+Fname+"';"); 
        	    			Logger.info("Head Feed - "+feedid+" & Trade ID - "+tradeid+" && Formula Name - "+Fname+" Achived End point.");
    	    			}
    	    			else
    	    			{
    	    				S = S + 1;
    	    			}
    	    			if(S == stopl)
    	    			{
    	    				
    	    				dbObj.executeNonQuery("UPDATE TBL_FORMULA  SET ISEND='true' WHERE FEEDSECID='"+feedid+"' AND TRADESECID='"+tradeid+"' AND FORMULANAME = '"+Fname+"';"); 
        	    			Logger.info("Head Feed - "+feedid+" & Trade ID - "+tradeid+" && Formula Name - "+Fname+" Achived End point.");
    	    			}
    	    			else
    	    			{
    	    				C =0;
    	    			}
    	    			
    	    			if (istradeswitch ==true)
    	    			{
    	    				if (askvolume >= 1)
        	    			{
    	    					orderid = funcom.LoadDataandOrder(objPresto, feedid, tradeid, "BUY");
    	    					dbObj.executeNonQuery("UPDATE TBL_F4_HRUN_TRADES  SET EXITCONDITION='N/A',EXITTIME='"+ticktime.toString()+"',BUYPRICE ="+buyPrice+", "
    	    							+ "Tcount="+TCount+", ISBUYSELLDONE = 'true' , LOW ="+low+", HIGH="+high+", Mpoint="+Mpoint+",C="+C+", LC="+LC+", S="+S+",  EXITID='"+orderid+"' WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"' and ISBUYSELLDONE ='false'");
    	    					Logger.info("Trade Switch is ON, Order Palced : "+orderid);
    	    				}
    	    			}
    	    			else 
    	    			{
    	    				dbObj.executeNonQuery("UPDATE TBL_F4_HRUN_TRADES  SET EXITCONDITION='N/A',EXITTIME='"+ticktime.toString()+"',BUYPRICE ="+buyPrice+", Tcount="+TCount+", ISBUYSELLDONE = 'true', LOW ="+low+", HIGH="+high+", Mpoint="+Mpoint+",C="+C+", LC="+LC+", S="+S+" WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"' and ISBUYSELLDONE ='false'");
    	    			}
    	    			funcom.calculatefigure(sellPrice, buyPrice, feedid,tradeid, Fname,dbtable);
    	    			//calculatefigure(feedid,tradeid);
					}
					else if(ticktime.after(t4))
					{
						String orderid = null;
						buyPrice =tickprice;				    	    			
    	    			fst2 = ticktime;
    	    			Logger.info("short buy Condition3:"+ticktime);
    	    			TCount =TCount +1;
    	    			if (istradeswitch ==true)
    	    			{
    	    				if (askvolume >= 1)
        	    			{
    	    					orderid = funcom.LoadDataandOrder(objPresto, feedid, tradeid, "BUY");
    	    					dbObj.executeNonQuery("UPDATE TBL_F4_HRUN_TRADES  SET EXITCONDITION='N/A',EXITTIME='"+ticktime.toString()+"',BUYPRICE ="+buyPrice+", "
    	    							+ "Tcount="+TCount+", ISBUYSELLDONE = 'true',LOW ="+low+", HIGH="+high+", Mpoint="+Mpoint+",C="+C+", LC="+LC+", S="+S+" , EXITID='"+orderid+"' WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"' and ISBUYSELLDONE ='false'");
    	    					Logger.info("Trade Switch is ON, Order Palced : "+orderid);
    	    				}
    	    			}
    	    			else 
    	    			{
    	    				dbObj.executeNonQuery("UPDATE TBL_F4_HRUN_TRADES  SET EXITCONDITION='N/A',EXITTIME='"+ticktime.toString()+"',BUYPRICE ="+buyPrice+", Tcount="+TCount+", ISBUYSELLDONE = 'true', LOW ="+low+", HIGH="+high+", Mpoint="+Mpoint+",C="+C+", LC="+LC+", S="+S+"  WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"' and ISBUYSELLDONE ='false'");
    	    			}
    	    			funcom.calculatefigure(sellPrice, buyPrice, feedid,tradeid, Fname,dbtable);
    	    			//Ending Execution for HEAD FEED
    	    			dbObj.executeNonQuery("UPDATE TBL_FORMULA  SET ISEND='true' WHERE FEEDSECID='"+feedid+"' AND TRADESECID='"+tradeid+"' AND FORMULANAME = '"+Fname+"';"); 
    	    			Logger.info("Head Feed - "+feedid+" & Trade ID - "+tradeid+" && Formula Name - "+Fname+" Achived End point.");
					}
	    	    }
				// branching Box 1  buyleg or shortleg box 1
				else if ((isBought==false)&&(isSell==false)&&(alreadyEntered==0))
	    	    {
					if ((low == 0.0) && (high == 0.0))
		    	    {
		    	    	low = tickprice;
		    	    	high =tickprice;
		    	    	dbObj.executeNonQuery("UPDATE TBL_F4_HRUN_TRADES SET LOW = "+low+" , HIGH ="+ high +""
		    	    			+ " WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"' and ISBUYSELLDONE ='false'");
		    	    }
					if(tickprice > high){high = tickprice;dbObj.executeNonQuery("UPDATE TBL_F4_HRUN_TRADES  SET HIGH ="+ high +" WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"' and ISBUYSELLDONE ='false'");}
					if(tickprice < low){low = tickprice;dbObj.executeNonQuery("UPDATE TBL_F4_HRUN_TRADES  SET LOW ="+ low +" WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"' and ISBUYSELLDONE ='false'");} 
					double buylegprice = low + (low*(x/100)), sellingprice = high - (high*(x/100));
					// buy leg box 1
					if (tickprice > buylegprice)
	        	    {
						fst1 = ticktime;
        	    		if (fst1.after(t2))
        	    		{
        	    			//Ending Execution for HEAD FEED
        	    			dbObj.executeNonQuery("UPDATE TBL_FORMULA  SET ISEND='true' WHERE FEEDSECID='"+feedid+"' AND TRADESECID='"+tradeid+"' AND FORMULANAME = '"+Fname+"';"); 
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
        	    					orderid = funcom.LoadDataandOrder(objPresto, feedid, tradeid, "BUY");
        	    					dbObj.executeNonQuery("UPDATE TBL_F4_HRUN_TRADES  SET ENTRYTIME = '"+ticktime.toString()+"',"
        	    							+ "BUYPRICE="+buyPrice+",ISSHOTSELL='"+isShotsell+"',  isBought='"+isBought+"', LOW ="+low+", HIGH="+high+", Mpoint="+Mpoint+",C="+C+", LC="+LC+", S="+S+" ,Tcount="+TCount+", ENTRYID='"+orderid+"' WHERE FEEDSECID='"+feedid+"'"
        	    									+ " and TRADESECID ='"+tradeid+"' and ISBUYSELLDONE ='false'");   
        	    					Logger.info("Trade Switch is ON, Order Palced : "+orderid);
        	    				}
	        	    		}
        	    			else
        	    			{
        	    				Logger.info("Trade Switch is OFF, paper order");
        	    				dbObj.executeNonQuery("UPDATE TBL_F4_HRUN_TRADES  SET ENTRYTIME = '"+ticktime.toString()+"',"
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
        	    			dbObj.executeNonQuery("UPDATE TBL_FORMULA  SET ISEND='true' WHERE FEEDSECID='"+feedid+"' AND TRADESECID='"+tradeid+"' AND FORMULANAME = '"+Fname+"';"); 
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
	        	    					orderid = funcom.LoadDataandOrder(objPresto, feedid, tradeid, "SELL");
	        	    					dbObj.executeNonQuery("UPDATE TBL_F4_HRUN_TRADES  SET ISSHOTSELL='"+isShotsell+"', ENTRYTIME='"+ticktime.toString()+"',SELLPRICE="+sellPrice+","
	    	        	    					+ " isSell='"+isSell+"',Tcount="+TCount+",LOW ="+low+", HIGH="+high+", Mpoint="+Mpoint+",C="+C+", LC="+LC+", S="+S+" , ENTRYID='"+orderid+"' WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"' "
	    	        	    							+ "and ISBUYSELLDONE ='false'");   
	        	    					Logger.info("Trade Switch is ON, Order Palced : "+orderid);
		        	    			}
	        	    			}
	        	    			else
	        	    			{
	        	    			dbObj.executeNonQuery("UPDATE TBL_F4_HRUN_TRADES  SET ISSHOTSELL='"+isShotsell+"', ENTRYTIME='"+ticktime.toString()+"',SELLPRICE="+sellPrice+","
	        	    					+ " isSell='"+isSell+"',Tcount="+TCount+", LOW ="+low+", HIGH="+high+", Mpoint="+Mpoint+",C="+C+", LC="+LC+", S="+S+" WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"' "
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
        	    			dbObj.executeNonQuery("UPDATE TBL_FORMULA  SET ISEND='true' WHERE FEEDSECID='"+feedid+"' AND TRADESECID='"+tradeid+"' AND FORMULANAME = '"+Fname+"';"); 
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
        	    					orderid = funcom.LoadDataandOrder(objPresto, feedid, tradeid, "BUY");
        	    					dbObj.executeNonQuery("UPDATE TBL_F4_HRUN_TRADES  SET ENTRYTIME = '"+ticktime.toString()+"',"
        	    							+ "BUYPRICE="+buyPrice+",ISSHOTSELL='"+isShotsell+"',  isBought='"+isBought+"', LOW ="+low+", HIGH="+high+", Mpoint="+Mpoint+",C="+C+", LC="+LC+", S="+S+" ,Tcount="+TCount+", ENTRYID='"+orderid+"' WHERE FEEDSECID='"+feedid+"'"
        	    									+ " and TRADESECID ='"+tradeid+"' and ISBUYSELLDONE ='false'");   
        	    					Logger.info("Trade Switch is ON, Order Palced : "+orderid);
        	    				}
	        	    		}
        	    			else
        	    			{
        	    				Logger.info("Trade Switch is OFF, paper order");
        	    				dbObj.executeNonQuery("UPDATE TBL_F4_HRUN_TRADES  SET ENTRYTIME = '"+ticktime.toString()+"',"
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
        	    			dbObj.executeNonQuery("UPDATE TBL_FORMULA  SET ISEND='true' WHERE FEEDSECID='"+feedid+"' AND TRADESECID='"+tradeid+"' AND FORMULANAME = '"+Fname+"';"); 
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
	        	    					orderid = funcom.LoadDataandOrder(objPresto, feedid, tradeid, "SELL");
	        	    					dbObj.executeNonQuery("UPDATE TBL_F4_HRUN_TRADES  SET ISSHOTSELL='"+isShotsell+"', ENTRYTIME='"+ticktime.toString()+"',SELLPRICE="+sellPrice+","
	    	        	    					+ " isSell='"+isSell+"',Tcount="+TCount+",LOW ="+low+", HIGH="+high+", Mpoint="+Mpoint+",C="+C+", LC="+LC+", S="+S+" , ENTRYID='"+orderid+"' WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"' "
	    	        	    							+ "and ISBUYSELLDONE ='false'");   
	        	    					Logger.info("Trade Switch is ON, Order Palced : "+orderid);
		        	    			}
	        	    			}
	        	    			else
	        	    			{
	        	    			dbObj.executeNonQuery("UPDATE TBL_F4_HRUN_TRADES  SET ISSHOTSELL='"+isShotsell+"', ENTRYTIME='"+ticktime.toString()+"',SELLPRICE="+sellPrice+","
	        	    					+ " isSell='"+isSell+"',Tcount="+TCount+", LOW ="+low+", HIGH="+high+", Mpoint="+Mpoint+",C="+C+", LC="+LC+", S="+S+" WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"' "
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
}
