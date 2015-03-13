package com.intuso.housemate.web.client.bootstrap.widget.login;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.event.CredentialsSubmittedEvent;
import com.intuso.housemate.web.client.ui.view.LoginView;
import org.gwtbootstrap3.client.ui.Alert;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.Input;
import org.gwtbootstrap3.client.ui.Modal;

import static com.google.gwt.user.client.DOM.getElementById;

/**
 */
public class LoginModal extends Composite implements LoginView {

    interface LoginModalUIBinder extends UiBinder<SimplePanel, LoginModal> {}

    private static LoginModalUIBinder ourUiBinder = GWT.create(LoginModalUIBinder.class);

    @UiField
    Modal modal;
    @UiField
    Input username;
    @UiField
    Input password;
    @UiField
    Alert message;
    @UiField
    Button loginButton;

    public LoginModal() {
        initWidget(ourUiBinder.createAndBindUi(this));
        // load username and password "remembered" by browser
        if(getElementById("username") != null)
            username.setText(getElementById("username").getPropertyString("value"));
        if(getElementById("password") != null)
            password.setText(getElementById("password").getPropertyString("value"));
    }

    @UiHandler(value = {"username", "password"})
    protected void doEnter(KeyDownEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
            submitLogin();
        }
    }

    @UiHandler("loginButton")
    protected void doSubmit(ClickEvent event) {
        submitLogin();
    }

    protected void submitLogin() {
        username.setEnabled(false);
        password.setEnabled(false);
        Housemate.INJECTOR.getEventBus().fireEvent(new CredentialsSubmittedEvent(username.getText(), password.getText()));
    }

    private static native void click(Element submitButton)
    /*-{
        submitButton.click();
    }-*/;

    @Override
    public void show() {
        modal.show();
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            public void execute() {
                username.setFocus(true);
            }
        });
    }

    @Override
    public void hide() {
        // hide means we logged in, so save the username and password
        getElementById("username").setPropertyString("value", username.getText());
        getElementById("password").setPropertyString("value", password.getText());
        click(getElementById("login"));
        message.setVisible(false);
        modal.hide();
    }

    @Override
    public void enable() {
        username.setEnabled(true);
        password.setEnabled(true);
    }

    @Override
    public void disable() {
        username.setEnabled(false);
        password.setEnabled(false);
    }

    @Override
    public void setMessage(String message) {
        if(message != null) {
            this.message.setVisible(true);
            this.message.setText(message);
        } else
            this.message.setVisible(false);
    }
}