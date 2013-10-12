package com.intuso.housemate.web.client.bootstrap.widget.command;

import com.github.gwtbootstrap.client.ui.Modal;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.intuso.housemate.api.object.command.CommandListener;
import com.intuso.housemate.web.client.object.GWTProxyCommand;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 07/10/13
 * Time: 08:44
 * To change this template use File | Settings | File Templates.
 */
public class CommandPopup extends Composite implements CommandListener<GWTProxyCommand> {

    interface CommandPopupUiBinder extends UiBinder<SimplePanel, CommandPopup> {}

    private static CommandPopupUiBinder ourUiBinder = GWT.create(CommandPopupUiBinder.class);

    @UiField
    Modal modal;
    @UiField(provided = true)
    Command command;

    public CommandPopup(GWTProxyCommand command) {
        this.command = new Command(command);

        initWidget(ourUiBinder.createAndBindUi(this));

        command.addObjectListener(this);

        modal.setTitle(command.getName());
        modal.show();
    }

    @Override
    public void commandStarted(GWTProxyCommand command) {
        // do nothing
    }

    @Override
    public void commandFinished(GWTProxyCommand command) {
        modal.hide();
    }

    @Override
    public void commandFailed(GWTProxyCommand command, String error) {
        // do nothing
    }
}
