package com.intuso.housemate.platform.android.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.google.common.collect.Lists;
import com.intuso.utilities.properties.api.Reader;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 03/01/14
 * Time: 15:36
 * To change this template use File | Settings | File Templates.
 */
public class SharedPreferencesReader extends Reader {

    private final List<Entry> entries = Lists.newArrayList();

    public SharedPreferencesReader(String sourceName, int priority, Context context) {
        super(sourceName, priority);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        for(Map.Entry<String, ?> entry : preferences.getAll().entrySet())
            entries.add(new Entry(entry.getKey(), entry.getValue().toString()));
    }

    @Override
    public Iterator<Entry> iterator() {
        return entries.iterator();
    }
}
