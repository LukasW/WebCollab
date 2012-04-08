package ch.hurz.webcollab.client.api;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface MessagingServiceAsync {

	void broadcast(String sendersClientToken, Message message,
			AsyncCallback<Void> callback);

	void login(String name, String password,
			AsyncCallback<LoginMessage> callback);

	void alive(String name, AsyncCallback<Void> callback);

}
