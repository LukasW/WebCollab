package ch.hurz.webcollab.client.api;

import java.io.Serializable;

public class LoginFailureException extends Exception implements Serializable {

	private static final long serialVersionUID = 1L;

	protected LoginFailureException() {

	}

	public LoginFailureException(final String message) {
		super(message);
	}

}
