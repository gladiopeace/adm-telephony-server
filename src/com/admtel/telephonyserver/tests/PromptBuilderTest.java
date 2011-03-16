package com.admtel.telephonyserver.tests;

import java.util.Date;
import java.util.Random;

import org.apache.log4j.Logger;

import com.admtel.telephonyserver.prompts.ArabicPromptBuilder;
import com.admtel.telephonyserver.prompts.EnglishPromptBuilder;
import com.admtel.telephonyserver.prompts.FrenchPromptBuilder;
import com.admtel.telephonyserver.prompts.PromptBuilder;

public class PromptBuilderTest {

	/**
	 * @param args
	 */
	
	static PromptBuilder aPb = new ArabicPromptBuilder();
	static PromptBuilder ePb = new EnglishPromptBuilder();
	static PromptBuilder fPb = new FrenchPromptBuilder();
	static Logger log = Logger.getLogger(PromptBuilderTest.class);
	static Random rnd = new Random(System.currentTimeMillis());

	public static void main(String[] args) {
		numberToPrompt();
		dateToPrompt();
		digtiToPrompt();
	}
	
////////////////////////////////////////////////////////////////////////////////////Number To Prompt////////////////////////////////////////////////////////////////////////////////////////////////
	private static void numberToPrompt(){
		System.out.println("\n");
		System.out.println("********************************************* Start Number To prompt *********************************************");
		System.out.println("\n");
		for (int i = 0; i < 100; i++) {
			int number = rnd.nextInt(999999);
			arabicNumberToPrompt(number);
			englishNumberToPrompt(number);
			frenchNumberToPrompt(number);
			System.out.println("\n");
		}
		System.out.println("********************************************* End Number To prompt *********************************************");
		System.out.println("\n");
		System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		System.out.println("\n");
	}
	private static void arabicNumberToPrompt(int number){
		log.trace("---------------------------- Arabic: numberToPrompt for "+ number	+ " is                           "	+ java.util.Arrays.toString(aPb.numberToPrompt((long) number).toArray()));
	}
	private static void englishNumberToPrompt(int number){
		log.trace("---------------------------- English: numberToPrompt for "+ number	+ " is                          "	+ java.util.Arrays.toString(ePb.numberToPrompt((long) number).toArray()));
	}
	private static void frenchNumberToPrompt(int number){
		log.trace("---------------------------- French: numberToPrompt for "+ number	+ " is                          "	+ java.util.Arrays.toString(fPb.numberToPrompt((long) number).toArray()));
	}
	
////////////////////////////////////////////////////////////////////////////////////////Date To Prompt////////////////////////////////////////////////////////////////////////////////////////////
	private static void dateToPrompt(){
		System.out.println("********************************************* Start Date To prompt *********************************************");
		System.out.println("\n");
		//for (int i = 0; i < 100; i++) {
			arabicDateToPrompt();
			englishDateToPrompt();
			frenchDateToPrompt();
			System.out.println("\n");
		//}
		System.out.println("********************************************* End Date To prompt *********************************************");
		System.out.println("\n");
		System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		System.out.println("\n");
	}
	private static void arabicDateToPrompt(){
		log.trace("---------------------------- Arabic: dateToPrompt for "+ new Date()	+ " is                             "	+ java.util.Arrays.toString(aPb.dateToPrompt(new Date()).toArray()));
	}
	private static void englishDateToPrompt(){
		log.trace("---------------------------- English: dateToPrompt for "+ new Date()	+ " is                            "	+ java.util.Arrays.toString(ePb.dateToPrompt(new Date()).toArray()));
	}
	private static void frenchDateToPrompt(){
		log.trace("---------------------------- French: dateToPrompt for "+ new Date()	+ " is                            "	+ java.util.Arrays.toString(fPb.dateToPrompt(new Date()).toArray()));
	}
	
////////////////////////////////////////////////////////////////////////////////////////////Digit To prompt////////////////////////////////////////////////////////////////////////////////////////	
	private static void digtiToPrompt(){
		System.out.println("********************************************* Start Digit To prompt *********************************************");
		System.out.println("\n");
		for (int i = 0; i < 100; i++) {
			int number = rnd.nextInt(999999);
			arabicDigitToPrompt(number);
			englishDigitToPrompt(number);
			frenchDigitToPrompt(number);
			System.out.println("\n");
		}
		System.out.println("********************************************* End Digit To prompt *********************************************");
		System.out.println("\n");
		System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
		System.out.println("\n");
	}
	private static void arabicDigitToPrompt(int number){
		log.trace("---------------------------- Arabic: digitToPrompt for "+ number	+ " is                         "	+ java.util.Arrays.toString(aPb.digitToPrompt("" + number).toArray()));
	}
	private static void englishDigitToPrompt(int number){
		log.trace("---------------------------- English: digitToPrompt for "+ number+ " is                        "	+ java.util.Arrays.toString(ePb.digitToPrompt("" + number).toArray()));
	}
	private static void frenchDigitToPrompt(int number){
		log.trace("---------------------------- French: digitToPrompt for "+ number+ " is                        "	+ java.util.Arrays.toString(fPb.digitToPrompt("" + number).toArray()));
	}
	
}
