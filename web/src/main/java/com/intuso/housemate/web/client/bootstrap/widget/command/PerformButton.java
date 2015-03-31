package com.intuso.housemate.web.client.bootstrap.widget.command;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.event.PerformCommandEvent;
import com.intuso.housemate.web.client.object.GWTProxyCommand;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxyType;
import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.constants.IconType;

/**
 */
public class PerformButton extends Button implements ClickHandler {

    private GWTProxyList<TypeData<?>, GWTProxyType> types;
    private GWTProxyCommand command;
    private String text;
    private IconType icon;
    private TypeInstanceMap values;

    public PerformButton() {
        this(null, null, null, null, null);
    }

    public PerformButton(GWTProxyList<TypeData<?>, GWTProxyType> types, GWTProxyCommand command) {
        this(types, command, (TypeInstanceMap)null);
    }

    public PerformButton(GWTProxyList<TypeData<?>, GWTProxyType> types, GWTProxyCommand command, TypeInstanceMap values) {
        this();
        setCommand(types, command, values);
    }

    public PerformButton(GWTProxyList<TypeData<?>, GWTProxyType> types, GWTProxyCommand command, String text) {
        this(types, command, text, null);
    }

    public PerformButton(GWTProxyList<TypeData<?>, GWTProxyType> types, GWTProxyCommand command, String text, TypeInstanceMap values) {
        this(types, command, text, null, values);
    }

    public PerformButton(GWTProxyList<TypeData<?>, GWTProxyType> types, GWTProxyCommand command, IconType icon) {
        this(types, command, icon, null);
    }

    public PerformButton(GWTProxyList<TypeData<?>, GWTProxyType> types, GWTProxyCommand command, IconType icon, TypeInstanceMap values) {
        this(types, command, null, icon, values);
    }

    public PerformButton(GWTProxyList<TypeData<?>, GWTProxyType> types, GWTProxyCommand command, String text, IconType icon, TypeInstanceMap values) {
        addClickHandler(this);
        this.text = text;
        this.icon = icon;
        setCommand(types, command, values);
    }

    public void setCommand(GWTProxyList<TypeData<?>, GWTProxyType>types, GWTProxyCommand command) {
        setCommand(types, command, null);
    }

    public void setCommand(GWTProxyList<TypeData<?>, GWTProxyType> types, GWTProxyCommand command, TypeInstanceMap values) {
        this.types = types;
        this.command = command;
        this.values = values;
        if(icon != null)
            setIcon(icon);
        else {
            if(text != null)
                setText(text);
            else if(command != null)
                setText(command.getName());

            if(command != null && command.getParameters() != null && command.getParameters().size() > 0)
                    setIcon(IconType.CHEVRON_RIGHT);
        }
    }

    @Override
    public void setIcon(IconType icon) {
        this.icon = icon;
        super.setIcon(icon);
    }

    @Override
    public void setText(String text) {
        this.text = text;
        super.setText(text);
    }

    @Override
    public void onClick(ClickEvent clickEvent) {
        if(command != null) {
            if(values == null && command.getParameters() != null && command.getParameters().size() > 0) {
                new CommandModal(types, command);
            } else
                Housemate.INJECTOR.getEventBus().fireEvent(
                        new PerformCommandEvent(command, values != null ? values : new TypeInstanceMap()));
        }
    }
}
