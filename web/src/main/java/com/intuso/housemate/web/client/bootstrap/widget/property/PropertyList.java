package com.intuso.housemate.web.client.bootstrap.widget.property;

import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.housemate.web.client.bootstrap.widget.list.ComplexWidgetList;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxyProperty;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 23/09/13
 * Time: 21:16
 * To change this template use File | Settings | File Templates.
 */
public class PropertyList extends ComplexWidgetList<PropertyData, GWTProxyProperty> {

    public PropertyList(GWTProxyList<PropertyData, GWTProxyProperty> list, String title,
                        List<String> filteredIds, boolean showOnEmpty) {
        super(list, title, filteredIds, showOnEmpty);
    }

    @Override
    protected Widget getWidget(final GWTProxyProperty property) {
        return new Property(property);
    }
}
