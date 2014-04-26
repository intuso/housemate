package com.intuso.housemate.web.client.bootstrap.widget.property;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.object.proxy.LoadManager;
import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.bootstrap.widget.type.TypeInputList;
import com.intuso.housemate.web.client.event.PerformCommandEvent;
import com.intuso.housemate.web.client.object.GWTProxyProperty;
import com.intuso.housemate.web.client.object.GWTProxyType;

/**
 */
public class Property extends Composite {

    interface PropertyUiBinder extends UiBinder<FlowPanel, Property> {}

    private static PropertyUiBinder ourUiBinder = GWT.create(PropertyUiBinder.class);

    @UiField
    SimplePanel typeContainer;

    private final TypeInstanceMap values;

    private GWTProxyProperty property;

    public Property(final GWTProxyProperty property) {

        this.property = property;

        initWidget(ourUiBinder.createAndBindUi(this));

        values = new TypeInstanceMap();
        if(property.getTypeInstances() != null)
            values.put(com.intuso.housemate.api.object.property.Property.VALUE_PARAM, property.getTypeInstances());
        else
            values.put(com.intuso.housemate.api.object.property.Property.VALUE_PARAM, new TypeInstances());

        final GWTProxyType type = property.getType();
        if(type != null)
            typeContainer.add(TypeInputList.getInput(type, values.get(com.intuso.housemate.api.object.property.Property.VALUE_PARAM)));
        else {
            Housemate.INJECTOR.getProxyRoot().getTypes().load(new LoadManager(new LoadManager.Callback() {
                @Override
                public void failed(HousemateObject.TreeLoadInfo path) {
                    // todo show error
                }

                @Override
                public void allLoaded() {
                    GWTProxyType loadedType = property.getType();
                    if(loadedType != null)
                        typeContainer.setWidget(TypeInputList.getInput(loadedType, values.get(com.intuso.housemate.api.object.property.Property.VALUE_PARAM)));
                }
            }, "loadPropertyType-" + property.getId(), new HousemateObject.TreeLoadInfo(property.getTypeId(), new HousemateObject.TreeLoadInfo(HousemateObject.EVERYTHING_RECURSIVE))));
        }
    }

    @UiFactory
    protected Property createDialog() {
        return this;
    }

    @UiHandler("updateButton")
    protected void onPerform(ClickEvent event) {
        Housemate.INJECTOR.getEventBus().fireEvent(new PerformCommandEvent(property.getSetCommand(), values));
    }
}