package dic.Acty;



import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import dic.Acty.LoginActivity.UserLoginTask;

import android.os.AsyncTask;
import android.util.Log;

/**
 * @author Buddhika
 *This is class for read file and search the meaning
 */
public class ReadFile {
	
	String meaning= new String("not found");
	String mainKey;
	private UserLoginTask mAuthTask = null;
	
	Hashtable<String, String> ht=new Hashtable<String, String>(); // creat hash table
    void read(InputStream is1) throws FileNotFoundException{  		// read methord

    	
        Scanner sc=new Scanner(is1); 
        String line;
        ht.clear();
        String[] str=new String[2];
        
        while(sc.hasNext()){
        	try{
            line=sc.nextLine();										// read line by line
            
            								// spliting ling by tab
            str=line.split("\t");
            
            ht.put(str[0],str[1]);									// put the splited words to hash table
            }catch(ArrayIndexOutOfBoundsException e){
               // System.out.println("exeption1"+e);
            }
            
        }
        

    }
    public String find(String key){										// finding word methord
    	
    	
    	//System.out.println(key+"     searching    ");
    	
    	if(key.endsWith(".")||key.endsWith(",")||key.endsWith("?")||key.endsWith("!")||key.endsWith("'")){
			key=key.substring(0, key.length()-1);
			
		}
    	mainKey = key;
    	meaning = ht.get(key.toLowerCase());											// get meaning from hash table
    	//System.out.println("searching the meaning is   "+meaning);
    	if(meaning==null){
    		
    		
    		if(key.endsWith("s")){
    			key=key.substring(0, key.length()-1);
    			meaning = ht.get(key.toLowerCase());
    		}
    		if(meaning==null){
    			
    			mAuthTask = new UserLoginTask();
    			AsyncTask<Void, Void, Boolean> temp = mAuthTask.execute((Void) null);
    			
    			
    			try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    			
    			
    			if(meaning == null){
    				meaning = "not found";
    			}
    		
    		}
    		//System.out.println(meaning);
    	}
    	return meaning;
    
    }
    
    
    /**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.
			InputStream is = null;
			String result = "";
			
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("word",mainKey));
			
			try {
				
				HttpClient httpclient = SessionControl.getHttpclient();
				HttpPost httppost = new HttpPost("http://192.168.43.174/popupdic/FindWords.php");
				
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				is = entity.getContent();
				// Simulate network access.
//				Thread.sleep(2000);
			} 
			catch (Exception e) {
				Log.e("log_tag", "Error in http connection "+e.toString());
				// TODO: handle exception
			}
			
			
			try{
				BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
				StringBuilder sb = new StringBuilder();
				String line ="";
				if ((line = reader.readLine()) != null) {
					System.out.println("Line...."+line);
				sb.append(line + "\n");
				}
				is.close();
				JSONObject jsonObject = new JSONObject(line);
				meaning = jsonObject.getString("meaning");
				
				}catch(Exception e){
				Log.e("log_tag", "Error converting result "+e.toString());
				}
			
			
			// TODO: register the new account here.
			return false;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			//showProgress(false);

			if (success) {
				//finish();
			} else {
//				mPasswordView
//						.setError(getString(R.string.error_incorrect_password));
//				mPasswordView.requestFocus();
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			//showProgress(false);
		}
	}
    
}






/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

    
   









