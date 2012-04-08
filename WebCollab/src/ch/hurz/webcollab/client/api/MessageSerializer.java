package ch.hurz.webcollab.client.api;

import java.util.LinkedList;
import java.util.List;

public class MessageSerializer {

	public String serialize(final Message message) {
		final StringBuilder builder = new StringBuilder();
		serialize(message, builder);
		return builder.toString();
	}

	private void serialize(final Message message, final StringBuilder builder) {
		if (message instanceof TextMessage) {
			builder.append("\"TextMessage\":{");
			serializeTextMessage((TextMessage) message, builder);
		} else if (message instanceof StatusMessage) {
			builder.append("\"StatusMessage\":{");
			serializeStatusMessage((StatusMessage) message, builder);
		} else if (message instanceof LoginMessage) {
			builder.append("\"LoginMessage\":{");
			serializeLoginMessage((LoginMessage) message, builder);
		} else {
			throw new IllegalArgumentException("Unknown message type: "
					+ message.getClass().getName());
		}
		builder.append("}");
	}

	private void serializeTextMessage(final TextMessage message,
			final StringBuilder builder) {
		builder.append("\"sender\":").append(escape(message.getSender()));
		builder.append(",");
		builder.append("\"message\":").append(escape(message.getMessage()));
	}

	private void serializeStatusMessage(final StatusMessage message,
			final StringBuilder builder) {
		builder.append("\"statusMessage\":").append(
				escape(message.getStatusMessage()));

	}

	private void serializeLoginMessage(final LoginMessage message,
			final StringBuilder builder) {
		builder.append("\"channelKey\":").append(
				escape(message.getChannelKey()));
		builder.append(",");
		builder.append("\"messages\":{");
		boolean first = true;
		for (final Message msg : message.getMessages()) {
			if (first) {
				first = false;
			} else {
				builder.append(",");
			}
			serialize(msg, builder);
		}
		builder.append("}");
	}

	private String escape(final String message) {
		return "\"" + message.replace("\"", "\\\"") + "\"";
	}

	public Message deserialize(final String serializedMessage) {
		final StringBuilder builder = new StringBuilder(serializedMessage);
		return deserialize(builder);
	}

	private Message deserialize(final StringBuilder builder) {
		Message result = null;
		final String type = readValue(builder);
		eat(builder, ':');
		if ("LoginMessage".equals(type)) {
			result = deserializeLoginMessage(builder);
		} else if ("StatusMessage".equals(type)) {
			result = deserializeStatusMessage(builder);
		} else if ("TextMessage".equals(type)) {
			result = deserializeTextMessage(builder);
		}
		return result;
	}

	private Message deserializeLoginMessage(final StringBuilder builder) {
		eat(builder, '{');
		readValue(builder); // channelKey
		eat(builder, ':');
		final String channelKey = readValue(builder);
		eat(builder, ',');
		readValue(builder); // messages
		eat(builder, ':');
		final List<Message> messages = deserializeMessages(builder);
		eat(builder, '}');
		return new LoginMessage(channelKey, messages);
	}

	private List<Message> deserializeMessages(final StringBuilder builder) {
		final List<Message> result = new LinkedList<Message>();
		eat(builder, '{');
		while (builder.charAt(0) != '}') {
			if (builder.charAt(0) == ',') {
				builder.delete(0, 1);
			}
			result.add(deserialize(builder));
		}
		eat(builder, '}');
		return result;
	}

	private Message deserializeTextMessage(final StringBuilder builder) {
		eat(builder, '{');
		readValue(builder); // sender
		eat(builder, ':');
		final String sender = readValue(builder);
		eat(builder, ',');
		readValue(builder); // message
		eat(builder, ':');
		final String message = readValue(builder);
		eat(builder, '}');
		return new TextMessage(sender, message);
	}

	private Message deserializeStatusMessage(final StringBuilder builder) {
		eat(builder, '{');
		readValue(builder); // statusMessage
		eat(builder, ':');
		final String statusMessage = readValue(builder);
		eat(builder, '}');
		return new StatusMessage(statusMessage);
	}

	private String readValue(final StringBuilder sequence) {
		int i = 0;
		do {
			i = sequence.indexOf("\"", i + 1);
		} while (i != -1 && sequence.charAt(i - 1) == '\\');

		if (i == -1) {
			throw new IllegalArgumentException("Not valid: " + sequence);
		}
		final String result = sequence.substring(1, i);
		sequence.delete(0, i + 1);
		return unescape(result);
	}

	private void eat(final StringBuilder builder, final char c) {
		if (builder.charAt(0) != c) {
			throw new IllegalArgumentException("Expected '" + c + "' but was '"
					+ builder.charAt(0) + "'");
		}
		builder.delete(0, 1);
	}

	private String unescape(final String message) {
		final StringBuilder result = new StringBuilder(message);
		int i = -1;
		do {
			i = result.indexOf("\"", i + 1);
			if (i > 0 && result.charAt(i - 1) == '\\') {
				result.delete(i - 1, i);
				i = i - 1;
			}
		} while (i >= 0);
		return result.toString();
	}
}
