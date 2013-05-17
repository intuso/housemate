package com.intuso.housemate.web.client.activity;

import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.place.AccountPlace;
import com.intuso.housemate.web.client.ui.view.AccountView;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 24/11/12
 * Time: 19:13
 * To change this template use File | Settings | File Templates.
 */
public class AccountActivity extends HousemateActivity<AccountPlace, AccountView> {

    protected AccountActivity(AccountPlace place) {
        super(place);
    }

    @Override
    public AccountView getView() {
        return Housemate.FACTORY.getAccountView();
    }
}
