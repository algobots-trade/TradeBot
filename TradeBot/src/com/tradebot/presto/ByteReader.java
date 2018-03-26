package com.tradebot.presto;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ByteReader {
	public static void readByte(byte[] buff) {
		ByteOrder bo = ByteOrder.LITTLE_ENDIAN;
		ByteBuffer buffer = ByteBuffer.allocate(748);
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
