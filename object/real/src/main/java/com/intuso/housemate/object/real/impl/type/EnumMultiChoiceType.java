package com.intuso.housemate.object.real.impl.type;

import com.intuso.housemate.object.real.RealResources;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 08/07/12
 * Time: 10:13
 * To change this template use File | Settings | File Templates.
 */
public abstract class EnumMultiChoiceType<E extends Enum<E>> extends RealMultiChoiceType<E> {

    protected EnumMultiChoiceType(RealResources resources, String id, String name, String description, Class<E> enumClass, E[] values) {
        super(resources, id, name, description, EnumSingleChoiceType.convertValuesToOptions(resources, values), new EnumSingleChoiceType.EnumInstanceSerialiser<E>(enumClass));
    }
}
