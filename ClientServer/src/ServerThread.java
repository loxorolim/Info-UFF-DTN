import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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

import org.apache.commons.io.IOUtils;

import uff.br.infouffdtn.dtn.DtnLog;


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
		InputStream is = null;
	  	BufferedInputStream buffer = null;   	   
	  	ObjectInput input = null;
    	try
    	{
      	   is = client.getInputStream();
      	   buffer = new BufferedInputStream( is );
      	   input = new ObjectInputStream ( buffer ); 
     	   String req = input.readUTF();
     	   if(req.equals("Log"))
     	   {
     		   receiveLog();
     	   }
     	   if(req.equals("Fetch"))
     	   {
     		  sendWelcomeMessage(client);
     	   }
     	   if(req.equals("DeleteAll"))
     	   {
     		   FileManager.deleteAllFiles();
     	   }
     	   if(req.equals("GetLog"))
    	   {
    		   sendDtnLog(client);
    	   }
     	   if(req.equals("Write"))
     	   {
     		   try
     		   {
     			  byte[] bytes = (byte[]) input.readObject();
        		   FileManager.writeImageFromBytes(bytes);
     		   }
     		   catch(Exception e)
     		   {
     			   Exception x = e;
     		   }
     		  
     	   }
    	}
    	catch(Exception e)
    	{
    		
    	}
		
	}
	private void receiveLog()
	{
		   ArrayList<String> log = null;
	       try
	       {
	    	    InputStream is = client.getInputStream();
	   		 	BufferedInputStream buffer = new BufferedInputStream( is );
	   			ObjectInput input = new ObjectInputStream ( buffer );	      
	 	        log = (ArrayList<String>) input.readObject();

			}
			catch(Exception e)
			{
				
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
    	
    	
   
    }
    private void sendDtnLog(Socket client) throws IOException 
    {
       OutputStream os = null;
  	   BufferedOutputStream buffer = null;
  	   ObjectOutput output = null;
       try
       {
    	   String log = FileManager.loadLog();
    	   os = client.getOutputStream();
    	   buffer = new BufferedOutputStream( os );
    	   output = new ObjectOutputStream ( buffer );   	    
    	   output.writeObject(log);	
       }
       catch(Exception e)
       {
    	   
       }
       finally
       {
    	   output.flush();
    	   output.close();
       }
    	
   
    }

}
