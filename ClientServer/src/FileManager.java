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





public class FileManager 
{
	private static ArrayList<String> fileNames = new ArrayList<String>();
	private static ArrayList<Integer> fileCounters = new ArrayList<Integer>();
	
	ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
	private static String pathname = "\\\\GAIVOTAS\\UserFolders$\\grolim\\Desktop\\";
	public static void writeFile(Content content, int counter) 
	{
		
		loadListFile();
		loadCounterListFile();
		String fileName = writeValidation(content.name,1);					
		try
		{
			File file = new File(pathname+fileName);
			FileOutputStream fOut = new FileOutputStream(file);
			BufferedOutputStream buffer = new BufferedOutputStream (fOut);
			ObjectOutput output = new ObjectOutputStream ( buffer);
			try
			{													
						output.writeObject(content);	
						fileNames.add(fileName);
						fileCounters.add(counter);
						saveListFile();
						saveCounterListFile();
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
	public static Content readFile(String fileName) 
	{
		loadListFile();
		loadCounterListFile();
		Content content = null;
		try
		{
		      //use buffering
			  File file = new File(pathname+fileName);
		      FileInputStream fIn = new FileInputStream(file);
		      BufferedInputStream buffer = new BufferedInputStream(fIn );
		      ObjectInput input = new ObjectInputStream ( buffer );
		      try
		      {
		    	if(decrementAndCheckIfFetchable(fileName))
		    		content = (Content)input.readObject();
		        
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
		return content;
		
		
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
			File file = new File(pathname+fileNames.get(i));
			file.delete();
		}
		for(int i = 0; i < fileCounters.size() ; i++)
		{
			File file2 = new File(pathname+fileCounters.get(i));
			file2.delete();
		}
		
		fileNames.clear();
		fileCounters.clear();
		saveListFile();
		saveCounterListFile();

	}
	public static String writeValidation(String filename, int num)
	{
		loadListFile();
		for(int i = 0;i<fileNames.size();i++)
		{
			if(filename.equals(fileNames.get(i)))		
			{
				if(num ==1)
					return writeValidation(filename+"("+num+")",++num);
				else
				{
					try
					{
					String file = filename.substring(0, filename.indexOf("("));
					return writeValidation(file+"("+num+")",++num);
					}
					catch(Exception e)
					{
						System.out.print(e);
					}
					
				}
			}
		}
		return filename;
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
		ArrayList<String> list = new ArrayList<String>();
		try
		{
		      //use buffering
		      FileInputStream file = new FileInputStream(new File(pathname+"SpecialList"));
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
	public static void saveCounterListFile()
	{
		try
		{
			FileOutputStream fOut = new FileOutputStream(new File(pathname + "CounterList"));
			BufferedOutputStream buffer = new BufferedOutputStream (fOut);
			ObjectOutput output = new ObjectOutputStream ( buffer);
			try
			{													
						output.writeObject(fileCounters);	
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
	public static void loadCounterListFile()
	{
		ArrayList<Integer> list = new ArrayList<Integer>();
		try
		{
		      //use buffering
		      FileInputStream file = new FileInputStream(new File(pathname+"CounterList"));
		      BufferedInputStream buffer = new BufferedInputStream( file );
		      ObjectInput input = new ObjectInputStream ( buffer );
		      try
		      {
		        list = (ArrayList<Integer>) input.readObject();
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
		fileCounters = list;
		
	}
	public static boolean decrementAndCheckIfFetchable(String filename)
	{
		for(int i = 0; i< fileNames.size(); i++)
		{
			if(fileNames.get(i).equals(filename))
			{
				Integer counter = fileCounters.get(i);
				if(counter > 0)
				{
					fileCounters.set(i, counter.intValue()-1);
					saveCounterListFile();
					return true;
				}
				else
				{
					return false;
				}
											
			}		
		}
		return false;
	}





}
