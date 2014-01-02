package com.intuso.housemate.server.plugin.main.condition;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.type.TypeData;
import com.intuso.housemate.object.server.real.ServerRealConditionOwner;
import com.intuso.housemate.object.server.real.ServerRealResources;
import com.intuso.housemate.server.plugin.main.type.comparison.ComparisonType;
import com.intuso.housemate.object.real.RealList;
import com.intuso.housemate.object.real.RealType;
import com.intuso.housemate.plugin.api.ServerConditionFactory;

/**
 */
@Singleton
public class ValueComparisonFactory implements ServerConditionFactory<ValueComparison> {

    private final ComparisonType comparisonType;
    private final RealList<TypeData<?>, RealType<?, ?, ?>> types;

    @Inject
    public ValueComparisonFactory(ComparisonType comparisonType, RealList<TypeData<?>, RealType<?, ?, ?>> types) {
        this.comparisonType = comparisonType;
        this.types = types;
    }

    @Override
    public String getTypeId() {
        return "value-comparison";
    }

    @Override
    public String getTypeName() {
        return "Value Comparison";
    }

    @Override
    public String getTypeDescription() {
        return "Compare a value";
    }

    @Override
    public ValueComparison create(ServerRealResources resources, String id, String name, String description,
                                  ServerRealConditionOwner owner) throws HousemateException {
        return new ValueComparison(resources, id, name, description, owner, types, comparisonType);
    }
}
