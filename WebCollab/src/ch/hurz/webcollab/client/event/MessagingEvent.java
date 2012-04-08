package ch.hurz.webcollab.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class MessagingEvent extends GwtEvent<MessagingEventHandler> {
	public static Type<MessagingEventHandler> TYPE = new Type<MessagingEventHandler>();

	@Override
	public Type<MessagingEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(final MessagingEventHandler handler) {
		handler.onMessage(this);
	}
}
