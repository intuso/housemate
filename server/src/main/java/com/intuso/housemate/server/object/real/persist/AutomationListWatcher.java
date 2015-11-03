package com.intuso.housemate.server.object.real.persist;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.Inject;
import com.intuso.housemate.client.real.api.internal.RealAutomation;
import com.intuso.housemate.client.real.impl.internal.type.BooleanType;
import com.intuso.housemate.object.api.internal.List;
import com.intuso.housemate.object.api.internal.TypeInstance;
import com.intuso.housemate.object.api.internal.TypeInstanceMap;
import com.intuso.housemate.object.api.internal.TypeInstances;
import com.intuso.housemate.persistence.api.internal.DetailsNotFoundException;
import com.intuso.housemate.persistence.api.internal.Persistence;
import com.intuso.utilities.listener.ListenerRegistration;
import com.intuso.utilities.log.Log;

import java.util.Arrays;
import java.util.Collection;

/**
* Created with IntelliJ IDEA.
* User: tomc
* Date: 14/02/14
* Time: 19:24
* To change this template use File | Settings | File Templates.
*/
public class AutomationListWatcher implements List.Listener<RealAutomation> {

    private final Multimap<RealAutomation, ListenerRegistration> listeners = HashMultimap.create();

    private final Log log;
    private final Persistence persistence;
    private final ValueBaseWatcher valueBaseWatcher;
    private final ConditionListWatcher conditionListWatcher;
    private final TaskListWatcher taskListWatcher;
    private final AutomationListener automationListener;

    @Inject
    public AutomationListWatcher(Log log, Persistence persistence, ValueBaseWatcher valueBaseWatcher,
                                 ConditionListWatcher conditionListWatcher, TaskListWatcher taskListWatcher, AutomationListener automationListener) {
        this.log = log;
        this.persistence = persistence;
        this.valueBaseWatcher = valueBaseWatcher;
        this.conditionListWatcher = conditionListWatcher;
        this.taskListWatcher = taskListWatcher;
        this.automationListener = automationListener;
    }

    @Override
    public void elementAdded(RealAutomation automation) {

        TypeInstanceMap toSave = new TypeInstanceMap();
        toSave.getChildren().put("id", new TypeInstances(new TypeInstance(automation.getId())));
        toSave.getChildren().put("name", new TypeInstances(new TypeInstance(automation.getName())));
        toSave.getChildren().put("description", new TypeInstances(new TypeInstance(automation.getDescription())));
        try {
            persistence.saveValues(automation.getPath(), toSave);
        } catch (Throwable t) {
            log.e("Failed to save new automation values", t);
        }

        listeners.put(automation, automation.addObjectListener(automationListener));
        listeners.put(automation, valueBaseWatcher.watch(automation.getRunningValue()));
        listeners.put(automation, automation.getConditions().addObjectListener(conditionListWatcher, true));
        listeners.put(automation, automation.getSatisfiedTasks().addObjectListener(taskListWatcher, true));
        listeners.put(automation, automation.getUnsatisfiedTasks().addObjectListener(taskListWatcher, true));
        try {
            TypeInstances instances = persistence.getTypeInstances(automation.getRunningValue().getPath());
            if(instances.getElements().size() > 0 && BooleanType.SERIALISER.deserialise(instances.getElements().get(0)))
                automation.getStartCommand().perform(new TypeInstanceMap(),
                        new CommandPerformListener(log, "Start automation \"" + automation.getId() + "\""));
        } catch(DetailsNotFoundException e) {
            log.w("No details found for whether the automation was previously running" + Arrays.toString(automation.getPath()));
        } catch(Throwable t) {
            log.e("Failed to check value for whether the automation was previously running", t);
        }
    }

    @Override
    public void elementRemoved(RealAutomation automation) {
        Collection<ListenerRegistration> registrations = listeners.removeAll(automation);
        if(registrations != null)
            for(ListenerRegistration registration : registrations)
                registration.removeListener();
        try {
            persistence.removeValues(automation.getPath());
        } catch(Throwable t) {
            log.e("Failed to delete automation properties", t);
        }
    }
}
