package uff.br.infouffdtn.db;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


import android.app.Activity;
import android.content.Context;
import android.net.Uri;

public class ContentsDatabase
{
    private static boolean[] avaiableArchivesNumbers = new boolean[30];

	public static void writeTest(Content content,Context ctx) throws IOException
	{ 
		 loadAvaiableArchiveNumbers(ctx);
		 try 
		 {			   
		
				int archiveLocation = getAvaiableArchiveNumber();
				if(archiveLocation!=-1)
				{
		            FileOutputStream fOut = ctx.openFileOutput(String.valueOf(archiveLocation),Context.MODE_PRIVATE);	            	            
		            OutputStreamWriter osw = new OutputStreamWriter(fOut);
		            BufferedWriter bwriter = new BufferedWriter(osw);
		            bwriter.write(content.getName());
		            bwriter.newLine();	        
		            bwriter.write(content.getDate().toString());
		            bwriter.newLine();
		            String x = content.getPayload();
		            bwriter.write (content.getPayload());	            
		            avaiableArchivesNumbers[archiveLocation] = true;
		            saveAvaiableArchiveNumbers(ctx);
		            bwriter.flush();
		            bwriter.close();
		            
				}
	            

	     }
		 catch (Exception e) 
	     {
	            e.printStackTrace();
	     }
	}
	public static String readTest(String archive,Context ctx) throws FileNotFoundException
	{
	    String datax = "" ;
        try 
        {
        
        	loadAvaiableArchiveNumbers(ctx);
            FileInputStream fIn = ctx.openFileInput (archive);
            InputStreamReader isr = new InputStreamReader(fIn) ;
            BufferedReader buffreader = new BufferedReader(isr) ;

            String readString = buffreader.readLine() ;
            while (readString != null) 
            {
                datax = datax + readString ;
                readString = buffreader.readLine();
            }

            isr.close();
        } 
        catch (IOException ioe ) 
        {
            ioe.printStackTrace();
        }
        return datax ;
	}
	public static String[] readAllArchivesNames(Context ctx)
	{
	
		 loadAvaiableArchiveNumbers(ctx);
		 LinkedList<String> list = new LinkedList<String>();
	        try 
	        {
	        	
	        	for(int i = 0; i< avaiableArchivesNumbers.length;i++)
	        	{
	        		if(avaiableArchivesNumbers[i])
	        		{
		        		try
		        		{
		        			FileInputStream fIn = ctx.openFileInput (String.valueOf(i));
				            InputStreamReader isr = new InputStreamReader(fIn) ;
				            BufferedReader buffreader = new BufferedReader(isr) ;
			        		list.add(buffreader.readLine());	
			 	            isr.close();			 	           
		        		}
		        		catch(Exception e)
		        		{
		        			
		        		}	
	        		}
	        	}
	                        
	        } 
	        catch (Exception e ) 
	        {
	            
	        }
	    String[] ret = new String[list.size()];
	    for(int i = 0; i< list.size();i++)
	    {
	    	ret[i] = list.get(i);
	    }
		return ret;
		
	}
	public static void saveAvaiableArchiveNumbers(Context ctx)
	{

		String booleanValues = "";
		for(int i = 0;i<avaiableArchivesNumbers.length;i++)
		{
			booleanValues = booleanValues + String.valueOf(avaiableArchivesNumbers[i]) +";";
		}		
		try
		{
			FileOutputStream fOut = ctx.openFileOutput("AANArchive",Context.MODE_PRIVATE);	            	            
			OutputStreamWriter osw = new OutputStreamWriter(fOut);
			BufferedWriter bwriter = new BufferedWriter(osw);
			bwriter.write(booleanValues);
			bwriter.flush();
            bwriter.close();
		}
		catch(Exception e)
		{
			
		}
			
	}
	public static void loadAvaiableArchiveNumbers(Context ctx)
	{
		String booleanValues = "" ;
        try 
        {
            FileInputStream fIn = ctx.openFileInput ("AANArchive");
            InputStreamReader isr = new InputStreamReader(fIn) ;
            BufferedReader buffreader = new BufferedReader(isr) ;
            booleanValues = buffreader.readLine() ;
            isr.close();
            String[] booleanValuesSplit =  booleanValues.split(";");
            for(int i = 0;i<booleanValuesSplit.length;i++)
            {
            	if(booleanValuesSplit[i].equals("true"))
            	{
            		avaiableArchivesNumbers[i] = true;
            	}
            	else
            	{
            		avaiableArchivesNumbers[i] = false;
            	}
            }
        } 
        catch (IOException ioe ) 
        {
            ioe.printStackTrace();
        }
		
	}
	
	public static String readArchiveContentPayload(String ArchiveName, Context ctx)
	{
		    loadAvaiableArchiveNumbers(ctx);
			String ret = "";
	        try 
	        {	        	
	        	for(int i = 0; i< avaiableArchivesNumbers.length;i++)
	        	{	        		
	        		if(avaiableArchivesNumbers[i] && getArchiveName(i,ctx).equals(ArchiveName))
	        		{
	        			FileInputStream fIn = ctx.openFileInput (String.valueOf(i));
			            InputStreamReader isr = new InputStreamReader(fIn) ;
			            BufferedReader buffreader = new BufferedReader(isr) ;
		        		try
		        		{
		        			
			        		buffreader.readLine();	
			        		buffreader.readLine();	
			        		while(true)
			        		{
			        			ret += buffreader.readLine() +"\n";
			        		}
			 	            			 	           
		        		}
		        		catch(Exception e)
		        		{
		        			
		        		}	
		        		finally
		        		{
		        			isr.close();
		        		}
	        		}
	        	}
	        	
	                        
	        } 
	        catch (Exception e ) 
	        {
	            
	        }
		return ret;
		
	}
	public static String getArchiveName(int pos, Context ctx)
	{
		String ret = "";		
		try 
		{
			FileInputStream fIn;
			fIn = ctx.openFileInput (String.valueOf(pos));
			InputStreamReader isr = new InputStreamReader(fIn) ;
	        BufferedReader buffreader = new BufferedReader(isr) ;
			ret = buffreader.readLine();	
	        isr.close();
		} 
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		
		return ret;
	}
	public static void deleteAllArchives(Context ctx)
	{
		loadAvaiableArchiveNumbers(ctx);
		File dir = ctx.getFilesDir();
		for(int i = 0; i< avaiableArchivesNumbers.length;i++)
		{
			if(avaiableArchivesNumbers[i])
			{
				File file = new File(dir,String.valueOf(i));
				file.delete();
				avaiableArchivesNumbers[i] = false;
			}						
		}
		saveAvaiableArchiveNumbers(ctx);
		
	}
	public static int getAvaiableArchiveNumber()
	{
		
		for(int i = 0; i<30;i++)
		{
			if(!avaiableArchivesNumbers[i])
			{
				return i;
			}
		}
		return -1;
	}
}