import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.imageio.ImageIO;


import uff.br.infouffdtn.db.Content;
import uff.br.infouffdtn.server.CommFile;






public class FileManager 
{
	private static ArrayList<ServerFile> fileNames = new ArrayList<ServerFile>();
	private static ArrayList<String> log = new ArrayList<String>();
	//private static ArrayList<Integer> fileCounters = new ArrayList<Integer>();
	
	ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	private static String pathname = "";
	public static void writeFile(String name,BufferedImage img, int counter) 
	{
		
		loadListFile();
		//loadCounterListFile();
		
		if(!tryToOverwrite(name,img,counter))
		{
			String fileName = getAvaiableFilepath();				

			//File file = new File(pathname+fileName);
			//FileOutputStream fOut = new FileOutputStream(file);
			//BufferedOutputStream buffer = new BufferedOutputStream (fOut);
			//ObjectOutput output = new ObjectOutputStream ( buffer);
			try
			{													
						//output.writeObject(content);	
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				Date d1 = new Date();
				String data = dateFormat.format(d1);
				fileNames.add(new ServerFile(fileName,counter,img,data,name));
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
		
		

		
		
		
	}
	public static boolean tryToOverwrite(String name,BufferedImage img, int counter)
	{
		for(int i = 0; i < fileNames.size();i++)
		{
			if(fileNames.get(i).getName().equals(name))
			{
				fileNames.get(i).setCounter(counter);
				FileManager.writeImageToFile(img, fileNames.get(i).getFilepath());
				saveListFile();
				return true;
			}
		}
		return false;
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
		log.clear();

		saveListFile();
		saveLogFile();
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
	    catch(Exception e)
	    {
	     // e.printStackTrace();
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
	public synchronized static ArrayList<byte[]> getBytessToSend(ArrayList<CommFile> celContents)
	{
		loadListFile();
		ArrayList<byte[]> ret = new ArrayList<byte[]>();
		ArrayList<ServerFile> filesToSend = getFilesToConvert(celContents);
		for(int i = 0;i<filesToSend.size();i++)
		{
			if(filesToSend.get(i).getCounter() > 0)
			{
				ret.add(FileManager.prepareFileToSend(filesToSend.get(i)));
				filesToSend.get(i).setCounter(filesToSend.get(i).getCounter() - 1);
			}
		}
		saveListFile();
		return ret;
	}
	public static ArrayList<ServerFile> getFilesToConvert(ArrayList<CommFile> celContents)
	{
		
		ArrayList<ServerFile> ret = new ArrayList<ServerFile>();
		if(celContents.size() == 0)
		{
			return fileNames;
		}
		else
		{
			for(int i = 0; i < fileNames.size() ; i++)
			{
				for(int j = 0 ; j < celContents.size() ; j++)
				{
					String celDate= celContents.get(j).getDate();
					String svDate = fileNames.get(i).getDate();
					String celName =celContents.get(j).getName();
					String svName = fileNames.get(i).getName();
					if(!celDate.equals(svDate) && celName.equals(svName))
					{
						ret.add(fileNames.get(i));
					}
				}
			}
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
	public static byte[] prepareFileToSend(ServerFile c)
	{
		//byte[] 0 a 1023 vai ter o Content, o resto será a imagem
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		byte[] cBytes = new byte[1024];
		byte[] bmBytes = null;
		try 
		{
		  CommFile comm = new CommFile(c.getName(),c.getDate());
		  out = new ObjectOutputStream(bos);   
		  out.writeObject(comm);
		  cBytes = bos.toByteArray();
		  byte[] intBytes = ByteBuffer.allocate(4).putInt(cBytes.length).array();
		  int x = byteArrayToInt(intBytes);
		  bmBytes = c.getImageBytes();
		  byte[] retBytes = new byte[intBytes.length + cBytes.length + bmBytes.length];
		  System.arraycopy(intBytes, 0, retBytes, 0, intBytes.length);
		  System.arraycopy(cBytes, 0, retBytes, intBytes.length, cBytes.length);
		  System.arraycopy(bmBytes, 0, retBytes, intBytes.length + cBytes.length, bmBytes.length);
		  return retBytes;
		
		}
		catch(Exception e)
		{
			Exception x = e;
		}
		
		return null;
	}
	public static Content writeImageFromBytes(byte[] b)
	{
		byte[] tam = new byte[4];
		tam[0] = b[0];
		tam[1] = b[1];
		tam[2] = b[2];
		tam[3] = b[3];
		int stringSize = byteArrayToInt(tam);
				
		tam[0] = b[4];
		tam[1] = b[5];
		tam[2] = b[6];
		tam[3] = b[7];
		int counter = byteArrayToInt(tam);
		
		
		byte[] strBytes = new byte[stringSize];
		System.arraycopy(b, 8 , strBytes, 0, strBytes.length);
		byte[] bitMapBytes = new byte[b.length - stringSize - 8];
		System.arraycopy(b, stringSize + 8 , bitMapBytes, 0, b.length - stringSize - 8);
		
		try
		{
			String name = new String(strBytes, "Cp1252");
			BufferedImage bi = createImageFromBytes(bitMapBytes);
			writeFile(name, bi, counter);

		
		}
		catch(Exception e)
		{
			Exception x = e;
		}
		return null;
		
		
		
	}
	public static int byteArrayToInt(byte[] b) 
	{
	    return   b[3] & 0xFF |
	            (b[2] & 0xFF) << 8 |
	            (b[1] & 0xFF) << 16 |
	            (b[0] & 0xFF) << 24;
	}
	public static BufferedImage createImageFromBytes(byte[] imageData) {
	    ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
	    try {
	        return ImageIO.read(bais);
	    } catch (IOException e) {
	        throw new RuntimeException(e);
	    }
	}
	public static void saveLogFile()
	{
		try
		{
			FileOutputStream fOut = new FileOutputStream(new File(pathname + "LogList"));
			BufferedOutputStream buffer = new BufferedOutputStream (fOut);
			ObjectOutput output = new ObjectOutputStream ( buffer);
			try
			{													
						output.writeObject(log);	
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
	public static void loadLogFile()
	{
		ArrayList<String> list = new ArrayList<String>();
		try
		{
		      //use buffering
		      FileInputStream file = new FileInputStream(new File(pathname+"LogList"));
		      BufferedInputStream buffer = new BufferedInputStream( file );
		      ObjectInput input = new ObjectInputStream ( buffer );
		      try
		      {
		        list = (ArrayList<String>) input.readObject();
		      }
		      finally
		      {
		        input.close();
		      }
	    }
	    catch(Exception e)
	    {
	     // e.printStackTrace();
	    }

		log = list;		
	}
	public synchronized static void  saveLog(ArrayList<String> list)
	{
		loadLogFile();
		
		for(int i = 0 ; i< list.size(); i++)
		{
			log.add(list.get(i));
		}
		
		saveLogFile();
	}
	public synchronized static String loadLog()
	{
		loadLogFile();
		String ret = "";
		
		for(int i = 0 ; i<log.size();i++)
		{
			ret += log.get(i) + "\n";
		}
		
		return ret;
		
		
	}
	





}
