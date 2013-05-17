package com.intuso.housemate.real.impl.type;

import com.intuso.housemate.real.RealResources;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 29/05/12
 * Time: 21:27
 * To change this template use File | Settings | File Templates.
 */
public class DaysType extends EnumMultiChoiceType<Day> {

    public final static String ID = "days";
    public final static String NAME = "Days";

    public DaysType(RealResources resources) {
        super(resources, ID, NAME, "Selection of days", Day.class, Day.values());
    }
}
