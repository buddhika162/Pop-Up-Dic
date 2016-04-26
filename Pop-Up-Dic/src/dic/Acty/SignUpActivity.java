package dic.Acty;

import java.io.BufferedReader;
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
import org.json.JSONObject;

import dic.Acty.LoginActivity.UserLoginTask;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class SignUpActivity extends Activity {

	private String mEmail;
	private String mPassword;
	private String mName;
	private String mHint;
	private EditText mEmailView;
	private EditText mPasswordView;
	private EditText mNameView;
	private EditText mHintView;
	private View mLoginStatusView;
	private View mLoginFormView;
	
	private UserSignUpTask mAuthTask = null;
	
	private TextView mLoginStatusMessageView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		mEmailView = (EditText) findViewById(R.id.editText2);
		mNameView = (EditText) findViewById(R.id.editText1);
		mPasswordView = (EditText) findViewById(R.id.editText3);
		mHintView = (EditText) findViewById(R.id.editText5);
		mLoginStatusView = findViewById(R.id.signUp_status);
		mLoginFormView = findViewById(R.id.signUp_form);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sign_up, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void signUpClicked(View view){
		attemptSignUp();

	}
	
	public void attemptSignUp() {
		
				// Reset errors.
				mEmailView.setError(null);

				// Store values at the time of the login attempt.
				mEmail = mEmailView.getText().toString();
				mPassword = mPasswordView.getText().toString();
				mHint = mHintView.getText().toString();
				mName = mNameView.getText().toString();
				
				boolean cancel = false;
				View focusView = null;
				if (TextUtils.isEmpty(mEmail)) {
					mEmailView.setError(getString(R.string.error_field_required));
					focusView = mEmailView;
					cancel = true;
				} else if (!mEmail.contains("@")) {
					mEmailView.setError(getString(R.string.error_invalid_email));
					focusView = mEmailView;
					cancel = true;
				}
				
				if (cancel) {
					// There was an error; don't attempt login and focus the first
					// form field with an error.
					focusView.requestFocus();
				} else {
					//mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
					showProgress(true);
					mAuthTask = new UserSignUpTask();
					AsyncTask<Void, Void, Boolean> temp = mAuthTask.execute((Void) null);
				}
		
	}
	
	
	
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
	public class UserSignUpTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.
			InputStream is = null;
			String result = "";
			
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("email",mEmail));
			nameValuePairs.add(new BasicNameValuePair("name",mName));
			nameValuePairs.add(new BasicNameValuePair("password",mPassword));
			nameValuePairs.add(new BasicNameValuePair("hint",mHint));
			
			try {
				
				HttpClient httpclient = SessionControl.getHttpclient();
				HttpPost httppost = new HttpPost("http://192.168.2.111/popupdic/SignUp.php");
				
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
				sb.append(line + "\n");
				}
				is.close();
				}catch(Exception e){
				Log.e("log_tag", "Error converting result "+e.toString());
				}
			
			//startActivity(signUpIntent);
			// TODO: register the new account here.
			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			showProgress(false);

			if (success) {
				finish();
			} else {
				mEmailView
						.setError(getString(R.string.error_invalid_email));
				mEmailView.requestFocus();
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}
}
