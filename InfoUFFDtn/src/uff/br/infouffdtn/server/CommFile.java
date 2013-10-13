package uff.br.infouffdtn.server;

import java.io.Serializable;

public class CommFile implements Serializable
{
	String name;
	String date;

	public CommFile(String name, String date)
	{
		setName(name);
		setDate(date);
		
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	

}