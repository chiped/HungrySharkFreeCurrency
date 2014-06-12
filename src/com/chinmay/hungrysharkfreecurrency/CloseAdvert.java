package com.chinmay.hungrysharkfreecurrency;

import java.io.DataOutputStream;
import java.io.IOException;

import android.util.Log;

public class CloseAdvert implements Runnable {
	
	private DataOutputStream os;
	private String closeCmd1;
	private String closeCmd2;
	public CloseAdvert(DataOutputStream os, String closeCmd1, String closeCmd2) {
		this.os = os;
		this.closeCmd1 = closeCmd1;
		this.closeCmd2 = closeCmd2;
	}
	@Override
	public void run() {
		try {
			FloatingHeadService.getTasks().remove(this);
			os.writeBytes(closeCmd2);
			os.flush();
			os.writeBytes(closeCmd1);
			os.flush();
//			Thread.sleep(5000);

			Log.i("Service", "Finished closing clicks");
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

}
