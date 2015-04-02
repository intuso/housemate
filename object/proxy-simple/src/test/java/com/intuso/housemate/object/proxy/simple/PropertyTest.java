package com.intuso.housemate.object.proxy.simple;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.command.CommandPerformListener;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.housemate.object.proxy.simple.comms.TestEnvironment;
import com.intuso.housemate.object.real.RealList;
import com.intuso.housemate.object.real.RealProperty;
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
public class PropertyTest {

    public final static String PROPERTIES = "properties";

    private SimpleProxyList<PropertyData, SimpleProxyProperty> proxyList
            = new SimpleProxyList<>(
            TestEnvironment.TEST_INSTANCE.getInjector().getInstance(Log.class),
            TestEnvironment.TEST_INSTANCE.getInjector().getInstance(ListenersFactory.class),
            TestEnvironment.TEST_INSTANCE.getInjector(),
            new ListData(PROPERTIES, PROPERTIES, PROPERTIES));
    private RealList<PropertyData, RealProperty<?>> realList = new RealList<>(
            TestEnvironment.TEST_INSTANCE.getInjector().getInstance(Log.class),
            TestEnvironment.TEST_INSTANCE.getInjector().getInstance(ListenersFactory.class),
            PROPERTIES, PROPERTIES, PROPERTIES, new ArrayList<RealProperty<?>>());
    private RealProperty<Integer> realProperty;
    private SimpleProxyProperty proxyProperty;

    public PropertyTest() throws HousemateException {
    }

    @Before
    public void addLists() throws HousemateException {
        TestEnvironment.TEST_INSTANCE.getProxyRoot().addChild(proxyList);
        TestEnvironment.TEST_INSTANCE.getRealRoot().addWrapper(realList);
        realProperty = IntegerType.createProperty(TestEnvironment.TEST_INSTANCE.getInjector().getInstance(Log.class),
                TestEnvironment.TEST_INSTANCE.getInjector().getInstance(ListenersFactory.class),
                "my-property", "My Property", "description", Arrays.asList(1234));
        realList.add(realProperty);
        proxyProperty = proxyList.get("my-property");
    }

    @Test
    public void testCreateProxyProperty() throws HousemateException {
        assertNotNull(proxyProperty);
    }

    @Test
    public void testSetProxyProperty() throws HousemateException {
        proxyProperty.set(new TypeInstances(new TypeInstance("-1234")), new CommandPerformListener<SimpleProxyCommand>() {
            @Override
            public void commandStarted(SimpleProxyCommand function) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void commandFinished(SimpleProxyCommand function) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void commandFailed(SimpleProxyCommand function, String error) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        assertEquals(-1234, (int) realProperty.getTypedValue());
    }

    @Test
    public void testListenerCalled() throws HousemateException {
        final AtomicBoolean called = new AtomicBoolean(false);
        proxyProperty.addObjectListener(new ValueListener<SimpleProxyProperty>() {

            @Override
            public void valueChanging(SimpleProxyProperty value) {
                // do nothing
            }

            @Override
            public void valueChanged(SimpleProxyProperty property) {
                called.set(true);
            }
        });
        realProperty.setTypedValues(-1234);
        assertEquals(true, called.get());
    }
}
