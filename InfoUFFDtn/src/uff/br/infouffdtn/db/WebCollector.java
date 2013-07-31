package uff.br.infouffdtn.db;
import android.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.PictureDrawable;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;



public class WebCollector 
{
    /** Called when the activity is first created. */

    private WebView web;
    private Bitmap bm;
    private ImageView imageView;


    public WebCollector( ImageView view,WebView webv )
    {
    	try
    	{
        	this.imageView = view;   	
        	web = webv;
            web.setWebViewClient(new myWebClient());
//            web.getSettings().setJavaScriptEnabled(true);
            web.loadUrl("http://www.google.com");
      //      webv.loadUrl("http://www.google.com");
    	}
    	catch(Exception e)
    	{
    		Exception x = e;
    	}

        
    }
	
    public class myWebClient extends WebViewClient
    {

        @Override
        public void onPageFinished(WebView view, String url) 
        {
        	 Picture pic = web.capturePicture();
  		     bm = pictureDrawable2Bitmap(pic);
  		     imageView.setImageBitmap(bm);
 
        }
      private Bitmap pictureDrawable2Bitmap(Picture picture)
  	  {
  		  try
  		  {
  		    PictureDrawable pd = new PictureDrawable(picture);
  		    int y = pd.getIntrinsicHeight();
  		    int x = pd.getIntrinsicWidth();
  		    Bitmap bitmap = Bitmap.createBitmap(x, y, Config.ARGB_8888);
  		    Canvas canvas = new Canvas(bitmap);
  		    canvas.drawPicture(pd.getPicture());
  		    return bitmap;
  		  }
  		  catch(Exception e)
  		  {
  			  System.out.print(e);
  		  }
  		  return null;
  		   
  		}
    }




}
