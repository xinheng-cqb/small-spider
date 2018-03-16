package com.small.crawler;

import java.util.concurrent.TimeUnit;

/**
 * @author xinheng-cqb
 * @date 2017年12月20日
 * @introduce:
 */
public class SynchronizedTest {
	public static void main(String[] args) {
		Obj obj = new Obj();
		Thread t1 = new RunObj(obj, "first");
		Thread t2 = new RunObj(obj, "second");
		t1.start();
		t2.start();
	}
}

class RunObj extends Thread {
	private Obj obj;
	private String type;

	public RunObj(Obj obj, String type) {
		this.obj = obj;
		this.type = type;
	}

	@Override
	public void run() {
		if (type.equals("first")) {
			obj.firPrint();
		} else {
			obj.secPrint();
		}
	}
}

class Obj {
	public synchronized void firPrint() {
		System.out.println("---> first start <---");
		try {
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("---> first end <---");
	}

	public synchronized void secPrint() {
		System.out.println("=== second start ===");
		try {
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("=== second end ===");
	}
}
