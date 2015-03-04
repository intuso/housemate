package com.intuso.housemate.web.client.bootstrap.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.place.shared.PlaceController;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import com.google.inject.Inject;
import com.intuso.housemate.web.client.comms.LoginManager;
import com.intuso.housemate.web.client.place.ApplicationsPlace;
import com.intuso.housemate.web.client.place.AutomationsPlace;
import com.intuso.housemate.web.client.place.DevicesPlace;
import com.intuso.housemate.web.client.place.UsersPlace;
import org.gwtbootstrap3.client.ui.AnchorListItem;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 13/09/13
 * Time: 20:49
 * To change this template use File | Settings | File Templates.
 */
public class Page extends Composite implements com.intuso.housemate.web.client.ui.view.Page, PlaceChangeEvent.Handler {

    interface BootstrapPageUiBinder extends UiBinder<Widget, Page> {
    }

    private static BootstrapPageUiBinder ourUiBinder = GWT.create(BootstrapPageUiBinder.class);

    @UiField
    SimplePanel container;
    @UiField
    AnchorListItem devicesButton;
    @UiField
    AnchorListItem automationsButton;
    @UiField
    AnchorListItem applicationsButton;
    @UiField
    AnchorListItem usersButton;

    private AnchorListItem activeButton = null;

    private final PlaceController placeController;
    private final LoginManager loginManager;

    @Inject
    public Page(PlaceController placeController, LoginManager loginManager, EventBus eventBus) {
        this.placeController = placeController;
        this.loginManager = loginManager;
        initWidget(ourUiBinder.createAndBindUi(this));
        eventBus.addHandler(PlaceChangeEvent.TYPE, this);
    }

    @Override
    public void onPlaceChange(PlaceChangeEvent event) {
        if(activeButton != null) {
            activeButton.setActive(false);
            activeButton = null;
        }
        if(event.getNewPlace() instanceof DevicesPlace)
            activeButton = devicesButton;
        else if(event.getNewPlace() instanceof AutomationsPlace)
            activeButton = automationsButton;
        else if(event.getNewPlace() instanceof ApplicationsPlace)
            activeButton = applicationsButton;
        else if(event.getNewPlace() instanceof UsersPlace)
            activeButton = usersButton;
        if(activeButton != null)
            activeButton.setActive(true);
    }

    @Override
    public AcceptsOneWidget getContainer() {
        return container;
    }

    @UiHandler("devicesButton")
    public void devicesButtonClicked(ClickEvent e) {
        placeController.goTo(new DevicesPlace());
    }

    @UiHandler("automationsButton")
    public void automationsButtonClicked(ClickEvent e) {
        placeController.goTo(new AutomationsPlace());
    }

    @UiHandler("applicationsButton")
    public void applicationsButtonClicked(ClickEvent e) {
        placeController.goTo(new ApplicationsPlace());
    }

    @UiHandler("usersButton")
    public void usersButtonClicked(ClickEvent e) {
        placeController.goTo(new UsersPlace());
    }

    @UiHandler("logoutButton")
    public void logoutButtonClicked(ClickEvent e) {
        loginManager.logout();
    }
}
