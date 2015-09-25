package com.intuso.housemate.web.client.bootstrap.widget.command;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.intuso.housemate.comms.v1_0.api.payload.TypeData;
import com.intuso.housemate.web.client.object.GWTProxyCommand;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxyType;
import org.gwtbootstrap3.client.ui.Modal;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 07/10/13
 * Time: 08:44
 * To change this template use File | Settings | File Templates.
 */
public class CommandModal extends Composite implements com.intuso.housemate.object.v1_0.api.Command.PerformListener<GWTProxyCommand> {

    interface CommandModalUiBinder extends UiBinder<SimplePanel, CommandModal> {}

    private static CommandModalUiBinder ourUiBinder = GWT.create(CommandModalUiBinder.class);

    @UiField
    Modal modal;
    @UiField(provided = true)
    Command command;

    private final com.intuso.housemate.object.v1_0.api.Command.PerformListener<GWTProxyCommand> listener;

    public CommandModal(GWTProxyList<TypeData<?>, GWTProxyType> types, GWTProxyCommand command) {
        this(types, command, null);
    }

    public CommandModal(GWTProxyList<TypeData<?>, GWTProxyType> types, GWTProxyCommand command, com.intuso.housemate.object.v1_0.api.Command.PerformListener<GWTProxyCommand> listener) {

        this.command = new Command(types, command, this);
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
