package com.intuso.housemate.core.object;

import com.intuso.housemate.core.HousemateException;
import com.intuso.housemate.core.TestEnvironment;
import com.intuso.housemate.core.object.command.CommandListener;
import com.intuso.housemate.core.object.list.ListWrappable;
import com.intuso.housemate.core.object.device.DeviceListener;
import com.intuso.housemate.core.object.device.DeviceWrappable;
import com.intuso.housemate.proxy.simple.SimpleProxyFactory;
import com.intuso.housemate.proxy.simple.SimpleProxyObject;
import com.intuso.housemate.real.RealDevice;
import com.intuso.housemate.real.RealList;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 08/07/12
 * Time: 19:14
 * To change this template use File | Settings | File Templates.
 */
public class PrimaryTest {

    public final static String PRIMARIES = "primaries";

    private final static CommandListener<SimpleProxyObject.Command> EMPTY_LISTENER = new CommandListener<SimpleProxyObject.Command>() {
        @Override
        public void commandStarted(SimpleProxyObject.Command function) {}

        @Override
        public void commandFinished(SimpleProxyObject.Command function) {}

        @Override
        public void commandFailed(SimpleProxyObject.Command function, String error) {}
    };

    private SimpleProxyObject.List<DeviceWrappable, SimpleProxyObject.Device> proxyList
            = new SimpleProxyObject.List<DeviceWrappable, SimpleProxyObject.Device>(
            SimpleProxyFactory.changeFactoryType(TestEnvironment.TEST_INSTANCE.getProxyResources(), new SimpleProxyFactory.Device()),
            TestEnvironment.TEST_INSTANCE.getProxyResources(),
            new ListWrappable(PRIMARIES, PRIMARIES, PRIMARIES));
    private RealList<DeviceWrappable, RealDevice> realList = new RealList<DeviceWrappable, RealDevice>(TestEnvironment.TEST_INSTANCE.getRealResources(), PRIMARIES, PRIMARIES, PRIMARIES, new ArrayList<RealDevice>());
    private RealDevice realPrimary;
    private SimpleProxyObject.Device proxyPrimary;

    public PrimaryTest() throws HousemateException {
    }

    @Before
    public void addLists() throws HousemateException {
        TestEnvironment.TEST_INSTANCE.getProxyRoot().addWrapper(proxyList);
        TestEnvironment.TEST_INSTANCE.getRealRoot().addWrapper(realList);
        realPrimary = new RealDevice(TestEnvironment.TEST_INSTANCE.getRealResources(), "my-primary", "My Primary", "description") {
            @Override
            public void _start() {
            }

            @Override
            public void _stop() {
            }
        };
        realList.add(realPrimary);
        proxyPrimary = proxyList.get("my-primary");
    }

    @Test
    public void testCreateProxyDevice() throws HousemateException {
        assertNotNull(proxyPrimary);
    }

    @Test
    public void testStartStopPrimary() throws HousemateException {
        assertFalse(proxyPrimary.isRunning());
        realPrimary.getRunningValue().setTypedValue(Boolean.TRUE);
        assertTrue(proxyPrimary.isRunning());
        realPrimary.getRunningValue().setTypedValue(Boolean.FALSE);
        assertFalse(proxyPrimary.isRunning());
        proxyPrimary.getStartCommand().perform(EMPTY_LISTENER);
        assertTrue(proxyPrimary.isRunning());
        proxyPrimary.getStopCommand().perform(EMPTY_LISTENER);
        assertFalse(proxyPrimary.isRunning());
    }

    @Test
    public void testError() {
        assertNull(proxyPrimary.getError());
        realPrimary.getErrorValue().setTypedValue("error");
        assertEquals("error", proxyPrimary.getError());
        realPrimary.getErrorValue().setTypedValue(null);
        assertNull(proxyPrimary.getError());
    }

    @Test
    public void testListener() throws HousemateException {
        final AtomicBoolean connectedUpdated = new AtomicBoolean(false);
        final AtomicBoolean runningUpdated = new AtomicBoolean(false);
        final AtomicBoolean errorUpdated = new AtomicBoolean(false);
        proxyPrimary.addObjectListener(new DeviceListener<SimpleProxyObject.Device>() {
            @Override
            public void error(SimpleProxyObject.Device entityWrapper, String description) {
                runningUpdated.set(true);
            }

            @Override
            public void running(SimpleProxyObject.Device entityWrapper, boolean running) {
                errorUpdated.set(true);
            }
        });
        proxyPrimary.getStartCommand().perform(EMPTY_LISTENER);
        proxyPrimary.getStopCommand().perform(EMPTY_LISTENER);
        realPrimary.getErrorValue().setValue("error");
        proxyPrimary.getRemoveCommand().perform(EMPTY_LISTENER);
        assertTrue(errorUpdated.get());
        assertTrue(runningUpdated.get());
    }
}
