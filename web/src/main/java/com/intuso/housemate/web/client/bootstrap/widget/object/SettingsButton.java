package com.intuso.housemate.web.client.bootstrap.widget.object;

import org.gwtbootstrap3.client.ui.Button;
import org.gwtbootstrap3.client.ui.constants.ButtonSize;
import org.gwtbootstrap3.client.ui.constants.IconType;

/**
 * Created by tomc on 10/03/15.
 */
public class SettingsButton extends Button {

    public SettingsButton() {
        addStyleName("settings");
        setSize(ButtonSize.EXTRA_SMALL);
        setIcon(IconType.COG);
    }
}
