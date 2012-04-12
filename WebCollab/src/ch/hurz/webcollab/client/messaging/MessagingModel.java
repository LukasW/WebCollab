package ch.hurz.webcollab.client.messaging;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.zschech.gwt.comet.client.CometClient;
import net.zschech.gwt.comet.client.CometListener;
import net.zschech.gwt.comet.client.CometSerializer;
import net.zschech.gwt.comet.client.SerialTypes;
import ch.hurz.webcollab.client.api.LoginFailureException;
import ch.hurz.webcollab.client.api.LoginMessage;
import ch.hurz.webcollab.client.api.Message;
import ch.hurz.webcollab.client.api.MessagingService;
import ch.hurz.webcollab.client.api.MessagingServiceAsync;
import ch.hurz.webcollab.client.api.StatusMessage;
import ch.hurz.webcollab.client.api.TextMessage;
import ch.hurz.webcollab.client.event.StatusEvent;
import ch.hurz.webcollab.client.presenter.MessageListener;

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

	private CometClient cometClient;

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

	@SerialTypes({ StatusMessage.class, TextMessage.class })
	public static abstract class ChatCometSerializer extends CometSerializer {
	}

	protected void openChannel() {
		final CometSerializer serializer = GWT
				.create(ChatCometSerializer.class);
		cometClient = new CometClient(GWT.getModuleBaseURL() + "comet",
				serializer, new CometListener() {
					@Override
					public void onConnected(final int heartbeat) {
						output("connected " + heartbeat, "silver");
					}

					private void output(final String string,
							final String string2) {
						messages.add(new StatusMessage("[" + string2 + "] "
								+ string));
						fireMessagesChanged();
					}

					@Override
					public void onDisconnected() {
						output("disconnected", "silver");
					}

					@Override
					public void onError(final Throwable exception,
							final boolean connected) {
						output("error " + connected + " " + exception, "red");
					}

					@Override
					public void onHeartbeat() {
						output("heartbeat", "silver");
					}

					@Override
					public void onRefresh() {
						output("refresh", "silver");
					}

					@Override
					public void onMessage(
							final List<? extends Serializable> messages) {
						for (final Serializable message : messages) {
							if (message instanceof Message) {
								final Message chatMessage = (Message) message;
								MessagingModel.this.messages.add(chatMessage);
								fireMessagesChanged();
							} else {
								output("unrecognised message " + message, "red");
							}
						}
					}
				});
		cometClient.start();
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
