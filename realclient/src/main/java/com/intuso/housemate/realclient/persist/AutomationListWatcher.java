package com.intuso.housemate.realclient.persist;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.Inject;
import com.intuso.housemate.api.HousemateException;
import com.intuso.housemate.api.object.list.ListListener;
import com.intuso.housemate.api.object.type.TypeInstanceMap;
import com.intuso.housemate.api.object.type.TypeInstances;
import com.intuso.housemate.object.real.RealAutomation;
import com.intuso.housemate.object.real.impl.type.BooleanType;
import com.intuso.housemate.persistence.api.DetailsNotFoundException;
import com.intuso.housemate.persistence.api.Persistence;
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
public class AutomationListWatcher implements ListListener<RealAutomation> {

    private final Multimap<RealAutomation, ListenerRegistration> listeners = HashMultimap.create();

    private final Log log;
    private final Persistence persistence;
    private final ValueWatcher valueWatcher;
    private final ConditionListWatcher conditionListWatcher;
    private final TaskListWatcher taskListWatcher;
    private final AutomationListener automationListener;

    @Inject
    public AutomationListWatcher(Log log, Persistence persistence, ValueWatcher valueWatcher,
                                 ConditionListWatcher conditionListWatcher, TaskListWatcher taskListWatcher, AutomationListener automationListener) {
        this.log = log;
        this.persistence = persistence;
        this.valueWatcher = valueWatcher;
        this.conditionListWatcher = conditionListWatcher;
        this.taskListWatcher = taskListWatcher;
        this.automationListener = automationListener;
    }

    @Override
    public void elementAdded(RealAutomation automation) {
        listeners.put(automation, automation.addObjectListener(automationListener));
        listeners.put(automation, automation.getRunningValue().addObjectListener(valueWatcher));
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
        } catch(HousemateException e) {
            log.e("Failed to check value for whether the automation was previously running", e);
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
        } catch(HousemateException e) {
            log.e("Failed to delete automation properties", e);
        }
    }
}
