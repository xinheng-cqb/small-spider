package com.small.crawler.concurrent;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.concurrent.Callable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.small.crawler.constants.ConcurrentConstants;

public class CallableTask<V> extends Task implements Callable<V> {
	private final static Log LOGGER = LogFactory.getLog(CallableTask.class);
	private Callable<V> caller;

	public CallableTask(Callable<V> caller) {
		new CallableTask<V>(Thread.currentThread().getName(), caller);
	}

	public CallableTask(String name, Callable<V> caller) {
		super.name = name;
		this.caller = caller;
	}

	@Override
	public V call() {
		super.status = ConcurrentConstants.RUNNING_STATUS;
		try {
			return caller.call();
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			LOGGER.error(sw.toString());
		} finally {
			super.status = ConcurrentConstants.SUCCESS_STATUS;
		}
		super.status = ConcurrentConstants.FAILURE_STATUS;
		return null;
	}

}
