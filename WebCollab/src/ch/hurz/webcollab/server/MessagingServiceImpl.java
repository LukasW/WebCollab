package ch.hurz.webcollab.server;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import ch.hurz.webcollab.client.api.LoginFailureException;
import ch.hurz.webcollab.client.api.LoginMessage;
import ch.hurz.webcollab.client.api.Message;
import ch.hurz.webcollab.client.api.MessageSerializer;
import ch.hurz.webcollab.client.api.MessagingService;
import ch.hurz.webcollab.client.api.StatusMessage;

import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class MessagingServiceImpl extends RemoteServiceServlet implements
		MessagingService {

	private final Map<String, Date> clients = Maps.newHashMap();

	private final List<Message> messages = new LinkedList<Message>();

	@Override
	public LoginMessage login(final String name, final String password)
			throws LoginFailureException {
		if (!"hurz".equals(password)) {
			throw new LoginFailureException();
		}
		broadcast(name, new StatusMessage(name + " connected."));

		final ChannelService channelService = ChannelServiceFactory
				.getChannelService();
		final String channelKey = channelService.createChannel(name);

		final List<Message> messagesToSend = new LinkedList<Message>(messages);
		for (final String n : clients.keySet()) {
			messagesToSend.add(new StatusMessage(n + " is online"));
		}

		clients.put(name, new Date());
		return new LoginMessage(channelKey, messagesToSend);
	}

	@Override
	public void broadcast(final String clientName, final Message message) {
		updateClients();
		putMessage(message);
		for (final String token : clients.keySet()) {
			if (!token.equals(clientName)) {
				sendMessage(token, message);
			}
		}
	}

	@Override
	public void alive(final String clientName) {
		clients.put(clientName, new Date());
		updateClients();
	}

	private void putMessage(final Message message) {
		messages.add(message);
		while (messages.size() > 100) {
			messages.remove(messages.size() - 1);
		}
	}

	private void sendMessage(final String token, final Message message) {
		final ChannelService channelService = ChannelServiceFactory
				.getChannelService();
		channelService.sendMessage(new ChannelMessage(token,
				new MessageSerializer().serialize(message)));
	}

	private void updateClients() {
		final List<String> toBeRemoved = Lists.newLinkedList();
		for (final String token : clients.keySet()) {
			final long delta = new Date().getTime()
					- clients.get(token).getTime();
			if (delta > 5L * 60L * 1000L) {
				toBeRemoved.add(token);
			}
		}
		clients.keySet().removeAll(toBeRemoved);
		for (final String token : toBeRemoved) {
			broadcast(null, new StatusMessage(token + " disconnected."));
		}
	}
}
