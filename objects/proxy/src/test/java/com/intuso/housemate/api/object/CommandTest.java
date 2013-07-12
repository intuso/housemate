package com.intuso.housemate.api.object;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.TestEnvironment;
import com.intuso.housemate.api.object.command.CommandData;
import com.intuso.housemate.api.object.command.CommandListener;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.object.proxy.simple.SimpleProxyFactory;
import com.intuso.housemate.object.proxy.simple.SimpleProxyObject;
import com.intuso.housemate.object.real.RealParameter;
import com.intuso.housemate.object.real.RealCommand;
import com.intuso.housemate.object.real.RealList;
import com.intuso.housemate.object.real.impl.type.IntegerType;
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

    private static CommandListener<SimpleProxyObject.Command> EMPTY_FUNCTION_LISTENER = new CommandListener<SimpleProxyObject.Command>() {
        @Override
        public void commandStarted(SimpleProxyObject.Command function) {}

        @Override
        public void commandFinished(SimpleProxyObject.Command function) {}

        @Override
        public void commandFailed(SimpleProxyObject.Command function, String error) {}
    };

    private SimpleProxyObject.List<CommandData, SimpleProxyObject.Command> proxyList
            = new SimpleProxyObject.List<CommandData, SimpleProxyObject.Command>(
            SimpleProxyFactory.changeFactoryType(TestEnvironment.TEST_INSTANCE.getProxyResources(), new SimpleProxyFactory.Command()),
            TestEnvironment.TEST_INSTANCE.getProxyResources(),
            new ListData(COMMANDS, COMMANDS, COMMANDS));
    private RealList<CommandData, RealCommand> realList = new RealList<CommandData, RealCommand>(TestEnvironment.TEST_INSTANCE.getRealResources(), COMMANDS, COMMANDS, COMMANDS, new ArrayList<RealCommand>());
    private RealCommand realCommand;
    private SimpleProxyObject.Command proxyCommand;

    public CommandTest() throws HousemateException {
    }

    @Before
    public void addLists() throws HousemateException {
        TestEnvironment.TEST_INSTANCE.getProxyRoot().addChild(proxyList);
        TestEnvironment.TEST_INSTANCE.getRealRoot().addWrapper(realList);
        realCommand = new RealCommand(TestEnvironment.TEST_INSTANCE.getRealResources(), "my-command", "My Command", "description", new ArrayList<RealParameter<?>>()) {
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
        RealCommand realCommand = new RealCommand(TestEnvironment.TEST_INSTANCE.getRealResources(), "my-other-command", "My Other Command", "description", new ArrayList<RealParameter<?>>()) {
            @Override
            public void perform(TypeInstanceMap values) throws HousemateException {
                called.set(true);
            }
        };
        realList.add(realCommand);
        SimpleProxyObject.Command proxyCommand = proxyList.get("my-other-command");
        proxyCommand.perform(new TypeInstanceMap(), EMPTY_FUNCTION_LISTENER);
        assertEquals(true, called.get());
    }

    @Test
    public void testParameter() throws HousemateException {
        final AtomicBoolean correctParam = new AtomicBoolean(false);
        RealCommand realCommand = new RealCommand(TestEnvironment.TEST_INSTANCE.getRealResources(), "my-other-command", "My Other Command", "description",
                Arrays.<RealParameter<?>>asList(IntegerType.createParameter(TestEnvironment.TEST_INSTANCE.getRealResources(), "my-parameter", "My Parameter", "description"))) {
            @Override
            public void perform(TypeInstanceMap values) throws HousemateException {
                correctParam.set(values.get("my-parameter") != null
                        && values.get("my-parameter").getFirstValue() != null
                        && values.get("my-parameter").getFirstValue().equals("1234"));
            }
        };
        realList.add(realCommand);
        SimpleProxyObject.Command proxyCommand = proxyList.get("my-other-command");
        proxyCommand.perform(new TypeInstanceMap() {
            {
                put("my-parameter", new TypeInstances(new TypeInstance("1234")));
            }
        }, EMPTY_FUNCTION_LISTENER);
        assertEquals(true, correctParam.get());
    }

    @Test
    public void testPerformListenerCalled() throws HousemateException {
        final AtomicBoolean functionStartedCalled = new AtomicBoolean(false);
        final AtomicBoolean functionFinishedCalled = new AtomicBoolean(false);
        proxyCommand.perform(new TypeInstanceMap(), new CommandListener<SimpleProxyObject.Command>() {
            @Override
            public void commandStarted(SimpleProxyObject.Command function) {
                functionStartedCalled.set(true);
            }

            @Override
            public void commandFinished(SimpleProxyObject.Command function) {
                functionFinishedCalled.set(true);
            }

            @Override
            public void commandFailed(SimpleProxyObject.Command function, String error) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        assertEquals(true, functionStartedCalled.get());
        assertEquals(true, functionFinishedCalled.get());
    }
}
