package com.intuso.housemate.broker.storage.impl;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.broker.storage.DetailsNotFoundException;
import com.intuso.housemate.broker.storage.Storage;

import javax.annotation.Nullable;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: ravnroot
 * Date: 23/01/13
 * Time: 09:10
 * To change this template use File | Settings | File Templates.
 */
public class SjoerdDB implements Storage {

    public final static String PROPERTY_VALUE_KEY = "value";
    public final static String PROPERTIES_EXTENSION = ".properties";
    public final static String VALUE_FILENAME = "value";

    private final Joiner JOINER = Joiner.on("/");
    private final Splitter SPLITTER = Splitter.on("/");

    private final Joiner pathJoiner = Joiner.on(File.separator);
    private final String basePath;

    public SjoerdDB(String basePath) throws HousemateException {
        File baseDir = new File(basePath);
        if(baseDir.exists()) {
            if(!baseDir.isDirectory())
                throw new HousemateException("File storage base directory is not a directory");
        } else {
            if(!baseDir.mkdirs())
                throw new HousemateException("File storage base directory did not exist and could not be created");
        }
        this.basePath = basePath.endsWith(File.separator) ? basePath : basePath + File.separator;
    }

    @Override
    public TypeInstance getValue(String[] path) throws DetailsNotFoundException, HousemateException {
        TypeInstances details = null;
        try {
            details = getDetails(getFile(false, path, VALUE_FILENAME));
        }  catch(FileNotFoundException e) {
            throw new DetailsNotFoundException(e);
        } catch(IOException e) {
            throw new HousemateException("Failed to get value", e);
        }
        TypeInstance value = details.get(PROPERTY_VALUE_KEY);
        return value;
    }

    @Override
    public void saveValue(String[] path, TypeInstance value) throws HousemateException {
        TypeInstances details = new TypeInstances();
        details.put(PROPERTY_VALUE_KEY, value);
        try {
            saveDetails(getFile(true, path, VALUE_FILENAME), details);
        } catch(IOException e) {
            throw new HousemateException("Could not save value", e);
        }
    }

    @Override
    public Set<String> getValuesKeys(String[] path) throws DetailsNotFoundException {
        File file = getFile(false, path);
        if(!file.exists())
            throw new DetailsNotFoundException();
        if(file.isDirectory()) {
            return new HashSet<String>(Lists.transform(Arrays.asList(
                    file.listFiles(new FileFilter() {
                        @Override
                        public boolean accept(File file) {
                            return file.isFile() && file.getName().endsWith(PROPERTIES_EXTENSION);
                        }
                    })
            ), new Function<File, String>() {
                @Override
                public String apply(@Nullable File file) {
                    return file.getName().substring(0, file.getName().length() - PROPERTIES_EXTENSION.length());
                }
            }));
        } else
            return new HashSet<String>();
    }

    @Override
    public TypeInstances getValues(String[] path, String detailsKey) throws DetailsNotFoundException, HousemateException {
        try {
            return readDetailsFile(path, detailsKey);
        } catch(FileNotFoundException e) {
            throw new DetailsNotFoundException(e);
        } catch(IOException e) {
            throw new HousemateException("Failed to read details file", e);
        }
    }

    @Override
    public void saveValues(String[] path, String detailsKey, TypeInstances details) throws HousemateException {
        try {
            saveDetails(getFile(true, path, detailsKey + PROPERTIES_EXTENSION), details);
        } catch(IOException e) {
            throw new HousemateException("Could not save details", e);
        }
    }

    @Override
    public void removeValues(String[] path) throws HousemateException {
        deleteFile(getFile(false, path));
        String[] propertiesPath = new String[path.length];
        System.arraycopy(path, 0, propertiesPath, 0, path.length - 1);
        propertiesPath[path.length - 1] = path[path.length - 1] + PROPERTIES_EXTENSION;
        deleteFile(getFile(false, propertiesPath));
    }

    private void deleteFile(File file) {
        if(file == null)
            return;
        if(file.isDirectory())
            for(File child : file.listFiles())
                deleteFile(child);
        file.delete();
    }

    private TypeInstances readDetailsFile(String[] path, String detailsKey) throws IOException {
        return getDetails(getFile(false, path, detailsKey + PROPERTIES_EXTENSION));
    }

    private TypeInstances getDetails(File file) throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream(file));
        TypeInstances result = new TypeInstances();
        for(Map.Entry<Object, Object> entry : properties.entrySet())
            if(entry.getKey() instanceof String && entry.getValue() instanceof String)
                addValue(result, (String) entry.getKey(), (String) entry.getValue());
        return result;
    }

    private void addValue(TypeInstances typeInstances, String path, String value) {
        List<String> pathElements = Lists.newArrayList(SPLITTER.split(path));
        for(int i = 0; i < pathElements.size() - 1; i++) {
            if(!typeInstances.containsKey(pathElements.get(i)))
                typeInstances.put(pathElements.get(i), new TypeInstance());
            typeInstances = typeInstances.get(pathElements.get(i)).getChildValues();
        }
        if(!typeInstances.containsKey(pathElements.get(pathElements.size() - 1)))
            typeInstances.put(pathElements.get(pathElements.size() - 1), new TypeInstance());
        typeInstances.get(pathElements.get(pathElements.size() - 1)).setValue(value);
    }

    private void saveDetails(File file, TypeInstances details) throws IOException {
        Properties properties = new Properties();
        List<String> path = Lists.newArrayList();
        addValues(properties, path, details);
        properties.store(new FileOutputStream(file), "");
    }

    private void addValues(Properties properties, List<String> path, TypeInstances typeInstances) {
        for(Map.Entry<String, TypeInstance> entry : typeInstances.entrySet()) {
            path.add(entry.getKey());
            addValue(properties, path, entry.getValue());
            path.remove(path.size() - 1);
        }
    }

    private void addValue(Properties properties, List<String> path, TypeInstance typeInstance) {
        if(typeInstance != null) {
            if(typeInstance.getValue() != null)
                properties.put(JOINER.join(path), typeInstance.getValue());
            addValues(properties, path, typeInstance.getChildValues());
        }
    }

    private File getFile(boolean createParentFolder, String[] path, String ... extras) {
        File result = extras.length == 0
            ? new File(basePath + pathJoiner.join(path))
            : new File(basePath + pathJoiner.join(path) + File.separator + pathJoiner.join(extras));
        if(createParentFolder)
            result.getParentFile().mkdirs();
        return result;
    }
}
