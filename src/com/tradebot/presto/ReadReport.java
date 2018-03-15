package com.tradebot.presto;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;

public class ReadReport {
	// read variable length byte report.
	public void start(byte[] type) {
		StringBuffer itemString = new StringBuffer();
		itemString.delete(0, itemString.length());
		try {
			InputStream myInputStream = new ByteArrayInputStream(type);
			DataInputStream in = new DataInputStream(myInputStream);
			// System.out.println("Entered inside while in tester");
			short byteRead = in.readShort();
			// System.out.println("byte read ..."+byteRead);
			short headerMessage = in.readShort();
			byte[] buff = new byte[byteRead];
			// 4
			int byteReceived = in.read(buff);
			// System.out.println("byteReceived"+byteReceived);
			if (headerMessage == 1) {
				ByteOrder bo = ByteOrder.LITTLE_ENDIAN;
				ByteBuffer buffer = ByteBuffer.allocate(byteRead);
				buffer.order(bo);
				buffer.put(buff);
				buffer.position(0);
				System.out.println("buff.ldength ^^^^^^^^^^^^" + buff.length);
				byte[] userName = new byte[20];
				buffer.get(userName, 0, 20);
				String user = new String(userName);
				if (user != null)
					user = user.trim();
				System.out.println("user: " + user);
				byte[] account = new byte[15];
				buffer.get(account, 0, 15);
				String acc = new String(account);
				if (acc != null)
					acc = acc.trim();
				System.out.println("acc: " + acc);
				byte[] orderID = new byte[30];
				buffer.get(orderID, 0, 30);
				String orderId = new String(orderID);
				if (orderId != null)
					orderId = orderId.trim();
				System.out.println("orderId: " + orderId);
				byte[] securityType = new byte[15];
				buffer.get(securityType, 0, 15);
				String secType = new String(securityType);
				if (secType != null)
					secType = secType.trim();
				System.out.println("secType: " + secType);
				byte[] exchange = new byte[15];
				buffer.get(exchange, 0, 15);
				String exch = new String(exchange);
				if (exch != null)
					exch = exch.trim();
				System.out.println("exch: " + exch);
				byte[] symbol = new byte[15];
				buffer.get(symbol, 0, 15);
				String sym = new String(symbol);
				if (sym != null)
					sym = sym.trim();
				System.out.println("sym:" + sym);
				java.util.Date date = new java.util.Date(buffer.getLong());
				String expDtTemp = date.toString();
				SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
				String expiryDate = format.format(date);
				if (expiryDate != null)
					expiryDate = expiryDate.trim();
				System.out.println("expiryDate: " + expiryDate);
				String strikePrice = String.valueOf(buffer.getDouble());
				if (strikePrice != null)
					strikePrice = strikePrice.trim();
				System.out.println("strikePrice: " + strikePrice);
				byte[] optionType = new byte[2];
				buffer.get(optionType, 0, 2);
				String opType = new String(optionType);
				if (opType != null)
					opType = opType.trim();
				byte[] orderType = new byte[10];
				buffer.get(orderType, 0, 10);
				String ordType = new String(orderType);
				if (ordType != null)
					ordType = ordType.trim();
				System.out.println("ordType: " + ordType);
				byte[] sideType = new byte[10];
				buffer.get(sideType, 0, 10);
				String side = new String(sideType);
				if (side != null)
					side = side.trim();
				System.out.println("side: " + side);
				byte[] timeInForceByte = new byte[10];
				buffer.get(timeInForceByte, 0, 10);
				String timeInForce = new String(timeInForceByte);
				if (timeInForce != null)
					timeInForce = timeInForce.trim();
				System.out.println("timeInForce: " + timeInForce);
				String orderQty = String.valueOf(buffer.getInt());
				System.out.println("orderQty: " + orderQty);
				String orderPrice = String.valueOf(buffer.getDouble());
				System.out.println("orderPrice: " + orderPrice);
				String stopPrice = String.valueOf(buffer.getDouble());
				if (stopPrice != null)
					stopPrice = stopPrice.trim();
				byte[] orderStatus = new byte[15];
				buffer.get(orderStatus, 0, 15);
				String status = new String(orderStatus);
				if (status != null)
					status = status.trim();
				System.out.println("status: " + status);
				byte[] exchangeOrderNum = new byte[25];
				buffer.get(exchangeOrderNum, 0, 25);
				String exchOrderNum = new String(exchangeOrderNum);
				if (exchOrderNum != null)
					exchOrderNum = exchOrderNum.trim();
				System.out.println("exchangeOrderNum: " + exchOrderNum);
				byte[] exchangeTrade = new byte[50];
				buffer.get(exchangeTrade, 0, 50);
				String exchTradeNum = new String(exchangeTrade);
				if (exchTradeNum != null)
					exchTradeNum = exchTradeNum.trim();
				System.out.println("exchTradeNum: " + exchTradeNum);
				String execPrice = String.valueOf(buffer.getDouble());
				if (execPrice != null)
					execPrice = execPrice.trim();
				String cummQty = String.valueOf(buffer.getInt());
				System.out.println("cummQty: " + cummQty);
				String leavesQty = String.valueOf(buffer.getInt());
				System.out.println("leavesQty: " + leavesQty);
				String lastQty = String.valueOf(buffer.getInt());
				System.out.println("lastQty: " + lastQty);
				java.util.Date dateT = new java.util.Date(buffer.getLong());
				String transactionTime = dateT.toString();
				String transactTime = format.format(dateT);
				byte[] originalOrderIDByte = new byte[30];
				buffer.get(originalOrderIDByte, 0, 30);
				String origOrderId = new String(originalOrderIDByte);
				if (origOrderId != null)
					origOrderId = origOrderId.trim();
				System.out.println("origOrderId: " + origOrderId);
				int orsSeqNum = buffer.getInt();
				int esbSeqNum = buffer.getInt();
				byte[] text = new byte[100];
				buffer.get(text, 0, 100);
				String messageDesc = new String(text);
				byte[] strategyInstanceId = new byte[40];
				buffer.get(strategyInstanceId, 0, 40);
				String instanceID = new String(strategyInstanceId);
				if (instanceID != null)
					instanceID = instanceID.trim();
				byte[] externalStrategyId = new byte[40];
				buffer.get(externalStrategyId, 0, 40);
				String externalInstanceId = new String(externalStrategyId);
				if (externalInstanceId != null)
					externalInstanceId = externalInstanceId.trim();
				byte[] customText = new byte[200];
				buffer.get(customText, 0, 200);
				String customTextvalue = new String(customText);
				if (customTextvalue != null)
					customTextvalue = customTextvalue.trim();
				short orderSource = buffer.getShort();
				long securityID = buffer.getLong();
				byte[] terminalInfoArr = new byte[20];
				buffer.get(terminalInfoArr, 0, 20);
				String terminalInfo = new String(terminalInfoArr);
				if (terminalInfo != null)
					terminalInfo = terminalInfo.trim();
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// shouldContinue = false;
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// read fixed length byte report.
	public void readByte(byte[] buff) {
		ByteOrder bo = ByteOrder.LITTLE_ENDIAN;
		ByteBuffer buffer = ByteBuffer.allocate(434);
		buffer.order(bo);
		buffer.put(buff);
		System.out.println("buff.length ^^^^^^^^^^^^" + buff.length);
		byte[] userName = new byte[20];
		for (int i = 0; i < 20; i++) {
			userName[i] = buff[i];
		}
		System.out.println("username " + new String(userName));
		byte[] account = new byte[15];
		for (int i = 0; i < 15; i++) {
			account[i] = buff[20 + i];
		}
		System.out.println("account " + new String(account));
		byte[] orderID = new byte[30];
		for (int i = 0; i < 30; i++) {
			orderID[i] = buff[35 + i];
		}
		byte[] securityType = new byte[15];
		for (int i = 0; i < 15; i++) {
			securityType[i] = buff[65 + i];
		}
		byte[] exchange = new byte[15];
		for (int i = 0; i < 15; i++) {
			exchange[i] = buff[80 + i];
		}
		byte[] symbol = new byte[15];
		for (int i = 0; i < 15; i++) {
			symbol[i] = buff[95 + i];
		}
		byte[] optionType = new byte[2];
		for (int i = 0; i < 2; i++) {
			optionType[i] = buff[126 + i];
		}
		byte[] orderType = new byte[10];
		for (int i = 0; i < 10; i++) {
			orderType[i] = buff[128 + i];
		}
		byte[] sideType = new byte[10];
		for (int i = 0; i < 10; i++) {
			sideType[i] = buff[138 + i];
		}
		byte[] timeInForceByte = new byte[10];
		for (int i = 0; i < 10; i++) {
			timeInForceByte[i] = buff[148 + i];
		}
		byte[] orderStatus = new byte[15];
		for (int i = 0; i < 10; i++) {
			orderStatus[i] = buff[178 + i];
		}
		byte[] exchangeOrderNum = new byte[25];
		for (int i = 0; i < 10; i++) {
			exchangeOrderNum[i] = buff[193 + i];
		}
		byte[] exchangeTrade = new byte[50];
		for (int i = 0; i < 10; i++) {
			exchangeTrade[i] = buff[218 + i];
		}
		System.out.println("username " + new String(userName) + " account"
				+ new String(account) + " orderID " + new String(orderID)
				+ " securityType " + new String(securityType) + " exchange "
				+ new String(exchange) + " symbol " + new String(symbol)
				+ " expiryDate" + buffer.getLong(110) + " Strikeprice "
				+ buffer.getDouble(118) + " optionType"
				+ new String(optionType) + " orderType "
				+ new String(orderType) + " sideType " + new String(sideType)
				+ " timeInForceByte " + new String(timeInForceByte)
				+ " OrderQuantity " + buffer.getInt(158) + " OrderPrice" + ""
				+ buffer.getDouble(162) + " StopPrice " + buffer.getDouble(170)
				+ " OrderStatus " + new String(orderStatus)
				+ " exchangeOrderNum " + new String(exchangeOrderNum)
				+ "exchangeTrade " + new String(exchangeTrade)
				+ " Executionprice " + buffer.getDouble(268)
				+ " cummulative Quantity" + buffer.getInt(276)
				+ " leavesQuantity " + buffer.getInt(280) + " Last quantity "
				+ buffer.getInt(284) + " Transaction time"
				+ buffer.getLong(288));
		byte[] originalOrderIDByte = new byte[30];
		for (int i = 0; i < 30; i++) {
			originalOrderIDByte[i] = buff[296 + i];
		}
		System.out
				.println("Originalorderid " + new String(originalOrderIDByte));
		byte[] text = new byte[100];
		for (int i = 0; i < 100; i++) {
			text[i] = buff[326 + i];
		}
	}
}
