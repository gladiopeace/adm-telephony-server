package com.admtel.telephonyserver.core;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class AdmThreadExecutor {

	private static AdmThreadExecutor instance = null;

	ThreadPoolExecutor executor;

	private AdmThreadExecutor() {
		executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(AdmTelephonyServer.getInstance().getDefinition().getMaxThreads());
	}

	public static AdmThreadExecutor getInstance() {
		if (instance == null) {
			instance = new AdmThreadExecutor();
		}
		return instance;
	}

	public void execute(Runnable task) {
		executor.execute(task);
	}

	public void shutdown() {
		executor.shutdown();
	}

	public String getStatus() {
		return String
				.format(
						"Completed tasks (%d) : Active threads (%d) : Maximum reached threads (%d) : Maximum allowed threads (%d) : Current threads in pool(%d)",
						executor.getCompletedTaskCount(), executor
								.getActiveCount(), executor
								.getLargestPoolSize(), executor
								.getMaximumPoolSize(), executor.getPoolSize());
	}
}
