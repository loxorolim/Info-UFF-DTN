package uff.br.infouffdtn.dtn;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import uff.br.infouffdtn.FetchThread;
import uff.br.infouffdtn.db.Content;


import uff.br.infouffdtn.db.FileManager;
import uff.br.infouffdtn.server.CommFile;
import android.app.IntentService;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.util.Log;
import de.tubs.ibr.dtn.api.Block;
import de.tubs.ibr.dtn.api.Bundle;
import de.tubs.ibr.dtn.api.Bundle.ProcFlags;
import de.tubs.ibr.dtn.api.BundleID;
import de.tubs.ibr.dtn.api.DTNClient;
import de.tubs.ibr.dtn.api.DTNClient.Session;
import de.tubs.ibr.dtn.api.DataHandler;
import de.tubs.ibr.dtn.api.GroupEndpoint;
import de.tubs.ibr.dtn.api.Node;
import de.tubs.ibr.dtn.api.Registration;
import de.tubs.ibr.dtn.api.ServiceNotAvailableException;
import de.tubs.ibr.dtn.api.SessionConnection;
import de.tubs.ibr.dtn.api.SessionDestroyedException;
import de.tubs.ibr.dtn.api.SingletonEndpoint;
import de.tubs.ibr.dtn.api.TransferMode;

public class InfoService extends IntentService
{

	// This TAG is used to identify this class (e.g. for debugging)
	private static final String TAG = "InfoService";

	// mark a specific bundle as delivered
	public static final String MARK_DELIVERED_INTENT = "uff.br.infouffdtn.MARK_DELIVERED";

	// process a status report
	public static final String REPORT_DELIVERED_INTENT = "uff.br.infouffdtn.REPORT_DELIVERED";

	// this intent send out a PING message
	public static final String DTN_REQUEST_INTENT = "uff.br.infouffdtn.PRESENCE";
	
	public static ArrayList<byte[]> toSendViaDtn;
	public static Content contentToSend;
	public static final String DTN_REFRESH_INTENT = "uff.br.infouffdtn.REFRESH";
    
	// indicates updated data to other components
	public static final String DATA_UPDATED = "uff.br.infouffdtn.DATA_UPDATED";
	public static final String PAYLOAD_UPDATED = "uff.br.infouffdtn.PAYLOAD_UPDATED";
	public static final String REFRESH = "uff.br.infouffdtn.REFRESH";
	// group EID of this app
	public static final GroupEndpoint PING_GROUP_EID = new GroupEndpoint("dtn://broadcast.dtn/infouffdtn");

	// This is the object that receives interactions from clients. See
	// RemoteService for a more complete example.
	private final IBinder mBinder = new LocalBinder();

	// The communication with the DTN service is done using the DTNClient
	private DTNClient mClient = null;

	private final int neighbourListSize = 10;
	private LinkedList<String> neighbourList;
	// Hold the last ping result
	private Double mLastMeasurement = 0.0;

	// Hold the start time of the last ping
	private Long mStart = 0L;

	public InfoService()
	{
		super(TAG);
	}

	/**
	 * Class for clients to access. Because we know this service always runs in
	 * the same process as its clients, we don't need to deal with IPC.
	 */
	public class LocalBinder extends Binder
	{
		public InfoService getService()
		{
			return InfoService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		return mBinder;
	}

	public GroupEndpoint getGroupEndpoint()
	{
		return PING_GROUP_EID;
	}

	public Double getLastMeasurement()
	{
		return mLastMeasurement;
	}

	private String getLocalEndpoint()
	{
		try
		{
			if (mClient != null)
			{
				if (mClient.getDTNService() == null)
					return null;
				return mClient.getDTNService().getEndpoint();
			}
		}
		catch (RemoteException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	private synchronized void sendInfoUffDtnRequestBundle()
	{	
			try
			{
				
				
				refreshNeighbours();
				
				for (int i = 0; i < neighbourList.size(); i++)
				{
					//String destAddress = neighbours.get(i).endpoint.toString() + "/InfoUffDtn";
					SingletonEndpoint destination = new SingletonEndpoint(neighbourList.get(i));
	
					// create a new bundle
					Bundle b = new Bundle();
	
					// set the destination of the bundle
					b.setDestination(destination);
	
					// limit the lifetime of the bundle to 60 seconds
					b.setLifetime(120L);
					
	
					// set status report requests for bundle reception
					 b.set(ProcFlags.REQUEST_REPORT_OF_BUNDLE_RECEPTION, true);
	
					// set destination for status reports
					 b.setReportto(SingletonEndpoint.ME);
	
					// generate some payload
					
	
					try
					{
						
						// get the DTN session
						Session s = mClient.getSession();
						
						byte [] bytes = prepareRequestBundleToSend();
						s.send(b, bytes);
							
					}
					catch (SessionDestroyedException e)
					{
						Log.e(TAG, "could not send the message", e);
//						DtnLog.writeErrorLog();
					}
					catch (InterruptedException e)
					{
						Log.e(TAG, "could not send the message", e);
//						DtnLog.writeErrorLog();
					}
				}
			}
			catch (Exception e)
			{
	
			}
		
	}
	private void sendInfoUffDtnBundle()
	{	
			try
			{
				

					//String destAddress = neighbours.get(i).endpoint.toString() + "/InfoUffDtn";
					//SingletonEndpoint destination = new SingletonEndpoint(neighbourList.get(i));
	
					// create a new bundle
					Bundle b = new Bundle();
	
					// set the destination of the bundle
					b.setDestination(PING_GROUP_EID);
	
					// limit the lifetime of the bundle to 60 seconds
					b.setLifetime(60*60*3L);
					
	
					// set status report requests for bundle reception
					 b.set(ProcFlags.REQUEST_REPORT_OF_BUNDLE_RECEPTION, true);
	
					// set destination for status reports
					 b.setReportto(SingletonEndpoint.ME);
	
					// generate some payload
					
	
					try
					{
						
						// get the DTN session
						Session s = mClient.getSession();
						//ArrayList<byte[]> filesInBytes = FileManager.getContentBytesToSend();
						if(contentToSend != null)
						{
							
							//byte [] bytes = prepareBundleToSend(toSendViaDtn);
							byte[] bytes = prepareBundleToSend(contentToSend);
							s.send(b, bytes);
						}
						
							
					}
					catch (SessionDestroyedException e)
					{
						Log.e(TAG, "could not send the message", e);
//						DtnLog.writeErrorLog();
					}
					catch (InterruptedException e)
					{
						Log.e(TAG, "could not send the message", e);
//						DtnLog.writeErrorLog();
					}
				
			}
			catch (Exception e)
			{
	
			}
		
	}
	private synchronized void sendInfoUffDtnContentsBundle(ArrayList<byte[]> bundlebytes, String destinationAddress)
	{	
			try
			{
				
				//List<Node> neighbours = mClient.getDTNService().getNeighbors();

					String destAddress = destinationAddress ;
					SingletonEndpoint destination = new SingletonEndpoint(destAddress);
	
					// create a new bundle
					Bundle b = new Bundle();
	
					// set the destination of the bundle
					b.setDestination(destination);
	
					// limit the lifetime of the bundle to 60 seconds
					b.setLifetime(120L);
					
	
					// set status report requests for bundle reception
					 b.set(ProcFlags.REQUEST_REPORT_OF_BUNDLE_RECEPTION, true);
	
					// set destination for status reports
					 b.setReportto(SingletonEndpoint.ME);
	
					// generate some payload
					
	
					try
					{
						
						// get the DTN session
						Session s = mClient.getSession();
						byte [] bytes = prepareContentsListBundleToSend(bundlebytes);
						s.send(b, bytes);
							
					}
					catch (SessionDestroyedException e)
					{
						Log.e(TAG, "could not send the message", e);
//						DtnLog.writeErrorLog();
					}
					catch (InterruptedException e)
					{
						Log.e(TAG, "could not send the message", e);
//						DtnLog.writeErrorLog();
					}
				
			}
			catch (Exception e)
			{
	
			}
		
	}
	
	private byte[] prepareRequestBundleToSend()
	{
		//1 BYTE MODO
		//16 BYTES ANDROID ID
		//RESTANTE � A LISTA DE COMMFILE DOS SEUS ARQUIVOS MAIS ATUAIS
		
		byte[] modeBytes =  new byte[1];
		modeBytes[0] = DtnMode.ALERTPRESENCE;
		byte[] androidIdBytes = DtnLog.getMyPhoneName().getBytes();
		ArrayList<CommFile> files = FileManager.getContentList();
		byte[] filesBytes = FileManager.getObjectBytes(files);
		byte[] ret = new byte[modeBytes.length + androidIdBytes.length + filesBytes.length];
		
		System.arraycopy(modeBytes,0, ret ,0, 1);	
		System.arraycopy(androidIdBytes,0, ret ,1, androidIdBytes.length);
		System.arraycopy(filesBytes, 0 , ret, 1+androidIdBytes.length, filesBytes.length);
		  

		return ret;	
	}
	private byte[] prepareBundleToSend(Content c)
	{
		byte[] modeBytes =  new byte[1];
		modeBytes[0] = DtnMode.SENDCONTENT;
		byte[] androidIdBytes = DtnLog.getMyPhoneName().getBytes();
		byte[] filesBytes = FileManager.prepareContentToSend(c);
		byte[] ret = new byte[modeBytes.length + androidIdBytes.length + filesBytes.length];
		System.arraycopy(modeBytes,0, ret ,0, 1);	
		System.arraycopy(androidIdBytes,0, ret ,1, androidIdBytes.length);
		System.arraycopy(filesBytes, 0 , ret, 1+androidIdBytes.length, filesBytes.length);
		
		return ret;
	}
	
	private byte[] prepareContentsListBundleToSend(ArrayList<byte[]> contentsBytes)
	{
		//1 BYTE MODO
		//16 BYTES ANDROID ID
		//RESTANTE � A LISTA DE COMMFILE DOS SEUS ARQUIVOS MAIS ATUAIS

		byte[] modeBytes =  new byte[1];
		modeBytes[0] = DtnMode.SENDCONTENT;
		byte[] androidIdBytes = DtnLog.getMyPhoneName().getBytes();
		byte[] filesBytes = FileManager.getObjectBytes(contentsBytes);
		byte[] ret = new byte[modeBytes.length + androidIdBytes.length + filesBytes.length];
		System.arraycopy(modeBytes,0, ret ,0, 1);	
		System.arraycopy(androidIdBytes,0, ret ,1, androidIdBytes.length);
		System.arraycopy(filesBytes, 0 , ret, 1+androidIdBytes.length, filesBytes.length);
		  

		return ret;	
	}
	private void refreshNeighbours()
	{
		try
		{
			List<Node> neighbours = mClient.getDTNService().getNeighbors();
			
			for(int i = 0; i < neighbours.size();i++)
			{
				String destAddress = neighbours.get(i).endpoint.toString() + "/InfoUffDtn";
				addNeighbourToList(destAddress);
			}
		}
		catch(Exception e)
		{
			
		}
		
	}

	@Override
	protected void onHandleIntent(Intent intent)
	{
		String action = intent.getAction();

		if (de.tubs.ibr.dtn.Intent.RECEIVE.equals(action))
		{
			// Received bundles from the DTN service here
			try
			{
				// We loop here until no more bundles are available
				// (queryNext() returns false)
				while (mClient.getSession().queryNext())
				{
				}
			}
			catch (SessionDestroyedException e)
			{
				Log.e(TAG, "Can not query for bundle", e);
			}
			catch (InterruptedException e)
			{
				Log.e(TAG, "Can not query for bundle", e);
			}
		}
		else
			if (MARK_DELIVERED_INTENT.equals(action))
			{
				// retrieve the bundle ID of the intent
				BundleID bundleid = intent.getParcelableExtra("bundleid");

				try
				{
					// mark the bundle ID as delivered
					mClient.getSession().delivered(bundleid);
				}
				catch (Exception e)
				{
					Log.e(TAG, "Can not mark bundle as delivered.", e);
				}
			}
			else
				if (REPORT_DELIVERED_INTENT.equals(action))
				{
					// retrieve the source of the status report
					SingletonEndpoint source = intent.getParcelableExtra("source");

					// retrieve the bundle ID of the intent
					BundleID bundleid = intent.getParcelableExtra("bundleid");

					Log.d(TAG, "Status report received for " + bundleid.toString() + " from " + source.toString());
				}
				else
					if (DTN_REQUEST_INTENT.equals(action))
					{
						sendInfoUffDtnBundle();
					}
					else
						if(DTN_REFRESH_INTENT.equals(action))
						{
							refreshNeighbours();
						}
	}

	SessionConnection mSession = new SessionConnection()
	{

		@Override
		public void onSessionConnected(Session session)
		{
			Log.d(TAG, "Session connected");

			String localeid = getLocalEndpoint();
			//DtnLog.setMyPhoneName(localeid);
			if (localeid != null)
			{
				// notify other components of the updated EID
				Intent i = new Intent(DATA_UPDATED);
				i.putExtra("localeid", localeid);
				sendBroadcast(i);
			}
		}

		@Override
		public void onSessionDisconnected()
		{
			Log.d(TAG, "Session disconnected");
		}

	};

	public void preparePayload(String payload)
	{
		// notify other components of the updated EID
		Intent i = new Intent(PAYLOAD_UPDATED);
		i.putExtra("payload", payload);
		sendBroadcast(i);
	}
	private void addNeighbourToList(String dest)
	{
		if(!neighbourList.contains(dest))
		{
			if(neighbourList.size() < neighbourListSize)
			{
				neighbourList.addFirst(dest);
			}
			else
			{
				neighbourList.removeLast();
				neighbourList.addFirst(dest);
			}
		}
	}

	@Override
	public void onCreate()
	{
		super.onCreate();

		// create a new DTN client
		mClient = new DTNClient(mSession);
		//neighbourList = new LinkedList<String>();
		
		
		
		
		// create registration with "example-app" as endpoint
		// if the EID of this device is "dtn://device" then the
		// address of this app will be "dtn://device/example-app"
		Registration registration = new Registration("InfoUffDtn");

		// additionally join a group
		registration.add(PING_GROUP_EID);

		// register own data handler for incoming bundles
		mClient.setDataHandler(mDataHandler);

		try
		{
			// initialize the connection to the DTN service
			mClient.initialize(this, registration);

			
			Log.d(TAG, "Connection to DTN service established.");
		}
		catch (ServiceNotAvailableException e)
		{
			// The DTN service has not been found
			Log.e(TAG, "DTN service unavailable. Is IBR-DTN installed?", e);
		}
		catch (SecurityException e)
		{
			// The service has not been found
			Log.e(TAG, "The app has no permission to access the DTN service. It is important to install the DTN service first and then the app.", e);
		}

	}
    
	@Override
	public void onDestroy()
	{
		// terminate the DTN service
		mClient.terminate();
		mClient = null;
		toSendViaDtn = null;
		
		super.onDestroy();
	}
	

	/**
	 * This data handler is used to process incoming bundles
	 */
	private DataHandler mDataHandler = new DataHandler()
	{

		ByteArrayOutputStream stream = null;
		private Bundle mBundle = null;

		@Override
		public void startBundle(Bundle bundle)
		{
			// store the bundle header locally
			mBundle = bundle;
		}

		@Override
		public void endBundle()
		{
			// complete bundle received
			BundleID received = new BundleID(mBundle);


			
			// mark the bundle as delivered
			Intent i = new Intent(InfoService.this, InfoService.class);
			i.setAction(MARK_DELIVERED_INTENT);
			i.putExtra("bundleid", received);
			startService(i);

			// free the bundle header
			mBundle = null;

			// notify other components of the updated value
			Intent updatedIntent = new Intent(DATA_UPDATED);
			updatedIntent.putExtra("bundleid", received);
			sendBroadcast(updatedIntent);
		}

		@Override
		public TransferMode startBlock(Block block)
		{
			// we are only interested in payload blocks (type = 1)
			if (block.type == 1)
			{
				// return SIMPLE mode to received the payload as "payload()"
				// calls
				stream = new ByteArrayOutputStream();
				return TransferMode.SIMPLE;
			}
			else
			{
				// return NULL to discard the payload of this block
				return TransferMode.NULL;
			}
		}

		@Override
		public void endBlock()
		{
			if (stream != null) {

				try
				{
					
					
					byte[] streamBytes = stream.toByteArray();
					byte mode = streamBytes[0];
					byte[] androidIdBytes = new byte[16];
					System.arraycopy(streamBytes, 1, androidIdBytes, 0, androidIdBytes.length);
					String androidId = new String(androidIdBytes,"Cp1252");
					
//					if(mode == DtnMode.ALERTPRESENCE)
//					{
//						byte[] commFilesBytes = new byte[streamBytes.length - 17];
//						System.arraycopy(streamBytes, 17, commFilesBytes, 0, commFilesBytes.length);
//						ArrayList<CommFile> commfiles = FileManager.getCommFileListFromBytes(commFilesBytes);
//						ArrayList<Content> filesToSend = FileManager.getContentsToConvert(commfiles);
//						ArrayList<byte[]> contentsInBytes = FileManager.getContentBytesToSend(filesToSend);
//						sendInfoUffDtnContentsBundle(contentsInBytes, mBundle.getReportto().toString());
//						
//					}
					if(mode == DtnMode.SENDCONTENT)
					{
						byte[] contentsInBytes = new byte[streamBytes.length - 17];
						System.arraycopy(streamBytes, 17, contentsInBytes, 0, contentsInBytes.length);
						Content content = FileManager.getContentFromBytes(contentsInBytes,false);
						FileManager.writeContent(content);
						DtnLog.writeReceiveLog(content, androidId);
						//						if(contentsBytes.size() > 0)
//						{
//							for(int i = 0 ; i < contentsBytes.size(); i++)
//							{
//								Content c = FileManager.getContentFromBytes(contentsBytes.get(i), false);
//								FileManager.writeContent(c);
//								DtnLog.writeReceiveLog(c, androidId);
//							}
//						}
//						else
//						{
//							//Thread t = new Thread(new HtmlGetterThread("rolim.no-ip.org", 9990,true));
//							//t.start();
//						}
					}
					

				}
				catch(Exception e)
				{
					DtnLog.writeErrorLog();
				}
           
		    
                stream = null;
		    }
		}

		@Override
		public ParcelFileDescriptor fd()
		{
			// This method is used to hand-over a file descriptor to the
			// DTN service. We do not need the method here and always return
			// null.
			return null;
		}

		@Override
		public void payload(byte[] data)
		{
			if (stream == null) return;
		    // write data to the stream
		    try {
                stream.write(data);
            } catch (IOException e) {
                Log.e(TAG, "error on writing payload", e);
            }

			


		}

		@Override
		public void progress(long offset, long length)
		{
			// if payload is written to a file descriptor, the progress
			// will be announced here
			Log.d(TAG, offset + " of " + length + " bytes received");
		}
	};
}
