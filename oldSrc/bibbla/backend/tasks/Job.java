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

import org.jsoup.Connection.Response;

import dat255.grupp06.bibbla.backend.Backend;

/**
 * An abstract class meant to be a superclass for all jobs
 * which performs network-related tasks (= all of them).
 * 
 * Usage:
 * 1. Override connect()-method and fill with your network code.
 * 2. Call connectAndRetry() when you want to execute it; this method
 *    tries connect() several times, until failure threshold is reached.
 *  
 *  Number of times to retry is defined in Backend.MAX_CONNECTION_ATTEMPTS.
 *  
 * @author Niklas Logren
 */
public abstract class Job {

	/**
	 * Tries to perform this Job's network connection job,
	 * and retries a specified number of times if it fails.
	 * The number of retries is specified in Backend.MAX_CONNECTION_ATTEMTS.
	 * 
	 * @returns the Response object retrieved by the network connection. May be null.
	 * @throws Exception if connection failed.
	 */
	protected Response connectAndRetry() throws Exception {
		Response response = null;
		// Retry network connection a specified number of times.
		int failureCounter = 0;
		while(true) {
			try {					
				response = connect();
				System.out.print("succeeded! *\n");
				break; // Break if we succeed.
			} catch (Exception e) {
				failureCounter++;
			}
			// If max attempts has been reached, abort Job.
			if (failureCounter > Backend.MAX_CONNECTION_ATTEMPTS) {
				throw new Exception("Network connection failed.");
			} else {
				System.out.print("failed. retrying... ");
			}
		}
		return response;
	}
	
	/**
	 * The network code which is executed.
	 * @returns the network Response object. May be null.
	 * @throws Exception if network connection failed.
	 */
	protected abstract Response connect() throws Exception;
	
}
