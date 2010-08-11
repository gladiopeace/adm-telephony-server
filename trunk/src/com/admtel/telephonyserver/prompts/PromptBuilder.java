package com.admtel.telephonyserver.prompts;

import java.math.BigDecimal;
import java.util.List;

public interface PromptBuilder {
	List<String> numberToPrompt(Long number);
	List<String> currencyToPrompt(BigDecimal amount);
}
