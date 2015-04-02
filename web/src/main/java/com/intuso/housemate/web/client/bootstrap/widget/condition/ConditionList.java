package com.intuso.housemate.web.client.bootstrap.widget.condition;

import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.api.object.ChildOverview;
import com.intuso.housemate.api.object.condition.ConditionData;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.web.client.bootstrap.widget.list.AddButton;
import com.intuso.housemate.web.client.bootstrap.widget.list.NestedList;
import com.intuso.housemate.web.client.object.GWTProxyCommand;
import com.intuso.housemate.web.client.object.GWTProxyCondition;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxyType;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 23/09/13
 * Time: 21:16
 * To change this template use File | Settings | File Templates.
 */
public class ConditionList extends NestedList<ConditionData, GWTProxyCondition> {

    private GWTProxyList<TypeData<?>, GWTProxyType> types;

    public void setTypes(GWTProxyList<TypeData<?>, GWTProxyType> types) {
        this.types = types;
    }

    public void setAddCommand(GWTProxyCommand command) {
        setHeaderWidget(new AddButton(types, command));
    }

    @Override
    protected Widget getWidget(ChildOverview childOverview, GWTProxyCondition condition) {
        return new Condition(types, condition);
    }
}
