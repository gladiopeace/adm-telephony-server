package com.admtel.telephonyserver.tests;

import com.admtel.telephonyserver.utils.LimitedQueue;

public class LimitedQueueTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LimitedQueue<String> queue = new LimitedQueue<String>(10);
		
		System.out.println (queue.size());
		for (int i=0;i<100;i++){
			queue.add(String.format("item %d", i));
		}
		System.out.println (queue.size());
	}

}
