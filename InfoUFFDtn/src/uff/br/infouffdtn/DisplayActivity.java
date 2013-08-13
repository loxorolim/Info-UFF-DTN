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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
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
	    String selectedArchiveDate = (String) getListAdapter().getItem(position);
	    
	    Intent intent = new Intent(this, ShowContentActivity.class);
	    intent.putExtra("archiveName", selectedArchiveDate);
    	startActivity(intent);  
	    
	    //Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
	  }
	  private void showContents()
	  {
//		  ImageView icon = (ImageView)findViewById(R.id.icon);
		  try
		    {
		    String[] values =  ContentsDatabase.readAllArchivesDates(this);	
		    sortByDate(values);
		    // Use your own layout
		    //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		    //R.layout.displayactivitymenu, R.id.label, values);
		    DisplayAdapter adapter = new DisplayAdapter(this,values);
				    
		    
		    
		    setListAdapter(adapter);
		    }
		    catch(Exception e)
		    {
		    	
		    }
	  }
	  private void sortByDate(String [] values) throws ParseException
	  {
		  DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	      Date di = new Date();
	      Date dj = new Date();
	   //   d1 = dateFormat.parse(date1);
	      for(int i = 0; i< values.length -1; i++)
	      {
	    	  
	    	  for(int j = i+1; j< values.length; j++)
	    	  {
	    		  di = dateFormat.parse(values[i]);
	    		  dj = dateFormat.parse(values[j]);
	    		  if(dj.after(di))
	    		  {
	    			  Date aux = di;
	    			  values[i] = dateFormat.format(dj);
	    			  values[j] = dateFormat.format(aux);
	    		  }
	    	  }
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
