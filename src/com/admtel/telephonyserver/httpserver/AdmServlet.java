package com.admtel.telephonyserver.httpserver;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.admtel.telephonyserver.utils.AdmUtils;
import com.admtel.telephonyserver.utils.TimedHashMap;
import org.apache.log4j.Logger;

abstract public class AdmServlet {

    static Logger log = Logger.getLogger(AdmServlet.class);
	TimedHashMap<String, Object> sessions = new TimedHashMap<String, Object>();  
	
	Object getSession(String sessionId){
		synchronized(sessions){
			return sessions.get(sessionId);
		}
	}

    private String dump(HttpRequestMessage request){
        Map<String, String[]> headers = request.getHeaders();
        Iterator<Map.Entry<String, String[]>> it = headers.entrySet().iterator();
        StringBuffer sb = new StringBuffer();

        while (it.hasNext()) {
            Map.Entry<String,String[]> pairs = it.next();
            sb.append(pairs.getKey() + " = ");
            for (int i =0; i< pairs.getValue().length;i++){
                sb.append((String) pairs.getValue()[i]);
                if ((i+1)<pairs.getValue().length){
                    sb.append(",");
                }
            }
        }
        return sb.toString();
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
