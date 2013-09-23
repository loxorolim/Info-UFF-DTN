import java.io.Serializable;


public class Content implements Serializable
{
	public String name;
	public String type;
	public String data;
	
	
	public Content(String name,String type,String data)
	{
		this.name = name;
		this.type = type;
		this.data = data;
	}

}
