package com.intuso.housemate.web.client.bootstrap.widget.object;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Collapse;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 12/10/13
 * Time: 17:37
 * To change this template use File | Settings | File Templates.
 */
public abstract class ConfigurableObject extends Composite {

    private Widget settingsWidget = null;

    @UiField
    public Button settingsButton;
    @UiField
    public Collapse settingsCollapse;
    @UiField
    public SimplePanel settingsPanel;

    @Override
    protected void initWidget(Widget widget) {
        super.initWidget(widget);
        settingsCollapse.setDefaultOpen(false);
        settingsButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                settingsButton.setActive(!settingsButton.isActive());

                // create the settings if we don't already have it
                if(settingsWidget == null) {
                    settingsWidget = createSettingsWidget();
                    settingsPanel.add(settingsWidget);
                }

                // toggle settings visibility
                settingsCollapse.toggle();
            }
        });
    }

    protected abstract Widget createSettingsWidget();
}
