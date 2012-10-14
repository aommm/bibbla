package dat255.grupp06.bibbla.backend.tasks;


import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import dat255.grupp06.bibbla.model.Library;
import dat255.grupp06.bibbla.utils.Error;
import dat255.grupp06.bibbla.utils.Message;

public class LibInfoJob {
	
//	private String searchPhrase = null;
	private Message message = new Message();
	private Document resultsDocument;
//	private int pageNumber;

	/**
	 * Creates a new LibInfoJob, which returns the first page of the search results.
	 * @param s - The string to search for.
	 */
	
	public LibInfoJob(){
		System.out.print("\n****** LibInfoJobcreated\n");

	}
	
	public Message run(){
					
		try {
			System.out.print("\n****** Do LibInfoJob\n");
			System.out.print("* step1(): ");
			step1();
			System.out.print("succeeded!? *\n");
			System.out.print("* step2(): ");
			step2();
			System.out.print("succeeded!? *\n*");
			System.out.print("****** LibInfoJob done \n");
		}
		catch (Exception e) {
			System.out.print("failed: "+e.getMessage()+" *** \n");
			message.error = Error.SEARCH_FAILED;
		}

		return message;
	}

	private void step1() throws Exception {
		// Fetch first page
			String url = "http://goteborg.se/wps/portal/invanare/bibliotek/hitta-biblioteken/" +
					"!ut/p/b1/jYxdcoIwFEbX0g2QS5NI8hgcgsgtKKAVXhirnQx_Yp22FFZfXIDTfm_fzDmHFCS3n" +
					"0FKxkAyciDF5fhdmeNn1V-O7f0Xi3Jjr7fCtRXEvtQQZOFGRyGKbQQzkM_A0lcr5iCAQJ9DoFa" +
					"7RG4pBUX_58ODKfjLX5Oieuus4dRZYPGFsKkUkgvJhEMdEq367p3kc8R5GNGUZOQArExruCGKq" +
					"z2K4ZqmNK3Hn3jatRGaNsb0hpnn7BPuvWBDwymdknqYED3mjczVhyxE4_ajKDcQ8F3S91x_5I0" +
					"x-wGZPeKpOWvlLl_PX80T6YpWCwPqF8ZzAyA!/pw/Z7_P1JQ8B1A0OG9F0ITKPFNKL8QF3/ren" +
					"/p=formFilter=selectService/-/?filterArea=%23&filterDirection=%23&formFilt" +
					"erSubmit=Visa";
			Response response = Jsoup.connect(url)
					.method(Method.GET)
					.timeout(5000)
					.execute();
			resultsDocument = response.parse();
	
	}
	
	private void step2() {		
		List<Library> results = new ArrayList<Library>();
		Elements searchResults = resultsDocument.select("li.div.unit-h cf");// resultsDocument.select("li.odd js-unit"); //odd js-unit??
		for(Element e : searchResults){
			System.out.println("Came into for loop");
			Library library = new Library();
			System.out.println("Created library");
			library.setName(e.select("a").first().text());
			System.out.println("setName..."+ library.getName());
			library.setAddress(e.select("span").first().text());
			System.out.println("setAddress...");
			library.setPhoneNr(e.select("a.phone-link").get(1).text());
			System.out.println("set phoneNr...");
			results.add(library);
			System.out.println(library.toString());
		}
		
		message.obj = results;
	}

}
