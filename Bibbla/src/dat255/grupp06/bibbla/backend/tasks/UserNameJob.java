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

import dat255.grupp06.bibbla.model.Credentials;
import dat255.grupp06.bibbla.utils.Message;
import dat255.grupp06.bibbla.utils.Session;

/**
 * Job to get user's last name.
 * @author arla
 *
 */
public class UserNameJob extends AuthorizedJob {
	
	public UserNameJob(Credentials credentials, Session session) {
		super(credentials, session);
	}
	
	public Message run() {
		login();
		Message message = new Message();
		message.obj = session.getName();
		return message;
	}

	@Override
	protected Response connect() throws Exception {
		return null;
	}

}