package com.intuso.housemate.client.api.internal.object.view;

/**
 * Created by tomc on 19/06/17.
 */
public class DeviceGroupView extends DeviceView<DeviceGroupView> {

    private ValueView errorValue;
    private CommandView removeCommand;
    private ListView<DeviceView<?>> playbackDevices;
    private CommandView addPlaybackDevice;
    private ListView<DeviceView<?>> powerDevices;
    private CommandView addPowerDevice;
    private ListView<DeviceView<?>> runDevices;
    private CommandView addRunDevice;
    private ListView<DeviceView<?>> temperatureSensorDevices;
    private CommandView addTemperatureSensorDevice;
    private ListView<DeviceView<?>> volumeDevices;
    private CommandView addVolumeDevice;

    public DeviceGroupView() {}

    public DeviceGroupView(Mode mode) {
        super(mode);
    }

    public DeviceGroupView(CommandView renameCommandView,
                           ListView<DeviceComponentView> componentsView,
                           ValueView errorValue,
                           CommandView removeCommand,
                           ListView<DeviceView<?>> playbackDevices,
                           CommandView addPlaybackDevice,
                           ListView<DeviceView<?>> powerDevices,
                           CommandView addPowerDevice,
                           ListView<DeviceView<?>> runDevices,
                           CommandView addRunDevice,
                           ListView<DeviceView<?>> temperatureSensorDevices,
                           CommandView addTemperatureSensorDevice,
                           ListView<DeviceView<?>> volumeDevices,
                           CommandView addVolumeDevice) {
        super(renameCommandView, componentsView);
        this.errorValue = errorValue;
        this.removeCommand = removeCommand;
        this.playbackDevices = playbackDevices;
        this.addPlaybackDevice = addPlaybackDevice;
        this.powerDevices = powerDevices;
        this.addPowerDevice = addPowerDevice;
        this.runDevices = runDevices;
        this.addRunDevice = addRunDevice;
        this.temperatureSensorDevices = temperatureSensorDevices;
        this.addTemperatureSensorDevice = addTemperatureSensorDevice;
        this.volumeDevices = volumeDevices;
        this.addVolumeDevice = addVolumeDevice;
    }

    public ValueView getErrorValue() {
        return errorValue;
    }

    public DeviceGroupView setErrorValue(ValueView errorValue) {
        this.errorValue = errorValue;
        return this;
    }

    public CommandView getRemoveCommand() {
        return removeCommand;
    }

    public DeviceGroupView setRemoveCommand(CommandView removeCommand) {
        this.removeCommand = removeCommand;
        return this;
    }

    public ListView<DeviceView<?>> getPlaybackDevices() {
        return playbackDevices;
    }

    public DeviceGroupView setPlaybackDevices(ListView<DeviceView<?>> playbackDevices) {
        this.playbackDevices = playbackDevices;
        return this;
    }

    public CommandView getAddPlaybackDevice() {
        return addPlaybackDevice;
    }

    public DeviceGroupView setAddPlaybackDevice(CommandView addPlaybackDevice) {
        this.addPlaybackDevice = addPlaybackDevice;
        return this;
    }

    public ListView<DeviceView<?>> getPowerDevices() {
        return powerDevices;
    }

    public DeviceGroupView setPowerDevices(ListView<DeviceView<?>> powerDevices) {
        this.powerDevices = powerDevices;
        return this;
    }

    public CommandView getAddPowerDevice() {
        return addPowerDevice;
    }

    public DeviceGroupView setAddPowerDevice(CommandView addPowerDevice) {
        this.addPowerDevice = addPowerDevice;
        return this;
    }

    public ListView<DeviceView<?>> getRunDevices() {
        return runDevices;
    }

    public DeviceGroupView setRunDevices(ListView<DeviceView<?>> runDevices) {
        this.runDevices = runDevices;
        return this;
    }

    public CommandView getAddRunDevice() {
        return addRunDevice;
    }

    public DeviceGroupView setAddRunDevice(CommandView addRunDevice) {
        this.addRunDevice = addRunDevice;
        return this;
    }

    public ListView<DeviceView<?>> getTemperatureSensorDevices() {
        return temperatureSensorDevices;
    }

    public DeviceGroupView setTemperatureSensorDevices(ListView<DeviceView<?>> temperatureSensorDevices) {
        this.temperatureSensorDevices = temperatureSensorDevices;
        return this;
    }

    public CommandView getAddTemperatureSensorDevice() {
        return addTemperatureSensorDevice;
    }

    public DeviceGroupView setAddTemperatureSensorDevice(CommandView addTemperatureSensorDevice) {
        this.addTemperatureSensorDevice = addTemperatureSensorDevice;
        return this;
    }

    public ListView<DeviceView<?>> getVolumeDevices() {
        return volumeDevices;
    }

    public DeviceGroupView setVolumeDevices(ListView<DeviceView<?>> volumeDevices) {
        this.volumeDevices = volumeDevices;
        return this;
    }

    public CommandView getAddVolumeDevice() {
        return addVolumeDevice;
    }

    public DeviceGroupView setAddVolumeDevice(CommandView addVolumeDevice) {
        this.addVolumeDevice = addVolumeDevice;
        return this;
    }
}
