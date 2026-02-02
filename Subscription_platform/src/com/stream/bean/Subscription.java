package com.stream.bean;

import java.util.Date;

public class Subscription {
	private int subscriptionID;
	private String userID;
	private String planCode;
	private Date startDate;
	private Date endDate;
	private String status;
	public int getSubscriptionID() {
		return subscriptionID;
	}
	public void setSubscriptionID(int subscriptionID) {
		this.subscriptionID = subscriptionID;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getPlanCode() {
		return planCode;
	}
	public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
