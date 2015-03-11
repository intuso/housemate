package com.intuso.housemate.web.client.bootstrap.widget.object;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.intuso.housemate.api.object.Renameable;
import com.intuso.housemate.api.object.command.CommandListener;
import com.intuso.housemate.web.client.bootstrap.widget.command.CommandModal;
import com.intuso.housemate.web.client.object.GWTProxyCommand;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.constants.IconType;

public class RenameableWidget extends Button implements ClickHandler, CommandListener<GWTProxyCommand> {

    private Renameable<GWTProxyCommand> object;
    private GWTProxyCommand command;

    public RenameableWidget() {
        setIcon(IconType.EDIT);
        addClickHandler(this);
    }

    public RenameableWidget(Renameable<GWTProxyCommand> object) {
        this();
        setObject(object);
    }

    public void setObject(Renameable<GWTProxyCommand> object) {
        this.object = object;
        this.command = object.getRenameCommand();
        command.addObjectListener(this);
    }

    @Override
    public void onClick(ClickEvent clickEvent) {
        setEnabled(false);
        new CommandModal(command);
    }

    @Override
    public void commandEnabled(GWTProxyCommand command, boolean enabled) {
        // do nothing
    }

    @Override
    public void commandStarted(GWTProxyCommand command, String userId) {
        // do nothing
    }

    @Override
    public void commandFinished(GWTProxyCommand command) {
        setEnabled(true);
    }

    @Override
    public void commandFailed(GWTProxyCommand command, String error) {
        // todo notify the failure
        setEnabled(true);
    }
}
