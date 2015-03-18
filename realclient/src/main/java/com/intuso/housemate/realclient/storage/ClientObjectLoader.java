package com.intuso.housemate.realclient.storage;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.command.Command;
import com.intuso.housemate.api.object.root.Root;
import com.intuso.housemate.persistence.api.DetailsNotFoundException;
import com.intuso.housemate.persistence.api.Persistence;
import com.intuso.housemate.realclient.object.RealClientRoot;
import com.intuso.utilities.log.Log;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 15/11/13
 * Time: 09:05
 * To change this template use File | Settings | File Templates.
 */
public class ClientObjectLoader {

    private final Log log;
    private final Persistence persistence;
    private final RealClientRoot root;

    @Inject
    public ClientObjectLoader(Log log, Persistence persistence, RealClientRoot root) {
        this.log = log;
        this.persistence = persistence;
        this.root = root;
    }

    public void loadObjects() {
        loadHardwares(Lists.newArrayList("", Root.HARDWARES_ID), root.getAddHardwareCommand());
        loadDevices(Lists.newArrayList("", Root.DEVICES_ID), root.getAddDeviceCommand());
    }

    private void loadHardwares(List<String> path, Command<?, ?, ?> addHardwareCommand) {
        try {
            for(String key : persistence.getValuesKeys(path.toArray(new String[path.size()]))) {
                try {
                    path.add(key);
                    addHardwareCommand.perform(persistence.getValues(path.toArray(new String[path.size()])),
                            new CommandPerformListener("Load hardware \"" + key + "\""));
                    path.remove(path.size() - 1);
                } catch(HousemateException e) {
                    log.e("Failed to load hardware", e);
                }
            }
        } catch(DetailsNotFoundException e) {
            log.w("No details found for saved hardwares at " + Joiner.on("/").join(path));
        } catch(HousemateException e) {
            log.e("Failed to get names of existing hardwares", e);
        }
    }

    private void loadDevices(List<String> path, Command<?, ?, ?> addDeviceCommand) {
        try {
            for(String key : persistence.getValuesKeys(path.toArray(new String[path.size()]))) {
                try {
                    path.add(key);
                    addDeviceCommand.perform(persistence.getValues(path.toArray(new String[path.size()])),
                            new CommandPerformListener("Load device \"" + key + "\""));
                    path.remove(path.size() - 1);
                } catch(HousemateException e) {
                    log.e("Failed to load device", e);
                }
            }
        } catch(DetailsNotFoundException e) {
            log.w("No details found for saved devices at " + Joiner.on("/").join(path));
        } catch(HousemateException e) {
            log.e("Failed to get names of existing devices", e);
        }
    }

    private class CommandPerformListener implements com.intuso.housemate.api.object.command.CommandPerformListener<Command<?, ?, ?>> {

        private final String description;

        private CommandPerformListener(String description) {
            this.description = description;
        }

        @Override
        public void commandStarted(Command<?, ?, ?> command) {
            log.d("Doing " + description);
        }

        @Override
        public void commandFinished(Command<?, ?, ?> command) {
            log.d("Done " + description);
        }

        @Override
        public void commandFailed(Command<?, ?, ?> command, String error) {
            log.d(description + " failed: " + error);
        }
    }
}
