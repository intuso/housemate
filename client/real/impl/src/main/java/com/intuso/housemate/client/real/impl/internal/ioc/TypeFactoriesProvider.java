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
public class TypeFactoriesProvider implements Provider<Map<String, RealTypeImpl.Factory<?>>> {

    private final RealTypeImpl.Factory<BooleanType> booleanTypeFactory;
    private final RealTypeImpl.Factory<DaysType> daysTypeFactory;
    private final RealTypeImpl.Factory<DoubleType> doubleTypeFactory;
    private final RealTypeImpl.Factory<EmailType> emailTypeFactory;
    private final RealTypeImpl.Factory<IntegerType> integerTypeFactory;
    private final RealTypeImpl.Factory<StringType> stringTypeFactory;
    private final RealTypeImpl.Factory<TimeType> timeTypeFactory;
    private final RealTypeImpl.Factory<TimeUnitType> timeUnitTypeFactory;

    @Inject
    public TypeFactoriesProvider(RealTypeImpl.Factory<BooleanType> booleanTypeFactory, RealTypeImpl.Factory<DaysType> daysTypeFactory, RealTypeImpl.Factory<DoubleType> doubleTypeFactory, RealTypeImpl.Factory<EmailType> emailTypeFactory, RealTypeImpl.Factory<IntegerType> integerTypeFactory, RealTypeImpl.Factory<StringType> stringTypeFactory, RealTypeImpl.Factory<TimeType> timeTypeFactory, RealTypeImpl.Factory<TimeUnitType> timeUnitTypeFactory) {
        this.booleanTypeFactory = booleanTypeFactory;
        this.daysTypeFactory = daysTypeFactory;
        this.doubleTypeFactory = doubleTypeFactory;
        this.emailTypeFactory = emailTypeFactory;
        this.integerTypeFactory = integerTypeFactory;
        this.stringTypeFactory = stringTypeFactory;
        this.timeTypeFactory = timeTypeFactory;
        this.timeUnitTypeFactory = timeUnitTypeFactory;
    }

    @Override
    public Map<String, RealTypeImpl.Factory<?>> get() {
        Map<String, RealTypeImpl.Factory<? extends RealTypeImpl<?>>> typeFactories = Maps.newHashMap();
        typeFactories.put(Boolean.class.getName(), booleanTypeFactory);
        typeFactories.put(Day.class.getName(), daysTypeFactory);
        typeFactories.put(Double.class.getName(), doubleTypeFactory);
        typeFactories.put(Email.class.getName(), emailTypeFactory);
        typeFactories.put(Integer.class.getName(), integerTypeFactory);
        typeFactories.put(String.class.getName(), stringTypeFactory);
        typeFactories.put(Time.class.getName(), timeTypeFactory);
        typeFactories.put(TimeUnit.class.getName(), timeUnitTypeFactory);
        return typeFactories;
    }
}
