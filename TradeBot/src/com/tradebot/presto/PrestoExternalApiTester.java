package com.tradebot.presto;

import java.math.BigDecimal;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.marketcetera.types.trade.OrderType;
import org.marketcetera.types.trade.Side;
import org.marketcetera.types.trade.TimeInForce;
import com.symphonyfintech.esb.facade.PrestoExternalClient;
import com.symphonyfintech.gateway.ConnectionException_Exception;
import com.symphonyfintech.gateway.ESBException_Exception;
import com.symphonyfintech.gateway.EsbOrder;
import com.symphonyfintech.gateway.ReportHolder;
import com.symphonyfintech.gateway.SecurityType;
import com.symphonyfintech.gateway.StrategyNotStartedException_Exception;

public class PrestoExternalApiTester {
	public static void main(String[] args) {
		EsbConnection esbConnect = new EsbConnection();
		esbConnect.setDealerName("nikul");
		esbConnect.setDealerPassword("sft@12345");
//		esbConnect.setIpAddress("192.168.50.44");
//		esbConnect.setPortNo("9100");
		ReportHandler reportHandler = new ReportHandler();
		esbConnect.initialize(reportHandler);
//		esbConnect.loginToOrs();
//		String orderIdGotOfOrderPlace = null;
//		try {
//			orderIdGotOfOrderPlace = esbConnect.getPrestoExternalClientObject()
//					.placeOrder("nikul", sendOrder());
//		} catch (ESBException_Exception e) {
//			e.printStackTrace();
//		} catch (StrategyNotStartedException_Exception e) {
//			e.printStackTrace();
//		}
//		System.out.println(orderIdGotOfOrderPlace);
		printAllReportOfDealer(esbConnect.getDealerName(), esbConnect
				.getPrestoExternalClientObject());
	}

	public static EsbOrder sendOrder(){
		EsbOrder esborder = new EsbOrder();
		//Set BrokerName
		esborder.setSecurityID("1234");
		esborder.setEsbexchange("nsefo_sim");
		esborder.setEsborderType(OrderType.LIMIT);
		esborder.setEsbsymbol("NIFTY");//optional if securityID is set
		esborder.setEsbprice(new BigDecimal("6000"));
		esborder.setEsbquantity(50);
		esborder.setEsbside(Side.BUY);
		Date date=new Date("30", "01", "2014");
		XMLGregorianCalendar xgcal = null;
		GregorianCalendar gcal = (GregorianCalendar)
		GregorianCalendar.getInstance();
		try {
		xgcal = DatatypeFactory.newInstance()
		.newXMLGregorianCalendar(gcal);
		} catch (DatatypeConfigurationException e2) {
		e2.printStackTrace();
		}
		if (date != null) {
		xgcal.setDay(Integer.parseInt(date.getDay()));
		xgcal.setMonth(Integer.parseInt(date.getMonth()));
		xgcal.setYear(Integer.parseInt(date.getYear()));
		esborder.setEsbexpiry(xgcal);
		}
		esborder.setEsbsecurityType(SecurityType.FUTURE);//optional if
		// securityID is set
		esborder.setEsbtimeInForce(TimeInForce.DAY);
		esborder.setStrikePrice("null");//optional if securityID is set
		esborder.setOptionType("null");//optional if securityID is set
		esborder.setEsbstopPrice(null);
		esborder.setEsbaccount("nk28");
		esborder.setEsbuser("nikul");
		esborder.setInstanceIdCustomField("MyStartegy");
		esborder.setRemark("Unknown");
		return esborder;
		}
	
	public static void printAllReportOfDealer(String delaerName,
			PrestoExternalClient eclient) {
		List<ReportHolder> tradedHistoryOfDealer = null;
		try {
			tradedHistoryOfDealer = eclient.getTradeHistory("nikul");
		} catch (ConnectionException_Exception e) {
			e.printStackTrace();
		} catch (ESBException_Exception e) {
			e.printStackTrace();
		}
		for (Iterator<ReportHolder> iterator = tradedHistoryOfDealer.iterator(); iterator
				.hasNext();) {
			ReportHolder reportHolder = iterator.next();
			System.out.println(reportHolder.getReportOrderStatus());
		}
	}
}
