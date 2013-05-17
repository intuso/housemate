package com.intuso.housemate.web.client.bootstrap.view;

import com.github.gwtbootstrap.client.ui.Breadcrumbs;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.place.shared.Place;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.place.AccountPlace;
import com.intuso.housemate.web.client.place.Breadcrumb;
import com.intuso.housemate.web.client.place.HousematePlace;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 10/03/12
 * Time: 09:31
 * To change this template use File | Settings | File Templates.
 */
public class Page extends Composite implements com.intuso.housemate.web.client.ui.view.Page, PlaceChangeEvent.Handler {

    interface BootstrapPageUiBinder extends UiBinder<Widget, Page> {
    }

    private static BootstrapPageUiBinder ourUiBinder = GWT.create(BootstrapPageUiBinder.class);

    @UiField
    NavLink accountButton;
    @UiField
    Breadcrumbs allBreadcrumbs;
    @UiField
    Breadcrumbs recentBreadcrumbs;
    @UiField
    SimplePanel container;

    public Page() {
        initWidget(ourUiBinder.createAndBindUi(this));

        Housemate.FACTORY.getEventBus().addHandler(PlaceChangeEvent.TYPE, this);
    }

    @Override
    public AcceptsOneWidget getContainer() {
        return container;
    }

    @Override
    public void onPlaceChange(PlaceChangeEvent placeChangeEvent) {

        // highlight top menu bar items if they're selected
        Place place = placeChangeEvent.getNewPlace();
        accountButton.setActive(false);
        if(place instanceof AccountPlace)
            accountButton.setActive(true);

        // set the breadcrumb
        if(place instanceof HousematePlace) {
            allBreadcrumbs.clear();
            recentBreadcrumbs.clear();
            List<Breadcrumb> breadcrumbList = ((HousematePlace)place).getBreadcrumbItems();
            for(Breadcrumb breadcrumb : breadcrumbList)
                allBreadcrumbs.add(new NavLink(breadcrumb.getLabel(), breadcrumb.getHref()));
            if(breadcrumbList.size() > 1) {
                Breadcrumb breadCrumb = breadcrumbList.get(breadcrumbList.size() - 2);
                recentBreadcrumbs.add(new NavLink(breadCrumb.getLabel(), breadCrumb.getHref()));
                breadCrumb = breadcrumbList.get(breadcrumbList.size() - 1);
                recentBreadcrumbs.add(new NavLink(breadCrumb.getLabel(), breadCrumb.getHref()));
            }
        }
    }

    @UiHandler("accountButton")
    public void accountButtonClicked(ClickEvent e) {
        Housemate.FACTORY.getPlaceController().goTo(AccountPlace.PLACE);
    }

    @UiHandler("logoutButton")
    public void logoutButtonClicked(ClickEvent e) {
        Housemate.ENVIRONMENT.getResources().getLoginManager().logout();
    }
}