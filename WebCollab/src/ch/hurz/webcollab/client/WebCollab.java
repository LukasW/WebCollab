package ch.hurz.webcollab.client;

import ch.hurz.webcollab.client.messaging.MessagingModel;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class WebCollab implements EntryPoint {

	/**
	 * This is the entry point method.
	 */
	@Override
	public void onModuleLoad() {
		final HandlerManager eventBus = new HandlerManager(null);
		final MessagingModel messagingModel = new MessagingModel(eventBus);
		final AppController appViewer = new AppController(messagingModel,
				eventBus);
		appViewer.go(RootPanel.get());

	}
}
