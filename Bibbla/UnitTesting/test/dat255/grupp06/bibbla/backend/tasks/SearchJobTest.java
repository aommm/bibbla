package dat255.grupp06.bibbla.backend.tasks;

import junit.framework.TestCase;
import dat255.grupp06.bibbla.SampleSession;
import dat255.grupp06.bibbla.backend.Session;
import dat255.grupp06.bibbla.utils.Message;

public class SearchJobTest extends TestCase {
	Session session;
	
	@Override
	public void setUp() {
		session = SampleSession.getSession();
	}
	
	public void testRun() {
		
		SearchJob searchJob = new SearchJob("hej", session);
		Message result = searchJob.run();
		//assertWhat?
		
	}
}
