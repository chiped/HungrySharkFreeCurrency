package com.chinmay.hungrysharkfreecurrency;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.IBinder;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

public class TransparentSettingsActivity extends Service {
	
	private ImageView doneButton;
	private ImageView closeButton1;
	private ImageView closeButton2;
	private ImageView gemsButton;
	private ImageView coinsButton;
	WindowManager.LayoutParams doneParams;
	private WindowManager.LayoutParams closeParams1;
	private WindowManager.LayoutParams closeParams2;
	private WindowManager.LayoutParams gemsParams;
	private WindowManager.LayoutParams coinsParams;
	private WindowManager windowManager;
	@Override
	public void onCreate() {
		super.onCreate();
		Log.i("Settings", "TransparentSettingsActivity started");
		generateButtons();
		addListeners();
	}
	@SuppressLint("NewApi")
	private void generateButtons() {
				 
		doneButton = new ImageView(this);
		doneButton.setImageResource(R.drawable.ic_done);
		
		closeButton1 = new ImageView(this);
		closeButton1.setImageResource(R.drawable.ic_close_edit);		
		
		closeButton2 = new ImageView(this);
		closeButton2.setImageResource(R.drawable.ic_close_edit);	

		gemsButton = new ImageView(this);
		gemsButton.setImageResource(R.drawable.ic_gems_edit);
		
		coinsButton = new ImageView(this);
		coinsButton.setImageResource(R.drawable.ic_coins_edit);
		
		windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
		Display display = windowManager.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		Log.e("Point x", ""+size.x);
		Log.e("Point y", ""+size.y);
				
		doneParams = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_PHONE,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);
		doneParams.gravity = Gravity.TOP | Gravity.LEFT;
		doneParams.x = 0;
		doneParams.y = size.y-72;		
		windowManager.addView(doneButton, doneParams);
		
		closeParams1 = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_PHONE,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);
		closeParams1.gravity = Gravity.TOP | Gravity.LEFT;
		closeParams1.x = FloatingHeadService.getClosePoint1().x - 48/2;
		closeParams1.y = FloatingHeadService.getClosePoint1().y - 48/2;
		windowManager.addView(closeButton1, closeParams1);
		
		closeParams2 = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_PHONE,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);
		closeParams2.gravity = Gravity.TOP | Gravity.LEFT;
		closeParams2.x = FloatingHeadService.getClosePoint2().x - 48/2;
		closeParams2.y = FloatingHeadService.getClosePoint2().y - 48/2;
		windowManager.addView(closeButton2, closeParams2);
		
		coinsParams = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_PHONE,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);
		coinsParams.gravity = Gravity.TOP | Gravity.LEFT;
		coinsParams.x = FloatingHeadService.getCoinsPoint().x - 48/2;
		coinsParams.y = FloatingHeadService.getCoinsPoint().y - 48/2;
		windowManager.addView(coinsButton, coinsParams);
		
		gemsParams = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_PHONE,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);
		gemsParams.gravity = Gravity.TOP | Gravity.LEFT;
		gemsParams.x = FloatingHeadService.getGemsPoint().x - 48/2;
		gemsParams.y = FloatingHeadService.getGemsPoint().y - 48/2;
		windowManager.addView(gemsButton, gemsParams);
	}

	private void addListeners() {
		closeButton1.setOnTouchListener(new OnTouchListener() {
			private int initialX;
			private int initialY;
			private float initialTouchX;
			private float initialTouchY;

			@Override 
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					initialX = closeParams1.x;
					initialY = closeParams1.y;
					initialTouchX = event.getRawX();
					initialTouchY = event.getRawY();
					return true;
				case MotionEvent.ACTION_UP:
					return true;
				case MotionEvent.ACTION_MOVE:
					closeParams1.x = initialX + (int) (event.getRawX() - initialTouchX);
					closeParams1.y = initialY + (int) (event.getRawY() - initialTouchY);
					windowManager.updateViewLayout(closeButton1, closeParams1);
					return true;
				}
				return false;
			}

		});
		closeButton2.setOnTouchListener(new OnTouchListener() {
			private int initialX;
			private int initialY;
			private float initialTouchX;
			private float initialTouchY;

			@Override 
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					initialX = closeParams2.x;
					initialY = closeParams2.y;
					initialTouchX = event.getRawX();
					initialTouchY = event.getRawY();
					return true;
				case MotionEvent.ACTION_UP:
					return true;
				case MotionEvent.ACTION_MOVE:
					closeParams2.x = initialX + (int) (event.getRawX() - initialTouchX);
					closeParams2.y = initialY + (int) (event.getRawY() - initialTouchY);
					windowManager.updateViewLayout(closeButton2, closeParams2);
					return true;
				}
				return false;
			}

		});
		
		coinsButton.setOnTouchListener(new OnTouchListener() {
			private int initialX;
			private int initialY;
			private float initialTouchX;
			private float initialTouchY;

			@Override 
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					initialX = coinsParams.x;
					initialY = coinsParams.y;
					initialTouchX = event.getRawX();
					initialTouchY = event.getRawY();
					return true;
				case MotionEvent.ACTION_UP:
					return true;
				case MotionEvent.ACTION_MOVE:
					coinsParams.x = initialX + (int) (event.getRawX() - initialTouchX);
					coinsParams.y = initialY + (int) (event.getRawY() - initialTouchY);
					windowManager.updateViewLayout(coinsButton, coinsParams);
					return true;
				}
				return false;
			}

		});
		
		
		gemsButton.setOnTouchListener(new OnTouchListener() {
			private int initialX;
			private int initialY;
			private float initialTouchX;
			private float initialTouchY;

			@Override 
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					initialX = gemsParams.x;
					initialY = gemsParams.y;
					initialTouchX = event.getRawX();
					initialTouchY = event.getRawY();
					return true;
				case MotionEvent.ACTION_UP:
					return true;
				case MotionEvent.ACTION_MOVE:
					gemsParams.x = initialX + (int) (event.getRawX() - initialTouchX);
					gemsParams.y = initialY + (int) (event.getRawY() - initialTouchY);
					windowManager.updateViewLayout(gemsButton, gemsParams);
					return true;
				}
				return false;
			}

		});

		doneButton.setOnClickListener(new OnClickListener() { 
			@Override
			public void onClick(View arg0) {
				FloatingHeadService.setClosePoint1(new Point(closeParams1.x+48/2, closeParams1.y+48/2));
				FloatingHeadService.setClosePoint2(new Point(closeParams2.x+48/2, closeParams2.y+48/2));
				FloatingHeadService.setCoinsPoint(new Point(coinsParams.x+48/2, coinsParams.y+48/2));
				FloatingHeadService.setGemsPoint(new Point(gemsParams.x+48/2, gemsParams.y+48/2));
			    TransparentSettingsActivity.this.stopSelf();
			} 
		});
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (doneButton != null) windowManager.removeView(doneButton);
		if (closeButton1 != null) windowManager.removeView(closeButton1);
		if (closeButton2 != null) windowManager.removeView(closeButton2);
		if (coinsButton != null) windowManager.removeView(coinsButton);
		if (gemsButton != null) windowManager.removeView(gemsButton);
		
	    Log.i("Settings", "Bye");
		Toast.makeText(getApplicationContext(), "Closing Settings", Toast.LENGTH_SHORT).show();
	}
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
}
