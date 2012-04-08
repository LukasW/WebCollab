package ch.hurz.webcollab.client.view;

import ch.hurz.webcollab.client.presenter.MessagingPresenter;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MessagingView extends Composite implements
		MessagingPresenter.Display {

	private final HTML htmlMessageLog;
	private final Button btnSend;
	private final TextBox txtMessage;

	public MessagingView() {

		final VerticalPanel verticalPanel = new VerticalPanel();
		initWidget(verticalPanel);
		verticalPanel.setSize("428px", "343px");

		htmlMessageLog = new HTML("New HTML", true);
		verticalPanel.add(htmlMessageLog);
		htmlMessageLog.setSize("100%", "100%");

		final FlexTable flexTable = new FlexTable();
		verticalPanel.add(flexTable);
		verticalPanel.setCellVerticalAlignment(flexTable,
				HasVerticalAlignment.ALIGN_BOTTOM);
		flexTable.setWidth("100%");

		txtMessage = new TextBox();
		flexTable.setWidget(0, 0, txtMessage);
		flexTable.getCellFormatter().setWidth(0, 0, "85%");
		txtMessage.setWidth("100%");

		btnSend = new Button("New button");
		btnSend.setText("Send");
		flexTable.setWidget(0, 1, btnSend);
		flexTable.getCellFormatter().setHeight(0, 1, "");
		flexTable.getCellFormatter().setWidth(0, 1, "");
		flexTable.getCellFormatter().setHorizontalAlignment(0, 1,
				HasHorizontalAlignment.ALIGN_CENTER);
	}

	@Override
	public HasClickHandlers getSendButton() {
		return btnSend;
	}

	@Override
	public HasHTML getMessageLog() {
		return htmlMessageLog;
	}

	@Override
	public HasText getMessage() {
		return txtMessage;
	}
}
