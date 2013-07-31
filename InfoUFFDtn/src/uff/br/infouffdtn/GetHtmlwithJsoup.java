package uff.br.infouffdtn;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.os.AsyncTask;

class GetHtmlwithJsoup extends AsyncTask<String, Integer, String>
{
protected String doInBackground(String... param) 
{  
   try 
   {
     Document doc  = Jsoup.connect("http://google.com").get();
     return doc.title();
   } 
   catch (Exception e) 
   {
	   Exception x = e;
     return "Failed to get";
   }
}

@Override
protected void onProgressUpdate(Integer... values)
{ // 
   super.onProgressUpdate(values);
   // Not used in my case
}

@Override
protected void onPostExecute(String result)
{ // 
   //show Jsoup HTML Ouput to UI widgets code at here...  
}
}