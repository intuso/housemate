package com.intuso.housemate.broker;

import com.google.common.collect.Maps;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.ObjectLifecycleListener;
import com.intuso.housemate.api.object.type.SimpleTypeWrappable;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.value.Value;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.housemate.broker.object.bridge.ValueBridge;
import com.intuso.housemate.broker.plugin.comparator.DoubleComparators;
import com.intuso.housemate.broker.plugin.comparator.IntegerComparators;
import com.intuso.housemate.broker.plugin.condition.ValueComparison;
import com.intuso.housemate.broker.plugin.type.comparison.Comparison;
import com.intuso.housemate.broker.plugin.type.valuesource.ConstantValue;
import com.intuso.housemate.broker.plugin.type.valuesource.ValueLocation;
import com.intuso.housemate.broker.plugin.type.valuesource.ValueSource;
import com.intuso.housemate.object.broker.real.BrokerRealProperty;
import com.intuso.housemate.object.real.RealType;
import com.intuso.housemate.object.real.impl.type.DoubleType;
import com.intuso.housemate.object.real.impl.type.IntegerType;
import com.intuso.housemate.object.real.impl.type.RealObjectType;
import com.intuso.housemate.plugin.api.Comparator;
import com.intuso.housemate.plugin.api.ComparisonOperator;
import com.intuso.utilities.listener.ListenerRegistration;
import org.junit.Test;

import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

/**
 */
public class TestValueComparison {

    private final static BrokerServerEnvironment SERVER_ENVIRONMENT = TestUtils.startBroker(65432);

    private final static Map<ComparisonOperator, Map<String, Comparator<?>>> COMPARISONS_BY_TYPE = Maps.newHashMap();
    static {
        COMPARISONS_BY_TYPE.put(ComparisonOperator.Simple.Equals, Maps.<String, Comparator<?>>newHashMap());
        COMPARISONS_BY_TYPE.get(ComparisonOperator.Simple.Equals).put(SimpleTypeWrappable.Type.Integer.getId(), new IntegerComparators.Equals());
        COMPARISONS_BY_TYPE.get(ComparisonOperator.Simple.Equals).put(SimpleTypeWrappable.Type.Double.getId(), new DoubleComparators.Equals());
    }

    @Test
    public void testTwoConstantsEqualsTrue() {
        RealType<?, ?, ?> integerType = new IntegerType(SERVER_ENVIRONMENT.getGeneralResources().getClientResources());
        ConstantValue valueOne = new ConstantValue((RealType<?, ?, Object>) integerType, new TypeInstance("1"));
        ConstantValue valueTwo = new ConstantValue((RealType<?, ?, Object>) integerType, new TypeInstance("1"));
        assertValueComparisonSatisfied(ComparisonOperator.Simple.Equals, valueOne, valueTwo, true);
    }

    @Test
    public void testTwoConstantsEqualsFalse() {
        RealType<?, ?, ?> integerType = new IntegerType(SERVER_ENVIRONMENT.getGeneralResources().getClientResources());
        ConstantValue valueOne = new ConstantValue((RealType<?, ?, Object>) integerType, new TypeInstance("1"));
        ConstantValue valueTwo = new ConstantValue((RealType<?, ?, Object>) integerType, new TypeInstance("2"));
        assertValueComparisonSatisfied(ComparisonOperator.Simple.Equals, valueOne, valueTwo, false);
    }

    @Test
    public void testConstantAndLocationGreaterThan() throws HousemateException, InterruptedException {
        final Object lock = new Object();
        RealType<?, ?, ?> doubleType = new DoubleType(SERVER_ENVIRONMENT.getGeneralResources().getClientResources());
        ConstantValue valueOne = new ConstantValue((RealType<?, ?, Object>) doubleType, new TypeInstance("2.0"));
        String[] valuePath = new String[] {"", "devices", "device", "values", "dv"};
        ValueLocation valueTwo = new ValueLocation(
                new RealObjectType.Reference<Value<?, ?>>(valuePath),
                SERVER_ENVIRONMENT.getGeneralResources().getBridgeResources().getRoot());
        ListenerRegistration lr = SERVER_ENVIRONMENT.getGeneralResources().getBridgeResources().getRoot().addObjectLifecycleListener(valuePath, new ObjectLifecycleListener() {
            @Override
            public void objectCreated(String[] path, HousemateObject<?, ?, ?, ?, ?> object) {
                synchronized (lock) {
                    lock.notify();
                }
            }

            @Override
            public void objectRemoved(String[] path, HousemateObject<?, ?, ?, ?, ?> object) {
                // do nothing
            }
        });
        ValueComparison vc = makeValueComparison(ComparisonOperator.Simple.Equals, valueOne, valueTwo);
        assertEquals(vc.getError(), "Second value is not available");
        TestDevice device = new TestDevice(SERVER_ENVIRONMENT.getGeneralResources().getClientResources(), "device", "Device", "Device");
        SERVER_ENVIRONMENT.getGeneralResources().getAnnotationProcessor().process(device);
        SERVER_ENVIRONMENT.getGeneralResources().getClient().getRoot().addDevice(device);
        synchronized (lock) {
            lock.wait();
        }
        lr.removeListener();
        device.values.doubleValue(0.0);
        assertNull(vc.getError());
        assertSatisfied(vc, false);
        lr = SERVER_ENVIRONMENT.getGeneralResources().getBridgeResources().getRoot().getDevices().get("device").getValues().get("dv").addObjectListener(new ValueListener<ValueBridge>() {
            @Override
            public void valueChanging(ValueBridge value) {
                // do nothing
            }

            @Override
            public void valueChanged(ValueBridge value) {
                synchronized (lock) {
                    lock.notify();
                }
            }
        });
        device.values.doubleValue(2.0);
        synchronized (lock) {
            lock.wait();
        }
        Thread.sleep(1); // the listener that notifies might get called before the vc listener, so the vc might not have updated yet
        lr.removeListener();
        assertSatisfied(vc, true);
    }

    private void assertValueComparisonSatisfied(ComparisonOperator operator, ValueSource sourceOne,
                                                ValueSource sourceTwo, boolean satisfied) {
        assertSatisfied(makeValueComparison(operator, sourceOne, sourceTwo), satisfied);
    }

    private void assertSatisfied(ValueComparison valueComparison, boolean satisfied) {
        assertEquals(satisfied, valueComparison.isSatisfied());
    }

    private ValueComparison makeValueComparison(ComparisonOperator operator, ValueSource sourceOne, ValueSource sourceTwo) {
        ValueComparison valueComparison = new ValueComparison(SERVER_ENVIRONMENT.getGeneralResources().getRealResources(),
                "test", "Test", "Test VC", SERVER_ENVIRONMENT.getGeneralResources());
        BrokerRealProperty<Comparison> comparisonProperty = (BrokerRealProperty<Comparison>) valueComparison.getProperties().get(ValueComparison.COMPARISON_ID);
        Comparison comparison = new Comparison(operator, COMPARISONS_BY_TYPE.get(operator), sourceOne, sourceTwo);
        comparisonProperty.setTypedValue(comparison);
        valueComparison.start();
        return valueComparison;
    }
}
