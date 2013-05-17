package com.intuso.housemate.web.client.bootstrap.widget.condition;

import com.github.gwtbootstrap.client.ui.Icon;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.core.object.condition.ConditionListener;
import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.bootstrap.widget.list.ObjectNavs;
import com.intuso.housemate.web.client.bootstrap.widget.property.PropertyList;
import com.intuso.housemate.web.client.event.ObjectSelectedEvent;
import com.intuso.housemate.web.client.handler.ObjectSelectedHandler;
import com.intuso.housemate.web.client.object.GWTProxyCondition;
import com.intuso.housemate.web.client.place.ConditionPlace;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 10/03/12
 * Time: 09:31
 * To change this template use File | Settings | File Templates.
 */
public class Condition extends Composite
        implements ConditionListener<GWTProxyCondition>, ObjectSelectedHandler<GWTProxyCondition> {

    interface ConditionUiBinder extends UiBinder<Widget, Condition> {
    }

    private static ConditionUiBinder ourUiBinder = GWT.create(ConditionUiBinder.class);

    @UiField
    public Icon satisfiedIcon;
    @UiField
    public ObjectNavs<GWTProxyCondition> conditionList;
    @UiField
    public PropertyList propertyList;

    private String ruleName;
    private List<String> conditionNames;

    public Condition(String ruleName, List<String> conditionNames, GWTProxyCondition condition) {

        this.ruleName = ruleName;
        this.conditionNames = conditionNames;

        initWidget(ourUiBinder.createAndBindUi(this));

        satisfiedIcon.setType(condition.isSatisfied() ? IconType.THUMBS_UP : IconType.THUMBS_DOWN);
        conditionList.setList(condition.getConditions(), condition.getAddConditionCommand());
        propertyList.setList(condition.getProperties());

        conditionList.addObjectSelectedHandler(this);
        condition.addObjectListener(this);
    }

    @Override
    public void conditionSatisfied(GWTProxyCondition condition, boolean satisfied) {
        satisfiedIcon.setType(satisfied ? IconType.THUMBS_UP : IconType.THUMBS_DOWN);
    }

    @Override
    public void conditionError(GWTProxyCondition condition, String error) {
        // TODO
    }

    @Override
    public void objectSelected(ObjectSelectedEvent<GWTProxyCondition> event) {
        List<String> newConditionNames = new LinkedList<String>(conditionNames);
        newConditionNames.add(event.getObject().getId());
        Housemate.FACTORY.getPlaceController().goTo(new ConditionPlace(ruleName, newConditionNames));
    }
}
