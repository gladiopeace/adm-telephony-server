package com.admtel.telephonyserver.prompts;

import java.util.ArrayList;
import java.util.List;

public class ArabicPromptBuilder implements PromptBuilder {

	@Override
	public List<String> numberToPrompt(Long number) {
		List<String> result = new ArrayList<String>();

		Long currentNumber = number;
		while (currentNumber > 0){
			if (result.size() >0 ){
				result.add("and");
			}
			if (currentNumber <= 20){
				result.add(currentNumber.toString());
				currentNumber = 0L;
			}
			else
			if (currentNumber<100){
				Long remainder = currentNumber % 10;// 31 (one and thirty
				Long value = currentNumber - remainder;
				if (remainder >0){
					result.addAll(numberToPrompt(remainder));
					result.add("and");
				}
				result.add(value.toString());
				currentNumber = 0L;
			}
			else
			if (currentNumber<1000){
				Long  remainder = currentNumber % 100;
				Long value = currentNumber / 100;
				if (value > 2){
					result.add(value.toString());
				}
				if (value == 2){
					result.add("2hundred");
				}
				else{
					result.add("hundred");
				}
				currentNumber = remainder;
			}
			else
			if (currentNumber<1000000){
				Long remainder = currentNumber % 1000;
				Long value = currentNumber / 1000;
				if (value > 2 && value <=10){
					result.addAll(numberToPrompt(value));
					result.add("thousands");
				}
				else if (value == 1){
					result.add("thousand");
				}
				else
				if (value == 2){
					result.add("2thousand");
				}
				else{
					result.addAll(numberToPrompt(value));
					result.add("thousand");					
				}
				currentNumber = remainder;
			}
			else
			if (currentNumber<1000000000){
				Long remainder = currentNumber % 1000000;
				Long value = currentNumber / 1000000;
				if (value > 2 && value <=10){
					result.addAll(numberToPrompt(value));
					result.add("millions");
				}
				else if (value == 1){
					result.add("million");
				}
				else if (value == 2){
					result.add("2million");
				}
				else{
					result.addAll(numberToPrompt(value));
					result.add("million");					
				}
				currentNumber = remainder;				
			}
		}
		return result;
	}

}
