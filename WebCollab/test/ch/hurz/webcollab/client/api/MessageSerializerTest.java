package ch.hurz.webcollab.client.api;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.junit.Test;

public class MessageSerializerTest {

	@Test
	public void serializeAndDeserializeMessages() {
		final LoginMessage loginMessage = new LoginMessage("\"", Arrays.asList(
				new TextMessage("gnu", "bla:bal"), new StatusMessage("hurz!")));

		final MessageSerializer testee = new MessageSerializer();
		final String serialize = testee.serialize(loginMessage);
		final Message message = testee.deserialize(serialize);

		final LoginMessage result = (LoginMessage) message;
		assertThat(result.getChannelKey(), is(equalTo("\"")));
	}
}
