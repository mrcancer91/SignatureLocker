package com.example.signaturelockscreen;

import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.content.ContextWrapper;
import android.view.View;
import android.widget.Toast;

public class MyCustomView extends View {
	public String sampleFile = "mySample.txt";
	private Canvas mCanvas;
	private Bitmap mBitmap;
	private Path mPath;
	private Paint mBitmapPaint;
	private Paint mP;

	public boolean showP;
	private long mStartTime;
	LockActivity lockAct;

	public static ArrayList<MPoint> catched_Point;
	public static ArrayList<MPoint> myPoint;

//	Thread mThread = new Thread() {
//
//		@Override
//		public void run() {
//			try {
//				Thread.sleep(500);
//				lockAct.doUnlock();
//				stopThread(this);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//
//	};
	boolean isThreadRunning = false;

	public MyCustomView(Context c) {
		super(c);
		firstInit();

	}

	public void setLock(LockActivity lock) {
		this.lockAct = lock;
	}

	public void firstInit() {
		mCanvas = new Canvas();
		mPath = new Path();
		mBitmapPaint = new Paint(Paint.DITHER_FLAG);
		mStartTime = 0;
		mP = new Paint();
		mP.setAntiAlias(true);
		mP.setColor(Color.BLUE);
		mP.setDither(true);
		mP.setStyle(Paint.Style.STROKE);
		mP.setStrokeCap(Paint.Cap.ROUND);
		mP.setStrokeJoin(Paint.Join.ROUND);
		mP.setStrokeWidth(10);
		showP = false;
		setBackgroundColor(Color.WHITE);
		catched_Point = new ArrayList<MPoint>();
		myPoint = new ArrayList<MPoint>();

	}

	private void stopThread(Thread theThread) {
		if (LockActivity.isLocking)
			if (theThread != null)
				theThread = null;
	}

	public MyCustomView(Context context, AttributeSet attSet) {
		// TODO Auto-generated constructor stub
		super(context, attSet);
		firstInit();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawColor(Color.WHITE);
		canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
		canvas.drawPath(mPath, mP);
		if (showP) {
			mP.setColor(Color.RED);
			for (MPoint p : myPoint)
				canvas.drawPoint(p.x, p.y, mP);
			mP.setColor(Color.YELLOW);
			for (MPoint p : catched_Point)
				canvas.drawPoint(p.x, p.y, mP);
			mP.setColor(Color.BLUE);
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		mBitmap = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);

	}

	public void _clearView() {
		mBitmap.eraseColor(Color.WHITE);
		mPath.reset();
		catched_Point = new ArrayList<MPoint>();
		myPoint = new ArrayList<MPoint>();
		mStartTime = 0;
		showP = false;
		invalidate();
	}

	private float mX, mY;
	private MPoint p1, p2, p3;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		MPoint point = new MPoint(x, y);
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (mStartTime == 0) {
				mStartTime = System.currentTimeMillis();
				point.time = 0;
			} else
				point.time = System.currentTimeMillis() - mStartTime;
			mPath.reset();
			mPath.moveTo(x, y);
			point.degre = 0;
			catched_Point.add(point);

			p2 = p3 = p1 = point;
			mX = x;
			mY = y;
			invalidate();
//			if (LockActivity.isLocking)
//				if (isThreadRunning) {
//					stopThread(mThread);
//					isThreadRunning = false;
//				}
			break;
		case MotionEvent.ACTION_MOVE:
			point.time = System.currentTimeMillis() - mStartTime;
			MPoint ptemp = p2;
			p2 = p3;
			p3 = point;
			mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
			mX = x;
			mY = y;
			double temp = getDegre(p1, p2, p3);
			if (temp * 180 / Math.PI < 160) {
				p2.degre = temp;
				p2.time = System.currentTimeMillis() - mStartTime;
				if (p2.time < 3000L) {
					catched_Point.add(p2);
				}
				p1 = ptemp;
			}
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			point.time = System.currentTimeMillis() - mStartTime;
			point.degre = 0;
			catched_Point.add(point);
			mPath.lineTo(mX, mY);
			mCanvas.drawPath(mPath, mP);
			mPath.reset();
			invalidate();
			
			break;
		}
		myPoint.add(point);
		return true;
	}

	public double getDegre(MPoint p1, MPoint p2, MPoint p3) {
		if ((p1.x == p2.x && p2.x != p3.x) || (p1.y == p2.y && p2.y == p3.y))
			return 180;
		else {
			double a = (p3.x - p2.x) * (p3.x - p2.x) + (p3.y - p2.y)
					* (p3.y - p2.y);
			double b = (p3.x - p1.x) * (p3.x - p1.x) + (p3.y - p1.y)
					* (p3.y - p1.y);
			double c = (p2.x - p1.x) * (p2.x - p1.x) + (p2.y - p1.y)
					* (p2.y - p1.y);
			return Math.acos((a + c - b) / (2 * Math.sqrt(c) * Math.sqrt(a)));
		}
	}

	public ArrayList<Double> nomalizeInput(ArrayList<MPoint> input,
			long timeOffset) {
		ArrayList<Double> list = new ArrayList<Double>();
		ArrayList<MPoint> newCatched = new ArrayList<MPoint>();
		int index = 0;
		// list.add(0.0);
		long time = 0;
		while (time < 3000L && list.size() < (3000L / timeOffset)) {
			if (index < input.size()) {
				while (time < input.get(index).time - timeOffset) {
					list.add(0.0);
					time += timeOffset;
					Log.d("MyTime", "Degre = " + list.get(list.size() - 1)
							+ " Time = " + time + " - "
							+ input.get(index - 1).time);
				}
				newCatched.add(input.get(index));
				list.add(input.get(index++).degre);
				time += timeOffset;
				Log.d("MyTime", "Degre = " + list.get(list.size() - 1)
						+ " Time = " + time + " - " + input.get(index - 1).time);
				while ((index < input.size() - 1)
						&& time > input.get(index).time)
					index++;
			} else {
				list.add(0.0);
				time += timeOffset;
				Log.d("MyTime", "Degre = " + list.get(list.size() - 1)
						+ " Time = " + time + " - " + input.get(index - 1).time);
			}
		}
		MyCustomView.catched_Point = newCatched;
		return list;
	}

	public StringBuffer readStringFromFile(String filePath)
			throws FileNotFoundException, IOException {
		File mFile = new File(sampleFile);
		StringBuffer stringBuffer = new StringBuffer();

		BufferedReader inputReader = new BufferedReader(
				new InputStreamReader(
						new ContextWrapper(getContext()).openFileInput(mFile
								.getPath())));
		String inputString;
		while ((inputString = inputReader.readLine()) != null) {
			stringBuffer.append(inputString + "\n");
		}
		return stringBuffer;
	}

	// public void save_catchedPoint_toFile(ArrayList<Double> input)
	// throws FileNotFoundException, JSONException, IOException {
	// File mFile = new File(sampleFile);
	// OutputStream os = null;
	// JSONObject root = new
	// JSONObject(readStringFromFile(sampleFile).toString());
	// JSONArray arr = root.getJSONArray("samples");
	// MainActivity.arrCount = arr.length() + 1;
	// os = new ContextWrapper(getContext()).openFileOutput(mFile.getPath(),
	// Context.MODE_PRIVATE);
	//
	// JSONObject object = new JSONObject();
	// String str = "";
	// for (Double p : input)
	// str += (p + " ");
	// object.put("id", MainActivity.arrCount);
	// object.put("value", str);
	// root.getJSONArray("samples").put(object);
	// os.write(root.toString().getBytes());
	// os.flush();
	// os.close();
	// Toast.makeText(getContext(),
	// "Save completed ID: " + MainActivity.arrCount,
	// Toast.LENGTH_SHORT).show();
	// }

	public void save1stFile(ArrayList<Double> input) {
		try {
			File sampleFile = new File("mySample.txt");
			JSONObject root = null;
			JSONArray arr = null;
			FileOutputStream outstream = null;
			String filePath = sampleFile.getPath();
			int arrLength = 1;

			outstream = new ContextWrapper(getContext()).openFileOutput(
					filePath, Context.MODE_PRIVATE);
			root = new JSONObject();
			arr = new JSONArray();
			JSONObject object = new JSONObject();
			String str = "";
			for (Double p : input)
				str += (p + " ");
			object.put("id", arrLength);
			object.put("value", str);
			arr.put(object);
			root.put("samples", arr);
			outstream.write(root.toString().getBytes());
			outstream.flush();
			outstream.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void save_everyPoint_toFile() {

	}

	public JSONObject parseJSONData(String filename) {
		String JSONString = null;
		JSONObject JSONObject = null;
		try {

			InputStream inputStream = new ContextWrapper(getContext())
					.getAssets().open(filename);
			int sizeOfJSONFile = inputStream.available();
			byte[] bytes = new byte[sizeOfJSONFile];
			inputStream.read(bytes);
			inputStream.close();
			JSONString = new String(bytes, "UTF-8");
			JSONObject = new JSONObject(JSONString);
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		} catch (JSONException x) {
			x.printStackTrace();
			return null;
		}
		return JSONObject;
	}
}

class MPoint {
	float x, y;
	public long time;
	public double degre;

	public MPoint(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public MPoint() {

	}

	@Override
	public String toString() {
		return x + "-" + y + "-" + degre;
	}

}