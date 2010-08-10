package com.admtel.telephonyserver.tests;

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
	
	
	public static void main(String[] args) {
		PromptBuilder pb = new ArabicPromptBuilder();
		Logger log = Logger.getLogger(PromptBuilderTest.class);
		Random rnd = new Random(System.currentTimeMillis());
		for (int i = 0;i <100;i++){
			int number = rnd.nextInt(999999);
			log.trace("numberToPrompt for " + number +" is " + java.util.Arrays.toString(pb.numberToPrompt((long)number).toArray()));
		}
	}

}
