package com.intuso.housemate.web.client.bootstrap.widget.rule;

import com.google.common.collect.Lists;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.bootstrap.widget.command.PerformButton;
import com.intuso.housemate.web.client.bootstrap.widget.list.ObjectNavs;
import com.intuso.housemate.web.client.event.ObjectSelectedEvent;
import com.intuso.housemate.web.client.handler.ObjectSelectedHandler;
import com.intuso.housemate.web.client.object.GWTProxyCondition;
import com.intuso.housemate.web.client.object.GWTProxyConsequence;
import com.intuso.housemate.web.client.object.GWTProxyRule;
import com.intuso.housemate.web.client.place.ConditionPlace;
import com.intuso.housemate.web.client.place.SatisfiedConsequencePlace;
import com.intuso.housemate.web.client.place.UnsatisfiedConsequencePlace;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 10/03/12
 * Time: 09:31
 * To change this template use File | Settings | File Templates.
 */
public class Rule extends Composite {

    interface RuleUiBinder extends UiBinder<Widget, Rule> {
    }

    private static RuleUiBinder ourUiBinder = GWT.create(RuleUiBinder.class);

    @UiField
    public PerformButton startButton;
    @UiField
    public PerformButton stopButton;
    @UiField
    public PerformButton removeButton;
    @UiField
    public ObjectNavs<GWTProxyCondition> conditionList;
    @UiField
    public ObjectNavs<GWTProxyConsequence> satisfiedConsequenceList;
    @UiField
    public ObjectNavs<GWTProxyConsequence> unsatisfiedConsequenceList;

    public Rule(final GWTProxyRule rule) {

        initWidget(ourUiBinder.createAndBindUi(this));

        startButton.setCommand(rule.getStartCommand());
        stopButton.setCommand(rule.getStopCommand());
        removeButton.setCommand(rule.getRemoveCommand());
        conditionList.setList(rule.getConditions(), rule.getAddConditionCommand());
        conditionList.addObjectSelectedHandler(new ObjectSelectedHandler<GWTProxyCondition>() {
            @Override
            public void objectSelected(ObjectSelectedEvent<GWTProxyCondition> event) {
                Housemate.FACTORY.getPlaceController().goTo(
                        new ConditionPlace(rule.getId(), Lists.newArrayList(event.getObject().getId())));
            }
        });
        satisfiedConsequenceList.setList(rule.getSatisfiedConsequences(), rule.getAddSatisifedConsequenceCommand());
        satisfiedConsequenceList.addObjectSelectedHandler(new ObjectSelectedHandler<GWTProxyConsequence>() {
            @Override
            public void objectSelected(ObjectSelectedEvent<GWTProxyConsequence> event) {
                Housemate.FACTORY.getPlaceController().goTo(
                        new SatisfiedConsequencePlace(rule.getId(), event.getObject().getId()));
            }
        });
        unsatisfiedConsequenceList.setList(rule.getUnsatisfiedConsequences(), rule.getAddUnsatisifedConsequenceCommand());
        unsatisfiedConsequenceList.addObjectSelectedHandler(new ObjectSelectedHandler<GWTProxyConsequence>() {
            @Override
            public void objectSelected(ObjectSelectedEvent<GWTProxyConsequence> event) {
                Housemate.FACTORY.getPlaceController().goTo(
                        new UnsatisfiedConsequencePlace(rule.getId(), event.getObject().getId()));
            }
        });
    }
}
