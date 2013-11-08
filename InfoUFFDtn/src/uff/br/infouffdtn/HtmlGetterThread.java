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
import uff.br.infouffdtn.db.FileManager;
import uff.br.infouffdtn.server.InfoClient;
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

	private String ip;
	private int port;
	private boolean fetch;
	private Context ctx;

	public HtmlGetterThread(String ip, int i, boolean fetch, Context ctx)
	{
		this.ip = ip;
		this.port = i;
		this.fetch = fetch;
		this.ctx = ctx;
	}

	@Override
	public void run()
	{
		try
		{

			InfoClient ic = new InfoClient(ip,port,ctx);
			ic.initialize(fetch);

		}
		catch (Exception e)
		{
			Exception x = e;

		}
	}
}
