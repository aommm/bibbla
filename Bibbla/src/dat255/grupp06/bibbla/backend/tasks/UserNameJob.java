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

//Copyright 2012 Fahad Al-Khameesi, Madeleine Appert, Niklas Logren, Arild Matsson and Jonathan Orrö.