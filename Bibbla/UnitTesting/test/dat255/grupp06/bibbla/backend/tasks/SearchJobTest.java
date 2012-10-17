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

public class SearchJobTest extends TestCase {
	Session session;
	
	@Override
	public void setUp() {
		session = SessionFactory.getSession();
	}
	
	public void testRun() {
		
		Message result;
		
		// Test normal string
		SearchJob searchJob = new SearchJob("hej");
		result = searchJob.run();
		assertNotNull(result.obj);
		assertTrue(result.obj instanceof List<?>);
		assertFalse(((List<?>) result.obj).isEmpty());
		
		// Test that we get unique results for different search phrases
		
		SearchJob searchJobPotter =  new SearchJob("potter");
		SearchJob searchJobHej = new SearchJob("hej");
		assertFalse((List<?>) searchJobHej.run().obj == ((List<?>) searchJobPotter.run().obj));
		
		// Test for different page numbers
		SearchJob searchJob0 = new SearchJob("Potter",0); 
		SearchJob searchJob1 = new SearchJob("potter", 1);
		SearchJob searchJob2 = new SearchJob("potter", 2);
		assertFalse((List<?>) searchJob1.run().obj == (List<?>) searchJob2.run().obj);
		assertTrue((List<?>) searchJob0.run().obj == (List<?>) searchJob1.run().obj)
		
		// Test weird string
		SearchJob searchJobDifficult = new SearchJob("?^~QXZX");
		result = searchJobDifficult.run();
		assertNotNull(result.obj);
		assertTrue(result.obj instanceof List<?>);
		assertTrue(((List<?>)result.obj).isEmpty());
		
		// Test empty string
		SearchJob searchJobEmpty = new SearchJob("");
		result = searchJobEmpty.run();
		assertTrue(((List<?>)result.obj).size() == 0);
		assertNull(result.error);
		// Perhaps we should check for more info?
	}
}
