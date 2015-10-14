package com.example.signaturelockscreen;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class MyService extends Service {

	

	public MyService() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	public static MyBroadcast mBroadcast = new MyBroadcast();
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate() {
		KeyguardManager.KeyguardLock k1;
		KeyguardManager km = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
		k1 = km.newKeyguardLock("IN");
		k1.disableKeyguard();
		
		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		//filter.addAction(Intent.ACTION_BOOT_COMPLETED);
		registerReceiver(mBroadcast, filter);

		super.onCreate();

	}
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		unregisterReceiver(mBroadcast);
		super.onDestroy();
	}

}
