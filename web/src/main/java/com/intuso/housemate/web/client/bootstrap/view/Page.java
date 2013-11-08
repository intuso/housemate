package com.intuso.housemate.web.client.bootstrap.view;

import com.github.gwtbootstrap.client.ui.Brand;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.place.shared.PlaceChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.*;
import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.place.AutomationsPlace;
import com.intuso.housemate.web.client.place.DevicesPlace;
import com.intuso.housemate.web.client.place.UsersPlace;

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
    Brand brand;
    @UiField
    SimplePanel container;
    @UiField
    NavLink devicesButton;
    @UiField
    NavLink automationsButton;
    @UiField
    NavLink usersButton;

    private NavLink activeButton = null;

    public Page() {
        initWidget(ourUiBinder.createAndBindUi(this));
        this.brand.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        final Image logo = new Image("img/nav-brand.png");
        final String html = "<center>" + logo.toString() + "</center>";//"<br>Ø®Ù…Ø³ Ø¯Ù‚Ø§ÙŠÙ‚ Ø£Ø®Ø¨Ø§Ø±</center>";
        this.brand.setHTML(html);
        this.brand.setDirectionEstimator(true);
        Housemate.FACTORY.getEventBus().addHandler(PlaceChangeEvent.TYPE, this);
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
        Housemate.FACTORY.getPlaceController().goTo(new DevicesPlace());
    }

    @UiHandler("automationsButton")
    public void automationsButtonClicked(ClickEvent e) {
        Housemate.FACTORY.getPlaceController().goTo(new AutomationsPlace());
    }

    @UiHandler("usersButton")
    public void usersButtonClicked(ClickEvent e) {
        Housemate.FACTORY.getPlaceController().goTo(new UsersPlace());
    }

    @UiHandler("logoutButton")
    public void logoutButtonClicked(ClickEvent e) {
        Housemate.ENVIRONMENT.getResources().getLoginManager().logout();
    }
}
