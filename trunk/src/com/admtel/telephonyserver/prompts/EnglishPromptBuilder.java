package com.admtel.telephonyserver.prompts;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

public class EnglishPromptBuilder extends GenericPromptBuilder {

	static Logger log = Logger.getLogger(EnglishPromptBuilder.class);

	@Override
	public List<String> numberToPrompt(Long number) {

		List<String> result = new ArrayList<String>();
		Long currentNumber = Math.abs(number);
		if (number == 0) {
			result.add("0");
		}
		if (number < 0) {
			result.add("minus");

		}
		while (currentNumber > 0) {
			if (currentNumber <= 20) {
				result.add(currentNumber.toString());
				currentNumber = 0L;
			} else if (currentNumber < 100) {
				Long remainder = currentNumber % 10;
				Long value = currentNumber - remainder;
				result.add(value.toString());
				currentNumber = remainder;
			} else if (currentNumber < 1000) {
				Long remainder = currentNumber % 100;
				Long value = currentNumber / 100;

				result.add(value.toString());
				result.add("hundred");
				currentNumber = remainder;
			} else if (currentNumber < 1000000) {
				Long remainder = currentNumber % 1000;
				Long value = currentNumber / 1000;
				result.addAll(numberToPrompt(value));
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
		String number_result = "";
		for (String str : result) {
			number_result += ", " + str;
		}
		log.trace("Building number for " + number + ", result = "
				+ number_result);
		return result;
	}

	@Override
	public List<String> dateToPrompt(Date date) {
		List<String> result = new ArrayList<String>();
		DateTime newDate = new DateTime(date);
		returnDay(newDate, result);
		result.add(returnMonth(newDate));
		returnYear(newDate, result);
		log
				.trace("English: ////////////////////////////////////////////////////////////////////////////////////"
						+ result
						+ "===================================================");
		return result;
	}

	public void returnDay(DateTime date, List<String> result) {
		int dayOfYear = date.getDayOfMonth();
		Long day = Long.valueOf(dayOfYear);
		if (day <= 20) {
			result.add(day.toString());
		} else if (day < 31) {
			Long remainder = day % 10;
			Long value = day - remainder;
			result.add(value.toString());
			if (remainder > 0) {
				result.add(remainder.toString());
			}
		}
		if ((dayOfYear == 1) || (dayOfYear == 21) || (dayOfYear == 31)) {
			result.add("st");
		} else if ((dayOfYear == 2) || (dayOfYear == 22)) {
			result.add("nd");
		} else if ((dayOfYear == 3) || (dayOfYear == 23)) {
			result.add("rd");
		} else {
			result.add("th");
		}
		result.add("of");
	}

	public String returnMonth(DateTime date) {
		String month = "";
		switch (date.getMonthOfYear()) {
		case 1:
			month = "January";
			break;
		case 2:
			month = "February";
			break;
		case 3:
			month = "March";
			break;
		case 4:
			month = "April";
			break;
		case 5:
			month = "May";
			break;
		case 6:
			month = "June";
			break;
		case 7:
			month = "July";
			break;
		case 8:
			month = "Augest";
			break;
		case 9:
			month = "September";
			break;
		case 10:
			month = "October";
			break;
		case 11:
			month = "November";
			break;
		case 12:
			month = "December";
			break;
		}
		return month;
	}

	public void returnYear(DateTime date, List<String> result) {
		int newYear = date.getYear();
		Long year = Long.valueOf(newYear);

		if (year < 3000) {
			Long remainder = year % 1000;
			Long value = (year - remainder) / 1000;
			if (value == 1) {
				result.add("thousand");
			} else if (value == 2) {
				result.add("2");
				result.add("thousand");
			} else if (value == 3) {
				result.add("3");
				result.add("thousand");
			}
			year = remainder;
			if (year < 1000) {
				remainder = year % 100;
				value = (year - remainder) / 100;
				if (value == 1) {
					result.add("hundred");
				} else if (value == 2) {
					result.add("2");
					result.add("hundred");
				} else if (value > 2) {
					result.add(value.toString());
					result.add("hundred");
				}
				year = remainder;
				if (year < 100) {
					remainder = year % 10;
					value = year - remainder;
					if (year <= 20) {
						result.add(year.toString());
					} else {
						if (remainder > 0) {
							result.add(remainder.toString());
						}
						result.add(value.toString());
					}
				}
			}
		}
	}

	@Override
	public List<String> digitToPrompt(String number) {
		List<String> result = new ArrayList<String>();
		String currentNumber = number.toString();
		for (int i = 0; i <=currentNumber.length() - 1; i++){
			String num = currentNumber.substring(i,i + 1);
			result.add(num);
		}
		log.trace("English: ++++++++result++++++++++++++++++++++++++++++++++++++++" + result + "++++++++++++++++++++++++++++++++++");
		return result;
	}
}
