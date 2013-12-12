import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;




public class FileGeneratorTest 
{

	public static ArrayList<ScheduledThreadPoolExecutor> execs = new ArrayList<ScheduledThreadPoolExecutor>();
	

	public  void setTimer(String name,long refreshTime,int counter)
	{
		ScheduledThreadPoolExecutor exec ;
		exec = new ScheduledThreadPoolExecutor(1);
		exec.scheduleAtFixedRate(new Task(counter,1,name), 0, refreshTime, TimeUnit.SECONDS); // execute every 60 seconds
		execs.add(exec);

	}
	public void deleteTimer()
	{
		for(int i = 0 ; i< execs.size();i++)
			execs.get(i).shutdown();
	}

	class Task implements Runnable
	{
		public int counter;
		public int num;
		public String name;
		public Task(int counter,int num,String filename)
		{
			this.counter = counter;
			this.num = num;
			this.name = filename;
		}
		@Override
		public void run() {
			
	
			BufferedImage originalImage;
			try {
				originalImage = ImageIO.read(new File("src/teste.jpg"));
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				ImageIO.write( originalImage, "png", baos );
				FileManager.writeFile(name+num,originalImage, counter);
				num++;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		 

			
		}
		
	}
}
