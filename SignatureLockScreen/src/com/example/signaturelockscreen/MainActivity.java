package com.example.signaturelockscreen;

import java.io.IOException;
import java.io.OutputStream;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.method.KeyListener;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.*;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

public class MainActivity extends ActionBarActivity {
	Button btnStartLock, btnGetSample;
	EditText txtPas;
	public static MyBroadcast mBroadcast;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btnStartLock = (Button) findViewById(R.id.btnStartLock);
		btnGetSample = (Button) findViewById(R.id.btnOK);
		// txtPas = (EditText) findViewById(R.id.txtPasword);
		if (isMyServiceRunning())
			btnStartLock.setText("Dừng khóa");

		btnStartLock.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (btnStartLock.getText().equals("Bắt đầu khóa")) {
					startLock();
					btnStartLock.setText("Dừng khóa");
					MainActivity.this.finish();
				} else {
					Intent i = new Intent(MainActivity.this, MyService.class);
					MainActivity.this.stopService(i);
					btnStartLock.setText("Bắt đầu khóa");
				}

			}
		});
		btnGetSample.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, GetSample.class);
				MainActivity.this.startActivity(i);
			}
		});

	}

	private boolean isMyServiceRunning() {
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if (MyService.class.getName()
					.equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}

	private void startLock() {
		try {
			startService(new Intent(this, MyService.class));
		} catch (Exception e) {
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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

	private void saveFile(String temp, String fileName) throws IOException {
		ContextWrapper ctw = new ContextWrapper(this);
		OutputStream os = ctw.openFileOutput(fileName, Context.MODE_PRIVATE);
		os.write(temp.trim().getBytes());
		os.flush();
		os.close();
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

	}

}
