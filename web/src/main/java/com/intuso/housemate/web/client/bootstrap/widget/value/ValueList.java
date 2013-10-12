package com.intuso.housemate.web.client.bootstrap.widget.value;

import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.api.object.value.ValueData;
import com.intuso.housemate.web.client.bootstrap.widget.list.WidgetList;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxyValue;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 23/09/13
 * Time: 21:16
 * To change this template use File | Settings | File Templates.
 */
public class ValueList extends WidgetList<ValueData, GWTProxyValue> {

    public ValueList(GWTProxyList<ValueData, GWTProxyValue> list, String title,
                     List<String> filteredIds, boolean showOnEmpty) {
        super(list, title, filteredIds, showOnEmpty);
        loadRows();
    }

    @Override
    protected Widget getWidget(GWTProxyValue value) {
        return Value.getWidget(value);
    }
}
