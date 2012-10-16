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
import dat255.grupp06.bibbla.SampleSession;
import dat255.grupp06.bibbla.backend.Session;
import dat255.grupp06.bibbla.model.Book;
import dat255.grupp06.bibbla.utils.Message;

public class SearchJobTest extends TestCase {
	Session session;
	
	@Override
	public void setUp() {
		session = SampleSession.getSession();
	}
	
	public void testRun() {
		
		Message result;
		
		// Test normal string
		SearchJob searchJob = new SearchJob("hej");
		result = searchJob.run();
		assertNotNull(result.obj);
		assertTrue(result.obj instanceof List<?>);
		assertFalse(((List<?>) result.obj).isEmpty());
		
		// Test weird string
		SearchJob searchJobDifficult = new SearchJob("?^~QXZX");
		result = searchJobDifficult.run();
		assertNotNull(result.obj);
		assertTrue(result.obj instanceof List<?>);
		assertTrue(((List<?>)result.obj).isEmpty());
		
		// Test empty string
		SearchJob searchJobEmpty = new SearchJob("");
		result = searchJobEmpty.run();
		assertTrue(((List<Book>)result.obj).size() == 0);
		assertNull(result.error);
		// Perhaps we should check for more info?
	}
}
