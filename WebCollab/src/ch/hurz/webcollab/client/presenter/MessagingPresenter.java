package ch.hurz.webcollab.client.presenter;

import ch.hurz.webcollab.client.api.Message;
import ch.hurz.webcollab.client.messaging.MessagingModel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class MessagingPresenter implements Presenter {

	private final MessagingModel messagingModel;
	private final HandlerManager eventBus;
	private final Display display;

	public interface Display {
		HasClickHandlers getSendButton();

		HasHTML getMessageLog();

		HasText getMessage();

		Widget asWidget();
	}

	public MessagingPresenter(final MessagingModel messagingModel,
			final HandlerManager eventBus, final Display display) {
		this.messagingModel = messagingModel;
		this.eventBus = eventBus;
		this.display = display;
		bind();
	}

	public void bind() {
		display.getSendButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				messagingModel.broadcast(display.getMessage().getText());
				display.getMessage().setText("");
			}
		});
		messagingModel.addMessageListener(new MessageListener() {

			@Override
			public void onMessage() {
				updateMessages();
			}

		});

		updateMessages();
	}

	private void updateMessages() {
		final StringBuilder builder = new StringBuilder();
		for (final Message message : messagingModel.getMessages()) {
			builder.append(message.toString());
			builder.append("<br/>");

		}
		display.getMessageLog().setHTML(builder.toString());
	}

	@Override
	public void go(final HasWidgets container) {
		container.clear();
		container.add(display.asWidget());
	}
}
