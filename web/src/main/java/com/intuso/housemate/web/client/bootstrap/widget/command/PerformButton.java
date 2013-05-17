package com.intuso.housemate.web.client.bootstrap.widget.command;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Icon;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.event.PerformCommandEvent;
import com.intuso.housemate.web.client.object.GWTProxyCommand;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 28/11/12
 * Time: 09:03
 * To change this template use File | Settings | File Templates.
 */
public class PerformButton extends Button implements ClickHandler {

    private GWTProxyCommand command;
    private Map<String, String> values;

    public PerformButton() {
        addClickHandler(this);
    }

    public PerformButton(GWTProxyCommand command) {
        this(command, (Map)null);
    }

    public PerformButton(GWTProxyCommand command, Map<String, String> values) {
        this();
        setCommand(command, values);
    }

    public PerformButton(GWTProxyCommand command, String text) {
        this(command, null, text);
    }

    public PerformButton(GWTProxyCommand command, Map<String, String> values, String text) {
        this();
        setText(text);
        setCommand(command, values);
    }

    public PerformButton(GWTProxyCommand command, Icon icon) {
        this(command, null, icon);
    }

    public PerformButton(GWTProxyCommand command, Map<String, String> values, Icon icon) {
        this();
        add(icon);
        setCommand(command, values);
    }

    public void setCommand(GWTProxyCommand command) {
        setCommand(command, null);
    }

    public void setCommand(GWTProxyCommand command, Map<String, String> values) {
        this.command = command;
        this.values = values;
        if(command.getArguments().size() > 0)
            setIcon(IconType.CHEVRON_RIGHT);
    }

    @Override
    public void onClick(ClickEvent clickEvent) {
        if(command != null) {
            if(values == null && command.getArguments().size() > 0) {
                CommandPopup popup = new CommandPopup(command);
                popup.show();
            } else
                Housemate.FACTORY.getEventBus().fireEvent(new PerformCommandEvent(command, values != null ? values : new HashMap<String, String>()));
        }
    }
}
