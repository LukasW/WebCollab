package ch.hurz.webcollab.client.api;

public class TextMessage implements Message {

	private static final long serialVersionUID = 1L;

	private String message;

	private String sender;

	@SuppressWarnings("unused")
	private TextMessage() {
		// Because of serializing
	}

	public TextMessage(final String sender, final String message) {
		this.sender = sender;
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public String getSender() {
		return sender;
	}

	@Override
	public String toString() {
		return sender + ": " + message;
	}
}
