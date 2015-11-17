package com.intuso.housemate.plugin.api.internal;

import com.intuso.housemate.client.real.api.internal.annotations.TypeInfo;

public class CommonComparators {

    private CommonComparators() {}

    @TypeInfo(id = "equal", name = "Equal", description = "True if two values are equal")
    public interface Equal<O> extends Comparator<O> {}

    @TypeInfo(id = "greater-than", name = "Greater than", description = "True if the first value is greater than the second")
    public interface GreaterThan<O> extends Comparator<O> {}

    @TypeInfo(id = "greater-than-or-equal", name = "Greater than or Equal to", description = "True if the first value is greater than or equal to the second")
    public interface GreaterThanOrEqual<O> extends Comparator<O> {}

    @TypeInfo(id = "less-than", name = "Less Than", description = "True if the first value is less than the second")
    public interface LessThan<O> extends Comparator<O> {}

    @TypeInfo(id = "less-than-or-equal", name = "Less Than or Equal to", description = "True if the first value is less than or equal to the second")
    public interface LessThanOrEqual<O> extends Comparator<O> {}
}
