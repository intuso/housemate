package com.intuso.housemate.api.object.device;

import com.intuso.housemate.api.object.command.Command;
import com.intuso.housemate.api.object.command.HasCommands;
import com.intuso.housemate.api.object.list.List;
import com.intuso.housemate.api.object.primary.PrimaryObject;
import com.intuso.housemate.api.object.property.HasProperties;
import com.intuso.housemate.api.object.property.Property;
import com.intuso.housemate.api.object.value.HasValues;
import com.intuso.housemate.api.object.value.Value;

/**
 * @param <REMOVE_COMMAND> the type of the command for removing the automation
 * @param <START_STOP_COMMAND> the type of the command for stopping or starting
 * @param <CONNECTED_VALUE> the type of the connected value
 * @param <RUNNING_VALUE> the type of the running value
 * @param <ERROR_VALUE> the type of the error value
 * @param <COMMAND> the type of the commands
 * @param <COMMANDS> the type of the commands list
 * @param <VALUE> the type of the values
 * @param <VALUES> the type of the values list
 * @param <PROPERTY> the type of the properties
 * @param <PROPERTIES> the type of the properties list
 * @param <DEVICE> the type of the device
 */
public interface Device<
            REMOVE_COMMAND extends Command<?, ?>,
            START_STOP_COMMAND extends Command<?, ?>,
            COMMAND extends Command<?, ?>,
            COMMANDS extends List<? extends COMMAND>,
            CONNECTED_VALUE extends Value<?, ?>,
            RUNNING_VALUE extends Value<?, ?>,
            ERROR_VALUE extends Value<?, ?>,
            VALUE extends Value<?, ?>,
            VALUES extends List<? extends VALUE>,
            PROPERTY extends Property<?, ?, ?>,
            PROPERTIES extends List<? extends PROPERTY>,
            DEVICE extends Device<REMOVE_COMMAND, START_STOP_COMMAND, COMMAND, COMMANDS, CONNECTED_VALUE, RUNNING_VALUE, ERROR_VALUE, VALUE, VALUES, PROPERTY, PROPERTIES, DEVICE>>
        extends PrimaryObject<REMOVE_COMMAND, START_STOP_COMMAND, CONNECTED_VALUE, RUNNING_VALUE, ERROR_VALUE, DEVICE, DeviceListener<? super DEVICE>>, HasCommands<COMMANDS>, HasValues<VALUES>, HasProperties<PROPERTIES> {

    public final static String COMMANDS_ID = "commands";
    public final static String VALUES_ID = "values";
    public final static String PROPERTIES_ID = "properties";
}
