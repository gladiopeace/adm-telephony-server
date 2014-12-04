package com.admtel.telephonyserver.prompts;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

abstract public class GenericPromptBuilder implements PromptBuilder {

	final static BigDecimal HUNDRED = new BigDecimal(100);
	
	@Override
	public List<String> currencyToPrompt(BigDecimal amount) {
		List<String> result = new ArrayList<String>();
		if (amount == null) return result;
		
		amount = amount.setScale(2, RoundingMode.HALF_EVEN);
		BigDecimal dollars = amount.setScale(0, RoundingMode.FLOOR);
		BigDecimal cents = amount.subtract(dollars).multiply(HUNDRED);
		
		long lDollars = dollars.longValue();
		long lCents = cents.longValue();
		
		result.addAll(numberToPrompt(lDollars));
		if (lDollars == 1){
			result.add("dollar");
		}
		else{
			result.add("dollars");
		}

		result.add("and");
		result.addAll(numberToPrompt(lCents));
		if(lCents == 1){
			result.add("cent");
		}
		else{
			result.add("cents");
		}
		return result;
	}
}
