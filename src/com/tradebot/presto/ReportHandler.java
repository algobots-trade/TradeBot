package com.tradebot.presto;

import com.symphonyfintech.esb.facade.ExecutionReportBean;
import com.symphonyfintech.esb.jms.listener.IReportListener;

public class ReportHandler implements IReportListener {

	@Override
	public void onReportReceived(ExecutionReportBean reportMessage) {
		System.out.println("ACCOUNT ::  "+reportMessage.getAccount());
	}

}