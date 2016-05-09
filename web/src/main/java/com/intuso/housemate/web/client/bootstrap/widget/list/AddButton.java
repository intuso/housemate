package com.intuso.housemate.web.client.bootstrap.widget.list;

import com.intuso.housemate.client.v1_0.api.object.Type;
import com.intuso.housemate.web.client.bootstrap.widget.command.PerformButton;
import com.intuso.housemate.web.client.object.GWTProxyCommand;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxyType;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.IconType;

/**
 * Created by tomc on 09/03/15.
 */
public class AddButton extends PerformButton {

    public AddButton() {
        addStyleName("add");
        setIcon(IconType.PLUS);
        setSize(ButtonSize.EXTRA_SMALL);
    }

    public AddButton(GWTProxyList<Type.Data<?>, GWTProxyType> types, GWTProxyCommand command) {
        this();
        setCommand(types, command);
    }
}
