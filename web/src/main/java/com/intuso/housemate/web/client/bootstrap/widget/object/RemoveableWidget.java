package com.intuso.housemate.web.client.bootstrap.widget.object;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.intuso.housemate.client.v1_0.proxy.api.ProxyRemoveable;
import com.intuso.housemate.comms.v1_0.api.payload.TypeData;
import com.intuso.housemate.object.v1_0.api.Command;
import com.intuso.housemate.web.client.bootstrap.widget.command.CommandModal;
import com.intuso.housemate.web.client.object.GWTProxyCommand;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxyType;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.constants.IconType;

public class RemoveableWidget extends Button implements ClickHandler, Command.Listener<GWTProxyCommand> {

    private GWTProxyList<TypeData<?>, GWTProxyType> types;
    private GWTProxyCommand command;

    public RemoveableWidget() {
        setIcon(IconType.REMOVE);
        addClickHandler(this);
    }

    public RemoveableWidget(GWTProxyList<TypeData<?>, GWTProxyType> types, ProxyRemoveable<GWTProxyCommand> object) {
        this();
        setObject(types, object);
    }

    public void setObject(GWTProxyList<TypeData<?>, GWTProxyType> types, ProxyRemoveable<GWTProxyCommand> object) {
        this.types = types;
        this.command = object.getRemoveCommand();
        command.addObjectListener(this);
    }

    @Override
    public void onClick(ClickEvent clickEvent) {
        setEnabled(false);
        new CommandModal(types, command);
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
