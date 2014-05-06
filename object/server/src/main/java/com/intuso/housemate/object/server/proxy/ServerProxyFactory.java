package com.intuso.housemate.object.server.proxy;

import com.google.inject.Inject;
import com.intuso.housemate.api.object.HousemateData;
import com.intuso.housemate.api.object.HousemateObjectFactory;
import com.intuso.housemate.api.object.command.CommandData;
import com.intuso.housemate.api.object.command.CommandFactory;
import com.intuso.housemate.api.object.device.DeviceData;
import com.intuso.housemate.api.object.device.DeviceFactory;
import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.list.ListFactory;
import com.intuso.housemate.api.object.option.OptionData;
import com.intuso.housemate.api.object.option.OptionFactory;
import com.intuso.housemate.api.object.parameter.ParameterData;
import com.intuso.housemate.api.object.parameter.ParameterFactory;
import com.intuso.housemate.api.object.property.PropertyData;
import com.intuso.housemate.api.object.property.PropertyFactory;
import com.intuso.housemate.api.object.subtype.SubTypeData;
import com.intuso.housemate.api.object.subtype.SubTypeFactory;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.api.object.type.TypeFactory;
import com.intuso.housemate.api.object.value.ValueData;
import com.intuso.housemate.api.object.value.ValueFactory;
import com.intuso.utilities.log.Log;

/**
 * Container class for all factories for server objects that are proxies of those on a client
 */
public class ServerProxyFactory {

    public static class All implements HousemateObjectFactory<HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>> {

        private final Log log;
        private final CommandFactory<ServerProxyCommand> commandFactory;
        private final DeviceFactory<ServerProxyDevice> deviceFactory;
        private final ListFactory<HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>, ServerProxyList<HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>>> listFactory;
        private final OptionFactory<ServerProxyOption> optionFactory;
        private final ParameterFactory<ServerProxyParameter> parameterFactory;
        private final PropertyFactory<ServerProxyProperty> propertyFactory;
        private final SubTypeFactory<ServerProxySubType> subTypeFactory;
        private final TypeFactory<ServerProxyType> typeFactory;
        private final ValueFactory<ServerProxyValue> valueFactory;

        @Inject
        public All(Log log, CommandFactory<ServerProxyCommand> commandFactory, DeviceFactory<ServerProxyDevice> deviceFactory, ListFactory<HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>, ServerProxyList<HousemateData<?>, ServerProxyObject<?, ?, ?, ?, ?>>> listFactory, OptionFactory<ServerProxyOption> optionFactory, ParameterFactory<ServerProxyParameter> parameterFactory, PropertyFactory<ServerProxyProperty> propertyFactory, SubTypeFactory<ServerProxySubType> subTypeFactory, TypeFactory<ServerProxyType> typeFactory, ValueFactory<ServerProxyValue> valueFactory) {
            this.log = log;
            this.commandFactory = commandFactory;
            this.deviceFactory = deviceFactory;
            this.listFactory = listFactory;
            this.optionFactory = optionFactory;
            this.parameterFactory = parameterFactory;
            this.propertyFactory = propertyFactory;
            this.subTypeFactory = subTypeFactory;
            this.typeFactory = typeFactory;
            this.valueFactory = valueFactory;
        }

        @Override
        public ServerProxyObject<?, ?, ?, ?, ?> create(HousemateData<?> data) {
            if(data instanceof ParameterData)
                return parameterFactory.create((ParameterData) data);
            else if(data instanceof CommandData)
                return commandFactory.create((CommandData) data);
            else if(data instanceof DeviceData)
                return deviceFactory.create((DeviceData) data);
            else if(data instanceof ListData)
                return listFactory.create((ListData<HousemateData<?>>) data);
            else if(data instanceof OptionData)
                return optionFactory.create((OptionData) data);
            else if(data instanceof PropertyData)
                return propertyFactory.create((PropertyData) data);
            else if(data instanceof SubTypeData)
                return subTypeFactory.create((SubTypeData) data);
            else if(data instanceof TypeData)
                return typeFactory.create((TypeData) data);
            else if(data instanceof ValueData)
                return valueFactory.create((ValueData) data);
            log.w("Don't know how to create an object from " + data.getClass().getName());
            return null;
        }
    }
}
