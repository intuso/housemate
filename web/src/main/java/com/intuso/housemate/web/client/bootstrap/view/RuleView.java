package com.intuso.housemate.web.client.bootstrap.view;

import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.api.object.rule.RuleWrappable;
import com.intuso.housemate.web.client.GWTResources;
import com.intuso.housemate.web.client.bootstrap.widget.rule.Rule;
import com.intuso.housemate.web.client.object.GWTProxyCommand;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxyRule;
import com.intuso.housemate.web.client.place.RulePlace;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 10/03/12
 * Time: 09:31
 * To change this template use File | Settings | File Templates.
 */
public class RuleView extends ObjectListView<GWTProxyRule, RulePlace> implements com.intuso.housemate.web.client.ui.view.RuleView {

    public RuleView(GWTResources<?> resources) {
        super(resources);
    }

    @Override
    protected GWTProxyList<RuleWrappable, GWTProxyRule> getList(RulePlace place) {
        return resources.getRoot().getRules();
    }

    @Override
    protected GWTProxyCommand getAddCommand(RulePlace place) {
        return resources.getRoot().getAddRuleCommand();
    }

    @Override
    protected String getSelectedObjectName(RulePlace place) {
        return place.getRuleName();
    }

    @Override
    protected Widget getObjectWidget(RulePlace place, GWTProxyRule rule) {
        return new Rule(rule);
    }

    @Override
    protected RulePlace getPlace(RulePlace place, GWTProxyRule rule) {
        if(rule == null)
            return new RulePlace();
        else
            return new RulePlace(rule.getId());
    }
}