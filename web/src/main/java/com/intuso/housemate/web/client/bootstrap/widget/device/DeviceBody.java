package com.intuso.housemate.web.client.bootstrap.widget.device;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.comms.v1_0.api.payload.TypeData;
import com.intuso.housemate.web.client.bootstrap.widget.NestedTable;
import com.intuso.housemate.web.client.bootstrap.widget.feature.FeatureWidget;
import com.intuso.housemate.web.client.object.GWTProxyDevice;
import com.intuso.housemate.web.client.object.GWTProxyFeature;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxyType;

/**
 */
public class DeviceBody extends Composite {

    interface DeviceUiBinder extends UiBinder<Widget, DeviceBody> {}

    private static DeviceUiBinder ourUiBinder = GWT.create(DeviceUiBinder.class);

    @UiField
    NestedTable featuresTable;

    public DeviceBody(GWTProxyList<TypeData<?>, GWTProxyType> types, final GWTProxyDevice device) {
        initWidget(ourUiBinder.createAndBindUi(this));
        for(GWTProxyFeature feature : device.getFeatures()) {
            FeatureWidget featureWidget = FeatureWidget.FACTORY.getFeatureAs(feature);
            featureWidget.setTypes(types);
            featuresTable.addRow(feature.getName(), featureWidget);
        }
    }
}
