package com.admtel.telephonyserver.utils;

import com.admtel.telephonyserver.config.SwitchType;

public class PromptsUtils {
	static public String expandPrompts(String[] prompts, String delimiter, SwitchType switchType){
		if (prompts == null || prompts.length==0) return "";
		if (prompts.length == 1) return prompts[0];
		String tPrompt ="";
		for (int i=0;i<prompts.length;i++){
			tPrompt += prompts[i];
			if (prompts[i].indexOf(".") == -1 && switchType == SwitchType.Freeswitch){
				tPrompt += ".wav";
			}
			if (i<prompts.length-1){
				tPrompt+=delimiter;
			}
		}
		return tPrompt;
	}
}
