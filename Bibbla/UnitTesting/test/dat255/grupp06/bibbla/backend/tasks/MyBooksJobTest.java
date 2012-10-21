package dat255.grupp06.bibbla.backend.tasks;

import java.util.List;

import dat255.grupp06.bibbla.CredentialsFactory;
import dat255.grupp06.bibbla.model.Book;
import dat255.grupp06.bibbla.model.Credentials;
import dat255.grupp06.bibbla.utils.Message;
import dat255.grupp06.bibbla.utils.Session;
import junit.framework.TestCase;

public class MyBooksJobTest extends TestCase {

	Credentials credentials;
	Session session;
	/**
	 * We get our credentials(If we have any).
	 * create a session if we do have credentials.
	 */
	@Override
	public void setUp() {
		// Get a new Credentials object.
		credentials = CredentialsFactory.getCredentials();
		// Create a session using these credentials.
		if(credentials != null){
		session = new Session(credentials);
		}
	}
	
	/**
	 * Fetches the user's loans twice, compares them,
	 * and verifies that the return values are okay.
	 */
	
	public void testLoanedBooks(){
		if(credentials != null){
		// Create and run first job.
		MyBooksJob firstLoanedBooksJob = new MyBooksJob(credentials, session);
		Message firstResult = firstLoanedBooksJob.run();
				
		// Create and run second job.
		MyBooksJob secondMyReservationsJob = new MyBooksJob(credentials, session);
		Message secondResult = secondMyReservationsJob.run();
				
		// Verify message returned by the first job.
		assertNotNull(firstResult);
		assertNull(firstResult.error);
		assertNotNull(firstResult.obj);
		assertTrue(firstResult.obj instanceof List<?>);

		// Verify message returned by the second job.
		assertNotNull(secondResult);
		assertNull(secondResult.error);
		assertNotNull(secondResult.obj);
		assertTrue(secondResult.obj instanceof List<?>);
				
		// Cast both return values to lists.
		List<Book> firstBookList = (List<Book>)firstResult.obj;
		List<Book> secondBookList = (List<Book>)secondResult.obj;
				
		// Both returned messages should be equal.
		assertEquals(firstBookList, secondBookList);
		}	
	}
}
