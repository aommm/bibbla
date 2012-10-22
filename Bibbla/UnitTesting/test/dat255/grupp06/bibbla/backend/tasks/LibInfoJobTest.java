/**
    Copyright 2012 Fahad Al-Khameesi, Madeleine Appert, Niklas Logren, Arild Matsson and Jonathan Orrö.
    
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
import dat255.grupp06.bibbla.model.Library;
import dat255.grupp06.bibbla.utils.Message;
import dat255.grupp06.bibbla.utils.Session;

/**
 * Tests LibInfoJob by running it and checking its return value.
 * 
 * @author Niklas Logren
 */
public class LibInfoJobTest extends TestCase {

	/**
	 * Runs two LibInfoJobs and verifies their properties and equality.
	 */
	public void testLibInfo() {

		// Create and run first job.
		LibInfoJob firstLibInfoJob = new LibInfoJob();
		Message firstResult = firstLibInfoJob.run();
		
		// Create and run second job.
		LibInfoJob secondLibInfoJob = new LibInfoJob();
		Message secondResult = firstLibInfoJob.run();

		// Verify message returned by the jobs.
		testResult(firstResult);
		testResult(secondResult);

		// Cast both return values to lists.
		List<Library> firstLibraryList = (List<Library>)firstResult.obj;
		List<Library> secondLibraryList = (List<Library>)secondResult.obj;
		
		// Both returned lists should be equal.
		assertEquals(firstLibraryList, secondLibraryList);
		
		// We should've found at least one library.
		assertTrue(firstLibraryList.size()>0);
		
		// Loop through all returned libraries,
		// and check that they all have correct properties.
		for (Library library : firstLibraryList) {
			testLibrary(library);			
		}
		
	}
	
	/**
	 * Tests the supplied message's properties.
	 * Nothing should be null except error, and result.obj is a list.
	 * 
	 * @param result - the Message to test.
	 */
	private void testResult(Message result) {
		// Verify message returned by the first job.
		assertNotNull(result);
		assertNull(result.error);
		assertNotNull(result.obj);
		assertTrue(result.obj instanceof List<?>);
	}
	
	/**
	 * Verifies that certain properties of Library are set.
	 * @param library - the library to check.
	 */
	private void testLibrary(Library library) {
		// All these properties should exist.
		assertNotNull(library.getAddress());
		assertNotNull(library.getArea());
		assertNotNull(library.getPostCode());
		assertNotNull(library.getPhoneNr());
		assertNotNull(library.getOpenH());
		assertNotNull(library.getName());
	}
	
}
