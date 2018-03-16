package com.small.crawler.concurrent;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.small.crawler.constants.ConcurrentConstants;

public class RunnableTask extends Task implements Runnable {
	private final static Log LOGGER = LogFactory.getLog(RunnableTask.class);
	private Runnable runner;

	public RunnableTask(Runnable runner) {
		new RunnableTask(Thread.currentThread().getName(), runner);
	}

	public RunnableTask(String name, Runnable runner) {
		super.name = name;
		this.runner = runner;
	}

	@Override
	public void run() {
		super.status = ConcurrentConstants.RUNNING_STATUS;
		try {
			this.runner.run();
		} catch (Exception e) {
			super.status = ConcurrentConstants.FAILURE_STATUS;
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			LOGGER.error(sw.toString());
		}
		super.status = ConcurrentConstants.SUCCESS_STATUS;
	}
}
