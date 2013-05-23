package com.intuso.housemate.web.client.bootstrap.view;

import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.api.object.consequence.ConsequenceWrappable;
import com.intuso.housemate.api.object.rule.RuleWrappable;
import com.intuso.housemate.web.client.GWTResources;
import com.intuso.housemate.web.client.bootstrap.widget.consequence.Consequence;
import com.intuso.housemate.web.client.object.GWTProxyCommand;
import com.intuso.housemate.web.client.object.GWTProxyConsequence;
import com.intuso.housemate.web.client.object.GWTProxyList;
import com.intuso.housemate.web.client.object.GWTProxyRule;
import com.intuso.housemate.web.client.place.SatisfiedConsequencePlace;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 10/03/12
 * Time: 09:31
 * To change this template use File | Settings | File Templates.
 */
public class SatisfiedConsequenceView extends ObjectListView<GWTProxyConsequence, SatisfiedConsequencePlace>
        implements com.intuso.housemate.web.client.ui.view.SatisfiedConsequenceView {

    private GWTProxyRule rule;

    public SatisfiedConsequenceView(GWTResources<?> resources) {
        super(resources);
    }

    @Override
    public void newPlace(SatisfiedConsequencePlace place) {

        rule = null;

        if(place.getRuleName() != null) {
            GWTProxyList<RuleWrappable, GWTProxyRule> rules = resources.getRoot().getRules();
            if(rules.get(place.getRuleName()) != null)
                rule = rules.get(place.getRuleName());
        }

        super.newPlace(place);
    }

    @Override
    protected GWTProxyList<ConsequenceWrappable, GWTProxyConsequence> getList(SatisfiedConsequencePlace place) {
        return rule.getSatisfiedConsequences();
    }

    @Override
    protected GWTProxyCommand getAddCommand(SatisfiedConsequencePlace place) {
        return rule.getAddSatisifedConsequenceCommand();
    }

    @Override
    protected String getSelectedObjectName(SatisfiedConsequencePlace place) {
        return place.getConsequenceName();
    }

    @Override
    protected Widget getObjectWidget(SatisfiedConsequencePlace place, GWTProxyConsequence consequence) {
        return new Consequence(consequence);
    }

    @Override
    protected SatisfiedConsequencePlace getPlace(SatisfiedConsequencePlace place, GWTProxyConsequence object) {
        if(object == null)
            return new SatisfiedConsequencePlace(place.getRuleName());
        else
            return new SatisfiedConsequencePlace(place.getRuleName(), object.getId());
    }
}