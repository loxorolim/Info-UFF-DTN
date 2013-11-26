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

//import org.apache.commons.io.IOUtils;

import uff.br.infouffdtn.db.Content;
import uff.br.infouffdtn.dtn.DtnLog;
import uff.br.infouffdtn.server.CommFile;


public class ServerThread implements Runnable
{
	
	Socket client;
	public FileGeneratorTest ft;
	public ServerThread(Socket client, FileGeneratorTest ft)
	{
		this.client = client;
		this.ft = ft;
	}
	@Override
	public synchronized void run() 
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
     		   try
     		   {
     			   ArrayList<CommFile> celContents = (ArrayList<CommFile>) input.readObject();
     			   sendFiles(client,celContents);
     		   }
     		   catch(Exception e)
     		   {
     			   Exception x = e;
     		   }
     		  
     		  //sendWelcomeMessage(client);
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
     	   if(req.equals("StartTimer"))
     	   {
     		  try
    		   {
    			  byte[] bytes = (byte[]) input.readObject();
    			  startTimerFromBytes(bytes);
       		  // FileManager.writeImageFromBytes(bytes);
    			  
    		   }
    		   catch(Exception e)
    		   {
    			   Exception x = e;
    		   }
     	   }
     	   if(req.equals("StopTimer"))
     	   {
     		  try
    		   {
     			  ft.deleteTimer();
    			  
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
	 	        FileManager.saveLog(log);

			}
			catch(Exception e)
			{
				
			}
	}
	public void startTimerFromBytes(byte[] b)
	{
		byte[] tam = new byte[4];
		tam[0] = b[0];
		tam[1] = b[1];
		tam[2] = b[2];
		tam[3] = b[3];
		int refreshTime = byteArrayToInt(tam);
				
		tam[0] = b[4];
		tam[1] = b[5];
		tam[2] = b[6];
		tam[3] = b[7];
		int counter = byteArrayToInt(tam);
		
		
		byte[] strBytes = new byte[b.length - 8];
		System.arraycopy(b, 8 , strBytes, 0, strBytes.length);

		
		try
		{
			String name = new String(strBytes, "Cp1252");
			ft.setTimer(name, refreshTime, counter);		
		
		}
		catch(Exception e)
		{
			Exception x = e;
		}

		
		
		
	}
	public static int byteArrayToInt(byte[] b) 
	{
	    return   b[3] & 0xFF |
	            (b[2] & 0xFF) << 8 |
	            (b[1] & 0xFF) << 16 |
	            (b[0] & 0xFF) << 24;
	}
	private void sendFiles(Socket client, ArrayList<CommFile> celFiles) throws IOException
	{
		   OutputStream os = null;
	  	   BufferedOutputStream buffer = null;
	  	   ObjectOutput output = null;
	       try
	       {
	    	   ArrayList<byte[]> files = FileManager.getBytessToSend(celFiles);
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
    
//    private void sendWelcomeMessage(Socket client) throws IOException 
//    {
//       OutputStream os = null;
//  	   BufferedOutputStream buffer = null;
//  	   ObjectOutput output = null;
//       try
//       {
//    	   ArrayList<byte[]> files = FileManager.getBytessToSend();
//    	   os = client.getOutputStream();
//    	   buffer = new BufferedOutputStream( os );
//    	   output = new ObjectOutputStream ( buffer ); 
//   	    
//    	   output.writeObject(files);	
//       }
//       catch(Exception e)
//       {
//    	   
//       }
//       finally
//       {
//    	   output.flush();
//    	   output.close();
//       }
//    	
//    	
//   
//    }
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
