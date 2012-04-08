package ch.hurz.webcollab.client.messaging;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import ch.hurz.webcollab.client.api.LoginFailureException;
import ch.hurz.webcollab.client.api.LoginMessage;
import ch.hurz.webcollab.client.api.Message;
import ch.hurz.webcollab.client.api.MessageSerializer;
import ch.hurz.webcollab.client.api.MessagingService;
import ch.hurz.webcollab.client.api.MessagingServiceAsync;
import ch.hurz.webcollab.client.api.TextMessage;
import ch.hurz.webcollab.client.event.StatusEvent;
import ch.hurz.webcollab.client.presenter.MessageListener;

import com.google.gwt.appengine.channel.client.Channel;
import com.google.gwt.appengine.channel.client.ChannelFactory;
import com.google.gwt.appengine.channel.client.ChannelFactory.ChannelCreatedCallback;
import com.google.gwt.appengine.channel.client.SocketError;
import com.google.gwt.appengine.channel.client.SocketListener;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class MessagingModel {

	private final MessagingServiceAsync greetingService = GWT
			.create(MessagingService.class);

	private String clientToken;
	private String name;
	private String password;

	private final HandlerManager eventBus;

	private final Set<MessageListener> messageListeners = new HashSet<MessageListener>();

	private final List<Message> messages = new LinkedList<Message>();

	public MessagingModel(final HandlerManager eventBus) {
		this.eventBus = eventBus;
	}

	public void login(final String name, final String password)
			throws LoginFailureException {
		this.name = name;
		this.password = password;
		greetingService.login(name, password,
				new AsyncCallback<LoginMessage>() {

					@Override
					public void onSuccess(final LoginMessage result) {
						clientToken = result.getChannelKey();
						messages.addAll(result.getMessages());
						fireMessagesChanged();
						openChannel();
					}

					@Override
					public void onFailure(final Throwable caught) {
						Window.alert("Communication error.");
					}
				});
		scheduleKeepAlive();
	}

	private void scheduleKeepAlive() {
		new Timer() {
			@Override
			public void run() {
				greetingService.alive(name, new AsyncCallback<Void>() {

					@Override
					public void onSuccess(final Void result) {
						// Nothing
					}

					@Override
					public void onFailure(final Throwable caught) {
						// Nothing
					}
				});
				scheduleKeepAlive();
			}
		}.schedule(2 * 60 * 1000);

	}

	public void broadcast(final String message) {
		messages.add(new TextMessage("me", message));
		fireMessagesChanged();
		greetingService.broadcast(name, new TextMessage(name, message),
				new AsyncCallback<Void>() {

					@Override
					public void onFailure(final Throwable caught) {
						Window.alert("Communication error : "
								+ caught.getMessage());
					}

					@Override
					public void onSuccess(final Void result) {
						// Everything is fine.
					}
				});

	}

	protected void openChannel() {
		ChannelFactory.createChannel(clientToken, new ChannelCreatedCallback() {

			@Override
			public void onChannelCreated(final Channel channel) {
				channel.open(new SocketListener() {
					@Override
					public void onOpen() {
						// Show status
					}

					@Override
					public void onMessage(final String serializedMessage) {
						final Message message = new MessageSerializer()
								.deserialize(serializedMessage);
						messages.add(message);
						fireMessagesChanged();
					}

					@Override
					public void onError(final SocketError error) {
						reconnect();
					}

					@Override
					public void onClose() {
						reconnect();
					}
				});
			}
		});
	}

	private void reconnect() {
		new Timer() {
			@Override
			public void run() {
				try {
					login(name, password);
				} catch (final LoginFailureException e) {
					eventBus.fireEvent(new StatusEvent("Reconnecting failed: "
							+ e.getMessage()));
				}
			}
		}.schedule(30 * 1000);
	}

	public void addMessageListener(final MessageListener messageListener) {
		messageListeners.add(messageListener);
	}

	private void fireMessagesChanged() {
		for (final MessageListener messageListener : messageListeners) {
			messageListener.onMessage();
		}
	}

	public List<Message> getMessages() {
		return messages;
	}
}
