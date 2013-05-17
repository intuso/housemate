package com.intuso.housemate.web.client.place;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.gwt.place.shared.Place;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 24/11/12
 * Time: 19:25
 * To change this template use File | Settings | File Templates.
 */
public abstract class HousematePlace extends Place {

    public final static String FIELD_SEPARATOR = "&";
    public final static String FIELD_VALUE_SEPARATOR = "=";
    public final static String NAMES_SEPARATOR = ",";

    protected static interface TokenisableField {
        String getFieldName();
    }

    protected static String namesToString(List<String> names) {
        return Joiner.on(NAMES_SEPARATOR).join(names);
    }

    protected static List<String> stringToNames(String names) {
        return Arrays.asList(names.split(NAMES_SEPARATOR));
    }

    protected final List<Breadcrumb> breadcrumbList = new ArrayList<Breadcrumb>();

    public final List<Breadcrumb> getBreadcrumbItems() {
        return breadcrumbList;
    }

    protected static Map<String, String> getFields(String token) {
        String[] fields = token.split(FIELD_SEPARATOR);
        Map<String, String> result = new HashMap<String, String>();
        for(String field : fields) {
            if(field.length() == 0)
                continue;
            int index = field.indexOf(FIELD_VALUE_SEPARATOR);
            if(index > 0) // normal field
                result.put(field.substring(0, index), field.substring(index + 1));
            else if(index < 0) // no "=" means we only have a field name
                result.put(field, null);
            // if == 0 means "=" was at the start so we have no field name, do nothing
        }
        return result;
    }

    protected static String getToken(Map<TokenisableField, String> fields) {
        if(fields == null)
            return "";
        return Joiner.on(FIELD_SEPARATOR).join(Iterables.transform(fields.entrySet(), new Function<Map.Entry<TokenisableField, String>, String>() {
            @Override
            public String apply(@Nullable Map.Entry<TokenisableField, String> entry) {
                if(entry.getValue() != null)
                    return entry.getKey().getFieldName() + FIELD_VALUE_SEPARATOR + entry.getValue();
                else
                    return "";
            }
        }));
    }
}
