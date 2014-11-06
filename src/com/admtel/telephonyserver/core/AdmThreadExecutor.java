package com.admtel.telephonyserver.core;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import com.admtel.telephonyserver.config.SystemConfig;

public class AdmThreadExecutor {

	public ThreadPoolExecutor executor;

	private AdmThreadExecutor() {
		executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(SystemConfig.getInstance().serverDefinition.getMaxThreads());
	}

	private static class SingletonHolder {
		final private static AdmThreadExecutor instance = new AdmThreadExecutor();
	}
	public static AdmThreadExecutor getInstance() {
		return SingletonHolder.instance;
	}

	public void execute(Runnable task) {
		executor.execute(task);
	}

	public void submit(Runnable task){
		executor.submit(task);
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
