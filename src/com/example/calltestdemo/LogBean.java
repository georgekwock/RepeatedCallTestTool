package com.example.calltestdemo;

import java.sql.Date;

public class LogBean {

	private boolean Isoutingcall;
	private String AnswerTime;
	private String EndCallTime;
	private boolean CallStatus;
	private boolean isAdded;
	private Date mAlertingTime;
	private Date mDisconnectingTime;

	public boolean isIsoutingcall() {
		return Isoutingcall;
	}

	public void setIsoutingcall(boolean isoutingcall) {
		Isoutingcall = isoutingcall;
	}

	public String getAnswerTime() {
		return AnswerTime;
	}

	public void setAnswerTime(String answerTime) {
		AnswerTime = answerTime;
	}

	public String getEndCallTime() {
		return EndCallTime;
	}

	public void setEndCallTime(String endCallTime) {
		EndCallTime = endCallTime;
	}

	public boolean isCallStatus() {
		return CallStatus;
	}

	public void setCallStatus(boolean callStatus) {
		CallStatus = callStatus;
	}

	public boolean isAdded() {
		return isAdded;
	}

	public void setAdded(boolean isAdded) {
		this.isAdded = isAdded;
	}

	public Date getmAlertingTime() {
		return mAlertingTime;
	}

	public void setmAlertingTime(Date mAlertingTime) {
		this.mAlertingTime = mAlertingTime;
	}

	public Date getmDisconnectingTime() {
		return mDisconnectingTime;
	}

	public void setmDisconnectingTime(Date mDisconnectingTime) {
		this.mDisconnectingTime = mDisconnectingTime;
	}

}
