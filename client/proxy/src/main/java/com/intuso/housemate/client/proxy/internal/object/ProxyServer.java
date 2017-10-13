package com.intuso.housemate.client.proxy.internal.object;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.AbstractIdleService;
import com.google.inject.Inject;
import com.intuso.housemate.client.api.internal.HousemateException;
import com.intuso.housemate.client.api.internal.object.*;
import com.intuso.housemate.client.api.internal.object.Object;
import com.intuso.housemate.client.api.internal.object.view.CommandView;
import com.intuso.housemate.client.api.internal.object.view.ListView;
import com.intuso.housemate.client.api.internal.object.view.ServerView;
import com.intuso.housemate.client.api.internal.object.view.View;
import com.intuso.housemate.client.api.internal.type.ObjectReference;
import com.intuso.housemate.client.messaging.api.internal.Receiver;
import com.intuso.housemate.client.proxy.internal.ChildUtil;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * @param <COMMAND> the type of the command
 * @param <AUTOMATIONS> the type of the automations list
 * @param <USERS> the type of the users list
 * @param <NODES> the type of the nodes list
 * @param <SERVER> the type of the server
 */
public abstract class ProxyServer<
        COMMAND extends ProxyCommand<?, ?, ?>,
        VALUE extends ProxyValue<?, ?>,
        VALUES extends ProxyList<VALUE, ?>,
        DEVICE extends ProxyDevice<?, ?, ?, ?, ?, ?, ?>,
        AUTOMATIONS extends ProxyList<? extends ProxyAutomation<?, ?, ?, ?, ?>, ?>,
        DEVICE_GROUPS extends ProxyList<? extends ProxyDeviceGroup<?, ?, ?, ?, ?, ?>, ?>,
        USERS extends ProxyList<? extends ProxyUser<?, ?, ?>, ?>,
        NODES extends ProxyList<? extends ProxyNode<?, ?, ?, ?>, ?>,
        SERVER extends ProxyServer<COMMAND, VALUE, VALUES, DEVICE, AUTOMATIONS, DEVICE_GROUPS, USERS, NODES, SERVER>>
        extends ProxyObject<Server.Data, Server.Listener<? super SERVER>, ServerView>
        implements Server<COMMAND, ConvertingList<VALUE, DEVICE>, AUTOMATIONS, DEVICE_GROUPS, USERS, NODES, SERVER> {

    private final ManagedCollectionFactory managedCollectionFactory;
    private final Map<List<?, ?>, Map<String, Map<ObjectReferenceImpl, Integer>>> missingReferences = new HashMap<>();
    private final Map<Object<?, ?, ?>, java.util.List<ObjectReferenceImpl>> references = Maps.newHashMap();

    private ProxyObject.Factory<COMMAND> commandFactory;
    private ProxyObject.Factory<VALUES> valuesFactory;
    private ProxyObject.Factory<AUTOMATIONS> automationsFactory;
    private ProxyObject.Factory<DEVICE_GROUPS> deviceGroupsFactory;
    private ProxyObject.Factory<USERS> usersFactory;
    private ProxyObject.Factory<NODES> nodesFactory;

    private final VALUES deviceReferences;
    private final ProxyConvertingList<VALUE, DEVICE> devices;

    private AUTOMATIONS automations;
    private COMMAND addAutomationCommand;
    private DEVICE_GROUPS deviceGroups;
    private COMMAND addDeviceGroupCommand;
    private USERS users;
    private COMMAND addUserCommand;
    private NODES nodes;

    private List.Listener<Object<?, ?, ?>, List<? extends Object<?, ?, ?>, ?>> missingReferenceLoader = new List.Listener<Object<?, ?, ?>, List<? extends Object<?, ?, ?>, ?>>() {
        @Override
        public void elementAdded(List<? extends Object<?, ?, ?>, ?> list, Object<?, ?, ?> element) {
            updateMissingReferences(list, element.getId());
        }

        @Override
        public void elementRemoved(List<? extends Object<?, ?, ?>, ?> list, Object<?, ?, ?> element) {
            // todo should update references at or below this object to say that the object has been removed!
        }
    };

    public ProxyServer(Logger logger,
                       ManagedCollectionFactory managedCollectionFactory,
                       Receiver.Factory receiverFactory) {
        super(logger, ChildUtil.name(null, PROXY, VERSION), Server.Data.class, managedCollectionFactory, receiverFactory);
        this.managedCollectionFactory = managedCollectionFactory;
        deviceReferences = valuesFactory.create(ChildUtil.logger(logger, DEVICES_ID), ChildUtil.name(name, DEVICES_ID));
        devices = new ProxyConvertingList<>(deviceReferences, new ReferenceLoaderConverter<DEVICE>());
        deviceReferences.load(new ListView(View.Mode.ANCESTORS));
    }

    public ProxyServer(Logger logger,
                       ManagedCollectionFactory managedCollectionFactory,
                       Receiver.Factory receiverFactory,
                       Factory<COMMAND> commandFactory,
                       Factory<VALUES> valuesFactory,
                       Factory<AUTOMATIONS> automationsFactory,
                       Factory<DEVICE_GROUPS> deviceGroupsFactory,
                       Factory<USERS> usersFactory,
                       Factory<NODES> nodesFactory) {
        super(logger, ChildUtil.name(null, PROXY, VERSION), Server.Data.class, managedCollectionFactory, receiverFactory);
        this.managedCollectionFactory = managedCollectionFactory;
        this.commandFactory = commandFactory;
        this.valuesFactory = valuesFactory;
        this.automationsFactory = automationsFactory;
        this.deviceGroupsFactory = deviceGroupsFactory;
        this.usersFactory = usersFactory;
        this.nodesFactory = nodesFactory;
        deviceReferences = valuesFactory.create(ChildUtil.logger(logger, DEVICES_ID), ChildUtil.name(name, DEVICES_ID));
        devices = new ProxyConvertingList<>(deviceReferences, new ReferenceLoaderConverter<DEVICE>());
        deviceReferences.load(new ListView(View.Mode.ANCESTORS));
    }

    public void setCommandFactory(Factory<COMMAND> commandFactory) {
        this.commandFactory = commandFactory;
    }

    public void setValuesFactory(Factory<VALUES> valuesFactory) {
        this.valuesFactory = valuesFactory;
    }

    public void setAutomationsFactory(Factory<AUTOMATIONS> automationsFactory) {
        this.automationsFactory = automationsFactory;
    }

    public void setDeviceGroupsFactory(Factory<DEVICE_GROUPS> deviceGroupsFactory) {
        this.deviceGroupsFactory = deviceGroupsFactory;
    }

    public void setUsersFactory(Factory<USERS> usersFactory) {
        this.usersFactory = usersFactory;
    }

    public void setNodesFactory(Factory<NODES> nodesFactory) {
        this.nodesFactory = nodesFactory;
    }

    public void start() {
        // nothing to do, wait for user to view something
    }

    public void stop() {
        uninit();
    }

    @Override
    public ServerView createView(View.Mode mode) {
        return new ServerView(mode);
    }

    @Override
    public Tree getTree(ServerView view) {

        // make sure what they want is loaded
        load(view);

        // create a result even for a null view
        Tree result = new Tree(getData());

        // get anything else the view wants
        if(view != null && view.getMode() != null) {
            switch (view.getMode()) {

                // get recursively
                case ANCESTORS:
                    result.getChildren().put(AUTOMATIONS_ID, automations.getTree(new ListView(View.Mode.ANCESTORS)));
                    result.getChildren().put(ADD_AUTOMATION_ID, addAutomationCommand.getTree(new CommandView(View.Mode.ANCESTORS)));
                    result.getChildren().put(DEVICES_ID, devices.getTree(new ListView(View.Mode.ANCESTORS)));
                    result.getChildren().put(DEVICE_GROUPS_ID, deviceGroups.getTree(new ListView(View.Mode.ANCESTORS)));
                    result.getChildren().put(ADD_DEVICE_GROUP_ID, addDeviceGroupCommand.getTree(new CommandView(View.Mode.ANCESTORS)));
                    result.getChildren().put(USERS_ID, users.getTree(new ListView(View.Mode.ANCESTORS)));
                    result.getChildren().put(ADD_USER_ID, addUserCommand.getTree(new CommandView(View.Mode.ANCESTORS)));
                    result.getChildren().put(NODES_ID, nodes.getTree(new ListView(View.Mode.ANCESTORS)));
                    break;

                    // get all children using inner view. NB all children non-null because of load(). Can give children null views
                case CHILDREN:
                    result.getChildren().put(AUTOMATIONS_ID, automations.getTree(view.getAutomationsView()));
                    result.getChildren().put(ADD_AUTOMATION_ID, addAutomationCommand.getTree(view.getAddAutomationCommandView()));
                    result.getChildren().put(DEVICES_ID, devices.getTree(view.getDevicesView()));
                    result.getChildren().put(DEVICE_GROUPS_ID, deviceGroups.getTree(view.getDeviceGroupsView()));
                    result.getChildren().put(ADD_DEVICE_GROUP_ID, addDeviceGroupCommand.getTree(view.getAddDeviceGroupCommandView()));
                    result.getChildren().put(USERS_ID, users.getTree(view.getUsersView()));
                    result.getChildren().put(ADD_USER_ID, addUserCommand.getTree(view.getAddUserCommandView()));
                    result.getChildren().put(NODES_ID, nodes.getTree(view.getNodesView()));
                    break;

                case SELECTION:
                    if(view.getAutomationsView() != null)
                        result.getChildren().put(AUTOMATIONS_ID, automations.getTree(view.getAutomationsView()));
                    if(view.getAddAutomationCommandView() != null)
                        result.getChildren().put(ADD_AUTOMATION_ID, addAutomationCommand.getTree(view.getAddAutomationCommandView()));
                    if(view.getDevicesView() != null)
                        result.getChildren().put(DEVICES_ID, devices.getTree(view.getDevicesView()));
                    if(view.getDeviceGroupsView() != null)
                        result.getChildren().put(DEVICE_GROUPS_ID, deviceGroups.getTree(view.getDeviceGroupsView()));
                    if(view.getAddDeviceGroupCommandView() != null)
                        result.getChildren().put(ADD_DEVICE_GROUP_ID, addDeviceGroupCommand.getTree(view.getAddDeviceGroupCommandView()));
                    if(view.getUsersView() != null)
                        result.getChildren().put(USERS_ID, users.getTree(view.getUsersView()));
                    if(view.getAddUserCommandView() != null)
                        result.getChildren().put(ADD_USER_ID, addUserCommand.getTree(view.getAddUserCommandView()));
                    if(view.getNodesView() != null)
                        result.getChildren().put(NODES_ID, nodes.getTree(view.getNodesView()));
                    break;
            }

        }

        return result;
    }

    @Override
    public void load(ServerView view) {

        super.load(view);

        if(view == null || view.getMode() == null)
            return;

        // create things according to the view's mode, sub-views, and what's already created
        switch (view.getMode()) {
            case ANCESTORS:
            case CHILDREN:
                if(automations == null)
                    automations = automationsFactory.create(ChildUtil.logger(logger, AUTOMATIONS_ID), ChildUtil.name(name, AUTOMATIONS_ID));
                if(addAutomationCommand == null)
                    addAutomationCommand = commandFactory.create(ChildUtil.logger(logger, ADD_AUTOMATION_ID), ChildUtil.name(name, ADD_AUTOMATION_ID));
                if(deviceGroups == null)
                    deviceGroups = deviceGroupsFactory.create(ChildUtil.logger(logger, DEVICE_GROUPS_ID), ChildUtil.name(name, DEVICE_GROUPS_ID));
                if(addDeviceGroupCommand == null)
                    addDeviceGroupCommand = commandFactory.create(ChildUtil.logger(logger, ADD_DEVICE_GROUP_ID), ChildUtil.name(name, ADD_DEVICE_GROUP_ID));
                if(users == null)
                    users = usersFactory.create(ChildUtil.logger(logger, USERS_ID), ChildUtil.name(name, USERS_ID));
                if(addUserCommand == null)
                    addUserCommand = commandFactory.create(ChildUtil.logger(logger, ADD_USER_ID), ChildUtil.name(name, ADD_USER_ID));
                if(nodes == null)
                    nodes = nodesFactory.create(ChildUtil.logger(logger, NODES_ID), ChildUtil.name(name, NODES_ID));
                break;
            case SELECTION:
                if(automations == null && view.getAutomationsView() != null)
                    automations = automationsFactory.create(ChildUtil.logger(logger, AUTOMATIONS_ID), ChildUtil.name(name, AUTOMATIONS_ID));
                if(addAutomationCommand == null && view.getAddAutomationCommandView() != null)
                    addAutomationCommand = commandFactory.create(ChildUtil.logger(logger, ADD_AUTOMATION_ID), ChildUtil.name(name, ADD_AUTOMATION_ID));
                if(deviceGroups == null && view.getDeviceGroupsView() != null)
                    deviceGroups = deviceGroupsFactory.create(ChildUtil.logger(logger, DEVICE_GROUPS_ID), ChildUtil.name(name, DEVICE_GROUPS_ID));
                if(addDeviceGroupCommand == null && view.getAddDeviceGroupCommandView() != null)
                    addDeviceGroupCommand = commandFactory.create(ChildUtil.logger(logger, ADD_DEVICE_GROUP_ID), ChildUtil.name(name, ADD_DEVICE_GROUP_ID));
                if(users == null && view.getUsersView() != null)
                    users = usersFactory.create(ChildUtil.logger(logger, USERS_ID), ChildUtil.name(name, USERS_ID));
                if(addUserCommand == null && view.getAddUserCommandView() != null)
                    addUserCommand = commandFactory.create(ChildUtil.logger(logger, ADD_USER_ID), ChildUtil.name(name, ADD_USER_ID));
                if(nodes == null && view.getNodesView() != null)
                    nodes = nodesFactory.create(ChildUtil.logger(logger, NODES_ID), ChildUtil.name(name, NODES_ID));
                break;
        }

        // view things according to the view's mode and sub-views
        switch (view.getMode()) {
            case ANCESTORS:
                automations.load(new ListView(View.Mode.ANCESTORS));
                addAutomationCommand.load(new CommandView(View.Mode.ANCESTORS));
                for(DEVICE device : devices)
                    ((ProxyObject) device).load(device.createView(View.Mode.ANCESTORS));
                deviceGroups.load(new ListView(View.Mode.ANCESTORS));
                addDeviceGroupCommand.load(new CommandView(View.Mode.ANCESTORS));
                users.load(new ListView(View.Mode.ANCESTORS));
                addUserCommand.load(new CommandView(View.Mode.ANCESTORS));
                nodes.load(new ListView(View.Mode.ANCESTORS));
                break;
            case CHILDREN:
            case SELECTION:
                if(view.getAutomationsView() != null)
                    automations.load(view.getAutomationsView());
                if(view.getAddAutomationCommandView() != null)
                    addAutomationCommand.load(view.getAddAutomationCommandView());
                if(view.getDevicesView() != null)
                    devices.load(view.getDevicesView());
                if(view.getDeviceGroupsView() != null)
                    deviceGroups.load(view.getDeviceGroupsView());
                if(view.getAddDeviceGroupCommandView() != null)
                    addDeviceGroupCommand.load(view.getAddDeviceGroupCommandView());
                if(view.getUsersView() != null)
                    users.load(view.getUsersView());
                if(view.getAddUserCommandView() != null)
                    addUserCommand.load(view.getAddUserCommandView());
                if(view.getNodesView() != null)
                    nodes.load(view.getNodesView());
                break;
        }
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        deviceReferences.uninit();
        if(automations != null)
            automations.uninit();
        if(addAutomationCommand != null)
            addAutomationCommand.uninit();
        if(deviceGroups != null)
            deviceGroups.uninit();
        if(addDeviceGroupCommand != null)
            addDeviceGroupCommand.uninit();
        if(users != null)
            users.uninit();
        if(addUserCommand != null)
            addUserCommand.uninit();
        if(nodes != null)
            nodes.uninit();
    }

    public VALUES getDeviceReferences() {
        return deviceReferences;
    }

    @Override
    public ConvertingList<VALUE, DEVICE> getDevices() {
        return devices;
    }

    @Override
    public AUTOMATIONS getAutomations() {
        return automations;
    }

    @Override
    public COMMAND getAddAutomationCommand() {
        return addAutomationCommand;
    }

    @Override
    public DEVICE_GROUPS getDeviceGroups() {
        return deviceGroups;
    }

    public COMMAND getAddDeviceGroupCommand() {
        return addDeviceGroupCommand;
    }

    @Override
    public USERS getUsers() {
        return users;
    }

    @Override
    public COMMAND getAddUserCommand() {
        return addUserCommand;
    }

    @Override
    public NODES getNodes() {
        return nodes;
    }

    @Override
    public Object<?, ?, ?> getChild(String id) {
        if(ADD_AUTOMATION_ID.equals(id)) {
            if(addAutomationCommand == null)
                addAutomationCommand = commandFactory.create(ChildUtil.logger(logger, ADD_AUTOMATION_ID), ChildUtil.name(name, ADD_AUTOMATION_ID));
            return addAutomationCommand;
        } else if(ADD_DEVICE_GROUP_ID.equals(id)) {
            if(addDeviceGroupCommand == null)
                addDeviceGroupCommand = commandFactory.create(ChildUtil.logger(logger, ADD_DEVICE_GROUP_ID), ChildUtil.name(name, ADD_DEVICE_GROUP_ID));
            return addDeviceGroupCommand;
        } else if(ADD_USER_ID.equals(id)) {
            if(addUserCommand == null)
                addUserCommand = commandFactory.create(ChildUtil.logger(logger, ADD_USER_ID), ChildUtil.name(name, ADD_USER_ID));
            return addUserCommand;
        } else if(DEVICES_ID.equals(id))
            return devices; // NB not an object that is loaded
        else if(AUTOMATIONS_ID.equals(id)) {
            if(automations == null)
                automations = automationsFactory.create(ChildUtil.logger(logger, AUTOMATIONS_ID), ChildUtil.name(name, AUTOMATIONS_ID));
            return automations;
        } else if(DEVICE_GROUPS_ID.equals(id)) {
            if(deviceGroups == null)
                deviceGroups = deviceGroupsFactory.create(ChildUtil.logger(logger, DEVICE_GROUPS_ID), ChildUtil.name(name, DEVICE_GROUPS_ID));
            return deviceGroups;
        } else if(NODES_ID.equals(id)) {
            if(nodes == null)
                nodes = nodesFactory.create(ChildUtil.logger(logger, NODES_ID), ChildUtil.name(name, NODES_ID));
            return nodes;
        } else if(USERS_ID.equals(id)) {
            if(users == null)
                users = usersFactory.create(ChildUtil.logger(logger, USERS_ID), ChildUtil.name(name, USERS_ID));
            return users;
        }
        return null;
    }

    public <OBJECT extends Object<?, ?, ?>> ConvertingList.Converter<ProxyValue<?, ?>, OBJECT> findConverter() {
        return new ReferenceLoaderConverter<>();
    }

    public <T extends Object<?, ?, ?>> T find(String[] path) {
        return find(path, true);
    }

    public <T extends Object<?, ?, ?>> T find(String[] path, boolean fail) {
        Object<?, ?, ?> current = this;
        for(int i = 0; i < path.length; i++) {
            current = current.getChild(path[i]);
            if(current == null) {
                if(fail) {
                    if (i == 0)
                        throw new HousemateException("Could not find " + path[i] + " for server");
                    else {
                        String[] subPath = new String[i];
                        System.arraycopy(path, 0, subPath, 0, i);
                        throw new HousemateException("Could not find " + path[i] + " at " + Joiner.on("/").join(subPath));
                    }
                } else
                    return null;
            }
        }
        return (T) current;
    }

    public <O extends Object<?, ?, ?>> ObjectReference<O> reference(String[] path) {
        ObjectReferenceImpl<O> reference = new ObjectReferenceImpl<>(managedCollectionFactory, path);
        reference(this, reference, 0);
        return reference;
    }

    protected void reference(Object<?, ?, ?> object, ObjectReferenceImpl reference, int pathIndex) {
        if(pathIndex == reference.getPath().length) {
            if(!references.containsKey(object))
                references.put(object, Lists.<ObjectReferenceImpl>newArrayList());
            references.get(object).add(reference);
            reference.setObject(this);
        } else {
            String id = reference.getPath()[pathIndex];
            Object<?, ?, ?> child = object.getChild(id);
            if(child != null)
                reference(child, reference, pathIndex + 1);
            else if(object instanceof List) {
                List<? extends Object<?, ?, ?>, ?> list = (List<? extends Object<?, ?, ?>, ?>) object;
                if(!missingReferences.containsKey(list))
                    missingReferences.put(list, new HashMap<String, Map<ObjectReferenceImpl, Integer>>());
                if(!missingReferences.get(list).containsKey(id))
                    missingReferences.get(list).put(id, new HashMap<ObjectReferenceImpl, Integer>());
                missingReferences.get(list).get(id).put(reference, pathIndex);
                list.addObjectListener(missingReferenceLoader);
            }
        }
    }

    protected void updateMissingReferences(List<?, ?> list, String id) {
        if(missingReferences.containsKey(list) && missingReferences.get(list).containsKey(id))
            for(Map.Entry<ObjectReferenceImpl, Integer> reference : missingReferences.get(list).remove(id).entrySet())
                reference(list, reference.getKey(), reference.getValue());
    }

    public static class Service extends AbstractIdleService {

        private final ProxyServer server;

        @Inject
        public Service(ProxyServer server) {
            this.server = server;
        }

        @Override
        protected void startUp() throws Exception {
            server.start();
        }

        @Override
        protected void shutDown() throws Exception {
            server.stop();
        }
    }

    /**
     * Created with IntelliJ IDEA.
     * User: tomc
     * Date: 14/01/14
     * Time: 13:17
     * To change this template use File | Settings | File Templates.
     */
    public static final class Simple extends ProxyServer<
            ProxyCommand.Simple,
            ProxyValue.Simple,
            ProxyList.Simple<ProxyValue.Simple>,
            ProxyDevice<?, ?, ?, ?, ?, ?, ?>,
            ProxyList.Simple<ProxyAutomation.Simple>,
            ProxyList.Simple<ProxyDeviceGroup.Simple>,
            ProxyList.Simple<ProxyUser.Simple>,
            ProxyList.Simple<ProxyNode.Simple>,
            Simple> {

        @Inject
        public Simple(@com.intuso.housemate.client.v1_0.proxy.object.ioc.Server Logger logger,
                      ManagedCollectionFactory managedCollectionFactory,
                      Receiver.Factory receiverFactory,
                      Factory<ProxyCommand.Simple> commandFactory,
                      Factory<ProxyList.Simple<ProxyValue.Simple>> valuesFactory,
                      Factory<ProxyList.Simple<ProxyAutomation.Simple>> automationsFactory,
                      Factory<ProxyList.Simple<ProxyDeviceGroup.Simple>> deviceGroupsFactory,
                      Factory<ProxyList.Simple<ProxyUser.Simple>> usersFactory,
                      Factory<ProxyList.Simple<ProxyNode.Simple>> nodesFactory) {
            super(logger, managedCollectionFactory, receiverFactory, commandFactory, valuesFactory, automationsFactory, deviceGroupsFactory, usersFactory, nodesFactory);
        }
    }

    private class ReferenceLoaderConverter<OBJECT extends Object<?, ?, ?>> implements ConvertingList.Converter<ProxyValue<?, ?>, OBJECT> {

        @Override
        public OBJECT apply(ProxyValue<?, ?> element) {
            if(element == null || element.getValue() == null || element.getValue().getFirstValue() == null)
                return null;
            return find(element.getValue().getFirstValue().split("/"));
        }
    }
}
