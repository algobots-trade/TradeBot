package com.tradebot.formulas;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.pmw.tinylog.Logger;
import java.sql.Connection;
import com.tradebot.dbcommons.db_commons;
import com.tradebot.presto.presto_commons;


public class Formula_Commons 
{
	public Connection conn;
	db_commons dbObj=new db_commons();
	public double CalculateTax(double price)
	{
		double finalcost =0.0, Brokerage=0.0,SST=0.0,TC=0.0,ST=0.0,SEBI=0.0,STAMP=0.0;
		try
		{
			Brokerage = (price*(0.006/100));
			SST=(price*(0.0125/100));
			TC=(price*(0.00325/100));
			ST = (Brokerage+TC*(18/100));
			SEBI=(price*(0.002/100));
			STAMP=(price*(0.006/100));
			finalcost = price - (Brokerage + SST + TC + ST + SEBI + STAMP);
		}
		catch(Exception ex)
		{
			Logger.error(ex);
		}
		return finalcost;
	}
	
	public void UpdateTradeBoard(Connection conn, String feedid, String tradeid, String fname, String dbtable)
	{
		String [][] pldata;
		double fpl=0.00, fpercent=0;
		DecimalFormat f = new DecimalFormat("##.00");
		try
		{
			pldata = dbObj.getMultiColumnRecords(conn,"SELECT SUM(SELLPRICE) as \"TOTAL_SELL\",SUM(BUYPRICE) as \"TOTAL_BUY\",  SUM(TCOUNT) as \"TOTAL_TRADE\" FROM "+dbtable+" WHERE FEEDSECID='"+feedid+"' and TRADESECID='"+tradeid+"' and TCOUNT=2;");
			if (pldata != null)
			{
				fpl = Double.parseDouble(pldata[0][0]) - Double.parseDouble(pldata[0][1]);
				double avg = (fpl*100) / (Integer.parseInt(pldata[0][2]));
				fpercent = (fpl*100)/avg;
				Logger.info("~~~~~~~~~~~~~~~");
				Logger.info("Selling Price : "+ pldata[0][0] );
	        	Logger.info("Buying Price : "+pldata[0][1] );
	        	Logger.info("Trade End of the Dau P&L : "+fpl +", Percentage % :"+fpercent +", Trade Count : "+pldata[0][2]);
	        	dbObj.executeNonQuery(conn, "UPDATE  TBL_TRADEBOARD SET "+fname+"PC="+f.format(fpercent)+", "+fname+"TC="+Integer.parseInt(pldata[0][2])+", "+fname+"PL="+ f.format(fpl)+" WHERE FEEDSECID ='"+feedid+"' and TRADESECID = '"+tradeid+"'");
			}
		}
		catch(Exception ex)
		{
			Logger.error(ex);
		}
	}

	public double[] calculatefigure(Connection connect, double sellprice, double buyprice, String feedid, String tradeid, String fname, String dbtable)
	{
		conn = connect;
		double [] buysell= new double[2];
		try
		{
			// commented below line since we don't want to calculate tax and brokerage etc, to enable this pls update the sell and buy price in algo class aswell.
			//buysell[0] = CalculateTax(sellprice);
			//buysell[1] = CalculateTax(buyprice);
			//dbObj.executeNonQuery("UPDATE "+dbtable+" SET SELLPRICE="+sellprice+", BUYPRICE ="+buyprice+" WHERE FEEDSECID='"+feedid+"' and TRADESECID ='"+tradeid+"' and ISBUYSELLDONE ='true'");
			UpdateTradeBoard(conn, feedid, tradeid,fname,dbtable);
		}
		catch(Exception ex)
		{
			Logger.error(ex);
		}
		return buysell;
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

	public String LoadDataandOrder(Connection conn, presto_commons objPresto, String feedid, String tradeid, String order)
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
			String [][] playerdet = dbObj.getMultiColumnRecords(conn,"SELECT FEEDSECID ,TRADESECID ,SYMBOL ,INSTTYPE ,EXPIRYDD ,EXPIRYMMMYY ,OPTTYPE ,STRIKEPRICE FROM TBL_TRADERS WHERE TRADESECID='"+tradeid+"' AND FEEDSECID ='"+feedid+"' ;");
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
			QUAN_TITY = dbObj.getSingleCell(conn,"SELECT LOTSIZE FROM TBL_FORMULA WHERE TRADESECID = '"+tradeid+"' AND FEEDSECID= '"+feedid+"'");
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
}
