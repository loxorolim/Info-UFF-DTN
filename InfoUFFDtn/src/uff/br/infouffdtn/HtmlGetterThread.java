package uff.br.infouffdtn;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.app.Activity;
import android.text.Html;
import android.text.Spanned;
import android.widget.TextView;

public class HtmlGetterThread implements Runnable
{
	private TextView editText;
	private Activity activity;
	public HtmlGetterThread(TextView t, Activity ac)
	{
		editText = t;
		activity = ac;
	}
	@Override
	public void run() 
	{
        Document doc;                                      
        try {                                              
            doc = Jsoup.connect("http://google.ca/").get();
            String html = doc.html();           
            
            activity.runOnUiThread(new HtmlGetterThreadUI(editText,html));
            
            
        } catch (IOException e) {                          
            e.printStackTrace();                           
        }                                                  
    } 
}
class HtmlGetterThreadUI implements Runnable
{
	private TextView editText;
	private String html;
	public HtmlGetterThreadUI(TextView t, String h)
	{
		editText = t;
		html = h;
	}
	@Override
	public void run() 
	{                                      
        editText.setText(Html.fromHtml(html));                                                  
    } 
}

