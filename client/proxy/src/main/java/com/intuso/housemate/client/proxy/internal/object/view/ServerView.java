package com.intuso.housemate.client.proxy.internal.object.view;

/**
 * Created by tomc on 19/06/17.
 */
public class ServerView extends View<ServerView> {

    private ListView<AutomationView> automationsView;
    private CommandView addAutomationCommandView;
    private ListView<DeviceGroupView> deviceGroupsView;
    private CommandView addDeviceGroupCommandView;
    private ListView<UserView> usersView;
    private CommandView addUserCommandView;
    private ListView<NodeView> nodesView;

    public ServerView() {}

    public ServerView(Mode mode) {
        super(mode);
    }

    public ServerView(ListView<AutomationView> automationsView,
                      CommandView addAutomationCommandView,
                      ListView<DeviceGroupView> deviceGroupsView,
                      CommandView addDeviceGroupCommandView,
                      ListView<UserView> usersView,
                      CommandView addUserCommandView,
                      ListView<NodeView> nodesView) {
        this.automationsView = automationsView;
        this.addAutomationCommandView = addAutomationCommandView;
        this.deviceGroupsView = deviceGroupsView;
        this.addDeviceGroupCommandView = addDeviceGroupCommandView;
        this.usersView = usersView;
        this.addUserCommandView = addUserCommandView;
        this.nodesView = nodesView;
    }

    public ListView<AutomationView> getAutomationsView() {
        return automationsView;
    }

    public ServerView setAutomationsView(ListView<AutomationView> automationsView) {
        this.automationsView = automationsView;
        return this;
    }

    public CommandView getAddAutomationCommandView() {
        return addAutomationCommandView;
    }

    public ServerView setAddAutomationCommandView(CommandView addAutomationCommandView) {
        this.addAutomationCommandView = addAutomationCommandView;
        return this;
    }

    public ListView<DeviceGroupView> getDeviceGroupsView() {
        return deviceGroupsView;
    }

    public ServerView setDeviceGroupsView(ListView<DeviceGroupView> deviceGroupsView) {
        this.deviceGroupsView = deviceGroupsView;
        return this;
    }

    public CommandView getAddDeviceGroupCommandView() {
        return addDeviceGroupCommandView;
    }

    public ServerView setAddDeviceGroupCommandView(CommandView addDeviceGroupCommandView) {
        this.addDeviceGroupCommandView = addDeviceGroupCommandView;
        return this;
    }

    public ListView<UserView> getUsersView() {
        return usersView;
    }

    public ServerView setUsersView(ListView<UserView> usersView) {
        this.usersView = usersView;
        return this;
    }

    public CommandView getAddUserCommandView() {
        return addUserCommandView;
    }

    public ServerView setAddUserCommandView(CommandView addUserCommandView) {
        this.addUserCommandView = addUserCommandView;
        return this;
    }

    public ListView<NodeView> getNodesView() {
        return nodesView;
    }

    public ServerView setNodesView(ListView<NodeView> nodesView) {
        this.nodesView = nodesView;
        return this;
    }
}
