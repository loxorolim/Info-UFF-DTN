package uff.br.infouffdtn.db;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
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
	private String filepath;

	public Content(String name, String date, boolean commSource,String filepath)
	{
		this.setName(name);
		this.setDate(date);
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
		return name + "<CONTENTSPLIT>" + date + "<CONTENTSPLIT>" + filepath;

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
	public byte[] getBitmapBytes()
	{
		byte[] byteArray = null;
		try 
		{
			Bitmap bm = getBitmap();
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
			byteArray = stream.toByteArray();
			
			
		} 
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return byteArray;
		
	}


	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
	

}
