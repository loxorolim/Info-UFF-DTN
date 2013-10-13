package uff.br.infouffdtn.server;



import java.io.BufferedInputStream;
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
        	FileManager.writeContent(c);
        }
	  
    	
    	//InputStream is = socketClient.getInputStream();
    	//byte[] bytes = IOUtils.toByteArray(is);
    	
    	//Bitmap b = FileManager.getBitmapFromBytes(bytes);
    	//DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		//Date d1 = new Date();
		//String data = dateFormat.format(d1);
    	//if( b != null)
    //	{
    		//Content x = new Content("Teste", data, true,FileManager.getAvaiableFilepath(),b);
    		//FileManager.writeContent(x);
    //		FileManager.getContentFromBytes(b)
    //	}
        //  System.out.println("Response from server:");
      //  while ((userInput = stdIn.readLine()) != null) {
      //      System.out.println(userInput);
      //  }
    }
    public void initialize()
    {
    	 InfoClient client = new InfoClient (hostname,port);
         try {
             //trying to establish connection to the server
             client.connect();
             //if successful, read response from server
             client.readResponse();

         } catch (UnknownHostException e) {
             System.err.println("Host unknown. Cannot establish connection");
         } catch (IOException e) {
             System.err.println("Cannot establish connection. Server may not be up."+e.getMessage());
         }
    }

}