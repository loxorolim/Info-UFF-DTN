package uff.br.infouffdtn;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import uff.br.infouffdtn.db.Content;
import uff.br.infouffdtn.db.ContentsDatabase;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.webkit.WebView;


public class HtmlGetterThread implements Runnable
{

	private String path;
	private Context ctx;
	
	public HtmlGetterThread(String path,Context c)
	{
		this.path = path;
		this.ctx = c;
	}
	@Override
	public void run() 
	{
        try
        {
        	//URL URL = new URL("http://www.ic.uff.br/index.php/pt/");
        	URL URL = new URL("http://www.9gag.com");
        	File File = new File(path + "/arquivo.html");    	
        	org.apache.commons.io.FileUtils.copyURLToFile(URL, File);
        	String fileString = org.apache.commons.io.FileUtils.readFileToString(File);
        	DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        	Date date = new Date();
        	String d = dateFormat.format(date);
        	Content newRecover = new Content("WebPage",d,true,fileString);
        	ContentsDatabase.writeTest(newRecover, ctx);
        	
        }
        catch(Exception e)
        {
        	Exception x = e;
        	
        }                                             
    } 
}


