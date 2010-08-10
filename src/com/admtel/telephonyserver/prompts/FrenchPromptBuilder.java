package com.admtel.telephonyserver.prompts;

import java.util.ArrayList;
import java.util.List;

public class FrenchPromptBuilder implements PromptBuilder {

	@Override
	public List<String> numberToPrompt(Long number) {
		List<String> result = new ArrayList<String>();

		Long currentNumber = number;
		while (currentNumber > 0) {
			if (currentNumber <= 20) {
				if (currentNumber == 1 && result.size() > 0) { // 21:
																// vingt-et-un
																// (twenty and
																// one)
					result.add("and");
				}
				result.add(currentNumber.toString());
				currentNumber = 0L;
			} else if (currentNumber >= 70 && currentNumber <= 79) {
				result.addAll(numberToPrompt(60L));
				currentNumber -= 60L;
			} else if (currentNumber >= 90 && currentNumber <= 99) {
				result.addAll(numberToPrompt(80L));
				currentNumber -= 80L;
			} else if (currentNumber < 100) {
				Long remainder = currentNumber % 10;
				Long value = currentNumber - remainder;
				result.add(value.toString());
				currentNumber = remainder;
			} else if (currentNumber < 1000) {
				Long remainder = currentNumber % 100;
				Long value = currentNumber / 100;
				if (value > 1) {
					result.add(value.toString());
				}
				result.add("hundred");
				currentNumber = remainder;
			} else if (currentNumber < 1000000) {
				Long remainder = currentNumber % 1000;
				Long value = currentNumber / 1000;
				if (value > 1) {
					result.addAll(numberToPrompt(value));
				}
				result.add("thousand");
				currentNumber = remainder;
			} else if (currentNumber < 1000000000) {
				Long remainder = currentNumber % 1000000;
				Long value = currentNumber / 1000000;
				result.addAll(numberToPrompt(value));
				result.add("million");
				currentNumber = remainder;
			}
		}
		return result;
	}

}
