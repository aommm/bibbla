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

import junit.framework.TestCase;
import dat255.grupp06.bibbla.SessionFactory;
import dat255.grupp06.bibbla.backend.Session;
import dat255.grupp06.bibbla.utils.Message;

/**
 * Tests LoginJob, by trying to log in with both bad and good credentials. 
 * 
 * @author Niklas Logren
 */
public class TestLoginJob extends TestCase {
	Session session;
	
	@Override
	public void setUp() {
		// Get a new Session object.
		session = SessionFactory.getSession();
	}
	
	/**
	 * Tries to login with wrong login details.
	 */
	public void testBogusCredentials() {
		Session bogusSession = new Session("a","b","c");
		LoginJob loginJob = new LoginJob(bogusSession);
		Message result = loginJob.run();
		
		assertNotNull(result.error);
		assertFalse(result.loggedIn);
	}
	
	/**
	 * Tries to login with correct login details.
	 */
	public void testNormalCredentials() {
		
		// Run test only if we have login credentials. Otherwise, auto-succeed.
		if (session.getHasCredentials()) {
			LoginJob loginJob = new LoginJob(session);
			Message result = loginJob.run();
			
			assertTrue(result.loggedIn);
			assertNull(result.error);
		}
	}

}
