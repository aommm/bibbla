package dat255.grupp06.bibbla.backend;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;

public class Backend {
	
	NetworkHandler network;
	Jsoup parser; // TODO: MIT license. needs to include notice?
	
	public Backend() {
		 network = new NetworkHandler();
	}
	
	/** Searches backend for the supplied string, and calls callback when done. **/
	public void search(String s, Callback c) {
		
		/**
		 * Todo: Implement "Job" structure?
		 * If so, this search() method will merely do
		 * new Search(s,c);
		 * and all this code should be moved to Search (or to its parent).
		 * All jobs should extend AsyncTask, and do callback when done.
		 */
		
		// Does not work.
		//String html = network.sendGetRequest("http://www.google.com");
		//Document doc = Jsoup.parse(html);
		// Get all tags with attribute href.
		//Elements links = doc.select("a[href]");
		
		// Create list of results.
		ArrayList<String> results = new ArrayList<String>();
		results.add("Resultat #1"); // Add example text.
		// Create bundle.
		Bundle bundle = new Bundle();
		bundle.putStringArrayList("results", results);
		// Create message.
		Message msg = Message.obtain();
		msg.setData(bundle);
		
		// Submit callback.
		c.handleMessage(msg);
	}
}