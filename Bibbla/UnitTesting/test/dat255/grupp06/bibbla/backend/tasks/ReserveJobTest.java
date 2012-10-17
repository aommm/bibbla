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

import java.util.Map;

import junit.framework.TestCase;

/**
 * Tests methods in ReserveJob, which are typically called hierarchically
 * through a single call to run().
 * @author arla
 */
public class ReserveJobTest extends TestCase {

	public void testRun() {
		fail("Not yet implemented");
	}

	public void testCreatePostData() {
		final String TEST_LIBRARY_CODE = "foo";
		Map<String,String> data = ReserveJob.createPostData(TEST_LIBRARY_CODE);
		assertTrue(data.size() == 4);
		assertEquals(data.get(ReserveJob.KEY_LIBCODE), TEST_LIBRARY_CODE);
		assertEquals(data.get(ReserveJob.KEY_YEAR), ReserveJob.VAL_YEAR);
		assertEquals(data.get(ReserveJob.KEY_MONTH), ReserveJob.VAL_MONTH);
		assertEquals(data.get(ReserveJob.KEY_DAY), ReserveJob.VAL_DAY);
	}

	public void testPostReservation() {
		fail("Not yet implemented");
	}

	public void testParseResults() {
		fail("Not yet implemented");
	}

	public void testFindDiv() {
		fail("Not yet implemented");
	}
}
