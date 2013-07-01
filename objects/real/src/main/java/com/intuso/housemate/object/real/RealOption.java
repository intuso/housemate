package com.intuso.housemate.object.real;

import com.intuso.housemate.api.object.list.ListWrappable;
import com.intuso.housemate.api.object.option.Option;
import com.intuso.housemate.api.object.option.OptionListener;
import com.intuso.housemate.api.object.option.OptionWrappable;
import com.intuso.housemate.api.object.subtype.SubTypeWrappable;

import java.util.Arrays;
import java.util.List;

public class RealOption
        extends RealObject<OptionWrappable, ListWrappable<SubTypeWrappable>,
            RealList<SubTypeWrappable, RealSubType<?>>, OptionListener>
        implements Option<RealList<SubTypeWrappable, RealSubType<?>>> {

    private final RealList<SubTypeWrappable, RealSubType<?>> subTypes;

    /**
     * @param resources {@inheritDoc}
     * @param id the option's id
     * @param name the option's name
     * @param description the option's description
     * @param subTypes the option's sub types
     */
    public RealOption(RealResources resources, String id, String name, String description, RealSubType<?> ... subTypes) {
        this(resources, id, name,  description, Arrays.asList(subTypes));
    }

    /**
     * @param resources {@inheritDoc}
     * @param id the option's id
     * @param name the option's name
     * @param description the option's description
     * @param subTypes the option's sub types
     */
    public RealOption(RealResources resources, String id, String name, String description, List<RealSubType<?>> subTypes) {
        super(resources, new OptionWrappable(id, name,  description));
        this.subTypes = new RealList<SubTypeWrappable, RealSubType<?>>(resources, SUB_TYPES_ID, "Sub Types",
                "The sub types of this option", subTypes);
        addWrapper(this.subTypes);
    }

    @Override
    public final RealList<SubTypeWrappable, RealSubType<?>> getSubTypes() {
        return subTypes;
    }
}
