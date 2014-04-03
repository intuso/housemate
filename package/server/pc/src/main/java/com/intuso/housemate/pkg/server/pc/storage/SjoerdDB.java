package com.intuso.housemate.pkg.server.pc.storage;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.type.TypeInstance;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.server.storage.DetailsNotFoundException;
import com.intuso.housemate.server.storage.Storage;
import com.intuso.utilities.properties.api.PropertyRepository;

import java.io.*;
import java.util.*;

public class SjoerdDB implements Storage {

    public final static String PATH_PROPERTY_KEY = "dbPath";

    public final static String PROPERTY_VALUE_KEY = "value";
    public final static String PROPERTIES_EXTENSION = ".properties";
    public final static String VALUE_FILENAME = "value";

    private final Joiner JOINER = Joiner.on("/");
    private final Splitter SPLITTER = Splitter.on("/");

    private final Joiner pathJoiner = Joiner.on(File.separator);
    private final String basePath;

    @Inject
    public SjoerdDB(PropertyRepository properties) throws HousemateException {
        String basePath = properties.get(PATH_PROPERTY_KEY);
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
    public TypeInstances getTypeInstances(String[] path) throws DetailsNotFoundException, HousemateException {
        TypeInstanceMap details = null;
        try {
            details = getDetails(getFile(false, path, VALUE_FILENAME));
        }  catch(FileNotFoundException e) {
            throw new DetailsNotFoundException(e);
        } catch(IOException e) {
            throw new HousemateException("Failed to get value", e);
        }
        TypeInstances instances = details.get(PROPERTY_VALUE_KEY);
        return instances;
    }

    @Override
    public void saveTypeInstances(String[] path, TypeInstances value) throws HousemateException {
        TypeInstanceMap details = new TypeInstanceMap();
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
                public String apply(File file) {
                    return file.getName().substring(0, file.getName().length() - PROPERTIES_EXTENSION.length());
                }
            }));
        } else
            return new HashSet<String>();
    }

    @Override
    public TypeInstanceMap getValues(String[] path, String detailsKey) throws DetailsNotFoundException, HousemateException {
        try {
            return readDetailsFile(path, detailsKey);
        } catch(FileNotFoundException e) {
            throw new DetailsNotFoundException(e);
        } catch(IOException e) {
            throw new HousemateException("Failed to read details file", e);
        }
    }

    @Override
    public void saveValues(String[] path, TypeInstanceMap details) throws HousemateException {
        try {
            String[] newPath = new String[path.length];
            System.arraycopy(path, 0, newPath, 0, path.length - 1);
            newPath[path.length - 1] = path[path.length - 1] + PROPERTIES_EXTENSION;
            saveDetails(getFile(true, newPath), details);
        } catch(IOException e) {
            throw new HousemateException("Could not save details", e);
        }
    }

    @Override
    public void saveValues(String[] path, String detailsKey, TypeInstanceMap details) throws HousemateException {
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

    private TypeInstanceMap readDetailsFile(String[] path, String detailsKey) throws IOException {
        return getDetails(getFile(false, path, detailsKey + PROPERTIES_EXTENSION));
    }

    private TypeInstanceMap getDetails(File file) throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream(file));
        TypeInstanceMap result = new TypeInstanceMap();
        for(Map.Entry<Object, Object> entry : properties.entrySet())
            if(entry.getKey() instanceof String && entry.getValue() instanceof String)
                addValue(result, (String) entry.getKey(), (String) entry.getValue());
        return result;
    }

    private void addValue(TypeInstanceMap typeInstanceMap, String path, String value) {
        List<String> pathElements = Lists.newArrayList(SPLITTER.split(path));
        for(int i = 0; i < pathElements.size() - 2; i += 2) {
            TypeInstances typeInstances = typeInstanceMap.get(pathElements.get(i));
            if(typeInstances == null) {
                typeInstances = new TypeInstances();
                typeInstanceMap.put(pathElements.get(i), typeInstances);
            }
            int index = Integer.parseInt(pathElements.get(i + 1));
            while(typeInstances.size() <= index)
                typeInstances.add(new TypeInstance());
            typeInstanceMap = typeInstances.get(index).getChildValues();
        }
        TypeInstances typeInstances = typeInstanceMap.get(pathElements.get(pathElements.size() - 2));
        if(typeInstances == null) {
            typeInstances = new TypeInstances();
            typeInstanceMap.put(pathElements.get(pathElements.size() - 2), typeInstances);
        }
        int index = Integer.parseInt(pathElements.get(pathElements.size() - 1));
        while(typeInstances.size() <= index)
            typeInstances.add(new TypeInstance());
        typeInstances.get(index).setValue(value);
    }

    private void saveDetails(File file, TypeInstanceMap details) throws IOException {
        Properties properties = new Properties();
        List<String> path = Lists.newArrayList();
        addValues(properties, path, details);
        properties.store(new FileOutputStream(file), "");
    }

    private void addValues(Properties properties, List<String> path, TypeInstanceMap typeInstances) {
        for(Map.Entry<String, TypeInstances> entry : typeInstances.entrySet()) {
            path.add(entry.getKey());
            addValue(properties, path, entry.getValue());
            path.remove(path.size() - 1);
        }
    }

    private void addValue(Properties properties, List<String> path, TypeInstances typeInstances) {
        if(typeInstances != null) {
            for(int i = 0; i < typeInstances.size(); i++) {
                path.add(Integer.toString(i));
                TypeInstance typeInstance = typeInstances.get(i);
                if(typeInstance != null) {
                    if(typeInstance.getValue() != null)
                        properties.put(JOINER.join(path), typeInstance.getValue());
                    addValues(properties, path, typeInstance.getChildValues());
                }
                path.remove(path.size() - 1);
            }
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
