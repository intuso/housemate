package com.intuso.housemate.web.client.bootstrap.widget.command;

import org.gwtbootstrap3.client.ui.Modal;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.intuso.housemate.api.object.command.CommandPerformListener;
import com.intuso.housemate.web.client.object.GWTProxyCommand;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 07/10/13
 * Time: 08:44
 * To change this template use File | Settings | File Templates.
 */
public class CommandPopup extends Composite implements CommandPerformListener<GWTProxyCommand> {

    interface CommandPopupUiBinder extends UiBinder<SimplePanel, CommandPopup> {}

    private static CommandPopupUiBinder ourUiBinder = GWT.create(CommandPopupUiBinder.class);

    @UiField
    Modal modal;
    @UiField(provided = true)
    Command command;

    private final CommandPerformListener<GWTProxyCommand> listener;

    public CommandPopup(GWTProxyCommand command) {
        this(command, null);
    }

    public CommandPopup(GWTProxyCommand command, CommandPerformListener<GWTProxyCommand> listener) {

        this.command = new Command(command, this);
        this.listener = listener;

        initWidget(ourUiBinder.createAndBindUi(this));

        modal.setTitle(command.getName());
        modal.show();
    }

    @Override
    public void commandStarted(GWTProxyCommand command) {
        if(listener != null)
            listener.commandStarted(command);
    }

    @Override
    public void commandFinished(GWTProxyCommand command) {
        modal.hide();
        if(listener != null)
            listener.commandFinished(command);
    }

    @Override
    public void commandFailed(GWTProxyCommand command, String error) {
        if(listener != null)
            listener.commandFailed(command, error);
    }
}
