package dat255.grupp06.bibbla.backend.tasks;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import dat255.grupp06.bibbla.utils.Message;

import android.net.Uri;

public class SearchJob {
	
	private String searchPhrase = null;
	private String htmlResults = null;
	private Message message;
	
		public SearchJob(String s){
			searchPhrase = s;
			message = new Message();
			
		}
		
		
		public Message runSearch(){
			
			try {
				step1();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				if (step1())
					step2();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return message;
			
		}
		
		private boolean step2() {
		
			// Check if logged in or not
			// Set appropriate value in message.loggedIn
			
			return false;

			
		}


		private boolean step1() throws IOException{
			
			/*URL url = new URL("http://encore.gotlib.goteborg.se/iii/encore/search/C__S"+searchPhrase+"__Orightresult__U1?lang=swe&suite=pearl");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			//InputStream inputStream = new BufferedInputStream(connection.getInputStream());
			htmlResults = (String)connection.getContent();
			if (connection.getResponseCode() == 200)
				return true;
			else
				return false;
				*/
			Document doc = Jsoup.connect("http://encore.gotlib.goteborg.se/iii/encore/search/C__S"+searchPhrase+"__Orightresult__U1?lang=swe&suite=pearl").get();
			
			return false;

			
			
		}

}
