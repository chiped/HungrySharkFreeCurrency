package com.chinmay.hungrysharkfreecurrency;

import java.io.DataOutputStream;
import java.io.IOException;

import android.util.Log;

public class ClickButton implements Runnable {
	
	private DataOutputStream os;
	private String cmd;
	public ClickButton(DataOutputStream os, String cmd) {
		this.os = os;
		this.cmd = cmd;
	}
	@Override
	public void run() {
		try {
			FloatingHeadService.getTasks().remove(this);
			os.writeBytes(cmd);
			os.flush();
//			Thread.sleep(25000);

			Log.i("Service", "Clicked on Button");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
