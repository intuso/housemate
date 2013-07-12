package com.intuso.housemate.api.object;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.TestEnvironment;
import com.intuso.housemate.api.object.command.CommandListener;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.device.DeviceListener;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.object.proxy.simple.SimpleProxyFactory;
import com.intuso.housemate.object.proxy.simple.SimpleProxyObject;
import com.intuso.housemate.object.real.RealDevice;
import com.intuso.housemate.object.real.RealList;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.*;

/**
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

    private SimpleProxyObject.List<DeviceData, SimpleProxyObject.Device> proxyList
            = new SimpleProxyObject.List<DeviceData, SimpleProxyObject.Device>(
            SimpleProxyFactory.changeFactoryType(TestEnvironment.TEST_INSTANCE.getProxyResources(), new SimpleProxyFactory.Device()),
            TestEnvironment.TEST_INSTANCE.getProxyResources(),
            new ListData(PRIMARIES, PRIMARIES, PRIMARIES));
    private RealList<DeviceData, RealDevice> realList = new RealList<DeviceData, RealDevice>(TestEnvironment.TEST_INSTANCE.getRealResources(), PRIMARIES, PRIMARIES, PRIMARIES, new ArrayList<RealDevice>());
    private RealDevice realPrimary;
    private SimpleProxyObject.Device proxyPrimary;

    public PrimaryTest() throws HousemateException {
    }

    @Before
    public void addLists() throws HousemateException {
        TestEnvironment.TEST_INSTANCE.getProxyRoot().addChild(proxyList);
        TestEnvironment.TEST_INSTANCE.getRealRoot().addWrapper(realList);
        realPrimary = new RealDevice(TestEnvironment.TEST_INSTANCE.getRealResources(), "my-primary", "My Primary", "description");
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
        realPrimary.getRunningValue().setTypedValues(Boolean.TRUE);
        assertTrue(proxyPrimary.isRunning());
        realPrimary.getRunningValue().setTypedValues(Boolean.FALSE);
        assertFalse(proxyPrimary.isRunning());
        proxyPrimary.getStartCommand().perform(EMPTY_LISTENER);
        assertTrue(proxyPrimary.isRunning());
        proxyPrimary.getStopCommand().perform(EMPTY_LISTENER);
        assertFalse(proxyPrimary.isRunning());
    }

    @Test
    public void testError() {
        assertNull(proxyPrimary.getError());
        realPrimary.getErrorValue().setTypedValues("error");
        assertEquals("error", proxyPrimary.getError());
        realPrimary.getErrorValue().setTypedValues((String)null);
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
        realPrimary.getErrorValue().setTypedValues("error");
        proxyPrimary.getRemoveCommand().perform(EMPTY_LISTENER);
        assertTrue(errorUpdated.get());
        assertTrue(runningUpdated.get());
    }
}
