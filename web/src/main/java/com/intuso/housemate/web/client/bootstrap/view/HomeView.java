package com.intuso.housemate.web.client.bootstrap.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.intuso.housemate.api.object.list.ListListener;
import com.intuso.housemate.web.client.GWTResources;
import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.bootstrap.widget.device.DeviceShortcut;
import com.intuso.housemate.web.client.bootstrap.widget.list.ObjectNavs;
import com.intuso.housemate.web.client.event.ObjectSelectedEvent;
import com.intuso.housemate.web.client.handler.ObjectSelectedHandler;
import com.intuso.housemate.web.client.object.GWTProxyAutomation;
import com.intuso.housemate.web.client.object.GWTProxyDevice;
import com.intuso.housemate.web.client.object.GWTProxyRootObject;
import com.intuso.housemate.web.client.object.GWTProxyUser;
import com.intuso.housemate.web.client.place.AutomationPlace;
import com.intuso.housemate.web.client.place.DevicePlace;
import com.intuso.housemate.web.client.place.HomePlace;
import com.intuso.housemate.web.client.place.UserPlace;

import java.util.HashMap;
import java.util.Map;

/**
 */
public class HomeView extends Composite implements com.intuso.housemate.web.client.ui.view.HomeView, ListListener<GWTProxyDevice> {

    interface BootstrapHomeViewUiBinder extends UiBinder<Widget, HomeView> {
    }

    private static BootstrapHomeViewUiBinder ourUiBinder = GWT.create(BootstrapHomeViewUiBinder.class);

    @UiField
    public FlowPanel deviceShortcutWidgetContainer;
    @UiField
    public ObjectNavs<GWTProxyDevice> deviceList;
    @UiField
    public ObjectNavs<GWTProxyAutomation> automationList;
    @UiField
    public ObjectNavs<GWTProxyUser> userList;

    private final Map<GWTProxyDevice, Widget> deviceShortcutWidgets = new HashMap<GWTProxyDevice, Widget>();

    public HomeView(GWTResources resources) {
        initWidget(ourUiBinder.createAndBindUi(this));

        GWTProxyRootObject root = resources.getRoot();
        deviceList.setList(root.getDevices(), root.getAddDeviceCommand());
        deviceList.addObjectSelectedHandler(new ObjectSelectedHandler() {
            @Override
            public void objectSelected(ObjectSelectedEvent event) {
                if(event.getObject() != null)
                    Housemate.FACTORY.getPlaceController().goTo(new DevicePlace(event.getObject().getId()));
                else
                    Housemate.FACTORY.getPlaceController().goTo(new DevicePlace());
            }
        });
        automationList.setList(root.getAutomations(), root.getAddAutomationCommand());
        automationList.addObjectSelectedHandler(new ObjectSelectedHandler() {
            @Override
            public void objectSelected(ObjectSelectedEvent event) {
                if(event.getObject() != null)
                    Housemate.FACTORY.getPlaceController().goTo(new AutomationPlace(event.getObject().getId()));
                else
                    Housemate.FACTORY.getPlaceController().goTo(new AutomationPlace());
            }
        });
        userList.setList(root.getUsers(), root.getAddUserCommand());
        userList.addObjectSelectedHandler(new ObjectSelectedHandler() {
            @Override
            public void objectSelected(ObjectSelectedEvent event) {
                if(event.getObject() != null)
                    Housemate.FACTORY.getPlaceController().goTo(new UserPlace(event.getObject().getId()));
                else
                    Housemate.FACTORY.getPlaceController().goTo(new UserPlace());
            }
        });

        if(root.getDevices() != null)
            root.getDevices().addObjectListener(this, true);
    }

    @Override
    public void newPlace(HomePlace place) {

    }

    @Override
    public void elementAdded(GWTProxyDevice device) {
        Widget deviceShortcutWidget = new DeviceShortcut(device);
        deviceShortcutWidgets.put(device, deviceShortcutWidget);
        deviceShortcutWidgetContainer.add(deviceShortcutWidget);
    }

    @Override
    public void elementRemoved(GWTProxyDevice device) {
        if(deviceShortcutWidgets.get(device) != null)
            deviceShortcutWidgetContainer.remove(deviceShortcutWidgets.remove(device));
    }
}
