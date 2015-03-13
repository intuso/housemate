package com.intuso.housemate.api.object.hardware;

import com.intuso.housemate.api.object.BaseHousemateObject;
import com.intuso.housemate.api.object.list.List;
import com.intuso.housemate.api.object.property.HasProperties;
import com.intuso.housemate.api.object.property.Property;

/**
 * @param <PROPERTIES> the type of the properties list
 * @param <HARDWARE> the type of the device
 */
public interface Hardware<
            PROPERTIES extends List<? extends Property<?, ?, ?>>,
            HARDWARE extends Hardware<PROPERTIES, HARDWARE>>
        extends
            BaseHousemateObject<HardwareListener<? super HARDWARE>>, HasProperties<PROPERTIES> {

    public final static String PROPERTIES_ID = "properties";
}
