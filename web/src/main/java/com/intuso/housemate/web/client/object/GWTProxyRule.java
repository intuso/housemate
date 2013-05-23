package com.intuso.housemate.web.client.object;

import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.condition.ConditionWrappable;
import com.intuso.housemate.api.object.consequence.ConsequenceWrappable;
import com.intuso.housemate.api.object.rule.RuleWrappable;
import com.intuso.housemate.object.proxy.ProxyObject;
import com.intuso.housemate.object.proxy.ProxyRule;
import com.intuso.housemate.web.client.GWTResources;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 07/08/12
 * Time: 00:36
 * To change this template use File | Settings | File Templates.
 */
public class GWTProxyRule extends ProxyRule<
            GWTResources<? extends HousemateObjectFactory<GWTResources<?>, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>>,
            GWTResources<?>,
            GWTProxyProperty, GWTProxyCommand, GWTProxyValue, GWTProxyCondition,
            GWTProxyList<ConditionWrappable, GWTProxyCondition>, GWTProxyConsequence,
            GWTProxyList<ConsequenceWrappable, GWTProxyConsequence>, GWTProxyRule> {
    public GWTProxyRule(GWTResources<? extends HousemateObjectFactory<GWTResources<?>, HousemateObjectWrappable<?>, ProxyObject<?, ?, ?, ?, ?, ?, ?>>> resources,
                        GWTResources<?> subResources,
                        RuleWrappable wrappable) {
        super(resources, subResources, wrappable);
    }
}
