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
import dat255.grupp06.bibbla.CredentialsFactory;
import dat255.grupp06.bibbla.backend.login.Session;
import dat255.grupp06.bibbla.model.Credentials;
import dat255.grupp06.bibbla.utils.Message;

public class MyDebtJobTest extends TestCase {

	Credentials credentials;
	Message message;
	Integer debt;
	
	public void setUp(){
		
		credentials = CredentialsFactory.getCredentials();
		if (credentials != null){
			MyDebtJob myDebtJob = new MyDebtJob(credentials, new Session());
			message = myDebtJob.run();
			
		}
	}
	
	/**
	 * Test that we dont get null object.
	 */
	public void testObjectNotNull(){
		if(credentials != null){
			assertNotNull(message.obj);			
		}
	}
	

	/**
	 * Test that we get an integer object
	 */
	public void testDebtObject(){
		
		if(credentials != null){
			assertTrue(message.obj instanceof Integer);
			debt = (Integer) message.obj;	
		}
	}
	
}
