package com.intuso.housemate.web.client.place;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.gwt.place.shared.Place;

import javax.annotation.Nullable;
import java.util.*;

/**
 */
public abstract class HousematePlace extends Place {

    public final static String FIELD_SEPARATOR = "&";
    public final static String FIELD_VALUE_SEPARATOR = "=";
    public final static String NAMES_SEPARATOR = ",";

    protected static interface TokenisableField {
        String getFieldName();
    }

    protected static String namesToString(Collection<String> names) {
        return Joiner.on(NAMES_SEPARATOR).join(names);
    }

    protected static List<String> stringToNames(String names) {
        return Arrays.asList(names.split(NAMES_SEPARATOR));
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
