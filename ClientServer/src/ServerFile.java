import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;

public class ServerFile implements Serializable{
	String filepath;
	int counter;

	public String getFilepath() 
	{
		return filepath;
	}

	public void setFilepath(String filepath) 
	{
		this.filepath = filepath;
	}

	public int getCounter()
	{
		return counter;
	}

	public void setCounter(int counter) 
	{
		this.counter = counter;
	}
	public byte[] getImageBytes()
	{
		byte[] imageInByte = null;
		try
	 	{			 
			BufferedImage originalImage =ImageIO.read(new File(getFilepath()));
		 
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write( originalImage, "png", baos );
			baos.flush();
			imageInByte = baos.toByteArray();
			baos.close(); 
		}
 		catch(IOException e)
 		{
			System.out.println(e.getMessage());
		}	
		return imageInByte;
		
			   	
	}

	public ServerFile(String filepath, int counter,BufferedImage img) 
	{
		this.filepath = filepath;
		this.counter = counter;
		FileManager.writeImageToFile(img,filepath);
	}


}
