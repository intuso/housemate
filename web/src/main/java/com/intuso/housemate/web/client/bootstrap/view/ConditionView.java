package com.intuso.housemate.web.client.bootstrap.view;

import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.api.object.condition.ConditionWrappable;
import com.intuso.housemate.api.object.rule.RuleWrappable;
import com.intuso.housemate.web.client.GWTResources;
import com.intuso.housemate.web.client.bootstrap.widget.condition.Condition;
import com.intuso.housemate.web.client.object.GWTProxyCommand;
import com.intuso.housemate.web.client.object.GWTProxyCondition;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxyRule;
import com.intuso.housemate.web.client.place.ConditionPlace;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 10/03/12
 * Time: 09:31
 * To change this template use File | Settings | File Templates.
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
        GWTProxyList<RuleWrappable, GWTProxyRule> rules = resources.getRoot().getRules();
        if(rules.get(place.getRuleName()) != null) {
            list = rules.get(place.getRuleName()).getConditions();
            addCommand = rules.get(place.getRuleName()).getAddConditionCommand();
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
        return new Condition(place.getRuleName(), place.getConditionNames(), condition);
    }

    @Override
    protected ConditionPlace getPlace(ConditionPlace place, GWTProxyCondition condition) {
        if(condition == null) {
            if(place.getConditionNames() == null)
                return new ConditionPlace(place.getRuleName());
            else
                return new ConditionPlace(place.getRuleName(), place.getConditionNames());
        } else {
            if(place.getConditionNames() == null)
                return new ConditionPlace(place.getRuleName(), Arrays.asList(condition.getId()));
            else {
                List<String> newConditionNames = new LinkedList<String>(place.getConditionNames());
                newConditionNames.add(condition.getId());
                return new ConditionPlace(place.getRuleName(), newConditionNames);
            }
        }
    }
}