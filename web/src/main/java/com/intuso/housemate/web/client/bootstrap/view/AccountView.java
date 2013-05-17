package com.intuso.housemate.web.client.bootstrap.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.web.client.place.AccountPlace;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 10/03/12
 * Time: 09:31
 * To change this template use File | Settings | File Templates.
 */
public class AccountView extends Composite implements com.intuso.housemate.web.client.ui.view.AccountView {

    interface BootstrapAccountViewUiBinder extends UiBinder<Widget, AccountView> {
    }

    private static BootstrapAccountViewUiBinder ourUiBinder = GWT.create(BootstrapAccountViewUiBinder.class);

    public AccountView() {
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void newPlace(AccountPlace place) {

    }
}