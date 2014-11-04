package com.intuso.housemate.comms.serialiser.json;

import com.google.common.collect.Lists;
import com.intuso.housemate.api.comms.Message;
import com.intuso.housemate.api.object.command.CommandData;
import com.intuso.housemate.api.object.device.Device;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.api.object.value.ValueData;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by tomc on 27/10/14.
 */
public class TestJsonSerialisation {

    private final List<Byte> bytes = Lists.newArrayList();
    private JsonSerialiser serialiser;

    @Before
    public void init() throws IOException {
        OutputStream os = new OutputStream() {
            @Override
            public void write(int oneByte) throws IOException {
                bytes.add((byte)oneByte);
            }
        };
        InputStream is = new InputStream() {
            @Override
            public int read() throws IOException {
                return bytes.size() > 0 ? bytes.remove(0) : -1;
            }
        };
        serialiser = new JsonSerialiser(os, is);
    }

    @Test
    public void testCommandData() throws IOException, InterruptedException {
        bytes.clear();
        CommandData startData = new CommandData("testCommand", "Test Command", "A command for testing");
        serialiser.write(new Message<CommandData>(new String[] {"path"}, "commandData", startData, Lists.newArrayList("0")));
        CommandData endData = (CommandData)(serialiser.read().getPayload());
        assertEquals(startData.getId(), endData.getId());
        assertEquals(startData.getName(), endData.getName());
        assertEquals(startData.getDescription(), endData.getDescription());
    }

    @Test
    public void testValueData() throws IOException, InterruptedException {
        bytes.clear();
        ValueData startData = new ValueData("testValue", "Test Value", "A value for testing", "string",
                new TypeInstances(new TypeInstance("value")));
        serialiser.write(new Message<ValueData>(new String[] {"path"}, "valueData", startData, Lists.newArrayList("0")));
        ValueData endData = (ValueData)(serialiser.read().getPayload());
        assertEquals(startData.getId(), endData.getId());
        assertEquals(startData.getName(), endData.getName());
        assertEquals(startData.getDescription(), endData.getDescription());
        assertEquals(startData.getType(), endData.getType());
        assertNotNull(endData.getTypeInstances());
        assertEquals(startData.getTypeInstances().getElements().size(), endData.getTypeInstances().getElements().size());
        assertEquals(startData.getTypeInstances().getFirstValue(), endData.getTypeInstances().getFirstValue());
    }

    @Test
    public void testPropertyData() throws IOException, InterruptedException {
        bytes.clear();
        PropertyData startData = new PropertyData("testProperty", "Test Property", "A property for testing", "string",
                new TypeInstances(new TypeInstance("value")));
        serialiser.write(new Message<PropertyData>(new String[] {"path"}, "propertyData", startData, Lists.newArrayList("0")));
        PropertyData endData = (PropertyData)(serialiser.read().getPayload());
        assertEquals(startData.getId(), endData.getId());
        assertEquals(startData.getName(), endData.getName());
        assertEquals(startData.getDescription(), endData.getDescription());
        assertEquals(startData.getType(), endData.getType());
        assertNotNull(endData.getTypeInstances());
        assertEquals(startData.getTypeInstances().getElements().size(), endData.getTypeInstances().getElements().size());
        assertEquals(startData.getTypeInstances().getFirstValue(), endData.getTypeInstances().getFirstValue());
    }

    @Test
    public void testDeviceData() throws IOException, InterruptedException {
        bytes.clear();
        DeviceData startData = new DeviceData("testDevice", "Test Device", "A device for testing",
                Lists.newArrayList("feature"), Lists.newArrayList("customCommand"), Lists.newArrayList("customValue"),
                Lists.newArrayList("customProperty"));
        serialiser.write(new Message<DeviceData>(new String[] {"path"}, "deviceData", startData, Lists.newArrayList("0")));
        DeviceData endData = (DeviceData)(serialiser.read().getPayload());
        assertEquals(startData.getId(), endData.getId());
        assertEquals(startData.getName(), endData.getName());
        assertEquals(startData.getDescription(), endData.getDescription());
        assertNotNull(endData.getFeatureIds());
        assertEquals(startData.getFeatureIds().size(), endData.getFeatureIds().size());
        assertNotNull(endData.getCustomCommandIds());
        assertEquals(startData.getCustomCommandIds().size(), endData.getCustomCommandIds().size());
        assertNotNull(endData.getCustomPropertyIds());
        assertEquals(startData.getCustomPropertyIds().size(), endData.getCustomPropertyIds().size());
        assertNotNull(endData.getCustomValueIds());
        assertEquals(startData.getCustomValueIds().size(), endData.getCustomValueIds().size());
    }

    @Test
    public void testComplexDeviceData() throws IOException, InterruptedException {
        bytes.clear();
        DeviceData startData = new DeviceData("testDevice", "Test Device", "A device for testing",
                Lists.newArrayList("feature"), Lists.newArrayList("customCommand"), Lists.newArrayList("customValue"),
                Lists.newArrayList("customProperty"));
        startData.addChildData(new ListData<CommandData>(Device.COMMANDS_ID, "Commands", "Commands",
                new CommandData("testCommand", "Test Command", "A command for testing")));
        startData.addChildData(new ListData<ValueData>(Device.VALUES_ID, "Values", "Values",
                new ValueData("testValue", "Test Value", "A value for testing", "string",
                        new TypeInstances(new TypeInstance("value")))));
        startData.addChildData(new ListData<PropertyData>(Device.PROPERTIES_ID, "Properties", "Properties",
                new PropertyData("testProperty", "Test Property", "A property for testing", "string",
                        new TypeInstances(new TypeInstance("value")))));
        serialiser.write(new Message<DeviceData>(new String[] {"path"}, "deviceData", startData, Lists.newArrayList("0")));
        DeviceData endData = (DeviceData)(serialiser.read().getPayload());

    }
}
