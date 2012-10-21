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

import java.util.List;

import junit.framework.TestCase;
import dat255.grupp06.bibbla.CredentialsFactory;
import dat255.grupp06.bibbla.model.Book;
import dat255.grupp06.bibbla.model.Credentials;
import dat255.grupp06.bibbla.utils.Message;
import dat255.grupp06.bibbla.utils.Session;

/**
 * Tests MyReservationsJob by running two ReservationJobs.
 * 
 * Fetches the user's current reservations twice,
 * verifies the return values, and compares them for equality.
 * 
 * @author Niklas Logren
 */
public class MyReservationsJobTest extends TestCase {
	Credentials credentials;
	Session session;
	
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
	 * Fetches the user's reservations twice, compares them,
	 * and verifies that the return values are okay.
	 */
	public void testReservations() {
		
		if(credentials != null){
		// Create and run first job.
		MyReservationsJob firstMyReservationsJob = new MyReservationsJob(credentials, session);
		Message firstResult = firstMyReservationsJob.run();
		
		// Create and run second job.
		MyReservationsJob secondMyReservationsJob = new MyReservationsJob(credentials, session);
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
