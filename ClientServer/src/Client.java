
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
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
import java.nio.ByteBuffer;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import uff.br.infouffdtn.db.Content;
import uff.br.infouffdtn.db.FileManager;
import uff.br.infouffdtn.dtn.DtnLog;
import uff.br.infouffdtn.server.CommFile;

/**
 * A Simple Socket client that connects to our socket server
 * @author faheem
 *
 */
public class Client {

    private String hostname;
    private int port;
    Socket socketClient;

    public Client(String hostname, int port){
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
      	String logList = "";
      	try
      	{
      		InputStream is = socketClient.getInputStream();
      		BufferedInputStream buffer = new BufferedInputStream( is );
      		ObjectInput input = new ObjectInputStream ( buffer );	      
    	    logList = (String) input.readObject();
    	    System.out.print(logList);
    	       
      	}
      	catch(Exception e)
      	{
      		Exception x = e;
      	}  	 

  	  

      }
    

    public void sendDeleteMessage() 
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
     	   output.writeUTF("DeleteAll");
      	   
      	   output.flush();
      	   output.close();

    	}
    	catch(Exception e)
    	{
    		
    	}
    }
    public void sendStopTimer() 
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
     	   output.writeUTF("StopTimer");
      	   
      	   output.flush();
      	   output.close();

    	}
    	catch(Exception e)
    	{
    		
    	}
    }
    public void sendImage(String imagePath,String name, int counter) 
    {
    	OutputStream os = null;
	  	BufferedOutputStream buffer = null;   	   
	  	ObjectOutput output = null;
    	try
    	{
    		 //Send the message to the server   	  	
    	   byte [] bytes = prepareBytes(imagePath,name,counter);
      	   os = socketClient.getOutputStream();
      	   buffer = new BufferedOutputStream( os );
      	   output = new ObjectOutputStream ( buffer ); 
     	  // output.writeUTF("Log");
      	   output.writeUTF("Write");
      	   output.writeObject(bytes);
      	   
      	   output.flush();
      	   output.close();

    	}
    	catch(Exception e)
    	{
    		
    	}
    }
    public void fetchLog() 
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
     	  // output.writeUTF("Log");
      	   output.writeUTF("GetLog");
      	   
      	   output.flush();
      	  // output.close();

    	}
    	catch(Exception e)
    	{
    		
    	}
    }
    public byte[] prepareBytes(String imagePath,String name, int counter) 
    {
	

		byte[] intBytes = null;
		byte[] bmBytes = null;
		byte[] strBytes = null;
		byte[] strSizeBytes = null;
		try 
		{
		  //CommFile comm = new CommFile(c.getName(),c.getDate());
		  //out = new ObjectOutputStream(bos);   
		  //out.writeObject(comm);
	      strBytes = name.getBytes();
	      strSizeBytes = ByteBuffer.allocate(4).putInt(strBytes.length).array();
		  intBytes = ByteBuffer.allocate(4).putInt(counter).array();
		  bmBytes = getImageBytes(imagePath);

		  byte[] retBytes = new byte[strSizeBytes.length + intBytes.length + bmBytes.length + strBytes.length ];
		  System.arraycopy(strSizeBytes, 0, retBytes, 0, strSizeBytes.length);
		  System.arraycopy(intBytes, 0, retBytes, 4, intBytes.length);
		  System.arraycopy(strBytes, 0, retBytes, 8, strBytes.length);
		  System.arraycopy(bmBytes, 0, retBytes, strBytes.length + 8, bmBytes.length);
		  return retBytes;
		
		}
		catch(Exception e)
		{
			
		}
		
		return null;
    }
    public byte[] prepareStartTimerBytes(int timeRefresh,String name, int counter) 
    {
	

		byte[] trBytes = null;
		byte[] cntBytes = null;
		byte[] strBytes = null;

		try 
		{
		  //CommFile comm = new CommFile(c.getName(),c.getDate());
		  //out = new ObjectOutputStream(bos);   
		  //out.writeObject(comm);
	      strBytes = name.getBytes();
	 //     strSizeBytes = ByteBuffer.allocate(4).putInt(strBytes.length).array();
	     
		  trBytes = ByteBuffer.allocate(4).putInt(timeRefresh).array();
		  cntBytes = ByteBuffer.allocate(4).putInt(counter).array();
		  byte[] retBytes = new byte[ trBytes.length + cntBytes.length + strBytes.length ];
		  System.arraycopy(trBytes, 0, retBytes, 0, trBytes.length);
		  System.arraycopy(cntBytes, 0, retBytes, 4, cntBytes.length);
		  System.arraycopy(strBytes, 0, retBytes, 8, strBytes.length);
		  return retBytes;
		
		}
		catch(Exception e)
		{
			
		}
		
		return null;
    }

    
    public byte[] getImageBytes(String filepath)
	{
		byte[] imageInByte = null;
		try
	 	{			 
			BufferedImage originalImage =ImageIO.read(new File(filepath));		 
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
    public void sendStartTimer(int timeRefresh,String name,int counter)
    {
    	OutputStream os = null;
	  	BufferedOutputStream buffer = null;   	   
	  	ObjectOutput output = null;
    	try
    	{
    		 //Send the message to the server   	  	
    	   byte [] bytes = prepareStartTimerBytes(timeRefresh, name, counter);
      	   os = socketClient.getOutputStream();
      	   buffer = new BufferedOutputStream( os );
      	   output = new ObjectOutputStream ( buffer ); 
     	  // output.writeUTF("Log");
      	   output.writeUTF("StartTimer");
      	   output.writeObject(bytes);
      	   
      	   output.flush();
      	   output.close();

    	}
    	catch(Exception e)
    	{
    		
    	}
    }

    public static void main(String arg[]) throws UnknownHostException, IOException{
    	//startTestTimers();
    	stopTimers();
    	deleteServerFiles();
    	//getLog();
    	
    	
    	
    	
    	
        //Creating a SocketClient object
      //  Client client = new Client ("rolim.no-ip.org",9990);
       // Client client = new Client ("rolim.no-ip.org",9990);
    	//Client client = new Client ("177.133.135.105",9990);
//        try {
            //trying to establish connection to the server
          //  client.connect();
                      
          //  client.fetchLog();
           // client.readResponse();
         //   client.sendStartTimer(10, "teste1!", 1);
          //   client.sendStartTimer(20, "teste2!", 2);
          //   client.sendStartTimer(30, "teste3!", 3);
//           client.sendStartTimer(60*10, "Teste10min1cont!", 1);
//           client.sendStartTimer(60*10, "Teste10min2cont!", 2);
//           client.sendStartTimer(60*10, "Teste10min3cont!", 3);
//           client.sendStartTimer(60*10, "Teste10min4cont!", 4);
//           client.sendStartTimer(60*30, "Teste30min1cont!", 1);
//           client.sendStartTimer(60*30, "Teste30min2cont!", 2);
//           client.sendStartTimer(60*30, "Teste30min3cont!", 3);
//           client.sendStartTimer(60*30, "Teste30min4cont!", 4);
//           client.sendStartTimer(60*60, "Teste60min1cont!", 1);
//           client.sendStartTimer(60*60, "Teste60min2cont!", 2);
//           client.sendStartTimer(60*60, "Teste60min3cont!", 3);
//           client.sendStartTimer(60*60, "Teste60min4cont!", 4);
          // client.sendStopTimer();
         //  client.sendDeleteMessage();
           // client.sendImage("C:\\Users\\Guilherme\\Pictures\\server-999px.png", "Computador", 3);
           // client.sendImage("C:\\Users\\Rolim\\Pictures\\derp-cat.jpg", "Arquivo Teste!", 1);

     
            //if successful, read response from server
           // client.readResponse();

//        } catch (UnknownHostException e) {
//            System.err.println("Host unknown. Cannot establish connection");
//        } catch (IOException e) {
//            System.err.println("Cannot establish connection. Server may not be up."+e.getMessage());
//        }
    }
    public static void startTestTimers() throws UnknownHostException, IOException
    {
    	 Client client = new Client ("rolim.no-ip.org",9990);
    	  client.connect();
	      client.sendStartTimer(60*10, "Teste10min1cont!", 1);
	      client.connect();
	      client.sendStartTimer(60*10, "Teste10min2cont!", 2);
	      client.connect();
	      client.sendStartTimer(60*10, "Teste10min3cont!", 3);
	      client.connect();
	      client.sendStartTimer(60*10, "Teste10min4cont!", 4);
	      client.connect();
	      client.sendStartTimer(60*30, "Teste30min1cont!", 1);
	      client.connect();
	      client.sendStartTimer(60*30, "Teste30min2cont!", 2);
	      client.connect();
	      client.sendStartTimer(60*30, "Teste30min3cont!", 3);
	      client.connect();
	      client.sendStartTimer(60*30, "Teste30min4cont!", 4);
	      client.connect();
	      client.sendStartTimer(60*60, "Teste60min1cont!", 1);
	      client.connect();
	      client.sendStartTimer(60*60, "Teste60min2cont!", 2);
	      client.connect();
	      client.sendStartTimer(60*60, "Teste60min3cont!", 3);
	      client.connect();
	      client.sendStartTimer(60*60, "Teste60min4cont!", 4);
    	
    }
    public static void deleteServerFiles() throws UnknownHostException, IOException
    {
    	Client client = new Client ("rolim.no-ip.org",9990);
    	client.connect();
    	client.sendDeleteMessage();
    }
    public static void getLog() throws UnknownHostException, IOException
    {
    	Client client = new Client ("rolim.no-ip.org",9990);
    	client.connect();
    	client.fetchLog();
        client.readResponse();
    }
    public static void stopTimers() throws UnknownHostException, IOException
    {
    	Client client = new Client ("rolim.no-ip.org",9990);
    	client.connect();
    	client.sendStopTimer();
    }
    
    
}
