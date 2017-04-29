package com.intuso.housemate.client.proxy.internal.object;

import com.google.common.base.Joiner;
import com.google.common.util.concurrent.AbstractIdleService;
import com.google.inject.Inject;
import com.intuso.housemate.client.api.internal.HousemateException;
import com.intuso.housemate.client.api.internal.object.Device;
import com.intuso.housemate.client.api.internal.object.Server;
import com.intuso.housemate.client.messaging.api.internal.Receiver;
import com.intuso.housemate.client.proxy.internal.ChildUtil;
import com.intuso.housemate.client.proxy.internal.ProxyRenameable;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import org.slf4j.Logger;

/**
 * @param <COMMAND> the type of the command
 * @param <AUTOMATIONS> the type of the automations list
 * @param <USERS> the type of the users list
 * @param <NODES> the type of the nodes list
 * @param <SERVER> the type of the server
 */
public abstract class ProxyServer<
        COMMAND extends ProxyCommand<?, ?, ?>,
        DEVICES extends ProxyList<? extends ProxyValue<?, ?>, ?>,
        AUTOMATIONS extends ProxyList<? extends ProxyAutomation<?, ?, ?, ?, ?>, ?>,
        COMBI_DEVICES extends ProxyList<? extends ProxyDeviceCombi<?, ?, ?, ?, ?, ?>, ?>,
        USERS extends ProxyList<? extends ProxyUser<?, ?, ?>, ?>,
        NODES extends ProxyList<? extends ProxyNode<?, ?, ?, ?>, ?>,
        SERVER extends ProxyServer<COMMAND, DEVICES, AUTOMATIONS, COMBI_DEVICES, USERS, NODES, SERVER>>
        extends ProxyObject<Server.Data, Server.Listener<? super SERVER>>
        implements Server<COMMAND, DEVICES, AUTOMATIONS, COMBI_DEVICES, USERS, NODES, SERVER>,
        Device.Container<Iterable<ProxyDevice<?, ?, ?, ?, ?, ?>>>,
        ProxyRenameable<COMMAND> {

    private final COMMAND renameCommand;
    private final DEVICES deviceReferences;
    private final AUTOMATIONS automations;
    private final COMMAND addAutomationCommand;
    private final COMBI_DEVICES systems;
    private final COMMAND addSystemCommand;
    private final USERS users;
    private final COMMAND addUserCommand;
    private final NODES nodes;

    public ProxyServer(Logger logger,
                       ManagedCollectionFactory managedCollectionFactory,
                       Receiver.Factory receiverFactory,
                       Factory<COMMAND> commandFactory,
                       Factory<DEVICES> valuesFactory,
                       Factory<AUTOMATIONS> automationsFactory,
                       Factory<COMBI_DEVICES> systemsFactory,
                       Factory<USERS> usersFactory,
                       Factory<NODES> nodesFactory) {
        super(logger, Server.Data.class, managedCollectionFactory, receiverFactory);
        renameCommand = commandFactory.create(ChildUtil.logger(logger, RENAME_ID));
        deviceReferences = valuesFactory.create(ChildUtil.logger(logger, DEVICES_ID));
        automations = automationsFactory.create(ChildUtil.logger(logger, AUTOMATIONS_ID));
        addAutomationCommand = commandFactory.create(ChildUtil.logger(logger, ADD_AUTOMATION_ID));
        systems = systemsFactory.create(ChildUtil.logger(logger, DEVICE_COMBIS_ID));
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
        renameCommand.init(ChildUtil.name(name, RENAME_ID));
        deviceReferences.init(ChildUtil.name(name, DEVICES_ID));
        automations.init(ChildUtil.name(name, AUTOMATIONS_ID));
        addAutomationCommand.init(ChildUtil.name(name, ADD_AUTOMATION_ID));
        systems.init(ChildUtil.name(name, DEVICE_COMBIS_ID));
        addSystemCommand.init(ChildUtil.name(name, ADD_SYSTEM_ID));
        users.init(ChildUtil.name(name, USERS_ID));
        addUserCommand.init(ChildUtil.name(name, ADD_USER_ID));
        nodes.init(ChildUtil.name(name, NODES_ID));
    }

    @Override
    protected void uninitChildren() {
        super.uninitChildren();
        renameCommand.uninit();
        deviceReferences.uninit();
        automations.uninit();
        addAutomationCommand.uninit();
        systems.uninit();
        addSystemCommand.uninit();
        users.uninit();
        addUserCommand.uninit();
        nodes.uninit();
    }

    @Override
    public COMMAND getRenameCommand() {
        return renameCommand;
    }

    @Override
    public DEVICES getDeviceReferences() {
        return deviceReferences;
    }

    @Override
    public Iterable<ProxyDevice<?, ?, ?, ?, ?, ?>> getDevices() {
        // todo transform references
        return null;
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
    public COMBI_DEVICES getDeviceCombis() {
        return systems;
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
    public ProxyObject<?, ?> getChild(String id) {
        if(RENAME_ID.equals(id))
            return renameCommand;
        else if(ADD_AUTOMATION_ID.equals(id))
            return addAutomationCommand;
        else if(ADD_SYSTEM_ID.equals(id))
            return addSystemCommand;
        else if(ADD_USER_ID.equals(id))
            return addUserCommand;
        else if(DEVICES_ID.equals(id))
            return deviceReferences;
        else if(AUTOMATIONS_ID.equals(id))
            return automations;
        else if(DEVICE_COMBIS_ID.equals(id))
            return systems;
        else if(NODES_ID.equals(id))
            return nodes;
        else if(USERS_ID.equals(id))
            return users;
        return null;
    }

    public <T extends ProxyObject<?, ?>> T find(String[] path) {
        return find(path, true);
    }

    public <T extends ProxyObject<?, ?>> T find(String[] path, boolean fail) {
        ProxyObject current = this;
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
            ProxyList.Simple<ProxyValue.Simple>,
            ProxyList.Simple<ProxyAutomation.Simple>,
            ProxyList.Simple<ProxyDeviceCombi.Simple>,
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
                      Factory<ProxyList.Simple<ProxyDeviceCombi.Simple>> systemsFactory,
                      Factory<ProxyList.Simple<ProxyUser.Simple>> usersFactory,
                      Factory<ProxyList.Simple<ProxyNode.Simple>> nodesFactory) {
            super(logger, managedCollectionFactory, receiverFactory, commandFactory, valuesFactory, automationsFactory, systemsFactory, usersFactory, nodesFactory);
        }
    }
}
