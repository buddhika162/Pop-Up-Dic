package dic.Acty;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import dic.Acty.LoginActivity.UserLoginTask;
import dic.Acty.SignUpActivity.UserSignUpTask;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.Service;

import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.graphics.Typeface;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.ServiceState;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.widget.Toast;


/**
 * @this is the class wich haets the clipboard and button events
 */
// /test
public class PopUpDicActivity extends Activity {
	/** Called when the activity is first created. */
	/**************************************************************************************/

	Button btnStartProgress;
	ProgressDialog progressBar;
	private int progressBarStatus = 0;
	private Handler progressBarHandler = new Handler();
	private UserAddMeaningTask mSaveTask = null;
	private String word;
	private String meaning;
	private TextView mLoginStatusMessageView;
	private View mLoginStatusView;
	private View mLoginFormView;

	/********************************************************************************************/
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main); // main interface
		mLoginStatusMessageView = (TextView) findViewById(R.id.save_status_message);
		mLoginStatusView = findViewById(R.id.save_status);
		mLoginFormView = findViewById(R.id.main_view);
		
		final Button ActiBtn = (Button) findViewById(R.id.button1);
		if (DictionarySrvice.isInstanceCreated()) {

			ActiBtn.setText("Deactivate");

			EditText findTxt1 = (EditText) findViewById(R.id.editText2);
			EditText meanTxt1 = (EditText) findViewById(R.id.editText1);
			Typeface tf1 = Typeface.createFromAsset(getAssets(),
					"fonts/kandyunicode.ttf");

			meanTxt1.setTypeface(tf1);
			meanTxt1.setText(DictionarySrvice.meaning);
			findTxt1.setText(DictionarySrvice.key1);

		}

		else { // if dic is activated

			ActiBtn.setText("Activate");
		}
		// Toast.makeText(this, "My Service Started", Toast.LENGTH_LONG).show();
		// i = new Intent(this, DictionarySrvice.class);
		// startService(i);

		// Context.startService(new Intent(context, DictionarySrvice.class));

		/*********************************************************************************************************/
		// Action burtton

		/**************************************************************************************************************/
		// Find button

		/**************************************************************************************************************************/

		// clear button on click event

		/******************************************************************************************************************/

	}

	Intent in;

	public void startClicked(View view) {
		final Button ActiBtn = (Button) findViewById(R.id.button1);
		in = new Intent(PopUpDicActivity.this, DictionarySrvice.class);
		if (DictionarySrvice.isInstanceCreated()) {
			stopService(in);

			ActiBtn.setText("Activate");

		}

		else { // if dic is activated

			progressBar = new ProgressDialog(view.getContext());
			progressBar.setCancelable(false);
			progressBar.setCanceledOnTouchOutside(false);
			progressBar.setMessage("Database Creating ...");
			progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progressBar.setProgress(0);
			progressBar.setMax(100);
			progressBar.show();

			// reset progress bar status
			progressBarStatus = 0;

			// reset filesize

			new Thread(new Runnable() {
				public void run() {

					while (progressBarStatus < 100) {

						// process some tasks

						progressBarStatus = startsrv();

						// your computer is too fast, sleep 1 second
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

						// Update the progress bar
						progressBarHandler.post(new Runnable() {
							public void run() {
								progressBar.setProgress(progressBarStatus);
							}
						});
					}

					// ok, file is downloaded,
					if (progressBarStatus >= 100) {

						// sleep 2 seconds, so that you can see the 100%
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}

						// close the progress bar dialog
						progressBar.dismiss();
					}
				}
			}).start();

			startService(in);

			ActiBtn.setText("Deactivate");
		}

	}

	public int startsrv() {
		if (DictionarySrvice.isInstanceCreated()) {

		} else {
			// startService(in);
		}
		if (DictionarySrvice.count == 0) {
			return 50;
		} else if (DictionarySrvice.count == 100) {
			return 100;
		}
		return 70;
	}

	public void findClicked(View view) {
		EditText findTxt = (EditText) findViewById(R.id.editText2);
		EditText meanTxt = (EditText) findViewById(R.id.editText1); // text
																	// editer
																	// name

		Typeface tf = Typeface.createFromAsset(getAssets(),
				"fonts/kandyunicode.ttf");
		String key;
		meanTxt.setTypeface(tf);
		key = findTxt.getText().toString(); // get text to string
		// key= st.toString();
		String meaning = DictionarySrvice.rd.find(key);
		meanTxt.setText(DictionarySrvice.rd.find(key));
		Toast.makeText(this, "found", Toast.LENGTH_LONG).show();
	}
	
	public void saveClicked(View view) {
		EditText findTxt = (EditText) findViewById(R.id.editText2);
		EditText meanTxt = (EditText) findViewById(R.id.editText1); // text
		word =  findTxt.getText().toString();										// editer
		meaning =  meanTxt.getText().toString();															// name

		mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
		showProgress(true);
		mSaveTask = new UserAddMeaningTask();
		AsyncTask<Void, Void, Boolean> temp = mSaveTask.execute((Void) null);
	}
	////////////////////////////////////////////////////////////////////
	/**
	 * Shows the progress UI and hides the login form.
	 */
//	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}
	
	
	
	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserAddMeaningTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.
			InputStream is = null;
			String result = "";
			
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("word",word));
			nameValuePairs.add(new BasicNameValuePair("meaning",meaning));
			
			try {
				
				HttpClient httpclient = SessionControl.getHttpclient();
				HttpPost httppost = new HttpPost("http://192.168.2.111/popupdic/AddWords.php");
				
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
				}catch(Exception e){
				Log.e("log_tag", "Error converting result "+e.toString());
				}
			
			//startActivity(signUpIntent);
			
			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mSaveTask = null;
			showProgress(false);
			if (success) {
				//finish();
			} else {
				
				//add what to do if fails to save
				
			}
		}

		@Override
		protected void onCancelled() {
			mSaveTask = null;
			showProgress(false);
		}
	}
	
	
	
///////////////////////////////////////////////////////////////////////////
	public void clearClicked(View view) {
		final EditText findTxt = (EditText) findViewById(R.id.editText2);
		final EditText meanTxt = (EditText) findViewById(R.id.editText1); // text
																			// editer
																			// name

		meanTxt.setText("");
		findTxt.setText("");

		Toast.makeText(this, "cleared", Toast.LENGTH_LONG).show();
	}

	/**
	 * get data from clipboard
	 */
	
	public void loginClicked(View view){
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);


	}
	
}