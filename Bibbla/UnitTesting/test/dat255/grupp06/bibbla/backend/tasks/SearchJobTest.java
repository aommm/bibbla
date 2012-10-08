package dat255.grupp06.bibbla.backend.tasks;

import java.util.List;

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
		
		Message result;
		
		// Test normal string
		SearchJob searchJob = new SearchJob("hej", session);
		result = searchJob.run();
		assertNotNull(result.obj);
		assertTrue(result.obj instanceof List<?>);
		assertFalse(((List<?>) result.obj).isEmpty());
		
		// Test weird string
		SearchJob searchJobDifficult = new SearchJob("?^~QXZX", session);
		result = searchJobDifficult.run();
		assertNotNull(result.obj);
		assertTrue(result.obj instanceof List<?>);
		assertTrue(((List<?>)result.obj).isEmpty());
		
		// Test empty string
		SearchJob searchJobEmpty = new SearchJob("", session);
		result = searchJobEmpty.run();
		assertNotNull(result.error);
		// Perhaps we should check for more info?
	}
}
