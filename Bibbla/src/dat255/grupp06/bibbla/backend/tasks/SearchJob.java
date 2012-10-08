package dat255.grupp06.bibbla.backend.tasks;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import dat255.grupp06.bibbla.backend.Session;
import dat255.grupp06.bibbla.utils.Book;
import dat255.grupp06.bibbla.utils.Message;
import dat255.grupp06.bibbla.utils.Error;

public class SearchJob {
	
	private String searchPhrase = null;
	private Message message = new Message();
	private Document resultsDocument;
	
		public SearchJob(String s){
			searchPhrase = s;
			message = new Message();
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
			}
			catch (Exception e) {
				System.out.print("failed: "+e.getMessage()+" *** \n");
				message.error = Error.SEARCH_FAILED;
			}

			return message;
		}


		private void step1() throws Exception {
			
			Response response = Jsoup.connect("http://www.gotlib.goteborg.se/search*swe/X?searchtype=X&searcharg="+searchPhrase+"&searchscope=6&SUBMIT=S%C3%B6k")
					.method(Method.GET)
					.execute();

			resultsDocument = response.parse();
		}
		
		private void step2() {		
			
			List<Book> results = new ArrayList<Book>();
			Elements searchResults = resultsDocument.select("table.breifCitTable");
			for(Element e : searchResults){
				Book book = new Book();
				book.setName(e.select("a").get(1).text());
				book.setAuthor(e.select("strong").first().text());
				book.setType(e.select("td.sokresultat").get(4).getElementsByTag("img").first().attr("alt"));
				book.setUrl(e.select("a").get(1).attr("abs:href")); 
				book.setReserveUrl(e.select("div.reserverapadding").select("a").attr("abs:href"));
				results.add(book);
			}
			
			message.obj = results;
		}

}
