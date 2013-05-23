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
import com.intuso.housemate.web.client.object.GWTProxyDevice;
import com.intuso.housemate.web.client.object.GWTProxyRootObject;
import com.intuso.housemate.web.client.object.GWTProxyRule;
import com.intuso.housemate.web.client.object.GWTProxyUser;
import com.intuso.housemate.web.client.place.DevicePlace;
import com.intuso.housemate.web.client.place.HomePlace;
import com.intuso.housemate.web.client.place.RulePlace;
import com.intuso.housemate.web.client.place.UserPlace;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 10/03/12
 * Time: 09:31
 * To change this template use File | Settings | File Templates.
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
    public ObjectNavs<GWTProxyRule> ruleList;
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
                Housemate.FACTORY.getPlaceController().goTo(new DevicePlace(event.getObject().getId()));
            }
        });
        ruleList.setList(root.getRules(), root.getAddRuleCommand());
        ruleList.addObjectSelectedHandler(new ObjectSelectedHandler() {
            @Override
            public void objectSelected(ObjectSelectedEvent event) {
                Housemate.FACTORY.getPlaceController().goTo(new RulePlace(event.getObject().getId()));
            }
        });
        userList.setList(root.getUsers(), root.getAddUserCommand());
        userList.addObjectSelectedHandler(new ObjectSelectedHandler() {
            @Override
            public void objectSelected(ObjectSelectedEvent event) {
                Housemate.FACTORY.getPlaceController().goTo(new UserPlace(event.getObject().getId()));
            }
        });

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
