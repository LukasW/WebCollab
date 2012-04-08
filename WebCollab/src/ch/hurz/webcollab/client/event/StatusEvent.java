package ch.hurz.webcollab.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class StatusEvent extends GwtEvent<StatusEventHandler> {
	public static Type<StatusEventHandler> TYPE = new Type<StatusEventHandler>();

	public StatusEvent(final String string) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Type<StatusEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(final StatusEventHandler handler) {
		handler.onLogin(this);
	}
}
