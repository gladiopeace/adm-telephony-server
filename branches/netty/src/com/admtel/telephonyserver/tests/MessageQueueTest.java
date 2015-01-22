package com.admtel.telephonyserver.tests;

import java.util.Random;

import com.admtel.telephonyserver.core.AdmThreadExecutor;
import com.admtel.telephonyserver.core.QueuedMessageHandler;

public class MessageQueueTest extends QueuedMessageHandler{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Random rnd = new Random(System.currentTimeMillis());
		MessageQueueTest mqt = new MessageQueueTest();
		MessageQueueTest mqt2 = new MessageQueueTest();
		for (int i = 0;i<200;i++){
			if (rnd.nextBoolean()){
				mqt.putMessage(String.format("message %d", i));
			}
			else{
				mqt2.putMessage(String.format("message %d", i));
			}
/*			try {
				Thread.sleep(rnd.nextInt(100));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
*/		}
		System.out.println(AdmThreadExecutor.getInstance().getStatus());
		AdmThreadExecutor.getInstance().shutdown();
	}

	@Override
	public void onMessage(Object message) {
		System.out.println(this+":"+Thread.currentThread()+":"+message);
		
	}

}
