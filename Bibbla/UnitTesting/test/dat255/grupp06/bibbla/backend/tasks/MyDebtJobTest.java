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
import dat255.grupp06.bibbla.model.Credentials;
import dat255.grupp06.bibbla.utils.Session;

/**
 * Test the object we get from the MyDebtValue class
 * @author Fahad
 *
 */

public class MyDebtJobTest extends TestCase {

	Credentials credentials;
	
	public void setUp(){
		
		credentials = CredentialsFactory.getCredentials();
			
		}
	
	/**
	 * Test that message object is not null.
	 */
	
	public void testMessageNotNull(){
		
		if(credentials != null){
			MyDebtJob myDebtJob = new MyDebtJob(credentials, new Session());
			assertNotNull(myDebtJob.run());	
		}
	}
	
	/**
	 * Test that we don't get null object.
	 */
	public void testObjectNotNull(){
		if(credentials != null){
			MyDebtJob myDebtJob = new MyDebtJob(credentials, new Session());
			assertNotNull(myDebtJob.run().obj);			
		}
	}

	/**
	 * Test that we get an integer object
	 */
	public void testObjectIsInteger(){
		
		if(credentials != null){
			MyDebtJob myDebtJob = new MyDebtJob(credentials, new Session());
			assertTrue(myDebtJob.run().obj instanceof Integer);	
		}
	}
	
	/**
	 * Test that debt value is not negative
	 */
	
	public void testIntegerValue(){
		
		if(credentials != null){
			MyDebtJob myDebtJob = new MyDebtJob(credentials, new Session());
			assertFalse((Integer) myDebtJob.run().obj < 0);
				
			}
		}
	
	
	
}
