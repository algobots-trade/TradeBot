package com.tradebot.presto;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.pmw.tinylog.Logger;

import com.sft.feedprovider.Executor;
import com.sft.feedprovider.FeedService;
import com.sft.feedprovider.MCXMarketPicture.OrderByPrice;
import com.sft.feedprovider.MarketDataProvider;
import com.tradebot.dbcommons.db_commons;
import com.tradebot.dbcommons.tradebot_utility;
import com.tradebot.formulas.F1_HRun_Algo;
import com.tradebot.formulas.F2_HCapture_Algo;
import com.tradebot.formulas.F4_HRun_Algo;
import com.tradebot.formulas.F5_HCapture_Algo;
import com.tradebot.formulas.F6_HRun_Algo;
import com.tradebot.formulas.F7_HCapture_Algo;
import java.sql.Connection;
import java.sql.SQLException;

public class presto_data_feeder implements FeedService {
	private String configprop=System.getProperty("user.dir")+File.separator+"resource"+File.separator+"config.properties";
	String tradelogpath;
	tradebot_utility utils = new tradebot_utility(); 
	db_commons dbobj=new db_commons();
	static String sleep = "1";
	Executor executor;
	presto_commons objPresto;
	Connection conn;
	
	public void presto_start_data_feeder(String headfeedsecid[][]) 
	{
		// TODO Auto-generated constructor stub
		tradelogpath = utils.configlogfile("TRADEBOT_LOG");
		conn = dbobj.CheckandConnectDB(conn);
		try
		{
			executor = new Executor();
			executor.start(this);
			for(int i =0; i<headfeedsecid.length; i++)
			{
				executor.subscribe(headfeedsecid[i][0], headfeedsecid[i][1]);
				objPresto = new presto_commons();
				Logger.info("Subscribed - "+headfeedsecid[i][0]+" , "+ headfeedsecid[i][1]);
			}
			try {
				Thread.sleep(Long.parseLong(sleep) * 2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		catch(Exception ex)
		{
			Logger.error(ex.toString());
		}
	}
	
	public void presto_stop_data_feeder(String headfeedsecid[][]) 
	{
		// TODO Auto-generated constructor stub
		try
		{
			for(int i =0; i<headfeedsecid.length; i++)
			{
				executor.unSubscribe(headfeedsecid[i][0], headfeedsecid[i][1]);
				Logger.info("Unsubscribed - "+headfeedsecid[i][0]+" , "+ headfeedsecid[i][1]);
			}
			executor.stop();
		}
		catch(Exception ex)
		{
			Logger.error(ex.toString());
		}
	}

	@Override
	public void onFeed(MarketDataProvider subject, String symbol) 
	{
		// TODO Auto-generated method stub
		Calendar c = Calendar.getInstance();
		Date date = new Date("12/31/1979 23:59:59");
		SimpleDateFormat monthyearDayCon = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		TimeZone istTime = TimeZone.getTimeZone("IST");
		monthyearDayCon.setTimeZone(istTime);
		c.setTime(date);
		long time = subject.getLastTradeTime(symbol);
		Date dtformat = new Date((time * 1000) + c.getTimeInMillis());
		
		String value =String.valueOf(subject.getLastTradePrice(symbol)).replace(".0", "");//.split(".")[0];
		int interval = value.length() - 2;
		char separator = '.';

		StringBuilder sb = new StringBuilder(value);

		for(int i = 0; i < value.length() / interval; i++) {
		    sb.insert(((i + 1) * interval) + i, separator);
		}

		Double ltp = Double.valueOf(sb.toString());
		//System.out.println("------LTP: " + ltp);
		List <OrderByPrice> aksvalues =  subject.getAsk(symbol);
		int asksize=0;
		for(OrderByPrice ask : aksvalues)
		{
			asksize = asksize + (int) ask.quantity;
		}
		
		List <OrderByPrice> bidvalues =  subject.getBid(symbol);
		int bidsize=0;
		for(OrderByPrice bid : bidvalues)
		{
			bidsize = bidsize + (int) bid.quantity;
		}
		
		System.out.print("\n"+symbol + " , LTP : " + ltp + " , ASK VOLUME : " + asksize +  " , BID VALOUME : " + bidsize +" , LTT :  " + monthyearDayCon.format(dtformat));
		try {
			if (conn.isClosed())
			{
				conn = dbobj.CheckandConnectDB(conn);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			Logger.error(e);
			e.printStackTrace();
		}
		F1_HRun_Algo f1algo = new F1_HRun_Algo(conn, objPresto, symbol,ltp, asksize,bidsize, monthyearDayCon.format(dtformat));
		F2_HCapture_Algo f2algo = new F2_HCapture_Algo(conn,objPresto, symbol,ltp, asksize,bidsize, monthyearDayCon.format(dtformat));
		F4_HRun_Algo f4algo = new F4_HRun_Algo(conn,objPresto, symbol,ltp, asksize,bidsize, monthyearDayCon.format(dtformat));
		F5_HCapture_Algo f5algo = new F5_HCapture_Algo(conn,objPresto, symbol,ltp, asksize,bidsize, monthyearDayCon.format(dtformat));
		F6_HRun_Algo f6algo = new F6_HRun_Algo(conn,objPresto, symbol,ltp, asksize,bidsize, monthyearDayCon.format(dtformat));
		F7_HCapture_Algo f7algo = new F7_HCapture_Algo(conn,objPresto, symbol,ltp, asksize,bidsize, monthyearDayCon.format(dtformat));
		
		
	}

}
