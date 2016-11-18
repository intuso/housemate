package com.intuso.housemate.platform.android.app.object;

import com.intuso.housemate.client.v1_0.proxy.api.object.ProxyObject;
import com.intuso.utilities.listener.ListenersFactory;
import org.slf4j.Logger;

/**
 * Created by tomc on 01/09/16.
 */
public class AndroidObjectFactories {

    private final ListenersFactory listenersFactory;

    private final ProxyObject.Factory<AndroidProxyAutomation> automation = new Automation();
    private final ProxyObject.Factory<AndroidProxyList<AndroidProxyAutomation>> automations = new Automations();
    private final ProxyObject.Factory<AndroidProxyCommand> command = new Command();
    private final ProxyObject.Factory<AndroidProxyList<AndroidProxyCommand>> commands = new Commands();
    private final ProxyObject.Factory<AndroidProxyCondition> condition = new Condition();
    private final ProxyObject.Factory<AndroidProxyList<AndroidProxyCondition>> conditions = new Conditions();
    private final ProxyObject.Factory<AndroidProxyDevice> device = new Device();
    private final ProxyObject.Factory<AndroidProxyList<AndroidProxyDevice>> devices = new Devices();
    private final ProxyObject.Factory<AndroidProxyFeature> feature = new Feature();
    private final ProxyObject.Factory<AndroidProxyList<AndroidProxyFeature>> features = new Features();
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
    private final ProxyObject.Factory<AndroidProxyTask> task = new Task();
    private final ProxyObject.Factory<AndroidProxyList<AndroidProxyTask>> tasks = new Tasks();
    private final ProxyObject.Factory<AndroidProxyType> type = new Type();
    private final ProxyObject.Factory<AndroidProxyList<AndroidProxyType>> types = new Types();
    private final ProxyObject.Factory<AndroidProxyUser> user = new User();
    private final ProxyObject.Factory<AndroidProxyList<AndroidProxyUser>> users = new Users();
    private final ProxyObject.Factory<AndroidProxyValue> value = new Value();
    private final ProxyObject.Factory<AndroidProxyList<AndroidProxyValue>> values = new Values();

    public AndroidObjectFactories(ListenersFactory listenersFactory) {
        this.listenersFactory = listenersFactory;
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

    public ProxyObject.Factory<AndroidProxyDevice> device() {
        return device;
    }

    public ProxyObject.Factory<AndroidProxyList<AndroidProxyDevice>> devices() {
        return devices;
    }

    public ProxyObject.Factory<AndroidProxyFeature> feature() {
        return feature;
    }

    public ProxyObject.Factory<AndroidProxyList<AndroidProxyFeature>> features() {
        return features;
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
            return new AndroidProxyAutomation(logger, listenersFactory, AndroidObjectFactories.this);
        }
    }

    public class Automations implements ProxyObject.Factory<AndroidProxyList<AndroidProxyAutomation>> {

        @Override
        public AndroidProxyList<AndroidProxyAutomation> create(Logger logger) {
            return new AndroidProxyList<>(logger, listenersFactory, automation);
        }
    }

    public class Command implements ProxyObject.Factory<AndroidProxyCommand> {

        @Override
        public AndroidProxyCommand create(Logger logger) {
            return new AndroidProxyCommand(logger, listenersFactory, AndroidObjectFactories.this);
        }
    }

    public class Commands implements ProxyObject.Factory<AndroidProxyList<AndroidProxyCommand>> {

        @Override
        public AndroidProxyList<AndroidProxyCommand> create(Logger logger) {
            return new AndroidProxyList<>(logger, listenersFactory, command);
        }
    }

    public class Condition implements ProxyObject.Factory<AndroidProxyCondition> {

        @Override
        public AndroidProxyCondition create(Logger logger) {
            return new AndroidProxyCondition(logger, listenersFactory, AndroidObjectFactories.this);
        }
    }

    public class Conditions implements ProxyObject.Factory<AndroidProxyList<AndroidProxyCondition>> {

        @Override
        public AndroidProxyList<AndroidProxyCondition> create(Logger logger) {
            return new AndroidProxyList<>(logger, listenersFactory, condition);
        }
    }

    public class Device implements ProxyObject.Factory<AndroidProxyDevice> {

        @Override
        public AndroidProxyDevice create(Logger logger) {
            return new AndroidProxyDevice(logger, listenersFactory, AndroidObjectFactories.this);
        }
    }

    public class Devices implements ProxyObject.Factory<AndroidProxyList<AndroidProxyDevice>> {

        @Override
        public AndroidProxyList<AndroidProxyDevice> create(Logger logger) {
            return new AndroidProxyList<>(logger, listenersFactory, device);
        }
    }

    public class Feature implements ProxyObject.Factory<AndroidProxyFeature> {

        @Override
        public AndroidProxyFeature create(Logger logger) {
            return new AndroidProxyFeature(logger, listenersFactory, AndroidObjectFactories.this);
        }
    }

    public class Features implements ProxyObject.Factory<AndroidProxyList<AndroidProxyFeature>> {

        @Override
        public AndroidProxyList<AndroidProxyFeature> create(Logger logger) {
            return new AndroidProxyList<>(logger, listenersFactory, feature);
        }
    }

    public class Hardware implements ProxyObject.Factory<AndroidProxyHardware> {

        @Override
        public AndroidProxyHardware create(Logger logger) {
            return new AndroidProxyHardware(logger, listenersFactory, AndroidObjectFactories.this);
        }
    }

    public class Hardwares implements ProxyObject.Factory<AndroidProxyList<AndroidProxyHardware>> {

        @Override
        public AndroidProxyList<AndroidProxyHardware> create(Logger logger) {
            return new AndroidProxyList<>(logger, listenersFactory, hardware);
        }
    }

    public class Node implements ProxyObject.Factory<AndroidProxyNode> {

        @Override
        public AndroidProxyNode create(Logger logger) {
            return new AndroidProxyNode(logger, listenersFactory, AndroidObjectFactories.this);
        }
    }

    public class Nodes implements ProxyObject.Factory<AndroidProxyList<AndroidProxyNode>> {

        @Override
        public AndroidProxyList<AndroidProxyNode> create(Logger logger) {
            return new AndroidProxyList<>(logger, listenersFactory, node);
        }
    }

    public class Option implements ProxyObject.Factory<AndroidProxyOption> {

        @Override
        public AndroidProxyOption create(Logger logger) {
            return new AndroidProxyOption(logger, listenersFactory, AndroidObjectFactories.this);
        }
    }

    public class Options implements ProxyObject.Factory<AndroidProxyList<AndroidProxyOption>> {

        @Override
        public AndroidProxyList<AndroidProxyOption> create(Logger logger) {
            return new AndroidProxyList<>(logger, listenersFactory, option);
        }
    }

    public class Parameter implements ProxyObject.Factory<AndroidProxyParameter> {

        @Override
        public AndroidProxyParameter create(Logger logger) {
            return new AndroidProxyParameter(logger, listenersFactory);
        }
    }

    public class Parameters implements ProxyObject.Factory<AndroidProxyList<AndroidProxyParameter>> {

        @Override
        public AndroidProxyList<AndroidProxyParameter> create(Logger logger) {
            return new AndroidProxyList<>(logger, listenersFactory, parameter);
        }
    }

    public class Property implements ProxyObject.Factory<AndroidProxyProperty> {

        @Override
        public AndroidProxyProperty create(Logger logger) {
            return new AndroidProxyProperty(logger, listenersFactory, AndroidObjectFactories.this);
        }
    }

    public class Properties implements ProxyObject.Factory<AndroidProxyList<AndroidProxyProperty>> {

        @Override
        public AndroidProxyList<AndroidProxyProperty> create(Logger logger) {
            return new AndroidProxyList<>(logger, listenersFactory, property);
        }
    }

    public class SubType implements ProxyObject.Factory<AndroidProxySubType> {

        @Override
        public AndroidProxySubType create(Logger logger) {
            return new AndroidProxySubType(logger, listenersFactory);
        }
    }

    public class SubTypes implements ProxyObject.Factory<AndroidProxyList<AndroidProxySubType>> {

        @Override
        public AndroidProxyList<AndroidProxySubType> create(Logger logger) {
            return new AndroidProxyList<>(logger, listenersFactory, subType);
        }
    }

    public class Task implements ProxyObject.Factory<AndroidProxyTask> {

        @Override
        public AndroidProxyTask create(Logger logger) {
            return new AndroidProxyTask(logger, listenersFactory, AndroidObjectFactories.this);
        }
    }

    public class Tasks implements ProxyObject.Factory<AndroidProxyList<AndroidProxyTask>> {

        @Override
        public AndroidProxyList<AndroidProxyTask> create(Logger logger) {
            return new AndroidProxyList<>(logger, listenersFactory,task);
        }
    }

    public class Type implements ProxyObject.Factory<AndroidProxyType> {

        @Override
        public AndroidProxyType create(Logger logger) {
            return new AndroidProxyType(logger, listenersFactory);
        }
    }

    public class Types implements ProxyObject.Factory<AndroidProxyList<AndroidProxyType>> {

        @Override
        public AndroidProxyList<AndroidProxyType> create(Logger logger) {
            return new AndroidProxyList<>(logger, listenersFactory, type);
        }
    }

    public class User implements ProxyObject.Factory<AndroidProxyUser> {

        @Override
        public AndroidProxyUser create(Logger logger) {
            return new AndroidProxyUser(logger, listenersFactory, AndroidObjectFactories.this);
        }
    }

    public class Users implements ProxyObject.Factory<AndroidProxyList<AndroidProxyUser>> {

        @Override
        public AndroidProxyList<AndroidProxyUser> create(Logger logger) {
            return new AndroidProxyList<>(logger, listenersFactory, user);
        }
    }

    public class Value implements ProxyObject.Factory<AndroidProxyValue> {

        @Override
        public AndroidProxyValue create(Logger logger) {
            return new AndroidProxyValue(logger, listenersFactory);
        }
    }

    public class Values implements ProxyObject.Factory<AndroidProxyList<AndroidProxyValue>> {

        @Override
        public AndroidProxyList<AndroidProxyValue> create(Logger logger) {
            return new AndroidProxyList<>(logger, listenersFactory, value);
        }
    }
}
