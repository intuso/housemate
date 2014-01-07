package com.intuso.housemate.annotations.processor;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.object.real.RealList;
import com.intuso.housemate.object.real.RealProperty;
import com.intuso.housemate.object.real.RealType;
import com.intuso.housemate.object.real.impl.type.BooleanType;
import com.intuso.housemate.object.real.impl.type.DoubleType;
import com.intuso.housemate.object.real.impl.type.IntegerType;
import com.intuso.housemate.object.real.impl.type.StringType;
import com.intuso.utilities.log.Log;
import org.junit.Test;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

/**
 */
public class TestAnnotationParser {

    private static Log createLog() {
        return new Log();
    }

    private static RealList<TypeData<?>, RealType<?, ?, ?>> createAvailableTypes(Log log) {
        RealList<TypeData<?>, RealType<?, ?, ?>> availableTypes = new RealList<TypeData<?>, RealType<?, ?, ?>>(
                log, "types", "types", "types");
        availableTypes.add(new StringType(log));
        availableTypes.add(new BooleanType(log));
        availableTypes.add(new IntegerType(log));
        availableTypes.add(new DoubleType(log));
        return availableTypes;
    }

    @Test
    public void testAvailableTypes() {
        new AnnotationProcessor(createLog());
    }

    @Test
    public void testCommands() throws HousemateException {
        // create classes
        Log log = createLog();
        AnnotationProcessor annotationParser = new AnnotationProcessor(log);
        TestChildDevice testDevice = new TestChildDevice(log, "test-device", "Test Device", "Test Device");

        // parse all annotations
        annotationParser.process(createAvailableTypes(log), testDevice);

        // check all parent device elements are created
        assertNotNull(testDevice.getCommands().get("command"));
        assertNotNull(testDevice.getValues().get("last-command"));
        assertNotNull(testDevice.getProperties().get("switch-number"));

        // check all child device elements are created
        assertNotNull(testDevice.getCommands().get("turn-up"));
        assertNotNull(testDevice.getCommands().get("turn-down"));
        assertNotNull(testDevice.getValues().get("volume"));
        assertNotNull(testDevice.getProperties().get("increment-amount"));

        // test parent device element instances
        ((RealProperty<Integer>)testDevice.getProperties().get("switch-number")).setTypedValues(new Integer(2));
        TypeInstanceMap values = new TypeInstanceMap();
        values.put("on", new TypeInstances(new TypeInstance("true")));
        testDevice.getCommands().get("command").perform(values);
        assertEquals("On 2", testDevice.getValues().get("last-command").getTypeInstances().getFirstValue());

        // test child device element instances
        ((RealProperty<Integer>)testDevice.getProperties().get("increment-amount")).setTypedValues(2);
        values = new TypeInstanceMap();
        testDevice.getCommands().get("turn-up").perform(values);
        testDevice.getCommands().get("turn-up").perform(values);
        testDevice.getCommands().get("turn-up").perform(values);
        testDevice.getCommands().get("turn-up").perform(values);
        testDevice.getCommands().get("turn-down").perform(values);
        assertEquals("6", testDevice.getValues().get("volume").getTypeInstances().getFirstValue());
    }
}
