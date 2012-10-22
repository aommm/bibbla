/**
    This file is part of Bibbla.

    Bibbla is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Bibbla is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Bibbla.  If not, see <http://www.gnu.org/licenses/>.
 **/

package dat255.grupp06.bibbla.backend.tasks;

import java.io.IOException;
import java.util.Map;

import junit.framework.TestCase;

import org.jsoup.Connection.Response;

import dat255.grupp06.bibbla.CredentialsFactory;
import dat255.grupp06.bibbla.model.Book;
import dat255.grupp06.bibbla.model.Credentials;
import dat255.grupp06.bibbla.utils.Error;
import dat255.grupp06.bibbla.utils.Message;
import dat255.grupp06.bibbla.utils.Session;

/**
 * Tests methods in ReserveJob, which are typically called hierarchically
 * through a single call to run().
 * @author arla
 */
public class ReserveJobTest extends TestCase {

	/** Test book representing Hej mössan by Boel Werner */
	public static final Book TEST_BOOK = new Book("", "", "", "", "https://" +
			"www.gotlib.goteborg.se/search*swe~S6?/Xhej&searchscope=6&SORT=D" +
			"/Xhej&searchscope=6&SORT=D&SUBKEY=hej/1%2C412%2C412%2CC/request" +
			"browse~b1741463&FF=Xhej&searchscope=6&SORT=D&1%2C1%2C");
	private static final Book EMPTYURL_BOOK = new Book("", "", "", "", "");
	private static final Book BADURL_BOOK = new Book("", "", "", "",
			"http://google.com/");
	/** The code for one of the available libraries */  
	public static final String LIBRARY_CODE = "tu";
	public static final String LIBRARY_NAME = "Tuve";
	public static final Credentials CREDENTIALS =
			CredentialsFactory.getCredentials();
	
	@Override
	public void tearDown() throws Exception {
		super.tearDown();
		UnreserveJob unreserveJob = new UnreserveJob(TEST_BOOK, CREDENTIALS,
				new Session());
		unreserveJob.run();
	}
	
	public void testRunValid() {
		ReserveJob job = new ReserveJob(TEST_BOOK, LIBRARY_CODE, CREDENTIALS,
				new Session());
		Message message = job.run();
		assertNull(message.error);
		String library = (String) message.obj;
		assertEquals(LIBRARY_NAME, library);
	}
	
	public void testRunEmptyUrl() {
		ReserveJob job = new ReserveJob(EMPTYURL_BOOK, LIBRARY_CODE,
				CREDENTIALS, new Session());
		try {
			@SuppressWarnings("unused")
			Message message = job.run();
			fail("Expected IllegalArgumentException from Jsoup.connect()");
		} catch (IllegalArgumentException e) {
			// Got the expected exception.
		}
	}
	
	public void testRunBadUrl() {
		ReserveJob job = new ReserveJob(BADURL_BOOK, LIBRARY_CODE, CREDENTIALS,
				new Session());
		Message message = job.run();
		assertNotNull(message.error);
		assertEquals(message.error, Error.RESERVE_FAILED);
	}
	
	public void testRunBadLibrary() {
		ReserveJob job = new ReserveJob(TEST_BOOK, "", CREDENTIALS,
				new Session());
		Message message = job.run();
		assertNotNull(message.error);
		assertEquals(message.error, Error.RESERVE_FAILED);
	}

	public void testCreatePostData() {
		final String TEST_LIBRARY_CODE = "foo";
		Map<String,String> data = ReserveJob.createPostData(TEST_LIBRARY_CODE);
		assertTrue(data.size() == 4);
		assertEquals(data.get(ReserveJob.KEY_LIBCODE), TEST_LIBRARY_CODE);
		assertEquals(data.get(ReserveJob.KEY_YEAR), ReserveJob.VAL_YEAR);
		assertEquals(data.get(ReserveJob.KEY_MONTH), ReserveJob.VAL_MONTH);
		assertEquals(data.get(ReserveJob.KEY_DAY), ReserveJob.VAL_DAY);
	}

	public void testConnect() throws IOException {
		ReserveJob job = new ReserveJob(TEST_BOOK, LIBRARY_CODE, CREDENTIALS,
				new Session()) {
			public final Response response = connect();
		};
//		Response response = job.response;
		fail("Not yet implemented");
	}

	public void testParseResults() {
		fail("Not yet implemented");
	}

	public void testFindDiv() {
		fail("Not yet implemented");
	}
}
