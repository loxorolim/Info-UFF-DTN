package uff.br.infouffdtn.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.Date;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Content implements Serializable
{
	private String name;
	private String date;
	private boolean fromWifi; // true = wifi false = dtn
	private String payload;
	private String filepath;

	public Content(String name, String date, boolean commSource, String payload,String filepath)
	{
		this.setName(name);
		this.setDate(date);
		this.setPayload(payload);
		this.setCommSource(commSource);
		this.setFilepath(filepath);

	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getPayload()
	{
		return payload;
	}

	public void setPayload(String payload2)
	{
		this.payload = payload2;
	}

	public String getDate()
	{
		return date;
	}

	public void setDate(String date)
	{
		this.date = date;
	}

	@Override
	public String toString()
	{
		return name + "<CONTENTSPLIT>" + date + "<CONTENTSPLIT>" + payload;

	}

	public boolean isCommSource()
	{
		return fromWifi;
	}

	public void setCommSource(boolean commSource)
	{
		this.fromWifi = commSource;
	}
	public Bitmap getBitmap()
	{
		return BitmapFactory.decodeFile(getFilepath()+".png");
	}
	public void setBitmap(Bitmap bm)
	{
		try 
		{
			FileOutputStream fos = new FileOutputStream(getFilepath()+".png");
			bm.compress(Bitmap.CompressFormat.PNG, 100, fos);
			fos.flush();
			fos.close();
			
		} 
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
	

}
