package com.intuso.housemate.api.object.automation;

import com.intuso.housemate.api.object.HousemateData;

/**
 * Data object for an automation
 */
public final class AutomationData extends HousemateData<HousemateData<?>> {

    private static final long serialVersionUID = -1L;

    private AutomationData() {}

    public AutomationData(String id, String name, String description) {
        super(id, name,  description);
    }

    @Override
    public HousemateData clone() {
        return new AutomationData(getId(), getName(), getDescription());
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof AutomationData))
            return false;
        AutomationData other = (AutomationData)o;
        if(!other.getChildData(Automation.CONDITIONS_ID).equals(getChildData(Automation.CONDITIONS_ID)))
            return false;
        if(!other.getChildData(Automation.SATISFIED_TASKS_ID).equals(getChildData(Automation.SATISFIED_TASKS_ID)))
            return false;
        if(!other.getChildData(Automation.UNSATISFIED_TASKS_ID).equals(getChildData(Automation.UNSATISFIED_TASKS_ID)))
            return false;
        return true;
    }
}
