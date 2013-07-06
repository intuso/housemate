package com.intuso.housemate.api.object;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.TestEnvironment;
import com.intuso.housemate.api.object.command.CommandListener;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.housemate.object.proxy.simple.SimpleProxyFactory;
import com.intuso.housemate.object.proxy.simple.SimpleProxyObject;
import com.intuso.housemate.object.real.RealList;
import com.intuso.housemate.object.real.RealProperty;
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
public class PropertyTest {

    public final static String PROPERTIES = "properties";

    private SimpleProxyObject.List<PropertyData, SimpleProxyObject.Property> proxyList
            = new SimpleProxyObject.List<PropertyData, SimpleProxyObject.Property>(
            SimpleProxyFactory.changeFactoryType(TestEnvironment.TEST_INSTANCE.getProxyResources(), new SimpleProxyFactory.Property()),
            TestEnvironment.TEST_INSTANCE.getProxyResources(),
            new ListData(PROPERTIES, PROPERTIES, PROPERTIES));
    private RealList<PropertyData, RealProperty<?>> realList = new RealList<PropertyData, RealProperty<?>>(TestEnvironment.TEST_INSTANCE.getRealResources(), PROPERTIES, PROPERTIES, PROPERTIES, new ArrayList<RealProperty<?>>());
    private RealProperty<Integer> realProperty;
    private SimpleProxyObject.Property proxyProperty;

    public PropertyTest() throws HousemateException {
    }

    @Before
    public void addLists() throws HousemateException {
        TestEnvironment.TEST_INSTANCE.getProxyRoot().addWrapper(proxyList);
        TestEnvironment.TEST_INSTANCE.getRealRoot().addWrapper(realList);
        realProperty = IntegerType.createProperty(TestEnvironment.TEST_INSTANCE.getRealResources(), "my-property", "My Property", "description", Arrays.asList(1234));
        realList.add(realProperty);
        proxyProperty = proxyList.get("my-property");
    }

    @Test
    public void testCreateProxyProperty() throws HousemateException {
        assertNotNull(proxyProperty);
    }

    @Test
    public void testSetProxyProperty() throws HousemateException {
        proxyProperty.set(new TypeInstances(new TypeInstance("-1234")), new CommandListener<SimpleProxyObject.Command>() {
            @Override
            public void commandStarted(SimpleProxyObject.Command function) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void commandFinished(SimpleProxyObject.Command function) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void commandFailed(SimpleProxyObject.Command function, String error) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        assertEquals(-1234, (int) realProperty.getTypedValue());
    }

    @Test
    public void testListenerCalled() throws HousemateException {
        final AtomicBoolean called = new AtomicBoolean(false);
        proxyProperty.addObjectListener(new ValueListener<SimpleProxyObject.Property>() {

            @Override
            public void valueChanging(SimpleProxyObject.Property value) {
                // do nothing
            }

            @Override
            public void valueChanged(SimpleProxyObject.Property property) {
                called.set(true);
            }
        });
        realProperty.setTypedValues(-1234);
        assertEquals(true, called.get());
    }
}
