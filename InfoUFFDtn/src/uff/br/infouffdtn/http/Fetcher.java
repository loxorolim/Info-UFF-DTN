package uff.br.infouffdtn.http;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.widget.Toast;

public class Fetcher implements Runnable
{
	 public static String stream = "";
	 public void getHtml() throws ClientProtocolException, IOException
	 {
	     HttpClient httpClient = new DefaultHttpClient();
	     HttpContext localContext = new BasicHttpContext();
	     HttpGet httpGet = new HttpGet("http://www.google.com");
	     HttpResponse response = httpClient.execute(httpGet, localContext);
	     String result = "";

	     BufferedReader reader = new BufferedReader(
	         new InputStreamReader(
	           response.getEntity().getContent()
	         )
	       );

	     String line = null;
	     while ((line = reader.readLine()) != null){
	       result += line + "\n";
	     }
	     String x = result;
	     System.out.print(x);
	 }

	 private String readStream(InputStream is) {
		    try {
		      ByteArrayOutputStream bo = new ByteArrayOutputStream();
		      int i = is.read();
		      while(i != -1) {
		        bo.write(i);
		        i = is.read();
		      }
		      String x = bo.toString();
		      stream = x;
		      return x;
		    } catch (IOException e) {
		      return "";
		    }
		}
	public static String getStream()
	{
		return stream;
	}
	public String download(String url) throws Exception {
	    String filename = "local.html";
	    save(url, filename);

	    List<String> imageLinks = getImageURLs(filename);
	    for (String imageLink : imageLinks) {
	        String imageFileName = getImageName(imageLink);
	        save(imageLink, imageFileName);
	    }

	    convertImageURLs(filename);
	    return filename;
	}

	public void save(String url, String saveTo) throws Exception {
	    HttpURLConnection conn = (HttpURLConnection) (new URL(url)).openConnection();
	    conn.connect();
	    InputStream is = conn.getInputStream();
	    save(is, saveTo);
	}

	public void save(InputStream is, String saveTo) {
	    // save actual content
	}

	public List<String> getImageURLs(String localHtmlFile) {
	    // parse localHtmlFile and get all URLs for the images
	    return Collections.EMPTY_LIST;
	}

	public String getImageName(String imageLink) {
	    // get image name, from url
	    return null;
	}

	void convertImageURLs(String localHtmlFile) {
	    // convert all URLs of the images, something like:
	    // <img src="original_url"> -> <img src="local_url">
	}
	@Override
	public void run() 
	{
		   try 
		   {
		   URL url = new URL("http://www.google.com/");
		   HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		   
		     InputStream in = new BufferedInputStream(urlConnection.getInputStream());
		     readStream(in);
		     
		     urlConnection.disconnect();
		   }
		   catch(Exception e)
		   {
			   Exception x = e;
		   }

		     
		   
		 }
	
		// TODO Auto-generated method stub
		
	
}
