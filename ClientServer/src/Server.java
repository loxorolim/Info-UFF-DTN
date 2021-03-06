import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JOptionPane;

/**
* A simple socket server
* @author faheem
*
*/
public class Server {
    
    private ServerSocket serverSocket;
    private int port;
    private FileGeneratorTest ft;
    
    public Server(int port) {
        this.port = port;
        ft = new FileGeneratorTest();
    }
    
    public void start() throws IOException {
        System.out.println("Starting the socket server at port:" + port);
        serverSocket = new ServerSocket(port);
        
        //Listen for clients. Block till one connects
        
        System.out.println("Waiting for clients...");
       
        
        FileManager.deleteAllFiles();
        
        JOptionPane.showMessageDialog(null, "SERVIDOR INICIADO!");

        
        while(true)
        {
	        Socket client = serverSocket.accept();
	        
	        Thread t = new Thread(new ServerThread(client,ft));
	        t.start();
	        //A client has connected to this server. Send welcome message
	        //sendWelcomeMessage(client);
	        
        }
    }

    
    /**
    * Creates a SocketServer object and starts the server.
    *
    * @param args
    */
    public static void main(String[] args) {
        // Setting a default port number.
        int portNumber = 9990;
        
        try {
            // initializing the Socket Server
  
  
            Server socketServer = new Server(portNumber);
           // FileGeneratorTest f = new FileGeneratorTest();
           // f.setTimer("Teste",10, 1);
            socketServer.start();
            
            

            
            } catch (IOException e) {
            e.printStackTrace();
        }
    }
}