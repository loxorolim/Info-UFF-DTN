package uff.br.infouffdtn;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import de.tubs.ibr.dtn.api.GroupEndpoint;
import uff.br.infouffdtn.R;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import uff.br.infouffdtn.db.*;
import uff.br.infouffdtn.dtn.InfoService;
import uff.br.infouffdtn.interfacepk.SlideTransition;

public class MainActivity extends Activity
{

	private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private GestureDetector gestureDetector;
	
	private InfoService mService = null;
	private boolean mBound = false;
	private Timer timerFetch;
	private Timer timerShare;
	private final int TIMETOFETCH = 10;
	private final int TIMETOSHARE = 10;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{

		setTitle("Info UFF DTN");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main3);
		// mTextEid = (EditText)findViewById(R.id.editEid);
		// editText = (TextView) findViewById(R.id.textView1);

	
		
		
		//DESCOMENTAR DEPOIS
		//timerFetch = new Timer();
		//timerFetch.schedule(new FetchTask(), TIMETOFETCH * 2000);
		//timerShare = new Timer();
		//timerShare.schedule(new ShareTask(), TIMETOSHARE * 1000);

		// assign an action to the ping button
		try
		{
			Button b = (Button) findViewById(R.id.button1);
			b.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					alertServiceToSend();
				//	DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				//	Date d1 = new Date();
				//	String data = dateFormat.format(d1);
				//	Content ct = new Content("JornalUFF", data, true, "teste");
				//	FileManager.writeContent(ct, MainActivity.this);

				}
			});
			Button b2 = (Button) findViewById(R.id.button2);
			b2.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					try
					{
						DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
						Date d1 = new Date();
						String data = dateFormat.format(d1);

						Content ct = new Content("JornalUFF", data, true, "teste");
						FileManager.writeContent(ct, MainActivity.this);
						ct = new Content("QuadroUFF", data, true, "teste");
						FileManager.writeContent(ct, MainActivity.this);
						ct = new Content("NoticiasUFF", data, true, "teste");
						FileManager.writeContent(ct, MainActivity.this);
						//recoverWebPage();
					}
					catch (Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});

			Button b4 = (Button) findViewById(R.id.displayactivity);
			b4.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					
					Intent intent = new Intent(MainActivity.this, DisplayActivity.class);
					startActivity(intent);
					SlideTransition.forwardTransition(MainActivity.this);
				}
			});
				
		}
		catch (Exception e)
		{

		}
	}

	@Override
	protected void onDestroy()
	{
		// unbind from service
		if (mBound)
		{
			// unbind from the PingService
			unbindService(mConnection);
			mBound = false;
		}

		super.onDestroy();
	}

	@Override
	protected void onPause()
	{
		super.onPause();

	}

	@Override
	protected void onResume()
	{
		super.onResume();

		if (!mBound)
		{
			// bind to the PingService
			bindService(new Intent(this, InfoService.class), mConnection, Context.BIND_AUTO_CREATE);
			mBound = true;
		}

	}

	class FetchTask extends TimerTask
	{
		public void run()
		{
			recoverWebPage();
			// timer.cancel(); //Terminate the timer thread
			timerFetch.schedule(new FetchTask(), TIMETOFETCH * 1000);
		}
	}

	class ShareTask extends TimerTask
	{
		public void run()
		{
			try
			{
				alertServiceToSend();
			}
			catch (Exception e)
			{

			}
			// timer.cancel(); //Terminate the timer thread
			timerShare.schedule(new ShareTask(), TIMETOSHARE * 1000);
		}
	}

	public void recoverWebPage()
	{
		PackageManager m = getPackageManager();
		String s = getPackageName();
		try
		{
			PackageInfo p = m.getPackageInfo(s, 0);
			s = p.applicationInfo.dataDir;
		}
		catch (NameNotFoundException e)
		{
			Log.w("yourtag", "Error Package name not found ", e);
		}
		Thread t = new Thread(new HtmlGetterThread(s, this));
		t.start();

	}

	private ServiceConnection mConnection = new ServiceConnection()
	{
		public void onServiceConnected(ComponentName name, IBinder service)
		{
			mService = ((InfoService.LocalBinder) service).getService();
		}

		public void onServiceDisconnected(ComponentName name)
		{
			mService = null;
		}
	};

	private void alertServiceToSend()
	{
		try
		{

			Intent i = new Intent(this, InfoService.class);
			i.setAction(InfoService.SEND_CONTENT_INTENT);
			startService(i);
			i = new Intent(this, InfoService.class);
			// i.setAction(InfoService.PING_INTENT);

			// i.putExtra("destination", mTextEid.getText().toString());
			// i.putExtra("destination", "dtn://androidRolim");
			// startService(i);
		}
		catch (Exception e)
		{

		}
	}
	@Override
	public void onBackPressed() 
	{
	    this.finish();
	    SlideTransition.backTransition(this);
	}
   /* class MyGestureDetector extends SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
	    Intent intent = new Intent(MainActivity.this.getBaseContext(), MainActivity.class);
 
            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH) {
                return false;
            }
 
            // right to left swipe
            if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
    		startActivity(intent);
    		MainActivity.this.overridePendingTransition(
			R.anim.slide_in_right,
			R.anim.slide_out_left
    		);
    	    // right to left swipe
            }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
    		startActivity(intent);
    		MainActivity.this.overridePendingTransition(
			R.anim.slide_in_left, 
			R.anim.slide_out_right
    		);
            }
 
            return false;
        }
 
        // It is necessary to return true from onDown for the onFling event to register
        @Override
        public boolean onDown(MotionEvent e) {
	        	return true;
        }

    }*/
}
