package com.intuso.housemate.object.proxy.simple;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.command.CommandData;
import com.intuso.housemate.api.object.command.CommandPerformListener;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.object.proxy.simple.comms.TestEnvironment;
import com.intuso.housemate.object.real.RealCommand;
import com.intuso.housemate.object.real.RealList;
import com.intuso.housemate.object.real.RealParameter;
import com.intuso.housemate.object.real.impl.type.IntegerType;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 */
public class CommandTest {

    public final static String COMMANDS = "commands";

    private static CommandPerformListener<SimpleProxyCommand> EMPTY_FUNCTION_LISTENER = new CommandPerformListener<SimpleProxyCommand>() {
        @Override
        public void commandStarted(SimpleProxyCommand function) {}

        @Override
        public void commandFinished(SimpleProxyCommand function) {}

        @Override
        public void commandFailed(SimpleProxyCommand function, String error) {}
    };

    private SimpleProxyList<CommandData, SimpleProxyCommand> proxyList
            = new SimpleProxyList<>(
            TestEnvironment.TEST_INSTANCE.getInjector().getInstance(Log.class),
            TestEnvironment.TEST_INSTANCE.getInjector().getInstance(ListenersFactory.class),
            TestEnvironment.TEST_INSTANCE.getInjector(),
            new ListData(COMMANDS, COMMANDS, COMMANDS));
    private RealList<CommandData, RealCommand> realList = new RealList<>(
            TestEnvironment.TEST_INSTANCE.getInjector().getInstance(Log.class),
            TestEnvironment.TEST_INSTANCE.getInjector().getInstance(ListenersFactory.class),
            COMMANDS, COMMANDS, COMMANDS, new ArrayList<RealCommand>());
    private RealCommand realCommand;
    private SimpleProxyCommand proxyCommand;

    public CommandTest() throws HousemateException {
    }

    @Before
    public void addLists() throws HousemateException {
        TestEnvironment.TEST_INSTANCE.getProxyRoot().addChild(proxyList);
        TestEnvironment.TEST_INSTANCE.getRealRoot().addWrapper(realList);
        realCommand = new RealCommand(TestEnvironment.TEST_INSTANCE.getInjector().getInstance(Log.class),
                TestEnvironment.TEST_INSTANCE.getInjector().getInstance(ListenersFactory.class),
                "my-command", "My Command", "description", new ArrayList<RealParameter<?>>()) {
            @Override
            public void perform(TypeInstanceMap values) throws HousemateException {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        };
        realList.add(realCommand);
        proxyCommand = proxyList.get("my-command");
    }

    @Test
    public void testCreateProxyCommand() throws HousemateException {
        assertNotNull(proxyCommand);
    }

    @Test
    public void testPerformProxyFunction() throws HousemateException {
        final AtomicBoolean called = new AtomicBoolean(false);
        RealCommand realCommand = new RealCommand(TestEnvironment.TEST_INSTANCE.getInjector().getInstance(Log.class),
                TestEnvironment.TEST_INSTANCE.getInjector().getInstance(ListenersFactory.class),
                "my-other-command", "My Other Command", "description", new ArrayList<RealParameter<?>>()) {
            @Override
            public void perform(TypeInstanceMap values) throws HousemateException {
                called.set(true);
            }
        };
        realList.add(realCommand);
        SimpleProxyCommand proxyCommand = proxyList.get("my-other-command");
        proxyCommand.perform(new TypeInstanceMap(), EMPTY_FUNCTION_LISTENER);
        assertEquals(true, called.get());
    }

    @Test
    public void testParameter() throws HousemateException {
        final AtomicBoolean correctParam = new AtomicBoolean(false);
        RealCommand realCommand = new RealCommand(TestEnvironment.TEST_INSTANCE.getInjector().getInstance(Log.class),
                TestEnvironment.TEST_INSTANCE.getInjector().getInstance(ListenersFactory.class),
                "my-other-command", "My Other Command",
                "description", Arrays.<RealParameter<?>>asList(
                        IntegerType.createParameter(TestEnvironment.TEST_INSTANCE.getInjector().getInstance(Log.class),
                                TestEnvironment.TEST_INSTANCE.getInjector().getInstance(ListenersFactory.class),
                                "my-parameter", "My Parameter", "description"))) {
            @Override
            public void perform(TypeInstanceMap values) throws HousemateException {
                correctParam.set(values.getChildren().get("my-parameter") != null
                        && values.getChildren().get("my-parameter").getFirstValue() != null
                        && values.getChildren().get("my-parameter").getFirstValue().equals("1234"));
            }
        };
        realList.add(realCommand);
        SimpleProxyCommand proxyCommand = proxyList.get("my-other-command");
        proxyCommand.perform(new TypeInstanceMap() {
            {
                getChildren().put("my-parameter", new TypeInstances(new TypeInstance("1234")));
            }
        }, EMPTY_FUNCTION_LISTENER);
        assertEquals(true, correctParam.get());
    }

    @Test
    public void testPerformListenerCalled() throws HousemateException {
        final AtomicBoolean functionStartedCalled = new AtomicBoolean(false);
        final AtomicBoolean functionFinishedCalled = new AtomicBoolean(false);
        proxyCommand.perform(new TypeInstanceMap(), new CommandPerformListener<SimpleProxyCommand>() {
            @Override
            public void commandStarted(SimpleProxyCommand function) {
                functionStartedCalled.set(true);
            }

            @Override
            public void commandFinished(SimpleProxyCommand function) {
                functionFinishedCalled.set(true);
            }

            @Override
            public void commandFailed(SimpleProxyCommand function, String error) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        assertEquals(true, functionStartedCalled.get());
        assertEquals(true, functionFinishedCalled.get());
    }
}
