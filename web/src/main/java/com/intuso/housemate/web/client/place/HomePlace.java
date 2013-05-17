package com.intuso.housemate.web.client.place;

import com.google.gwt.place.shared.PlaceTokenizer;
import com.google.gwt.place.shared.Prefix;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 24/11/12
 * Time: 19:13
 * To change this template use File | Settings | File Templates.
 */
public class HomePlace extends HousematePlace {

    public HomePlace() {
        breadcrumbList.add(new Breadcrumb("Home", "#home:"));
    }

    @Prefix("home")
    public static class Tokeniser implements PlaceTokenizer<HomePlace> {

        @Override
        public HomePlace getPlace(String token) {
            return new HomePlace();
        }

        @Override
        public String getToken(HomePlace homePlace) {
            return HousematePlace.getToken(null);
        }
    }
}
