package com.intuso.housemate.web.client.bootstrap.widget.consequence;

import com.github.gwtbootstrap.client.ui.Icon;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.api.object.consequence.ConsequenceListener;
import com.intuso.housemate.web.client.bootstrap.widget.property.PropertyList;
import com.intuso.housemate.web.client.object.GWTProxyConsequence;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 10/03/12
 * Time: 09:31
 * To change this template use File | Settings | File Templates.
 */
public class Consequence extends Composite implements ConsequenceListener<GWTProxyConsequence> {

    interface ConsequenceUiBinder extends UiBinder<Widget, Consequence> {
    }

    private static ConsequenceUiBinder ourUiBinder = GWT.create(ConsequenceUiBinder.class);

    @UiField
    public Icon executingIcon;
    @UiField
    public PropertyList propertyList;

    public Consequence(GWTProxyConsequence consequence) {

        initWidget(ourUiBinder.createAndBindUi(this));

        executingIcon.setType(consequence.isExecuting() ? IconType.THUMBS_UP : IconType.THUMBS_DOWN);
        propertyList.setList(consequence.getProperties());

        consequence.addObjectListener(this);
    }

    @Override
    public void consequenceExecuting(GWTProxyConsequence consequence, boolean executing) {
        executingIcon.setType(executing ? IconType.THUMBS_UP : IconType.THUMBS_DOWN);
    }

    @Override
    public void consequenceError(GWTProxyConsequence consequence, String error) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
