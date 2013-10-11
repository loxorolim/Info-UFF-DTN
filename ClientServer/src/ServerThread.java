import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.IOException;
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
    
    private void sendWelcomeMessage(Socket client) throws IOException {
       
    	//ArrayList<ServerFile> files = FileManager.getFilesToSend();
    	ArrayList<BufferedImage>imgs = FileManager.getImagesToSend();
    	for(int i = 0;i<imgs.size();i++)
    	{
	    //	BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
	        //Content c6 = FileManager.readFile("nome3");
	    	
    		ImageIO.write(imgs.get(i), "png", client.getOutputStream());
    		
    	/*	OutputStream socketOutputStream = client.getOutputStream();
	        try
	        {
	        	socketOutputStream.write(files.get(i).getImageBytes());
	        }
	        catch(Exception e)
	        {
	        	//socketOutputStream.write("Cabou!");
	        }
	        
	        socketOutputStream.flush();
	        socketOutputStream.close();
	        */
    	}
    }

}
