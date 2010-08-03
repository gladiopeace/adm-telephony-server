package com.admtel.telephonyserver.prompts;

import java.util.ArrayList;
import java.util.List;

public class DefaultPromptBuilder implements PromptBuilder {

	@Override
	public String[] numberToPrompt(Long number) {
		List<String> result = new ArrayList<String>();
		
		return result.toArray(new String[result.size()]);
	}

}
