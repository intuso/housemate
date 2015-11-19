package com.intuso.housemate.web.client.bootstrap.widget.feature;

import com.google.gwt.user.client.ui.IsWidget;
import com.intuso.housemate.comms.v1_0.api.ChildOverview;
import com.intuso.housemate.comms.v1_0.api.payload.FeatureData;
import com.intuso.housemate.comms.v1_0.api.payload.TypeData;
import com.intuso.housemate.web.client.bootstrap.widget.list.NestedList;
import com.intuso.housemate.web.client.object.GWTProxyFeature;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxyType;

public class FeatureList extends NestedList<FeatureData, GWTProxyFeature> {

    private GWTProxyList<TypeData<?>, GWTProxyType> types;

    public void setTypes(GWTProxyList<TypeData<?>, GWTProxyType> types) {
        this.types = types;
    }

    @Override
    protected IsWidget getWidget(ChildOverview childOverview, GWTProxyFeature feature) {
        FeatureWidget featureWidget = FeatureWidget.FACTORY.getFeatureAs(feature);
        featureWidget.setTypes(types);
        return featureWidget;
    }
}