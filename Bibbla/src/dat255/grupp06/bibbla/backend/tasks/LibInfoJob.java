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
/**
 * 
 * @author Madeleine Appert
 * A class that collects all the necessary information about the available libraries.
 *
 */
public class LibInfoJob {

	//	private String searchPhrase = null;
	private Message message = new Message();
	private Document resultsDocument;
	private List<Library> results = new ArrayList<Library>();
	private String url1 = "http://goteborg.se/wps/portal/invanare/bibliotek/hit" +
	"ta-biblioteken/!ut/p/b1/jYxdcoIwFEbX0g2QS5NI8hgcgsgtKKAVXhirnQx_Yp22FFZfXI" +
	"DTfm_fzDmHFCS3n0FKxkAyciDF5fhdmeNn1V-O7f0Xi3Jjr7fCtRXEvtQQZOFGRyGKbQQzkM_A" +
	"0lcr5iCAQJ9DoFa7RG4pBUX_58ODKfjLX5Oieuus4dRZYPGFsKkUkgvJhEMdEq367p3kc8R5GN" +
	"GUZOQArExruCGKqz2K4ZqmNK3Hn3jatRGaNsb0hpnn7BPuvWBDwymdknqYED3mjczVhyxE4_aj" +
	"KDcQ8F3S91x_5I0x-wGZPeKpOWvlLl_PX80T6YpWCwPqF8ZzAyA!/pw/Z7_P1JQ8B1A0OG9F0I" +
	"TKPFNKL8QF3/ren/p=formFilter=selectService/-/?filterArea=%23&filterDirecti" +
	"on=%23&formFilterSubmit=Visa";
	private String url2 = "http://goteborg.se/wps/portal/invanare/bibliotek/hit" +
	"ta-biblioteken/!ut/p/b1/jYzbcoIwFEW_pT9gTppEwmNwiBdOQQlY4IWx2slwE-u0pfD11Q" +
	"9w2v22Z9ZapCC5A890zjiVJCPF-fBd2cNn1Z8P7f0X83JLNzvpUQXR0tWwToKtDgOUuxBuQH4D" +
	"Fku14g4CSFwKWKtVGrs7xkCx__nwYAr-8jekqN662XDsZjATc0mZK10hXS4d5pBw1XfvJL9FnI" +
	"cRzUhCMuClqeGKKC90lMPFGGbq8Sea0jZE20Zorpj4zj4W_gs2LJjMFNfDhOhzf-SezpIArdeP" +
	"stzCWqRx3wv9kTfW7gfkdMRjc9LKW7yevpon0hWtlhbUL7g166c!/pw/Z7_P1JQ8B1A0OG9F0I" +
	"TKPFNKL8QF3/ren/p=filterDirection/p=filterOrganisationType/p=filterArea/p=" +
	"pagination=20/-/";


	/**
	 * Creates a new LibInfoJob, which returns the first page of the search results.
	 * @param s - The string to search for.
	 */

	public LibInfoJob(){
		System.out.print("\n****** LibInfoJobcreated\n");

	}

	public Message run(){

		try {
			step1(url1);
			step2();
			step1(url2);
			step2();
		}
		catch (Exception e) {
			System.out.print("failed: "+e.getMessage()+" *** \n");
			message.error = Error.SEARCH_FAILED;
		}
		return message;
	}

	private void step1(String url) throws Exception {
		Response response = Jsoup.connect(url)
		.method(Method.GET)
		.timeout(5000)
		.execute();
		resultsDocument = response.parse();

	}

	private void step2() {		
		//	List<Library> results = new ArrayList<Library>();
		System.out.println("Came into step2");
		//	System.out.println(resultsDocument.html());
		Element searchResult = resultsDocument.select("ul.unit-list").first();// resultsDocument.select("li.odd js-unit"); //odd js-unit??

		Elements children = searchResult.children();
		for(Element e : children){

			Library library = new Library();

			//Set library name
			library.setName(e.select("a").first().text());

			Element div = e.child(1);
			Element adr=div.select("div.adr").first();

			//Set library visiting address
			String adress = adr.select("span.street-address").first().text();
			library.setAddress(adress);

			//Set library postal code
			String pC = adr.select("span.postal-code").first().text();
			library.setPostCode(pC);

			//Set library area
			String area = adr.select("span.locality").first().text();
			library.setArea(area);

			//Set library phone number
			String phoneNr = div.select("div.tel").first().select("span.value").first().select("a.phone-link").first().text();
			library.setPhoneNr(phoneNr);	

			//Set library open hours
			String openH = div.select("div.hours").first().select("span").first().text();
			library.setOpenH(openH);	

			//Set library email if exists
			Elements emailElement =div.select("div.email");
			if (emailElement.size()!=0){
				String email = div.select("div.email").first().select("a").first().text();
				library.setEmail(email);
			}
			
			//Set library visiting address if exists.
			Elements adrElements =div.select("div.adr");
			if (adrElements.size()==2){
				String visAdr = div.select("div.adr").last().select("span.street-address").first().text();
				library.setAddress(visAdr);
			}
			results.add(library);
		}

		message.obj = results;
	}
	/**
	 * Returns the list of Libraries that the class has parsed from the internet.
	 * @return	A list of Library objects.
	 */
	public List<Library> getLibraries(){
		return results;
	}
}
