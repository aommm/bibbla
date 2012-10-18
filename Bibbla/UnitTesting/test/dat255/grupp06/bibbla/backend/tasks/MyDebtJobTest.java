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

import dat255.grupp06.bibbla.SessionFactory;
import dat255.grupp06.bibbla.backend.Session;
import dat255.grupp06.bibbla.utils.Message;
import junit.framework.TestCase;

public class MyDebtJobTest extends TestCase {

	Session session;
	Message message;
	Integer debt;
	
	public void setUp(){
		
		session = SessionFactory.getSession();
		if (session.getHasCredentials()){
			MyDebtJob myDebtJob = new MyDebtJob(session);
			message = myDebtJob.run();
			
		}
	}
	
	/**
	 * Test that we dont get null object.
	 */
	public void testObjectNotNull(){
		if(session.getHasCredentials()){
			assertNotNull(message.obj);			
		}
	}
	

	/**
	 * Test that we get an integer object
	 */
	public void testDebtObject(){
		
		if(session.getHasCredentials()){
			assertTrue(message.obj instanceof Integer);
			debt = (Integer) message.obj;	
		}
	}
	
}
