package com.intuso.housemate.web.client.bootstrap.widget.command;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Icon;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.event.PerformCommandEvent;
import com.intuso.housemate.web.client.object.GWTProxyCommand;

/**
 */
public class PerformButton extends Button implements ClickHandler {

    private GWTProxyCommand command;
    private TypeInstances values;

    public PerformButton() {
        addClickHandler(this);
    }

    public PerformButton(GWTProxyCommand command) {
        this(command, (TypeInstances)null);
    }

    public PerformButton(GWTProxyCommand command, TypeInstances values) {
        this();
        setCommand(command, values);
    }

    public PerformButton(GWTProxyCommand command, String text) {
        this(command, null, text);
    }

    public PerformButton(GWTProxyCommand command, TypeInstances values, String text) {
        this();
        setText(text);
        setCommand(command, values);
    }

    public PerformButton(GWTProxyCommand command, Icon icon) {
        this(command, null, icon);
    }

    public PerformButton(GWTProxyCommand command, TypeInstances values, Icon icon) {
        this();
        add(icon);
        setCommand(command, values);
    }

    public void setCommand(GWTProxyCommand command) {
        setCommand(command, null);
    }

    public void setCommand(GWTProxyCommand command, TypeInstances values) {
        this.command = command;
        this.values = values;
        if(command.getParameters().size() > 0)
            setIcon(IconType.CHEVRON_RIGHT);
    }

    @Override
    public void onClick(ClickEvent clickEvent) {
        if(command != null) {
            if(values == null && command.getParameters().size() > 0) {
                CommandPopup popup = new CommandPopup(command);
                popup.show();
            } else
                Housemate.FACTORY.getEventBus().fireEvent(
                        new PerformCommandEvent(command, values != null ? values : new TypeInstances()));
        }
    }
}
