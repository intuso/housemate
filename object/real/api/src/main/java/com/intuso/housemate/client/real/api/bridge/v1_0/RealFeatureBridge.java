package com.intuso.housemate.client.real.api.bridge.v1_0;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.real.api.internal.RealCommand;
import com.intuso.housemate.client.real.api.internal.RealFeature;
import com.intuso.housemate.client.real.api.internal.RealList;
import com.intuso.housemate.client.real.api.internal.RealValue;
import com.intuso.utilities.listener.ListenerRegistration;

/**
 * Created by tomc on 03/11/15.
 */
public class RealFeatureBridge
        implements RealFeature<
        RealList<? extends RealCommand<?, ?, ?>, ?>,
        RealList<? extends RealValue<?, ?, ?>, ?>,
        RealFeatureBridge> {

    private final com.intuso.housemate.client.v1_0.real.api.RealFeature<?, ?, ?> feature;
    private final ListMapper listMapper;
    private final CommandMapper commandMapper;
    private final ValueMapper valueMapper;

    @Inject
    public RealFeatureBridge(@Assisted com.intuso.housemate.client.v1_0.real.api.RealFeature<?, ?, ?> feature,
                             ListMapper listMapper,
                             CommandMapper commandMapper,
                             ValueMapper valueMapper) {
        this.feature = feature;
        this.listMapper = listMapper;
        this.commandMapper = commandMapper;
        this.valueMapper = valueMapper;
    }

    public com.intuso.housemate.client.v1_0.real.api.RealFeature getFeature() {
        return feature;
    }

    @Override
    public String getId() {
        return feature.getId();
    }

    @Override
    public String getName() {
        return feature.getName();
    }

    @Override
    public String getDescription() {
        return feature.getDescription();
    }

    @Override
    public ListenerRegistration addObjectListener(Listener<? super RealFeatureBridge> listener) {
        return null; //todo
    }

    @Override
    public RealList<? extends RealCommand<?, ?, ?>, ?> getCommands() {
        return listMapper.map((com.intuso.housemate.client.v1_0.real.api.RealList<com.intuso.housemate.client.v1_0.real.api.RealCommand<?, ?, ?>, ?>) feature.getCommands(),
                commandMapper.getFromV1_0Function(),
                commandMapper.getToV1_0Function());
    }

    @Override
    public RealList<? extends RealValue<?, ?, ?>, ?> getValues() {
        return listMapper.map((com.intuso.housemate.client.v1_0.real.api.RealList<com.intuso.housemate.client.v1_0.real.api.RealValue<?, ?, ?>, ?>) feature.getValues(),
                valueMapper.getFromV1_0Function(),
                valueMapper.getToV1_0Function());
    }

    public interface Factory {
        RealFeatureBridge create(com.intuso.housemate.client.v1_0.real.api.RealFeature<?, ?, ?> feature);
    }
}
