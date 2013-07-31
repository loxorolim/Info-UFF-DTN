package uff.br.infouffdtn;

import java.io.IOException;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import uff.br.infouffdtn.MainActivity.RemindTask;
import uff.br.infouffdtn.db.ContentsDatabase;
import uff.br.infouffdtn.db.WebCollector;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.drawable.PictureDrawable;
import android.os.Bundle;
import android.text.Html;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

//Classe responsável por abrir o conteúdo de um Content quando selecionado
public class ShowContentActivity extends Activity
{
	  private Context ctx;
	  private TextView editText;
	  private WebView mWebview;
	  private ImageView imageView;
	  private static String html;
	  
	  Bitmap bm;
	  @Override
	  public void onCreate(Bundle savedInstanceState)
	  {
	        super.onCreate(savedInstanceState);

	        setContentView(R.layout.showcontentactivitymenu);
	        editText = (TextView) findViewById(R.id.textView1);	
	        HtmlGetterThread gt = new HtmlGetterThread(editText,this);
	        new Thread(gt).start();
	        

	        
	  }



}
