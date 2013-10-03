package uff.br.infouffdtn;

import java.util.List;

import uff.br.infouffdtn.R;
import uff.br.infouffdtn.dtn.DtnLog;
import uff.br.infouffdtn.interfacepk.Item;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.TextView;

public class ShowLogActivity extends Activity 
{
	TextView tx;
	public void onCreate(Bundle icicle)
	{
		super.onCreate(icicle);
		try
		{
	//	getListView().setBackgroundResource(R.drawable.infouffdtnbackground);
		setContentView(R.layout.logactivity);
		tx = (TextView)findViewById(R.id.textView1);
		//registerForContextMenu(listView);
		tx.setText(DtnLog.getLogText());
		}
		catch(Exception e)
		{
			Exception x = e;
		}
	

	}
}
