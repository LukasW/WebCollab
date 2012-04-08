package ch.hurz.webcollab.client.api;

public class StatusMessage implements Message {

	private static final long serialVersionUID = 1L;

	private String statusMessage;

	@SuppressWarnings("unused")
	private StatusMessage() {
		// Because of serializing
	}

	public StatusMessage(final String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	@Override
	public String toString() {
		return "Status: " + statusMessage;
	}

}
