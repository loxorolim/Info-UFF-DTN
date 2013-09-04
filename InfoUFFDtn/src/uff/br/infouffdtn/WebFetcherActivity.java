package uff.br.infouffdtn;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

public class WebFetcherActivity extends Activity {

	WebView w;
	ImageView i;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web);
		i = (ImageView) findViewById(R.id.imageView1);
		w = (WebView) findViewById(R.id.webView1);
		w.setWebViewClient(new WebViewClient() {
			public void onPageFinished(WebView view, String url) {
				Picture picture = view.capturePicture();
				Bitmap b = Bitmap.createBitmap(picture.getWidth(),
						picture.getHeight(), Bitmap.Config.ARGB_8888);
				Canvas c = new Canvas(b);

				picture.draw(c);
				FileOutputStream fos = null;
				try {

					fos = new FileOutputStream("mnt/sdcard/yahoo.jpg");
					if (fos != null) {
						b.compress(Bitmap.CompressFormat.JPEG, 100, fos);
						
					//	File sdCard = Environment.getExternalStorageDirectory();

					//	File directory = new File (sdCard.getAbsolutePath() + "/Pictures");

					//	File file = new File(directory, "image_name.jpg"); //or any other format supported
						fos.close();
						FileInputStream streamIn = new FileInputStream(new File("mnt/sdcard/yahoo.jpg"));

						i.setImageBitmap(BitmapFactory.decodeStream(streamIn)); //This gets the image
				

						streamIn.close();
						
						
					}
				} catch (Exception e) {

				}
			}
		});

		//setContentView(w);
		w.loadUrl("http://www.9gag.com");
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		getMenuInflater().inflate(R.menu.activity_main, menu);
//		return true;
//	}
}