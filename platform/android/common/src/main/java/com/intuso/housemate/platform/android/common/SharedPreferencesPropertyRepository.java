package com.intuso.housemate.platform.android.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.google.inject.Inject;
import com.intuso.utilities.collection.ManagedCollectionFactory;
import com.intuso.utilities.properties.api.PropertyRepository;

import java.util.Set;

public class SharedPreferencesPropertyRepository
        extends PropertyRepository
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private final SharedPreferences sharedPreferences;

    @Inject
    public SharedPreferencesPropertyRepository(ManagedCollectionFactory managedCollectionFactory, Context context) {
        this(managedCollectionFactory, null, context);
    }

    public SharedPreferencesPropertyRepository(ManagedCollectionFactory managedCollectionFactory, PropertyRepository parent, Context context) {
        super(managedCollectionFactory, parent);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void _set(String key, String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }

    @Override
    protected void _remove(String key) {
        sharedPreferences.edit().remove(key).apply();
    }

    @Override
    protected Set<String> _keySet() {
        return sharedPreferences.getAll().keySet();
    }

    @Override
    protected String _get(String key) {
        return sharedPreferences.getString(key, null);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        notifyListeners(key, null, get(key));
    }
}
