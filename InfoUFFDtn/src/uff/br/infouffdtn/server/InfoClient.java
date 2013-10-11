package uff.br.infouffdtn.server;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.commons.io.IOUtils;

import android.graphics.Bitmap;
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

    public void readResponse() throws IOException{
      //  String userInput;
      //  BufferedReader stdIn = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
        //InputStream socketInputStream = socketClient.getInputStream();
        //socketInputStream.

    	InputStream is = socketClient.getInputStream();
    	byte[] bytes = IOUtils.toByteArray(is);
    	Bitmap b = FileManager.getBitmapFromBytes(bytes);
        //  System.out.println("Response from server:");
      //  while ((userInput = stdIn.readLine()) != null) {
      //      System.out.println(userInput);
      //  }
    }

    public static void main(String arg[]){
        //Creating a SocketClient object
        InfoClient client = new InfoClient ("localhost",9990);
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
