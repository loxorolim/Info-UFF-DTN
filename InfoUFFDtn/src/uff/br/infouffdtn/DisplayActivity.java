package uff.br.infouffdtn;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;


import java.util.List;
















import uff.br.infouffdtn.db.Content;
import uff.br.infouffdtn.db.FileManager;
import uff.br.infouffdtn.dtn.InfoService;
import uff.br.infouffdtn.interfacepk.Item;
import uff.br.infouffdtn.interfacepk.ListItem;
import uff.br.infouffdtn.interfacepk.SlideTransition;
import uff.br.infouffdtn.interfacepk.TextArrayAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.MenuItem;
import android.view.View;

public class DisplayActivity extends Activity 
{
	@Override
	protected void onDestroy() {
		unregisterReceiver(mDataReceiver);
		super.onDestroy();
	}



	private ListView listView;
	private List<Item> values;	
	private AlertDialog.Builder deleteAllBuilder;
	private AlertDialog deleteAllDialog;
	private AlertDialog.Builder deleteBuilder;
	private AlertDialog deleteDialog;
	

	private OnItemLongClickListener llistener = new OnItemLongClickListener()
	{

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			try
			{
			// TODO Auto-generated method stub
			setDeleteDialogBuilder(arg2);
			deleteDialog.show();

			}
			catch(Exception e)
			{
				
			}
			return false;
		}
		
	};
	private OnItemClickListener listener = new OnItemClickListener()
	{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			try
			{
			String filepath = ((ListItem)(values.get(arg2))).getContent().getFilepath();
			Intent intent = new Intent(DisplayActivity.this, ShowContentActivity.class);
			intent.putExtra("filepath", filepath);
			startActivity(intent);
			SlideTransition.forwardTransition(DisplayActivity.this);
			// TODO Auto-generated method stub
			//setDeleteDialogBuilder(arg2);
			//deleteDialog.show();
			}
			catch(Exception e)
			{
				Exception x = e;
			}

		}
		
	};
	public void onCreate(Bundle icicle)
	{
		super.onCreate(icicle);
		IntentFilter filter = new IntentFilter(InfoService.REFRESH);
		IntentFilter filter2 = new IntentFilter(FileManager.REFRESH);
		registerReceiver(mDataReceiver, filter);
		registerReceiver(mDataReceiver, filter2);
	//	getListView().setBackgroundResource(R.drawable.infouffdtnbackground);
		setContentView(R.layout.displayactivitymenu);
		listView = (ListView)findViewById(R.id.listView1);
		//registerForContextMenu(listView);
		listView.setOnItemLongClickListener(llistener);
		listView.setOnItemClickListener(listener);
		
		
		setDeleteAllDialogBuilder();

		Button b = (Button) findViewById(R.id.button1);
		b.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				deleteAllDialog.show();


			}
		});

		
		

		showContents();

	}
	

	private void setDeleteAllDialogBuilder()
	{
		deleteAllBuilder = new AlertDialog.Builder(this);
		deleteAllBuilder.setMessage("Are you sure you want to delete ALL contents?");
		deleteAllBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	        	   FileManager.deleteAllFiles(DisplayActivity.this);
					showContents();
	           }
	       });
		deleteAllBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	               // User cancelled the dialog
	           }
	       });
		
		deleteAllDialog = deleteAllBuilder.create();
	}
	private void setDeleteDialogBuilder(final int pos)
	{
		deleteBuilder = new AlertDialog.Builder(this);
		deleteBuilder.setMessage("Do you want to delete this content?");
		deleteBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	        	    itemClickDelete(pos);
					showContents();
	           }
	       });
		deleteBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	               // User cancelled the dialog
	           }
	       });
		
		deleteDialog = deleteBuilder.create();
	}
	private void showContents()
	{
		// ImageView icon = (ImageView)findViewById(R.id.icon);
		try
		{
			values = FileManager.prepareContentList();
	        TextArrayAdapter adapter = new TextArrayAdapter(this, values);
	        listView.setAdapter(adapter);
		}
		catch (Exception e)
		{

		}
	}
	private void itemClickDelete(int pos)
	{
		String filepath = ((ListItem)(values.get(pos))).getContent().getFilepath();
		FileManager.deleteContent(filepath);
		showContents();
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
	public void onBackPressed() 
	{
	    this.finish();
	    SlideTransition.backTransition(this);
	}
	
	@Override  
	  public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {  
	     
		super.onCreateContextMenu(menu, v, menuInfo);  
	           menu.setHeaderTitle("Delete Content?");  
	           menu.add(0, v.getId(), 0, "Yes");  
	           menu.add(0, v.getId(), 0, "No");  
	           
	      }  
	  @Override  
	      public boolean onContextItemSelected(MenuItem item) {  
		  	
		  AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

	         if(item.getTitle()=="Yes"){yesSelected(item.getItemId(),info.id);}  	       
	       else {return false;}  
	      return true;  
	      }  
	  public void yesSelected(int id,long pos){ 
		   itemClickDelete((int)pos);
	       Toast.makeText(this, "Content Deleted", Toast.LENGTH_SHORT).show();  
	  }  
 

}
