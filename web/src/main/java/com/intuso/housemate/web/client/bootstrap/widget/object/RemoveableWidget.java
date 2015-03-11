package com.intuso.housemate.web.client.bootstrap.widget.object;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.intuso.housemate.api.object.Removeable;
import com.intuso.housemate.api.object.command.CommandPerformListener;
import com.intuso.housemate.web.client.object.GWTProxyCommand;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.constants.IconType;

public class RemoveableWidget extends Button implements ClickHandler {

    private Removeable<GWTProxyCommand> object;

    public RemoveableWidget() {
        setIcon(IconType.REMOVE);
        addClickHandler(this);
    }

    public RemoveableWidget(Removeable<GWTProxyCommand> object) {
        this();
        setObject(object);
    }

    public void setObject(Removeable<GWTProxyCommand> object) {
        this.object = object;
    }

    @Override
    public void onClick(ClickEvent clickEvent) {
        setEnabled(false);
        object.getRemoveCommand().perform(new CommandPerformListener<GWTProxyCommand>() {
            @Override
            public void commandStarted(GWTProxyCommand command) {
                // do nothing
            }

            @Override
            public void commandFinished(GWTProxyCommand command) {
                // do nothing, object is now removed
            }

            @Override
            public void commandFailed(GWTProxyCommand command, String error) {
                // todo notify the failure
                setEnabled(true);
            }
        });
    }
}
