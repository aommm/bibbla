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
import dat255.grupp06.bibbla.SessionFactory;
import dat255.grupp06.bibbla.backend.Session;
import dat255.grupp06.bibbla.model.Book;
import dat255.grupp06.bibbla.utils.Message;

/**
 * Tests RenewJob, by renewing one, several and all loaned books.
 * Is dependent on MyBooksJob.
 * 
 * @author Niklas Logren
 */
public class RenewJobTest extends TestCase {
	Session session;
	List<Book> loanedBooks;
	
	@Override
	public void setUp() {
		// Get a new Session object.
		session = SessionFactory.getSession();
		
		// Get our currently loaned books.
		MyBooksJob myBooksJob = new MyBooksJob(session);
		loanedBooks = (List<Book>)myBooksJob.run().obj;
		// Assert that MyBooksJob succeeded.
		assertTrue(loanedBooks instanceof List<?>);
	}
	
	/**
	 * Try to renew the first loaned book.
	 */
	public void testSingleRenewal() {

		if (loanedBooks.size()>0) {
			// Select first book of our loaned books.
			Book firstBook = loanedBooks.get(0);
			
			RenewJob renewJob = new RenewJob(session, firstBook);
			Message result = renewJob.run();
			assertNotNull(result);
		}
		
	}

}
