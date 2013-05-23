package com.intuso.housemate.broker.storage;

import com.intuso.housemate.api.HousemateException;

import java.util.Map;
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
    public Set<String> getDetailsKeys(String[] path) throws DetailsNotFoundException, HousemateException;
    public Map<String, String> getDetails(String[] path, String detailsKey) throws DetailsNotFoundException, HousemateException;
    public void saveDetails(String[] path, String detailsKey, Map<String, String> details) throws HousemateException;
    public void removeDetails(String[] path) throws HousemateException;
}
