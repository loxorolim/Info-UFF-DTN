import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.sql.Connection;
import java.util.ArrayList;

import javax.imageio.ImageIO;


public class ServerThread implements Runnable
{
	
	Socket client;
	public ServerThread(Socket client)
	{
		this.client = client;
	}
	@Override
	public void run() 
	{
		// TODO Auto-generated method stub
		try 
		{
			sendWelcomeMessage(client);
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
    
    private void sendWelcomeMessage(Socket client) throws IOException 
    {
       OutputStream os = null;
  	   BufferedOutputStream buffer = null;
  	   ObjectOutput output = null;
       try
       {
    	   ArrayList<byte[]> files = FileManager.getBytessToSend();
    	   os = client.getOutputStream();
    	   buffer = new BufferedOutputStream( os );
    	   output = new ObjectOutputStream ( buffer ); 
   	    
    	   output.writeObject(files);	
       }
       catch(Exception e)
       {
    	   
       }
       finally
       {
    	   output.flush();
    	   output.close();
       }
    	
    	
    	
    	
    	
    	
    	
    	/*
    	ArrayList<BufferedImage>imgs = FileManager.getImagesToSend();
    	OutputStream os = client.getOutputStream();
    	for(int i = 0;i<imgs.size();i++)
    	{

    		ByteArrayOutputStream out = new ByteArrayOutputStream();
    		ImageIO.write(imgs.get(i), "BMP", out);
    		byte[] b = out.toByteArray();  		
    		os.write(b);
    		
    		

    	}
    	os.flush();
		os.close();
		*/
		
   
    }

}
