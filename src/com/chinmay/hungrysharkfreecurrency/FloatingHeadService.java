package com.chinmay.hungrysharkfreecurrency;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Handler;
import android.os.IBinder;
import android.text.InputType;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FloatingHeadService extends Service {

	private WindowManager windowManager;
	private ImageView closeButton;
	private ImageView gemsButton;
	private ImageView coinsButton;
	private ImageView settingsButton;
	private ImageView calibButton;
	private WindowManager.LayoutParams closeParams;
	private WindowManager.LayoutParams gemsParams;
	private WindowManager.LayoutParams coinsParams;
	private WindowManager.LayoutParams settingsParams;
	private WindowManager.LayoutParams calibParams;
	private Process process;
	private DataOutputStream os;
	private String tapCmd;
	private String gemsStartToast;
	private String coinsStartToast;
	private static int loopCount;
	private static int waitBeforeAdCloses;
	private static int waitAfterAdCloses;
	private static Point closePoint1;
	private static Point closePoint2;
	private static Point coinsPoint;
	private static Point gemsPoint;
	private Handler handler;
	private static List<Runnable> tasks;

	public static Point getClosePoint1() {
		return closePoint1;
	}

	public static void setClosePoint1(Point closePoint1) {
		FloatingHeadService.closePoint1 = closePoint1;
	}

	public static Point getClosePoint2() {
		return closePoint2;
	}

	public static void setClosePoint2(Point closePoint2) {
		FloatingHeadService.closePoint2 = closePoint2;
	}

	public static Point getCoinsPoint() {
		return coinsPoint;
	}

	public static void setCoinsPoint(Point coinsPoint) {
		FloatingHeadService.coinsPoint = coinsPoint;
	}

	public static Point getGemsPoint() {
		return gemsPoint;
	}

	public static void setGemsPoint(Point gemsPoint) {
		FloatingHeadService.gemsPoint = gemsPoint;
	}

	public static List<Runnable> getTasks() {
		return tasks;
	}

	public static void setTasks(List<Runnable> tasks) {
		FloatingHeadService.tasks = tasks;
	}

	public static int getLoopCount() {
		return loopCount;
	}

	public static void setLoopCount(int loopCount) {
		FloatingHeadService.loopCount = loopCount;
	}

	@Override 
	public IBinder onBind(Intent intent) {
		// Not used
		return null;
	}

	@Override 
	public void onCreate() {
		super.onCreate();
		Log.i(this.getClass().toString(), "Service started");
		
		initialize();
	
		generateButtons();
		addActionsListeners();
		
	}

	private void initialize() {
		windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);		
		gemsStartToast = "Clicking on get gems";
		coinsStartToast = "Clicking on get coins";

		handler = new Handler();
		setLoopCount(10);
		waitBeforeAdCloses = 25;
		waitAfterAdCloses = 5;
		tasks = new ArrayList<Runnable>();

		try {
			process = Runtime.getRuntime().exec("su");
			os = new DataOutputStream(process.getOutputStream());
			tapCmd = "/system/bin/input tap %d %d\n";
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressLint("NewApi")
	private void generateButtons() {
		closeButton = new ImageView(this);
		closeButton.setImageResource(R.drawable.ic_close);		

		gemsButton = new ImageView(this);
		gemsButton.setImageResource(R.drawable.ic_gems);
		
		coinsButton = new ImageView(this);
		coinsButton.setImageResource(R.drawable.ic_coins);
		
		settingsButton = new ImageView(this);
		settingsButton.setImageResource(R.drawable.ic_settings);
		
		calibButton = new ImageView(this);
		calibButton.setImageResource(R.drawable.ic_calib);
		
		Display display = windowManager.getDefaultDisplay();
		Point size = new Point();
		display.getRealSize(size);
		
		int width = Math.max(size.x, size.y);
		int height = Math.min(size.x, size.y);

		closePoint1 = new Point(770*width/800, 45*height/480);
		closePoint2 = new Point(10*width/800, 27*height/480);
		coinsPoint = new Point(440*width/800, 250*height/480);
		gemsPoint = new Point(440*width/800, 350*height/480);
		
		closeParams = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_PHONE,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);
		closeParams.gravity = Gravity.TOP | Gravity.LEFT;
		closeParams.x = size.x-72;
		closeParams.y = size.y-72;		
		windowManager.addView(closeButton, closeParams);
		
		coinsParams = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_PHONE,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);
		coinsParams.gravity = Gravity.TOP | Gravity.LEFT;
		coinsParams.x = closeParams.x - 72;
		coinsParams.y = size.y-72;
		windowManager.addView(coinsButton, coinsParams);
		
		gemsParams = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_PHONE,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);
		gemsParams.gravity = Gravity.TOP | Gravity.LEFT;
		gemsParams.x = coinsParams.x - 72;
		gemsParams.y = size.y-72;		
		windowManager.addView(gemsButton, gemsParams);
		
		settingsParams = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_PHONE,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);
		settingsParams.gravity = Gravity.TOP | Gravity.LEFT;
		settingsParams.x = gemsParams.x - 72;
		settingsParams.y = size.y-72;		
		windowManager.addView(settingsButton, settingsParams);
		
		calibParams = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_PHONE,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);
		calibParams.gravity = Gravity.TOP | Gravity.LEFT;
		calibParams.x = settingsParams.x - 72;
		calibParams.y = size.y-72;		
		windowManager.addView(calibButton, calibParams);
	}

	private void addActionsListeners() {
		final Service service = this;
		closeButton.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				service.stopSelf();
			}
		});
		gemsButton.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				if(check())	{
					runClicks(String.format(tapCmd, gemsPoint.x, gemsPoint.y), gemsStartToast);
				}
			}
		});
		coinsButton.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				if(check()) {
					runClicks(String.format(tapCmd, coinsPoint.x, coinsPoint.y), coinsStartToast);
				}
			}
		});
		settingsButton.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				getUserInput(service);				
			}
		});
		calibButton.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				startService(new Intent(service, TransparentSettingsActivity.class));
				
			}
		});
	}

	protected void getUserInput(Service service) {
		AlertDialog.Builder builder = new AlertDialog.Builder(service);

		builder.setTitle("Settings");
		builder.setMessage("Modify Values");

		TextView loopLabel = new TextView(service);
		final EditText loopText = new EditText(service);
		TextView adLabel = new TextView(service);
		final EditText adText = new EditText(service);
		TextView closeLabel = new TextView(service);
		final EditText closeText = new EditText(service);
		LinearLayout linearLayout = new LinearLayout(service);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		
		loopLabel.setText("Loop Count");
		linearLayout.addView(loopLabel);
		loopText.setInputType(InputType.TYPE_CLASS_NUMBER);
		loopText.setText(String.valueOf(getLoopCount()));
		linearLayout.addView(loopText);
		adLabel.setText("Close ad after (secs)");
		linearLayout.addView(adLabel);
		adText.setInputType(InputType.TYPE_CLASS_NUMBER);
		adText.setText(String.valueOf(waitBeforeAdCloses));
		linearLayout.addView(adText);
		closeLabel.setText("Wait before pressing button (secs)");
		linearLayout.addView(closeLabel);
		closeText.setInputType(InputType.TYPE_CLASS_NUMBER);
		closeText.setText(String.valueOf(waitAfterAdCloses));
		linearLayout.addView(closeText);
		
		builder.setView(linearLayout);
		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				loopCount = Integer.parseInt(loopText.getText().toString());
				waitBeforeAdCloses = Integer.parseInt(adText.getText().toString());
				waitAfterAdCloses = Integer.parseInt(closeText.getText().toString());
			}
		});

		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// Canceled.
			}
		});
		AlertDialog alert = builder.create();
		alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		alert.show();
	}

	private void runClicks(final String cmd, String toast) {
		for(int i=0; i<getLoopCount(); i++) {
			Thread clickButton = new Thread(new ClickButton(os, cmd));
			
			int totalWait = waitBeforeAdCloses + waitAfterAdCloses;
			tasks.add(clickButton);
			handler.postDelayed(clickButton, i*(totalWait*1000) + waitAfterAdCloses*1000);
			
			Thread closeAdvert = new Thread(new CloseAdvert(os, 
										String.format(tapCmd, closePoint1.x, closePoint1.y), 
										String.format(tapCmd, closePoint2.x, closePoint2.y)));
			tasks.add(closeAdvert);
			handler.postDelayed(closeAdvert, i*(totalWait*1000) + waitBeforeAdCloses*1000);
			
		}
	}

	public boolean check() {
		ActivityManager activityManager = (ActivityManager) this.getSystemService( ACTIVITY_SERVICE );
        List<RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
        
        for(int i = 0; i < procInfos.size(); i++)
        {
            if(procInfos.get(i).processName.equals("com.fgol.HungrySharkEvolution")) 
            {
                Toast.makeText(this, "Game is running", Toast.LENGTH_SHORT).show();
                Log.e("App running at position", ""+i);
                return true;
            }
        }
        Toast.makeText(getApplicationContext(), "Game is not running", Toast.LENGTH_SHORT).show();
        return false;        
	}
	
	@SuppressLint("NewApi")
	public void updateButtons() {
	    Display display = windowManager.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		closeParams.x = size.x-72;
		closeParams.y = size.y-72;
		coinsParams.x = closeParams.x - 72;
		coinsParams.y = size.y-72;
		gemsParams.x = coinsParams.x - 72;
		gemsParams.y = size.y-72;
		settingsParams.x = gemsParams.x - 72;
		settingsParams.y = size.y-72;
		calibParams.x = settingsParams.x - 72;
		calibParams.y = size.y-72;
		windowManager.updateViewLayout(closeButton, closeParams);
		windowManager.updateViewLayout(coinsButton, coinsParams);
		windowManager.updateViewLayout(gemsButton, gemsParams);
		windowManager.updateViewLayout(settingsButton, settingsParams);
		windowManager.updateViewLayout(calibButton, calibParams);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if(!tasks.isEmpty()) {
			for(Runnable r: tasks) {
				handler.removeCallbacks(r);
			}
		}
		if (closeButton != null) windowManager.removeView(closeButton);
		if (coinsButton != null) windowManager.removeView(coinsButton);
		if (gemsButton != null) windowManager.removeView(gemsButton);
		if (settingsButton != null) windowManager.removeView(settingsButton);
		if (calibButton != null) windowManager.removeView(calibButton);
		

		try {
			os.writeBytes("exit\n");
			os.flush();
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Log.i(this.getClass().toString(), "Closing Service");
		Toast.makeText(getApplicationContext(), "Closing Free Currency Service", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	    //setContentView(R.layout.activity_main);      
	    updateButtons();
	}
}
