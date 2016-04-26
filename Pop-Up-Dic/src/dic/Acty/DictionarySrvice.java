package dic.Acty;

import java.io.FileNotFoundException;
import java.io.InputStream;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.IBinder;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DictionarySrvice extends Service implements Runnable{
	final static  ReadFile rd= new ReadFile();
	private static DictionarySrvice instance;
	public static int count = 0; 
	private ClipboardManager mClipboard;
    
    private String ns;
    private NotificationManager mNotificationManager;
    private Context context;
    private Intent notificationIntent;
    CharSequence st;
    
    ClipboardManager.OnPrimaryClipChangedListener mPrimaryChangeListener
    = new ClipboardManager.OnPrimaryClipChangedListener() {
		 	public void onPrimaryClipChanged() {
		 		
		 		updateClipData();
		 		
		 	}
	 };
	 
	 
	

	   public static boolean isInstanceCreated() { 
	      return instance != null; 
	   }
	 
	 
	 @Override
		public void onStart(Intent intent, int startid) {	
			Toast.makeText(this, "My Service new started", Toast.LENGTH_LONG).show();
				
			}
	 @Override
	 public void onDestroy(){
	        //System.out.println("clearing");
	        rd.ht.clear();
	       instance= null;
	       count=0;
	       Toast.makeText(this, "Service Destroyed", Toast.LENGTH_SHORT).show();
	       stopForeground(true);
	       int pid = android.os.Process.myPid();
	       android.os.Process.killProcess(pid);
	       
	       //Log.e(\"killed\",process.processName);
	      
	    }
	 

	 
	 @Override
	 public int onStartCommand(Intent intent, int flags, int startId) {
	     
		 
		 final int myID = 1234;

		//The intent to launch when the user clicks the expanded notification
		Intent intent1 = new Intent(this, PopUpDicActivity.class);
		intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent pendIntent = PendingIntent.getActivity(this, 0, intent1, 0);

		//This constructor is deprecated. Use Notification.Builder instead
		Notification notice = new Notification(R.drawable.dictionary, "Pop-Up-Dic Service", System.currentTimeMillis());

		//This method is deprecated. Use Notification.Builder instead.
		notice.setLatestEventInfo(this, "Pop-Up-Dic", "Activated", pendIntent);

		notice.flags |= Notification.FLAG_NO_CLEAR;
		startForeground(myID, notice);

		 
		 
		// Toast.makeText(this, "on start command", Toast.LENGTH_LONG).show();
		 
		 
		 
	     return START_STICKY;
	 }
	 
	@Override
	public void onCreate() {
		instance = this;
		count=0;
		
		Thread thread = new Thread(this);
        thread.start();
        instance = this;
		
		 
	     // We want this service to continue running until it is explicitly
	     // stopped, so return sticky.
		 mClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
	        ns = Context.NOTIFICATION_SERVICE;
	        context = getApplicationContext();
	        mNotificationManager = (NotificationManager) getSystemService(ns);
	        //notificationIntent = getIntent();
			
			
			mClipboard.addPrimaryClipChangedListener(mPrimaryChangeListener);
			
			

		
		
		
		
		
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		
		
		
		return null;
	}
	
	
	
	public  void run() {
		
		final InputStream is = getResources().openRawResource(R.raw.sinhala1);
		count=0;
    	try {
    		rd.read(is);									// read file
			//System.out.println("file reded"+rd.ht.get("ability"));
			if(rd.ht.isEmpty()){
				//System.out.println("empty");
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//System.out.println("no file found");			// file notfound exeption
		}
		
		count=100;
		//Toast.makeText(this, "Database loaded", Toast.LENGTH_LONG).show();
}
	
	
	public static String key1,meaning;

	void updateClipData(){
		ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
    	
    	// Gets the clipboard as text.
    	st = item.getText();
    	key1 = st.toString();
    	//System.out.println(key1);
    	meaning = rd.find(key1);
    	//EditText findTxt1 = (EditText) findViewById(R.id.editText2);
	  //  EditText meanTxt1 = (EditText) findViewById(R.id.editText1);
    	Typeface tf1 = Typeface.createFromAsset(getAssets(), "fonts/kandyunicode.ttf");
		   
		//   	meanTxt1.setTypeface(tf1);
		 //  	meanTxt1.setText(meaning);
		  // 	findTxt1.setText(st);
    	//
    	Toast toast = Toast.makeText(this, st+"-"+meaning, Toast.LENGTH_LONG);
    	TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
    	v.setTypeface(tf1);
    	v.setTextColor(Color.WHITE);
    	toast.show();
   		
    	
    	
    	//Toast toast = new Toast(getApplicationContext());
    	// toast.setTypeface(tf1);
    	int icon = R.drawable.dictionary1;
		CharSequence tickerText = st+"-"+meaning;
		long when = System.currentTimeMillis();
	
		//Toast.makeText(this, st+"-"+meaning, Toast.LENGTH_LONG).show();
    	Notification notification = new Notification(icon, tickerText, when); 
    	Intent intent2 = new Intent(this, PopUpDicActivity.class);
		intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent pendIntent = PendingIntent.getActivity(this, 0, intent2, 0);
		
    	CharSequence contentTitle = st+"-"+meaning;
    	CharSequence contentText = "Please check furthe deatails in the app";
    	//notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
    	//PendingIntent contentIntent = PendingIntent.getActivity(context, 0,notificationIntent, 0);
    	notification.setLatestEventInfo(context,contentTitle, contentText,pendIntent);
    	notification.flags = Notification.FLAG_AUTO_CANCEL;
    	int HELLO_ID = 100;
    	mNotificationManager.notify(HELLO_ID,notification);
        
    	
		} 
	
	
}
