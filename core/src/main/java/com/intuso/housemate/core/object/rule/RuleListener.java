package com.intuso.housemate.core.object.rule;

import com.intuso.housemate.core.object.primary.PrimaryListener;

/**
 * Interface for classes wishing to listen to a condition
 * @author tclabon
 *
 */
public interface RuleListener<R extends Rule<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?>>
        extends PrimaryListener<R> {
    public void satisfied(R rule, boolean satisfied);
}