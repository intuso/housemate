package com.intuso.housemate.platform.android.app.object;

import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyObject;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * Created by tomc on 01/09/16.
 */
public class AndroidObjectFactories {

    private final ManagedCollectionFactory managedCollectionFactory;

    private final ProxyObject.Factory<AndroidProxyAutomation> automation = new Automation();
    private final ProxyObject.Factory<AndroidProxyList<AndroidProxyAutomation>> automations = new Automations();
    private final ProxyObject.Factory<AndroidProxyCommand> command = new Command();
    private final ProxyObject.Factory<AndroidProxyList<AndroidProxyCommand>> commands = new Commands();
    private final ProxyObject.Factory<AndroidProxyCondition> condition = new Condition();
    private final ProxyObject.Factory<AndroidProxyList<AndroidProxyCondition>> conditions = new Conditions();
    private final ProxyObject.Factory<AndroidProxyConnectedDevice> connectedDevice = new ConnectedDevice();
    private final ProxyObject.Factory<AndroidProxyList<AndroidProxyConnectedDevice>> connectedDevices = new ConnectedDevices();
    private final ProxyObject.Factory<AndroidProxyHardware> hardware = new Hardware();
    private final ProxyObject.Factory<AndroidProxyList<AndroidProxyHardware>> hardwares = new Hardwares();
    private final ProxyObject.Factory<AndroidProxyNode> node = new Node();
    private final ProxyObject.Factory<AndroidProxyList<AndroidProxyNode>> nodes = new Nodes();
    private final ProxyObject.Factory<AndroidProxyOption> option = new Option();
    private final ProxyObject.Factory<AndroidProxyList<AndroidProxyOption>> options = new Options();
    private final ProxyObject.Factory<AndroidProxyParameter> parameter = new Parameter();
    private final ProxyObject.Factory<AndroidProxyList<AndroidProxyParameter>> parameters = new Parameters();
    private final ProxyObject.Factory<AndroidProxyProperty> property = new Property();
    private final ProxyObject.Factory<AndroidProxyList<AndroidProxyProperty>> properties = new Properties();
    private final ProxyObject.Factory<AndroidProxySubType> subType = new SubType();
    private final ProxyObject.Factory<AndroidProxyList<AndroidProxySubType>> subTypes = new SubTypes();
    private final ProxyObject.Factory<AndroidProxySystem> system = new System();
    private final ProxyObject.Factory<AndroidProxyList<AndroidProxySystem>> systems = new Systems();
    private final ProxyObject.Factory<AndroidProxyTask> task = new Task();
    private final ProxyObject.Factory<AndroidProxyList<AndroidProxyTask>> tasks = new Tasks();
    private final ProxyObject.Factory<AndroidProxyType> type = new Type();
    private final ProxyObject.Factory<AndroidProxyList<AndroidProxyType>> types = new Types();
    private final ProxyObject.Factory<AndroidProxyUser> user = new User();
    private final ProxyObject.Factory<AndroidProxyList<AndroidProxyUser>> users = new Users();
    private final ProxyObject.Factory<AndroidProxyValue> value = new Value();
    private final ProxyObject.Factory<AndroidProxyList<AndroidProxyValue>> values = new Values();

    public AndroidObjectFactories(ManagedCollectionFactory managedCollectionFactory) {
        this.managedCollectionFactory = managedCollectionFactory;
    }

    public ProxyObject.Factory<AndroidProxyAutomation> automation() {
        return automation;
    }

    public ProxyObject.Factory<AndroidProxyList<AndroidProxyAutomation>> automations() {
        return automations;
    }

    public ProxyObject.Factory<AndroidProxyCommand> command() {
        return command;
    }

    public ProxyObject.Factory<AndroidProxyList<AndroidProxyCommand>> commands() {
        return commands;
    }

    public ProxyObject.Factory<AndroidProxyCondition> condition() {
        return condition;
    }

    public ProxyObject.Factory<AndroidProxyList<AndroidProxyCondition>> conditions() {
        return conditions;
    }

    public ProxyObject.Factory<AndroidProxyConnectedDevice> connectedDevice() {
        return connectedDevice;
    }

    public ProxyObject.Factory<AndroidProxyList<AndroidProxyConnectedDevice>> connectedDevices() {
        return connectedDevices;
    }

    public ProxyObject.Factory<AndroidProxyHardware> hardware() {
        return hardware;
    }

    public ProxyObject.Factory<AndroidProxyList<AndroidProxyHardware>> hardwares() {
        return hardwares;
    }

    public ProxyObject.Factory<AndroidProxyNode> node() {
        return node;
    }

    public ProxyObject.Factory<AndroidProxyList<AndroidProxyNode>> nodes() {
        return nodes;
    }

    public ProxyObject.Factory<AndroidProxyOption> option() {
        return option;
    }

    public ProxyObject.Factory<AndroidProxyList<AndroidProxyOption>> options() {
        return options;
    }

    public ProxyObject.Factory<AndroidProxyParameter> parameter() {
        return parameter;
    }

    public ProxyObject.Factory<AndroidProxyList<AndroidProxyParameter>> parameters() {
        return parameters;
    }

    public ProxyObject.Factory<AndroidProxyProperty> property() {
        return property;
    }

    public ProxyObject.Factory<AndroidProxyList<AndroidProxyProperty>> properties() {
        return properties;
    }

    public ProxyObject.Factory<AndroidProxySubType> subType() {
        return subType;
    }

    public ProxyObject.Factory<AndroidProxyList<AndroidProxySubType>> subTypes() {
        return subTypes;
    }

    public ProxyObject.Factory<AndroidProxySystem> system() {
        return system;
    }

    public ProxyObject.Factory<AndroidProxyList<AndroidProxySystem>> systems() {
        return systems;
    }

    public ProxyObject.Factory<AndroidProxyTask> task() {
        return task;
    }

    public ProxyObject.Factory<AndroidProxyList<AndroidProxyTask>> tasks() {
        return tasks;
    }

    public ProxyObject.Factory<AndroidProxyType> type() {
        return type;
    }

    public ProxyObject.Factory<AndroidProxyList<AndroidProxyType>> types() {
        return types;
    }

    public ProxyObject.Factory<AndroidProxyUser> user() {
        return user;
    }

    public ProxyObject.Factory<AndroidProxyList<AndroidProxyUser>> users() {
        return users;
    }

    public ProxyObject.Factory<AndroidProxyValue> value() {
        return value;
    }

    public ProxyObject.Factory<AndroidProxyList<AndroidProxyValue>> values() {
        return values;
    }

    public class Automation implements ProxyObject.Factory<AndroidProxyAutomation> {

        @Override
        public AndroidProxyAutomation create(Logger logger) {
            return new AndroidProxyAutomation(logger, managedCollectionFactory, AndroidObjectFactories.this);
        }
    }

    public class Automations implements ProxyObject.Factory<AndroidProxyList<AndroidProxyAutomation>> {

        @Override
        public AndroidProxyList<AndroidProxyAutomation> create(Logger logger) {
            return new AndroidProxyList<>(logger, managedCollectionFactory, automation);
        }
    }

    public class Command implements ProxyObject.Factory<AndroidProxyCommand> {

        @Override
        public AndroidProxyCommand create(Logger logger) {
            return new AndroidProxyCommand(logger, managedCollectionFactory, AndroidObjectFactories.this);
        }
    }

    public class Commands implements ProxyObject.Factory<AndroidProxyList<AndroidProxyCommand>> {

        @Override
        public AndroidProxyList<AndroidProxyCommand> create(Logger logger) {
            return new AndroidProxyList<>(logger, managedCollectionFactory, command);
        }
    }

    public class Condition implements ProxyObject.Factory<AndroidProxyCondition> {

        @Override
        public AndroidProxyCondition create(Logger logger) {
            return new AndroidProxyCondition(logger, managedCollectionFactory, AndroidObjectFactories.this);
        }
    }

    public class Conditions implements ProxyObject.Factory<AndroidProxyList<AndroidProxyCondition>> {

        @Override
        public AndroidProxyList<AndroidProxyCondition> create(Logger logger) {
            return new AndroidProxyList<>(logger, managedCollectionFactory, condition);
        }
    }

    public class ConnectedDevice implements ProxyObject.Factory<AndroidProxyConnectedDevice> {

        @Override
        public AndroidProxyConnectedDevice create(Logger logger) {
            return new AndroidProxyConnectedDevice(logger, managedCollectionFactory, AndroidObjectFactories.this);
        }
    }

    public class ConnectedDevices implements ProxyObject.Factory<AndroidProxyList<AndroidProxyConnectedDevice>> {

        @Override
        public AndroidProxyList<AndroidProxyConnectedDevice> create(Logger logger) {
            return new AndroidProxyList<>(logger, managedCollectionFactory, connectedDevice);
        }
    }

    public class System implements ProxyObject.Factory<AndroidProxySystem> {

        @Override
        public AndroidProxySystem create(Logger logger) {
            return new AndroidProxySystem(logger, managedCollectionFactory, AndroidObjectFactories.this);
        }
    }

    public class Systems implements ProxyObject.Factory<AndroidProxyList<AndroidProxySystem>> {

        @Override
        public AndroidProxyList<AndroidProxySystem> create(Logger logger) {
            return new AndroidProxyList<>(logger, managedCollectionFactory, system);
        }
    }

    public class Hardware implements ProxyObject.Factory<AndroidProxyHardware> {

        @Override
        public AndroidProxyHardware create(Logger logger) {
            return new AndroidProxyHardware(logger, managedCollectionFactory, AndroidObjectFactories.this);
        }
    }

    public class Hardwares implements ProxyObject.Factory<AndroidProxyList<AndroidProxyHardware>> {

        @Override
        public AndroidProxyList<AndroidProxyHardware> create(Logger logger) {
            return new AndroidProxyList<>(logger, managedCollectionFactory, hardware);
        }
    }

    public class Node implements ProxyObject.Factory<AndroidProxyNode> {

        @Override
        public AndroidProxyNode create(Logger logger) {
            return new AndroidProxyNode(logger, managedCollectionFactory, AndroidObjectFactories.this);
        }
    }

    public class Nodes implements ProxyObject.Factory<AndroidProxyList<AndroidProxyNode>> {

        @Override
        public AndroidProxyList<AndroidProxyNode> create(Logger logger) {
            return new AndroidProxyList<>(logger, managedCollectionFactory, node);
        }
    }

    public class Option implements ProxyObject.Factory<AndroidProxyOption> {

        @Override
        public AndroidProxyOption create(Logger logger) {
            return new AndroidProxyOption(logger, managedCollectionFactory, AndroidObjectFactories.this);
        }
    }

    public class Options implements ProxyObject.Factory<AndroidProxyList<AndroidProxyOption>> {

        @Override
        public AndroidProxyList<AndroidProxyOption> create(Logger logger) {
            return new AndroidProxyList<>(logger, managedCollectionFactory, option);
        }
    }

    public class Parameter implements ProxyObject.Factory<AndroidProxyParameter> {

        @Override
        public AndroidProxyParameter create(Logger logger) {
            return new AndroidProxyParameter(logger, managedCollectionFactory);
        }
    }

    public class Parameters implements ProxyObject.Factory<AndroidProxyList<AndroidProxyParameter>> {

        @Override
        public AndroidProxyList<AndroidProxyParameter> create(Logger logger) {
            return new AndroidProxyList<>(logger, managedCollectionFactory, parameter);
        }
    }

    public class Property implements ProxyObject.Factory<AndroidProxyProperty> {

        @Override
        public AndroidProxyProperty create(Logger logger) {
            return new AndroidProxyProperty(logger, managedCollectionFactory, AndroidObjectFactories.this);
        }
    }

    public class Properties implements ProxyObject.Factory<AndroidProxyList<AndroidProxyProperty>> {

        @Override
        public AndroidProxyList<AndroidProxyProperty> create(Logger logger) {
            return new AndroidProxyList<>(logger, managedCollectionFactory, property);
        }
    }

    public class SubType implements ProxyObject.Factory<AndroidProxySubType> {

        @Override
        public AndroidProxySubType create(Logger logger) {
            return new AndroidProxySubType(logger, managedCollectionFactory);
        }
    }

    public class SubTypes implements ProxyObject.Factory<AndroidProxyList<AndroidProxySubType>> {

        @Override
        public AndroidProxyList<AndroidProxySubType> create(Logger logger) {
            return new AndroidProxyList<>(logger, managedCollectionFactory, subType);
        }
    }

    public class Task implements ProxyObject.Factory<AndroidProxyTask> {

        @Override
        public AndroidProxyTask create(Logger logger) {
            return new AndroidProxyTask(logger, managedCollectionFactory, AndroidObjectFactories.this);
        }
    }

    public class Tasks implements ProxyObject.Factory<AndroidProxyList<AndroidProxyTask>> {

        @Override
        public AndroidProxyList<AndroidProxyTask> create(Logger logger) {
            return new AndroidProxyList<>(logger, managedCollectionFactory,task);
        }
    }

    public class Type implements ProxyObject.Factory<AndroidProxyType> {

        @Override
        public AndroidProxyType create(Logger logger) {
            return new AndroidProxyType(logger, managedCollectionFactory);
        }
    }

    public class Types implements ProxyObject.Factory<AndroidProxyList<AndroidProxyType>> {

        @Override
        public AndroidProxyList<AndroidProxyType> create(Logger logger) {
            return new AndroidProxyList<>(logger, managedCollectionFactory, type);
        }
    }

    public class User implements ProxyObject.Factory<AndroidProxyUser> {

        @Override
        public AndroidProxyUser create(Logger logger) {
            return new AndroidProxyUser(logger, managedCollectionFactory, AndroidObjectFactories.this);
        }
    }

    public class Users implements ProxyObject.Factory<AndroidProxyList<AndroidProxyUser>> {

        @Override
        public AndroidProxyList<AndroidProxyUser> create(Logger logger) {
            return new AndroidProxyList<>(logger, managedCollectionFactory, user);
        }
    }

    public class Value implements ProxyObject.Factory<AndroidProxyValue> {

        @Override
        public AndroidProxyValue create(Logger logger) {
            return new AndroidProxyValue(logger, managedCollectionFactory);
        }
    }

    public class Values implements ProxyObject.Factory<AndroidProxyList<AndroidProxyValue>> {

        @Override
        public AndroidProxyList<AndroidProxyValue> create(Logger logger) {
            return new AndroidProxyList<>(logger, managedCollectionFactory, value);
        }
    }
}
