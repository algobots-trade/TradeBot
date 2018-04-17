package com.tradebot.presto;

import java.io.File;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.marketcetera.types.trade.OrderStatus;
import org.marketcetera.types.trade.OrderType;
import org.marketcetera.types.trade.Side;
import org.marketcetera.types.trade.TimeInForce;
import org.pmw.tinylog.Logger;

import com.tradebot.presto.Date;
import com.tradebot.presto.EsbConnection;
import com.tradebot.presto.ReportHandler;
import com.symphonyfintech.gateway.EsbOrder;
import com.symphonyfintech.gateway.PositionBean;
import com.symphonyfintech.gateway.ReportHolder;
import com.symphonyfintech.gateway.SecurityType;
import com.symphonyfintech.gateway.SymbolDetail;
import com.tradebot.dbcommons.db_commons;
import com.tradebot.dbcommons.tradebot_utility;

public class presto_commons {
	
	static List<SymbolDetail> symbolDetailExchange,
	symbolDetailExchangeInstrument, SYMBOLDETAIL;
	static ReportHolder allReport;
	static List<ReportHolder> allList;
	static SymbolDetail SYMBOLDETAILS;
	static String USERNAME, ESBUSER, PASSWORD, ORDERTYPE, ESBSECURITYID,
		SECURITYTYPE, ESBEXCHANGE, INSTRUMENTTYPE, ESBOPTIONTYPE,
		ESBSYMBOL, REMARK, ESBACCOUNT, SIDE, INPUT, ORIGINALORDERID,
		STRATEGYINSTANCEID, EXTERNALINSTANCEID, EXPDATE, clientOrderID,
		QUANTITY, PRICE, STOPPRICE, ESBSTRIKEPPRICE, INSTANCEIDCUSTOMFIELD,
		SEGMENT;
	static List<byte[]> allReportFormated, openOrderFormated,
	tradeHistoryFormated;
	static List<PositionBean> position = null;
	static boolean connectionStatus;
	static OrderStatus ESBORDERSTATUS;
	static byte[] clientLastTradeHistory, clientLatestReport, REPORT;
	static OrderType ESBORDERTYPE;
	static SecurityType ESBSECURITYTYPE;
	static Side ESBSIDE;
	static TimeInForce ESBTIMEINFORCE;
	static String TIMEINFORCE = "DAY";
	static BigDecimal ESBPRICE, ESBSTOPPRICE;
	static Integer ESBQUANTITY;
	
	static Date ESBEXPDATE;
	static GregorianCalendar gcal;
	static XMLGregorianCalendar xgcal = null;
	static Scanner IN = new Scanner(System.in);
	static EsbConnection esbConnect;
	static Iterator<byte[]> iter;
	ReportHandler reportHandler;
	
	static String FutOptExchange = "NSEFO", EquExchange = "NSECM", ComExchange = "NSEDC", McxExchange = "MCX";	
	static String FutSegment = "FUT", optSegment = "OPT", EquSegment = "CM";
	static String FutIdxInsttype = "FUTIDX", FutStkInsttype = "FUTSTK", OptIdxInsttype = "OPTIDX", 
			OptStkInsttype = "OPTSTK", EquInsttype = "Equities", FutComInsttype = "FUTCOM";
	
	
	 
	
	private String configprop=System.getProperty("user.dir")+File.separator+"resource"+File.separator+"config.properties";
	String tradelogpath;
	tradebot_utility utils = new tradebot_utility(); 
	
	db_commons dbobj=new db_commons();
	
	public presto_commons()
	{
		try
		{
			USERNAME="shanmuga";
			PASSWORD = "sm@12345";
			tradelogpath = utils.configlogfile("TRADEBOT_LOG");
			reportHandler = new ReportHandler();
			esbConnect = new EsbConnection();
			esbConnect.initialize(reportHandler);
			logintopresto();
		}
		catch(Exception ex)
		{
			Logger.error(ex);
		}
	}
	public boolean logintopresto()
	{
		boolean connected =false;
		try
		{
			esbConnect.setDealerName(USERNAME);
			esbConnect.setDealerPassword(PASSWORD);
			esbConnect.forceLogoutFromORS(USERNAME, PASSWORD);
			esbConnect.loginToOrs(USERNAME, PASSWORD);
		}
		catch(Exception ex)
		{
			System.out.print(ex);
		}
		connected = esbConnect.getConnectionStatus(USERNAME);
		if (connected == true)
		{
			System.out.print("Is Connected to Broker : "+ connected );
		}
		else if (connected == false)
		{
			System.out.print("Is Connected to Broker : "+ connected); 
	    }
		return connected;
	}
	public Hashtable InstValues_inter(String [] InstValues)
	{
		Hashtable insts = new Hashtable();
		try
		{
			switch (InstValues[1]) {
			case "STOCK":
				insts.put("ESBEXCHANGE", EquExchange);
				insts.put("ESBSYMBOL", InstValues[0]);
				insts.put("SEGMENT", EquSegment);
				insts.put("INSTRUMENTTYPE", EquInsttype);
				break;
			
			case "INDEX":
				//Need to be implemented
				break;
				
			case "FUTURE":
				insts.put("ESBEXCHANGE", FutOptExchange);
				insts.put("ESBSYMBOL", InstValues[0]);
				insts.put("SEGMENT", FutSegment);
				insts.put("INSTRUMENTTYPE", FutStkInsttype);
				break;
			
			case "OPTIONS":
				insts.put("ESBEXCHANGE", FutOptExchange);
				insts.put("ESBSYMBOL", InstValues[0]);
				insts.put("SEGMENT", optSegment);
				insts.put("INSTRUMENTTYPE", OptStkInsttype);
				break;

			default:
				break;
			}
		}
		catch(Exception ex)
		{
				
		}
		finally
		{
			
		}
		return insts;
	}
	public String [] VerifySymbol(String [] InstValues)
	{
		String [] st = null;
		try
		{
			//ESBEXCHANGE = orderData.nextToken();
			//ESBSYMBOL = orderData.nextToken();
			//SEGMENT = orderData.nextToken();
			//INSTRUMENTTYPE = orderData.nextToken();
			//SYMBOLDETAIL = esbConnect.getSymbolDetailsForInstrType(
			//		ESBEXCHANGE, ESBSYMBOL, INSTRUMENTTYPE, SEGMENT);
		}
		catch(Exception ex)
		{
			
		}
		finally
		{
			
		}
		return st;
	}
	
	public boolean checkandLoginFinvasia()
	{
		Boolean connectionStatus = false;
		try
		{
			connectionStatus = esbConnect.getConnectionStatus(USERNAME);
			if (connectionStatus == true)
			{
				System.out.print("Is Connected to Broker : "+ connectionStatus );
			}
			else if (connectionStatus == false)
			{
				System.out.print("Is Connected to Broker : "+ logintopresto()); 
		    }
			connectionStatus = esbConnect.getConnectionStatus(USERNAME);
			return connectionStatus;
			
		}
		catch(Exception ex)
		{
			System.out.print(ex);
		}
		return connectionStatus;
	}
	
	public String PlaceOrder(String ESB_EXCHANGE,String SECURITY_TYPE,String ESB_SYMBOL,String ESB_SECURITYID,
			String EXP_DATE,String ESB_ACCOUNT,String QUAN_TITY,String T_PRICE,String STOP_PRICE,	
			String ESB_OPTIONTYPE,String ESB_STRIKEPPRICE,String ORDER_TYPE,String INSTANCEID_CUSTOMFIELD,String T_REMARK,
			String TIME_INFORCE,String T_SIDE)
	{
		String strClientId=null;
		try
		{
			boolean constate = false;
		
			try
			{
				constate = esbConnect.getConnectionStatus(USERNAME);
			}
			catch(Exception ex)
			{
				
			}
			if (constate == false)
			{
			  checkandLoginFinvasia();
			  strClientId = userPlaceOrderNSE(ESB_EXCHANGE,
						SECURITY_TYPE, ESB_SYMBOL, ESB_SECURITYID, EXP_DATE,
						ESB_ACCOUNT, QUAN_TITY, T_PRICE, STOP_PRICE,
						ESB_OPTIONTYPE, ESB_STRIKEPPRICE, ORDER_TYPE,
						INSTANCEID_CUSTOMFIELD, T_REMARK, TIME_INFORCE, T_SIDE);
				 System.out.print("Order Client ID - " + strClientId);
			}
			else
			{
				strClientId = userPlaceOrderNSE(ESB_EXCHANGE,
						SECURITY_TYPE, ESB_SYMBOL, ESB_SECURITYID, EXP_DATE,
						ESB_ACCOUNT, QUAN_TITY, T_PRICE, STOP_PRICE,
						ESB_OPTIONTYPE, ESB_STRIKEPPRICE, ORDER_TYPE,
						INSTANCEID_CUSTOMFIELD, T_REMARK, TIME_INFORCE, T_SIDE);
				 System.out.print("Order Client ID - " + strClientId);
			}
		}
		catch(Exception ex)
		{
			System.out.print(ex);;
		}
		return strClientId;
	}
	
	private static String userPlaceOrderNSE(String exchange,
			String securityType, String symbol, String securityId,
			String expDate, String account, String quantity, String price,
			String stopPrice, String optionType, String strikePrice,
			String orderType, String customField, String remark,
			String timeInForce, String side) {
		String clientID = null;
		try {
			EsbOrder esbOrder = new EsbOrder();

			esbConnect.setDealerName(USERNAME);
			esbOrder.setEsbuser(USERNAME);
			esbOrder.setEsbexchange(exchange);
			esbOrder.setEsbsymbol(symbol);
			esbOrder.setSecurityID(securityId);

			StringTokenizer date = new StringTokenizer(expDate, "-");
			String day = date.nextToken();
			String month = date.nextToken();
			String year = date.nextToken();

			
			
			ESBEXPDATE = new Date(day, month, year);
			gcal = (GregorianCalendar) GregorianCalendar.getInstance();

			xgcal = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);

			if (date != null) {
				xgcal.setDay(Integer.parseInt(ESBEXPDATE.getDay()));
				xgcal.setMonth(Integer.parseInt(ESBEXPDATE.getMonth()));
				xgcal.setYear(Integer.parseInt(ESBEXPDATE.getYear()));
				esbOrder.setEsbexpiry(xgcal);
			}

			esbOrder.setEsbaccount(account);
			esbOrder.setEsbquantity(new Integer(quantity));
			esbOrder.setEsbprice(new BigDecimal(price));
			esbOrder.setEsbstopPrice(new BigDecimal(stopPrice));
			esbOrder.setOptionType(optionType);
			esbOrder.setStrikePrice(strikePrice);
			esbOrder.setInstanceIdCustomField(customField);
			esbOrder.setRemark(remark);

			if (securityType.equalsIgnoreCase("CM")) {
				esbOrder.setEsbsecurityType(SecurityType.COMMON_STOCK);
				// esbOrder.setEsbexpiry(null);
				esbOrder.setOptionType("null");
				esbOrder.setStrikePrice("null");
			} else if (securityType.equalsIgnoreCase("FUT")) {
				esbOrder.setEsbsecurityType(SecurityType.FUTURE);
			} else if (securityType.equalsIgnoreCase("OPT")) {
				esbOrder.setEsbsecurityType(SecurityType.OPTION);
			}

			if (orderType.equalsIgnoreCase("limit")) {
				esbOrder.setEsborderType(OrderType.LIMIT);
				esbOrder.setEsbstopPrice(null);
			} else if (orderType.equalsIgnoreCase("market")) {
				esbOrder.setEsborderType(OrderType.MARKET);
				esbOrder.setEsbprice(null);
				esbOrder.setEsbstopPrice(null);
			} else if (orderType.equalsIgnoreCase("stop limit")) {
				esbOrder.setEsborderType(OrderType.STOP_LIMIT);
			} else if (orderType.equalsIgnoreCase("stop market")) {
				esbOrder.setEsborderType(OrderType.STOP);
				esbOrder.setEsbprice(null);
			}
			if (timeInForce.equalsIgnoreCase("day")) {
				esbOrder.setEsbtimeInForce(TimeInForce.DAY);
			}
			if (side.equalsIgnoreCase("buy")) {
				esbOrder.setEsbside(Side.BUY);
			} else if (side.equalsIgnoreCase("sell")) {
				esbOrder.setEsbside(Side.SELL);
			}

			clientID = esbConnect.placeOrder(USERNAME, esbOrder);
			System.err.println("CLIENT ID:######################### "
					+ clientID);

		} catch (DatatypeConfigurationException e2) {
			e2.printStackTrace();
		}
		return clientID;
	}
	public String getEquities(String scrib)
	{
		String validSecData = null;
		try
		{
			
		}
		catch(Exception ex)
		{
			
		}
		return validSecData;
	}

}
