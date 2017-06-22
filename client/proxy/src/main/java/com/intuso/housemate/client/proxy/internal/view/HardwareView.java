package com.intuso.housemate.client.proxy.internal.view;

/**
 * Created by tomc on 19/06/17.
 */
public class HardwareView extends View<HardwareView> {

    private CommandView renameCommandView;
    private CommandView startCommandView;
    private CommandView stopCommandView;
    private ValueView runningValueView;
    private ValueView errorValueView;
    private CommandView removeCommandView;
    private ValueView driverLoadedView;
    private ValueView satisfiedValueView;
    private ListView<CommandView> commandsView;
    private ListView<ValueView> valuesView;
    private ListView<PropertyView> propertiesView;
    private ListView<DeviceConnectedView> deviceConnectedsView;

    public HardwareView() {}

    public HardwareView(Mode mode) {
        super(mode);
    }

    public HardwareView(CommandView renameCommandView, 
                        CommandView startCommandView,
                        CommandView stopCommandView,
                        ValueView runningValueView,
                        ValueView errorValueView,
                        CommandView removeCommandView,
                        ValueView driverLoadedView,
                        ValueView satisfiedValueView, 
                        ListView<CommandView> commandsView,
                        ListView<ValueView> valuesView,
                        ListView<PropertyView> propertiesView,
                        ListView<DeviceConnectedView> deviceConnectedsView) {
        super(Mode.SELECTION);
        this.renameCommandView = renameCommandView;
        this.startCommandView = startCommandView;
        this.stopCommandView = stopCommandView;
        this.runningValueView = runningValueView;
        this.errorValueView = errorValueView;
        this.removeCommandView = removeCommandView;
        this.driverLoadedView = driverLoadedView;
        this.satisfiedValueView = satisfiedValueView;
        this.commandsView = commandsView;
        this.valuesView = valuesView;
        this.propertiesView = propertiesView;
        this.deviceConnectedsView = deviceConnectedsView;
    }

    public CommandView getRenameCommandView() {
        return renameCommandView;
    }

    public HardwareView setRenameCommandView(CommandView renameCommandView) {
        this.renameCommandView = renameCommandView;
        return this;
    }

    public CommandView getStartCommandView() {
        return startCommandView;
    }

    public HardwareView setStartCommandView(CommandView startCommandView) {
        this.startCommandView = startCommandView;
        return this;
    }

    public CommandView getStopCommandView() {
        return stopCommandView;
    }

    public HardwareView setStopCommandView(CommandView stopCommandView) {
        this.stopCommandView = stopCommandView;
        return this;
    }

    public ValueView getRunningValueView() {
        return runningValueView;
    }

    public HardwareView setRunningValueView(ValueView runningValueView) {
        this.runningValueView = runningValueView;
        return this;
    }

    public ValueView getErrorValueView() {
        return errorValueView;
    }

    public HardwareView setErrorValueView(ValueView errorValueView) {
        this.errorValueView = errorValueView;
        return this;
    }

    public CommandView getRemoveCommandView() {
        return removeCommandView;
    }

    public HardwareView setRemoveCommandView(CommandView removeCommandView) {
        this.removeCommandView = removeCommandView;
        return this;
    }

    public ValueView getDriverLoadedView() {
        return driverLoadedView;
    }

    public HardwareView setDriverLoadedView(ValueView driverLoadedView) {
        this.driverLoadedView = driverLoadedView;
        return this;
    }

    public ValueView getSatisfiedValueView() {
        return satisfiedValueView;
    }

    public HardwareView setSatisfiedValueView(ValueView satisfiedValueView) {
        this.satisfiedValueView = satisfiedValueView;
        return this;
    }

    public ListView<CommandView> getCommandsView() {
        return commandsView;
    }

    public HardwareView setCommandsView(ListView<CommandView> commandsView) {
        this.commandsView = commandsView;
        return this;
    }

    public ListView<ValueView> getValuesView() {
        return valuesView;
    }

    public HardwareView setValuesView(ListView<ValueView> valuesView) {
        this.valuesView = valuesView;
        return this;
    }

    public ListView<PropertyView> getPropertiesView() {
        return propertiesView;
    }

    public HardwareView setPropertiesView(ListView<PropertyView> propertiesView) {
        this.propertiesView = propertiesView;
        return this;
    }

    public ListView<DeviceConnectedView> getDeviceConnectedsView() {
        return deviceConnectedsView;
    }

    public HardwareView setDeviceConnectedsView(ListView<DeviceConnectedView> deviceConnectedsView) {
        this.deviceConnectedsView = deviceConnectedsView;
        return this;
    }
}
