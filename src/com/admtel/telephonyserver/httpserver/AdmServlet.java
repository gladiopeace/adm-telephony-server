package com.admtel.telephonyserver.httpserver;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.admtel.telephonyserver.utils.AdmUtils;
import com.admtel.telephonyserver.utils.TimedHashMap;

abstract public class AdmServlet {
	TimedHashMap<String, Object> sessions = new TimedHashMap<String, Object>();  
	
	Object getSession(String sessionId){
		synchronized(sessions){
			return sessions.get(sessionId);
		}
	}
	void setSession(HttpResponseMessage response, String sessionId, Object session){
		response.addToCookie("session", sessionId);
		synchronized (sessions){
			sessions.put(sessionId, session);
		}
	}
	
	final public void internalProcess(HttpRequestMessage request, HttpResponseMessage response){		
		String[] cookieArr = request.getHeader("Cookie");
		if (cookieArr != null && cookieArr.length>0){
		if (cookieArr[0] != null){
			Map<String, String> cookieMap = AdmUtils.parseVars(cookieArr[0], ";");
			for (Iterator it = cookieMap.keySet().iterator();it.hasNext();){
				String key = (String) it.next();
				String value = cookieMap.get(key);
				String[] a = new String[1];
				a[0] = (value != null?value:"");
				request.getHeaders().put("@".concat(key), a);
			}
		}
		}		
		process(request, response);
		String sessionId = request.getParameter("session");
		if (sessionId != null){
			synchronized(sessions){
				Object session = sessions.get(sessionId);
				if (session != null){
					sessions.put(sessionId, session);//reset timer
				}
			}
		}
	}
	abstract public void process (HttpRequestMessage request, HttpResponseMessage response);
}
