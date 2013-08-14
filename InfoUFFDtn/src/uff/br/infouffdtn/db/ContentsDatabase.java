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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class ContentsDatabase extends Activity
{
    private static boolean[] avaiableArchivesNumbers = new boolean[30];
    private static final String REFRESH = "uff.br.infouffdtn.REFRESH";
	public static void writeTest(Content content,Context ctx) throws IOException
	{ 
		 loadAvaiableArchiveNumbers(ctx);
		 try 
		 {			   
		
				int archiveLocation = getAvaiableArchiveNumber(ctx);
				if(archiveLocation!=-1)
				{
		            FileOutputStream fOut = ctx.openFileOutput(String.valueOf(archiveLocation),Context.MODE_PRIVATE);	            	            
		            OutputStreamWriter osw = new OutputStreamWriter(fOut);
		            BufferedWriter bwriter = new BufferedWriter(osw);
		            bwriter.write(content.getName());
		            bwriter.newLine();	        
		            bwriter.write(content.getDate().toString());
		            bwriter.newLine();
		            bwriter.write (Boolean.toString(content.isCommSource()));
		            bwriter.newLine();
		            bwriter.write (content.getPayload());			            
		            avaiableArchivesNumbers[archiveLocation] = true;
		            saveAvaiableArchiveNumbers(ctx);
		            bwriter.flush();
		            bwriter.close();
		            Intent i = new Intent(REFRESH);
	        		ctx.sendBroadcast(i);
		            
		            
				}
				else
				{
					
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
	public static String[] readAllArchivesDates(Context ctx)
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
				            buffreader.readLine();
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
	
	public static String readArchiveContentPayload(String ArchiveDate, Context ctx)
	{
		    loadAvaiableArchiveNumbers(ctx);
			String ret = "";
	        try 
	        {	        	
	        	for(int i = 0; i< avaiableArchivesNumbers.length;i++)
	        	{	        		
	        		if(avaiableArchivesNumbers[i] && getArchiveDate(i,ctx).equals(ArchiveDate))
	        		{
	        			FileInputStream fIn = ctx.openFileInput (String.valueOf(i));
			            InputStreamReader isr = new InputStreamReader(fIn) ;
			            BufferedReader buffreader = new BufferedReader(isr) ;
		        		try
		        		{
		        			
			        		buffreader.readLine();	
			        		buffreader.readLine();	
			        		buffreader.readLine();
			        		String lineRead;
			        		while((lineRead = buffreader.readLine()) != null)
			        		{
			        			ret += lineRead +"\n";
			        		}
			        		String x = ret;
			        		String end = "";
			 	            			 	           
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
	public static boolean getSourceFromDate(String ArchiveDate, Context ctx)
	{
		    loadAvaiableArchiveNumbers(ctx);

	        try 
	        {	        	
	        	for(int i = 0; i< avaiableArchivesNumbers.length;i++)
	        	{	        		
	        		if(avaiableArchivesNumbers[i] && getArchiveDate(i,ctx).equals(ArchiveDate))
	        		{
	        			FileInputStream fIn = ctx.openFileInput (String.valueOf(i));
			            InputStreamReader isr = new InputStreamReader(fIn) ;
			            BufferedReader buffreader = new BufferedReader(isr) ;
		        		try
		        		{
		        			
			        		buffreader.readLine();	
			        		buffreader.readLine();	
			        		
			        		String commSrc = buffreader.readLine();
			        		return Boolean.valueOf(commSrc);
			 	            			 	           
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
		return true;
		
	}
	public static String getArchiveDate(int pos, Context ctx)
	{
		String ret = "";		
		try 
		{
			FileInputStream fIn;
			fIn = ctx.openFileInput (String.valueOf(pos));
			InputStreamReader isr = new InputStreamReader(fIn) ;
	        BufferedReader buffreader = new BufferedReader(isr) ;
	        buffreader.readLine();
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
	public static int getAvaiableArchiveNumber(Context ctx)
	{
		String date = null;
		int olderDatePosition = -1;
		for(int i = 0; i<30;i++)
		{
			if(!avaiableArchivesNumbers[i])
			{
				return i;
			}
			else
			{
				String archiveDate = getArchiveDate(i,ctx);
				if(dateComparison(date,archiveDate))
				{
					date = archiveDate;
					olderDatePosition = i;
				}
			}
		}
		return olderDatePosition;
	}
	private static boolean dateComparison(String date1,String date2)
	{
		if(date1 == null)
		{
			return true;
		}
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    	Date d1 = new Date();
    	Date d2 = new Date();
    	try
    	{
    		d1 = dateFormat.parse(date1);
    		d2 = dateFormat.parse(date2);
    		if(d1.after(d2))
    		{
    			return true;
    		}
    		return false;
    	}
    	catch(Exception e)
    	{
    		
    	}
    	
		return false;
		
	}
}