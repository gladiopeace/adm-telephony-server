package com.admtel.telephonyserver.requests;

import com.admtel.telephonyserver.requests.Request.RequestType;

public abstract class SwitchRequest extends Request{

	public SwitchRequest(){
		
	}
	public SwitchRequest(String switchId){
		this.switchId = switchId;
	}
	@Override
	public String toString() {
		return "SwitchRequest ["
				+ (switchId != null ? "switchId=" + switchId + ", " : "")
				+ (super.toString() != null ? "toString()=" + super.toString()
						: "") + "]";
	}
	String switchId;
	public String getSwitchId() {
		return switchId;
	}
	public void setSwitchId(String switchId) {
		this.switchId = switchId;
	}
	public SwitchRequest(RequestType type, String switchId) {
		super(type);		
		this.switchId = switchId;
	}

}
