package com.intuso.housemate.web.client.bootstrap.view;

import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.core.object.consequence.ConsequenceWrappable;
import com.intuso.housemate.core.object.rule.RuleWrappable;
import com.intuso.housemate.web.client.GWTResources;
import com.intuso.housemate.web.client.bootstrap.widget.consequence.Consequence;
import com.intuso.housemate.web.client.object.GWTProxyCommand;
import com.intuso.housemate.web.client.object.GWTProxyConsequence;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxyRule;
import com.intuso.housemate.web.client.place.UnsatisfiedConsequencePlace;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 10/03/12
 * Time: 09:31
 * To change this template use File | Settings | File Templates.
 */
public class UnsatisfiedConsequenceView extends ObjectListView<GWTProxyConsequence, UnsatisfiedConsequencePlace>
        implements com.intuso.housemate.web.client.ui.view.UnsatisfiedConsequenceView {

    private GWTProxyRule rule;

    public UnsatisfiedConsequenceView(GWTResources<?> resources) {
        super(resources);
    }

    @Override
    public void newPlace(UnsatisfiedConsequencePlace place) {

        rule = null;

        if(place.getRuleName() != null) {
            GWTProxyList<RuleWrappable, GWTProxyRule> rules = resources.getRoot().getRules();
            if(rules.get(place.getRuleName()) != null)
                rule = rules.get(place.getRuleName());
        }

        super.newPlace(place);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    protected GWTProxyList<ConsequenceWrappable, GWTProxyConsequence> getList(UnsatisfiedConsequencePlace place) {
        return rule.getUnsatisfiedConsequences();
    }

    @Override
    protected GWTProxyCommand getAddCommand(UnsatisfiedConsequencePlace place) {
        return rule.getAddUnsatisifedConsequenceCommand();
    }

    @Override
    protected String getSelectedObjectName(UnsatisfiedConsequencePlace place) {
        return place.getConsequenceName();
    }

    @Override
    protected Widget getObjectWidget(UnsatisfiedConsequencePlace place, GWTProxyConsequence consequence) {
        return new Consequence(consequence);
    }

    @Override
    protected UnsatisfiedConsequencePlace getPlace(UnsatisfiedConsequencePlace place, GWTProxyConsequence object) {
        if(object == null)
            return new UnsatisfiedConsequencePlace(place.getRuleName());
        else
            return new UnsatisfiedConsequencePlace(place.getRuleName(), object.getId());
    }
}