package ch.hurz.webcollab.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface StatusEventHandler extends EventHandler {
	void onLogin(StatusEvent event);
}
