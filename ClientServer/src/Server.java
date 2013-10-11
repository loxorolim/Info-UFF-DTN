import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
* A simple socket server
* @author faheem
*
*/
public class Server {
    
    private ServerSocket serverSocket;
    private int port;
    
    public Server(int port) {
        this.port = port;
    }
    
    public void start() throws IOException {
        System.out.println("Starting the socket server at port:" + port);
        serverSocket = new ServerSocket(port);
        
        //Listen for clients. Block till one connects
        
        System.out.println("Waiting for clients...");
        FileManager.deleteAllFiles();
        BufferedImage img1 = null;
        try
        {
        	img1 = FileManager.readImageFromFile("\\\\GAIVOTAS\\UserFolders$\\grolim\\Desktop\\Chrysanthemum.jpg");
        }
        catch(Exception e)
        {
        	Exception x = e;
        }
        FileManager.writeFile(img1, 3);
        while(true)
        {
	        Socket client = serverSocket.accept();
	        Thread t = new Thread(new ServerThread(client));
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
            socketServer.start();
            

            
            } catch (IOException e) {
            e.printStackTrace();
        }
    }
}