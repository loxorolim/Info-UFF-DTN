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

	public HtmlGetterThread(String ip, int i)
	{
		this.ip = ip;
		this.port = i;
	}

	@Override
	public void run()
	{
		try
		{

			InfoClient ic = new InfoClient("177.40.233.154",9990);
			ic.initialize();

		}
		catch (Exception e)
		{
			Exception x = e;

		}
	}
}
