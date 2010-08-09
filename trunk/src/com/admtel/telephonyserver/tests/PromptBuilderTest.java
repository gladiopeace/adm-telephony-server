package com.admtel.telephonyserver.tests;

import java.util.Random;

import org.apache.log4j.Logger;

import com.admtel.telephonyserver.prompts.EnglishPromptBuilder;

public class PromptBuilderTest {

	/**
	 * @param args
	 */
	
	
	public static void main(String[] args) {
		EnglishPromptBuilder pb = new EnglishPromptBuilder();
		Logger log = Logger.getLogger(PromptBuilderTest.class);
		Random rnd = new Random(System.currentTimeMillis());
		for (int i = 0;i <1000;i++){
			int number = rnd.nextInt(10000000);
			log.trace("numberToPrompt for " + number +" is " + java.util.Arrays.toString(pb.numberToPrompt((long)number).toArray()));
		}
	}

}
