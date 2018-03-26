package com.tradebot.presto;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.util.List;

import org.marketcetera.presto.trade.Side;
import org.marketcetera.presto.trade.TimeInForce;
import org.marketcetera.types.trade.OrderStatus;
import org.marketcetera.types.trade.OrderType;

import com.symphonyfintech.esb.facade.ExecutionReportBean;
import com.symphonyfintech.esb.facade.PrestoExternalClient;
import com.symphonyfintech.esb.jms.listener.IReportListener;
import com.symphonyfintech.gateway.ClientInitException_Exception;
import com.symphonyfintech.gateway.ConnectionException_Exception;
import com.symphonyfintech.gateway.ESBException_Exception;
import com.symphonyfintech.gateway.EsbOrder;
import com.symphonyfintech.gateway.MessageCreationException_Exception;
import com.symphonyfintech.gateway.NoMoreIDsException_Exception;
import com.symphonyfintech.gateway.PositionBean;
import com.symphonyfintech.gateway.RemoteException_Exception;
import com.symphonyfintech.gateway.SecurityType;
import com.symphonyfintech.gateway.SymbolDetail;

public class EsbConnection implements IReportListener {
	private List<byte[]> openOrderFormated = null;
	private byte[] clientLastTradeHistory, clientLatestReport;
	private OrderType esbOrderType;
	private String orderType;
	private SecurityType esbSecurityType;
	private String securityType;
	private String esbSymbol;
	private String esbAccount;
	private Side esbSide;
	private String side;
	private TimeInForce esbTimeInForce;
	private String timeInForce;
	private BigDecimal esbStopPrice;
	private BigDecimal esbPrice;
	private Integer esbQuantity;
	private Date esbExpDate;
	private String expDate;
	private OrderStatus orderStatus;
	private String ipAddress;
	private String portNo;
	private String dealerName;
	private String dealerPassword;
	private String exchange;
	private String instrumentType;
	private PrestoExternalClient pcObject;
	private SymbolDetail symbolDetails;
	private List<SymbolDetail> symbolDetail;
	private List<byte[]> openOrder, tradeHistory;

	public void initialize(IReportListener iReportListener) {
		pcObject = new PrestoExternalClient(iReportListener);
	}

	public boolean loginToOrs(String dealerName, String dealerPassword) {
		boolean isSuccess = false;
		try {
			isSuccess = pcObject.loginToORS(dealerName, dealerPassword);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ConnectionException_Exception e) {
			e.printStackTrace();
		} catch (ESBException_Exception e) {
			e.printStackTrace();
		} catch (NoMoreIDsException_Exception e) {
			e.printStackTrace();
		} catch (RemoteException_Exception e) {
			e.printStackTrace();
		}
		if (isSuccess) {
			System.out.println("in loginToOrs(service wrapper)..." + isSuccess
					+ "\n");
		}
		return isSuccess;
	}

	public void logoutFromORS(String dealerName) {
		try {
			pcObject.logoutFromORS(dealerName);
		} catch (ESBException_Exception e) {
			e.printStackTrace();
		}
	}

	public List<PositionBean> getPositionAsOf(String dealerName) {
		List<PositionBean> position = null;
		try {
			position = pcObject.getPositionAsOf(dealerName);
		} catch (ClientInitException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ConnectionException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ESBException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return position;
	}

	public boolean getConnectionStatus(String dealerName) {
		boolean isSuccess = false;

		try {
			isSuccess = pcObject.getConnectionStatus(dealerName);
		} catch (ESBException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return isSuccess;
	}

	// public List<byte[]> getOpenOrder(String dealerName) {
	// try {
	// openOrder = pcObject.getOpenOrdersFormated(dealerName);
	// } catch (ConnectionException_Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (ESBException_Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (MessageCreationException_Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// return openOrder;
	// }

	// public List<ReportHolder> getTradeHistory(String dealerName) {
	// try {
	// tradeHistory = pcObject.getTradedHistory(dealerName);
	// } catch (ConnectionException_Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (ESBException_Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// return tradeHistory;
	// }

	public String placeOrder(String dealerName, EsbOrder esbOrder) {
		String clientId = null;
		try {
			clientId = pcObject.placeOrder(dealerName, esbOrder);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return clientId;

	}

	public List<SymbolDetail> scriptsForExchangeInstrType(String exchange,
			String instrumentType) {
		try {
			symbolDetail = pcObject.scriptsForExchangeInstrType(exchange,
					instrumentType);
		} catch (ESBException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return symbolDetail;
	}

	public List<SymbolDetail> getSymbolDetailsDerivativeOPT(String exchange,
			String symbol, String expiry, String optionType) {
		try {
			symbolDetail = pcObject.getSymbolDetailsDerivativeOPT(exchange,
					symbol, expiry, optionType);
		} catch (ESBException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return symbolDetail;
	}

	public List<SymbolDetail> scriptsForExchange(String exchange) {
		try {
			symbolDetail = pcObject.scriptsForExchange(exchange);
		} catch (ESBException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return symbolDetail;
	}

	public List<String> getAvailableBrokers() {
		List<String> availableBrokers = null;
		try {
			availableBrokers = pcObject.getAvailableBrokers();
		} catch (ESBException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return availableBrokers;
	}

	public SymbolDetail getSymbolDetails(String exchange, String securityID) {
		try {
			try {
				symbolDetails = pcObject.getSymbolDetail(exchange, securityID);
			} catch (ConnectionException_Exception e) {
				e.printStackTrace();
			}
		} catch (ESBException_Exception e) {
			e.printStackTrace();
		}
		return symbolDetails;
	}

	public List<SymbolDetail> getSymbolDetailsForInstrType(String exchange,
			String symbol, String instrumentType, String segment) {

		try {
			symbolDetail = pcObject.getSymbolDetailsForInstrType(exchange,
					symbol, instrumentType, segment);
		} catch (ESBException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return symbolDetail;
	}

	public OrderStatus getOrderStatus(String dealerName, String clientOrderId) {
		try {
			orderStatus = pcObject.getOrderStatus(dealerName, clientOrderId);
		} catch (ESBException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ConnectionException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return orderStatus;
	}

	public OrderStatus getOrderStatusOfClientOrderID(String dealerName,
			String clientOrderId) {
		try {
			orderStatus = pcObject.getOrderStatusOfClientOrderID(dealerName,
					clientOrderId);
		} catch (ESBException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ConnectionException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return orderStatus;
	}

	public List<byte[]> getOpenOrdersFormated(String dealerName) {
		try {
			openOrderFormated = pcObject.getOpenOrdersFormated(dealerName);
		} catch (ConnectionException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ESBException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessageCreationException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return openOrderFormated;
	}

	public List<byte[]> getTradedHistoryFormated(String dealerName) {
		List<byte[]> allHistory = null;
		try {
			allHistory = pcObject.getTradedHistoryFormated(dealerName);
		} catch (ConnectionException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ESBException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessageCreationException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return allHistory;
	}

	public List<byte[]> getTradedHistoryFormatedForClientOrderID(
			String dealerName, String clientOrderId) {
		List<byte[]> allReports = null;
		try {
			allReports = pcObject.getTradedHistoryFormatedForClientOrderID(
					dealerName, clientOrderId);
		} catch (ConnectionException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ESBException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessageCreationException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return allReports;
	}

	public List<byte[]> getAllReportsFormated(String dealerName,
			String clientOrderId) {
		try {
			openOrderFormated = pcObject.getAllReportsFormated(dealerName,
					clientOrderId);
		} catch (ConnectionException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ESBException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessageCreationException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return openOrderFormated;
	}

	public List<byte[]> getTradeHistoryFormated(String dealerName) {
		try {
			openOrderFormated = pcObject.getTradeHistoryFormated(dealerName);
		} catch (ConnectionException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ESBException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessageCreationException_Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return openOrderFormated;
	}

	public List<byte[]> getTradeHistoryFormatedForClientOrderID(
			String dealerName, String clientOrderId) {
		try {
			openOrderFormated = pcObject
					.getTradeHistoryFormatedForClientOrderID(dealerName,
							clientOrderId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return openOrderFormated;
	}

	public byte[] getLastTradeHistoryFormatedForClientOrderID(
			String dealerName, String clientOrderId) {
		try {
			clientLastTradeHistory = pcObject
					.getLastTradeHistoryFormatedForClientOrderID(dealerName,
							clientOrderId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return clientLastTradeHistory;
	}

	public byte[] getLatestReport(String dealerName, String clientOrderId) {
		try {
			clientLatestReport = pcObject.getLastestReport(dealerName,
					clientOrderId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return clientLatestReport;
	}

	public String ReplaceOrder(String dealerName, String clientOrderId,
			Integer newQty, BigDecimal newPrice, OrderType newOrdType,
			BigDecimal newStopPx) {
		String clientId = null;
		try {
			clientId = pcObject.replaceOrder(dealerName, clientOrderId, newQty,
					newPrice, newOrdType, newStopPx);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return clientId;
	}

	public String cancelOrder(String dealerName, String clientOrderId) {
		String clientId = null;
		try {
			clientId = pcObject.cancelOrder(dealerName, clientOrderId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return clientId;
	}

	public String cancelOrderWithCustomeFields(String dealerName,
			String clientOrderId, String instanceId, String xternalInstanceId) {
		String clientId = null;
		try {
			clientId = pcObject.cancelOrderWithCustomField(dealerName,
					clientOrderId, instanceId, xternalInstanceId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return clientId;
	}

	public String replaceOrderWithCustomField(String dealerName,
			String clientOrderId, Integer quantity, BigDecimal price,
			OrderType odrType, BigDecimal stopPx, String instanceId,
			String xternalInstanceId) {
		String clientId = null;
		try {
			clientId = pcObject.replaceOrderWithCustomField(dealerName,
					clientOrderId, quantity, price, odrType, stopPx,
					instanceId, xternalInstanceId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return clientId;
	}

	public void forceLogoutFromORS(String dealerName, String dealerPassword) {
		try {
			pcObject.forceLogoutFromORS(dealerName, dealerPassword);
		} catch (ESBException_Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getSecurityType() {
		return securityType;
	}

	public void setSecurityType(String securityType) {
		this.securityType = securityType;
	}

	public OrderType getEsbOrderType() {
		return esbOrderType;
	}

	public void setEsbOrderType(OrderType esbOrderType) {
		this.esbOrderType = esbOrderType;
	}

	public SecurityType getEsbSecurityType() {
		return esbSecurityType;
	}

	public void setEsbSecurityType(SecurityType esbSecurityType) {
		this.esbSecurityType = esbSecurityType;
	}

	public String getEsbSymbol() {
		return esbSymbol;
	}

	public void setEsbSymbol(String esbSymbol) {
		this.esbSymbol = esbSymbol;
	}

	public String getEsbAccount() {
		return esbAccount;
	}

	public void setEsbAccount(String esbAccount) {
		this.esbAccount = esbAccount;
	}

	public Side getEsbSide() {
		return esbSide;
	}

	public void setEsbSide(Side esbSide) {
		this.esbSide = esbSide;
	}

	public String getSide() {
		return side;
	}

	public void setSide(String side) {
		this.side = side;
	}

	public TimeInForce getEsbTimeInForce() {
		return esbTimeInForce;
	}

	public void setEsbTimeInForce(TimeInForce esbTimeInForce) {
		this.esbTimeInForce = esbTimeInForce;
	}

	public String getTimeInForce() {
		return timeInForce;
	}

	public void setTimeInForce(String timeInForce) {
		this.timeInForce = timeInForce;
	}

	public BigDecimal getEsbStopPrice() {
		return esbStopPrice;
	}

	public void setEsbStopPrice(BigDecimal esbStopPrice) {
		this.esbStopPrice = esbStopPrice;
	}

	public BigDecimal getEsbPrice() {
		return esbPrice;
	}

	public void setEsbPrice(BigDecimal esbPrice) {
		this.esbPrice = esbPrice;
	}

	public Integer getEsbQuantity() {
		return esbQuantity;
	}

	public void setEsbQuantity(Integer esbQuantity) {
		this.esbQuantity = esbQuantity;
	}

	public Date getEsbExpDate() {
		return esbExpDate;
	}

	public void setEsbExpDate(Date esbExpDate) {
		this.esbExpDate = esbExpDate;
	}

	public String getExpDate() {
		return expDate;
	}

	public void setExpDate(String expDate) {
		this.expDate = expDate;
	}

	public String getExchange() {
		return exchange;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	public String getInstrumentType() {
		return instrumentType;
	}

	public void setInstrumentType(String instrumentType) {
		this.instrumentType = instrumentType;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getPortNo() {
		return portNo;
	}

	public void setPortNo(String portNo) {
		this.portNo = portNo;
	}

	public String getDealerName() {
		return dealerName;
	}

	public void setDealerName(String dealerName) {
		this.dealerName = dealerName;
	}

	public String getDealerPassword() {
		return dealerPassword;
	}

	public void setDealerPassword(String dealerPassword) {
		this.dealerPassword = dealerPassword;
	}

	public PrestoExternalClient getPrestoExternalClientObject() {
		return pcObject;
	}

	public void setPrestoExternalClientObject(PrestoExternalClient pcObject) {
		this.pcObject = pcObject;
	}

	@Override
	public void onReportReceived(ExecutionReportBean arg0) {
		// TODO Auto-generated method stub

	}
}
