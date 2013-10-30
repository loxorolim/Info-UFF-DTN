package uff.br.infouffdtn.server;



import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.io.IOUtils;

import android.graphics.Bitmap;
import uff.br.infouffdtn.db.Content;
import uff.br.infouffdtn.db.FileManager;
import uff.br.infouffdtn.dtn.DtnLog;


/**
 * A Simple Socket client that connects to our socket server
 * @author faheem
 *
 */
public class InfoClient {

    private String hostname;
    private int port;
    Socket socketClient;

    public InfoClient(String hostname, int port){
        this.hostname = hostname;
        this.port = port;
    }

    public void connect() throws UnknownHostException, IOException{
        System.out.println("Attempting to connect to "+hostname+":"+port);
        socketClient = new Socket(hostname,port);
        System.out.println("Connection Established");
    }

    public synchronized void  readResponse() throws IOException{
      //  String userInput;
      //  BufferedReader stdIn = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
        //InputStream socketInputStream = socketClient.getInputStream();
        //socketInputStream.
    	ArrayList<byte[]> bytesList = new ArrayList<byte[]>();
    	try
    	{
    		InputStream is = socketClient.getInputStream();
    		BufferedInputStream buffer = new BufferedInputStream( is );
    		ObjectInput input = new ObjectInputStream ( buffer );	      
  	        bytesList = (ArrayList<byte[]>) input.readObject();
  	       
    	}
    	catch(Exception e)
    	{
    		Exception x = e;
    	}  	 
        for(int i = 0; i< bytesList.size(); i++)
        {
        	Content c =FileManager.getContentFromBytes(bytesList.get(i),true);
        	//DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			//Date d1 = new Date();
			//String data = dateFormat.format(d1);
			//c.setDate(data);
        	FileManager.writeContent(c);
        	DtnLog.writeReceiveLogFromServer(c);
        }
	  

    }
    public void sendDtnLog() 
    {
    	OutputStream os = null;
	  	BufferedOutputStream buffer = null;   	   
	  	ObjectOutput output = null;
    	try
    	{
    		 //Send the message to the server   	  	
    	   ArrayList<String> log = DtnLog.getLog();
      	   os = socketClient.getOutputStream();
      	   buffer = new BufferedOutputStream( os );
      	   output = new ObjectOutputStream ( buffer ); 
     	  // output.writeUTF("Log");
      	   output.writeObject(log);	
      	   
      	   output.flush();
      	   output.close();

    	}
    	catch(Exception e)
    	{
    		
    	}
    }

    public void writeHeader(String header, boolean fetch)
    {
    	OutputStream os = null;
	  	BufferedOutputStream buffer = null;   	   
	  	ObjectOutput output = null;
    	try
    	{
    		 //Send the message to the server   	  	
      	   os = socketClient.getOutputStream();
      	   buffer = new BufferedOutputStream( os );
      	   output = new ObjectOutputStream ( buffer ); 
     	   output.writeUTF(header);
     	   if(fetch)
     	   output.writeObject(FileManager.getContentList());
      	   
      	   output.flush();
      	  // output.close();

    	}
    	catch(Exception e)
    	{
    		
    	}
    	
    }
    public void initialize(boolean fetch)
    {
    	// InfoClient client = new InfoClient (hostname,port);
    	InfoClient client = new InfoClient ("177.133.135.105",port);
         try {
             //trying to establish connection to the server
             client.connect();
             //if successful, read response from server
             if(fetch)
             {
                 client.writeHeader("Fetch",true);

            	 client.readResponse();
             }
             else
             {
            	 client.writeHeader("Log",false);
            	 client.sendDtnLog();
             }

         } catch (UnknownHostException e) {
             System.err.println("Host unknown. Cannot establish connection");
         } catch (IOException e) {
             System.err.println("Cannot establish connection. Server may not be up."+e.getMessage());
         }
    }

}
