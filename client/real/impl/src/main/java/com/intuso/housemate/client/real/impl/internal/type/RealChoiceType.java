package com.intuso.housemate.client.real.impl.internal.type;

import com.intuso.housemate.client.real.api.internal.RealList;
import com.intuso.housemate.client.real.api.internal.RealOption;
import com.intuso.housemate.client.real.impl.internal.RealListImpl;
import com.intuso.housemate.client.real.impl.internal.RealOptionImpl;
import com.intuso.housemate.client.real.impl.internal.RealTypeImpl;
import com.intuso.housemate.comms.api.internal.payload.ChoiceTypeData;
import com.intuso.housemate.comms.api.internal.payload.ListData;
import com.intuso.housemate.comms.api.internal.payload.OptionData;
import com.intuso.housemate.object.api.internal.Option;
import com.intuso.utilities.listener.ListenersFactory;
import com.intuso.utilities.log.Log;

import java.util.Arrays;
import java.util.List;

/**
 * Type for selecting options from a list
 */
public abstract class RealChoiceType<O>
        extends RealTypeImpl<ChoiceTypeData, ListData<OptionData>, O>
        implements Option.Container<RealList<? extends RealOption>> {

    public final static String OPTIONS = "options";

    protected final RealListImpl<OptionData, RealOptionImpl> options;

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
                             int maxValues, RealOptionImpl... options) {
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
                             int maxValues, List<RealOptionImpl> options) {
        super(log, listenersFactory, new ChoiceTypeData(id, name, description, minValues, maxValues));
        this.options = new RealListImpl<>(log, listenersFactory, OPTIONS, OPTIONS, "The options for the choice", options);
        addChild(this.options);
    }

    @Override
    public RealList<? extends RealOption> getOptions() {
        return options;
    }
}
