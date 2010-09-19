package com.admtel.telephonyserver.remoteapi;

public class InboundAlertingMessage extends Message {
	String switchId;
	String channelId;
	String calledIdNumber;
	String callerIdNumber;
	String callerIdName;
	public String getSwitchId() {
		return switchId;
	}
	public void setSwitchId(String switchId) {
		this.switchId = switchId;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public String getCalledIdNumber() {
		return calledIdNumber;
	}
	public void setCalledIdNumber(String calledIdNumber) {
		this.calledIdNumber = calledIdNumber;
	}
	public String getCallerIdNumber() {
		return callerIdNumber;
	}
	public void setCallerIdNumber(String callerIdNumber) {
		this.callerIdNumber = callerIdNumber;
	}
	public String getCallerIdName() {
		return callerIdName;
	}
	public void setCallerIdName(String callerIdName) {
		this.callerIdName = callerIdName;
	}
}
