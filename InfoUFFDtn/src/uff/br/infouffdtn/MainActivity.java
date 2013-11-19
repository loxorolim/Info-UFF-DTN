package uff.br.infouffdtn;

import java.io.File;

import android.provider.Settings.Secure;

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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
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
import uff.br.infouffdtn.dtn.DtnLog;
import uff.br.infouffdtn.dtn.DtnMode;
import uff.br.infouffdtn.dtn.InfoService;
import uff.br.infouffdtn.interfacepk.SlideTransition;
import uff.br.infouffdtn.server.InfoClient;

public class MainActivity extends Activity
{

	
	private InfoService mService = null;
	private boolean mBound = false;

	private Timer timerShare;

	private final int TIMETOSHARE = 45; //15 min
	
	private Timer timerRefreshNeighbours;

	private final int TIMETOREFRESHNEIGHBOURS = 30; //2 min
	
	private Timer timerToFetch;

	private final int TIMETOFETCH= 60; //6 horas
	
	private Timer timerToSendLog;

	private final int TIMETOSENDLOG= 30; //30 seg




	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		FileManager.setAppPath(MainActivity.this.getFilesDir().getAbsolutePath());
		FileManager.setContext(MainActivity.this);
		
		String android_id = Secure.getString(MainActivity.this.getContentResolver(),Secure.ANDROID_ID);
		DtnLog.setMyPhoneName(android_id);
		setTitle("Info UFF DTN");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main3);
		
//		alertServiceToSend();
//		timerShare = new Timer();
//		timerShare.schedule(new ShareTask(), TIMETOSHARE * 1000);
//		
//		timerRefreshNeighbours = new Timer();
//		timerRefreshNeighbours.schedule(new RefreshTask(), TIMETOREFRESHNEIGHBOURS * 1000);
//		
//		Thread t = new Thread(new HtmlGetterThread("rolim.no-ip.org", 9990,true));
//		t.start();
//		timerToFetch = new Timer();
//		timerToFetch.schedule(new FetchTask(), TIMETOFETCH * 1000);
//		
		timerToSendLog = new Timer();
		timerToSendLog.schedule(new SendLogTask(), TIMETOSENDLOG * 1000);
//		
//		
		

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
						
					//	Thread t = new Thread(new HtmlGetterThread("rolim.no-ip.org", 9990,true));
					//	t.start();
						
						
						DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
						Date d1 = new Date();
						String data = dateFormat.format(d1);
						String filepath = FileManager.getAvaiableFilepath();
						Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.infouffdtnlogo);
						
						//String fp = FileManager.writeValidation("JornalUFF", MainActivity.this, 0);						
						Content ct = new Content("Teste", data, false,filepath,bm);
						FileManager.writeContent(ct);
						InfoService.contentToSend = ct;
						alertServiceToSend();

//						
//						//recoverWebPage();
					}
					catch (Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			Button b3 = (Button) findViewById(R.id.logButton);
			b3.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					try
					{
						Thread t = new Thread(new HtmlGetterThread("rolim.no-ip.org", 9990,false,MainActivity.this));
						t.start();
						//Intent intent = new Intent(MainActivity.this, ShowLogActivity.class);
						//startActivity(intent);
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
			Button b5 = (Button) findViewById(R.id.button3);
			b5.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{

					Thread t = new Thread(new HtmlGetterThread("rolim.no-ip.org", 9990,true,MainActivity.this));
					t.start();

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

			timerShare.schedule(new ShareTask(), TIMETOSHARE * 1000);
		}
	}
	class RefreshTask extends TimerTask
	{
		public void run()
		{
			try
			{
				alertServiceToRefresh();
			}
			catch (Exception e)
			{
				
			}

			timerRefreshNeighbours.schedule(new RefreshTask(), TIMETOREFRESHNEIGHBOURS * 1000);
		}
	}
	class FetchTask extends TimerTask
	{
		public void run()
		{
			try
			{
				Thread t = new Thread(new HtmlGetterThread("rolim.no-ip.org", 9990,true,MainActivity.this));
				t.start();
			}
			catch (Exception e)
			{

			}

			timerToFetch.schedule(new FetchTask(), TIMETOFETCH * 1000);
		}
	}
	class SendLogTask extends TimerTask
	{
		public void run()
		{
			try
			{
				Thread t = new Thread(new HtmlGetterThread("rolim.no-ip.org", 9990,false,MainActivity.this));
				t.start();
			}
			catch (Exception e)
			{

			}

			timerToSendLog.schedule(new SendLogTask(), TIMETOSENDLOG* 1000);
		}
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
				i.setAction(InfoService.DTN_REQUEST_INTENT);
				this.startService(i);
				i = new Intent(this, InfoService.class);
				
			}
			catch (Exception e)
			{

			}
		}
	private void alertServiceToRefresh()
	{
		try
		{
			
			Intent i = new Intent(this, InfoService.class);
			i.setAction(InfoService.DTN_REFRESH_INTENT);
			startService(i);
			i = new Intent(this, InfoService.class);
			
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
  
}
