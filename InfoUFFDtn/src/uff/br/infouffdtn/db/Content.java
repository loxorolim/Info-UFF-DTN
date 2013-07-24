package uff.br.infouffdtn.db;

import java.util.Date;

public class Content 
{
	private String name;
	private String date;
	private String payload;
	
	
	public Content(String name,String date, String payload)
	{
		this.setName(name);
		this.setDate(date);	
		this.setPayload(payload);

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	@Override
	public String toString()
	{
		return name + ";" + date + ";" + payload;
		
	}

}
