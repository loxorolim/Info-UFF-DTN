package uff.br.infouffdtn.db;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import uff.br.infouffdtn.interfacepk.Header;
import uff.br.infouffdtn.interfacepk.Item;
import uff.br.infouffdtn.interfacepk.ListItem;
import de.tubs.ibr.dtn.util.Base64.OutputStream;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;


public class FileManager extends Activity
{
	// Lista que conter� o nome dos contents
	private static int listSize = 10;
	private static String regex = "teste";
	private static ArrayList<String[]> filesPaths = new ArrayList<String[]>();
	private static ArrayList<Content> contents = new ArrayList<Content>();
	private static final String REFRESH = "uff.br.infouffdtn.REFRESH";
	//private static String contentFilePath ="/data/data/br.uff.pse.dest/contents/";

	public static void writeContent(Content content, Context ctx) 
	{
		
		loadListFile(ctx);
		String fileName = writeValidation(content.getName(),ctx,1);	
		//String fileName = content.getName()+regex+content.getDate();
		
		try
		{
			if(dateComparison(content.getDate(),getMostRecentDate(content.getName(),ctx)))
			{
				
				FileOutputStream fOut = ctx.openFileOutput(fileName, Context.MODE_PRIVATE);
				BufferedOutputStream buffer = new BufferedOutputStream (fOut);
				ObjectOutput output = new ObjectOutputStream ( buffer);
				try
				{		
					if(filesPaths.size() == listSize)
					{
						int pos = getLeastRecentDatePos(ctx);
						ctx.deleteFile(filesPaths.get(pos)[0]);
						filesPaths.remove(pos);
						
					}
							output.writeObject(content);	
							String[] info = new String[3];
							info[0] = fileName;
							info[1] = content.getName();
							info[2] = content.getDate();
							filesPaths.add(info);
							saveListFile(ctx);
							Intent i = new Intent(REFRESH);
							ctx.sendBroadcast(i);
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
		}			
		catch(IOException ex)
		{
		    ex.printStackTrace();
		}
		
		
		
	}
	public static boolean canWrite(Content c)
	{
		
		
		
		return false;
	}
	public static String getMostRecentDate(String type,Context ctx)
	{
		loadListFile(ctx);
		if(filesPaths.isEmpty())
		{
			return null;
		}
		else
		{
			String date = null;
			for (int i = 0; i < filesPaths.size(); i++)
			{
				if(filesPaths.get(i)[1].equals(type))
				{
					if(date == null)
					{
						date = filesPaths.get(i)[2];
					}
					else
					{										
						if (dateComparison(filesPaths.get(i)[2],date))
						{
							date = filesPaths.get(i)[2];
						}			
					}
				}
			}
			return date;
		}
	
	}
	public static Content getMostRecentContent(String type,Context ctx)
	{
		loadListFile(ctx);
		if(filesPaths.isEmpty())
		{
			return null;
		}
		else
		{
			Content ct = null;
			for (int i = 0; i < filesPaths.size(); i++)
			{
				if(filesPaths.get(i)[1].equals(type))
				{
					if(ct == null)
					{
						ct = readContent(filesPaths.get(i)[0],ctx);
					}
					else
					{										
						if (dateComparison(filesPaths.get(i)[2],ct.getDate()))
						{							
							ct = readContent(filesPaths.get(i)[0],ctx);
						}			
					}
				}
			}
			return ct;
		}
	
	}
	public static int getLeastRecentDatePos(Context ctx)
	{
		loadListFile(ctx);
		int ret = -1;
		if(filesPaths.isEmpty())
		{
			return ret;
		}
		else
		{
			String date = null;
			for (int i = 0; i < filesPaths.size(); i++)
			{

					if(date == null)
					{
						date = filesPaths.get(i)[2];
						ret = i;
					}
					else
					{										
						if (dateComparison(date,filesPaths.get(i)[2]))
						{
							date = filesPaths.get(i)[2];
							ret = i;
						}			
					}
				
			}
			return ret;
		}
	
	}
	private static boolean dateComparison(String date1, String date2)
	{
		if (date1 == null)
			return false;
		else
			if (date2 == null)
				return true;

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date d1 = new Date();
		Date d2 = new Date();
		try
		{
			d1 = dateFormat.parse(date1);
			d2 = dateFormat.parse(date2);
			
			if (d1.after(d2))
			{
				return true;
			}
			return false;
		}
		catch (Exception e)
		{

		}

		return false;

	}
	public static Content readContent(String fileName, Context ctx) 
	{
		loadListFile(ctx);
		Content content = null;
		try
		{
		      //use buffering
		      FileInputStream file = ctx.openFileInput(fileName);
		      BufferedInputStream buffer = new BufferedInputStream( file );
		      ObjectInput input = new ObjectInputStream ( buffer );
		      try
		      {
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
	public static ArrayList<Item> readAllFilesNames(Context ctx) throws ParseException
	{		
		loadListFile(ctx);

		ArrayList<String> types = new ArrayList<String>();
		ArrayList<String[]> list = sortByDate(ctx);
		for(int i = 0;i<list.size();i++)
		{

			String type = list.get(i)[1];	
			if(!checkIfTypeExists(type,types))
			{
				types.add(type);
			}			
		}
		ArrayList<Item> ret = new ArrayList<Item>();
		for(int i = 0; i<types.size();i++)
		{
			ret.add(new Header(types.get(i)));
			for(int j = 0; j < list.size();j++)
			{
				if(list.get(j)[1].equals(types.get(i)))
				{
					ret.add(new ListItem(list.get(j)[0],list.get(j)[1],list.get(j)[2]));
				}
				
			}
		}
	/*	ArrayList<Item> ret = new ArrayList<Item>();
		for(int i = 0; i< filesPaths.size();i++)
		{
			
			ret.add(new ListItem(filesPaths.get(i)[1],filesPaths.get(i)[2]));
			
		}
*/


		return ret;
	}
	
	private static boolean checkIfTypeExists(String type,ArrayList<String> list)
	{
		for(int i = 0; i< list.size();i++)
		{
			if(list.get(i).equals(type))
			{
				return true;
			}
		}	
		return false;
		
	}
	public static void deleteContent(String fileName,Context ctx)
	{
		loadListFile(ctx);
		int pos = getContentListPosition(fileName);
		if(pos != -1)
		{
			ctx.deleteFile(fileName);
		//	filesPaths.remove(fileName);
			filesPaths.remove(pos);
		}
		saveListFile(ctx);
	}
	public static int getContentListPosition(String filename)
	{
		for(int i = 0 ; i < filesPaths.size();i++)
		{
			if(filesPaths.get(i)[0].equals(filename))
				return i;
		}
		return -1;
	}
	public static void deleteAllFiles(Context ctx)
	{
		loadListFile(ctx);
		for(int i = 0; i < filesPaths.size() ; i++)
		{
			ctx.deleteFile(filesPaths.get(i)[0]);
		}
		filesPaths.clear();
		saveListFile(ctx);
	}
	public static String writeValidation(String filename,Context ctx, int num)
	{
		loadListFile(ctx);
		for(int i = 0;i<filesPaths.size();i++)
		{
			if(filename.equals(filesPaths.get(i)[0]))		
			{
				if(num ==1)
					return writeValidation(filename+"("+num+")",ctx,++num);
				else
				{
					try
					{
					String file = filename.substring(0, filename.indexOf("("));
					return writeValidation(file+"("+num+")",ctx,++num);
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

	public static void saveListFile(Context ctx)
	{
		try
		{
			FileOutputStream fOut = ctx.openFileOutput("SpecialListArchive", Context.MODE_PRIVATE);
			BufferedOutputStream buffer = new BufferedOutputStream (fOut);
			ObjectOutput output = new ObjectOutputStream ( buffer);
			try
			{													
						output.writeObject(filesPaths);	
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
	public static void loadListFile(Context ctx)
	{
		ArrayList<String[]> list = new ArrayList<String[]>();
		try
		{
		      //use buffering
		      FileInputStream file = ctx.openFileInput("SpecialListArchive");
		      BufferedInputStream buffer = new BufferedInputStream( file );
		      ObjectInput input = new ObjectInputStream ( buffer );
		      try
		      {
		        list = (ArrayList<String[]>) input.readObject();
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
		filesPaths = list;
		
	}	

	private static ArrayList<String[]> sortByDate(Context ctx) throws ParseException
	{
		ArrayList<String[]> ret = new ArrayList<String[]>();
		ArrayList<String[]> list = (ArrayList<String[]>)filesPaths.clone();
		int tam = list.size();
		String[] aux = null;
		for(int i = 0; i< tam;i++)
		{
			
			for(int j = 0 ;j<list.size();j++)
			{
				
					if(aux == null)
					{
						aux = list.get(j);
					}
					else
					{
						if(dateComparison(list.get(j)[2],aux[2]))
						{
							aux = list.get(j);
						}
					}
			}
			list.remove(aux);
			ret.add(aux);
			aux = null;
		}
		return ret;
		
		
		
	}
	
	//TESTAAAAAAAAAAAAR
	public static ArrayList<Content> getFilesToSend(Context ctx)
	{
		loadListFile(ctx);
		ArrayList<Content> ret = new ArrayList<Content>();
		ArrayList<String> types = new ArrayList<String>();		
		for(int i = 0;i<filesPaths.size();i++)
		{

			String type = filesPaths.get(i)[1];	
			if(!checkIfTypeExists(type,types))
			{
				types.add(type);
			}			
		}
		for(int i = 0; i < types.size();i++)
		{
		
			Content c = getMostRecentContent(types.get(i),ctx);
			if(c!=null)
			{
				ret.add(c);
			}
		}
		
		return ret;
		
	}

	

	


}