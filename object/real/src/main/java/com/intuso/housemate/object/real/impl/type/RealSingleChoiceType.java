package com.intuso.housemate.object.real.impl.type;

import com.intuso.housemate.api.object.list.ListWrappable;
import com.intuso.housemate.api.object.type.SingleChoiceTypeWrappable;
import com.intuso.housemate.api.object.option.HasOptions;
import com.intuso.housemate.api.object.option.OptionWrappable;
import com.intuso.housemate.object.real.RealList;
import com.intuso.housemate.object.real.RealOption;
import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.real.RealType;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 08/07/12
 * Time: 10:13
 * To change this template use File | Settings | File Templates.
 */
public abstract class RealSingleChoiceType<O>
        extends RealType<SingleChoiceTypeWrappable, ListWrappable<OptionWrappable>, O>
        implements HasOptions<RealList<OptionWrappable, RealOption>> {

    private final static String OPTIONS = "options";

    private RealList<OptionWrappable, RealOption> options;

    protected RealSingleChoiceType(RealResources resources, String id, String name, String description,
                                   List<RealOption> options) {
        super(resources, new SingleChoiceTypeWrappable(id, name, description));
        this.options = new RealList<OptionWrappable, RealOption>(resources, OPTIONS, OPTIONS, "The options for the choice", options);
        addWrapper(this.options);
    }

    @Override
    public RealList<OptionWrappable, RealOption> getOptions() {
        return options;
    }
}
