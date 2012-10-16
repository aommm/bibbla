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

package dat255.grupp06.bibbla;

import dat255.grupp06.bibbla.backend.Session;
import dat255.grupp06.bibbla.utils.PrivateCredentials;

public class SampleSession {

	// TODO: Turn into Singleton
	public static Session getSession() {
		// Note:
		// PrivateCredentials (used below) is an unpublished class in the tasks-package.
		// Is for debugging purposes. Create your own!
		
		// Create a new Session, which keeps track of our session cookies.
		return new Session(PrivateCredentials.name, PrivateCredentials.code, PrivateCredentials.pin);
	}

}
