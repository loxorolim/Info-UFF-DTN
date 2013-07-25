package uff.br.infouffdtn;

import java.util.LinkedList;

import uff.br.infouffdtn.db.ContentsDatabase;


import android.app.Activity;
import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.view.View;

public class DisplayActivity extends ListActivity 
{

	 public void onCreate(Bundle icicle)
	 {	 
	    super.onCreate(icicle);
	    IntentFilter filter = new IntentFilter(InfoService.REFRESH);
        registerReceiver(mDataReceiver, filter);
	    showContents();
	   
	  }

	  @Override
	  protected void onListItemClick(ListView l, View v, int position, long id) 
	  {
	    String selectedArchiveName = (String) getListAdapter().getItem(position);
	    
	    Intent intent = new Intent(this, ShowContentActivity.class);
	    intent.putExtra("archiveName", selectedArchiveName);
    	startActivity(intent);  
	    
	    //Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
	  }
	  private void showContents()
	  {
		  try
		    {
		    String[] values =  ContentsDatabase.readAllArchivesNames(this);
		    // Use your own layout
		    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		        R.layout.displayactivitymenu, R.id.label, values);
		    setListAdapter(adapter);
		    }
		    catch(Exception e)
		    {
		    	
		    }
	  }
	  private BroadcastReceiver mDataReceiver = new BroadcastReceiver() {
	        @Override
	        public void onReceive(Context context, Intent intent)
	        {
	        	showContents();

	        }
	    };

	  
	  
	 


}
