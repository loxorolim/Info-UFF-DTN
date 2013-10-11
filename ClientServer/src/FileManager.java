import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;





public class FileManager 
{
	private static ArrayList<ServerFile> fileNames = new ArrayList<ServerFile>();
	//private static ArrayList<Integer> fileCounters = new ArrayList<Integer>();
	
	ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	private static String pathname = "\\\\GAIVOTAS\\UserFolders$\\grolim\\Desktop\\";
	public static void writeFile(BufferedImage img, int counter) 
	{
		
		loadListFile();
		//loadCounterListFile();
		String fileName = getAvaiableFilepath();				

			//File file = new File(pathname+fileName);
			//FileOutputStream fOut = new FileOutputStream(file);
			//BufferedOutputStream buffer = new BufferedOutputStream (fOut);
			//ObjectOutput output = new ObjectOutputStream ( buffer);
			try
			{													
						//output.writeObject(content);	
						fileNames.add(new ServerFile(fileName,counter,img));
						saveListFile();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
			}
					

		
		
		
	}

	public static void deleteFile(String fileName)
	{
		loadListFile();
		int pos = getFileListPosition(fileName);
		if(pos != -1)
		{
			File file = new File(pathname+fileName);
			file.delete();
		//	fileNames.remove(fileName);
			fileNames.remove(pos);
		}
		saveListFile();
	}
	public static int getFileListPosition(String filename)
	{
		for(int i = 0 ; i < fileNames.size();i++)
		{
			if(fileNames.get(i).equals(filename))
				return i;
		}
		return -1;
	}
	public static void deleteAllFiles()
	{
		loadListFile();
		for(int i = 0; i < fileNames.size() ; i++)
		{
			File file = new File(fileNames.get(i).getFilepath());
			file.delete();
		}

		
		fileNames.clear();

		saveListFile();

	}

	public static void saveListFile()
	{
		try
		{
			FileOutputStream fOut = new FileOutputStream(new File(pathname + "SpecialList"));
			BufferedOutputStream buffer = new BufferedOutputStream (fOut);
			ObjectOutput output = new ObjectOutputStream ( buffer);
			try
			{													
						output.writeObject(fileNames);	
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				output.close();
			}
		}			
		catch(IOException ex)
		{
		    ex.printStackTrace();
		}	
	}
	public static void loadListFile()
	{
		ArrayList<ServerFile> list = new ArrayList<ServerFile>();
		try
		{
		      //use buffering
		      FileInputStream file = new FileInputStream(new File(pathname+"SpecialList"));
		      BufferedInputStream buffer = new BufferedInputStream( file );
		      ObjectInput input = new ObjectInputStream ( buffer );
		      try
		      {
		        list = (ArrayList<ServerFile>) input.readObject();
		      }
		      finally
		      {
		        input.close();
		      }
	    }
	    catch(ClassNotFoundException ex)
	    {
	      ex.printStackTrace();
	    }
	    catch(IOException ex)
	    {
	     ex.printStackTrace();
	    }
		fileNames = list;
		
	}
	public static String writeValidation(String type,int num)
	{
		loadListFile();
		String fp = pathname;
		for(int i = 0; i < fileNames.size();i++)
		{
			if(fileNames.get(i).getFilepath().equals(fp+type))
			{
				if(num == 0 )
					return writeValidation(type+"("+(num+1)+")",++num);
				else
				{
					String newType = type.substring(0, type.indexOf("("));
					return writeValidation(newType+"("+(num+1)+")",++num);
				}
			}
		}
		String x = fp+type;
		return fp+type;			
	}
	public static String getAvaiableFilepath()
	{
		
		return writeValidation("InfoUffServerFile",0);
	}
	public static void writeImageToFile(BufferedImage img, String dest)
	{
		 try
		 {
	            ImageIO.write(img, "png",new File(dest));
	 
	     } 
		 catch (IOException e) 
		 {
	        	e.printStackTrace();
	     }
	}
	public static BufferedImage readImageFromFile(String filepath)
	{
		 BufferedImage image = null;
		 	try 
		 	{			 
	            return ImageIO.read(new File(filepath));
	         
	        } 
		 	catch (IOException e) 
	        {
	        	e.printStackTrace();
	        }
			return image;
	}
	public static ArrayList<ServerFile> getFilesToSend()
	{
		ArrayList<ServerFile> ret = new ArrayList<ServerFile>();
		for(int i = 0 ; i < fileNames.size() ; i++)
		{
			ret.add(fileNames.get(i));
		}
		return ret;
	}
	public static ArrayList<BufferedImage> getImagesToSend()
	{
		loadListFile();
		ArrayList<BufferedImage> ret = new ArrayList<BufferedImage>();
		for(int i = 0 ; i < fileNames.size() ; i++)
		{
			try
			{
				if(fileNames.get(i).getCounter() > 0)
				{
					ret.add(FileManager.readImageFromFile(fileNames.get(i).getFilepath()));
					fileNames.get(i).setCounter(fileNames.get(i).getCounter() - 1);
					saveListFile();
				}
			}
			catch(Exception e)
			{
				
			}
		}
		return ret;
	}
	





}
