package com.intuso.housemate.api.object.type;

import com.intuso.housemate.api.object.HousemateObjectWrappable;
import com.intuso.housemate.api.object.NoChildrenWrappable;

/**
 * Created by IntelliJ IDEA.
 * User: tomc
 * Date: 29/05/12
 * Time: 21:05
 * To change this template use File | Settings | File Templates.
 */
public final class RegexTypeWrappable extends TypeWrappable<NoChildrenWrappable> {

    private String regexPattern;

    private RegexTypeWrappable() {}

    public RegexTypeWrappable(String id, String name, String description, String regexPattern) {
        super(id, name, description);
        this.regexPattern = regexPattern;
    }

    @Override
    public HousemateObjectWrappable clone() {
        return new RegexTypeWrappable(getId(), getName(), getDescription(), regexPattern);
    }

    public String getRegexPattern() {
        return regexPattern;
    }
}
