/*
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

	private final static int SIZE = 10;
	private static boolean[] avaiableArchivesNumbers = new boolean[SIZE];
	private static final String REFRESH = "uff.br.infouffdtn.REFRESH";

	// Escreve os dados de um Content em um arquivo cujo nome será a posição do
		// vetor avaiableArchivesNumbers.
		// Ex: Se um content estiver na posição 4 do vetor, o nome do seu arquivo
		// sera 4.
	public static void writeContent(Content content, Context ctx) throws IOException
	{
		loadAvaiableArchiveNumbers(ctx);
		try
		{
			String recentDate = getMostRecentDate(ctx);
			if(recentDate == null || dateComparison(content.getDate(),recentDate)) //SE A LISTA TIVER VAZIA OU SE A DATA RECEBIDA FOR MAIS ATUAL QUE A MAIS ATUAL QUE JA EXISTIA, PODE ESCREVER
			{
				int archiveLocation = getAvaiableArchiveNumber(ctx);
				if (archiveLocation != -1)
				{
					FileOutputStream fOut = ctx.openFileOutput(String.valueOf(archiveLocation), Context.MODE_PRIVATE);
					OutputStreamWriter osw = new OutputStreamWriter(fOut);
					BufferedWriter bwriter = new BufferedWriter(osw);
					String cname = content.getName();
					bwriter.write(cname);
					bwriter.newLine();
					String cdate = content.getDate().toString();
					bwriter.write(cdate);
					bwriter.newLine();
					String ccom = Boolean.toString(content.isCommSource());
					bwriter.write(ccom);
					bwriter.newLine();
					String cpay = content.getPayload();
					bwriter.write(cpay);
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

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	// Le todas as datas dos arquivos e passa para um vetor de String, para que
		// possa ser usado na classe DisplayActivity
	public static String[] readAllArchivesDates(Context ctx)
	{

		loadAvaiableArchiveNumbers(ctx);
		LinkedList<String> list = new LinkedList<String>();
		try
		{

			for (int i = 0; i < avaiableArchivesNumbers.length; i++)
			{
				if (avaiableArchivesNumbers[i])
				{
					try
					{
						FileInputStream fIn = ctx.openFileInput(String.valueOf(i));
						InputStreamReader isr = new InputStreamReader(fIn);
						BufferedReader buffreader = new BufferedReader(isr);
						buffreader.readLine();
						list.add(buffreader.readLine());
						isr.close();
					}
					catch (Exception e)
					{

					}
				}
			}

		}
		catch (Exception e)
		{

		}
		String[] ret = new String[list.size()];
		for (int i = 0; i < list.size(); i++)
		{
			ret[i] = list.get(i);
		}
		return ret;

	}


	// Salva a condição atual do vetor de arquivos no arquivo específico
	// AANArchive.
	public static void saveAvaiableArchiveNumbers(Context ctx)
	{

		String booleanValues = "";
		for (int i = 0; i < avaiableArchivesNumbers.length; i++)
		{
			booleanValues = booleanValues + String.valueOf(avaiableArchivesNumbers[i]) + ";";
		}
		try
		{
			FileOutputStream fOut = ctx.openFileOutput("AANArchive", Context.MODE_PRIVATE);
			OutputStreamWriter osw = new OutputStreamWriter(fOut);
			BufferedWriter bwriter = new BufferedWriter(osw);
			bwriter.write(booleanValues);
			bwriter.flush();
			bwriter.close();
		}
		catch (Exception e)
		{

		}

	}

	// Carrega os valores para o vetor de arquivos, indicando se a posição já
	// está alocada(true) ou não (false)
	// OBS: Esse método foi criado porque ele reinicializava o vetor quando a
	// aplicação fosse reinicializada/inicializada
	// embora os arquivos permanesessem lá. Esse método carrega o vetor de um
	// arquivo específico chamado AANArchive.
	public static void loadAvaiableArchiveNumbers(Context ctx)
	{
		String booleanValues = "";
		try
		{
			FileInputStream fIn = ctx.openFileInput("AANArchive");
			InputStreamReader isr = new InputStreamReader(fIn);
			BufferedReader buffreader = new BufferedReader(isr);
			booleanValues = buffreader.readLine();
			isr.close();
			String[] booleanValuesSplit = booleanValues.split(";");
			for (int i = 0; i < SIZE; i++)
			{
				if (booleanValuesSplit[i].equals("true"))
				{
					avaiableArchivesNumbers[i] = true;
				}
				else
				{
					avaiableArchivesNumbers[i] = false;
				}
			}
		}
		catch (IOException ioe)
		{
			ioe.printStackTrace();
		}

	}
	// Retorna o payload em string do arquivo cuja data é ArchiveDate
	public static String readArchiveContentPayload(String ArchiveDate, Context ctx)
	{
		loadAvaiableArchiveNumbers(ctx);
		String ret = "";
		try
		{
			for (int i = 0; i < avaiableArchivesNumbers.length; i++)
			{
				if (avaiableArchivesNumbers[i] && getArchiveDate(i, ctx).equals(ArchiveDate))
				{
					FileInputStream fIn = ctx.openFileInput(String.valueOf(i));
					InputStreamReader isr = new InputStreamReader(fIn);
					BufferedReader buffreader = new BufferedReader(isr);
					try
					{

						buffreader.readLine();
						buffreader.readLine();
						buffreader.readLine();
						String lineRead;
						while ((lineRead = buffreader.readLine()) != null)
						{
							ret += lineRead + "\n";
						}
						String x = ret;
						String end = "";

					}
					catch (Exception e)
					{

					}
					finally
					{
						isr.close();
					}
				}
			}

		}
		catch (Exception e)
		{

		}
		return ret;

	}

	
	// Retorna o tipo de comunicação que o arquivo foi recebido, true = wifi,
	// false = dtn
	public static boolean getSourceFromDate(String ArchiveDate, Context ctx)
	{
		loadAvaiableArchiveNumbers(ctx);

		try
		{
			for (int i = 0; i < avaiableArchivesNumbers.length; i++)
			{
				if (avaiableArchivesNumbers[i] && getArchiveDate(i, ctx).equals(ArchiveDate))
				{
					FileInputStream fIn = ctx.openFileInput(String.valueOf(i));
					InputStreamReader isr = new InputStreamReader(fIn);
					BufferedReader buffreader = new BufferedReader(isr);
					try
					{

						buffreader.readLine();
						buffreader.readLine();

						String commSrc = buffreader.readLine();
						return Boolean.valueOf(commSrc);

					}
					catch (Exception e)
					{

					}
					finally
					{
						isr.close();
					}
				}
			}

		}
		catch (Exception e)
		{

		}
		return true;

	}

	// Retorna a data do arquivo que está na posição POS do vetor.
	public static String getArchiveDate(int pos, Context ctx)
	{
		String ret = "";
		try
		{
			FileInputStream fIn;
			fIn = ctx.openFileInput(String.valueOf(pos));
			InputStreamReader isr = new InputStreamReader(fIn);
			BufferedReader buffreader = new BufferedReader(isr);
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
	// Deleta todos os arquivos e limpa o vetor.
	public static void deleteAllArchives(Context ctx)
	{
		loadAvaiableArchiveNumbers(ctx);
		File dir = ctx.getFilesDir();
		for (int i = 0; i < SIZE; i++)
		{
			try
			{
				File file = new File(dir, String.valueOf(i));
				file.delete();
				avaiableArchivesNumbers[i] = false;
			}
			catch(Exception e)
			{
				
			}
		}
		saveAvaiableArchiveNumbers(ctx);

	}

	// Retorna a posição do vetor disponível para escrever um arquivo. Caso
		// esteja cheio, ele vai retornar a posição
		// do arquivo menos atual para que seja sobrescrito.
	public static int getAvaiableArchiveNumber(Context ctx)
	{
		String date = null;
		int olderDatePosition = -1;
		for (int i = 0; i < SIZE; i++)
		{
			if (!avaiableArchivesNumbers[i])
			{
				return i;
			}
			else
			{
				String archiveDate = getArchiveDate(i, ctx);
				if (dateComparison(date, archiveDate) || date == null)
				{
					date = archiveDate;
					olderDatePosition = i;
				}
			}
		}
		return olderDatePosition;
	}

	// Pega a data do arquivo mais recente, e retorna o payload (em String)
		// dessa data.

	public static String getMostRecentDate(Context ctx)
	{
		loadAvaiableArchiveNumbers(ctx);
		if(isEmpty())
		{
			return null;
		}
		String date = null;
		for (int i = 0; i < SIZE; i++)
		{
			String archiveDate = getArchiveDate(i, ctx);
			if (dateComparison(archiveDate, date))
			{
				date = archiveDate;
			}
		}
		return date;
	}
	private static boolean isEmpty()
	{
		for(int i = 0; i< SIZE ;i++)
		{
			if(avaiableArchivesNumbers[i] == true)
				return false;
		}
		return true;
		
	}

	// Compara se uma data no formato "dd/MM/yyyy HH:mm:ss" é posterior a outra.
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
	


}
*/