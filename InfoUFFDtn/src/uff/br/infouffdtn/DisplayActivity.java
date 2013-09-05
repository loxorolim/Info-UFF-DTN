package uff.br.infouffdtn;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;


import uff.br.infouffdtn.db.ContentsDatabase;

import android.app.Activity;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.view.View;

public class DisplayActivity extends Activity implements OnItemClickListener
{
	private ListView listView;
	private String[] values;

	public void onCreate(Bundle icicle)
	{
		super.onCreate(icicle);
		IntentFilter filter = new IntentFilter(InfoService.REFRESH);
		registerReceiver(mDataReceiver, filter);
	//	getListView().setBackgroundResource(R.drawable.infouffdtnbackground);
		setContentView(R.layout.displayactivitymenu);
		listView = (ListView)findViewById(R.id.listView1);
		showContents();

	}



	private void showContents()
	{
		// ImageView icon = (ImageView)findViewById(R.id.icon);
		try
		{
			values = ContentsDatabase.readAllArchivesDates(this);
			sortByDate(values);
			// Use your own layout
			// ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
			// R.layout.displayactivitymenu, R.id.label, values);

			DisplayAdapter adapter = new DisplayAdapter(R.layout.rowlayout ,values,this);
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(this);
		}
		catch (Exception e)
		{

		}
	}

	private void sortByDate(String[] values) throws ParseException
	{
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date di = new Date();
		Date dj = new Date();
		// d1 = dateFormat.parse(date1);
		for (int i = 0; i < values.length - 1; i++)
		{

			for (int j = i + 1; j < values.length; j++)
			{
				di = dateFormat.parse(values[i]);
				dj = dateFormat.parse(values[j]);
				if (dj.after(di))
				{
					Date aux = di;
					values[i] = dateFormat.format(dj);
					values[j] = dateFormat.format(aux);
				}
			}
		}
	}

	// Quando receber um intent chamado REFRESH, ele mostra os arquivos.
	private BroadcastReceiver mDataReceiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			showContents();

		}
	};

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int position, long id) 
	{
		String selectedArchiveDate = values[position];
		Intent intent = new Intent(this, ShowContentActivity.class);
		intent.putExtra("archiveName", selectedArchiveDate);
		startActivity(intent);
		
	}

}
