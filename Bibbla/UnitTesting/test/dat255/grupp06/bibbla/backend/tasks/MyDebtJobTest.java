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
import junit.framework.TestCase;

public class MyDebtJobTest extends TestCase {

	Session session;
	
	public void setUp(){
		session = SessionFactory.getSession();
		
	}
	
	public void testDebtValue(){
		
		if(session.getHasCredentials()){
			
		}
		
	}
}
