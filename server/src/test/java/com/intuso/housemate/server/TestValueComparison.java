package com.intuso.housemate.server;

import com.google.common.collect.Maps;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.intuso.housemate.client.real.api.internal.*;
import com.intuso.housemate.client.real.api.internal.annotations.TypeInfo;
import com.intuso.housemate.client.real.api.internal.driver.DeviceDriver;
import com.intuso.housemate.client.real.impl.internal.RealTypeImpl;
import com.intuso.housemate.client.real.impl.internal.factory.FactoryType;
import com.intuso.housemate.client.real.impl.internal.factory.condition.AddConditionCommand;
import com.intuso.housemate.client.real.impl.internal.type.DoubleType;
import com.intuso.housemate.client.real.impl.internal.type.IntegerType;
import com.intuso.housemate.client.real.impl.internal.type.RealObjectType;
import com.intuso.housemate.comms.api.internal.payload.DeviceData;
import com.intuso.housemate.comms.api.internal.payload.SimpleTypeData;
import com.intuso.housemate.object.api.internal.*;
import com.intuso.housemate.plugin.api.internal.*;
import com.intuso.housemate.plugin.manager.PluginManager;
import com.intuso.housemate.server.comms.MainRouter;
import com.intuso.housemate.server.ioc.ServerModule;
import com.intuso.housemate.server.object.bridge.RootBridge;
import com.intuso.housemate.server.object.bridge.ValueBridge;
import com.intuso.housemate.server.object.real.FactoryPluginListener;
import com.intuso.housemate.server.plugin.main.comparator.DoubleComparators;
import com.intuso.housemate.server.plugin.main.comparator.IntegerComparators;
import com.intuso.housemate.server.plugin.main.condition.ValueComparison;
import com.intuso.housemate.server.plugin.main.ioc.MainPluginModule;
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
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;

/**
 */
public class TestValueComparison {

    private final static Map<String, Map<String, Comparator<?>>> COMPARISONS_BY_TYPE = Maps.newHashMap();
    static {
        COMPARISONS_BY_TYPE.put(CommonComparators.Equal.class.getAnnotation(TypeInfo.class).id(), Maps.<String, Comparator<?>>newHashMap());
        COMPARISONS_BY_TYPE.get(CommonComparators.Equal.class.getAnnotation(TypeInfo.class).id()).put(SimpleTypeData.Type.Integer.getId(), new IntegerComparators.Equal());
        COMPARISONS_BY_TYPE.get(CommonComparators.Equal.class.getAnnotation(TypeInfo.class).id()).put(SimpleTypeData.Type.Double.getId(), new DoubleComparators.Equal());
    }
    
    private static Injector injector;
    private static Log log;
    private static RealAutomation automation;
    private static final ListenersFactory listenersFactory = new ListenersFactory() {
        @Override
        public <LISTENER extends Listener> Listeners<LISTENER> create() {
            return new Listeners<>(new CopyOnWriteArrayList<LISTENER>());
        }
    };

    @BeforeClass
    public static void initResources() {
        // create the default property repository
        WriteableMapPropertyRepository defaultProperties = WriteableMapPropertyRepository.newEmptyRepository(listenersFactory);
        // create the main injector
        injector = Guice.createInjector(new TestModule(listenersFactory, defaultProperties), new ServerModule(defaultProperties));
        injector = injector.createChildInjector(injector.getInstance(MainPluginModule.class));
        // make sure all the framework is created before we add plugins
        injector.getInstance(RootBridge.class);
        injector.getInstance(MainRouter.class).start();
        // force factory listener to be created and register itself for new plugins
        injector.getInstance(FactoryPluginListener.class);
//        need to get local client too? something not adding plugins types to main type list
        // add the main plugin module
        injector.getInstance(PluginManager.class).addPlugin(injector.createChildInjector(new MainPluginModule()));
        log = injector.getInstance(Log.class);
        RealRoot root = injector.getInstance(RealRoot.class);
        TypeInstanceMap automationValues = new TypeInstanceMap();
        automationValues.getChildren().put(AddConditionCommand.NAME_PARAMETER_ID, new TypeInstances(new TypeInstance("test-automation")));
        automationValues.getChildren().put(AddConditionCommand.DESCRIPTION_PARAMETER_ID, new TypeInstances(new TypeInstance("test-automation")));
        root.getAddAutomationCommand().perform(automationValues);
        automation = root.getAutomations().get("test-automation");
    }

    @Test
    public void testTwoConstantsEqualsTrue() {
        RealType<?> integerType = new IntegerType(log, listenersFactory);
        ConstantValue valueOne = new ConstantValue(listenersFactory, integerType, new TypeInstances(new TypeInstance("1")));
        ConstantValue valueTwo = new ConstantValue(listenersFactory, integerType, new TypeInstances(new TypeInstance("1")));
        assertValueComparisonSatisfied("constant-true", CommonComparators.Equal.class.getAnnotation(TypeInfo.class), valueOne, valueTwo, true);
    }

    @Test
    public void testTwoConstantsEqualsFalse() {
        RealType<?> integerType = new IntegerType(log, listenersFactory);
        ConstantValue valueOne = new ConstantValue(listenersFactory, integerType, new TypeInstances(new TypeInstance("1")));
        ConstantValue valueTwo = new ConstantValue(listenersFactory, integerType, new TypeInstances(new TypeInstance("2")));
        assertValueComparisonSatisfied("constant-false", CommonComparators.Equal.class.getAnnotation(TypeInfo.class), valueOne, valueTwo, false);
    }

    @Test
    public void testLocationSources() throws InterruptedException {
        final Object lock = new Object();

        RealList<RealType<?>> types = injector.getInstance(new Key<RealList<RealType<?>>>() {});

        // create value one as (((double) 2) + 3.0)
        RealTypeImpl<?, ?, ?> integerType = new IntegerType(log, listenersFactory);
        RealTypeImpl<?, ?, ?> doubleType = new DoubleType(log, listenersFactory);
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
                new Operation(CommonOperators.Add.class.getAnnotation(TypeInfo.class), new HashMap<String, Operator<?, ?>>() {
                    {
                        put(SimpleTypeData.Type.Double.getId(), new DoubleOperators.Add());
                    }
                }, doubleTwo, doubleThree));

        // create second value as one from the device
        String[] valuePath = new String[] {"", "servers", "local-Internal Client", "devices", "device", "features", "feature", "values", "value"};
        ValueLocation valueTwo = new ValueLocation(listenersFactory,
                new RealObjectType.Reference<Value<TypeInstances, ?>>(valuePath),
                injector.getInstance(RootBridge.class));
        ListenerRegistration lr = injector.getInstance(RootBridge.class).
                addObjectLifecycleListener(valuePath, new ObjectLifecycleListener() {
                    @Override
                    public void objectCreated(String[] path, BaseHousemateObject<?> object) {
                        synchronized (lock) {
                            lock.notify();
                        }
                    }

                    @Override
                    public void objectRemoved(String[] path, BaseHousemateObject<?> object) {
                        // do nothing
                    }
                });
        RealCondition<ValueComparison> vc = makeValueComparison("locations", CommonComparators.Equal.class.getAnnotation(TypeInfo.class), valueOne, valueTwo);
        assertEquals(vc.getErrorValue().getTypedValue(), "Second value is not available");
        RealDevice<TestDeviceDriver> device = (RealDevice<TestDeviceDriver>) injector.getInstance(RealDevice.Factory.class).create(new DeviceData("device", "Device", "Device"), null);
        device.getDriverProperty().setTypedValues(injector.getInstance(new Key<FactoryType.Entry<DeviceDriver.Factory<TestDeviceDriver>>>() {}));
        injector.getInstance(RealRoot.class).addDevice(device);
        synchronized (lock) {
            lock.wait(1000);
        }
        lr.removeListener();
        device.getDriver().values.doubleValue(0.0);
        assertNull(vc.getErrorValue().getTypedValue());
        assertSatisfied(vc, false);
        ValueBridge value = injector.getInstance(RootBridge.class).getServers().get("local-Internal Client").getDevices().get("device").getFeatures().get("feature").getValues().get("value");
        lr = value.addObjectListener(new Value.Listener<ValueBridge>() {
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
        device.getDriver().values.doubleValue(5.0);
        synchronized (lock) {
            lock.wait(1000);
        }
        Thread.sleep(1); // the listener that notifies might get called before the vc listener, so the vc might not have updated yet
        lr.removeListener();
        assertSatisfied(vc, true);
    }

    private void assertValueComparisonSatisfied(String id, TypeInfo typeInfo, ValueSource sourceOne,
                                                ValueSource sourceTwo, boolean satisfied) {
        assertSatisfied(makeValueComparison(id, typeInfo, sourceOne, sourceTwo), satisfied);
    }

    private void assertSatisfied(RealCondition<ValueComparison> valueComparison, boolean satisfied) {
        assertEquals(satisfied, valueComparison.getSatisfiedValue().getTypedValue());
    }

    private RealCondition<ValueComparison> makeValueComparison(String id, TypeInfo typeInfo, ValueSource sourceOne, ValueSource sourceTwo) {
        TypeInstanceMap conditionValues = new TypeInstanceMap();
        conditionValues.getChildren().put(AddConditionCommand.TYPE_PARAMETER_ID, new TypeInstances(new TypeInstance("value-comparison")));
        conditionValues.getChildren().put(AddConditionCommand.NAME_PARAMETER_ID, new TypeInstances(new TypeInstance(id)));
        conditionValues.getChildren().put(AddConditionCommand.DESCRIPTION_PARAMETER_ID, new TypeInstances(new TypeInstance(id)));
        automation.getAddConditionCommand().perform(conditionValues);
        RealCondition<ValueComparison> valueComparison = (RealCondition<ValueComparison>) automation.getConditions().get(id);
        RealProperty<Comparison> comparisonProperty = (RealProperty<Comparison>) valueComparison.getProperties().get("comparison");
        Comparison comparison = new Comparison(typeInfo, COMPARISONS_BY_TYPE.get(typeInfo.id()), sourceOne, sourceTwo);
        comparisonProperty.setTypedValues(comparison);
        valueComparison.start();
        return valueComparison;
    }
}
