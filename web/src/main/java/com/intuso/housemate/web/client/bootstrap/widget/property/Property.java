package com.intuso.housemate.web.client.bootstrap.widget.property;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.bootstrap.widget.type.TypeInput;
import com.intuso.housemate.web.client.bootstrap.widget.type.TypeInputList;
import com.intuso.housemate.web.client.event.PerformCommandEvent;
import com.intuso.housemate.web.client.object.GWTProxyProperty;

/**
 */
public class Property extends Composite {

    interface PropertyUiBinder extends UiBinder<FlowPanel, Property> {}

    private static PropertyUiBinder ourUiBinder = GWT.create(PropertyUiBinder.class);

    @UiField(provided = true)
    TypeInput typeInput;

    private final TypeInstanceMap values;

    private GWTProxyProperty property;

    public Property(GWTProxyProperty property) {

        this.property = property;

        values = new TypeInstanceMap();
        if(property.getTypeInstances() != null)
            values.put(com.intuso.housemate.api.object.property.Property.VALUE_PARAM, property.getTypeInstances());
        else
            values.put(com.intuso.housemate.api.object.property.Property.VALUE_PARAM, new TypeInstances());

        typeInput = TypeInputList.getInput(property.getType(), values.get(com.intuso.housemate.api.object.property.Property.VALUE_PARAM));

        initWidget(ourUiBinder.createAndBindUi(this));
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