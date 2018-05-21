package com.tradebot.formulas;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.pmw.tinylog.Logger;

import com.tradebot.dbcommons.db_commons;
import com.tradebot.dbcommons.tradebot_utility;
import com.tradebot.presto.presto_commons;

import sun.util.locale.provider.LocaleServiceProviderPool.LocalizedObjectGetter;

import java.sql.Connection;

public class F1_HRun_Algo 
{
	public double x,y,z,C=0,LC=0,S=0;
	public Date t1,t2,t3,t4;
	public int maxtradecount =0, TCount=0, Lcount=0;
	public boolean isBought =false, isSell = false, isShotsell=false, istradeswitch=false;
	public double buyPrice,sellPrice,low=0.0,high=0.0,Mpoint,stopl=0.0;
	public Date fst1, fst2;
	final static String Fname="F1";
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
    public static String dbtable= "TBL_F1_HRUN_TRADES";
    public Connection conn;

	public F1_HRun_Algo(Connection connect,  presto_commons objconnect,  String feedid, double tradeprice, int asksize,int bidsize ,String tickdatetime) 
	{
		conn =connect;
		tradelogpath = utils.configlogfile("F1_LOG");
		askvolume = asksize;
		bidvolume = bidsize;		 
		objPresto =objconnect;
		 try
		 {
			 ArrayList<String> tradeplayers = dbObj.getSingleColumnRecords(conn,"SELECT TRADESECID FROM TBL_TRADERS WHERE FEEDSECID = '"+feedid+"';");	
			 datefmt=new SimpleDateFormat("yyyyMMdd HH:mm:ss");
			 for(int i =0; i < tradeplayers.size(); i++)
			 {
				 if ((dbObj.getRowCount(conn,"SELECT * FROM TBL_FORMULA WHERE FEEDSECID ='"+feedid+"' AND TRADESECID ='"+tradeplayers.get(i)+"' AND FORMULANAME ='"+Fname+"'") !=0 )&&(dbObj.getSingleCell(conn,"SELECT ISEND FROM TBL_FORMULA WHERE FEEDSECID ='"+feedid+"' AND TRADESECID ='"+tradeplayers.get(i)+"' AND FORMULANAME ='"+Fname+"'").equals("false")))
				 {
					 if (dbObj.getRowCount(conn,"SELECT * FROM TBL_F1_HRUN_TRADES  WHERE FEEDSECID='"+feedid+"' and TRADESECID='"+tradeplayers.get(i)+"'") == 0)
					 {
						 dbObj.executeNonQuery(conn,"insert into TBL_F1_HRUN_TRADES (FEEDSECID, TRADESECID,ENTRYTIME,BUYPRICE,SELLPRICE,EXITTIME,ISSHOTSELL,HIGH,LOW,ISBOUGHT, ISSELL,MPOINT,EXITCONDITION,TCOUNT,ISBUYSELLDONE,ENTRYID,EXITID,C,LC,S) values "
						 		+ "('"+feedid+"', '"+tradeplayers.get(i)+"', null, 0.0,0.0,null,'false',0.0,0.0,'false','false',0.0,null,0,'false',null,null,0,0,0)");
					 }
					 if((dbObj.getRowCount(conn,"SELECT * FROM TBL_F1_HRUN_TRADES  WHERE FEEDSECID='"+feedid+"' and TRADESECID='"+tradeplayers.get(i)+"' and ISBUYSELLDONE ='true'") > 0) && (dbObj.getRowCount(conn,"SELECT * FROM TBL_F1_HRUN_TRADES  WHERE FEEDSECID='"+feedid+"' and TRADESECID='"+tradeplayers.get(i)+"' and ISBUYSELLDONE ='false'") != 1 ))
					 {
						String[][] lasttransac =  dbObj.getMultiColumnRecords(conn,"SELECT high, low, mpoint, C, LC, S FROM TBL_F1_HRUN_TRADES WHERE id = (SELECT MAX(id) FROM TBL_F1_HRUN_TRADES WHERE FEEDSECID='"+feedid+"' and TRADESECID='"+tradeplayers.get(i)+"') and FEEDSECID='"+feedid+"' and TRADESECID='"+tradeplayers.get(i)+"';");
						
						dbObj.executeNonQuery(conn,"insert into TBL_F1_HRUN_TRADES (FEEDSECID, TRADESECID,ENTRYTIME,BUYPRICE,SELLPRICE,EXITTIME,ISSHOTSELL,HIGH,LOW,ISBOUGHT, ISSELL,MPOINT,EXITCONDITION,TCOUNT,ISBUYSELLDONE,ENTRYID,EXITID,C,LC,S) values "
						 		+ "('"+feedid+"', '"+tradeplayers.get(i)+"', null, 0.0,0.0,null,'false',"+Double.parseDouble(lasttransac[0][0])+","+Double.parseDouble(lasttransac[0][1])+",'false','false',"+Double.parseDouble(lasttransac[0][2])+",null,0,'false',null,null,"+Integer.parseInt(lasttransac[0][3])+","+Integer.parseInt(lasttransac[0][4])+","+Integer.parseInt(lasttransac[0][5])+")");
					
					 }
					 assginVariables(feedid, tradeplayers.get(i),tickdatetime);
					 algotrade(feedid, tradeplayers.get(i),Double.valueOf(tradeprice), datefmt.parse(tickdatetime));
					
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
	
  	public void assginVariables(String feedid, String tradeid, String livedate)
	{
		String [][] Formulainputdata;
		String [][] tradedata;
		try
		{
			Formulainputdata = dbObj.getMultiColumnRecords(conn,"SELECT * FROM TBL_FORMULA WHERE FEEDSECID='"+feedid+"' and TRADESECID='"+tradeid+"' and FORMULANAME ='"+Fname+"'");
			if (Formulainputdata !=null)
			{
				x = ((Formulainputdata[0][colx] == null) ? 0.0 : Double.parseDouble(Formulainputdata[0][colx]));
				y = ((Formulainputdata[0][coly] == null) ? 0.0 : Double.parseDouble(Formulainputdata[0][coly]));
				z = ((Formulainputdata[0][colz] == null) ? 0.0 : Double.parseDouble(Formulainputdata[0][colz]));
				datefmt=new SimpleDateFormat("yyyyMMdd HH:mm:ss");
				t1 = ((Formulainputdata[0][colt1] == null) ? null : datefmt.parse(livedate.split(" ")[0]+" "+Formulainputdata[0][colt1]));
				t2 = ((Formulainputdata[0][colt2] == null) ? null : datefmt.parse(livedate.split(" ")[0]+" "+Formulainputdata[0][colt2]));
				t3 = ((Formulainputdata[0][colt3] == null) ? null : datefmt.parse(livedate.split(" ")[0]+" "+Formulainputdata[0][colt3]));
				t4 = ((Formulainputdata[0][colt4] == null) ? null : datefmt.parse(livedate.split(" ")[0]+" "+Formulainputdata[0][colt4]));
				Lcount = ((Formulainputdata[0][collount] == null ? null : Integer.parseInt(Formulainputdata[0][collount])));
				stopl=((Formulainputdata[0][colstopl] == null ? 0.0 : Double.parseDouble(Formulainputdata[0][colstopl])));
				istradeswitch=((Formulainputdata[0][coltradeswitch] == null ? false : Boolean.parseBoolean(Formulainputdata[0][coltradeswitch])));
			}
			
			tradedata = dbObj.getMultiColumnRecords(conn,"SELECT * FROM TBL_F1_HRUN_TRADES where id = (SELECT MAX(id) FROM TBL_F1_HRUN_TRADES WHERE FEEDSECID='"+feedid+"' and TRADESECID='"+tradeid+"') and FEEDSECID='"+feedid+"' and TRADESECID='"+tradeid+"' and ISBUYSELLDONE='false'");
			
			if (tradedata != null)
			{
				isBought=((tradedata[0][tisbought] == null ? false : Boolean.parseBoolean(tradedata[0][tisbought])));
				isSell=((tradedata[0][tissell] == null ? false : Boolean.parseBoolean(tradedata[0][tissell])));
				isShotsell=((tradedata[0][tisshotsell] == null ? false : Boolean.parseBoolean(tradedata[0][tisshotsell])));
				buyPrice = ((tradedata[0][tbuyprice] == null) ? 0.0 : Double.parseDouble(tradedata[0][tbuyprice]));
				sellPrice = ((tradedata[0][tsellprice] == null) ? 0.0 : Double.parseDouble(tradedata[0][tsellprice]));
				low =  ((tradedata[0][tlow] == null) ? 0.0 : Double.parseDouble(tradedata[0][tlow]));
				high =  ((tradedata[0][thigh] == null) ? 0.0 : Double.parseDouble(tradedata[0][thigh]));
				Mpoint = ((tradedata[0][tmpoint] == null) ? 0.0 : Double.parseDouble(tradedata[0][tmpoint]));
				C = ((tradedata[0][tC] == null) ? 0.0 : Integer.parseInt(tradedata[0][tC]));
				LC= ((tradedata[0][tLC] == null) ? 0.0 : Integer.parseInt(tradedata[0][tLC]));
				S= ((tradedata[0][tS] == null) ? 0.0 : Integer.parseInt(tradedata[0][tS]));
				TCount = ((tradedata[0][ttcount] == null) ? 0 : Integer.parseInt(tradedata[0][ttcount]));
			}
			
		}
		catch(Exception ex)
		{
			Logger.error(ex);
		}
		
	}
	
	public void algotrade(String feedid, String tradeid, Double tickprice, Date ticktime)
	{
		try
		{
			if (ticktime.after(t1))
			{
				alreadyEntered = dbObj.getRowCount(conn,"SELECT * FROM TBL_F1_HRUN_TRADES WHERE FEEDSECID = '"+feedid+"' and TRADESECID = '"+tradeid+"' and ISBUYSELLDONE ='true'");
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
    	    					orderid = funcom.LoadDataandOrder(conn,objPresto,feedid, tradeid, "SELL");
    	    					dbObj.executeNonQuery(conn,"UPDATE TBL_F1_HRUN_TRADES  SET EXITCONDITION='N/A',EXITTIME='"+ticktime.toString()+"',SELLPRICE ="+sellPrice+", "
    	    							+ "Tcount="+TCount+", ISBUYSELLDONE = 'true',LOW ="+low+", HIGH="+high+", Mpoint="+Mpoint+",C="+C+", LC="+LC+", S="+S+" , EXITID='"+orderid+"' WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"' and ISBUYSELLDONE ='false'");
    	    					Logger.info("Trade Switch is ON, Order Palced : "+orderid);
    	    				}
    	    			}
    	    			else 
    	    			{
    	    				dbObj.executeNonQuery(conn,"UPDATE TBL_F1_HRUN_TRADES  SET EXITCONDITION='N/A',EXITTIME='"+ticktime.toString()+"',SELLPRICE ="+sellPrice+", Tcount="+TCount+", ISBUYSELLDONE = 'true', LOW ="+low+", HIGH="+high+", Mpoint="+Mpoint+",C="+C+", LC="+LC+", S="+S+" WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"' and ISBUYSELLDONE ='false'");
    	    			}
    	    			funcom.calculatefigure(conn,sellPrice, buyPrice, feedid,tradeid, Fname,dbtable);
    	    			//Ending
    	    			dbObj.executeNonQuery(conn,"UPDATE TBL_FORMULA  SET ISEND='true' WHERE FEEDSECID='"+feedid+"' AND TRADESECID='"+tradeid+"' AND FORMULANAME = '"+Fname+"';"); 
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
    	    				// ending
        	    			dbObj.executeNonQuery(conn,"UPDATE TBL_FORMULA  SET ISEND='true' WHERE FEEDSECID='"+feedid+"' AND TRADESECID='"+tradeid+"' AND FORMULANAME = '"+Fname+"';"); 
        	    			Logger.info("Head Feed - "+feedid+" & Trade ID - "+tradeid+" && Formula Name - "+Fname+" Achived End point.");
    	    			}
    	    			else
    	    			{
    	    				S = S +1;
    	    			}
    	    			if(S == stopl)
    	    			{
    	    				// ending
        	    			dbObj.executeNonQuery(conn,"UPDATE TBL_FORMULA  SET ISEND='true' WHERE FEEDSECID='"+feedid+"' AND TRADESECID='"+tradeid+"' AND FORMULANAME = '"+Fname+"';"); 
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
    	    					orderid = funcom.LoadDataandOrder(conn,objPresto,feedid, tradeid, "SELL");
    	    					dbObj.executeNonQuery(conn,"UPDATE TBL_F1_HRUN_TRADES  SET EXITCONDITION='N/A',EXITTIME='"+ticktime.toString()+"',SELLPRICE ="+sellPrice+", "
    	    							+ "Tcount="+TCount+", ISBUYSELLDONE = 'true' , LOW ="+low+", HIGH="+high+", Mpoint="+Mpoint+",C="+C+", LC="+LC+", S="+S+",  EXITID='"+orderid+"' WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"' and ISBUYSELLDONE ='false'");
    	    					Logger.info("Trade Switch is ON, Order Palced : "+orderid);
    	    				}
    	    			}
    	    			else 
    	    			{
    	    				dbObj.executeNonQuery(conn,"UPDATE TBL_F1_HRUN_TRADES  SET EXITCONDITION='N/A',EXITTIME='"+ticktime.toString()+"',SELLPRICE ="+sellPrice+", Tcount="+TCount+", ISBUYSELLDONE = 'true', LOW ="+low+", HIGH="+high+", Mpoint="+Mpoint+",C="+C+", LC="+LC+", S="+S+" WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"' and ISBUYSELLDONE ='false'");
    	    			}
    	    			funcom.calculatefigure(conn,sellPrice, buyPrice, feedid,tradeid, Fname,dbtable);
					}
					// Box 4
					else if(tickprice >= Mpoint + (Mpoint*(z/100)))
					{
						Mpoint= Mpoint + (Mpoint*(z/100));
						C=C+1;
						dbObj.executeNonQuery(conn,"UPDATE TBL_F1_HRUN_TRADES SET LOW ="+low+", HIGH="+high+", Mpoint="+Mpoint+",C="+C+", LC="+LC+", S="+S+" WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"' and ISBUYSELLDONE ='false'");
    	    			
					}
					
				}
				else if (isSell == true)
	    	    {
					if (tickprice <= (Mpoint - (Mpoint*(z/100))))
					{
						Mpoint = Mpoint - (Mpoint*(z/100));
						C=C+1;
						dbObj.executeNonQuery(conn,"UPDATE TBL_F1_HRUN_TRADES SET LOW ="+low+", HIGH="+high+", Mpoint="+Mpoint+",C="+C+", LC="+LC+", S="+S+" WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"' and ISBUYSELLDONE ='false'");
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
    	    				// ending
        	    			dbObj.executeNonQuery(conn,"UPDATE TBL_FORMULA  SET ISEND='true' WHERE FEEDSECID='"+feedid+"' AND TRADESECID='"+tradeid+"' AND FORMULANAME = '"+Fname+"';"); 
        	    			Logger.info("Head Feed - "+feedid+" & Trade ID - "+tradeid+" && Formula Name - "+Fname+" Achived End point.");
    	    			}
    	    			else
    	    			{
    	    				S = S + 1;
    	    			}
    	    			if(S == stopl)
    	    			{	
    	    				dbObj.executeNonQuery(conn,"UPDATE TBL_FORMULA  SET ISEND='true' WHERE FEEDSECID='"+feedid+"' AND TRADESECID='"+tradeid+"' AND FORMULANAME = '"+Fname+"';"); 
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
    	    					orderid = funcom.LoadDataandOrder(conn,objPresto, feedid, tradeid, "BUY");
    	    					dbObj.executeNonQuery(conn,"UPDATE TBL_F1_HRUN_TRADES  SET EXITCONDITION='N/A',EXITTIME='"+ticktime.toString()+"',BUYPRICE ="+buyPrice+", "
    	    							+ "Tcount="+TCount+", ISBUYSELLDONE = 'true' , LOW ="+low+", HIGH="+high+", Mpoint="+Mpoint+",C="+C+", LC="+LC+", S="+S+",  EXITID='"+orderid+"' WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"' and ISBUYSELLDONE ='false'");
    	    					Logger.info("Trade Switch is ON, Order Palced : "+orderid);
    	    				}
    	    			}
    	    			else 
    	    			{
    	    				dbObj.executeNonQuery(conn,"UPDATE TBL_F1_HRUN_TRADES  SET EXITCONDITION='N/A',EXITTIME='"+ticktime.toString()+"',BUYPRICE ="+buyPrice+", Tcount="+TCount+", ISBUYSELLDONE = 'true', LOW ="+low+", HIGH="+high+", Mpoint="+Mpoint+",C="+C+", LC="+LC+", S="+S+" WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"' and ISBUYSELLDONE ='false'");
    	    			}
    	    			funcom.calculatefigure(conn,sellPrice, buyPrice, feedid,tradeid, Fname,dbtable);
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
    	    					orderid = funcom.LoadDataandOrder(conn,objPresto, feedid, tradeid, "BUY");
    	    					dbObj.executeNonQuery(conn,"UPDATE TBL_F1_HRUN_TRADES  SET EXITCONDITION='N/A',EXITTIME='"+ticktime.toString()+"',BUYPRICE ="+buyPrice+", "
    	    							+ "Tcount="+TCount+", ISBUYSELLDONE = 'true',LOW ="+low+", HIGH="+high+", Mpoint="+Mpoint+",C="+C+", LC="+LC+", S="+S+" , EXITID='"+orderid+"' WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"' and ISBUYSELLDONE ='false'");
    	    					Logger.info("Trade Switch is ON, Order Palced : "+orderid);
    	    				}
    	    			}
    	    			else 
    	    			{
    	    				dbObj.executeNonQuery(conn,"UPDATE TBL_F1_HRUN_TRADES  SET EXITCONDITION='N/A',EXITTIME='"+ticktime.toString()+"',BUYPRICE ="+buyPrice+", Tcount="+TCount+", ISBUYSELLDONE = 'true', LOW ="+low+", HIGH="+high+", Mpoint="+Mpoint+",C="+C+", LC="+LC+", S="+S+"  WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"' and ISBUYSELLDONE ='false'");
    	    			}
    	    			funcom.calculatefigure(conn,sellPrice, buyPrice, feedid,tradeid, Fname,dbtable);
    	    			//Ending
    	    			dbObj.executeNonQuery(conn,"UPDATE TBL_FORMULA  SET ISEND='true' WHERE FEEDSECID='"+feedid+"' AND TRADESECID='"+tradeid+"' AND FORMULANAME = '"+Fname+"';"); 
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
		    	    	dbObj.executeNonQuery(conn,"UPDATE TBL_F1_HRUN_TRADES SET LOW = "+low+" , HIGH ="+ high +""
		    	    			+ " WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"' and ISBUYSELLDONE ='false'");
		    	    }
					if(tickprice > high){high = tickprice;dbObj.executeNonQuery(conn,"UPDATE TBL_F1_HRUN_TRADES  SET HIGH ="+ high +" WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"' and ISBUYSELLDONE ='false'");}
					if(tickprice < low){low = tickprice;dbObj.executeNonQuery(conn,"UPDATE TBL_F1_HRUN_TRADES  SET LOW ="+ low +" WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"' and ISBUYSELLDONE ='false'");} 
					double buylegprice = low + (low*(x/100)), sellingprice = high - (high*(x/100));
					// buy leg box 1
					if (tickprice > buylegprice)
	        	    {
						fst1 = ticktime;
        	    		if (fst1.after(t2))
        	    		{
        	    			//Ending Execution for HEAD FEED
        	    			dbObj.executeNonQuery(conn,"UPDATE TBL_FORMULA  SET ISEND='true' WHERE FEEDSECID='"+feedid+"' AND TRADESECID='"+tradeid+"' AND FORMULANAME = '"+Fname+"';"); 
        	    			Logger.info("Head Feed - "+feedid+" & Trade ID - "+tradeid+" && Formula Name - "+Fname+" Achived End point.");
        	    			//goto end;
        	    			
        	    		}
        	    		else {
        	    			String orderid=null;
        	    			isShotsell = false;
        	    			//buy 
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
        	    					orderid = funcom.LoadDataandOrder(conn,objPresto, feedid, tradeid, "BUY");
        	    					dbObj.executeNonQuery(conn,"UPDATE TBL_F1_HRUN_TRADES  SET ENTRYTIME = '"+ticktime.toString()+"',"
        	    							+ "BUYPRICE="+buyPrice+",ISSHOTSELL='"+isShotsell+"',  isBought='"+isBought+"', LOW ="+low+", HIGH="+high+", Mpoint="+Mpoint+",C="+C+", LC="+LC+", S="+S+" ,Tcount="+TCount+", ENTRYID='"+orderid+"' WHERE FEEDSECID='"+feedid+"'"
        	    									+ " and TRADESECID ='"+tradeid+"' and ISBUYSELLDONE ='false'");   
        	    					Logger.info("Trade Switch is ON, Order Palced : "+orderid);
        	    				}
	        	    		}
        	    			else
        	    			{
        	    				Logger.info("Trade Switch is OFF, paper order");
        	    				dbObj.executeNonQuery(conn,"UPDATE TBL_F1_HRUN_TRADES  SET ENTRYTIME = '"+ticktime.toString()+"',"
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
        	    			dbObj.executeNonQuery(conn,"UPDATE TBL_FORMULA  SET ISEND='true' WHERE FEEDSECID='"+feedid+"' AND TRADESECID='"+tradeid+"' AND FORMULANAME = '"+Fname+"';"); 
        	    			Logger.info("Head Feed - "+feedid+" & Trade ID - "+tradeid+" && Formula Name - "+Fname+" Achived End point.");
        	    		}
        	    		else {
        	    				//sell 
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
	        	    					orderid = funcom.LoadDataandOrder(conn,objPresto, feedid, tradeid, "SELL");
	        	    					dbObj.executeNonQuery(conn,"UPDATE TBL_F1_HRUN_TRADES  SET ISSHOTSELL='"+isShotsell+"', ENTRYTIME='"+ticktime.toString()+"',SELLPRICE="+sellPrice+","
	    	        	    					+ " isSell='"+isSell+"',Tcount="+TCount+",LOW ="+low+", HIGH="+high+", Mpoint="+Mpoint+",C="+C+", LC="+LC+", S="+S+" , ENTRYID='"+orderid+"' WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"' "
	    	        	    							+ "and ISBUYSELLDONE ='false'");   
	        	    					Logger.info("Trade Switch is ON, Order Palced : "+orderid);
		        	    			}
	        	    			}
	        	    			else
	        	    			{
	        	    			dbObj.executeNonQuery(conn,"UPDATE TBL_F1_HRUN_TRADES  SET ISSHOTSELL='"+isShotsell+"', ENTRYTIME='"+ticktime.toString()+"',SELLPRICE="+sellPrice+","
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
        	    			dbObj.executeNonQuery(conn,"UPDATE TBL_FORMULA  SET ISEND='true' WHERE FEEDSECID='"+feedid+"' AND TRADESECID='"+tradeid+"' AND FORMULANAME = '"+Fname+"';"); 
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
        	    					orderid = funcom.LoadDataandOrder(conn,objPresto, feedid, tradeid, "BUY");
        	    					dbObj.executeNonQuery(conn,"UPDATE TBL_F1_HRUN_TRADES  SET ENTRYTIME = '"+ticktime.toString()+"',"
        	    							+ "BUYPRICE="+buyPrice+",ISSHOTSELL='"+isShotsell+"',  isBought='"+isBought+"', LOW ="+low+", HIGH="+high+", Mpoint="+Mpoint+",C="+C+", LC="+LC+", S="+S+" ,Tcount="+TCount+", ENTRYID='"+orderid+"' WHERE FEEDSECID='"+feedid+"'"
        	    									+ " and TRADESECID ='"+tradeid+"' and ISBUYSELLDONE ='false'");   
        	    					Logger.info("Trade Switch is ON, Order Palced : "+orderid);
        	    				}
	        	    		}
        	    			else
        	    			{
        	    				Logger.info("Trade Switch is OFF, paper order");
        	    				dbObj.executeNonQuery(conn,"UPDATE TBL_F1_HRUN_TRADES  SET ENTRYTIME = '"+ticktime.toString()+"',"
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
        	    			dbObj.executeNonQuery(conn,"UPDATE TBL_FORMULA  SET ISEND='true' WHERE FEEDSECID='"+feedid+"' AND TRADESECID='"+tradeid+"' AND FORMULANAME = '"+Fname+"';"); 
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
	        	    					orderid = funcom.LoadDataandOrder(conn,objPresto, feedid, tradeid, "SELL");
	        	    					dbObj.executeNonQuery(conn,"UPDATE TBL_F1_HRUN_TRADES  SET ISSHOTSELL='"+isShotsell+"', ENTRYTIME='"+ticktime.toString()+"',SELLPRICE="+sellPrice+","
	    	        	    					+ " isSell='"+isSell+"',Tcount="+TCount+",LOW ="+low+", HIGH="+high+", Mpoint="+Mpoint+",C="+C+", LC="+LC+", S="+S+" , ENTRYID='"+orderid+"' WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"' "
	    	        	    							+ "and ISBUYSELLDONE ='false'");   
	        	    					Logger.info("Trade Switch is ON, Order Palced : "+orderid);
		        	    			}
	        	    			}
	        	    			else
	        	    			{
	        	    			dbObj.executeNonQuery(conn,"UPDATE TBL_F1_HRUN_TRADES  SET ISSHOTSELL='"+isShotsell+"', ENTRYTIME='"+ticktime.toString()+"',SELLPRICE="+sellPrice+","
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
