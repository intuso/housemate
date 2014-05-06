package com.intuso.housemate.object.real.impl.type;

import com.intuso.housemate.api.object.list.ListData;
import com.intuso.housemate.api.object.option.HasOptions;
import com.intuso.housemate.api.object.option.OptionData;
import com.intuso.housemate.api.object.type.ChoiceTypeData;
import com.intuso.housemate.object.real.RealList;
import com.intuso.housemate.object.real.RealOption;
import com.intuso.housemate.object.real.RealType;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.Arrays;
import java.util.List;

/**
 * Type for selecting options from a list
 */
public abstract class RealChoiceType<O>
        extends RealType<ChoiceTypeData, ListData<OptionData>, O>
        implements HasOptions<RealList<OptionData, RealOption>> {

    public final static String OPTIONS = "options";

    private RealList<OptionData, RealOption> options;

    /**
     * @param log the log
     * @param listenersFactory
     * @param id the type's id
     * @param name the type's name
     * @param description the type's description
     * @param minValues the minimum number of values the type can have
     * @param maxValues the maximum number of values the type can have
     * @param options the type's options
     */
    protected RealChoiceType(Log log, ListenersFactory listenersFactory, String id, String name, String description, int minValues,
                             int maxValues, RealOption... options) {
        this(log, listenersFactory, id, name, description, minValues, maxValues, Arrays.asList(options));
    }

    /**
     * @param log the log
     * @param listenersFactory
     * @param id the type's id
     * @param name the type's name
     * @param description the type's description
     * @param minValues the minimum number of values the type can have
     * @param maxValues the maximum number of values the type can have
     * @param options the type's options
     */
    protected RealChoiceType(Log log, ListenersFactory listenersFactory, String id, String name, String description, int minValues,
                             int maxValues, List<RealOption> options) {
        super(log, listenersFactory, new ChoiceTypeData(id, name, description, minValues, maxValues));
        this.options = new RealList<OptionData, RealOption>(log, listenersFactory, OPTIONS, OPTIONS, "The options for the choice", options);
        addChild(this.options);
    }

    @Override
    public RealList<OptionData, RealOption> getOptions() {
        return options;
    }
}
