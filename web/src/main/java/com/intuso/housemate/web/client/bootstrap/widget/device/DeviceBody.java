package com.intuso.housemate.web.client.bootstrap.widget.device;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.web.client.bootstrap.widget.NestedTable;
import com.intuso.housemate.web.client.bootstrap.widget.command.CommandList;
import com.intuso.housemate.web.client.bootstrap.widget.value.ValueList;
import com.intuso.housemate.web.client.object.GWTProxyDevice;
import com.intuso.housemate.web.client.object.device.feature.GWTProxyFeature;

/**
 */
public class DeviceBody extends Composite {

    interface DeviceUiBinder extends UiBinder<Widget, DeviceBody> {}

    private static DeviceUiBinder ourUiBinder = GWT.create(DeviceUiBinder.class);

    @UiField
    NestedTable featuresTable;
    @UiField
    CommandList commandsList;
    @UiField
    ValueList valuesList;

    public DeviceBody(final GWTProxyDevice device) {
        initWidget(ourUiBinder.createAndBindUi(this));
        commandsList.filter(device.getCustomCommandIds(), true);
        commandsList.setList(device.getCommands());
        valuesList.filter(device.getCustomValueIds(), true);
        valuesList.setList(device.getValues());
        for(String featureId : device.getFeatureIds()) {
            GWTProxyFeature feature = device.getFeature(featureId);
            if(feature != null)
                featuresTable.addRow(feature.getTitle(), feature.getWidget());
        }
    }
}
