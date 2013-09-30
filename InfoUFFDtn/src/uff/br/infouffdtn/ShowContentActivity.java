package uff.br.infouffdtn;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import uff.br.infouffdtn.interfacepk.SlideTransition;
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
import android.webkit.WebSettings.ZoomDensity;
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
		// editText = (TextView) findViewById(R.id.textView1);
		mWebview = (WebView) findViewById(R.id.webView1);
		mWebview.setClickable(false);
		mWebview.getSettings().setDefaultZoom(ZoomDensity.FAR);
		mWebview.getSettings().setBuiltInZoomControls(true);
		mWebview.getSettings().setSupportZoom(true);
		// mWebview.getSettings().setJavaScriptEnabled(true);
		Intent intent = getIntent();
		String archiveName = intent.getStringExtra("archiveName");
		try
		{
			/*String payloadString = ContentsDatabase.readArchiveContentPayload(archiveName, this);
			File AuxFile = new File("/data/data/uff.br.infouffdtn/arquivoHtmlAux");
			org.apache.commons.io.FileUtils.writeStringToFile(AuxFile, payloadString);
			mWebview.loadUrl("file:///data/data/uff.br.infouffdtn/arquivoHtmlAux");
			// mWebview.loadUrl(payloadString);
*/
		}
		catch (Exception e)
		{

		}
		// HtmlGetterThread gt = new HtmlGetterThread(mWebview,this);
		// new Thread(gt).start();

	}
	@Override
	public void onBackPressed() 
	{
	    this.finish();
	    SlideTransition.backTransition(this);
	}

}
