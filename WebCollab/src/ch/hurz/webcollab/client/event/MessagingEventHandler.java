package ch.hurz.webcollab.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface MessagingEventHandler extends EventHandler {
	void onMessage(MessagingEvent event);
}
