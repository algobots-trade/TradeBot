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
import java.sql.Connection;

public class F3_Dummy_Algo {
	public double x,y,z;
	public Date t1,t2,t3,t4;
	public int maxtradecount =0, TCount=0;
	public boolean isBought =false, isSell = false, isShotsell=false, istradeswitch=false;
	public double buyPrice,sellPrice,low=0.0,high=0.0,Mpoint,stopl=0.0;
	public Date fst1, fst2;
	final static String Fname="F3";
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
    public Formula_Commons funcom=new Formula_Commons();
    public String dbtable = "TBL_F3_DUMMY_TRADES";
    public Connection conn;
    
	public F3_Dummy_Algo(Connection connect, presto_commons objconnect,  String feedid, double tradeprice, int asksize,int bidsize ,String tickdatetime) 
	{
		// TODO Auto-generated constructor stub
		 conn = connect;
		 tradelogpath = utils.configlogfile("F3_LOG");
		 askvolume = asksize;
		 bidvolume = bidsize;		 
		 objPresto =objconnect;
		 try
		 {
			 ArrayList<String> tradeplayers = dbObj.getSingleColumnRecords(conn, "SELECT TRADESECID FROM TBL_TRADERS WHERE FEEDSECID = '"+feedid+"';");	
			 datefmt=new SimpleDateFormat("yyyyMMdd HH:mm:ss");
			 for(int i =0; i < tradeplayers.size(); i++)
			 {
				 if ((dbObj.getRowCount(conn,"SELECT * FROM TBL_FORMULA WHERE FEEDSECID ='"+feedid+"' AND TRADESECID ='"+tradeplayers.get(i)+"'") !=0 )&&(dbObj.getSingleCell(conn,"SELECT ISEND FROM TBL_FORMULA WHERE FEEDSECID ='"+feedid+"' AND TRADESECID ='"+tradeplayers.get(i)+"' AND FORMULANAME ='"+Fname+"'").equals("false")))
				 {
				 
					 if (dbObj.getRowCount(conn,"SELECT * FROM TBL_F3_DUMMY_TRADES  WHERE FEEDSECID='"+feedid+"' and TRADESECID='"+tradeplayers.get(i)+"' and ISBUYSELLDONE ='false'") == 0)
					 {
						 dbObj.executeNonQuery(conn,"insert into TBL_F3_DUMMY_TRADES (FEEDSECID, TRADESECID,ENTRYTIME,BUYPRICE,SELLPRICE,EXITTIME,ISSHOTSELL,HIGH,LOW,ISBOUGHT, ISSELL,MPOINT,EXITCONDITION,TCOUNT,ISBUYSELLDONE ) values "
						 		+ "('"+feedid+"', '"+tradeplayers.get(i)+"', null, 0.0,0.0,null,'false',0.0,0.0,'false','false',0.0,null,0,'false')");
					 }
					 
					 assginF1variables(feedid, tradeplayers.get(i),tickdatetime);
					 
					 int tradec =Integer.parseInt(dbObj.getSingleCell(conn,"SELECT SUM(TCOUNT) as \"count\" FROM TBL_F3_DUMMY_TRADES  WHERE FEEDSECID='"+feedid+"' and TRADESECID='"+tradeplayers.get(i)+"'"));
					 TCount = tradec;
					 if (tradec < maxtradecount)
					 {
	
						 CalculateF1(feedid, tradeplayers.get(i),Double.valueOf(tradeprice), datefmt.parse(tickdatetime));
					 }
					 else
					 {
						 System.out.print("Max trade Count achieved...");
						//Ending Execution for HEAD FEED
	    	    		dbObj.executeNonQuery(conn,"UPDATE TBL_FORMULA  SET ISEND='true' WHERE FEEDSECID='"+feedid+"' AND TRADESECID='"+tradeid+"' AND FORMULANAME = '"+Fname+"';"); 
	    	    		Logger.info("Head Feed - "+feedid+" & Trade ID - "+tradeid+" && Formula Name - "+Fname+" Achived End point.");
					 }
				 
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
			F1inputdata = dbObj.getMultiColumnRecords(conn,"SELECT * FROM TBL_FORMULA WHERE FEEDSECID='"+feedid+"' and TRADESECID='"+tradeid+"' and FORMULANAME ='"+Fname+"'");
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
			F1tradedata = dbObj.getMultiColumnRecords(conn ,"SELECT * FROM TBL_F3_DUMMY_TRADES  where FEEDSECID='"+feedid+"' and TRADESECID='"+tradeid+"' and ISBUYSELLDONE='false'");
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
			String orderid = null;
			if (TCount <= maxtradecount)
			{
				if (ticktime.after(t1))
				{
					if ((low == 0.0) && (high == 0.0))
		    	    {
		    	    	low = tickprice;
		    	    	high =tickprice;
		    	    	dbObj.executeNonQuery(conn,"UPDATE TBL_F3_DUMMY_TRADES SET LOW = "+low+" , HIGH ="+ high +" WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"' and ISBUYSELLDONE ='false'");
		    	    }
					if ((isBought==false)&&(isSell==false))
					{
						if(tickprice > high){high = tickprice;dbObj.executeNonQuery(conn,"UPDATE TBL_F3_DUMMY_TRADES  SET HIGH ="+ high +" WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"' and ISBUYSELLDONE ='false'");}
						if(tickprice < low) {low = tickprice;dbObj.executeNonQuery(conn,"UPDATE TBL_F3_DUMMY_TRADES  SET LOW ="+ low +" WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"' and ISBUYSELLDONE ='false'");}   
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
	    	    					orderid = funcom.LoadDataandOrder(conn,objPresto,feedid, tradeid, "SELL");
	    	    				}
        	    			}
	    	    			dbObj.executeNonQuery(conn,"UPDATE TBL_F3_DUMMY_TRADES  SET EXITCONDITION='PROFIT',EXITID='"+orderid+"',EXITTIME='"+ticktime.toString()+"',SELLPRICE ="+sellPrice+", Tcount="+TCount+", ISBUYSELLDONE = 'true' WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"'and ISBUYSELLDONE ='false'");
	    	    			funcom.calculatefigure(conn,sellPrice, buyPrice, feedid, tradeid, Fname, dbtable);
	    	    			
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
	    	    					orderid = funcom.LoadDataandOrder(conn,objPresto,feedid, tradeid, "SELL");
	    	    				}
        	    			}
	    	    			dbObj.executeNonQuery(conn,"UPDATE TBL_F3_DUMMY_TRADES  SET EXITCONDITION='LOSS',EXITID='"+orderid+"',EXITTIME='"+ticktime.toString()+"',SELLPRICE ="+sellPrice+", Tcount="+TCount+", ISBUYSELLDONE = 'true' WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"' and ISBUYSELLDONE ='false'");
	    	    			funcom.calculatefigure(conn,sellPrice, buyPrice, feedid, tradeid, Fname, dbtable);
	    	    			
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
	    	    					orderid = funcom.LoadDataandOrder(conn,objPresto,feedid, tradeid, "SELL");
	    	    				}
        	    			}
	    	    			dbObj.executeNonQuery(conn,"UPDATE TBL_F3_DUMMY_TRADES  SET EXITCONDITION='Validity Expires',EXITID='"+orderid+"',EXITTIME='"+ticktime.toString()+"',SELLPRICE ="+sellPrice+", Tcount="+TCount+", ISBUYSELLDONE = 'true' WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"' and ISBUYSELLDONE ='false'");
	    	    			funcom.calculatefigure(conn,sellPrice, buyPrice, feedid, tradeid, Fname, dbtable);
	    	    				
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
		        	    				orderid = funcom.LoadDataandOrder(conn,objPresto,feedid, tradeid, "BUY");
		        	    			}
	        	    			}
		        	    		dbObj.executeNonQuery(conn,"UPDATE TBL_F3_DUMMY_TRADES  SET EXITCONDITION='PROFIT', EXITID='"+orderid+"',EXITTIME='"+ticktime.toString()+"',BUYPRICE ="+buyPrice+", Tcount="+TCount+", ISBUYSELLDONE = 'true' WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"' and ISBUYSELLDONE ='false'");
		        	    		funcom.calculatefigure(conn,sellPrice, buyPrice, feedid, tradeid, Fname, dbtable);
		        	    		
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
		        	    				orderid = funcom.LoadDataandOrder(conn,objPresto,feedid, tradeid, "BUY");
		        	    			}
	        	    			}
		        	    		dbObj.executeNonQuery(conn,"UPDATE TBL_F3_DUMMY_TRADES  SET EXITCONDITION='LOSS',EXITID='"+orderid+"' ,EXITTIME='"+ticktime.toString()+"',BUYPRICE ="+buyPrice+", Tcount="+TCount+", ISBUYSELLDONE = 'true' WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"' and ISBUYSELLDONE ='false'");
		        	    		funcom.calculatefigure(conn,sellPrice, buyPrice, feedid, tradeid, Fname, dbtable);
		        	    		
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
		        	    				orderid = funcom.LoadDataandOrder(conn,objPresto,feedid, tradeid, "BUY");
		        	    			}
	        	    			}
		        	    		dbObj.executeNonQuery(conn,"UPDATE TBL_F3_DUMMY_TRADES  SET EXITCONDITION='Validity Expires',EXITID='"+orderid+"',EXITTIME='"+ticktime.toString()+"',BUYPRICE ="+buyPrice+", Tcount="+TCount+", ISBUYSELLDONE = 'true' WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"' and ISBUYSELLDONE ='false'");
		        	    		funcom.calculatefigure(conn,sellPrice, buyPrice, feedid, tradeid, Fname, dbtable);
		        	    		
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
			        	    			
			        	    			//goto end;
			        	    			//Ending Execution for HEAD FEED
			        	    			dbObj.executeNonQuery(conn,"UPDATE TBL_FORMULA  SET ISEND='true' WHERE FEEDSECID='"+feedid+"' AND TRADESECID='"+tradeid+"' AND FORMULANAME = '"+Fname+"';"); 
			        	    			Logger.info("Head Feed - "+feedid+" & Trade ID - "+tradeid+" && Formula Name - "+Fname+" Achived End point.");
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
				        	    					orderid = funcom.LoadDataandOrder(conn,objPresto,feedid, tradeid, "BUY");
				        	    				}
					        	    		}
				        	    			dbObj.executeNonQuery(conn,"UPDATE TBL_F3_DUMMY_TRADES  SET ISSHOTSELL='"+isShotsell+"', ENTRYTIME = '"+ticktime.toString()+"',ENTRYID='"+orderid+"',BUYPRICE="+buyPrice+", Mpoint="+Mpoint+", isBought='"+isBought+"',Tcount="+TCount+" WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"' and ISBUYSELLDONE ='false'");
				        	    			
			        	    		}
			        	    		
			        	    }
			        	    else if (tickprice < sellegprice)
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
				        	    					orderid = funcom.LoadDataandOrder(conn,objPresto,feedid, tradeid,"SELL");
					        	    			}
				        	    			}
				        	    			dbObj.executeNonQuery(conn,"UPDATE TBL_F3_DUMMY_TRADES  SET ISSHOTSELL='"+isShotsell+"', ENTRYTIME='"+ticktime.toString()+"', ENTRYID='"+orderid+"' ,SELLPRICE="+sellPrice+",Mpoint="+Mpoint+", isSell='"+isSell+"',Tcount="+TCount+" WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"' and ISBUYSELLDONE ='false'");
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


}
