package com.intuso.housemate.client.real.impl.internal.ioc;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.intuso.housemate.client.real.api.internal.type.Day;
import com.intuso.housemate.client.real.api.internal.type.Email;
import com.intuso.housemate.client.real.api.internal.type.Time;
import com.intuso.housemate.client.real.api.internal.type.TimeUnit;
import com.intuso.housemate.client.real.impl.internal.RealTypeImpl;
import com.intuso.housemate.client.real.impl.internal.type.*;

import java.util.Map;

/**
 * Created by tomc on 05/05/16.
 */
public class TypesProvider implements Provider<Map<String, RealTypeImpl<?>>> {

    private final BooleanType booleanType;
    private final DaysType daysType;
    private final DoubleType doubleType;
    private final EmailType emailType;
    private final IntegerType integerType;
    private final StringType stringType;
    private final TimeType timeType;
    private final TimeUnitType timeUnitType;

    @Inject
    public TypesProvider(BooleanType booleanType, DaysType daysType, DoubleType doubleType, EmailType emailType, IntegerType integerType, StringType stringType, TimeType timeType, TimeUnitType timeUnitType) {
        this.booleanType = booleanType;
        this.daysType = daysType;
        this.doubleType = doubleType;
        this.emailType = emailType;
        this.integerType = integerType;
        this.stringType = stringType;
        this.timeType = timeType;
        this.timeUnitType = timeUnitType;
    }

    @Override
    public Map<String, RealTypeImpl<?>> get() {
        Map<String, RealTypeImpl<?>> typeFactories = Maps.newHashMap();
        typeFactories.put(Boolean.class.getName(), booleanType);
        typeFactories.put(Day.class.getName(), daysType);
        typeFactories.put(Double.class.getName(), doubleType);
        typeFactories.put(Email.class.getName(), emailType);
        typeFactories.put(Integer.class.getName(), integerType);
        typeFactories.put(String.class.getName(), stringType);
        typeFactories.put(Time.class.getName(), timeType);
        typeFactories.put(TimeUnit.class.getName(), timeUnitType);
        return typeFactories;
    }
}
