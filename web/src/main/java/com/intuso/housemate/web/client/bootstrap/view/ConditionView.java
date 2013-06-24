package com.intuso.housemate.web.client.bootstrap.view;

import com.google.common.collect.Lists;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.api.object.automation.AutomationWrappable;
import com.intuso.housemate.api.object.condition.ConditionWrappable;
import com.intuso.housemate.web.client.GWTResources;
import com.intuso.housemate.web.client.bootstrap.widget.condition.Condition;
import com.intuso.housemate.web.client.object.GWTProxyAutomation;
import com.intuso.housemate.web.client.object.GWTProxyCommand;
import com.intuso.housemate.web.client.object.GWTProxyCondition;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.place.ConditionPlace;

import java.util.List;

/**
 */
public class ConditionView extends ObjectListView<GWTProxyCondition, ConditionPlace>
        implements com.intuso.housemate.web.client.ui.view.ConditionView {

    private GWTProxyList<ConditionWrappable, GWTProxyCondition> list;
    private GWTProxyCommand addCommand;

    public ConditionView(GWTResources<?> resources) {
        super(resources);
    }

    @Override
    public void newPlace(ConditionPlace place) {
        list = null;
        addCommand = null;
        GWTProxyList<AutomationWrappable, GWTProxyAutomation> automations = resources.getRoot().getAutomations();
        if(automations.get(place.getAutomationName()) != null) {
            list = automations.get(place.getAutomationName()).getConditions();
            addCommand = automations.get(place.getAutomationName()).getAddConditionCommand();
            if(place.getConditionNames() != null) {
                for(int i = 0; i < place.getConditionNames().size() - 1; i++) {
                    String conditionName = place.getConditionNames().get(i);
                    if(list.get(conditionName) != null) {
                        // get add command before reassigning the list
                        addCommand = list.get(conditionName).getAddConditionCommand();
                        list = list.get(conditionName).getConditions();
                    } else {
                        list = null;
                        addCommand = null;
                        break;
                    }
                }
            }
        }
        super.newPlace(place);
    }

    @Override
    protected GWTProxyList<ConditionWrappable, GWTProxyCondition> getList(ConditionPlace place) {
        return list;
    }

    @Override
    protected GWTProxyCommand getAddCommand(ConditionPlace place) {
        return addCommand;
    }

    @Override
    protected String getSelectedObjectName(ConditionPlace place) {
        if(place.getConditionNames() == null || place.getConditionNames().size() == 0)
            return null;
        return place.getConditionNames().get(place.getConditionNames().size() - 1);
    }

    @Override
    protected Widget getObjectWidget(ConditionPlace place, GWTProxyCondition condition) {
        return new Condition(place.getAutomationName(), place.getDepth(), place.getConditionNames(), condition);
    }

    @Override
    protected ConditionPlace getPlace(ConditionPlace place, GWTProxyCondition condition) {
        List<String> conditionNames = Lists.newArrayList(place.getConditionNames());
        if(conditionNames.size() >= place.getDepth())
            conditionNames.remove(conditionNames.size() - 1);
        if(condition != null)
            conditionNames.add(condition.getId());
        return new ConditionPlace(place.getAutomationName(), place.getDepth(), conditionNames);
    }
}