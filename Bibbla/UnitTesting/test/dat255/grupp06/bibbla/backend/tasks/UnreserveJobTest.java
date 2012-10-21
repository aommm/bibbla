package dat255.grupp06.bibbla.backend.tasks;

import dat255.grupp06.bibbla.utils.Message;
import dat255.grupp06.bibbla.utils.Session;
import junit.framework.TestCase;

public class UnreserveJobTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
		ReserveJob reserveJob = new ReserveJob(ReserveJobTest.TEST_BOOK,
				ReserveJobTest.LIBRARY_CODE, ReserveJobTest.CREDENTIALS,
				new Session());
		reserveJob.run();
	}

	public void testConnect() {
		fail("Not yet implemented");
	}

	public void testRun() {
		fail("Not yet implemented");
		ReserveJob job = new ReserveJob(ReserveJobTest.TEST_BOOK,
				ReserveJobTest.LIBRARY_CODE, ReserveJobTest.CREDENTIALS,
				new Session());
		Message message = job.run();
		assertNull(message.error);
		String library = (String) message.obj;
		assertEquals(library, ReserveJobTest.LIBRARY_NAME);
	}

}
