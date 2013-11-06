
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

    public static void main(String arg[]){
        //Creating a SocketClient object
      //  Client client = new Client ("rolim.no-ip.org",9990);
        Client client = new Client ("rolim.no-ip.org",9990);
    	//Client client = new Client ("177.133.135.105",9990);
        try {
            //trying to establish connection to the server
            client.connect();
                      
           // client.fetchLog();
           // client.readResponse();
           // client.sendDeleteMessage();
           // client.sendImage("C:\\Users\\Guilherme\\Pictures\\server-999px.png", "Computador", 3);
            client.sendImage("\\\\GAIVOTAS\\UserFolders$\\grolim\\Pictures\\Speaker_Icon_rtl.png", "Imagem Audio", 10);

     
            //if successful, read response from server
           // client.readResponse();

        } catch (UnknownHostException e) {
            System.err.println("Host unknown. Cannot establish connection");
        } catch (IOException e) {
            System.err.println("Cannot establish connection. Server may not be up."+e.getMessage());
        }
    }
}
