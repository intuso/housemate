package com.intuso.housemate.object.api.internal;

/**
 * @param <COMMANDS> the type of the commands list
 * @param <VALUES> the type of the values list
 * @param <FEATURE> the type of the feature
 */
public interface Feature<
        COMMANDS extends List<? extends Command<?, ?, ?, ?>>,
        VALUES extends List<? extends Value<?, ?>>,
        FEATURE extends Feature<COMMANDS, VALUES, FEATURE>>
        extends
        BaseHousemateObject<Feature.Listener<? super FEATURE>>,
        Command.Container<COMMANDS>,
        Value.Container<VALUES> {

    /**
     *
     * Listener interface for features
     */
    interface Listener<FEATURE extends Feature<?, ?, ?>> extends ObjectListener {}

    /**
     *
     * Interface to show that the implementing object has a list of features
     */
    interface Container<FEATURES extends List<? extends Feature<?, ?, ?>>> {

        /**
         * Gets the features list
         * @return the features list
         */
        FEATURES getFeatures();
    }
}
