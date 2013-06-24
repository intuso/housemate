package com.intuso.housemate.web.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

/**
 */
public class AccountPlace extends HomePlace {

    public final static AccountPlace PLACE = new AccountPlace();

    private AccountPlace() {
        breadcrumbList.add(new Breadcrumb("Account", PlaceName.Account.getToken()));
    }

    @Prefix("account")
    public static class Tokeniser implements PlaceTokenizer<AccountPlace> {

        @Override
        public AccountPlace getPlace(String token) {
            return new AccountPlace();
        }

        @Override
        public String getToken(AccountPlace accountPlace) {
            return HousematePlace.getToken(null);
        }
    }
}
