package com.intuso.housemate.client.real.api.bridge.v1_0;

import com.google.common.base.Function;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.intuso.housemate.client.real.api.bridge.v1_0.driver.FeatureDriverFactoryMapper;
import com.intuso.housemate.client.real.api.bridge.v1_0.driver.FeatureDriverMapper;
import com.intuso.housemate.client.real.api.internal.driver.FeatureDriver;
import com.intuso.housemate.client.real.api.internal.driver.PluginDependency;
import com.intuso.housemate.client.real.api.internal.object.*;
import com.intuso.utilities.listener.ListenerRegistration;

/**
 * Created by tomc on 03/11/15.
 */
public class RealFeatureBridgeReverse
        implements com.intuso.housemate.client.v1_0.real.api.object.RealFeature<
        com.intuso.housemate.client.v1_0.real.api.object.RealCommand<?, ?, ?>,
        com.intuso.housemate.client.v1_0.real.api.object.RealList<? extends com.intuso.housemate.client.v1_0.real.api.object.RealCommand<?, ?, ?>, ?>,
        com.intuso.housemate.client.v1_0.real.api.object.RealValue<Boolean, ?, ?>,
        com.intuso.housemate.client.v1_0.real.api.object.RealValue<String, ?, ?>,
        com.intuso.housemate.client.v1_0.real.api.object.RealList<? extends com.intuso.housemate.client.v1_0.real.api.object.RealValue<?, ?, ?>, ?>,
        com.intuso.housemate.client.v1_0.real.api.object.RealProperty<com.intuso.housemate.client.v1_0.real.api.driver.PluginDependency<com.intuso.housemate.client.v1_0.real.api.driver.FeatureDriver.Factory<?>>, ?, ?, ?>,
        com.intuso.housemate.client.v1_0.real.api.object.RealList<? extends com.intuso.housemate.client.v1_0.real.api.object.RealProperty<?, ?, ?, ?>, ?>,
        RealFeatureBridgeReverse> {

    private final RealFeature<?, ?, ?, ?, ?, ?, ?, ?> feature;
    private final ListMapper listMapper;
    private final CommandMapper commandMapper;
    private final ValueMapper valueMapper;
    private final PropertyMapper propertyMapper;
    private final com.intuso.housemate.client.real.api.bridge.v1_0.driver.PluginResourceMapper pluginResourceMapper;
    private final FeatureDriverMapper featureDriverMapper;
    private final FeatureDriverFactoryMapper featureDriverFactoryMapper;

    @Inject
    public RealFeatureBridgeReverse(@Assisted RealFeature<?, ?, ?, ?, ?, ?, ?, ?> feature,
                                    ListMapper listMapper,
                                    CommandMapper commandMapper,
                                    ValueMapper valueMapper,
                                    PropertyMapper propertyMapper,
                                    com.intuso.housemate.client.real.api.bridge.v1_0.driver.PluginResourceMapper pluginResourceMapper,
                                    FeatureDriverMapper featureDriverMapper,
                                    FeatureDriverFactoryMapper featureDriverFactoryMapper) {
        this.feature = feature;
        this.listMapper = listMapper;
        this.commandMapper = commandMapper;
        this.valueMapper = valueMapper;
        this.propertyMapper = propertyMapper;
        this.pluginResourceMapper = pluginResourceMapper;
        this.featureDriverMapper = featureDriverMapper;
        this.featureDriverFactoryMapper = featureDriverFactoryMapper;
    }

    public RealFeature<?, ?, ?, ?, ?, ?, ?, ?> getFeature() {
        return feature;
    }

    @Override
    public <TO extends com.intuso.housemate.client.v1_0.real.api.driver.FeatureDriver> TO getDriver() {
        return (TO) featureDriverMapper.map(feature.getDriver());
    }

    @Override
    public boolean isDriverLoaded() {
        return feature.isDriverLoaded();
    }

    @Override
    public void setError(String error) {
        feature.setError(error);
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
    public ListenerRegistration addObjectListener(com.intuso.housemate.client.v1_0.api.object.Feature.Listener<? super RealFeatureBridgeReverse> listener) {
        return null; //todo
    }

    @Override
    public com.intuso.housemate.client.v1_0.real.api.object.RealValue<String, ?, ?> getErrorValue() {
        return valueMapper.map(feature.getErrorValue());
    }

    @Override
    public com.intuso.housemate.client.v1_0.real.api.object.RealCommand getRemoveCommand() {
        return commandMapper.map(feature.getRemoveCommand());
    }

    @Override
    public com.intuso.housemate.client.v1_0.real.api.object.RealCommand getRenameCommand() {
        return commandMapper.map(feature.getRenameCommand());
    }

    @Override
    public com.intuso.housemate.client.v1_0.real.api.object.RealCommand getStartCommand() {
        return commandMapper.map(feature.getStopCommand());
    }

    @Override
    public com.intuso.housemate.client.v1_0.real.api.object.RealCommand getStopCommand() {
        return commandMapper.map(feature.getStopCommand());
    }

    @Override
    public com.intuso.housemate.client.v1_0.real.api.object.RealValue<Boolean, ?, ?> getRunningValue() {
        return valueMapper.map(feature.getRunningValue());
    }

    @Override
    public com.intuso.housemate.client.v1_0.real.api.object.RealProperty<com.intuso.housemate.client.v1_0.real.api.driver.PluginDependency<com.intuso.housemate.client.v1_0.real.api.driver.FeatureDriver.Factory<?>>, ?, ?, ?> getDriverProperty() {
        Function<FeatureDriver.Factory<?>, com.intuso.housemate.client.v1_0.real.api.driver.FeatureDriver.Factory<?>> ddfConvertFrom
                = featureDriverFactoryMapper.getToV1_0Function();
        Function<PluginDependency<FeatureDriver.Factory<?>>, com.intuso.housemate.client.v1_0.real.api.driver.PluginDependency<com.intuso.housemate.client.v1_0.real.api.driver.FeatureDriver.Factory<?>>> convertFrom
                = pluginResourceMapper.getToV1_0Function(ddfConvertFrom);
        Function<com.intuso.housemate.client.v1_0.real.api.driver.FeatureDriver.Factory<?>, FeatureDriver.Factory<?>> ddfConvertTo
                = featureDriverFactoryMapper.getFromV1_0Function();
        Function<com.intuso.housemate.client.v1_0.real.api.driver.PluginDependency<com.intuso.housemate.client.v1_0.real.api.driver.FeatureDriver.Factory<?>>, PluginDependency<FeatureDriver.Factory<?>>> convertTo
                = pluginResourceMapper.getFromV1_0Function(ddfConvertTo);
        return propertyMapper.map(feature.getDriverProperty(),
                convertFrom,
                convertTo);
    }

    @Override
    public com.intuso.housemate.client.v1_0.real.api.object.RealValue<Boolean, ?, ?> getDriverLoadedValue() {
        return valueMapper.map(feature.getDriverLoadedValue());
    }

    @Override
    public com.intuso.housemate.client.v1_0.real.api.object.RealList<? extends com.intuso.housemate.client.v1_0.real.api.object.RealCommand<?, ?, ?>, ?> getCommands() {
        return listMapper.map((RealList<RealCommand<?, ?, ?>, ?>) feature.getCommands(),
                commandMapper.getToV1_0Function(),
                commandMapper.getFromV1_0Function());
    }

    @Override
    public com.intuso.housemate.client.v1_0.real.api.object.RealList<? extends com.intuso.housemate.client.v1_0.real.api.object.RealValue<?, ?, ?>, ?> getValues() {
        return listMapper.map((RealList<RealValue<?, ?, ?>, ?>) feature.getValues(),
                valueMapper.getToV1_0Function(),
                valueMapper.getFromV1_0Function());
    }

    @Override
    public com.intuso.housemate.client.v1_0.real.api.object.RealList<? extends com.intuso.housemate.client.v1_0.real.api.object.RealProperty<?, ?, ?, ?>, ?> getProperties() {
        return listMapper.map((RealList<RealProperty<?, ?, ?, ?>, ?>) feature.getProperties(),
                propertyMapper.getToV1_0Function(),
                propertyMapper.getFromV1_0Function());
    }

    public interface Factory {
        RealFeatureBridgeReverse create(RealFeature<?, ?, ?, ?, ?, ?, ?, ?> feature);
    }
}
