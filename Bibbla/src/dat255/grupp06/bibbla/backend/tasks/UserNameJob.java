package dat255.grupp06.bibbla.backend.tasks;

import org.jsoup.Connection.Response;

import dat255.grupp06.bibbla.backend.login.Session;
import dat255.grupp06.bibbla.model.Credentials;
import dat255.grupp06.bibbla.utils.Message;

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
