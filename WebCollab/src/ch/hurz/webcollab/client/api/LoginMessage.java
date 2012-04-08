package ch.hurz.webcollab.client.api;

import java.util.List;

public class LoginMessage implements Message {

	private static final long serialVersionUID = -1918145975421161185L;

	private String channelKey;
	private List<Message> messages;

	@SuppressWarnings("unused")
	private LoginMessage() {
		// Because of serializing
	}

	public LoginMessage(final String channelKey, final List<Message> messages) {
		this.channelKey = channelKey;
		this.messages = messages;
	}

	public String getChannelKey() {
		return channelKey;
	}

	public List<Message> getMessages() {
		return messages;
	}

}
