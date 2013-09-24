package uff.br.infouffdtn.db;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

public class Content implements Serializable
{
	private String name;
	private String date;
	private boolean fromWifi; // true = wifi false = dtn
	private String payload;

	public Content(String name, String date, boolean commSource, String payload)
	{
		this.setName(name);
		this.setDate(date);
		this.setPayload(payload);
		this.setCommSource(commSource);

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

}
