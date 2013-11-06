package uff.br.infouffdtn.dtn;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import uff.br.infouffdtn.db.Content;


public class DtnLog 
{
	
	//Log vai ser do tipo: Data: Dispositivo X enviou Content(Data e tipo) Y para Dispositivo Z.
	//                     Data: Dispositivo Z recebendo Content Y de Dispositivo X.  
	private static ArrayList<String> logs  = new ArrayList<String>();
	private static String cel1;
	

	public static void writeSendLog(Content c)
	{
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date d1 = new Date();
		String data = dateFormat.format(d1);
		String slog = data + " :\n " + "Dispositivo " + cel1 + " enviando o Content do tipo " + c.getName() + " com data " + c.getDate() + " para seus vizinhos ";
		logs.add(slog);
	}
	public static void writeReceiveLog(Content c, String cel2)
	{
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date d1 = new Date();
		String data = dateFormat.format(d1);
		String slog = data + " :\n " + "Dispositivo " + cel1 + " recebendo o Content do tipo " + c.getName() + " com data " + c.getDate() + " de " + cel2;
		logs.add(slog);		
	}
	public static void writeReceiveLogFromServer(Content c)
	{
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date d1 = new Date();
		String data = dateFormat.format(d1);
		String slog = data + " :\n " + "Dispositivo " + cel1 + " recebendo o Content do tipo " + c.getName() + " do servidor";
		logs.add(slog);		
	}
	public static void writeErrorLog()
	{
		
	}

	public static String getLogText()
	{
		String ret = "";
		for(int i = 0; i< logs.size(); i++)
		{
			ret += "\n\n " + logs.get(i);
		}
		return ret;
		
	}
	public static void setMyPhoneName(String n)
	{
		cel1 = n;
	}
	public static String getMyPhoneName()
	{
		return cel1;
	}
	public static ArrayList<String> getLog()
	{
		return logs;
	}
	public static void restartLog()
	{
		logs.clear();
	}
	
}
