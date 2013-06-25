package com.intuso.housemate.object.real.impl.type;

import com.intuso.housemate.api.object.list.ListWrappable;
import com.intuso.housemate.api.object.type.SingleChoiceTypeWrappable;
import com.intuso.housemate.api.object.option.HasOptions;
import com.intuso.housemate.api.object.option.OptionWrappable;
import com.intuso.housemate.object.real.RealList;
import com.intuso.housemate.object.real.RealOption;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.real.RealType;

import java.util.Arrays;
import java.util.List;

/**
 * A type for selecting one of a set of options
 */
public abstract class RealSingleChoiceType<O>
        extends RealType<SingleChoiceTypeWrappable, ListWrappable<OptionWrappable>, O>
        implements HasOptions<RealList<OptionWrappable, RealOption>> {

    private final static String OPTIONS_ID = "options";

    private RealList<OptionWrappable, RealOption> options;

    /**
     * @param resources the resources
     * @param id the type's id
     * @param name the type's name
     * @param description the type's description
     * @param options the type's options
     */
    protected RealSingleChoiceType(RealResources resources, String id, String name, String description,
                                   RealOption ... options) {
        this(resources, id, name, description, Arrays.asList(options));
    }

    /**
     * @param resources the resources
     * @param id the type's id
     * @param name the type's name
     * @param description the type's description
     * @param options the type's options
     */
    protected RealSingleChoiceType(RealResources resources, String id, String name, String description,
                                   List<RealOption> options) {
        super(resources, new SingleChoiceTypeWrappable(id, name, description));
        this.options = new RealList<OptionWrappable, RealOption>(resources, OPTIONS_ID, OPTIONS_ID, "The options for the choice", options);
        addWrapper(this.options);
    }

    @Override
    public RealList<OptionWrappable, RealOption> getOptions() {
        return options;
    }
}
