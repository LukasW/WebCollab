package com.google.gwt.sample.contacts.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.RootPanel;

public class Contacts implements EntryPoint {

	@Override
	public void onModuleLoad() {
		final ContactsServiceAsync rpcService = GWT
				.create(ContactsService.class);
		final HandlerManager eventBus = new HandlerManager(null);
		final AppController appViewer = new AppController(rpcService, eventBus);
		appViewer.go(RootPanel.get());
	}
}
