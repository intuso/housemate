package com.intuso.housemate.api.object.rule;

import com.intuso.housemate.api.object.primary.PrimaryListener;

/**
 * Interface for classes wishing to listen to a condition
 * @author tclabon
 *
 */
public interface RuleListener<R extends Rule<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>>
        extends PrimaryListener<R> {
    public void satisfied(R rule, boolean satisfied);
}