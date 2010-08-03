package com.admtel.telephonyserver.prompts;

import java.util.Locale;

import org.apache.log4j.Logger;

import com.admtel.telephonyserver.core.SmartClassLoader;

public class PromptBuilderFactory {
	
	static Logger log = Logger.getLogger(PromptBuilderFactory.class);
	
	public static PromptBuilder buildPromptBuilder(Locale local) {
		log.trace("Building PromptBuilder for " + local);
		PromptBuilder result = null;
		String className = "PromptBuilder" + local;
		result = SmartClassLoader
				.createInstance(PromptBuilder.class, className);
		if (result == null) {
			className = "PromptBuilder" + local.getLanguage();
			result = SmartClassLoader.createInstance(PromptBuilder.class,
					className);
			if (result == null) {
				result = new DefaultPromptBuilder();
			}
		}
		log.trace("Built Prompt builder for "+ local +" : "+ result);
		return result;
	}
}
