package com.intuso.housemate.client.proxy.internal.object;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.AbstractIdleService;
import com.google.inject.Inject;
import com.intuso.housemate.client.api.internal.HousemateException;
import com.intuso.housemate.client.api.internal.object.ConvertingList;
import com.intuso.housemate.client.api.internal.object.List;
import com.intuso.housemate.client.api.internal.object.Object;
import com.intuso.housemate.client.api.internal.object.Server;
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
        DEVICE extends ProxyDevice<?, ?, ?, ?, ?, ?>,
        AUTOMATIONS extends ProxyList<? extends ProxyAutomation<?, ?, ?, ?, ?>, ?>,
        DEVICE_GROUPS extends ProxyList<? extends ProxyDeviceGroup<?, ?, ?, ?, ?, ?>, ?>,
        USERS extends ProxyList<? extends ProxyUser<?, ?, ?>, ?>,
        NODES extends ProxyList<? extends ProxyNode<?, ?, ?, ?>, ?>,
        SERVER extends ProxyServer<COMMAND, VALUE, VALUES, DEVICE, AUTOMATIONS, DEVICE_GROUPS, USERS, NODES, SERVER>>
        extends ProxyObject<Server.Data, Server.Listener<? super SERVER>>
        implements Server<COMMAND, ConvertingList<VALUE, DEVICE>, AUTOMATIONS, DEVICE_GROUPS, USERS, NODES, SERVER> {

    private final ManagedCollectionFactory managedCollectionFactory;
    private final Map<List<?, ?>, Map<String, Map<ObjectReferenceImpl, Integer>>> missingReferences = new HashMap<>();
    private final Map<Object<?, ?>, java.util.List<ObjectReferenceImpl>> references = Maps.newHashMap();

    private final VALUES deviceReferences;
    private final ConvertingList<VALUE, DEVICE> devices;
    private final AUTOMATIONS automations;
    private final COMMAND addAutomationCommand;
    private final DEVICE_GROUPS deviceGroups;
    private final COMMAND addSystemCommand;
    private final USERS users;
    private final COMMAND addUserCommand;
    private final NODES nodes;
    private List.Listener<Object<?, ?>, List<? extends Object<?, ?>, ?>> missingReferenceLoader = new List.Listener<Object<?, ?>, List<? extends Object<?, ?>, ?>>() {
        @Override
        public void elementAdded(List<? extends Object<?, ?>, ?> list, Object<?, ?> element) {
            updateMissingReferences(list, element.getId());
        }

        @Override
        public void elementRemoved(List<? extends Object<?, ?>, ?> list, Object<?, ?> element) {
            // todo should update references at or below this object to say that the object has been removed!
        }
    };

    public ProxyServer(Logger logger,
                       ManagedCollectionFactory managedCollectionFactory,
                       Receiver.Factory receiverFactory) {
        super(logger, Server.Data.class, managedCollectionFactory, receiverFactory);
        this.managedCollectionFactory = managedCollectionFactory;
        deviceReferences = createValues(ChildUtil.logger(logger, DEVICES_ID));
        devices = new ConvertingList<>(deviceReferences, new ReferenceLoaderConverter<DEVICE>());
        automations = createAutomations(ChildUtil.logger(logger, AUTOMATIONS_ID));
        addAutomationCommand = createCommand(ChildUtil.logger(logger, ADD_AUTOMATION_ID));
        deviceGroups = createDeviceGroups(ChildUtil.logger(logger, DEVICE_GROUPS_ID));
        addSystemCommand = createCommand(ChildUtil.logger(logger, ADD_SYSTEM_ID));
        users = createUsers(ChildUtil.logger(logger, USERS_ID));
        addUserCommand = createCommand(ChildUtil.logger(logger, ADD_USER_ID));
        nodes = createNodes(ChildUtil.logger(logger, NODES_ID));
    }

    protected COMMAND createCommand(Logger logger) {
        throw new UnsupportedOperationException("Not implemented in this proxy implementation");
    }

    protected VALUES createValues(Logger logger) {
        throw new UnsupportedOperationException("Not implemented in this proxy implementation");
    }

    protected AUTOMATIONS createAutomations(Logger logger) {
        throw new UnsupportedOperationException("Not implemented in this proxy implementation");
    }

    protected DEVICE_GROUPS createDeviceGroups(Logger logger) {
        throw new UnsupportedOperationException("Not implemented in this proxy implementation");
    }

    protected USERS createUsers(Logger logger) {
        throw new UnsupportedOperationException("Not implemented in this proxy implementation");
    }

    protected NODES createNodes(Logger logger) {
        throw new UnsupportedOperationException("Not implemented in this proxy implementation");
    }


    public ProxyServer(Logger logger,
                       ManagedCollectionFactory managedCollectionFactory,
                       Receiver.Factory receiverFactory,
                       Factory<COMMAND> commandFactory,
                       Factory<VALUES> valuesFactory,
                       Factory<AUTOMATIONS> automationsFactory,
                       Factory<DEVICE_GROUPS> systemsFactory,
                       Factory<USERS> usersFactory,
                       Factory<NODES> nodesFactory) {
        super(logger, Server.Data.class, managedCollectionFactory, receiverFactory);
        this.managedCollectionFactory = managedCollectionFactory;
        deviceReferences = valuesFactory.create(ChildUtil.logger(logger, DEVICES_ID));
        devices = new ConvertingList<>(deviceReferences, new ReferenceLoaderConverter<DEVICE>());
        automations = automationsFactory.create(ChildUtil.logger(logger, AUTOMATIONS_ID));
        addAutomationCommand = commandFactory.create(ChildUtil.logger(logger, ADD_AUTOMATION_ID));
        deviceGroups = systemsFactory.create(ChildUtil.logger(logger, DEVICE_GROUPS_ID));
        addSystemCommand = commandFactory.create(ChildUtil.logger(logger, ADD_SYSTEM_ID));
        users = usersFactory.create(ChildUtil.logger(logger, USERS_ID));
        addUserCommand = commandFactory.create(ChildUtil.logger(logger, ADD_USER_ID));
        nodes = nodesFactory.create(ChildUtil.logger(logger, NODES_ID));
    }

    public void start() {
        init(ChildUtil.name(null, PROXY, VERSION));
    }

    public void stop() {
        uninit();
    }

    @Override
    protected void initChildren(String name) {
        super.initChildren(name);
        deviceReferences.init(ChildUtil.name(name, DEVICES_ID));
        automations.init(ChildUtil.name(name, AUTOMATIONS_ID));
        addAutomationCommand.init(ChildUtil.name(name, ADD_AUTOMATION_ID));
        deviceGroups.init(ChildUtil.name(name, DEVICE_GROUPS_ID));
        addSystemCommand.init(ChildUtil.name(name, ADD_SYSTEM_ID));
        users.init(ChildUtil.name(name, USERS_ID));
        addUserCommand.init(ChildUtil.name(name, ADD_USER_ID));
        nodes.init(ChildUtil.name(name, NODES_ID));
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        deviceReferences.uninit();
        automations.uninit();
        addAutomationCommand.uninit();
        deviceGroups.uninit();
        addSystemCommand.uninit();
        users.uninit();
        addUserCommand.uninit();
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

    @Override
    public COMMAND getAddSystemCommand() {
        return addSystemCommand;
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
    public Object<?, ?> getChild(String id) {
        if(ADD_AUTOMATION_ID.equals(id))
            return addAutomationCommand;
        else if(ADD_SYSTEM_ID.equals(id))
            return addSystemCommand;
        else if(ADD_USER_ID.equals(id))
            return addUserCommand;
        else if(DEVICES_ID.equals(id))
            return devices;
        else if(AUTOMATIONS_ID.equals(id))
            return automations;
        else if(DEVICE_GROUPS_ID.equals(id))
            return deviceGroups;
        else if(NODES_ID.equals(id))
            return nodes;
        else if(USERS_ID.equals(id))
            return users;
        return null;
    }

    public <OBJECT extends ProxyObject<?, ?>> ConvertingList.Converter<ProxyValue<?, ?>, OBJECT> findConverter() {
        return new ReferenceLoaderConverter<>();
    }

    public <T extends ProxyObject<?, ?>> T find(String[] path) {
        return find(path, true);
    }

    public <T extends Object<?, ?>> T find(String[] path, boolean fail) {
        Object<?, ?> current = this;
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

    public <O extends Object<?, ?>> ObjectReference<O> reference(String[] path) {
        ObjectReferenceImpl<O> reference = new ObjectReferenceImpl<>(managedCollectionFactory, path);
        reference(this, reference, 0);
        return reference;
    }

    protected void reference(Object<?, ?> object, ObjectReferenceImpl reference, int pathIndex) {
        if(pathIndex == reference.getPath().length) {
            if(!references.containsKey(object))
                references.put(object, Lists.<ObjectReferenceImpl>newArrayList());
            references.get(object).add(reference);
            reference.setObject(this);
        } else {
            String id = reference.getPath()[pathIndex];
            Object<?, ?> child = object.getChild(id);
            if(child != null)
                reference(child, reference, pathIndex + 1);
            else if(object instanceof List) {
                List<? extends Object<?, ?>, ?> list = (List<? extends Object<?, ?>, ?>) object;
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
            ProxyDevice<?, ?, ?, ?, ?, ?>,
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
                      Factory<ProxyList.Simple<ProxyDeviceGroup.Simple>> systemsFactory,
                      Factory<ProxyList.Simple<ProxyUser.Simple>> usersFactory,
                      Factory<ProxyList.Simple<ProxyNode.Simple>> nodesFactory) {
            super(logger, managedCollectionFactory, receiverFactory, commandFactory, valuesFactory, automationsFactory, systemsFactory, usersFactory, nodesFactory);
        }
    }

    private class ReferenceLoaderConverter<OBJECT extends ProxyObject<?, ?>> implements ConvertingList.Converter<ProxyValue<?, ?>, OBJECT> {

        @Override
        public OBJECT apply(ProxyValue<?, ?> element) {
            if(element == null || element.getValue() == null || element.getValue().getFirstValue() == null)
                return null;
            return find(element.getValue().getFirstValue().split("/"));
        }
    }
}
