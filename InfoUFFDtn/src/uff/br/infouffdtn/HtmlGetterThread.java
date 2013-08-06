package uff.br.infouffdtn;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.webkit.WebView;


public class HtmlGetterThread implements Runnable
{
	private WebView webView;
	private Activity activity;
	private String path;
	public HtmlGetterThread(WebView t, Activity ac)
	{
		webView = t;
		activity = ac;
	}
	public HtmlGetterThread(String path)
	{
		this.path = path;
	}
	@Override
	public void run() 
	{
        try
        {
        	URL URL = new URL("http://www.ic.uff.br/index.php/pt/");
        	File File = new File(path + "/arquivo.html");
        	
        	org.apache.commons.io.FileUtils.copyURLToFile(URL, File);
        	String k = File.getAbsolutePath();
        	String x = " ";
        }
        catch(Exception e)
        {
        	Exception x = e;
        	
        }
     /*   Document doc;                                      
        try {                                              
            doc = Jsoup.connect("http://google.ca/").get();
            String html = doc.html();           
            
            activity.runOnUiThread(new HtmlGetterThreadUI(webView,html));
            
            
        } catch (IOException e) {                          
            e.printStackTrace();                           
        }    */                                              
    } 
}
class HtmlGetterThreadUI implements Runnable
{
	private WebView webView;
	private String html;
	public HtmlGetterThreadUI(WebView t, String h)
	{
		webView = t;
		html = h;
	}
	@Override
	public void run() 
	{                                      
        webView.loadData(html, "text/html", "UTF-8");                                           
    } 
}

