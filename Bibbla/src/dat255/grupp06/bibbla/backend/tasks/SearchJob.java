package dat255.grupp06.bibbla.backend.tasks;


import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import dat255.grupp06.bibbla.backend.Session;
import dat255.grupp06.bibbla.utils.Book;
import dat255.grupp06.bibbla.utils.Message;
import dat255.grupp06.bibbla.utils.Error;

public class SearchJob {
	
	private String searchPhrase = null;
	private Message message;
	private Session session;
	private Map<String,String> sessionCookies;
	
		public SearchJob(String s, Session session){
			searchPhrase = s;
			message = new Message();
			this.session = session;
			
		}
		
		public Message run(){
			
			// Create a response object.
			Message msg = new Message();
			
			try {
				System.out.print("\n****** SearchJob\n");
				System.out.print("* step1(): ");
				step1();
				System.out.print("succeeded! *\n");
				System.out.print("* step2(): ");
				step2();
				System.out.print("succeeded! *\n*");
				System.out.print("****** SearchJob done \n");
				
				// We made it through!
				//session.setCookies(sessionCookies);
				// Is public setCookies() needed?
				msg.loggedIn = true;
			}
			catch (Exception e) {
				System.out.print("failed: "+e.getMessage()+" *** \n");
				message.error = Error.SEARCH_FAILED;
			}

			return message;
		}


		private void step1() throws Exception{
			
			// Check if we're logged in, and if so, get session cookies.
			if (session.checkLogin()) {
				sessionCookies = session.getCookies();
				message.loggedIn = true; // Tell frontend
			} else { // Abort search.
				throw new Exception("Session.checkLogin() failed.");
			}
			
			Document doc = Jsoup.connect("http://encore.gotlib.goteborg.se/iii/encore/search/C__S"+searchPhrase+"__Orightresult__U1?lang=swe&suite=pearl")
					.cookies(sessionCookies)
					.get();	
		}
		
		private void step2() {		
			List<Book> results = new ArrayList<Book>();
			results.add(new Book("The Tibethan Book of the Dead", "Dalai Lama"));
			message.obj = results;
		}

}
