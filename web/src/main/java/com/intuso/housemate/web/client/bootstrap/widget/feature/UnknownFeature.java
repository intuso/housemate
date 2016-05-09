package com.intuso.housemate.web.client.bootstrap.widget.feature;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.client.v1_0.api.object.Type;
import com.intuso.housemate.web.client.bootstrap.widget.command.CommandList;
import com.intuso.housemate.web.client.bootstrap.widget.value.ValueList;
import com.intuso.housemate.web.client.object.GWTProxyFeature;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxyType;

/**
 */
public class UnknownFeature extends Composite implements FeatureWidget {

    interface FeatureUiBinder extends UiBinder<Widget, UnknownFeature> {}

    private static FeatureUiBinder ourUiBinder = GWT.create(FeatureUiBinder.class);

    @UiField
    CommandList commandsList;
    @UiField
    ValueList valuesList;

    private final GWTProxyFeature feature;

    public UnknownFeature(final GWTProxyFeature feature) {
        this.feature = feature;
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void setTypes(GWTProxyList<Type.Data<?>, GWTProxyType> types) {
        commandsList.setTypes(types);
        commandsList.setList(feature.getCommands());
        valuesList.setTypes(types);
        valuesList.setList(feature.getValues());
    }
}
