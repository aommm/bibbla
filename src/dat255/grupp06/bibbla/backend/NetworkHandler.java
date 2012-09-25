package dat255.grupp06.bibbla.backend;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

public class NetworkHandler extends AsyncTask<String, String, String> {

    @Override
    protected String doInBackground(String... uri) {
    	//return apacheGet(uri);
    	return urlGet(uri[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }

    private String urlGet(String s) {
    	URL url = null;
    	HttpURLConnection connection = null;
    	String result = null;
    	
    	try {
    		url = new URL(s);
        	connection = (HttpURLConnection) url.openConnection();
        	
        	// Send request.
        	connection.connect(); 
        	
    		// Get data. (can also use stream)
    		result = (String)connection.getContent();
    		
    	} catch (Exception e) {} // Blablabla
    	finally {
    		try {connection.disconnect();} finally {} // It's art
    	}
    	
    	return result;
    }
    
	// Copy-pasted
	private String readDataStream(InputStream is) {
	    try {
	      ByteArrayOutputStream bo = new ByteArrayOutputStream();
	      int i = is.read();
	      while(i != -1) {
	        bo.write(i);
	        i = is.read();
	      }
	      return bo.toString();
	    } catch (IOException e) {
	      return "";
	    }
	}


    public String sendGetRequest(String url) {
    	String result = null;
    	try {
    		// THis call blocks.
			result = this.execute(url).get(); // TODO timeout?
		} catch (InterruptedException e) {}
    	  catch (ExecutionException e) {}
    	
    	return result;
    }
    
}