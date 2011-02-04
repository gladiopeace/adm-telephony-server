package com.admtel.telephonyserver.acd.impl;

import java.util.HashMap;
import java.util.Map;

public class ACDQueue {
	String name;
	Map<String, ACDChannel> acdChannels = new HashMap<String, ACDChannel>();
}
