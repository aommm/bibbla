package dat255.grupp06.bibbla.backend.tasks;


import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import dat255.grupp06.bibbla.backend.Session;
import dat255.grupp06.bibbla.utils.Book;
import dat255.grupp06.bibbla.utils.Message;

public class SearchJob {
	
	private String searchPhrase = null;
	private String htmlResults = null;
	private Message message;
	private Session session;
	
		public SearchJob(String s, Session session){
			searchPhrase = s;
			message = new Message();
			this.session = session;
			
		}
		
		
		public Message run(){
			
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
			List<Book> results = new ArrayList<Book>();
			return message;
			
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
		
		
		private boolean step2() {
		
			// Check if logged in or not
			// Set appropriate value in message.loggedIn
			
			return false;

			
		}





}
