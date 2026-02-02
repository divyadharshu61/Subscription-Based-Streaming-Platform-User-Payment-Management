package com.stream.bean;

import java.util.Date;

public class Payment {
	private long paymentID;
	private int subscriptionID;
	private double amount;
	private Date paymentDate;
	private String paymentMethod;
	private String status;
	public long getPaymentID() {
		return paymentID;
	}
	public void setPaymentID(long paymentID) {
		this.paymentID = paymentID;
	}
	public int getSubscriptionID() {
		return subscriptionID;
	}
	public void setSubscriptionID(int subscriptionID) {
		this.subscriptionID = subscriptionID;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public Date getPaymentDate() {
		return paymentDate;
	}
	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}
	public String getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
