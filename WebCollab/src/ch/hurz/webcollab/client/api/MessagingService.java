package ch.hurz.webcollab.client.api;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * Client interface to the collab server.
 */
@RemoteServiceRelativePath("messaging")
public interface MessagingService extends RemoteService {
	/**
	 * Tries to login and returns a security token if successful.
	 */
	LoginMessage login(String name, String password)
			throws LoginFailureException;

	/**
	 * Broadcasts a message.
	 */
	void broadcast(String name, Message message);

	/**
	 * Sends an alive message.
	 */
	void alive(String name);
}
