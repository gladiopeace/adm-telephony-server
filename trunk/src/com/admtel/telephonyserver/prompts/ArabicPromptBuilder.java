package com.admtel.telephonyserver.prompts;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalTime;

public class ArabicPromptBuilder extends GenericPromptBuilder {

	static Logger log = Logger.getLogger(EnglishPromptBuilder.class);

	@Override
	public List<String> numberToPrompt(Long number) {
		List<String> result = new ArrayList<String>();

		Long currentNumber = Math.abs(number);
		if (number < 0) {
			result.add("minus");

		}
		if (number == 0) {
			result.add("0");
		}
		while (currentNumber > 0) {
			if (result.size() > 0) {
				result.add("and");
			}
			if (currentNumber <= 20) {
				result.add(currentNumber.toString());
				currentNumber = 0L;
			} else if (currentNumber < 100) {
				Long remainder = currentNumber % 10;// 31 (one and thirty
				Long value = currentNumber - remainder;
				if (remainder > 0) {
					result.addAll(numberToPrompt(remainder));
					result.add("and");
				}
				result.add(value.toString());
				currentNumber = 0L;
			} else if (currentNumber < 1000) {
				Long remainder = currentNumber % 100;
				Long value = currentNumber / 100;
				if (value > 2) {
					result.add(value.toString());
				}
				if (value == 2) {
					result.add("2 hundred");
				} else {
					result.add("hundred");
				}
				currentNumber = remainder;
			} else if (currentNumber < 1000000) {
				Long remainder = currentNumber % 1000;
				Long value = currentNumber / 1000;
				if (value > 2 && value <= 10) {
					result.addAll(numberToPrompt(value));
					result.add("thousands");
				} else if (value == 1) {
					result.add("thousand");
				} else if (value == 2) {
					result.add("2thousand");
				} else {
					result.addAll(numberToPrompt(value));
					result.add("thousand");
				}
				currentNumber = remainder;
			} else if (currentNumber < 1000000000) {
				Long remainder = currentNumber % 1000000;
				Long value = currentNumber / 1000000;
				if (value > 2 && value <= 10) {
					result.addAll(numberToPrompt(value));
					result.add("millions");
				} else if (value == 1) {
					result.add("million");
				} else if (value == 2) {
					result.add("2million");
				} else {
					result.addAll(numberToPrompt(value));
					result.add("million");
				}
				currentNumber = remainder;
			}
		}
		return result;
	}

	@Override
	public List<String> currencyToPrompt(BigDecimal amount) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> dateToPrompt(Date date) {
		List<String> result = new ArrayList<String>();
		DateTime newDate = new DateTime(date);
		returnDay(newDate, result);
		result.add(returnMonth(newDate));
		returnYear(newDate, result);
		log
				.trace("Arabic: ////////////////////////////////////////////////////////////////////////////////////"
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
			Long remainder = day % 10;// 31 (one and thirty)
			Long value = day - remainder;
			if (remainder > 0) {
				result.add(remainder.toString());
				result.add("and");
			}
			result.add(value.toString());
		}
	}

	public String returnMonth(DateTime date) {
		String month = "";
		switch (date.getMonthOfYear()) {
		case 1:
			month = "Kanon Al-thani";
			break;
		case 2:
			month = "Chbat";
			break;
		case 3:
			month = "Azar";
			break;
		case 4:
			month = "Nisan";
			break;
		case 5:
			month = "Ayyar";
			break;
		case 6:
			month = "Hozayran";
			break;
		case 7:
			month = "Tamoz";
			break;
		case 8:
			month = "Aab";
			break;
		case 9:
			month = "Ayloul";
			break;
		case 10:
			month = "Teshren Al-awal";
			break;
		case 11:
			month = "Teshren Al-thani";
			break;
		case 12:
			month = "Kanon Al-awal";
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
				result.add("and");
			} else if (value == 2) {
				result.add("2");
				result.add("thousand");
				result.add("and");
			} else if (value == 3) {
				result.add("3");
				result.add("thousand");
				result.add("and");
			}
			year = remainder;
			if (year < 1000) {
				remainder = year % 100;
				value = (year - remainder) / 100;
				if (value == 1) {
					result.add("hundred");
					result.add("and");
				} else if (value == 2) {
					result.add("2");
					result.add("hundred");
					result.add("and");
				} else if (value > 2) {
					result.add(value.toString());
					result.add("hundred");
					result.add("and");
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
							result.add("and");
						}
						result.add(value.toString());
					}
				}
			}
		}
	}
}
