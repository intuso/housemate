package com.intuso.housemate.object.real.impl.type;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.intuso.housemate.api.HousemateRuntimeException;
import com.intuso.housemate.api.object.type.TypeSerialiser;
import com.intuso.housemate.api.object.type.option.OptionWrappable;
import com.intuso.housemate.object.real.RealOption;
import com.intuso.housemate.object.real.RealResources;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 08/07/12
 * Time: 10:13
 * To change this template use File | Settings | File Templates.
 */
public abstract class EnumSingleChoiceType<E extends Enum<E>> extends RealSingleChoiceType<E> {

    private Class<E> enumClass;

    protected EnumSingleChoiceType(RealResources resources, String id, String name, String description, Class<E> enumClass, E[] values) {
        super(resources, id, name, description, convertValuesToOptions(resources, values), new EnumInstanceSerialiser<E>(enumClass));
        this.enumClass = enumClass;
    }

    public static List<RealOption> convertValuesToOptions(final RealResources resources, Enum[] values) {
        return Lists.transform(Arrays.asList(values), new Function<Enum, RealOption>() {
            @Override
            public RealOption apply(@Nullable Enum value) {
                return new RealOption(resources, value.name(), value.name(), value.name());
            }
        });
    }

    public static List<OptionWrappable> convertValuesToOptionWrappables(Enum[] values) {
        return Lists.transform(Arrays.asList(values), new Function<Enum, OptionWrappable>() {
            @Override
            public OptionWrappable apply(@Nullable Enum value) {
                return new OptionWrappable(value.name(), value.name(), value.name());
            }
        });
    }

    public static class EnumInstanceSerialiser<E extends Enum<E>> implements TypeSerialiser<E> {

        private final Class<E> enumClass;

        public EnumInstanceSerialiser(Class<E> enumClass) {
            this.enumClass = enumClass;
        }

        @Override
        public String serialise(E value) {
            return value.name();
        }

        @Override
        public E deserialise(String value) {
            try {
                return Enum.valueOf(enumClass, value);
            } catch(Throwable t) {
                throw new HousemateRuntimeException("Could not convert \"" + value + "\" to instance of enum " + enumClass.getName());
            }
        }
    }
}
