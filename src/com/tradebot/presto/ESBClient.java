package com.tradebot.presto;

/**
 * 
 * NIKUL BRAHMBHATT
 * 
 * */

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.GregorianCalendar;
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

import com.symphonyfintech.gateway.EsbOrder;
import com.symphonyfintech.gateway.PositionBean;
import com.symphonyfintech.gateway.ReportHolder;
import com.symphonyfintech.gateway.SecurityType;
import com.symphonyfintech.gateway.SymbolDetail;


//NSE,CM,RELIANCE,2885,,FA8872,10,1000,,,,MARKET,Mathartz,test,DAY,BUY
public class ESBClient {

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

	public static void main(String args[]) throws InterruptedException,
			UnsupportedEncodingException {

		System.out.println("1. LOGIN TO ORS");
		System.out.println("2. SCRIPTS FOR EXCHANGE INSTRUMENT TYPE");
		System.out.println("3. SCRIPTS FOR EXCHANGE");
		System.out.println("4. GET SYMBOL DETAILS");
		System.out.println("5. GET SYMBOL DETAILS FOR INSTRUMENT TYPE");
		System.out.println("6. GET SYMBOL DETAILS DERIVATIVE OPT");
		System.out.println("7. PLACE ORDER");
		System.out.println("8. GET ORDER STATUS");
		System.out.println("9. GET ORDER STATUS FOR CLIENT ORDER ID");
		System.out.println("10. GET OPEN ORDERS FORMATED");
		System.out.println("11. GET TRADE HISTORY FORMATED");
		System.out
				.println("12. GET TRADE HISTORY FORMATED FOR CLIENT ORDER ID");
		System.out
				.println("13. GET LAST TRADE HISTORY FORMATED FOR CLIENT ORDER ID");
		System.out.println("14. GET LATEST REPORT");
		System.out.println("15. GET ALL REPORTS FORMATED");
		System.out.println("16. GET TRADED HISTORY FORMATED");
		System.out.println("17. GET TRADED HISTORY FORMATED FOR CLIENT ORDER ID");
		System.out.println("18. CANCEL ORDER");
		System.out.println("19. REPLACE ORDER");
		System.out.println("20. REPLACE ORDER WITH CUSTOM FIELD");
		System.out.println("21. CANCEL ORDER WITH CUSTOM FIELD");
		System.out.println("22. GET POSITION AS OF");
		System.out.println("23. GET CONNECTION STATUS");
		System.out.println("24. LOGOUT FROM ORS");
		System.out.println("25. FORCE LOGOUT FROM ORS");
		System.out.println("26. GET AVAILABLE BROKERS\n");
		System.out.println("27. CALL UNSTRUCTURED DATA \n");

		ReportHandler reportHandler = new ReportHandler();
		esbConnect = new EsbConnection();
		esbConnect.initialize(reportHandler);
		boolean quit = false;
		int menuItem;
		StringTokenizer orderData;
		while (!quit) {
			System.out.print("Enter your choice :");
			menuItem = IN.nextInt();

			switch (menuItem) {

			case 1:
				userLoginRequest();
				esbConnect.setDealerName(USERNAME);
				esbConnect.setDealerPassword(PASSWORD);
				esbConnect.forceLogoutFromORS(USERNAME, PASSWORD);
				esbConnect.loginToOrs(USERNAME, PASSWORD);
				break;

			case 2:
				System.out.print("Please Enter\nExchange,\nInstrument Type\n");
				INPUT = IN.next();
				orderData = new StringTokenizer(INPUT, ",");
				ESBEXCHANGE = orderData.nextToken();
				INSTRUMENTTYPE = orderData.nextToken();
				symbolDetailExchangeInstrument = esbConnect
						.scriptsForExchangeInstrType(ESBEXCHANGE,
								INSTRUMENTTYPE);
				System.out.println("SYMBOL SIZE: "
						+ symbolDetailExchangeInstrument.size());
				break;

			case 3:
				System.out.print("Please Enter\nExchange\n");
				ESBEXCHANGE = IN.next();
				symbolDetailExchange = esbConnect
						.scriptsForExchange(ESBEXCHANGE);
				// for (int i = 0; i < symbolDetailExchange.size(); i++) {
				// System.out.println("ITERATOT :"
				// + symbolDetailExchange.get(i).getExchange());
				// }
				System.out.println("SYMBOL SIZE: "
						+ symbolDetailExchange.size());
				break;

			case 4:
				System.out.print("Please Enter\nExchange,\nSecurityID\n");
				INPUT = IN.next();
				orderData = new StringTokenizer(INPUT, ",");
				ESBEXCHANGE = orderData.nextToken();
				ESBSECURITYID = orderData.nextToken();
				SYMBOLDETAILS = esbConnect.getSymbolDetails(ESBEXCHANGE,
						ESBSECURITYID);
				System.out.println("SYMBOL DETAILS : "
						+ SYMBOLDETAILS.getSymbol());
				break;

			case 5:
				System.out
						.print("Please Enter\nExchange,\nSymbol\nSegment,\nInstrument Type\n");
				INPUT = IN.next();
				orderData = new StringTokenizer(INPUT, ",");
				ESBEXCHANGE = orderData.nextToken();
				ESBSYMBOL = orderData.nextToken();
				SEGMENT = orderData.nextToken();
				INSTRUMENTTYPE = orderData.nextToken();

				SYMBOLDETAIL = esbConnect.getSymbolDetailsForInstrType(
						ESBEXCHANGE, ESBSYMBOL, INSTRUMENTTYPE, SEGMENT);

				System.out.println("SYMBOL SIZE : " + SYMBOLDETAIL.size());
				break;

			case 6:
				System.out
						.print("Please Enter\nExchange,\nSymbol\nExpiry,\nOption Type\n");
				INPUT = IN.next();
				orderData = new StringTokenizer(INPUT, ",");
				ESBEXCHANGE = orderData.nextToken();
				ESBSYMBOL = orderData.nextToken();
				EXPDATE = orderData.nextToken();
				ESBOPTIONTYPE = orderData.nextToken();

				SYMBOLDETAIL = esbConnect.getSymbolDetailsDerivativeOPT(
						ESBEXCHANGE, ESBSYMBOL, EXPDATE, ESBOPTIONTYPE);

				System.out.println("SYMBOL SIZE : " + SYMBOLDETAIL.size());
				break;

			case 7:
				System.out
						.print("Please Enter\nexchange,\nsegment(CM/FUT/OPT),"
								+ "\nsymbol,\nsecurity id,\nexpiry date(dd-mm-yyyy),\naccount,\nquantity,"
								+ "\nprice,\nstop price,\noption type,\nstrike Price,"
								+ "\norder type(LIMIT/MARKET/STOP LIMIT/STOP MARKET), \nInstanceId Custom Field,"
								+ "\nRemark,\ntime in force,\nside(BUY/SELL)\n");
				INPUT = IN.next();
				orderData = new StringTokenizer(INPUT, ",");
				ESBEXCHANGE = orderData.nextToken();
				SECURITYTYPE = orderData.nextToken();
				ESBSYMBOL = orderData.nextToken();
				ESBSECURITYID = orderData.nextToken();
				EXPDATE = orderData.nextToken();
				ESBACCOUNT = orderData.nextToken();
				QUANTITY = orderData.nextToken();
				PRICE = orderData.nextToken();
				STOPPRICE = orderData.nextToken();
				ESBOPTIONTYPE = orderData.nextToken();
				ESBSTRIKEPPRICE = orderData.nextToken();
				ORDERTYPE = orderData.nextToken();
				INSTANCEIDCUSTOMFIELD = orderData.nextToken();
				REMARK = orderData.nextToken();
				TIMEINFORCE = orderData.nextToken();
				SIDE = orderData.nextToken();
				try {
					clientOrderID = userPlaceOrderNSE(ESBEXCHANGE,
							SECURITYTYPE, ESBSYMBOL, ESBSECURITYID, EXPDATE,
							ESBACCOUNT, QUANTITY, PRICE, STOPPRICE,
							ESBOPTIONTYPE, ESBSTRIKEPPRICE, ORDERTYPE,
							INSTANCEIDCUSTOMFIELD, REMARK, TIMEINFORCE, SIDE);
				} catch (Exception e) {
					e.printStackTrace();
				}

				break;

			case 8:
				System.out.print("Please Enter\nCustomer OrderID\n");
				INPUT = IN.next();
				orderData = new StringTokenizer(INPUT, ",");
				clientOrderID = orderData.nextToken();

				ESBORDERSTATUS = esbConnect.getOrderStatus(USERNAME,
						clientOrderID);

				System.out.println("ORDER STATUS : " + ESBORDERSTATUS);
				break;

			case 9:
				System.out.print("Please Enter\nCustomer OrderID\n");
				INPUT = IN.next();
				orderData = new StringTokenizer(INPUT, ",");
				clientOrderID = orderData.nextToken();

				ESBORDERSTATUS = esbConnect.getOrderStatusOfClientOrderID(
						USERNAME, clientOrderID);

				System.out.println("ORDER STATUS : " + ESBORDERSTATUS);
				break;

			case 10:
				openOrderFormated = esbConnect.getOpenOrdersFormated(USERNAME);
				System.out.println("OPEN ORDER : " + openOrderFormated);

				iter = openOrderFormated.iterator();
				while (iter.hasNext()) {
					REPORT = (byte[]) iter.next();
					System.out.println(REPORT.length);
					readByte(REPORT);
				}
				break;

			case 11:
				tradeHistoryFormated = esbConnect
						.getTradeHistoryFormated(USERNAME);
				System.out.println("TRADING HISTORY : " + tradeHistoryFormated);

				iter = tradeHistoryFormated.iterator();
				while (iter.hasNext()) {
					REPORT = (byte[]) iter.next();
					System.out.println(REPORT.length);
					readByte(REPORT);
				}
				break;

			case 12:
				System.out.print("Please Enter\nOriginal OrderID\n");
				ORIGINALORDERID = IN.next();
				try {
					tradeHistoryFormated = esbConnect
							.getTradeHistoryFormatedForClientOrderID(USERNAME,
									ORIGINALORDERID);
					System.out.println("TRADING HISTORY FORMATED : "
							+ tradeHistoryFormated);

					iter = tradeHistoryFormated.iterator();
					while (iter.hasNext()) {
						REPORT = (byte[]) iter.next();
						System.out.println(REPORT.length);
						readByte(REPORT);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;

			case 13:
				System.out.print("Please Enter\nOriginal OrderID\n");
				ORIGINALORDERID = IN.next();
				try {
					clientLastTradeHistory = esbConnect
							.getLastTradeHistoryFormatedForClientOrderID(
									USERNAME, ORIGINALORDERID);
					System.out
							.println("CLIENT'S FORMATED LAST TRADING HISTORY  : "
									+ clientLastTradeHistory);
					readByte(clientLastTradeHistory);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;

			case 14:
				System.out.print("Please Enter\nOriginal OrderID\n");
				ORIGINALORDERID = IN.next();
				try {
					clientLatestReport = esbConnect.getLatestReport(USERNAME,
							ORIGINALORDERID);
					System.out.println("LATEST REPORT : " + clientLatestReport);
					readByte(clientLatestReport);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;

			case 15:
				System.out.print("Please Enter\nOriginal OrderID,\n");
				ORIGINALORDERID = IN.next();
				try {
					allReportFormated = esbConnect.getAllReportsFormated(
							USERNAME, ORIGINALORDERID);
					System.out
							.println("FORMATED REPORT : " + allReportFormated);

					iter = allReportFormated.iterator();
					while (iter.hasNext()) {
						REPORT = (byte[]) iter.next();
						System.out.println(REPORT.length);
						readByte(REPORT);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;

			case 16:
				allReportFormated = esbConnect
						.getTradedHistoryFormated(USERNAME);
				System.out.println("FORMATED HISTORY : " + allReportFormated);

				iter = allReportFormated.iterator();
				while (iter.hasNext()) {
					REPORT = (byte[]) iter.next();
					System.out.println(REPORT.length);
					readByte(REPORT);
				}
				break;

			case 17:
				System.out.print("Please Enter\nOriginal OrderID\n");
				ORIGINALORDERID = IN.next();
				try {
					allReportFormated = esbConnect
							.getTradedHistoryFormatedForClientOrderID(USERNAME,
									ORIGINALORDERID);
					System.out
							.println("FORMATED REPORT : " + allReportFormated);

					iter = allReportFormated.iterator();
					while (iter.hasNext()) {
						REPORT = (byte[]) iter.next();
						System.out.println(REPORT.length);
						readByte(REPORT);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;

			case 18:
				System.out.print("Please Enter\nOriginal OrderID\n");
				ORIGINALORDERID = IN.next();
				try {
					clientOrderID = esbConnect.cancelOrder(USERNAME,
							ORIGINALORDERID);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;

			case 19:
				System.out
						.print("Please Enter\nOriginal OrderID,\nQuantity,"
								+ "\nPrice,\nOrder Type(LIMIT/MARKET/STOP LIMIT/STOP MARKET),\nStop Price\n");
				INPUT = IN.next();
				orderData = new StringTokenizer(INPUT, ",");
				clientOrderID = orderData.nextToken();
				QUANTITY = orderData.nextToken();
				PRICE = orderData.nextToken();
				ORDERTYPE = orderData.nextToken();
				STOPPRICE = orderData.nextToken();
				try {
					ESBSTOPPRICE = new BigDecimal(STOPPRICE);
					ESBPRICE = new BigDecimal(PRICE);

					if (ORDERTYPE.equalsIgnoreCase("limit")) {
						ESBORDERTYPE = OrderType.LIMIT;
						ESBSTOPPRICE = null;
					} else if (ORDERTYPE.equalsIgnoreCase("market")) {
						ESBORDERTYPE = OrderType.MARKET;
						ESBPRICE = null;
						ESBSTOPPRICE = null;
					} else if (ORDERTYPE.equalsIgnoreCase("stop limit")) {
						ESBORDERTYPE = OrderType.STOP_LIMIT;
					} else if (ORDERTYPE.equalsIgnoreCase("stop market")) {
						ESBORDERTYPE = OrderType.STOP;
						ESBPRICE = null;
					}

					System.out.println("STOP PRICE :" + ESBSTOPPRICE);
					System.out.println("PRICE :" + ESBPRICE);
					System.out.println("ORDER TYPE :" + ESBORDERTYPE);

					clientOrderID = esbConnect.ReplaceOrder(USERNAME,
							clientOrderID, new Integer(QUANTITY), ESBPRICE,
							ESBORDERTYPE, ESBSTOPPRICE);

				} catch (Exception e) {
					e.printStackTrace();
				}
				break;

			case 20:
				System.out
						.print("Please Enter\nOriginal OrderID,\nQuantity, \nPrice, \nOrder Type, \nStop Price, \nStrategy InstanceId,\nExternal InstanceId\n");
				INPUT = IN.next();
				orderData = new StringTokenizer(INPUT, ",");
				ORIGINALORDERID = orderData.nextToken();
				QUANTITY = orderData.nextToken();
				PRICE = orderData.nextToken();
				ORDERTYPE = orderData.nextToken();
				STOPPRICE = orderData.nextToken();
				STRATEGYINSTANCEID = orderData.nextToken();
				EXTERNALINSTANCEID = orderData.nextToken();

				clientOrderID = esbConnect.replaceOrderWithCustomField(
						USERNAME, ORIGINALORDERID, new Integer(QUANTITY),
						new BigDecimal(PRICE), OrderType.valueOf(ORDERTYPE),
						new BigDecimal(STOPPRICE), STRATEGYINSTANCEID,
						EXTERNALINSTANCEID);

				System.out.println("REPLACE ORDER WITH CUSTOM FIELDS :"
						+ clientOrderID);
				break;

			case 21:
				System.out
						.print("Please Enter\nOriginal OrderID,\nStrategy InstanceId,\nExternal InstanceId\n");
				INPUT = IN.next();
				orderData = new StringTokenizer(INPUT, ",");
				ORIGINALORDERID = orderData.nextToken();
				STRATEGYINSTANCEID = orderData.nextToken();
				EXTERNALINSTANCEID = orderData.nextToken();

				clientOrderID = esbConnect.cancelOrderWithCustomeFields(
						USERNAME, ORIGINALORDERID, STRATEGYINSTANCEID,
						EXTERNALINSTANCEID);

				System.out.println("CANCEL ORDER WITH CUSTOM FIELDS :"
						+ clientOrderID);
				break;

			case 22:
				position = esbConnect.getPositionAsOf(USERNAME);
				for (int i = 0; i < position.size(); i++) {
					System.out.println("ITERATOT :"
							+ position.get(i).getPosQty());
					System.out.println("ITERATOT :"
							+ position.get(i).getSymbol());
				}

				System.out.println("POSITION :" + position);
				break;

			case 23:
				connectionStatus = esbConnect.getConnectionStatus(USERNAME);
				System.out.println("CONNECTION STATUS :" + connectionStatus);
				break;

			case 24:
				esbConnect.logoutFromORS(USERNAME);
				System.out
						.println("#############################  LOGGED OUT...!!!!!\n");
				break;

			case 25:
				esbConnect.forceLogoutFromORS(USERNAME, PASSWORD);
				System.err
						.println("#############################  LOGGED OUT...!!!!!\n");
				break;

			case 26:
				List<String> brokers = null;
				brokers = esbConnect.getAvailableBrokers();
				for (int i = 0; i < brokers.size(); i++) {
					System.out.println("ITERATOT :" + brokers.get(i));
				}
				System.out.println(brokers.size());
				break;
			case 0:
				quit = true;
				break;
			default:
				esbConnect.setDealerName(USERNAME);
				esbConnect.setDealerPassword(PASSWORD);
				System.out.println("Invalid choice.");
			}

			Thread.sleep(1000);

		}
	}

	private static void userLoginRequest() {
		System.out.println("Username :");
		USERNAME = IN.next();
		System.out.println("Password :");
		PASSWORD = IN.next();
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

			StringTokenizer date = new StringTokenizer(EXPDATE, "-");
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

	public static void readByte(byte[] buff) {
		ByteOrder bo = ByteOrder.LITTLE_ENDIAN;
		ByteBuffer buffer = ByteBuffer.allocate(buff.length);
		buffer.order(bo);
		buffer.put(buff);
		System.out.println("buff.length ^^^^^^^^^^^^" + buff.length);
		byte[] userName = new byte[20];
		for (int i = 0; i < 20; i++) {
			userName[i] = buff[4 + i];
		}
		byte[] account = new byte[15];
		for (int i = 0; i < 15; i++) {
			account[i] = buff[24 + i];
		}
		byte[] orderID = new byte[30];
		for (int i = 0; i < 30; i++) {
			orderID[i] = buff[39 + i];
		}
		byte[] securityType = new byte[15];
		for (int i = 0; i < 15; i++) {
			securityType[i] = buff[69 + i];
		}
		byte[] exchange = new byte[15];
		for (int i = 0; i < 15; i++) {
			exchange[i] = buff[84 + i];
		}
		byte[] symbol = new byte[15];
		for (int i = 0; i < 15; i++) {
			symbol[i] = buff[99 + i];
		}
		byte[] optionType = new byte[2];
		for (int i = 0; i < 2; i++) {
			optionType[i] = buff[130 + i];
		}
		byte[] orderType = new byte[10];
		for (int i = 0; i < 10; i++) {
			orderType[i] = buff[132 + i];
		}
		byte[] sideType = new byte[10];
		for (int i = 0; i < 10; i++) {
			sideType[i] = buff[142 + i];
		}
		byte[] timeInForceByte = new byte[10];
		for (int i = 0; i < 10; i++) {
			timeInForceByte[i] = buff[152 + i];
		}
		byte[] orderStatus = new byte[15];
		for (int i = 0; i < 15; i++) {
			orderStatus[i] = buff[182 + i];
		}
		byte[] exchangeOrderNum = new byte[25];
		for (int i = 0; i < 25; i++) {
			exchangeOrderNum[i] = buff[197 + i];
		}
		byte[] exchangeTrade = new byte[50];
		for (int i = 0; i < 50; i++) {
			exchangeTrade[i] = buff[222 + i];
		}

		byte[] originalOrderIDByte = new byte[30];
		for (int i = 0; i < 30; i++) {
			originalOrderIDByte[i] = buff[300 + i];
		}

		System.out.println("username " + new String(userName) + "\naccount "
				+ new String(account) + " \norderID " + new String(orderID)
				+ " \nsecurityType " + new String(securityType)
				+ " \nexchange " + new String(exchange) + "\nsymbol "
				+ new String(symbol) + "\nexpiryDate " + buffer.getLong(110)
				+ " \nStrikeprice " + buffer.getDouble(118) + " \noptionType "
				+ new String(optionType) + "\norderType "
				+ new String(orderType) + " \nsideType " + new String(sideType)
				+ "\ntimeInForceByte " + new String(timeInForceByte)
				+ "\nOrderQuantity " + buffer.getInt(162) + "\nOrderPrice "
				+ buffer.getDouble(166) + "\nStopPrice "
				+ buffer.getDouble(174) + " \nOrderStatus "
				+ new String(orderStatus) + "\nexchangeOrderNum "
				+ new String(exchangeOrderNum) + "\nexchangeTrade "
				+ new String(exchangeTrade) + " \nExecutionprice "
				+ buffer.getDouble(272) + "\ncummulative Quantity "
				+ buffer.getInt(280) + " \nleavesQuantity "
				+ buffer.getInt(284) + "\nLast quantity " + buffer.getInt(288)
				+ "\nTransaction time " + buffer.getLong(292)
				+ "\nOriginalorderid " + new String(originalOrderIDByte)
				+ "\nBridgeSeqNo " + buffer.getInt(330) + "\nORSSeqNo "
				+ buffer.getInt(334));

		byte[] text = new byte[100];
		for (int i = 0; i < 100; i++) {
			text[i] = buff[338 + i];
		}
		System.out.println("\nText " + new String(text));
	}
}