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
	// Lista que conterï¿½ o nome dos contents
	private static int listSize = 30;
	private static ArrayList<String> filesPaths = new ArrayList<String>();
	//private static String contentFilePath ="/data/data/br.uff.pse.dest/contents/";

	public static void writeContent(Content content, Context ctx) 
	{
		
		loadListFile(ctx);
		String fileName = writeValidation(content.getName(),ctx,1);					
		try
		{
			FileOutputStream fOut = ctx.openFileOutput(fileName, Context.MODE_PRIVATE);
			BufferedOutputStream buffer = new BufferedOutputStream (fOut);
			ObjectOutput output = new ObjectOutputStream ( buffer);
			try
			{													
						output.writeObject(content);	
						filesPaths.add(fileName);
						saveListFile(ctx);
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
		ArrayList<Item> items = new ArrayList<Item>();
//		ArrayList<Item> capacetes = new ArrayList<Item>();
//		ArrayList<Item> ombreiras = new ArrayList<Item>();
//		ArrayList<Item> terrenos = new ArrayList<Item>();
//		capacetes.add(new Header("Capacetes"));
//		ombreiras.add(new Header("Ombreiras"));
//		terrenos.add(new Header("Terrenos"));
		//items.add(new Header("Contents"));
		items = sortByDate(ctx);
		ArrayList<ArrayList<Item>> lists = new ArrayList<ArrayList<Item>>();
		for(int i = 0;i<items.size();i++)
		{
			
		}
		

	/*	items.add(new Header("Terrain Content"));
        items.add(new ListItem("Default", "Terrain"));
        items.add(new ListItem("Zézinho", "Terreno de fogo"));
        items.add(new ListItem("Jurema", "Terreno de gelo"));

		items.add(new Header("Enemy Content"));
        items.add(new ListItem("Default", "Enemy"));
        items.add(new ListItem("Zézinho", "Inimigo de fogo"));
        items.add(new ListItem("Jurema", "Inimigo de gelo"));

		items.add(new Header("Hero Content"));
        items.add(new ListItem("Default", "Hero"));
        items.add(new ListItem("Cláudio", "Herói boladão"));
        items.add(new ListItem("Jeremias", "Obi Juan"));
        items.add(new ListItem("Francis", "Megaman"));

		items.add(new Header("Background Content"));
        items.add(new ListItem("Default", "Background"));
        items.add(new ListItem("Emanuelle", "Chaos"));
        items.add(new ListItem("Maximillian", "Armageddon"));
    */    


		return items;
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
			if(filesPaths.get(i).equals(filename))
				return i;
		}
		return -1;
	}
	public static void deleteAllFiles(Context ctx)
	{
		loadListFile(ctx);
		for(int i = 0; i < filesPaths.size() ; i++)
		{
			ctx.deleteFile(filesPaths.get(i));
		}
		filesPaths.clear();
		saveListFile(ctx);
	}
	public static String writeValidation(String filename,Context ctx, int num)
	{
		loadListFile(ctx);
		for(int i = 0;i<filesPaths.size();i++)
		{
			if(filename.equals(filesPaths.get(i)))		
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
		ArrayList<String> list = new ArrayList<String>();
		try
		{
		      //use buffering
		      FileInputStream file = ctx.openFileInput("SpecialListArchive");
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
		filesPaths = list;
		
	}	

	private static ArrayList<Item> sortByDate(Context ctx) throws ParseException
	{
		ArrayList<String> ret = (ArrayList<String>) filesPaths.clone();
		ArrayList<Item> sortedItems = new ArrayList<Item>();
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date daux = null;
		Content caux = null;
		int paux = -1;
		int firstSize = ret.size();
		// d1 = dateFormat.parse(date1);
		for (int i = 0; i < firstSize; i++)
		{
			for (int j = 0; j < ret.size(); j++)
			{
				if(daux == null)
				{
					Content c1 = FileManager.readContent(ret.get(j), ctx);
					Date d = new Date();
					d = dateFormat.parse(c1.getDate());
					daux = d;
					caux = c1;
					paux = j;
				}
				else
				{
					Content c1 = FileManager.readContent(ret.get(j), ctx);
					Date d = new Date();
					d = dateFormat.parse(c1.getDate());
					if(d.after(daux))
					{
						daux = d;		
						caux = c1;
						paux = j;
					}
				}				
			}
			// sortedItems.add(new ListItem("Maximillian", "Armageddon"));
			 daux = null;
			 sortedItems.add(new ListItem(caux.getName(),caux.getDate()));
			 ret.remove(paux);			
		}

		return sortedItems;
	}

	

	


}