package com.admtel.telephonyserver.radius;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AuthorizeResult {
	
	String userName;
	Boolean authorized = false;
	Integer allowedTime = 0;
	BigDecimal credit = BigDecimal.ZERO;

	public BigDecimal getCredit() {
		return credit;
	}
	public void setCredit(BigDecimal credit) {
		this.credit = credit;
	}
	public Map<String, Object> getAttributes() {
		return attributes;
	}
	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}
	Map<String, Object> attributes = new HashMap<String, Object>();
	
	public List<String> getRoutes() {
		return (List<String>) attributes.get("routes");
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
	public Boolean getAuthorized() {
		return authorized;
	}
	public void setAuthorized(Boolean authorized) {
		this.authorized = authorized;
	}
	public Integer getAllowedTime() {
		return allowedTime;
	}
	public void setAllowedTime(Integer allowedTime) {
		this.allowedTime = allowedTime;
	}
	public Object get(String key){
		return attributes.get(key);
	}
	public void put(String key, Object value){
		attributes.put(key, value);
	}
	public void setRoutes(ArrayList arrayList) {
		put("routes", arrayList);
		
	}
	@Override
	public String toString() {
		final int maxLen = 20;
		return "AuthorizeResult ["
				+ (allowedTime != null ? "allowedTime=" + allowedTime + ", "
						: "")
				+ (attributes != null ? "attributes="
						+ toString(attributes.entrySet(), maxLen) + ", " : "")
				+ (authorized != null ? "authorized=" + authorized + ", " : "")
				+ (userName != null ? "userName=" + userName : "") + "]";
	}
	private String toString(Collection<?> collection, int maxLen) {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		int i = 0;
		for (Iterator<?> iterator = collection.iterator(); iterator.hasNext()
				&& i < maxLen; i++) {
			if (i > 0)
				builder.append(", ");
			builder.append(iterator.next());
		}
		builder.append("]");
		return builder.toString();
	}
	
}
