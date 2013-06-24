package com.intuso.housemate.web.client.bootstrap.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.web.client.place.AccountPlace;

/**
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