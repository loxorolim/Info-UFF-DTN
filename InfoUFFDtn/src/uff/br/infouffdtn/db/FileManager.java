package uff.br.infouffdtn.db;



import java.nio.ByteBuffer;
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
import java.nio.ByteOrder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import android.graphics.BitmapFactory;
import uff.br.infouffdtn.MainActivity;
import uff.br.infouffdtn.interfacepk.Header;
import uff.br.infouffdtn.interfacepk.Item;
import uff.br.infouffdtn.interfacepk.ListItem;
import uff.br.infouffdtn.server.CommFile;
import de.tubs.ibr.dtn.util.Base64.OutputStream;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;


public class FileManager extends Activity
{
	// Lista que conterï¿½ o nome dos contents
	private static int listSize = 10;

	private static ArrayList<Content> filesPaths = new ArrayList<Content>();

	public static final String REFRESH = "uff.br.infouffdtn.REFRESH";
	private static Context ctx = null;
	private static String appPath = "";


	public static synchronized void writeContent(Content content) 
	{
	
		String x = content.getDate();
		String y = content.getName();
		loadListFile();
		//String fileName = writeValidation(content.getName(),ctx,1);	
		//String fileName = content.getFilepath();
		

			if(dateComparison(content.getDate(),getMostRecentDate(content.getName())))
			{

				//FileOutputStream fOut = new FileOutputStream(fileName);
				//BufferedOutputStream buffer = new BufferedOutputStream (fOut);
				//ObjectOutput output = new ObjectOutputStream ( buffer);
				try
				{		
					if(filesPaths.size() == listSize)
					{
						int pos = getLeastRecentDatePos();
						File f = new File(filesPaths.get(pos).getFilepath());
						f.delete();
						filesPaths.remove(pos);
						
					}

							filesPaths.add(content);
							saveListFile();
							Intent i = new Intent(REFRESH);
							ctx.sendBroadcast(i);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				finally
				{
					//output.close();
				}
			}

		
		
	}
	public static boolean canWrite(Content c)
	{
		
		
		
		return false;
	}
	public static String getMostRecentDate(String type)
	{
		loadListFile();
		if(filesPaths.isEmpty())
		{
			return null;
		}
		else
		{
			String date = null;
			for (int i = 0; i < filesPaths.size(); i++)
			{
				if(filesPaths.get(i).getName().equals(type))
				{
					if(date == null)
					{
						date = filesPaths.get(i).getDate();
					}
					else
					{										
						if (dateComparison(filesPaths.get(i).getDate(),date))
						{
							date = filesPaths.get(i).getDate();
						}			
					}
				}
			}
			return date;
		}
	
	}
	public static Content getMostRecentContent(String type)
	{
		loadListFile();
		if(filesPaths.isEmpty())
		{
			return null;
		}
		else
		{
			Content ct = null;
			for (int i = 0; i < filesPaths.size(); i++)
			{
				if(filesPaths.get(i).getName().equals(type))
				{
					if(ct == null)
					{
						ct = filesPaths.get(i);
					}
					else
					{										
						if (dateComparison(filesPaths.get(i).getDate(),ct.getDate()))
						{							
							ct = filesPaths.get(i);
						}			
					}
				}
			}
			return ct;
		}
	
	}
	public static int getLeastRecentDatePos()
	{
		loadListFile();
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
						date = filesPaths.get(i).getDate();
						ret = i;
					}
					else
					{										
						if (dateComparison(date,filesPaths.get(i).getDate()))
						{
							date = filesPaths.get(i).getDate();
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

	public static ArrayList<Item> readAllFilesNames() throws ParseException
	{		
		loadListFile();

		ArrayList<String> types = new ArrayList<String>();
		ArrayList<Content> list = sortByDate();
		for(int i = 0;i<list.size();i++)
		{

			String type = list.get(i).getName();	
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
				if(list.get(j).getName().equals(types.get(i)))
				{
					ret.add(new ListItem(list.get(j)));
				}
				
			}
		}



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
	public static void deleteContent(String fileName)
	{
		loadListFile();
		int pos = getContentListPosition(fileName);
		if(pos != -1)
		{
			//ctx.deleteFile(fileName);
			File f = new File(filesPaths.get(pos).getFilepath());
			f.delete();
		//	filesPaths.remove(fileName);
			filesPaths.remove(pos);
		}
		saveListFile();
	}
	public static int getContentListPosition(String filename)
	{
		for(int i = 0 ; i < filesPaths.size();i++)
		{
			if(filesPaths.get(i).getFilepath().equals(filename))
				return i;
		}
		return -1;
	}
	public static void deleteAllFiles(Context ctx)
	{
		loadListFile();
		for(int i = 0; i < filesPaths.size() ; i++)
		{
			//ctx.deleteFile(filesPaths.get(i)[0]);
			File f = new File(filesPaths.get(i).getFilepath());
			f.delete();
		}
		filesPaths.clear();
		saveListFile();
	}


	public static void saveListFile()
	{
		try
		{
			
			//FileOutputStream fOut = ctx.openFileOutput("SpecialListArchive", Context.MODE_PRIVATE);
			FileOutputStream fOut = new FileOutputStream(new File(appPath+"/"+"SpecialListArchive"));
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
	public static void loadListFile()
	{
		ArrayList<Content> list = new ArrayList<Content>();
		try
		{
		      //use buffering
			
			
		   //   FileInputStream file = ctx.openFileInput("SpecialListArchive");
			  FileInputStream fOut = new FileInputStream(new File(appPath+"/"+"SpecialListArchive"));
		      BufferedInputStream buffer = new BufferedInputStream( fOut);
		      ObjectInput input = new ObjectInputStream ( buffer );
		      try
		      {
		        list = (ArrayList<Content>) input.readObject();
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

	private static ArrayList<Content> sortByDate() throws ParseException
	{
		ArrayList<Content> ret = new ArrayList<Content>();
		ArrayList<Content> list = (ArrayList<Content>)filesPaths.clone();
		int tam = list.size();
		Content aux = null;
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
						if(dateComparison(list.get(j).getDate(),aux.getDate()))
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
	

	public static ArrayList<byte[]> getContentBytesToSend(ArrayList<Content> list)
	{
		loadListFile();
		ArrayList<byte[]> ret = new ArrayList<byte[]>();
		for(int i = 0 ; i < list.size();i++)
		{
			ret.add(FileManager.prepareContentToSend(list.get(i)));
		}
		return ret;
		
	}
	public static byte[] prepareContentToSend(Content c)
	{
		//byte[] 0 a 1023 vai ter o Content, o resto será a imagem
		loadListFile();
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
		  bmBytes = c.getBitmapBytes();
		  byte[] retBytes = new byte[cBytes.length + bmBytes.length + intBytes.length];
		  System.arraycopy(intBytes, 0, retBytes, 0, intBytes.length);
		  System.arraycopy(cBytes, 0, retBytes, intBytes.length, cBytes.length);
		  System.arraycopy(bmBytes, 0, retBytes, intBytes.length + cBytes.length, bmBytes.length);
		  return retBytes;
		
		}
		catch(Exception e)
		{
			
		}
		
		return null;
	}
	public static byte[] getObjectBytes(Object obj)
	{
		byte[] ret = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		try
		{
			out = new ObjectOutputStream(bos);
			out.writeObject(obj);
			ret = bos.toByteArray();
		}
		catch(Exception e)
		{
			
		}
		return ret;
		
	}
	public static ArrayList<CommFile> getCommFileListFromBytes(byte[] bytes)
	{
		ArrayList<CommFile> ret = null;
		ByteArrayInputStream bos = new ByteArrayInputStream(bytes);
		ObjectInputStream ois;
		try
		{
			ois = new ObjectInputStream(bos);
			ret = (ArrayList<CommFile>) ois.readObject();			
		}
		catch(Exception e)
		{
			
		}
		return ret;		
	}
	public static ArrayList<byte[]> getContentByteListFromBytes(byte[] bytes)
	{
		ArrayList<byte[]> ret = null;
		ByteArrayInputStream bos = new ByteArrayInputStream(bytes);
		ObjectInputStream ois;
		try
		{
			ois = new ObjectInputStream(bos);
			ret = (ArrayList<byte[]>) ois.readObject();			
		}
		catch(Exception e)
		{
			
		}
		return ret;		
	}
	public static ArrayList<Content> getContentsToConvert(ArrayList<CommFile> celContents)
	{
		loadListFile();
		ArrayList<Content> ret = new ArrayList<Content>();
		if(celContents.size() == 0)
		{
			return filesPaths;
		}
		else
		{
			for(int i = 0; i < filesPaths.size() ; i++)
			{
				String thisCelDate = filesPaths.get(i).getDate();
				String thisCelName = filesPaths.get(i).getName();
				boolean found = false;
				for(int j = 0 ; j < celContents.size() ; j++)
				{
					String celDate= celContents.get(j).getDate();
					String celName =celContents.get(j).getName();
					if(celName.equals(thisCelName))
					{
						found = true;
						if(dateComparison(thisCelDate,celDate))
						{
							ret.add(filesPaths.get(i));
						}
					}
				}
				if(!found)
					ret.add(filesPaths.get(i));
				
			}
		}
		return ret;
		
	}
	
	

	public static Content getContentFromBytes(byte[] b, boolean fromWifi)
	{
		byte[] tam = new byte[4];
		tam[0] = b[0];
		tam[1] = b[1];
		tam[2] = b[2];
		tam[3] = b[3];
		int contentSize = byteArrayToInt(tam);
		byte[] contentBytes = new byte[contentSize];
		System.arraycopy(b, 4 , contentBytes, 0, contentBytes.length);
		byte[] bitMapBytes = new byte[b.length - contentSize - 4];
		System.arraycopy(b, contentSize + 4 , bitMapBytes, 0, b.length - contentSize - 4);
		
		try
		{
			ByteArrayInputStream bos = new ByteArrayInputStream(contentBytes);
			ObjectInputStream ois = new ObjectInputStream(bos);
			CommFile comm = (CommFile) ois.readObject();
			Bitmap bitmap = BitmapFactory.decodeByteArray(bitMapBytes , 0, bitMapBytes.length);
			String fp = FileManager.getAvaiableFilepath();
			//c.setFilepath(ctx.getFilesDir()+"/"+fp);
			//c.setBitmap(bitmap);
			Content c = new Content(comm.getName(),comm.getDate(),fromWifi,fp , bitmap);
			return c;
		
		}
		catch(Exception e)
		{
			Exception x = e;
		}
		return null;
		
		
		
	}
	public static Bitmap getBitmapFromBytes(byte[] b)
	{				
		try
		{
			Bitmap bitmap = BitmapFactory.decodeByteArray(b , 0, b.length);
			return bitmap;		
		}
		catch(Exception e)
		{
			
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
	public static Bitmap getBitmapFromFilepath(String filepath)
	{
		return BitmapFactory.decodeFile(filepath);
	}
	public static ArrayList<CommFile> getContentList()
	{
		loadListFile();
		ArrayList<CommFile> ret = new ArrayList<CommFile>();
		ArrayList<String> types = new ArrayList<String>();		
		for(int i = 0;i<filesPaths.size();i++)
		{

			String type = filesPaths.get(i).getName();	
			if(!checkIfTypeExists(type,types))
			{
				types.add(type);
			}			
		}
		for(int i = 0; i < types.size();i++)
		{
		
			Content c = getMostRecentContent(types.get(i));
			if(c!=null)
			{
				ret.add(new CommFile(c.getName(),c.getDate()));
			}
		}
		
		return ret;
		
	}
	public static synchronized String writeValidation(String type,int num)
	{
		try
		{
		loadListFile();
		String fp = appPath;
		for(int i = 0; i < filesPaths.size();i++)
		{
			if(filesPaths.get(i).getFilepath().equals(fp+"/"+type))
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
		return fp+"/"+type;	
		}
		catch(Exception e)
		{
			
		}
		return null;
	}
	public static synchronized String getAvaiableFilepath()
	{
		
		return writeValidation("InfoUffDtnFile",0);
	}
	public static void setAppPath(String  path)
	{

		appPath= path;
	}
	public static void setContext(Context c) {
		ctx = c;
		
	}



	

	


}