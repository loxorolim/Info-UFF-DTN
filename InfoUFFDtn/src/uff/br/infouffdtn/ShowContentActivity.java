package uff.br.infouffdtn;

import uff.br.infouffdtn.db.ContentsDatabase;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

//Classe responsável por abrir o conteúdo de um Content quando selecionado
public class ShowContentActivity extends Activity
{
	  private Context ctx;
	  private TextView editText;
	  @Override
	  public void onCreate(Bundle savedInstanceState)
	  {
	        super.onCreate(savedInstanceState);
	        ctx = this;
	        try
	        {
		        setContentView(R.layout.showcontentactivitymenu);
		        editText = (TextView) findViewById(R.id.textView1);
		        Intent intent = getIntent();
		        String message = ContentsDatabase.readArchiveContentPayload(intent.getStringExtra("archiveName"),ctx);
		        editText.setText(message);
	        }
	        catch(Exception e)
	        {
	        	
	        }
	        

	        
	  }

}
