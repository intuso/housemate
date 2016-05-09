package com.intuso.housemate.web.client.bootstrap.widget.feature;

import com.google.gwt.user.client.ui.IsWidget;
import com.intuso.housemate.client.v1_0.api.object.Feature;
import com.intuso.housemate.client.v1_0.api.object.Type;
import com.intuso.housemate.client.v1_0.data.api.ChildOverview;
import com.intuso.housemate.web.client.bootstrap.widget.list.NestedList;
import com.intuso.housemate.web.client.object.GWTProxyFeature;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxyType;

public class FeatureList extends NestedList<Feature.Data, GWTProxyFeature> {

    private GWTProxyList<Type.Data<?>, GWTProxyType> types;

    public void setTypes(GWTProxyList<Type.Data<?>, GWTProxyType> types) {
        this.types = types;
    }

    @Override
    protected IsWidget getWidget(ChildOverview childOverview, GWTProxyFeature feature) {
        FeatureWidget featureWidget = FeatureWidget.FACTORY.getFeatureAs(feature);
        featureWidget.setTypes(types);
        return featureWidget;
    }
}