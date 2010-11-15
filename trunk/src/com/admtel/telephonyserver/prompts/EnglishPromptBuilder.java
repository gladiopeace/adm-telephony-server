package com.admtel.telephonyserver.prompts;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class EnglishPromptBuilder extends GenericPromptBuilder  {
	
	static Logger log = Logger.getLogger(EnglishPromptBuilder.class);
	
	@Override
	public List<String> numberToPrompt(Long number) {
		
		List<String> result = new ArrayList<String>();
		Long currentNumber = Math.abs(number);
		if (number == 0) {
			result.add("0");
		}
		if (number<0){
			result.add("minus");
			
		}
		while (currentNumber > 0){
			if (currentNumber <= 20){
				result.add(currentNumber.toString());
				currentNumber = 0L;
			}
			else
			if (currentNumber<100){
				Long remainder = currentNumber % 10;
				Long value = currentNumber - remainder;
				result.add(value.toString());
				currentNumber = remainder;
			}
			else
			if (currentNumber<1000){
				Long  remainder = currentNumber % 100;
				Long value = currentNumber / 100;
				
				result.add(value.toString());
				result.add("hundred");
				currentNumber = remainder;
			}
			else
			if (currentNumber<1000000){
				Long remainder = currentNumber % 1000;
				Long value = currentNumber / 1000;
				result.addAll(numberToPrompt(value));
				result.add("thousand");
				currentNumber = remainder;
			}
			else
			if (currentNumber<1000000000){
				Long remainder = currentNumber % 1000000;
				Long value = currentNumber / 1000000;
				result.addAll(numberToPrompt(value));
				result.add("million");
				currentNumber = remainder;				
			}
		}
		String number_result="";
		for (String str : result){
			number_result +=", " + str;
		}
		log.trace("Building number for "+number+", result = "+ number_result);
		return result;
	}

}
