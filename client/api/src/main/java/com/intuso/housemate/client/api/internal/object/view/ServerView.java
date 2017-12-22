package com.intuso.housemate.client.api.internal.object.view;

/**
 * Created by tomc on 19/06/17.
 */
public class ServerView extends View {

    private ListView<AutomationView> automations;
    private CommandView addAutomationCommand;
    private ListView<DeviceView<?>> devices;
    private ListView<DeviceGroupView> deviceGroups;
    private CommandView addDeviceGroupCommand;
    private ListView<UserView> users;
    private CommandView addUserCommand;
    private ListView<NodeView> nodes;

    public ServerView() {}

    public ServerView(Mode mode) {
        super(mode);
    }

    public ServerView(ListView<AutomationView> automations,
                      CommandView addAutomationCommand,
                      ListView<DeviceView<?>> devices,
                      ListView<DeviceGroupView> deviceGroups,
                      CommandView addDeviceGroupCommand,
                      ListView<UserView> users,
                      CommandView addUserCommand,
                      ListView<NodeView> nodes) {
        this.automations = automations;
        this.addAutomationCommand = addAutomationCommand;
        this.devices = devices;
        this.deviceGroups = deviceGroups;
        this.addDeviceGroupCommand = addDeviceGroupCommand;
        this.users = users;
        this.addUserCommand = addUserCommand;
        this.nodes = nodes;
    }

    public ListView<AutomationView> getAutomations() {
        return automations;
    }

    public ServerView setAutomations(ListView<AutomationView> automations) {
        this.automations = automations;
        return this;
    }

    public CommandView getAddAutomationCommand() {
        return addAutomationCommand;
    }

    public ServerView setAddAutomationCommand(CommandView addAutomationCommand) {
        this.addAutomationCommand = addAutomationCommand;
        return this;
    }

    public ListView<DeviceView<?>> getDevices() {
        return devices;
    }

    public ServerView setDevices(ListView<DeviceView<?>> devices) {
        this.devices = devices;
        return this;
    }

    public ListView<DeviceGroupView> getDeviceGroups() {
        return deviceGroups;
    }

    public ServerView setDeviceGroups(ListView<DeviceGroupView> deviceGroups) {
        this.deviceGroups = deviceGroups;
        return this;
    }

    public CommandView getAddDeviceGroupCommand() {
        return addDeviceGroupCommand;
    }

    public ServerView setAddDeviceGroupCommand(CommandView addDeviceGroupCommand) {
        this.addDeviceGroupCommand = addDeviceGroupCommand;
        return this;
    }

    public ListView<UserView> getUsers() {
        return users;
    }

    public ServerView setUsers(ListView<UserView> users) {
        this.users = users;
        return this;
    }

    public CommandView getAddUserCommand() {
        return addUserCommand;
    }

    public ServerView setAddUserCommand(CommandView addUserCommand) {
        this.addUserCommand = addUserCommand;
        return this;
    }

    public ListView<NodeView> getNodes() {
        return nodes;
    }

    public ServerView setNodes(ListView<NodeView> nodes) {
        this.nodes = nodes;
        return this;
    }
}
