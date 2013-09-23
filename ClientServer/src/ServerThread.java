import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;


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
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
        Content c6 = FileManager.readFile("nome3");
        try
        {
        	writer.write(c6.data);
        }
        catch(Exception e)
        {
        	writer.write("Cabou!");
        }
        
        writer.flush();
        writer.close();
    }

}
