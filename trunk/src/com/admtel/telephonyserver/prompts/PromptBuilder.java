package com.admtel.telephonyserver.prompts;

import java.util.List;

public interface PromptBuilder {
	List<String> numberToPrompt(Long number);
}
