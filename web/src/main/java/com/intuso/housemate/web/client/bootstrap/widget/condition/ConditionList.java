package com.intuso.housemate.web.client.bootstrap.widget.condition;

import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.api.object.condition.ConditionData;
import com.intuso.housemate.web.client.bootstrap.widget.list.ComplexWidgetList;
import com.intuso.housemate.web.client.object.GWTProxyCondition;
import com.intuso.housemate.web.client.object.GWTProxyList;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 23/09/13
 * Time: 21:16
 * To change this template use File | Settings | File Templates.
 */
public class ConditionList extends ComplexWidgetList<ConditionData, GWTProxyCondition> {

    public ConditionList(GWTProxyList<ConditionData, GWTProxyCondition> list, String title,
                         List<String> filteredIds, boolean showOnEmpty) {
        super(list, title, filteredIds, showOnEmpty);
    }

    @Override
    protected Widget getWidget(GWTProxyCondition condition) {
            return new Condition(condition);
    }
}
