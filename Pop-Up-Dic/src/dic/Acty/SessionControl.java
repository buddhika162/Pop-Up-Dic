package dic.Acty;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

public class SessionControl {
	
	static private HttpClient httpclient = null;

	public static HttpClient getHttpclient() {
	    if( httpclient == null){
	        SessionControl.setHttpclient(new DefaultHttpClient());
	    }
	    return httpclient;
	}

	public static void setHttpclient(HttpClient httpclient) {
	    SessionControl.httpclient = httpclient;
	}

}
