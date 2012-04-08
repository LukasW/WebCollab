package ch.hurz.webcollab.client.view;

import ch.hurz.webcollab.client.presenter.LoginPresenter;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class LoginView extends Composite implements LoginPresenter.Display {

	private final TextBox txtUsername;
	private final PasswordTextBox txtPassword;
	private final CheckBox cbRemember;
	private final Button btnLogin;
	private final Label lblErrorMessage;

	public LoginView() {

		final VerticalPanel verticalPanel = new VerticalPanel();
		initWidget(verticalPanel);
		verticalPanel.setSize("300px", "202px");

		final Label lblTitle = new Label("Sign in to your account");
		lblTitle.setStyleName("gwt-Label-Login");
		verticalPanel.add(lblTitle);

		lblErrorMessage = new Label("");
		lblErrorMessage.setStyleName("gwt-Login-Error");
		verticalPanel.add(lblErrorMessage);

		final FlexTable flexTable = new FlexTable();
		verticalPanel.add(flexTable);

		final Label lblUser = new Label("Username:");
		lblUser.setStyleName("gwt-Label-Login");
		flexTable.setWidget(1, 0, lblUser);

		txtUsername = new TextBox();
		flexTable.setWidget(1, 1, txtUsername);

		final Label lblPassword = new Label("Password:");
		lblPassword.setStyleName("gwt-Label-Login");
		flexTable.setWidget(2, 0, lblPassword);

		txtPassword = new PasswordTextBox();
		flexTable.setWidget(2, 1, txtPassword);

		cbRemember = new CheckBox("Remember me on this computer");
		flexTable.setWidget(3, 1, cbRemember);

		btnLogin = new Button("Sign In");
		flexTable.setWidget(4, 1, btnLogin);
	}

	@Override
	public HasClickHandlers getLoginButton() {
		return btnLogin;
	}

	@Override
	public HasValue<Boolean> getRememberCheckBox() {
		return cbRemember;
	}

	@Override
	public HasValue<String> getNameTextBox() {
		return txtUsername;
	}

	@Override
	public HasValue<String> getPasswordTextBox() {
		return txtPassword;
	}

	@Override
	public HasText getErrorText() {
		return lblErrorMessage;
	}

}
