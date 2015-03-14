package com.intuso.housemate.server;

import com.google.common.collect.Maps;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.HousemateObject;
import com.intuso.housemate.api.object.ObjectLifecycleListener;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.type.*;
import com.intuso.housemate.api.object.value.Value;
import com.intuso.housemate.api.object.value.ValueListener;
import com.intuso.housemate.object.real.RealList;
import com.intuso.housemate.object.real.RealType;
import com.intuso.housemate.object.real.annotations.AnnotationProcessor;
import com.intuso.housemate.object.real.impl.type.DoubleType;
import com.intuso.housemate.object.real.impl.type.IntegerType;
import com.intuso.housemate.object.real.impl.type.RealObjectType;
import com.intuso.housemate.object.server.real.ServerRealCondition;
import com.intuso.housemate.object.server.real.ServerRealConditionOwner;
import com.intuso.housemate.object.server.real.ServerRealProperty;
import com.intuso.housemate.plugin.api.*;
import com.intuso.housemate.server.client.LocalClient;
import com.intuso.housemate.server.comms.MainRouter;
import com.intuso.housemate.server.factory.ConditionFactory;
import com.intuso.housemate.server.object.bridge.RootBridge;
import com.intuso.housemate.server.object.bridge.ValueBridge;
import com.intuso.housemate.server.plugin.PluginManager;
import com.intuso.housemate.server.plugin.main.MainPluginModule;
import com.intuso.housemate.server.plugin.main.comparator.DoubleComparators;
import com.intuso.housemate.server.plugin.main.comparator.IntegerComparators;
import com.intuso.housemate.server.plugin.main.condition.ValueComparison;
import com.intuso.housemate.server.plugin.main.operator.DoubleOperators;
import com.intuso.housemate.server.plugin.main.transformer.FromInteger;
import com.intuso.housemate.server.plugin.main.type.comparison.Comparison;
import com.intuso.housemate.server.plugin.main.type.operation.Operation;
import com.intuso.housemate.server.plugin.main.type.transformation.Transformation;
import com.intuso.housemate.server.plugin.main.type.valuesource.*;
import com.intuso.utilities.listener.Listener;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.listener.Listeners;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;
import com.intuso.utilities.properties.api.WriteableMapPropertyRepository;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;

/**
 */
public class TestValueComparison {

    private final static Map<ComparisonType, Map<String, Comparator<?>>> COMPARISONS_BY_TYPE = Maps.newHashMap();
    static {
        COMPARISONS_BY_TYPE.put(ComparisonType.Simple.Equals, Maps.<String, Comparator<?>>newHashMap());
        COMPARISONS_BY_TYPE.get(ComparisonType.Simple.Equals).put(SimpleTypeData.Type.Integer.getId(), new IntegerComparators.Equals());
        COMPARISONS_BY_TYPE.get(ComparisonType.Simple.Equals).put(SimpleTypeData.Type.Double.getId(), new DoubleComparators.Equals());
    }
    
    private Injector injector;
    private Log log;
    private final ListenersFactory listenersFactory = new ListenersFactory() {
        @Override
        public <LISTENER extends Listener> Listeners<LISTENER> create() {
            return new Listeners<LISTENER>(new CopyOnWriteArrayList<LISTENER>());
        }
    };

    @Before
    public void initResources() {
        // create the default property repository
        WriteableMapPropertyRepository defaultProperties = WriteableMapPropertyRepository.newEmptyRepository(listenersFactory);
        // create the main injector
        injector = Guice.createInjector(new TestModule(listenersFactory, defaultProperties), new ServerModule(defaultProperties));
        // make sure all the framework is created before we add plugins
        injector.getInstance(RootBridge.class);
        injector.getInstance(MainRouter.class).start();
//        need to get local client too? something not adding plugins types to main type list
        // add the main plugin module
        injector.getInstance(PluginManager.class).addPlugin(MainPluginModule.class);
        log = injector.getInstance(Log.class);
    }

    @Test
    @Ignore
    public void testTwoConstantsEqualsTrue() throws HousemateException {
        RealType<?, ?, ?> integerType = new IntegerType(log, listenersFactory);
        ConstantValue valueOne = new ConstantValue(listenersFactory, integerType, new TypeInstances(new TypeInstance("1")));
        ConstantValue valueTwo = new ConstantValue(listenersFactory, integerType, new TypeInstances(new TypeInstance("1")));
        assertValueComparisonSatisfied(ComparisonType.Simple.Equals, valueOne, valueTwo, true);
    }

    @Test
    public void testTwoConstantsEqualsFalse() throws HousemateException {
        RealType<?, ?, ?> integerType = new IntegerType(log, listenersFactory);
        ConstantValue valueOne = new ConstantValue(listenersFactory, integerType, new TypeInstances(new TypeInstance("1")));
        ConstantValue valueTwo = new ConstantValue(listenersFactory, integerType, new TypeInstances(new TypeInstance("2")));
        assertValueComparisonSatisfied(ComparisonType.Simple.Equals, valueOne, valueTwo, false);
    }

    @Test
    public void testLocationSources() throws HousemateException, InterruptedException {
        final Object lock = new Object();

        RealList<TypeData<?>, RealType<?, ?, ?>> types = injector.getInstance(new Key<RealList<TypeData<?>, RealType<?, ?, ?>>>() {});

        // create value one as (((double) 2) + 3.0)
        RealType<?, ?, ?> integerType = new IntegerType(log, listenersFactory);
        RealType<?, ?, ?> doubleType = new DoubleType(log, listenersFactory);
        ConstantValue intTwo = new ConstantValue(listenersFactory, integerType, new TypeInstances(new TypeInstance("2")));
        ConstantValue doubleThree = new ConstantValue(listenersFactory, doubleType, new TypeInstances(new TypeInstance("3.0")));

        Thread.sleep(100); // let types and transformers be added

        TransformationOutput doubleTwo = new TransformationOutput(log, listenersFactory, types,
                new Transformation(doubleType, new HashMap<String, Transformer<?, ?>>() {
                    {
                        put(SimpleTypeData.Type.Integer.getId(), new FromInteger.ToDouble());
                    }
                }, intTwo));
        OperationOutput valueOne = new OperationOutput(log, listenersFactory, types,
                new Operation(OperationType.Simple.Plus, new HashMap<String, Operator<?, ?>>() {
                    {
                        put(SimpleTypeData.Type.Double.getId(), new DoubleOperators.Plus());
                    }
                }, doubleTwo, doubleThree));

        // create second value as one from the device
        String[] valuePath = new String[] {"", "devices", "device", "values", "dv"};
        ValueLocation valueTwo = new ValueLocation(listenersFactory,
                new RealObjectType.Reference<Value<?, ?>>(valuePath),
                injector.getInstance(RootBridge.class));
        ListenerRegistration lr = injector.getInstance(RootBridge.class).
                addObjectLifecycleListener(valuePath, new ObjectLifecycleListener() {
                    @Override
                    public void objectCreated(String[] path, HousemateObject<?, ?, ?, ?> object) {
                        synchronized (lock) {
                            lock.notify();
                        }
                    }

                    @Override
                    public void objectRemoved(String[] path, HousemateObject<?, ?, ?, ?> object) {
                        // do nothing
                    }
                });
        ValueComparison vc = makeValueComparison(ComparisonType.Simple.Equals, valueOne, valueTwo);
        assertEquals(vc.getError(), "Second value is not available");
        TestDevice device = new TestDevice(log, listenersFactory, new DeviceData("device", "Device", "Device"));
        injector.getInstance(AnnotationProcessor.class).process(
                injector.getInstance(new Key<RealList<TypeData<?>, RealType<?, ?, ?>>>() {}),
                device);
        injector.getInstance(LocalClient.class).getRoot().addDevice(device);
        synchronized (lock) {
            lock.wait();
        }
        lr.removeListener();
        device.values.doubleValue(0.0);
        assertNull(vc.getError());
        assertSatisfied(vc, false);
        lr = injector.getInstance(RootBridge.class).getDevices().get("device").getValues().get("dv").addObjectListener(new ValueListener<ValueBridge>() {
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
        device.values.doubleValue(5.0);
        synchronized (lock) {
            lock.wait();
        }
        Thread.sleep(1); // the listener that notifies might get called before the vc listener, so the vc might not have updated yet
        lr.removeListener();
        assertSatisfied(vc, true);
    }

    private void assertValueComparisonSatisfied(ComparisonType operator, ValueSource sourceOne,
                                                ValueSource sourceTwo, boolean satisfied) throws HousemateException {
        assertSatisfied(makeValueComparison(operator, sourceOne, sourceTwo), satisfied);
    }

    private void assertSatisfied(ValueComparison valueComparison, boolean satisfied) {
        assertEquals(satisfied, valueComparison.isSatisfied());
    }

    private ValueComparison makeValueComparison(ComparisonType operator, ValueSource sourceOne, ValueSource sourceTwo) throws HousemateException {
        TypeInstanceMap values = new TypeInstanceMap();
        values.getChildren().put(ConditionFactory.TYPE_PARAMETER_ID, new TypeInstances(new TypeInstance("value-comparison")));
        values.getChildren().put(ConditionFactory.NAME_PARAMETER_ID, new TypeInstances(new TypeInstance("Test")));
        values.getChildren().put(ConditionFactory.DESCRIPTION_PARAMETER_ID, new TypeInstances(new TypeInstance("Test VC")));
        ValueComparison valueComparison = (ValueComparison) injector.getInstance(ConditionFactory.class).createCondition(values,
                new ServerRealConditionOwner() {
                    @Override
                    public void remove(ServerRealCondition condition) {
                        // do nothing
                    }
                });
        ServerRealProperty<Comparison> comparisonProperty = (ServerRealProperty<Comparison>) valueComparison.getProperties().get(ValueComparison.COMPARISON_ID);
        Comparison comparison = new Comparison(operator, COMPARISONS_BY_TYPE.get(operator), sourceOne, sourceTwo);
        comparisonProperty.setTypedValue(comparison);
        valueComparison.start();
        return valueComparison;
    }
}
