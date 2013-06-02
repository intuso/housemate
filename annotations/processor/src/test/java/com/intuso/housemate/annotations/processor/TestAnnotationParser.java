package com.intuso.housemate.annotations.processor;

import com.google.common.collect.Maps;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.type.TypeWrappable;
import com.intuso.housemate.object.real.RealList;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.real.RealType;
import com.intuso.housemate.object.real.impl.type.BooleanType;
import com.intuso.housemate.object.real.impl.type.DoubleType;
import com.intuso.housemate.object.real.impl.type.IntegerType;
import com.intuso.housemate.object.real.impl.type.StringType;
import com.intuso.utilities.log.Log;
import org.junit.Test;

import java.util.Map;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 02/06/13
 * Time: 11:32
 * To change this template use File | Settings | File Templates.
 */
public class TestAnnotationParser {

    private static RealResources createResources() {
        return new RealResources(new Log("testLog"), Maps.<String, String>newHashMap(), null);
    }

    private static RealList<TypeWrappable<?>, RealType<?, ?, ?>> createAvailableTypes(RealResources resources) {
        RealList<TypeWrappable<?>, RealType<?, ?, ?>> availableTypes = new RealList<TypeWrappable<?>, RealType<?, ?, ?>>(
                resources, "types", "types", "types");
        availableTypes.add(new StringType(resources));
        availableTypes.add(new BooleanType(resources));
        availableTypes.add(new IntegerType(resources));
        availableTypes.add(new DoubleType(resources));
        return availableTypes;
    }

    @Test
    public void testAvailableTypes() {
        RealResources resources = createResources();
        new AnnotationProcessor(resources.getLog(), createAvailableTypes(resources));
    }

    @Test
    public void testCommands() throws HousemateException {
        // create classes
        RealResources resources = createResources();
        AnnotationProcessor annotationParser = new AnnotationProcessor(resources.getLog(), createAvailableTypes(resources));
        TestChildDevice testDevice = new TestChildDevice(resources, "test-device", "Test Device", "Test Device");

        // parse all annotations
        annotationParser.process(testDevice);

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
        testDevice.getProperties().get("switch-number").setValue("2");
        Map<String, String> values = Maps.newHashMap();
        values.put("on", "true");
        testDevice.getCommands().get("command").perform(values);
        assertEquals("On 2", testDevice.getValues().get("last-command").getValue());

        // test child device element instances
        testDevice.getProperties().get("increment-amount").setValue("2");
        values = Maps.newHashMap();
        testDevice.getCommands().get("turn-up").perform(values);
        testDevice.getCommands().get("turn-up").perform(values);
        testDevice.getCommands().get("turn-up").perform(values);
        testDevice.getCommands().get("turn-up").perform(values);
        testDevice.getCommands().get("turn-down").perform(values);
        assertEquals("6", testDevice.getValues().get("volume").getValue());
    }
}
