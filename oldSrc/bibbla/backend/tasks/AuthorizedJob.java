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

import dat255.grupp06.bibbla.model.Credentials;
import dat255.grupp06.bibbla.model.CredentialsMissingException;
import dat255.grupp06.bibbla.utils.Message;
import dat255.grupp06.bibbla.utils.Session;

/**
 * Job classes that need the user to be logged in extend this class.
 * The constructor runs a LoginJob to ensure that every run of ancestor jobs
 * should be carried out with user logged-in.
 * @author arla
 */
public abstract class AuthorizedJob extends Job {
	
	protected final Credentials credentials;
	protected final Session session;
	
	/**
	 * Construct a job that requires being logged in.
	 * @param credentials Which credentials to use if not logged in.
	 * @param session cookies to detect whether logged in.
	 */
	public AuthorizedJob(Credentials credentials, Session session) {
		if (credentials == null);
		this.credentials = credentials;
		this.session = session;
	}
	
	/**
	 * Log in if needed (which is determined by the session object supplied in
	 * constructor). Should be called in ancestor's run() before
	 * login-dependent connections are made.
	 * @throws RuntimeException if login fails
	 * TODO Replace with something semantically correct
	 */
	public void login() {
		if (session.isActive())
			return;
		LoginJob loginJob = new LoginJob(credentials, session);
		Message message = loginJob.run();
		if (!session.isActive()) {
			throw new RuntimeException("Login failed.");
		}
	}
}