package com.admtel.telephonyserver.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
	static public String prepend(String input, String ... arguments){
		StringBuilder sb = new StringBuilder();
		for (String argument:arguments){
			sb.append(argument);
		}
		sb.append(input);
		return sb.toString();
	}
	static public String[] prepend(String[] input, String ... arguments){
		for (int i=0;i<input.length;i++){
			input[i] = prepend(input[i], arguments);
		}
		return input;
	}
	
	static public void main(String[] args){
		String[] strs = {"1","2","3"};
		strs = prepend(strs,"x","y");
		for (int i=0;i<strs.length;i++){
			System.out.println(strs[i]);
		}
	}
}
