package ch.hurz.webcollab.server;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.http.HttpSession;

import net.zschech.gwt.comet.server.CometServlet;
import net.zschech.gwt.comet.server.CometSession;
import ch.hurz.webcollab.client.api.LoginFailureException;
import ch.hurz.webcollab.client.api.LoginMessage;
import ch.hurz.webcollab.client.api.Message;
import ch.hurz.webcollab.client.api.MessagingService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class MessagingServiceImpl extends RemoteServiceServlet implements
		MessagingService {

	/**
	 * A mapping of user names to CometSessions used for routing messages.
	 */
	private final ConcurrentMap<String, CometSession> users = new ConcurrentHashMap<String, CometSession>();

	public String getUsername() {
		// check if there is a HTTP session setup.
		final HttpSession httpSession = getThreadLocalRequest().getSession(
				false);
		if (httpSession == null) {
			return null;
		}

		// return the user name
		return (String) httpSession.getAttribute("username");
	}

	@Override
	public LoginMessage login(final String name, final String password)
			throws LoginFailureException {
		// Get or create the HTTP session for the browser
		final HttpSession httpSession = getThreadLocalRequest().getSession();
		// Get or create the Comet session for the browser
		final CometSession cometSession = CometServlet
				.getCometSession(httpSession);
		// Remember the user name for the
		httpSession.setAttribute("username", name);

		// setup the mapping of user names to CometSessions
		if (users.putIfAbsent(name, cometSession) != null) {
			// some one else has already logged in with this user name
			httpSession.invalidate();
			throw new LoginFailureException("User: " + name
					+ " already logged in");
		}

		return new LoginMessage(null, null);
	}

	@Override
	public void broadcast(final String clientName, final Message message) {
		updateClients();
		putMessage(message);
		for (final String token : users.keySet()) {
			if (!token.equals(clientName)) {
				sendMessage(users.get(token), message);
			}
		}
	}

	private void sendMessage(final CometSession cometSession,
			final Message message) {
		cometSession.enqueue(message);
	}

	@Override
	public void alive(final String clientName) {
	}

	private void putMessage(final Message message) {
	}

	private void updateClients() {
	}
}
