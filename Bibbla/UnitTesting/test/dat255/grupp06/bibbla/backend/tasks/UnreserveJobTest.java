package dat255.grupp06.bibbla.backend.tasks;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import dat255.grupp06.bibbla.model.Book;
import dat255.grupp06.bibbla.utils.Message;
import dat255.grupp06.bibbla.utils.Session;

public class UnreserveJobTest extends TestCase {
	
	private final Session SESSION = new Session();
	
	private final Book TEST_BOOK = ReserveJobTest.TEST_BOOK;
	private final Book TEST_BOOK2 = ReserveJobTest.TEST_BOOK2;
	private final Book TEST_BOOK3 = ReserveJobTest.TEST_BOOK3;
	@SuppressWarnings("serial")
	private final List<Book> TEST_BOOKS = new ArrayList<Book>() {{
		add(TEST_BOOK); add(TEST_BOOK2); add(TEST_BOOK3);
	}};

	private final ReserveJob reserveJob = new ReserveJob(
			TEST_BOOK, ReserveJobTest.LIBRARY_CODE,
			ReserveJobTest.CREDENTIALS, SESSION);
	private final ReserveJob reserveJob2 = new ReserveJob(
			TEST_BOOK2, ReserveJobTest.LIBRARY_CODE,
			ReserveJobTest.CREDENTIALS, SESSION);
	private final ReserveJob reserveJob3 = new ReserveJob(
			TEST_BOOK3, ReserveJobTest.LIBRARY_CODE,
			ReserveJobTest.CREDENTIALS, SESSION);
	
	public UnreserveJobTest() {
		TEST_BOOK.setUnreserveId("canceli4058195x01");
		TEST_BOOK2.setUnreserveId("cancelb1758572x03");
		TEST_BOOK3.setUnreserveId("canceli4105561x02");
	}

	public void testOneRun() {
		if (ReserveJobTest.CREDENTIALS == null) return;
		// Prepare: Get number of reservations and reserve the test book.
		MyReservationsJob reservationsJob = new MyReservationsJob(
				ReserveJobTest.CREDENTIALS, SESSION);
		@SuppressWarnings("unchecked")
		int beforeReserveSize = ((List<Book>) reservationsJob.run().obj).size();
		Message reserveMessage = reserveJob.run();
		assertNull(reserveMessage.error);
		
		// Method call
		UnreserveJob job = new UnreserveJob(TEST_BOOK,
				ReserveJobTest.CREDENTIALS, SESSION);
		Message message = job.run();
		
		// Assertion
		assertNull(message.error);
		@SuppressWarnings("unchecked")
		List<Book> myBooksAfterUnreserve = (List<Book>) message.obj;
		assertFalse(myBooksAfterUnreserve.contains(TEST_BOOK));

		@SuppressWarnings("unchecked")
		int afterUnreserveSize = ((List<Book>) reservationsJob.run().obj).size();
		assertEquals(beforeReserveSize, afterUnreserveSize);
	}
	
	public void testMultipleRun() {
		if (ReserveJobTest.CREDENTIALS == null) return;
		// Prepare: Get number of reservations and reserve the test books.
		MyReservationsJob reservationsJob = new MyReservationsJob(
				ReserveJobTest.CREDENTIALS, SESSION);
		@SuppressWarnings("unchecked")
		int beforeReserveSize = ((List<Book>) reservationsJob.run().obj).size();
		ReserveJob[] reserveJobs = {reserveJob, reserveJob2, reserveJob3};
		Message rmessage;
		for (ReserveJob rjob : reserveJobs) {
			rmessage = rjob.run();
			assertNull(rmessage.error);
		}
		
		// Method call
		UnreserveJob job = new UnreserveJob(TEST_BOOKS, ReserveJobTest.CREDENTIALS,
				SESSION);
		Message unreserveMessage = job.run();
		
		// Assertion
		assertNull(unreserveMessage.error);
		@SuppressWarnings("unchecked")
		List<Book> myBooksAfterUnreserve = (List<Book>) unreserveMessage.obj;
		for(Book book : TEST_BOOKS) {
			assertFalse(myBooksAfterUnreserve.contains(book));
		}

		@SuppressWarnings("unchecked")
		int afterUnreserveSize = ((List<Book>) reservationsJob.run().obj).size();
		assertEquals(beforeReserveSize, afterUnreserveSize);
	}
}
