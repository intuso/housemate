package com.intuso.housemate.web.client.bootstrap.widget.device;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.Collapse;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.web.client.bootstrap.widget.WidgetRow;
import com.intuso.housemate.web.client.bootstrap.widget.command.CommandList;
import com.intuso.housemate.web.client.bootstrap.widget.object.Control;
import com.intuso.housemate.web.client.bootstrap.widget.property.PropertyList;
import com.intuso.housemate.web.client.bootstrap.widget.value.ValueList;
import com.intuso.housemate.web.client.object.GWTProxyDevice;
import com.intuso.housemate.web.client.object.device.feature.GWTProxyFeature;

/**
 */
public class Device extends Composite {

    interface DeviceUiBinder extends UiBinder<Widget, Device> {}

    private static DeviceUiBinder ourUiBinder = GWT.create(DeviceUiBinder.class);

    @UiField
    FlowPanel featureWidgets;

    @UiField(provided = true)
    CommandList commandsList;
    @UiField(provided = true)
    ValueList valuesList;

    @UiField
    Button settings;
    @UiField
    Collapse settingsPanel;
    @UiField(provided = true)
    Control control;
    @UiField(provided = true)
    PropertyList propertiesList;

    public Device(final GWTProxyDevice device) {

        commandsList = new CommandList(device.getCommands(), "Commands", device.getCustomCommandIds(), false);
        valuesList = new ValueList(device.getValues(), "Values", device.getCustomValueIds(), false);
        control = new Control(device);
        propertiesList = new PropertyList(device.getProperties(), "Properties", device.getCustomPropertyIds(), false);

        initWidget(ourUiBinder.createAndBindUi(this));

        for(String featureId : device.getFeatureIds()) {
            GWTProxyFeature feature = device.getFeature(featureId);
            if(feature != null)
                featureWidgets.add(new WidgetRow(feature.getTitle(), feature.getWidget()));
        }
    }

    @UiHandler("settings")
    public void settingsClicked(ClickEvent event) {
        settings.setActive(!settings.isActive());
        if(settings.isActive())
            settingsPanel.show();
        else
            settingsPanel.hide();
    }
}
