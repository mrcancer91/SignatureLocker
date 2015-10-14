package com.example.signaturelockscreen;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import com.example.signaturelockscreen.util.KohonenNetwork;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import android.os.Build;

public class GetSample extends ActionBarActivity {

	private int count;
	MyCustomView mcus;
	Button btnClear, btnOK;
	String temp = "";
	long timeOffset = 100;
	KohonenNetwork mNetwork;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_sample);
		Toast.makeText(getApplicationContext(), "Bạn sẽ nhập 5 mẫu chữ ký!",
				Toast.LENGTH_LONG).show();
		mcus = (MyCustomView) findViewById(R.id.mCusView);
		mNetwork = new KohonenNetwork();
		count = 0;
		btnOK = (Button) findViewById(R.id.btnOK);
		btnClear = (Button) findViewById(R.id.btnClear);

		btnClear.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mcus._clearView();

			}
		});

		btnOK.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					getRecognizeID();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		});
	}

	private void getRecognizeID() throws IOException {
		int k = mNetwork.recognize(mcus.nomalizeInput(mcus.catched_Point, 100));
		temp += k + " ";
		count++;
		if (5 - count > 0) {
			Toast.makeText(
					getApplicationContext(),
					"ID = " + k + "\nBạn cần nhập " + (5 - count) + " lần nữa!",
					Toast.LENGTH_LONG).show();
			mcus._clearView();
		} else {
			Toast.makeText(getApplicationContext(), "Đã xong. Thoát!",
					Toast.LENGTH_SHORT).show();
			ContextWrapper ctw = new ContextWrapper(this);
			OutputStream os = ctw.openFileOutput("myID.txt",
					Context.MODE_PRIVATE);
			os.write(temp.trim().getBytes());
			os.flush();
			os.close();
			temp = "";
			GetSample.this.finish();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.get_sample, menu);
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

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

	}

}
