package com.intuso.housemate.client.real.api.bridge.v1_0;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.v1_0.real.api.RealCommand;
import com.intuso.housemate.client.v1_0.real.api.RealFeature;
import com.intuso.housemate.client.v1_0.real.api.RealList;
import com.intuso.housemate.client.v1_0.real.api.RealValue;
import com.intuso.utilities.listener.ListenerRegistration;

/**
 * Created by tomc on 03/11/15.
 */
public class RealFeatureBridgeReverse implements RealFeature {

    private final com.intuso.housemate.client.real.api.internal.RealFeature feature;
    private final ListMapper listMapper;
    private final CommandMapper commandMapper;
    private final ValueMapper valueMapper;

    @Inject
    public RealFeatureBridgeReverse(@Assisted com.intuso.housemate.client.real.api.internal.RealFeature feature,
                                    ListMapper listMapper,
                                    CommandMapper commandMapper,
                                    ValueMapper valueMapper) {
        this.feature = feature;
        this.listMapper = listMapper;
        this.commandMapper = commandMapper;
        this.valueMapper = valueMapper;
    }

    public com.intuso.housemate.client.real.api.internal.RealFeature getFeature() {
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
    public String[] getPath() {
        return feature.getPath();
    }

    @Override
    public ListenerRegistration addObjectListener(Listener<? super RealFeature> listener) {
        return null; //todo
    }

    @Override
    public RealList<RealCommand> getCommands() {
        return listMapper.map(feature.getCommands(),
                commandMapper.getToV1_0Function(),
                commandMapper.getFromV1_0Function());
    }

    @Override
    public RealList<RealValue<?>> getValues() {
        return listMapper.map(feature.getValues(),
                valueMapper.getToV1_0Function(),
                valueMapper.getFromV1_0Function());
    }

    public interface Factory {
        RealFeatureBridgeReverse create(com.intuso.housemate.client.real.api.internal.RealFeature feature);
    }
}
