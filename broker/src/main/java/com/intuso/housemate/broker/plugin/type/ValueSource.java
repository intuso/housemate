package com.intuso.housemate.broker.plugin.type;

import com.intuso.housemate.object.real.RealResources;
import com.intuso.housemate.object.real.impl.type.EnumSingleChoiceType;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 03/06/13
 * Time: 07:20
 * To change this template use File | Settings | File Templates.
 */
public class ValueSource extends EnumSingleChoiceType<ValueSource.SourceType> {

    public final static String ID = "value-source";
    public final static String NAME = "Value Source";
    public final static String DESCRIPTION = "The source for a value";

    public enum SourceType {
        Object,
        Constant
    }

    protected ValueSource(RealResources resources) {
        super(resources, ID, NAME, DESCRIPTION, SourceType.class, SourceType.values());
    }
}
