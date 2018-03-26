package com.tradebot.presto;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import com.sft.feedprovider.Executor;
import com.sft.feedprovider.FeedService;
import com.sft.feedprovider.MarketDataProvider;

public class TestClient implements FeedService {
	static String sleep = "1";

	public static void main(String[] args) {
		new TestClient().feedrequest();
	}

	public void feedrequest() {
		Executor executor = new Executor();
		executor.start(this);
		executor.subscribe("20188", ""); // acc
		executor.subscribe("2885", ""); // bankindia
		System.out.println(executor.getProperties().toString());
		try {
			Thread.sleep(Long.parseLong(sleep) * 2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//executor.unSubscribe("2885", "");
		//executor.unSubscribe("20188", "");
		//System.out.println("-------------stopping---------");
		//executor.stop();
	}

	// public void start() {
	//
	// }

	@Override
	public void onFeed(MarketDataProvider subject, String symbol) {
		
		System.out.println("-----symbol: " + symbol);
		BigDecimal ltp = new BigDecimal(subject.getLastTradePrice(symbol));
		System.out.println("------LTP: " + ltp);
		System.out.print(subject.toString());
		//Calendar c = Calendar.getInstance();
		//Date date = new Date("12/31/1979 23:59:59");
		//SimpleDateFormat monthyearDayCon = new SimpleDateFormat("yyyyMMdd hh:mm:ss a");
		//TimeZone istTime = TimeZone.getTimeZone("IST");
		//monthyearDayCon.setTimeZone(istTime);
		//c.setTime(date);
		//long time = subject.getLastTradeTime(symbol);
		// System.out.println("------LTT: "+time);
		//Date date1 = new Date((time * 1000) + c.getTimeInMillis());
		//System.out.println("------LTT: date1: " + monthyearDayCon.format(date1));
		System.out.println("------NEXT--------");

		/*
		 * List<OrderByPrice> askOrderByPrice = new ArrayList<OrderByPrice>(
		 * subject.getAsk(symbol)); List<OrderByPrice> bidOrderByPrice = new
		 * ArrayList<OrderByPrice>( subject.getBid(symbol)); double askPrice =
		 * askOrderByPrice.get(0).orderPrice; double bidPrice =
		 * bidOrderByPrice.get(0).orderPrice;
		 */// System.out.println("-----askPrice: "+askPrice);
		// //System.out.println("-----bidPrice: "+bidPrice);
		// //System.out.println("-----volume: "+subject.getVolume(symbol));
	}
}