package ch.hurz.webcollab.client;

import ch.hurz.webcollab.client.event.LoginEvent;
import ch.hurz.webcollab.client.event.LoginEventHandler;
import ch.hurz.webcollab.client.event.MessagingEvent;
import ch.hurz.webcollab.client.event.MessagingEventHandler;
import ch.hurz.webcollab.client.messaging.MessagingModel;
import ch.hurz.webcollab.client.presenter.LoginPresenter;
import ch.hurz.webcollab.client.presenter.MessagingPresenter;
import ch.hurz.webcollab.client.presenter.Presenter;
import ch.hurz.webcollab.client.view.LoginView;
import ch.hurz.webcollab.client.view.MessagingView;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;

public class AppController implements Presenter, ValueChangeHandler<String> {
	private final HandlerManager eventBus;
	private final MessagingModel messagingModel;
	private HasWidgets container;

	public AppController(final MessagingModel messagingModel,
			final HandlerManager eventBus) {
		this.eventBus = eventBus;
		this.messagingModel = messagingModel;
		bind();
	}

	private void bind() {
		History.addValueChangeHandler(this);

		eventBus.addHandler(LoginEvent.TYPE, new LoginEventHandler() {

			@Override
			public void onLogin(final LoginEvent event) {
				doLogin();
			}
		});
		eventBus.addHandler(MessagingEvent.TYPE, new MessagingEventHandler() {
			@Override
			public void onMessage(final MessagingEvent event) {
				doMessaging();
			}
		});
	}

	private void doMessaging() {
		History.newItem("messaging", false);
		final Presenter presenter = new MessagingPresenter(messagingModel,
				eventBus, new MessagingView());
		presenter.go(container);
	}

	private void doLogin() {
		History.newItem("login", false);
		final Presenter presenter = new LoginPresenter(messagingModel,
				eventBus, new LoginView());
		presenter.go(container);
	}

	@Override
	public void go(final HasWidgets container) {
		this.container = container;

		if ("".equals(History.getToken())) {
			History.newItem("login");
		} else {
			History.fireCurrentHistoryState();
		}
	}

	@Override
	public void onValueChange(final ValueChangeEvent<String> event) {
		final String token = event.getValue();

		if (token != null) {
			Presenter presenter = null;

			if (token.equals("login")) {
				presenter = new LoginPresenter(messagingModel, eventBus,
						new LoginView());
			} else if (token.equals("messaging")) {
				presenter = new MessagingPresenter(messagingModel, eventBus,
						new MessagingView());
			}

			if (presenter != null) {
				presenter.go(container);
			}
		}
	}
}
