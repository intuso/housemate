package com.intuso.housemate.web.client.bootstrap.view;

import com.github.gwtbootstrap.client.ui.Button;
import com.github.gwtbootstrap.client.ui.constants.IconType;
import com.github.gwtbootstrap.client.ui.resources.ButtonSize;
import com.google.common.collect.Lists;
import com.google.gwt.user.client.ui.FlowPanel;
import com.intuso.housemate.web.client.Housemate;
import com.intuso.housemate.web.client.bootstrap.widget.automation.AutomationList;
import com.intuso.housemate.web.client.bootstrap.widget.command.PerformButton;
import com.intuso.housemate.web.client.place.AutomationsPlace;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: tomc
 * Date: 24/09/13
 * Time: 00:15
 * To change this template use File | Settings | File Templates.
 */
public class AutomationView extends FlowPanel implements com.intuso.housemate.web.client.ui.view.AutomationView {
    
    private final AutomationList favouritesList;
    private final AutomationList allList;
    
    public AutomationView() {
        
        List<String> favourites = Lists.newArrayList();
        favouritesList = new AutomationList("favourites", favourites, true);
        allList = new AutomationList("all", favourites, false);
        
        add(favouritesList);
        add(allList);
        Button addButton = new PerformButton(Housemate.ENVIRONMENT.getResources().getRoot().getAddAutomationCommand(), IconType.PLUS);
        addButton.setSize(ButtonSize.SMALL);
        add(addButton);
    }

    @Override
    public void newPlace(AutomationsPlace place) {
        favouritesList.setSelected(place.getAutomationNames());
        allList.setSelected(place.getAutomationNames());
    }
}
