package com.intuso.housemate.broker.storage;

import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.type.TypeValues;

import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 23/01/13
 * Time: 09:08
 * To change this template use File | Settings | File Templates.
 */
public interface Storage {
    public String getValue(String[] path) throws DetailsNotFoundException, HousemateException;
    public void saveValue(String[] path, String value) throws HousemateException;
    public Set<String> getValuesKeys(String[] path) throws DetailsNotFoundException, HousemateException;
    public TypeValues getValues(String[] path, String detailsKey) throws DetailsNotFoundException, HousemateException;
    public void saveValues(String[] path, String detailsKey, TypeValues details) throws HousemateException;
    public void removeValues(String[] path) throws HousemateException;
}
