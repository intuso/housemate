package com.intuso.housemate.api.object;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.TestEnvironment;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.value.ValueBaseData;
import com.intuso.housemate.api.object.value.ValueData;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.housemate.object.proxy.simple.SimpleProxyFactory;
import com.intuso.housemate.object.proxy.simple.SimpleProxyObject;
import com.intuso.housemate.object.real.RealList;
import com.intuso.housemate.object.real.RealValue;
import com.intuso.housemate.object.real.impl.type.IntegerType;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 */
public class ValueTest {

    public final static String VALUES = "values";

    private SimpleProxyObject.List<ValueData, SimpleProxyObject.Value> proxyList
            = new SimpleProxyObject.List<ValueData, SimpleProxyObject.Value>(
                SimpleProxyFactory.changeFactoryType(TestEnvironment.TEST_INSTANCE.getProxyResources(), new SimpleProxyFactory.Value()),
                TestEnvironment.TEST_INSTANCE.getProxyResources(),
            new ListData(VALUES, VALUES, VALUES));
    private RealList<ValueBaseData<NoChildrenData>, RealValue<?>> realList = new RealList<ValueBaseData<NoChildrenData>, RealValue<?>>(TestEnvironment.TEST_INSTANCE.getRealResources(), VALUES, VALUES, VALUES, new ArrayList<RealValue<?>>());
    private RealValue<Integer> realValue;
    private SimpleProxyObject.Value proxyValue;

    public ValueTest() throws HousemateException {
    }

    @Before
    public void addLists() throws HousemateException {
        TestEnvironment.TEST_INSTANCE.getProxyRoot().addWrapper(proxyList);
        TestEnvironment.TEST_INSTANCE.getRealRoot().addWrapper(realList);
        realValue = IntegerType.createValue(TestEnvironment.TEST_INSTANCE.getRealResources(), "my-value", "My Value", "description", 1234);
        realList.add(realValue);
        proxyValue = proxyList.get("my-value");
    }

    @Test
    public void testCreateProxyValue() throws HousemateException {
        assertNotNull(proxyValue);
    }

    @Test
    public void testSetRealValue() throws HousemateException {
        realValue.setTypedValues(-1234);
        assertEquals("-1234", proxyValue.getTypeInstances().getFirstValue());
    }

    @Test
    public void testListenerCalled() throws HousemateException {
        final AtomicBoolean called = new AtomicBoolean(false);
        proxyValue.addObjectListener(new ValueListener<SimpleProxyObject.Value>() {

            @Override
            public void valueChanging(SimpleProxyObject.Value value) {
                // do nothing
            }

            @Override
            public void valueChanged(SimpleProxyObject.Value value) {
                called.set(true);
            }
        });
        realValue.setTypedValues(-1234);
        assertEquals(true, called.get());
    }
}
