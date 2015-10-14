package com.example.signaturelockscreen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyBroadcast extends BroadcastReceiver {
	public static boolean screenOn = true;

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
			screenOn = false;
			Intent mIntent = new Intent(context, LockActivity.class);
			mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(mIntent);
		} else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
			Intent intent11 = new Intent(context, LockActivity.class);
			intent11.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			screenOn = true;
		} else if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			Intent mIntent = new Intent(context, LockActivity.class);
			mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(mIntent);
		}

	}

}
