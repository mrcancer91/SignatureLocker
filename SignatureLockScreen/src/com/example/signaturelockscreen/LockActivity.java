package com.example.signaturelockscreen;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.example.signaturelockscreen.util.HomeKeyLocker;
import com.example.signaturelockscreen.util.KohonenNetwork;

import android.R.bool;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class LockActivity extends ActionBarActivity {

	Button btnOK;
	HomeKeyLocker mKeyLock;
	TextView txtPas;
	MyCustomView mcusCustomView;
	KohonenNetwork mNet;
	String[] kq;
	public static boolean isLocking = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mKeyLock = new HomeKeyLocker();
		mKeyLock.lock(this);
		this.getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
						| WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
						| WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_lock);
		btnOK = (Button) findViewById(R.id.btnStartLock);
		mNet = new KohonenNetwork();
		isLocking = true;
		try {
			kq = readStringFromFile("myID.txt").split(" ");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mcusCustomView = (MyCustomView) findViewById(R.id.mCusView);
		mcusCustomView.setLock(this);
		btnOK.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					if (mcusCustomView.catched_Point.size() > 0)
						if (recognize() == true) {
							isLocking = false;
							LockActivity.this.finish();
						}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		if (getIntent() != null && getIntent().hasExtra("kill")
				&& getIntent().getExtras().getInt("kill") == 1) {
			finish();
		}
	}

	public void doUnlock() {
		try {
			if (mcusCustomView.catched_Point.size() > 0)
				if (recognize() == true) {
					isLocking = false;
					LockActivity.this.finish();
				}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String readStringFromFile(String filePath)
			throws FileNotFoundException, IOException {
		File mFile = new File(filePath);
		StringBuffer stringBuffer = new StringBuffer();

		BufferedReader inputReader = new BufferedReader(new InputStreamReader(
				openFileInput(mFile.getPath())));
		String inputString;
		while ((inputString = inputReader.readLine()) != null) {
			stringBuffer.append(inputString + "\n");
		}
		return stringBuffer.toString();
	}

	private boolean recognize() throws IOException {

		int id = mNet.recognize(mcusCustomView.nomalizeInput(
				mcusCustomView.catched_Point, 100));
//		Toast.makeText(getApplicationContext(), "ID: " + id, Toast.LENGTH_SHORT)
//				.show();
		for (String temp : kq)
			if (id == Integer.parseInt(temp.trim()))
				return true;
		Toast.makeText(getApplicationContext(), "Bạn đã nhập sai ID",
				Toast.LENGTH_SHORT).show();
		mcusCustomView._clearView();
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.lock, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		return;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)
				|| (keyCode == KeyEvent.KEYCODE_VOLUME_UP)
				|| (keyCode == KeyEvent.KEYCODE_POWER)
				|| (keyCode == KeyEvent.KEYCODE_CAMERA))

			return true;
		else if ((keyCode == KeyEvent.KEYCODE_HOME)) {

			return true;
		} else
			return false;

	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if ((event.getKeyCode() == KeyEvent.KEYCODE_HOME)
				|| (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_DOWN)
				|| (event.getKeyCode() == KeyEvent.KEYCODE_POWER)
				|| (event.getKeyCode() == KeyEvent.KEYCODE_VOLUME_UP)) {

			return true;
		}
		return false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mKeyLock.unlock();
		mKeyLock = null;

	}

}
