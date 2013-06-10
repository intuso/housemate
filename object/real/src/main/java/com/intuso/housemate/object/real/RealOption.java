package com.intuso.housemate.object.real;

import com.google.common.collect.Lists;
import com.intuso.housemate.api.object.list.ListWrappable;
import com.intuso.housemate.api.object.option.Option;
import com.intuso.housemate.api.object.option.OptionListener;
import com.intuso.housemate.api.object.option.OptionWrappable;
import com.intuso.housemate.api.object.subtype.SubTypeWrappable;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 08/07/12
 * Time: 10:13
 * To change this template use File | Settings | File Templates.
 */
public class RealOption
        extends RealObject<OptionWrappable, ListWrappable<SubTypeWrappable>,
            RealList<SubTypeWrappable, RealSubType<?>>, OptionListener>
        implements Option<RealList<SubTypeWrappable, RealSubType<?>>> {

    private final RealList<SubTypeWrappable, RealSubType<?>> subTypes;

    public RealOption(RealResources resources, String id, String name, String description) {
        this(resources, id, name,  description, Lists.<RealSubType<?>>newArrayList());
    }

    public RealOption(RealResources resources, String id, String name, String description, List<RealSubType<?>> subTypes) {
        super(resources, new OptionWrappable(id, name,  description));
        this.subTypes = new RealList<SubTypeWrappable, RealSubType<?>>(resources, SUB_TYPES, "Sub Types",
                "The sub types of this option", subTypes);
        addWrapper(this.subTypes);
    }

    @Override
    public final RealList<SubTypeWrappable, RealSubType<?>> getSubTypes() {
        return subTypes;
    }
}
