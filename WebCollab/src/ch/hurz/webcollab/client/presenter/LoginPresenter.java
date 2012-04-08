package ch.hurz.webcollab.client.presenter;

import ch.hurz.webcollab.client.api.LoginFailureException;
import ch.hurz.webcollab.client.event.MessagingEvent;
import ch.hurz.webcollab.client.messaging.MessagingModel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class LoginPresenter implements Presenter {

	public interface Display {
		HasClickHandlers getLoginButton();

		HasValue<Boolean> getRememberCheckBox();

		HasValue<String> getNameTextBox();

		HasValue<String> getPasswordTextBox();

		HasText getErrorText();

		Widget asWidget();
	}

	private final HandlerManager eventBus;
	private final Display display;
	private final MessagingModel messagingModel;

	public LoginPresenter(final MessagingModel messagingModel,
			final HandlerManager eventBus, final Display view) {
		this.messagingModel = messagingModel;
		this.eventBus = eventBus;
		this.display = view;
		bind();
	}

	public void bind() {

		display.getLoginButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				try {
					messagingModel.login(display.getNameTextBox().getValue(),
							display.getPasswordTextBox().getValue());
					eventBus.fireEvent(new MessagingEvent());
				} catch (final LoginFailureException e) {
					display.getErrorText().setText(
							"Unknown User or Invalid Password!");
				}
			}
		});
	}

	@Override
	public void go(final HasWidgets container) {
		container.clear();
		container.add(display.asWidget());
	}

}
